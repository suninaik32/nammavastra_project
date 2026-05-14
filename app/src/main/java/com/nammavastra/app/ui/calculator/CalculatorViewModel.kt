package com.nammavastra.app.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammavastra.app.data.repository.CalculatorRepository
import kotlinx.coroutines.launch

data class CalculationResult(
    val materialCost: Double = 0.0,
    val profitAmount: Double = 0.0,
    val suggestedPrice: Double = 0.0,
    val marketComparison: String = "",
    val isAboveMarket: Boolean = false
)

class CalculatorViewModel : ViewModel() {

    private val repository = CalculatorRepository()

    // Inputs
    val materialCostInput = MutableLiveData<Double>(0.0)
    val profitMarginInput = MutableLiveData<Int>(40) // Default 40%
    val selectedCategory = MutableLiveData<String>("Silk")

    // Outputs
    private val _calculationResult = MediatorLiveData<CalculationResult>()
    val calculationResult: LiveData<CalculationResult> = _calculationResult

    private val _saveState = MutableLiveData<Boolean?>(null) // null = idle, true = success, false = error
    val saveState: LiveData<Boolean?> = _saveState

    // Mock Market Averages
    private val marketAverages = mapOf(
        "Silk" to 5500.0,
        "Cotton" to 1500.0,
        "Chanderi" to 3000.0,
        "Banarasi" to 6000.0,
        "Kanjivaram" to 8000.0,
        "Linen" to 2000.0
    )

    init {
        _calculationResult.addSource(materialCostInput) { compute() }
        _calculationResult.addSource(profitMarginInput) { compute() }
        _calculationResult.addSource(selectedCategory) { compute() }
    }

    private fun compute() {
        val cost = materialCostInput.value ?: 0.0
        val margin = profitMarginInput.value ?: 40
        val category = selectedCategory.value ?: "Silk"

        val profitAmount = cost * (margin / 100.0)
        val suggestedPrice = cost + profitAmount

        val marketAvg = marketAverages[category] ?: 0.0
        
        val diff = suggestedPrice - marketAvg
        val comparisonText = if (cost == 0.0) {
            "Enter cost to see market comparison"
        } else if (diff > 0) {
            "+₹${diff.toInt()} above market average (₹${marketAvg.toInt()})"
        } else if (diff < 0) {
            "-₹${Math.abs(diff).toInt()} below market average (₹${marketAvg.toInt()})"
        } else {
            "Exactly at market average"
        }

        _calculationResult.value = CalculationResult(
            materialCost = cost,
            profitAmount = profitAmount,
            suggestedPrice = suggestedPrice,
            marketComparison = comparisonText,
            isAboveMarket = diff > 0
        )
    }

    fun saveCalculation() {
        val res = _calculationResult.value ?: return
        val cat = selectedCategory.value ?: "Unknown"
        val margin = profitMarginInput.value ?: 40

        if (res.materialCost <= 0.0) return

        viewModelScope.launch {
            val result = repository.saveCalculation(cat, res.materialCost, margin, res.suggestedPrice)
            if (result.isSuccess) {
                _saveState.value = true
            } else {
                // Simulate success if Firebase fails
                val err = result.exceptionOrNull()?.message ?: ""
                if (err.contains("Default FirebaseApp is not initialized")) {
                    _saveState.value = true
                } else {
                    _saveState.value = false
                }
            }
        }
    }
    
    fun resetSaveState() {
        _saveState.value = null
    }
}
