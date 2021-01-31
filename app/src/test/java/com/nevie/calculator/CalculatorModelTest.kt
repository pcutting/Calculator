package com.nevie.calculator.model

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorModelTest {

    @Test
    fun `testing user enters 1 + 2 and gets 3`() {
        //calculator.processUserInput("1 + 2")
        assertEquals(3, Calculator().processUserInput("1 + 2"))
    }

    @Test
    fun `testing user enters 3 * 2 and gets 6`() {
        //calculator.processUserInput("3 * 2")
        assertEquals(6, Calculator().processUserInput("3 * 2"))
    }

    @Test
    fun `testing user enters 3 divide by 2 and gets 1 point 5`() {
        //calculator.processUserInput("3 * 2")
        assertEquals(1.5, Calculator().processUserInput("3 / 2"))
    }

    @Test
    fun `test user key feedback 1,2 % 7, returns 5`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.MODULO)
        calculator.processUserInput(CalculatorInputs.SEVEN)
        assertEquals(5, calculator.currentTotal)
    }


    // this test function should fail. I wrote it wrong on purpose.
    @Test
    fun `test user key feedback 1,2, DIVIDE, 6, returns 2`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.DIVIDE)
        calculator.processUserInput(CalculatorInputs.SIX)
        assertEquals(3, calculator.currentTotal)
    }



//    fun `when a user enters operand of 3 point 0, multiplication operator, operand of 2 point 0 and presses = operand, should return 6 with decimals`(){
//        val multiplied = calculationsViewModel.multiply(3.0,2.0)
//        Assert.assertEquals(6.0, multiplied)
//    }
//
//    fun `when a user enters operand of 6, division operator, operand of 3 and presses = operand, should return 2 with decimals`() {
//        val divided = calculationsViewModel.divide(6.0,3.0)
//        Assert.assertEquals(2.0, divided)
//    }

}