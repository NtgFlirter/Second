package com.yashwant.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.calculator.CalculatorEngine
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.HistoryItem
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    // Naya Repository initialize
    private val repository = UserRepository(SettingsManager(application))
    var expression = mutableStateOf("")
        private set

    var result = mutableStateOf("0")
        private set

    // History state
    var history = mutableStateOf<List<HistoryItem>>(emptyList())
        private set

    var isFinalResult = mutableStateOf(false)
        private set


    init {
        viewModelScope.launch {
            // Firebase se purani history uthao
            val cloudHistory = repository.loadHistory()
            history.value = cloudHistory
        }
    }

    // =========================
    // INPUT HANDLER
    // =========================
    fun onButtonClick(value: String) {

        val operators = listOf("+", "-", "×", "÷")

        when (value) {

            // CLEAR
            "C" -> {
                expression.value = ""
                result.value = "0"
                isFinalResult.value = false
            }

            // BACKSPACE
            "⌫" -> {
                handleDelete()
            }

            // EQUALS
            "=" -> {
                if (expression.value.isBlank()) return

                val output = CalculatorEngine.calculate(expression.value)
                result.value = output

                val newItem = HistoryItem(
                    expression = expression.value,
                    result = output
                )

                // Duplicate check
                if (history.value.firstOrNull() != newItem) {
                    history.value = (listOf(newItem) + history.value).take(50)

                    // SAVE TO FIREBASE (Coroutine use kiya)
                    viewModelScope.launch {
                        repository.saveHistory(history.value)
                    }
                }

                expression.value = output
                isFinalResult.value = true
            }

            // NORMAL INPUT
            else -> {
                val lastChar = expression.value.lastOrNull()?.toString()
                val currentNumber = expression.value
                    .split("+", "-", "×", "÷")
                    .lastOrNull() ?: ""

                val digitCount = currentNumber.length

                // AFTER RESULT RESET
                if (isFinalResult.value) {
                    if (value.first().isDigit() || value == ".") {
                        expression.value = ""
                    }
                    isFinalResult.value = false
                }

                // DIGIT LIMIT
                if (value.first().isDigit() && digitCount >= 20) return

                // DECIMAL RULE
                if (value == "." && currentNumber.contains(".")) return

                // LEADING ZERO FIX
                if (value.first().isDigit()) {
                    if (currentNumber == "0") {
                        expression.value = expression.value.dropLast(1)
                    }
                    if (currentNumber == "0" && value == "0") return
                }

                // OPERATOR RULE
                if (value in operators) {
                    if (expression.value.isEmpty()) return
                    if (lastChar in operators) {
                        expression.value = expression.value.dropLast(1)
                    }
                }

                // % RULE
                if (value == "%") {
                    if (expression.value.isEmpty()) return
                    if (lastChar == "%") return
                }

                expression.value += value
                updateLiveResult()
            }
        }
    }

    fun restoreHistory(item: HistoryItem) {
        expression.value = item.expression
        result.value = item.result
        isFinalResult.value = false
    }

    private fun handleDelete() {
        if (expression.value.isNotEmpty()) {
            expression.value = expression.value.dropLast(1)
            isFinalResult.value = false
            updateLiveResult()
        }
    }

    fun clearHistory() {
        history.value = emptyList()
        // Clear from Firebase
        viewModelScope.launch {
            repository.saveHistory(emptyList())
        }
    }

    private fun updateLiveResult() {
        try {
            if (expression.value.isBlank()) {
                result.value = "0"
                return
            }
            result.value = CalculatorEngine.calculate(expression.value)
        } catch (e: Exception) {
            result.value = "Error"
        }
    }
}