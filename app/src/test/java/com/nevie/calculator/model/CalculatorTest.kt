package com.nevie.calculator.model

import org.junit.Test

import org.junit.Assert.*
import java.math.RoundingMode

class CalculatorTest {
    @Test
    fun `Test addition with 1 plus  2, returns 3`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.ADD)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals(3.toString(), calculator.currentTotal)
    }

    @Test
    fun `Test subtraction with 1 SUBTRACT NEGATER  2, returns -1`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.SUBTRACT)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals("-1", calculator.currentTotal)
    }

    @Test
    fun `testing multiplication, user enters 3 times 2, returns 6`() {
        assertEquals("6", Calculator().processUserInput("3 * 2 ="))
    }

    @Test
    fun `testing division, user enters 3 divide by 2 and gets 1 point 5`() {
        assertEquals("1.5", Calculator().processUserInput("3 / 2 ="))
    }

    @Test
    fun `test user key feedback 1,2 Mod 7, returns 5`() {
        val calculator = Calculator()
        calculator.processUserInput(CalculatorInputs.ONE)
        calculator.processUserInput(CalculatorInputs.TWO)
        calculator.processUserInput(CalculatorInputs.MODULO)
        calculator.processUserInput(CalculatorInputs.SEVEN)
        calculator.processUserInput(CalculatorInputs.EQUALS)
        assertEquals(5.toString(), calculator.currentTotal)
    }

    @Test
    fun `testing decimals with 2 times 2point3, returns 4point6`() {
        val equation = "2 * 2.3 ="
        val output = Calculator()
            .processUserInput(equation)?.toBigDecimal()?.setScale(3,RoundingMode.CEILING)
            .toString()
        assertEquals("4.600", output)
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

    @Test
    fun `testing helper function processUserInput() user input with 1 + 2, returns 3`() {
        val calculator = Calculator()
        calculator.processUserInput("1 + 2 =")
        assertEquals(3.toString() , calculator.currentTotal)
    }

    @Test
    fun `tested helper function divide, divide(6 point 0,2 point 0), returns 3 point 0`(){
        val calculator = Calculator()
        val output = calculator.divide(6.0,2.0)
        assertEquals("3.0", output)
    }
}