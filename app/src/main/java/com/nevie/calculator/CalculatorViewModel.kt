package com.nevie.calculator

import android.util.Log
import com.nevie.calculator.model.Calculator
import com.nevie.calculator.model.CalculatorInputs

class CalculatorViewModel  {

    private val calculator: Calculator = Calculator()

    // can last button pressed logic be pushed to model?
    private var lastButton : String = ""


    // TODO should I get rid of the passing text option to prevent errors? code smell? either way?
    fun buttonPush(buttonText : String) {
        Log.d("CalculatorViewModel:", "Button pressed: $buttonText")
        lastButton = buttonText
        calculator.processUserInput(buttonText)
    }

    fun buttonPush(button : CalculatorInputs) {
        Log.d("CalculatorViewModel:", "Button pressed: ${button.text_value}")
        lastButton  = button.text_value
        calculator.processUserInput(button)
    }

    fun getCalculationString():String {
        val calcString = calculator.currentCalculation
        return calcString
    }

    fun getTotalValueFromCalculator():String {
        return calculator.currentTotal.toString()
    }
}