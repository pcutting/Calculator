package com.nevie.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.nevie.calculator.model.CalculatorInputs

class MainActivity : AppCompatActivity() {

    private var calculatorViewModel  = CalculatorViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //val mTextStatus =  findViewById<TextView>(R.id.calculation_text_view);
        val mScrollView : ScrollView =  findViewById<ScrollView>(R.id.calculation_text_scroller);


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
            calculatorViewModel.buttonPush(CalculatorInputs.SQUARED)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }

        nthPowerButton?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(CalculatorInputs.NTHPOWEROF)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }

        nthRootButton?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(CalculatorInputs.NTHROOTOF)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }

        log2Button?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(CalculatorInputs.LOG2)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }

        mod?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush("%")
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }

        sqrtButton?.setOnClickListener {
            makeCardInvisible(moreOptionsCard)
            calculatorViewModel.buttonPush(CalculatorInputs.SQUAREROOT)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }

        negator.setOnClickListener {
            calculatorViewModel.buttonPush(CalculatorInputs.NEGATE)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }
        n0.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.ZERO)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n1.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.ONE)
            updateScreen(calculationTextView,totalTextView, mScrollView) }
        n2.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.TWO)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n3.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.THREE)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n4.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.FOUR)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n5.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.FIVE)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n6.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.SIX)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n7.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.SEVEN)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n8.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.EIGHT)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        n9.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.NINE)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        dot.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.DOT)
            updateScreen(calculationTextView,totalTextView, mScrollView)}


        //Operations

        divide.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.DIVIDE)
            updateScreen(calculationTextView,totalTextView, mScrollView)
        }
        multiply.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.MULTIPLY)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        subtract.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.SUBTRACT)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        add.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.ADD)
            updateScreen(calculationTextView,totalTextView, mScrollView)}

        //Function calls.
        equals.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.EQUALS)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        clear.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.CLEAR)
            updateScreen(calculationTextView,totalTextView, mScrollView)}
        delete.setOnClickListener { calculatorViewModel.buttonPush(CalculatorInputs.DELETE)
            updateScreen(calculationTextView,totalTextView, mScrollView)}


        
    }

    private fun scrollToBottom(mScrollView : ScrollView) {
        val mTextStatus = mScrollView.findViewById<TextView>(R.id.calculation_text_view)
         mScrollView.smoothScrollTo(0, mTextStatus.getBottom());

    }
    
    
    private fun updateScreen( calculationTextView: TextView, totalTextView: TextView, mScrollView : ScrollView){

        calculationTextView.text = calculatorViewModel.getCalculationString()
        totalTextView.text = calculatorViewModel.getTotalValueFromCalculator()
        scrollToBottom(mScrollView)
        
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
