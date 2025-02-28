package com.example.calculadoraapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tvHistory: TextView
    private lateinit var scrollView: ScrollView
    private var currentNumber: String = ""
    private var operator: String? = null
    private var firstNumber: Double = 0.0
    private val historyList = mutableListOf<String>() // Lista para historial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        tvHistory = findViewById(R.id.tvHistory)
        scrollView = findViewById(R.id.scrollView)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnEquals, R.id.btnClear, R.id.btnBackspace,
            R.id.btnPercentage, R.id.btnComa
        )

        buttons.forEach { id: Int ->
            val button: Button = findViewById(id)
            button.setOnClickListener { onButtonClick(button) }
        }
    } // **¡Cerramos correctamente onCreate aquí!**

    private fun onButtonClick(button: Button) {
        when (val text = button.text.toString()) {
            "C" -> clear()
            "CA" -> backspace()
            "%" -> aplicarPorcentaje()
            "+", "-", "x", "÷" -> setOperator(text)
            "=" -> calculate()
            "." -> agregarComa()
            else -> appendNumber(text)
        }
    }

    private fun agregarComa() {
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                currentNumber = "0."
            } else {
                currentNumber += "."
            }
            tvResult.text = currentNumber
        }
    }

    private fun clear() {
        currentNumber = ""
        operator = null
        firstNumber = 0.0
        tvResult.text = "0"
        // Historial no se borra
    }

    private fun backspace() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1) // Elimina el último carácter
            tvResult.text = if (currentNumber.isEmpty()) "0" else currentNumber
        }
    }

    private fun aplicarPorcentaje() {
        if (currentNumber.isNotEmpty()) {
            val number = currentNumber.toDouble() / 100
            currentNumber = number.toString()
            tvResult.text = currentNumber
        }
    }

    private fun setOperator(op: String) {
        if (currentNumber.isNotEmpty()) {
            firstNumber = currentNumber.toDouble()
            operator = op
            currentNumber = ""
        }
    }

    private fun calculate() {
        if (operator != null && currentNumber.isNotEmpty()) {
            val secondNumber = currentNumber.toDouble()
            val result = when (operator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "x" -> firstNumber * secondNumber
                "÷" -> firstNumber / secondNumber
                else -> 0.0
            }

            val formattedFirst = formatNumber(firstNumber)
            val formattedSecond = formatNumber(secondNumber)
            val formattedResult = formatNumber(result)

            // Guardar operación en historial
            val operation = "$formattedFirst $operator $formattedSecond = $formattedResult"
            historyList.add(operation)

            // Mantener solo los últimos 10 registros
            if (historyList.size > 10) {
                historyList.removeAt(0)
            }

            tvHistory.text = historyList.joinToString("\n")

            // Desplazarse automáticamente al final del historial
            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }

            tvResult.text = formattedResult
            currentNumber = formattedResult
            operator = null
        }
    }

    private fun formatNumber(value: Double): String {
        return if (value % 1 == 0.0) value.toInt().toString() else value.toString()
    }

    private fun appendNumber(number: String) {
        currentNumber += number
        tvResult.text = currentNumber
    }
}
