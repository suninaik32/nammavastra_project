package com.nammavastra.app.ui.calculator

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.nammavastra.app.R

class PriceCalculatorFragment : Fragment() {

    private val viewModel: CalculatorViewModel by viewModels()

    // Views that need animation
    private lateinit var tvResultCost: TextView
    private lateinit var tvResultProfit: TextView
    private lateinit var tvResultTotal: TextView

    // Keep track of current displayed values for smooth animation
    private var currentCost = 0
    private var currentProfit = 0
    private var currentTotal = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_price_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvResultCost = view.findViewById(R.id.tv_result_cost)
        tvResultProfit = view.findViewById(R.id.tv_result_profit)
        tvResultTotal = view.findViewById(R.id.tv_result_total)

        setupDropdown(view)
        setupInputs(view)
        setupSaveButton(view)
        observeViewModel(view)
    }

    private fun setupDropdown(view: View) {
        val categories = arrayOf("Silk", "Cotton", "Chanderi", "Banarasi", "Kanjivaram", "Linen")
        val actv = view.findViewById<AutoCompleteTextView>(R.id.actv_category)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        actv.setAdapter(adapter)

        actv.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedCategory.value = categories[position]
        }
    }

    private fun setupInputs(view: View) {
        val etCost = view.findViewById<TextInputEditText>(R.id.et_cost)
        val slider = view.findViewById<Slider>(R.id.slider_profit)
        val tvMarginVal = view.findViewById<TextView>(R.id.tv_margin_value)

        etCost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cost = s.toString().toDoubleOrNull() ?: 0.0
                viewModel.materialCostInput.value = cost
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        slider.addOnChangeListener { _, value, _ ->
            val margin = value.toInt()
            tvMarginVal.text = "$margin%"
            viewModel.profitMarginInput.value = margin
            
            // Premium Card Flip micro-interaction
            val card = view.findViewById<View>(R.id.card_result)
            android.animation.ObjectAnimator.ofFloat(card, "rotationX", 0f, 10f, 0f).apply {
                duration = 300
                start()
            }
        }
    }

    private fun setupSaveButton(view: View) {
        val btnSave = view.findViewById<Button>(R.id.btn_save_calc)
        btnSave.setOnClickListener {
            viewModel.saveCalculation()
        }
    }

    private fun observeViewModel(view: View) {
        val tvComparison = view.findViewById<TextView>(R.id.tv_market_comparison)

        viewModel.calculationResult.observe(viewLifecycleOwner) { result ->
            animateValue(currentCost, result.materialCost.toInt(), tvResultCost, "₹")
            animateValue(currentProfit, result.profitAmount.toInt(), tvResultProfit, "+ ₹")
            animateValue(currentTotal, result.suggestedPrice.toInt(), tvResultTotal, "₹")

            currentCost = result.materialCost.toInt()
            currentProfit = result.profitAmount.toInt()
            currentTotal = result.suggestedPrice.toInt()

            tvComparison.text = result.marketComparison
            if (result.isAboveMarket) {
                tvComparison.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
            } else {
                tvComparison.setTextColor(requireContext().getColor(android.R.color.holo_green_dark))
            }
        }

        viewModel.saveState.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == true) {
                Toast.makeText(requireContext(), "Calculation Saved!", Toast.LENGTH_SHORT).show()
                viewModel.resetSaveState()
            } else if (isSuccess == false) {
                Toast.makeText(requireContext(), "Error saving calculation", Toast.LENGTH_SHORT).show()
                viewModel.resetSaveState()
            }
        }
    }

    private fun animateValue(start: Int, end: Int, textView: TextView, prefix: String) {
        if (start == end) {
            textView.text = "$prefix $end"
            return
        }
        val animator = ValueAnimator.ofInt(start, end)
        animator.duration = 400 // Smooth fast animation
        animator.addUpdateListener { animation ->
            textView.text = "$prefix ${animation.animatedValue}"
        }
        animator.start()
    }
}
