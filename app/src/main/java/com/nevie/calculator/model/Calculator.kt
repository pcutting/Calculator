package com.nevie.calculator.model

import android.util.Log
import java.lang.Exception
import java.math.RoundingMode

private val TAG = "Calculator model"

data class CalulatingUnit(
    var numberData: String = "", // screen representation, ie "9"
    //var sign: Int = 1, // 1 for positive, -1 for negative
    var operation: Operations? = null,
    var kind: Kinds
) {
    fun isNumber(): Boolean {
        return kind == Kinds.NUMBER
    }

    fun isOperation(): Boolean {
        return kind == Kinds.OPERATOR
    }
}

enum class Kinds {
    NUMBER, OPERATOR;
}

enum class Operations(val symbol: String) {
    DIVIDE("/"),
    MULTIPLY("*"),
    ADD("+"),
    SUBTRACT("-"),
    MODULO("%"),
    SQUARED("^2"),
    NEGATE("+/-"),
    NULL("FAIL");


    companion object {
        fun isSymbol(symbol: String): Boolean {
            return Operations.values().find { it.symbol == symbol } != null
        }

        fun getEnumValue(symbol: String): Operations? {
            return Operations.values().find { it.symbol == symbol }
        }
    }
}
//TODO Consider making a functionsList into an Enum class.


// Should decimalScale be shifted to the view Model?
class Calculator(
    val operationsList: MutableList<CalulatingUnit> = mutableListOf<CalulatingUnit>(),
    var currentTotal: String = "",
    var currentCalculation: String = "",
    var decimalScale: Int = 6,
    var lastOperator: String = "",
    private var hasDecimal: Boolean = false
) {

    companion object {
        private val numberTypeButtonsList =
            listOf<String>("+/-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")
    }


    //  ******* ALAN  **** ????????????????????
    // At this point did I clean up this code in a way you were looking for?
    //
    // TODO code smell? around line 195


    fun processUserInput(buttonText: String) {
        // 0-9 .:  are numbers, add them to the calculation string
        // % / * - + are operators, check the operator to see if it's postfix or surround operators
        // = means to execute the calculation where ever it is.

        when {
            // "+/-, 0..9, "."
            numberTypeButtonsList.contains(buttonText) -> {
                processNumericKeys(buttonText)
            }

            Operations.isSymbol(buttonText) -> {
                //"%","/","*","-","+","^" -> {
                tryToCalculateExistingCalculation(buttonText)
            }

            "=".contains(buttonText) -> {

                if (operationsList.count() > 2) {
                    tryToCalculateExistingCalculation()
                }
            }

            "clear".contains(buttonText) -> {
                clearCalculator()
            }

            "delete".contains(buttonText) -> {
                //handleErrors("Delete last character or operation")
                if (operationsList.isNullOrEmpty()) {
                    currentCalculation = "Nothing to delete."
                } else if (operationsList.last().isOperation()) {
                    operationsList.removeAt(operationsList.count()-1)
                } else {
                    removeLastCharacterFromNumberData()
                }
                buildCurrentCalculationStringFromOperationsList()
            }
            else -> {
                handleErrors("unknown operation being called.  Possible error")
            }


        }
    }


    private fun processNumericKeys(buttonText : String) {
        val unit: CalulatingUnit = CalulatingUnit(
            numberData = buttonText,
            kind = Kinds.NUMBER,
            operation = Operations.getEnumValue(buttonText)
        )
        if (buttonText == ("+/-")) {
            unit.numberData = "-"
        }

        if (unit.numberData == ".") {
            hasDecimal = true
        }

        when {
            this.operationsList.isNullOrEmpty() -> {
                pushToOperationsList(unit)
            }

            unit.operation == Operations.NEGATE && operationsList.last().isNumber() -> {
                var priorUnit = operationsList.last()
                if (priorUnit.numberData[0] == '-') {
                    priorUnit.numberData =
                        priorUnit.numberData.substring(1, priorUnit.numberData.length)
                    replaceLastOperationInOperationsList(priorUnit)
                } else {
                    priorUnit.numberData = "-" + priorUnit.numberData
                    replaceLastOperationInOperationsList(priorUnit)
                }
            }

            (unit.operation == Operations.NEGATE && operationsList.last().isOperation()) -> {
                unit.numberData = "-"
                pushToOperationsList(unit)
            }

            (operationsList.last().numberData.contains(".") && buttonText == "." && isNumeric(
                operationsList.last().numberData
            )) -> {
                Log.d(TAG, "processUserInput($buttonText) Working with numbers")
                // do nothing, it already has a . in it, don't add another one.
                // user is trying to double click the "." button
            }
            (operationsList.last().isOperation() && isNumeric(buttonText)) -> {
                pushToOperationsList(unit)
            }
            (operationsList.last().isNumber() && (isNumeric(buttonText))) -> {
                //combine and replace last.
                combineLastNumberWithNewInputDigitAndUpdateList(buttonText)
            }
            else -> {
                handleErrors("FAIL")
            }
        }
    }

    private fun tryToCalculateExistingCalculation(buttonText: String = ""): String {

        // * factored out of when statement back at user input processing.
        val unit: CalulatingUnit = CalulatingUnit(
            kind = Kinds.OPERATOR, operation = Operations.getEnumValue(buttonText)
        )
        if (unit.operation == null) {
            handleErrors("expected a known operation, non found.")
            return "Error"
        }

        if (this.operationsList.isNullOrEmpty()) {
            handleErrors("Currently program doesn't allow for math that begines with an operator.")
            pushToOperationsList(unit)
        } else if (operationsList.last().isOperation()) {
            // replacing last pressed operation with new one.  user changed their mind on operator.
            replaceLastOperatorInOperationsList(unit)
        } else {
            pushToOperationsList(unit)
        }

        // * end of code relocation.




        var subTotal = ""
        try {
            subTotal = operationsList[0].numberData
            (0 until operationsList.count() - 2 step 2).forEach() {
                //TODO reconsider this operation.
                subTotal = callOperation(
                    // TODO verify if this is a code smell.???
                    operation = operationsList[it + 1].operation ?: Operations.NULL,
                    operandFirst = subTotal,
                    operandSecond = operationsList[it + 2].numberData
                )
                if (!isNumeric(subTotal)) {
                    // Got an error, or nan, or invalid response.
                    return subTotal
                }
            }
        } catch (e: Error) {
            subTotal = "Error in calculation"
            Log.d(TAG, "Error $e in tryToCalculate...")
        }
        return subTotal
     }

    private fun isNumeric(text: String): Boolean {
        return text.matches("(-?\\d+(\\.)?)|(-?(\\.\\d+)?)|(-?\\d+(\\.\\d+)?)|(^-?\\.?$)".toRegex())
    }

    private fun buildCurrentCalculationStringFromOperationsList() {
        currentCalculation = ""
        operationsList.forEach {
            if (it.isOperation()) {
                currentCalculation += it.operation!!.symbol
            } else {
                currentCalculation += it.numberData
            }
        }
    }

    private fun pushToOperationsList(unit: CalulatingUnit) {
        this.operationsList.add(unit)
        this.buildCurrentCalculationStringFromOperationsList()
    }

    private fun replaceLastOperatorInOperationsList(
        unit: CalulatingUnit,
        buttonText: String? = null
    ) {
        // this is called when a user changes the last operator they selected
        // before they add a number.  This calculator doesn't allow for operator chaining.
        if (unit.isNumber()) {
            handleErrors("trying to remove a non operator item")
        }
        operationsList.removeAt(operationsList.count() - 1)
        pushToOperationsList(unit)
    }


    private fun replaceLastOperationInOperationsList(
        unit: CalulatingUnit,
        buttonText: String? = null
    ) {
        // this is called when a user changes the last operator they selected
        // before they add a number.  This calculator doesn't allow for operator chaining.
        if (unit == null) {
            handleErrors("trying to remove a non operator item")
        }
        operationsList.removeAt(operationsList.count()-1)
        pushToOperationsList(unit)
    }

    private fun combineLastNumberWithNewInputDigitAndUpdateList(text: String) {
        var unit = this.operationsList.last()
        if (!isNumeric(text) || !isNumeric(unit.numberData)) {
            handleErrors("Not a number provided or being operated on")
        }
        this.operationsList.removeAt(operationsList.count() - 1)
        unit.numberData += text
        operationsList
        pushToOperationsList(unit)
    }


    private fun removeLastCharacterFromNumberData() {
        var unit = operationsList.last()
        var digit = unit.numberData

        if (!isNumeric(unit.numberData) || !isNumeric(digit)) {
            handleErrors("Not a number")
        }

        //this.operationsList.removeAt(operationsList.count() - 1)
        if (unit.numberData.length > 1) {
            //remove last char, remove last from list, update and add back to list
            unit.numberData = unit.numberData.substring(0, unit.numberData.length - 1)
            operationsList.last().numberData = unit.numberData
        } else {
            operationsList.removeAt(operationsList.count()-1)
        }
    }

    private fun clearCalculator() {
        this.operationsList.clear()
        this.currentCalculation = ""
        this.lastOperator = ""
        this.currentTotal = ""
        this.hasDecimal = false
    }

    private fun textifyNumericFeedback(number: Double): String {
        return if (number % 1 == 0.0)
            "${number.toInt()}"
        else
            number.toBigDecimal()
                .setScale(this.decimalScale, RoundingMode.DOWN)
                .toString()
    }

    private fun callOperation(
        operation: Operations,
        operandFirst: String,
        operandSecond: String = ""
    ): String {
        var newTotal = ""
        if (operation == Operations.NULL) {
            handleErrors("missing operator")
            return "Error"
        }


        var operand1 = operandFirst.toDouble()
        var operand2 = operandSecond.toDouble()
        Log.d(TAG, "callOperation($operation, $operand1, $operand2")


        newTotal = when (operation) {
            Operations.DIVIDE -> {
                divide(operand1, operand2)
            }
            Operations.MULTIPLY -> {
                multiply(operand1, operand2)
            }
            Operations.MODULO -> {
                modulo(operand1, operand2)
            }
            Operations.ADD -> {
                addition(operand1, operand2)
            }
            Operations.SUBTRACT -> {
                subtraction(operand1, operand2)
            }
            Operations.SQUARED -> {
                squared(operand1)
            }
            else -> {
                handleErrors("Invalid operation")
                return currentTotal
            }

        }
        this.currentTotal = formatTotal(newTotal)
        return currentTotal
    }

    private fun formatTotal(total: String): String {
        var formatted = ""
        var num = 0.0

        if (hasDecimal && isNumeric(total)) {
            formatted = textifyNumericFeedback(total.toDouble())
        } else if (hasDecimal) {
            formatted = total
        } else if (isNumeric(total)) {
            num = total.toDouble()
            if (num % 1 == 0.0)
                formatted = num.toInt().toString()
            else
                formatted = num.toString()
        } else {
            formatted = total
        }
        return formatted

    }

    private fun equals(): String {
        return (tryToCalculateExistingCalculation())
    }

    private fun addition(operand1: Double, operand2: Double): String {
        return (operand1 + operand2).toString()
    }


    private fun subtraction(operand1: Double, operand2: Double): String {
        return (operand1 - operand2).toString()
    }


    private fun squared(operand: Double): String {
        return (operand * operand).toString()
    }

    private fun multiply(operand1: Double, operand2: Double): String {
        var math = operand1 * operand2
        var testSize = (operand1 * operand2) > Double.MAX_VALUE
        Log.d(TAG, "Divide, looking for oversized numbers ${testSize}")

        return (math).toString()
    }

    private fun divide(operand1: Double, operand2: Double): String {
        return if (operand2 == 0.0) {
            return "Error div by 0"
        } else {
            (operand1 / operand2).toString()
        }
    }

    private fun modulo(operand1: Double, operand2: Double): String {
        return if (operand2 == 0.0) {
            return "Error Modulo by 0"
        } else {
            (operand1 % operand2).toString()
        }
    }

    private fun handleErrors(message: String) {
        Log.d(TAG, "Possible error.  Check out '$message'.")
    }

}