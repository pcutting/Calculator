package com.nevie.calculator.model

import org.junit.Test

import org.junit.Assert.*

class CalculatorTest {

    @Test
    fun `1 plus  2, returns 3`() {
        // 1 ADD 2 = 3
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)

        calculator.processUserInput(CalculatorInputs.ADD)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals(3.toString(), calculator.currentTotal)
    }

    @Test
    fun `negation 1 SUBTRACT NEGATER  2, returns 3`() {
        // 1 - -2 = 3
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)

        calculator.processUserInput(CalculatorInputs.SUBTRACT)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals(3.toString(), calculator.currentTotal)
    }


    @Test
    fun `testing user enters 1 + 2 and gets 3`() {
        //calculator.processUserInput("1 + 2 =")
        val calculator = Calculator()
        calculator.processUserInput("1 + 2 =")

        assertEquals(3.toString() , calculator.currentTotal)
    }

    @Test
    fun `testing user enters 3 * 2 and gets 6`() {
        //calculator.processUserInput("3 * 2 =")
        assertEquals("6", Calculator().processUserInput("3 * 2 ="))
    }

    @Test
    fun `testing user enters 3 divide by 2 and gets 1 point 5`() {
        //calculator.processUserInput("3 * 2")
        assertEquals(1.5.toString(), Calculator().processUserInput("3 / 2"))
    }

    @Test
    fun `test user key feedback 1,2 % 7, returns 5`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.MODULO)
        calculator.processUserInput(CalculatorInputs.SEVEN)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals(5.toString(), calculator.currentTotal)
    }



    @Test
    fun `test user key feedback 1,2, DIVIDE, 6, returns 2`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.DIVIDE)
        calculator.processUserInput(CalculatorInputs.SIX)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals(2.toString(), calculator.currentTotal)
    }



//    @Test
//    fun processUserInput() {
//    }
//
//    @Test
//    fun testProcessUserInput() {
//    }
//
//    @Test
//    fun getOperationsList() {
//    }
//
//    @Test
//    fun getCurrentTotal() {
//    }
//
//    @Test
//    fun setCurrentTotal() {
//    }
//
//    @Test
//    fun getCurrentCalculation() {
//    }
//
//    @Test
//    fun setCurrentCalculation() {
//    }
//
//    @Test
//    fun getDecimalScale() {
//    }
//
//    @Test
//    fun setDecimalScale() {
//    }
//
//    @Test
//    operator fun component1() {
//    }
//
//    @Test
//    operator fun component2() {
//    }
//
//    @Test
//    operator fun component3() {
//    }
//
//    @Test
//    operator fun component4() {
//    }
//
//    @Test
//    fun copy() {
//    }
//
//    @Test
//    fun testToString() {
//    }
//
//    @Test
//    fun testHashCode() {
//    }
//
//    @Test
//    fun testEquals() {
//    }
}