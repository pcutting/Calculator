package com.nevie.calculator.model

import android.util.Log
import java.lang.Exception
import java.math.RoundingMode

private val TAG = "Calculator model"

class Unit(
    var numberData:String = "", // screen representation, ie "9"
    var sign: Int = 1, // 1 for positive, -1 for negative
    var operation: Operations? = null,
    var kind: Kinds
) {
    fun isNumber():Boolean { return kind == Kinds.NUMBER}
    fun isOperation():Boolean { return kind== Kinds.OPERATOR}
}

enum class Kinds{
    NUMBER, OPERATOR;
}

enum class Operations(val symbol: String) {
    DIVIDE("/"),
    MULTIPLY("*"),
    ADD("+"),
    SUBTRACT("-"),
    MODULO("%"),
    SQUARED("^2"),
    NEGATE("+/-");


    companion object {
        fun isSymbol(symbol: String): Boolean {
            return Operations.values().find { it.symbol == symbol } != null
        }
        fun getEnumValue(symbol:String):Operations? {
            return Operations.values().find{it.symbol == symbol}
        }
    }
}
//TODO Consider making a functionsList into an Enum class.


// Should decimalScale be shifted to the view Model?
class Calculator(
    val operationsList: MutableList<Unit> = mutableListOf<Unit>(),
    var currentTotal: String = "",
    var currentOperator: String? = null,
    var currentCalculation: String = "",
    var decimalScale: Int = 6,
    var lastOperator: String = "",
    private var hasDecimal : Boolean = false,
    val functionsList: List<String> = listOf("clear", "delete", "...", "=")
) {

    // takes button feedback and processes it.
    @ExperimentalStdlibApi
    fun processUserInput(buttonText: String) {
        // 0-9 .:  are numbers, add them to the calculation string
        // % / * - + are operators, check the operator to see if it's postfix or surround operators
        // = means to execute the calculation where ever it is.

        when {
            listOf("+/-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".").contains(buttonText) -> {
                Log.d(TAG, "fun proccessUserInput ('$buttonText')")
                val unit : Unit = Unit(numberData = buttonText, kind = Kinds.NUMBER, operation = Operations.getEnumValue(buttonText))
                if (buttonText==("+/-")) {
                    unit.numberData = "-"
                }
                processNumericKeys(unit)
            }
            Operations.isSymbol(buttonText) -> {
                //"%","/","*","-","+","^" -> {
                val unit : Unit = Unit (kind = Kinds.OPERATOR, operation = Operations.getEnumValue(buttonText)
                )
                if (unit.operation == null)  Exception("expected a known operation, non found.")

                if (this.operationsList.isNullOrEmpty()) {
                    TODO("Currently program doesn't allow for math that begines with an operator.")
                    pushToOperationsList(unit)
                } else if (operationsList.last().isOperation()) {
                    // replacing last pressed operation with new one.  user changed their mind on operator.
                    replaceLastOperatorInOperationsList(unit)
                } else {
                    pushToOperationsList(unit)
                }

                tryToCalculateExistingCalculation()
            }
            "=".contains(buttonText) -> {
                //var unit :Unit = Unit(kind = Kinds.OPERATOR, operation = Operations.EQUALS)
                //pushToOperationsList(unit)
                if (operationsList.count() > 2) {
                    tryToCalculateExistingCalculation()
                } else {
                    //TODO, give a message that there is nothing to do with dempty list.
                }
            }
            "clear".contains(buttonText) -> {
                clearCalculator()
            }
            "delete".contains(buttonText) -> {
                //TODO("Delete last character or operation")
                if (operationsList.isNullOrEmpty()) {
                    currentCalculation = "Nothing to delete."
                } else if (operationsList.last().isOperation()) {
                    operationsList.removeLastOrNull()
                } else {
                    removeLastCharacterFromNumberData()
                }
                buildCurrentCalculationStringFromOperationsList()
            } else -> {
                TODO("unknown operation being called.  Throw error")
            }


        }
    }

    @ExperimentalStdlibApi
    private fun processNumericKeys(unit: Unit) {
        val buttonText = unit.numberData
        if(unit.numberData == ".") hasDecimal = true

        if(unit.operation == Operations.NEGATE) {
            unit.sign *= -1
        }

        when {
            (this.operationsList.isNullOrEmpty()) -> {
                pushToOperationsList(unit)
            }

            unit.operation == Operations.NEGATE && operationsList.last().isNumber() -> {
                var priorUnit = operationsList.last()
                if (priorUnit.numberData[0] == '-') {
                        priorUnit.numberData = priorUnit.numberData.substring(1,priorUnit.numberData.length)
                        replaceLastOperationInOperationsList(priorUnit)
                } else {
                    priorUnit.numberData = "-" + priorUnit.numberData
                    replaceLastOperationInOperationsList(priorUnit)
                }
            }

            (unit.operation == Operations.NEGATE && operationsList.last().isOperation() )-> {
                unit.numberData="-"
                pushToOperationsList(unit)
            }

            (operationsList.last().numberData.contains(".") && buttonText == "." && isNumeric(operationsList.last().numberData))->  {
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
            else -> {TODO("FAIL")}
        }
    }

    private fun tryToCalculateExistingCalculation(wasEqualsPressed : Boolean = false): String {
        var subTotal = ""
        try {
            subTotal = operationsList[0].numberData

            (0 until operationsList.count() - 2 step 2).forEach() {
                //Log.d(TAG, "tryToCalculate: $operator, $operand1, $operand2")

                //TODO reconsider this operation.
                subTotal = callOperation(
                    operationsList[it + 1].operation!!.symbol,
                    subTotal,
                    operationsList[it + 2].numberData
                )
                if (!isNumeric(subTotal)) {
                    // Got an error, or nan, or invalid response.
                    return subTotal
                }
            }
        } catch (e : Error) {
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

    private fun pushToOperationsList(unit: Unit) {
        this.operationsList.add(unit)
        this.buildCurrentCalculationStringFromOperationsList()
    }

    private fun replaceLastOperatorInOperationsList(unit: Unit, buttonText: String? = null) {
        // this is called when a user changes the last operator they selected
        // before they add a number.  This calculator doesn't allow for operator chaining.
        if (unit.isNumber()) {
            throw Exception("trying to remove a non operator item")
        }
        operationsList.removeAt(operationsList.count() - 1)
        pushToOperationsList(unit)
    }

    @ExperimentalStdlibApi
    private fun replaceLastOperationInOperationsList(unit: Unit, buttonText: String? = null) {
        // this is called when a user changes the last operator they selected
        // before they add a number.  This calculator doesn't allow for operator chaining.
        if (unit == null) {
            throw Exception("trying to remove a non operator item")
        }
        operationsList.removeLastOrNull()
        pushToOperationsList(unit)
    }

    private fun combineLastNumberWithNewInputDigitAndUpdateList(text: String) {
        var unit = this.operationsList.last()
        if (!isNumeric(text) || !isNumeric(unit.numberData)) {
            throw Exception("Not a number provided or being operated on")
        }
        this.operationsList.removeAt(operationsList.count() - 1)
        unit.numberData += text
        operationsList
        pushToOperationsList(unit)
    }

    @ExperimentalStdlibApi
    private fun removeLastCharacterFromNumberData() {
        var unit = operationsList.last()
        var digit = unit.numberData

        if (!isNumeric(unit.numberData) || !isNumeric(digit)) {
            throw Exception("Not a number")
        }

        //this.operationsList.removeAt(operationsList.count() - 1)
        if (unit.numberData.length > 1) {
            //remove last char, remove last from list, update and add back to list
            unit.numberData = unit.numberData.substring(0,unit.numberData.length-1)
            operationsList.last().numberData = unit.numberData
        } else {
            operationsList.removeLastOrNull()
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

    //TODO Get rid of operation String, make it enum type.
    private fun callOperation(operation: String?, operandFirst: String, operandSecond: String = ""): String {
        var newTotal = ""
        if (operation.isNullOrEmpty()) { throw Exception("missing operator") }

        val op = Operations.values().find {it.symbol == operation}
        var operand1 = operandFirst.toDouble()
        var operand2 = operandSecond.toDouble()
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
                subtraction(operand1,operand2)
            }
            Operations.SQUARED -> {
                squared(operand1)
            }
            else -> {
                throw Exception("Invalid operation")
            }

        }
        this.currentTotal = formatTotal(newTotal)
        return currentTotal
    }

    private fun formatTotal(total: String):String{
        var formatted = ""
        var num = 0.0

        if (hasDecimal && isNumeric(total)) {
            formatted = textifyNumericFeedback( total.toDouble())
        } else if (hasDecimal) {
            formatted = total
        } else if (isNumeric(total)) {
             num = total.toDouble()
            if (num % 1  == 0.0 )
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
        var testSize = (operand1 * operand2) >  Double.MAX_VALUE
        Log.d(TAG, "Divide, looking for oversized numbers ${testSize}")

        return (math).toString()
    }

    private fun divide(operand1: Double, operand2: Double): String {
        return if (operand2 == 0.0) {
            return "Error div by 0"
        }else {
            (operand1 / operand2).toString()
        }
    }

    private fun modulo(operand1: Double, operand2: Double): String {
        return if (operand2 == 0.0) {
            return "Error Modulo by 0"
        }else  {
            (operand1 % operand2).toString()
        }
    }

}