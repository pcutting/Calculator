package com.nevie.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nevie.calculator.model.Calculator

class CalculatorViewModel  {

    private val calculator = Calculator()

    // can last button pressed logic be pushed to model?
    private var lastButton : String = ""


    fun buttonPush(buttonText : String) {
        Log.d("CalculatorViewModel:", "Button text passed: $buttonText")
        lastButton = buttonText
        calculator.processUserInput(buttonText)
    }

    // should this be pushed to model?
    fun lastButtonPressedWasDot() : Boolean { return lastButton == "."}


    fun getCaluculationString():String {
        val calc_string = calculator.currentCalculation
        return calc_string
    }

    fun getTotalValueFromCalculator():String {
        return calculator.currentTotal.toString()
    }
}