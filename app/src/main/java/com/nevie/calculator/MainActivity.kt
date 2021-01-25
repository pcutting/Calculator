package com.nevie.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var calculatorViewModel  = CalculatorViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculationTextView = findViewById<TextView>(R.id.calculation_text_view)
        val totalTextView = findViewById<TextView>(R.id.total_text_view)

        val n0 = findViewById<Button>(R.id.n0_button)
        val n1 = findViewById<Button>(R.id.n1_button)
        val n2 = findViewById<Button>(R.id.n2_button)
        val n3 = findViewById<Button>(R.id.n3_button)
        val n4 = findViewById<Button>(R.id.n4_button)
        val n5 = findViewById<Button>(R.id.n5_button)
        val n6 = findViewById<Button>(R.id.n6_button)
        val n7 = findViewById<Button>(R.id.n7_button)
        val n8 = findViewById<Button>(R.id.n8_button)
        val n9 = findViewById<Button>(R.id.n9_button)

        val dot  = findViewById<Button>(R.id.dot_button)
        val delete = findViewById<Button>(R.id.delete_button)
        val clear = findViewById<Button>(R.id.clear_button)
        val mod = findViewById<Button>(R.id.modulo_button)
        val divide = findViewById<Button>(R.id.divide_button)
        val multiply = findViewById<Button>(R.id.multiply_button)
        val subtract = findViewById<Button>(R.id.subtract_button)
        val add = findViewById<Button>(R.id.addition_button)
        val equals = findViewById<Button>(R.id.equals_button)

        val negator = findViewById<Button>(R.id.negate_button)

        //val moreFunctions = findViewById<Button>(R.id.negate_button)

        //values
        negator.setOnClickListener { calculatorViewModel.buttonPush("+/-")
            updateScreen(calculationTextView,totalTextView)}
        n0.setOnClickListener { calculatorViewModel.buttonPush("0")
            updateScreen(calculationTextView,totalTextView)}
        n1.setOnClickListener { calculatorViewModel.buttonPush("1")
            updateScreen(calculationTextView,totalTextView)
        }
        n2.setOnClickListener { calculatorViewModel.buttonPush("2")
            updateScreen(calculationTextView,totalTextView)}
        n3.setOnClickListener { calculatorViewModel.buttonPush("3")
            updateScreen(calculationTextView,totalTextView)}
        n4.setOnClickListener { calculatorViewModel.buttonPush("4")
            updateScreen(calculationTextView,totalTextView)}
        n5.setOnClickListener { calculatorViewModel.buttonPush("5")
            updateScreen(calculationTextView,totalTextView)}
        n6.setOnClickListener { calculatorViewModel.buttonPush("6")
            updateScreen(calculationTextView,totalTextView)}
        n7.setOnClickListener { calculatorViewModel.buttonPush("7")
            updateScreen(calculationTextView,totalTextView)}
        n8.setOnClickListener { calculatorViewModel.buttonPush("8")
            updateScreen(calculationTextView,totalTextView)}
        n9.setOnClickListener { calculatorViewModel.buttonPush("9")
            updateScreen(calculationTextView,totalTextView)}
        dot.setOnClickListener { calculatorViewModel.buttonPush(".")
            updateScreen(calculationTextView,totalTextView)}


        //Operations
        mod.setOnClickListener {
            calculatorViewModel.buttonPush("%")
            updateScreen(calculationTextView,totalTextView)
        }
        divide.setOnClickListener { calculatorViewModel.buttonPush("/")
            updateScreen(calculationTextView,totalTextView)
        }
        multiply.setOnClickListener { calculatorViewModel.buttonPush("*")
            updateScreen(calculationTextView,totalTextView)}
        subtract.setOnClickListener { calculatorViewModel.buttonPush("-")
            updateScreen(calculationTextView,totalTextView)}
        add.setOnClickListener { calculatorViewModel.buttonPush("+")
            updateScreen(calculationTextView,totalTextView)}

        //Function calls.
        equals.setOnClickListener { calculatorViewModel.buttonPush("=")
            updateScreen(calculationTextView,totalTextView)}
        clear.setOnClickListener { calculatorViewModel.buttonPush("clear")
            updateScreen(calculationTextView,totalTextView)}
        delete.setOnClickListener { calculatorViewModel.buttonPush("delete")
            updateScreen(calculationTextView,totalTextView)}
//        moreFunctions.setOnClickListener {
//            calculatorViewModel.buttonPush("...")
//            //DO("make proper impementation for this.")
//            updateScreen(calculationTextView,totalTextView)
//        }
        //            .setOnClickListener { calculatorViewModel.buttonPush("") }
    }

    private fun updateScreen(calculationTextView: TextView, totalTextView: TextView){
        calculationTextView.text = calculatorViewModel.getCaluculationString()
        totalTextView.text = calculatorViewModel.getTotalValueFromCalculator()
    }

}
