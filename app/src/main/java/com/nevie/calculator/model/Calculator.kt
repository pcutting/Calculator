package com.nevie.calculator.model

import android.util.Log
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
        fun isSymbol(symbol: String) : Boolean {
            return Operations.values().find{it.symbol == symbol} != null
        }
    }
}
//TODO Consider making a functionsList into an Enum class.


// Should decimalScale be shifted to the view Model?
class Calculator(
    val operationsList: MutableList<String> = mutableListOf<String>(),
    var currentTotal : Double = 0.0,
    var currentOperator: String? = null ,
    var currentCalculation: String = "",
    var decimalScale: Int = 6,
    var lastOperator : String = "",
    val functionsList: List<String> = listOf("clear","delete", "...", "=")
) {

    // takes button feedback and processes it.
    fun processUserInput(buttonText : String) {
        // 0-9 .:  are numbers, add them to the calculation string
        // % / * - + are operators, check the operator to see if it's postfix or surround operators
        // = means to execute the calculation where ever it is.

        //TODO!!! THIS MUST BE MOVED TO THE WHEN BLOCK.
        //  pushToOperationsList(buttonText)

        //when (buttonText) {
        when {
            listOf("0","1","2","3","4","5","6","7","8","9").contains(buttonText) -> {
                Log.d(TAG, "fun proccessUserInput ('$buttonText')")

                if (this.operationsList.isNullOrEmpty()) {TODO("initiate empty list")}

                if (operationsList.last().contains(".")  && buttonText == "."
                    && isNumeric(operationsList.last())) {
                    Log.d(
                        "Calculator",
                        "fun pushTopOperationsList if second . added to numberic value was attempted"
                    )
                    // already has a . in it, don't add another one.
                } else if (Operations.isSymbol(operationsList.last()) && isNumeric(buttonText)) {
                    operationsList.add(buttonText)
                } else if (isNumeric(this.operationsList.last()) && (isNumeric(buttonText))) {
                    Log.d(TAG, "pushToOperationsList  ... if is numberic")
                    val op = operationsList.last() + buttonText
                    operationsList.removeAt(operationsList.count() - 1)
                    operationsList.add(op)
                }



                //pushToOperationsList(buttonText)

            }
            ".".contains(buttonText) -> {
                //TODO("special logic for when decimals are used should go here.

                Log.d("Calculator", "fun buildCalculation ('$buttonText')")
                pushToOperationsList(buttonText)

            }
            Operations.isSymbol(buttonText) -> {
                //"%","/","*","-","+","^" -> {
                // 1) check to see if the previous operation was an operator.
                // true ), replace with current operator. and continue to check for subtotal option
                // false ) add operator to list and calcs and go to 2
                if (this.operationsList.isNullOrEmpty()) {
                    TODO("what to do when operator is first button")
                } else if (this.operationsList != null &&
                    this.operationsList.isNotEmpty() &&
                    Operations.isSymbol(operationsList.last())) {
                    //Last operation was an operand, not a number, so user is trying to
                    //change the operand it's working with.

                    lastOperator = buttonText
                    this.pushToOperationsList(buttonText)

                } else if (lastOperator != null && lastOperator != "") {
                    tryToCalculateExistingCalculation(buttonText)
                    this.lastOperator = buttonText
                    this.buildCurrentCalculationStringFromOperationsList()
                    pushToOperationsList(buttonText)
                } else {
                    this.lastOperator = buttonText
                    pushToOperationsList(buttonText)
                    this.buildCurrentCalculationStringFromOperationsList()

                }
                // 2) if there is a executable function, get subtotal.
                // true) Label as subtotal

                Log.d("Calculator when ops", "$buttonText")


                tryToCalculateExistingCalculation(buttonText)

            }
            "=".contains(buttonText)  -> {
                    TODO("= operation function that figures what to do.")
                    pushToOperationsList(buttonText)

            }
            "clear".contains(buttonText)  -> {
                clearCalculator()
            }
            "delete".contains(buttonText)  -> {TODO("Delete last character or operation")
                pushToOperationsList(buttonText)
            }
            "...".contains(buttonText)  -> {
                TODO("more_functions operation called... this probably should be handled " +
                        "inside the ViewModel")
                pushToOperationsList(buttonText)
            }
            else -> {TODO("unknown operatiion being called.  Throw error")}

        }
    }

    private fun tryToCalculateExistingCalculation(buttonText: String) {
        //Not sure passing buttonText is needed here.
        operationsList.forEach{
            Log.d("Calculator model" ,"tryToCalculateExistingCalculation. $it")
        }
        this.currentTotal = 999.9999
    }

    private fun isNumeric(text: String) :Boolean {
        return text.matches("(-?\\d+(\\.)?)|(-?(\\.\\d+)?)|(-?\\d+(\\.\\d+)?)|(^-?\\.?$)".toRegex())
    }


    private fun buildCurrentCalculationStringFromOperationsList(){
        //this.currentCalculation = ""
        this.currentCalculation = operationsList.joinToString(" ", "","")
        Log.d("Calculator",
            "buildCurrentCalculationString... ${operationsList.joinToString(" ", "","")}")
    }



    private fun pushToOperationsList(text:String) {
        if(operationsList == null)
            TODO("This list should never be null")
        else if (operationsList.isEmpty() ) {
            this.operationsList.add(text)
            Log.d(TAG, "pushToOperationsList  ... if operationsList.isEmpty()")

        } else if (Operations.isSymbol(text) && Operations.isSymbol(operationsList.last())){
            operationsList.removeAt(operationsList.count()-1)
            operationsList.add(text)
        } else if (Operations.isSymbol(text)){
            operationsList.add(text)
        } else {
            TODO("why did it get here. Trying to push something not a number and not a operator" +
                    " onto the function list")
        }

        this.buildCurrentCalculationStringFromOperationsList()
    }

    private fun clearCalculator() {
        this.operationsList.clear()
        this.currentCalculation =""
        this.lastOperator = ""
        this.currentTotal = 0.0
    }

    private fun isOperatorOrFunction(value : String): Boolean {
        Operations.values().forEach { if (value == it.name) return true }
        //the functions list check may not make sense.  verify if it even should be used.
        this.functionsList.forEach { if (it == value) return true }
        return false
    }

    private fun hasPriorDot(): Boolean {
        operationsList.reversed().forEach  {
            if (it == ".") return true
            if (isOperatorOrFunction(it)) return false
        }
        return false
    }

    private fun lastButtonPressed() : String {return this.operationsList.last()}

    private fun textifyNumericFeedback(number: Double) : String {
        return if (number % 2 == 0.0)
            "${number.toInt()}"
        else
            number.toBigDecimal().setScale(this.decimalScale, RoundingMode.DOWN).toString()
    }

    // should this be pushed to model?
    private fun lastButtonPressedWasDot() : Boolean { return this.lastButtonPressedWasDot()}

    private fun callOperation(op: Operations, operand1: Double, operand2:Double):Double {

        // TODO : check to see if last button pressed was a . without a following number.  If so
        //          add a zero to it.
        operationsList.add(op.name)
        operationsList.add(operand2.toString())
        this.currentOperator = op.name
        this.currentCalculation += " ${op.name} $operand2"
        var newTotal = 0.0

        newTotal = when (op) {
            Operations.DIVIDE -> {
                this.divide(operand2)
            }
            Operations.MULTIPLY -> {
                this.multiply(operand2)
            }
            Operations.MODULO -> {
                0.0
            }
            Operations.ADD -> {
                0.0
            }
            Operations.SUBTRACT -> {
                0.0
            }
            Operations.SQUARED -> {
                0.0
            }
    //            Operations -> { }
        }

        this.currentTotal = newTotal

        return newTotal
    }

    private fun multiply(operand: Double): Double {
        return if(this.currentTotal != null) this.currentTotal!! * operand else 0.0

        // return this.currentTotal * operand
    }

    private fun divide(operand: Double): Double {
        return if(this.currentTotal != null) this.currentTotal!! / operand else 0.0
        //return this.currentTotal / operand
    }





}