package com.example.calculadoraapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var currentNumber: String = ""
    private var operator: String? = null
    private var firstNumber: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnEquals, R.id.btnClear, R.id.btnClearAll, // btnClearAll reemplaza a ⌫
            R.id.btnPercentage, // Nuevo botón de porcentaje
            R.id.btnComa // Botón de coma
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it as Button) }
        }
    }

    private fun onButtonClick(button: Button) {
        when (val text = button.text.toString()) {
            "C" -> clear()
            "CA" -> borrarUltimoNumero()
            "%" -> aplicarPorcentaje()
            "+", "-", "x", "÷" -> setOperator(text)  // Cambia "*" a "x" y "/" a "÷"
            "=" -> calculate()
            "." -> agregarComa()
            else -> appendNumber(text)
        }
    }
    private fun agregarComa() {
        if (!currentNumber.contains(".")) { // Verifica que no haya otra coma
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
    }

    private fun borrarUltimoNumero() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
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

            val formattedResult = if (result % 1 == 0.0) result.toInt().toString() else result.toString()

            tvResult.text = formattedResult
            currentNumber = formattedResult
            operator = null
        }
    }

    private fun appendNumber(number: String) {
        currentNumber += number
        tvResult.text = currentNumber
    }
}
