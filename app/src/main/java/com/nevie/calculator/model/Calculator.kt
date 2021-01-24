package com.nevie.calculator.model

import android.util.Log
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode

private val TAG = "Calculator model"

enum class Operations(val symbol: String) {
    DIVIDE("/"),
    MULTIPLY("*"),
    ADD("+"),
    SUBTRACT("-"),
    MODULO("%"),
    SQUARED("^2");

    companion object {
        fun isSymbol(symbol: String): Boolean {
            return Operations.values().find { it.symbol == symbol } != null
        }
    }
}
//TODO Consider making a functionsList into an Enum class.


// Should decimalScale be shifted to the view Model?
class Calculator(
    val operationsList: MutableList<String> = mutableListOf<String>(),
    var currentTotal: String = "",
    var currentOperator: String? = null,
    var currentCalculation: String = "",
    var decimalScale: Int = 6,
    var lastOperator: String = "",
    private var hasDecimal : Boolean = false,
    val functionsList: List<String> = listOf("clear", "delete", "...", "=")
) {

    // takes button feedback and processes it.
    fun processUserInput(buttonText: String) {
        // 0-9 .:  are numbers, add them to the calculation string
        // % / * - + are operators, check the operator to see if it's postfix or surround operators
        // = means to execute the calculation where ever it is.

        when {
            listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".").contains(buttonText) -> {
                Log.d(TAG, "fun proccessUserInput ('$buttonText')")

                if(buttonText == ".") hasDecimal = true

                if (this.operationsList.isNullOrEmpty()) {
                    pushToOperationsList(buttonText)
                } else if (operationsList.last().contains(".") &&
                    buttonText == "." &&
                    isNumeric(operationsList.last())
                ) {
                    Log.d(TAG, "processUserInput($buttonText) Working with numbers")
                    // do nothing, it already has a . in it, don't add another one.
                    // user is trying to double click the "." button
                } else if (Operations.isSymbol(operationsList.last()) && isNumeric(buttonText)) {
                    pushToOperationsList(buttonText)
                } else if (isNumeric(this.operationsList.last()) && (isNumeric(buttonText))) {
                    //combine and replace last.
                    combineLastNumberWithNewInputDigitAndUpdateList(buttonText)
                }
            }
            Operations.isSymbol(buttonText) -> {
                //"%","/","*","-","+","^" -> {
                // 1) check to see if the previous operation was an operator.
                // true ), replace with current operator. and continue to check for subtotal option
                // false ) add operator to list and calcs and go to 2
                if (this.operationsList.isNullOrEmpty()) {
                    TODO("Currently program doesn't allow for math that begines with an operator.")
                    pushToOperationsList(buttonText)
                } else if (Operations.isSymbol(operationsList.last())) {
                    // replacing last pressed operation with new one.  user changed their mind on operator.
                    replaceLastOperatorInOperationsList(buttonText)
                } else if (isNumeric(operationsList.last()) &&
                    operationsList.last().endsWith(".")
                ) {
                    //Check if last is a number with decimal without a digit after the decimal.
                    //if trailing decimal, add a zero after the decimal for formating. Then add operation
                    //to end of list.
                    combineLastNumberWithNewInputDigitAndUpdateList("0")
                    pushToOperationsList(buttonText)
                } else {
                    pushToOperationsList(buttonText)
                }

                tryToCalculateExistingCalculation()
            }
            "=".contains(buttonText) -> {
                TODO("= operation function that figures what to do.")
                pushToOperationsList(buttonText)

            }
            "clear".contains(buttonText) -> {
                clearCalculator()
            }
            "delete".contains(buttonText) -> {
                TODO("Delete last character or operation")
                pushToOperationsList(buttonText)
            }
            "...".contains(buttonText) -> {
                TODO(
                    "more_functions operation called... this probably should be handled " +
                            "inside the ViewModel"
                )
                pushToOperationsList(buttonText)
            }
            else -> {
                TODO("unknown operatiion being called.  Throw error")
            }

        }
    }

    private fun tryToCalculateExistingCalculation(text: String = "") {
        // What to do when the function "=" is passed to argument text?


        var total = ""
        operationsList.forEach {
            Log.d(TAG, "tryToCalculateExistingCalculation. $it , ${it.iterator()}")
        }

        if (operationsList.count() < 3) {
            total = ""
        } else {
            total = callOperation(
                operationsList[1],
                operationsList[0].toDouble(),
                operationsList[2].toDouble()
            )
        }

        if (text == "=" && (total == "" || !isNumeric(total))) {
            total = "undefined, failed try again."
        }

        this.currentTotal = total
    }

    private fun isNumeric(text: String): Boolean {
        return text.matches("(-?\\d+(\\.)?)|(-?(\\.\\d+)?)|(-?\\d+(\\.\\d+)?)|(^-?\\.?$)".toRegex())
    }


    private fun buildCurrentCalculationStringFromOperationsList() {
        //this.currentCalculation = ""
        this.currentCalculation = operationsList.joinToString(" ", "", "")
        Log.d(TAG,"buildCurrentCalculationString... " +
                "${operationsList.joinToString(" ", "", "")}"
        )
    }


    private fun pushToOperationsList(text: String) {
        this.operationsList.add(text)
        this.buildCurrentCalculationStringFromOperationsList()
    }

    private fun replaceLastOperatorInOperationsList(text: String) {
        // this is called when a user changes the last operator they selected
        // before they add a number.  This calculator doesn't allow for operator chaining.
        if (!Operations.isSymbol(text)) {
            throw Exception("trying to remove a non operator item")
        }
        operationsList.removeAt(operationsList.count() - 1)
        pushToOperationsList(text)
    }

    private fun combineLastNumberWithNewInputDigitAndUpdateList(text: String) {
        var digit = this.operationsList.last()
        if (!isNumeric(text) || !isNumeric(digit)) {
            throw Exception("Not a number")
        }
        this.operationsList.removeAt(operationsList.count() - 1)
        digit += text
        pushToOperationsList(digit)
    }

    private fun clearCalculator() {
        this.operationsList.clear()
        this.currentCalculation = ""
        this.lastOperator = ""
        this.currentTotal = ""
        this.hasDecimal = false
    }

    private fun isOperatorOrFunction(value: String): Boolean {
        Operations.values().forEach { if (value == it.name) return true }
        //the functions list check may not make sense.  verify if it even should be used.
        this.functionsList.forEach { if (it == value) return true }
        return false
    }

    private fun lastButtonPressed(): String {
        return this.operationsList.last()
    }

    private fun textifyNumericFeedback(number: Double): String {
        return if (number % 2 == 0.0)
            "${number.toInt()}"
        else
            number.toBigDecimal().setScale(this.decimalScale, RoundingMode.DOWN).toString()
    }

    private fun callOperation(operation: String, operand1: Double, operand2: Double = 0.0): String {
        var newTotal = ""
        val op = Operations.values().find {it.symbol == operation}
        Log.d(TAG, "callOperation($operation, $operand1, $operand2")

        newTotal = when (op) {
            Operations.DIVIDE -> {
                divide(operand1,operand2)
            }
            Operations.MULTIPLY -> {
                multiply(operand1, operand2)
            }
            Operations.MODULO -> {
                modulo(operand1,operand2)
            }
            Operations.ADD -> {
                addition(operand1,operand2)
            }
            Operations.SUBTRACT -> {
                subraction(operand1,operand2)
            }
            Operations.SQUARED -> {
                squared(operand1)
            }
            else -> {
                throw Exception("Invalid operation")
            }
            //            Operations -> { }
        }
        this.currentTotal = newTotal
        return newTotal
    }

    

    private fun addition(operand1: Double, operand2: Double): String {

        return (operand1 + operand2).toString()

    }


    private fun subraction(operand1: Double, operand2: Double): String {
        return if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (operand1 - operand2).toString()
        } else "nan"
    }


    private fun squared(operand: Double): String {
        return if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (operand * operand).toString()
        } else "nan"
    }

    private fun multiply(operand1: Double, operand2: Double): String {
        return if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (operand1 * operand2).toString()
        } else "nan"
    }

    private fun divide(operand1: Double, operand2: Double): String {
        return if (operand2 == 0.0) {
            return "Error div by 0"
        }else if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (operand1 / operand2).toString()
        } else "nan"
    }

    private fun modulo(operand1: Double, operand2: Double): String {
        return if (operand2 == 0.0) {
            return "Error div by 0"
        }else if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (operand1 % operand2).toString()
        } else "nan"
    }

    private fun multiply(operand: Double): String {
        return if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (currentTotal.toDouble() * operand).toString()
        } else "nan"
    }

    private fun divide(operand: Double): String {
        return if (this.currentTotal != "" && !isNumeric(currentTotal)) {
            (currentTotal.toDouble() / operand).toString()
        } else "nan"
    }


}