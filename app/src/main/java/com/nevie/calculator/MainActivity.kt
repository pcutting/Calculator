package com.nevie.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.nevie.calculator.model.Inputs

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


        val divide = findViewById<Button>(R.id.divide_button)
        val multiply = findViewById<Button>(R.id.multiply_button)
        val subtract = findViewById<Button>(R.id.subtract_button)
        val add = findViewById<Button>(R.id.addition_button)
        val equals = findViewById<Button>(R.id.equals_button)

        val negator = findViewById<Button>(R.id.negate_button)

        val sqrtButton = findViewById<Button>(R.id.sqrt_button)
        val squaredButton = findViewById<Button>(R.id.squared_button)
        val nthPowerButton = findViewById<Button>(R.id.nth_power_of_button)
        val nthRootButton = findViewById<Button>(R.id.nth_root_button)
        val log2Button = findViewById<Button>(R.id.log2_button)
        val mod = findViewById<Button>(R.id.modulo_button)


        val moreOptionsButton : Button? = findViewById<Button>(R.id.more_options_button)

        val moreOptionsCard : CardView? = findViewById<CardView>(R.id.more_options_card_view)

        if (moreOptionsButton != null) {
            moreOptionsButton.setOnClickListener {
                toggleMoreOptionsView(moreOptionsCard)
            }
        }

        squaredButton?.setOnClickListener {
            if (moreOptionsButton != null) {
                makeCardInvisible(moreOptionsCard)
            }
            calculatorViewModel.buttonPush(Inputs.SQUARED)
            updateScreen(calculationTextView,totalTextView)
        }

        nthPowerButton?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(Inputs.NTHPOWEROF)
            updateScreen(calculationTextView,totalTextView)
        }

        nthRootButton?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(Inputs.NTHROOTOF)
            updateScreen(calculationTextView,totalTextView)
        }

        log2Button?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(Inputs.LOG2)
            updateScreen(calculationTextView,totalTextView)
        }

        mod?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush("%")
            updateScreen(calculationTextView,totalTextView)
        }

        sqrtButton?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(Inputs.SQUAREROOT)
            updateScreen(calculationTextView,totalTextView)
        }

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

    private fun toggleMoreOptionsView(card : CardView?) {
        if (card != null) {
            if (card?.visibility == View.INVISIBLE ?: false) {
                makeCardVisible(card)
            } else {
                makeCardInvisible(card)
            }
        }
    }


    private fun makeCardVisible(card : CardView?) {
        //TODO change background color
        if (card != null) {
            card.visibility=View.VISIBLE
        }

    }

    private fun makeCardInvisible(card : CardView? ) {
        if (card != null) {
            card.visibility=View.INVISIBLE
        }
    }

//    fun setColor (colorID: Int) {
//        layout.setBackgroundColor(ContextCompat.getColor(coolorID))
//    }
}
