package com.nevie.calculator.model

import android.util.Log
import java.math.RoundingMode
import kotlin.math.ln
import kotlin.math.pow

private const val TAG = "Calculator model"

data class CalculatingUnit(
    var numberData: String = "", // screen representation, ie "9"
    var operation: CalculatorInputs? = null,
    var kind: Kinds,
    var isResult: Boolean = false
) {
    fun isNumber(): Boolean {
        return kind == Kinds.NUMBER
    }

    fun isOperation(): Boolean {
        return kind == Kinds.OPERATOR
    }

    fun isAOneOperatorOperation(): Boolean {
        return operation?.isAOneVariableOperation() ?: false
    }
}

enum class Kinds {
    NUMBER, OPERATOR, FUNCTION_CALL;
}

enum class CalculatorInputs(private val kindOf : Kinds, val text_value: String, val textOnlyEquivalent: String = "") {
    CLEAR(Kinds.FUNCTION_CALL,"clear"),
    DELETE(Kinds.FUNCTION_CALL, "delete"),

    DIVIDE(Kinds.OPERATOR,"/"),
    MULTIPLY(Kinds.OPERATOR,"*", "x"),
    ADD(Kinds.OPERATOR,"+", "plus"),
    SUBTRACT(Kinds.OPERATOR,"-", "minus"),
    MODULO(Kinds.OPERATOR,"%", "mod"),
    SQUARED(Kinds.OPERATOR,"^2", "squared"),
    NTHPOWEROF(Kinds.OPERATOR,"^ˣ", "x^y"),
    SQUAREROOT(Kinds.OPERATOR,"\u221a", "sqrt"),
    NTHROOTOF(Kinds.OPERATOR,"\u02e3√", "xRootOf"),
    LOG2(Kinds.OPERATOR,"Log2"),
    EQUALS(Kinds.OPERATOR,"=", "equals"),
    NULL(Kinds.OPERATOR,"null"),

    NEGATE(Kinds.NUMBER,"+/-", "+/-"),
    ONE(Kinds.NUMBER,"1"),
    TWO(Kinds.NUMBER,"2"),
    THREE(Kinds.NUMBER,"3"),
    FOUR(Kinds.NUMBER,"4"),
    FIVE(Kinds.NUMBER,"5"),
    SIX(Kinds.NUMBER,"6"),
    SEVEN(Kinds.NUMBER,"7"),
    EIGHT(Kinds.NUMBER,"8"),
    NINE(Kinds.NUMBER,"9"),
    ZERO(Kinds.NUMBER,"0"),
    DOT(Kinds.NUMBER,".", "dot");

    fun isOperator():Boolean {
        return this.kindOf == Kinds.OPERATOR
    }

    fun isNumber(): Boolean {
        return this.kindOf == Kinds.NUMBER
    }

    fun isAOneVariableOperation(): Boolean {
        return (listOfOneVariableOperations.contains(this))
    }

    companion object {
        fun isSymbol(symbol: String): Boolean {
            return CalculatorInputs.values().find { it.text_value == symbol ||
                    it.textOnlyEquivalent.toLowerCase() == symbol.toLowerCase() ||
                    it.text_value.toLowerCase() == symbol.toLowerCase()} != null
        }

        fun getEnumValue(symbol: String): CalculatorInputs {
            return CalculatorInputs.values().find { it.text_value   == symbol ||
                    it.textOnlyEquivalent.toLowerCase() == symbol.toLowerCase() ||
                    it.text_value.toLowerCase() == symbol.toLowerCase()} ?: NULL
        }

        private val listOfOneVariableOperations = listOf(
            EQUALS,
            NULL,
            NEGATE,
            SQUARED,
            SQUAREROOT,
            LOG2
        )
    }
}


// TODO: Remove. This was left in only for discussion.
//enum class Operations(val symbol: String) {
//    DIVIDE("/"),
//    MULTIPLY("*"),
//    ADD("+"),
//    SUBTRACT("-"),
//    MODULO("%"),
//    SQUARED("^2"),
//    NTHPOWEROF("^n"),
//    SQUAREROOT("\u221a"),
//    NTHROOTOF("\u00B2\u221a"),
//    NEGATE("+/-"),
//    EQUALS("="),
//    NULL("FAIL");
//
//    companion object {
//        fun isSymbol(symbol: String): Boolean {
//            return values().find { it.symbol == symbol } != null
//        }
//
//        fun getEnumValue(symbol: String): Operations? {
//            return values().find { it.symbol == symbol }
//        }
//
//        private val listOfOneVariableOperations = listOf(EQUALS, NULL, NEGATE, SQUARED,SQUAREROOT)
//
//        fun isAOneVariableOperation(operation:Operations): Boolean {
//            return (listOfOneVariableOperations.contains(operation))
//        }
//    }
//}

class Calculator(
    val operationsList: MutableList<CalculatingUnit> = mutableListOf<CalculatingUnit>(),
    var currentTotal: String = "",
    var currentCalculation: String = "",
    var decimalScale: Int = 6,
    private var hasDecimal: Boolean = false
) {
    companion object {
        private val numberTypeButtonsList = listOf<String>("+/-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")
    }


    //  ******* ALAN  **** ????????????????????
    // At this point did I clean up this code in a way you were looking for?
    //
    // TODO code smell? around line 334


    fun processUserInput(buttonText: String) {
        // Verify input provided. Either it is in form of a simple enum class CalculatorInput.
        //  or it'll be strings, maybe simple operator equivalents, numbmers, or complete calculations.

        if (buttonText.isNullOrBlank()) {
            return
        } else if (buttonText.contains(" ")) {
            //has more than one operation.  Send it to check to see if it is a calculation substring
            processStatementInputs(buttonText)
        } else if (isNumeric(buttonText)) {
            processCompleteNumberInputs(buttonText)
        } else {
            processUserInput(CalculatorInputs.getEnumValue(buttonText))
        }
    }

    private fun processCompleteNumberInputs(numeric : String) {
        if (isNumeric(numeric)) {
            // Send the entire number to be processed.

            (0 until numeric.length).forEach {
                if (it == 0 && numeric[0] == '-') {
                    processUserInput(CalculatorInputs.NEGATE)
                } else {
                    processUserInput(CalculatorInputs.getEnumValue(numeric[it].toString()))
                }
            }
        } else {
            Log.d(TAG, "processCompleteNumberInputs(${numeric}) was passed an invalid string")
        }
    }

    private fun processStatementInputs(string: String) {
        Log.d(TAG, "Processing complete calculation from user input: $string")
        string.split(" ").forEach {
            if(isNumeric(it)) {
                processCompleteNumberInputs(it)
            } else {
                processUserInput(CalculatorInputs.getEnumValue(string))
            }
        }
    }

    fun processUserInput(button: CalculatorInputs ) {
        // 0-9 .:  are numbers, add them to the calculation string
        // % / * - + are operators, check the operator to see if it's postfix or surround operators
        // = means to execute the calculation where ever it is.

        when {
            // "+/-, 0..9, "."
            button.isNumber() -> {
                processNumericKeys(button)
            }

            button.isOperator() -> {
                //"%","/","*","-","+","^","=",.... -> {
                tryToCalculateExistingCalculation(button)
            }

//            "=".contains(button.text_value) -> {
//                if (operationsList.count() > 2) {
//                    tryToCalculateExistingCalculation(button.text_value)
//                }
//            }

            button == CalculatorInputs.CLEAR -> {
                clearCalculator()
            }

            button == CalculatorInputs.DELETE-> {
                //handleErrors("Delete last character or operation")
                if (operationsList.isNullOrEmpty()) {
                    currentCalculation = "Nothing to delete."
                } else if (operationsList.last().isResult) {
                    operationsList.removeAt(operationsList.size-1)
                    operationsList.removeAt(operationsList.size-1)

                }else if (operationsList.last().isOperation()) {
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

    private fun processNumericKeys(button : CalculatorInputs) {
        if (!operationsList.isNullOrEmpty() && operationsList.last().isResult) {
            clearCalculator()
        }

        val unit: CalculatingUnit = CalculatingUnit(
            numberData = button.text_value,
            kind = Kinds.NUMBER,
            operation = null  // Operations.getEnumValue(button.text_value)
        )
        if (button == CalculatorInputs.NEGATE) {
            unit.numberData = "-"
        }

        if (button == CalculatorInputs.DOT) {
            hasDecimal = true
        }

        when {
            this.operationsList.isNullOrEmpty() -> {
                pushToOperationsList(unit)
            }

            unit.operation == CalculatorInputs.NEGATE && operationsList.last().isNumber() -> {
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

            (unit.operation == CalculatorInputs.NEGATE && operationsList.last().isOperation()) -> {
                unit.numberData = "-"
                pushToOperationsList(unit)
            }

            operationsList.last().numberData.contains(".") && button.text_value == "."
                    && isNumeric(operationsList.last().numberData) -> {
                Log.d(TAG, "processUserInput($button.text_value) Working with numbers")
                // do nothing, it already has a . in it, don't add another one.
                // user is trying to double click the "." button
            }
            operationsList.last().isOperation() && isNumeric(button.text_value) -> {
                pushToOperationsList(unit)
            }

            (operationsList.last().isNumber() && (isNumeric(button.text_value))) -> {
                //combine and replace last.
                combineLastNumberWithNewInputDigitAndUpdateList(button.text_value)
            }
            else -> {
                handleErrors("FAIL")
            }
        }
    }

    private fun tryToCalculateExistingCalculation(button: CalculatorInputs): String {
        val unit: CalculatingUnit = CalculatingUnit(
            kind = Kinds.OPERATOR, operation = CalculatorInputs.getEnumValue(button.text_value)
        )

        if (unit.operation == null || unit.operation == CalculatorInputs.NULL) {
            handleErrors("expected a known operation, non found.")
            return "Error"
        }

        if (this.operationsList.isNullOrEmpty()) {
            handleErrors("Currently program doesn't allow for math that begines with an operator.")
            return "Error. nothing to perform operation on, put a number first please."
        } else if (operationsList.last().isOperation()) {
            // replacing last pressed operation with new one.  user changed their mind on operator.
            replaceLastOperatorInOperationsList(unit)
        } else {
            pushToOperationsList(unit)
        }

        var subTotal = ""
        try {
            subTotal = operationsList[0].numberData
            (0 until operationsList.count() - 1 step 2).forEach() {
                //TODO reconsider this operation.
                if ((operationsList[it+1].operation?.isAOneVariableOperation() ?: false)) {
                    subTotal = callOneOperationOperator(
                        operation = operationsList[it + 1].operation ?: CalculatorInputs.NULL,
                        operand = subTotal
                    )
                } else if (it >= operationsList.size-2) {
                    //at end of list and it's not a one operator. to prevent searching past end of list.
                } else {
                    subTotal = callTwoOperationOperator(
                        // TODO verify if this is a code smell.???
                        operation = operationsList[it + 1].operation ?: CalculatorInputs.NULL,
                        operandFirst = subTotal,
                        operandSecond = operationsList[it + 2].numberData
                    )
                }
                if (!isNumeric(subTotal)) {
                    // Got an error, or nan, or invalid response.
                    return subTotal
                }
            }
        } catch (e: Error) {
            subTotal = "Error in calculation"
            Log.d(TAG, "Error $e in tryToCalculate...")
        }

        if ( operationsList.last().operation?.isAOneVariableOperation() ?: false) {
//        if (operationsList.last().operation == Inputs.EQUALS) {
            pushToOperationsList(CalculatingUnit(isResult = true, numberData = textifyNumericFeedback(subTotal.toDouble()), kind = Kinds.NUMBER))
        }

        return subTotal
     }

    private fun isNumeric(text: String): Boolean {
        return text.matches("(-?\\d+(\\.)?)|(-?(\\.\\d+)?)|(-?\\d+(\\.\\d+)?)|(^-?\\.?$)".toRegex())
    }

    private fun buildCurrentCalculationStringFromOperationsList() {
        currentCalculation = ""
        operationsList.forEach {
            if (it.isOperation() && it.isAOneOperatorOperation() && it.operation != CalculatorInputs.EQUALS) {
                currentCalculation += " ${it.operation?.text_value ?: " ERROR "} = "
            } else if (it.isOperation()) {
                currentCalculation += " ${it.operation?.text_value ?: " ERROR "} "
            } else {
                currentCalculation += it.numberData
            }
        }
    }

    private fun pushToOperationsList(unit: CalculatingUnit) {
        this.operationsList.add(unit)
        this.buildCurrentCalculationStringFromOperationsList()
    }

    private fun replaceLastOperatorInOperationsList(
        unit: CalculatingUnit
    ) {
        // this is called when a user changes the last operator they selected
        // before they add a number.  This calculator doesn't allow for operator chaining.
        if (unit.isNumber()) {
            handleErrors("trying to remove a non operator item")
        }
        operationsList.removeAt(operationsList.count() - 1)
        pushToOperationsList(unit)
    }


    private fun replaceLastOperationInOperationsList(unit: CalculatingUnit) {
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

    private fun callOneOperationOperator(
        operation: CalculatorInputs,
        operand: String
    ): String {
        var newTotal = ""
        if (operation == CalculatorInputs.NULL) {
            handleErrors("missing operator")
            return "Error"
        }

        var operand = operand.toDoubleOrNull() ?: 0.0

        Log.d(TAG, "callOneOneOperationOperator($operation, $operand")

        newTotal = when (operation) {
            CalculatorInputs.NULL -> {
                "Error or null used."
            }

            CalculatorInputs.EQUALS -> {
                equals(operand)
            }

            CalculatorInputs.NEGATE -> {
                Log.d(TAG, "ERROR.  this operator should note have been handled here.")
                "" // implemented in numbers function
            }

            CalculatorInputs.SQUAREROOT -> {
                squareroot(operand)
            }

            CalculatorInputs.SQUARED -> {
                squared(operand)
            }

            CalculatorInputs.LOG2 -> {
                log2(operand)
            }

            else -> {
                handleErrors("Invalid operation")
                return currentTotal
            }
        }
        this.currentTotal = formatTotal(newTotal)
        return currentTotal
    }


    private fun callTwoOperationOperator(
        operation: CalculatorInputs,
        operandFirst: String,
        operandSecond: String = ""
    ): String {
        var newTotal = ""
        if (operation == CalculatorInputs.NULL) {
            handleErrors("missing operator")
            return "Error"
        }


        var operand1 = operandFirst.toDouble()
        var operand2 = operandSecond.toDouble()
        Log.d(TAG, "callOperation($operation, $operand1, $operand2")


        newTotal = when (operation) {
            CalculatorInputs.DIVIDE -> {
                divide(operand1, operand2)
            }
            CalculatorInputs.MULTIPLY -> {
                multiply(operand1, operand2)
            }
            CalculatorInputs.MODULO -> {
                modulo(operand1, operand2)
            }
            CalculatorInputs.ADD -> {
                addition(operand1, operand2)
            }
            CalculatorInputs.SUBTRACT -> {
                subtraction(operand1, operand2)
            }
            CalculatorInputs.NTHROOTOF -> {
                sqrtOfX(operand1, operand2)
            }
            CalculatorInputs.NTHPOWEROF -> {
                powerOfX(operand1,operand2)
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

    private fun nullOperation(operand1: Double = 0.0, operand2: Double = 0.0): String {
        return "Some error"
    }
//
//    private fun equals(): String {
//
//        return (tryToCalculateExistingCalculation())
//    }

    private fun addition(operand1: Double, operand2: Double): String {
        return (operand1 + operand2).toString()
    }

    private fun subtraction(operand1: Double, operand2: Double): String {
        return (operand1 - operand2).toString()
    }

    private fun squared(operand: Double): String {
        return (operand * operand).toString()
    }

    private fun equals(operand: Double): String {
        return (operand).toString()
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

    private fun squareroot(operand: Double): String {
        return kotlin.math.sqrt(operand).toString()
    }

    private fun sqrtOfX(operand1: Double, operand2: Double): String {
        return (operand1.pow(1.0 / operand2)).toString()

    }

    private fun powerOfX(operand1: Double, operand2: Double): String {
        return operand1.pow(operand2).toString()
    }

    private fun log2(operand:Double):String {
        return (ln(operand)/ln(2.0)).toString()
    }

    private fun handleErrors(message: String) {
        Log.d(TAG, "Possible error.  Check out '$message'.")
    }

}