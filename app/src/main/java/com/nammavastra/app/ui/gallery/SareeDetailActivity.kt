package com.nammavastra.app.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.nammavastra.app.R
import com.nammavastra.app.data.model.Trend
import com.nammavastra.app.data.repository.GalleryRepository
import kotlinx.coroutines.launch

class SareeDetailActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FullscreenPagerAdapter
    private val repository = GalleryRepository()
    private var sarees: List<Trend> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saree_detail)

        val startPosition = intent.getIntExtra("START_POSITION", 0)
        val transitionName = intent.getStringExtra("TRANSITION_NAME")
        
        viewPager = findViewById(R.id.viewPager)
        if (transitionName != null) {
            viewPager.transitionName = transitionName
        }

        setupToolbar()
        loadDataAndSetupPager(startPosition)
        setupBottomSheet()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        toolbar.inflateMenu(R.menu.menu_saree_detail)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    Toast.makeText(this, "Edit coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_delete -> {
                    val currentSaree = sarees.getOrNull(viewPager.currentItem)
                    if (currentSaree != null) {
                        lifecycleScope.launch {
                            repository.deleteSarees(listOf(currentSaree.id))
                            Toast.makeText(this@SareeDetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
                            finish() // Close activity after delete
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun loadDataAndSetupPager(startPosition: Int) {
        viewPager = findViewById(R.id.viewPager)
        
        lifecycleScope.launch {
            // Fetching all again for demo purposes. In production, pass list or use shared ViewModel.
            sarees = repository.getUserSarees("user_123")
            adapter = FullscreenPagerAdapter(sarees)
            viewPager.adapter = adapter
            viewPager.setCurrentItem(startPosition, false)
            
            updateBottomSheetData(startPosition)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateBottomSheetData(position)
                }
            })
        }
    }

    private fun setupBottomSheet() {
        val bottomSheet = findViewById<View>(R.id.bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun updateBottomSheetData(position: Int) {
        val saree = sarees.getOrNull(position) ?: return

        findViewById<TextView>(R.id.tv_bs_title).text = saree.title
        findViewById<Chip>(R.id.chip_bs_category).text = saree.category

        findViewById<ImageButton>(R.id.btn_share).setOnClickListener {
            shareSaree(saree)
        }

        findViewById<Button>(R.id.btn_whatsapp_inquire).setOnClickListener {
            val message = "Hello! I am interested in purchasing the '${saree.title}' handloom saree from Namma-Vastra. What is the retail pricing?"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(message)}")
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareSaree(saree: Trend) {
        ShareCompat.IntentBuilder(this)
            .setType("text/plain")
            .setChooserTitle("Share Saree")
            .setText("Check out this beautiful ${saree.title} saree!\n\n${saree.imageUrl}")
            .startChooser()
    }
}
