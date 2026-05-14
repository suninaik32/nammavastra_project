package com.nammavastra.app.ui.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.nammavastra.app.R

import android.graphics.Color
import com.google.android.material.transition.MaterialContainerTransform

class UploadSareeFragment : Fragment() {

    private val viewModel: UploadViewModel by viewModels()

    // Modern Activity Result API for Image Picking
    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 400
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_saree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMaterialDropdown(view)
        setupImagePicker(view)
        setupUploadButton(view)
        observeViewModel(view)
    }

    private fun setupMaterialDropdown(view: View) {
        val materials = arrayOf("Silk", "Cotton", "Chanderi", "Banarasi", "Kanjivaram", "Linen")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, materials)
        view.findViewById<AutoCompleteTextView>(R.id.actv_material).setAdapter(adapter)
    }

    private fun setupImagePicker(view: View) {
        val cardImagePicker = view.findViewById<MaterialCardView>(R.id.card_image_picker)
        cardImagePicker.setOnClickListener {
            pickMedia.launch("image/*")
        }
    }

    private fun setupUploadButton(view: View) {
        val btnUpload = view.findViewById<Button>(R.id.btn_upload)
        btnUpload.setOnClickListener {
            val title = view.findViewById<TextInputEditText>(R.id.et_title).text.toString()
            val material = view.findViewById<AutoCompleteTextView>(R.id.actv_material).text.toString()
            val priceStr = view.findViewById<TextInputEditText>(R.id.et_price).text.toString()
            
            // Get selected category chip
            val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group_category)
            val selectedChipId = chipGroup.checkedChipId
            val category = if (selectedChipId != View.NO_ID) {
                view.findViewById<Chip>(selectedChipId).text.toString()
            } else ""

            viewModel.uploadSaree(title, material, category, priceStr)
        }
    }

    private fun observeViewModel(view: View) {
        val ivPreview = view.findViewById<ImageView>(R.id.iv_preview)
        val llPlaceholder = view.findViewById<LinearLayout>(R.id.ll_placeholder)
        val progressIndicator = view.findViewById<LinearProgressIndicator>(R.id.progress_indicator)
        val btnUpload = view.findViewById<Button>(R.id.btn_upload)

        viewModel.selectedImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                ivPreview.visibility = View.VISIBLE
                llPlaceholder.visibility = View.GONE
                ivPreview.load(uri) {
                    crossfade(true)
                }
            } else {
                ivPreview.visibility = View.GONE
                llPlaceholder.visibility = View.VISIBLE
            }
        }

        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UploadState.Idle -> {
                    progressIndicator.visibility = View.INVISIBLE
                    btnUpload.isEnabled = true
                    btnUpload.text = "Upload Saree"
                }
                is UploadState.Uploading -> {
                    progressIndicator.visibility = View.VISIBLE
                    if (progressIndicator.isIndeterminate) {
                        progressIndicator.isIndeterminate = false
                    }
                    progressIndicator.progress = state.progress.toInt()
                    btnUpload.isEnabled = false
                    btnUpload.text = "Uploading..."
                }
                is UploadState.Success -> {
                    progressIndicator.visibility = View.INVISIBLE
                    btnUpload.isEnabled = true
                    btnUpload.text = "Upload Saree"
                    
                    val bottomSheet = UploadSuccessBottomSheet {
                        // Reset form
                        viewModel.resetState()
                        view.findViewById<TextInputEditText>(R.id.et_title).text?.clear()
                        view.findViewById<TextInputEditText>(R.id.et_price).text?.clear()
                        view.findViewById<AutoCompleteTextView>(R.id.actv_material).text.clear()
                    }
                    bottomSheet.show(childFragmentManager, "UploadSuccess")
                }
                is UploadState.Error -> {
                    progressIndicator.visibility = View.INVISIBLE
                    btnUpload.isEnabled = true
                    btnUpload.text = "Upload Saree"
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
            }
        }
    }
}
