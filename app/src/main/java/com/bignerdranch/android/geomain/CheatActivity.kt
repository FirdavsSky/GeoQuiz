package com.bignerdranch.android.geomain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val EXTRA_ANSWER_IS_TRUE =
    "com.bignerdranch.android.geoquiz.answer_is_true"


const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private var answerIsTrue = false

class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var show_api: TextView
    private lateinit var showAnswerButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)


        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        show_api = findViewById(R.id.show_api)
        show_api.setText("API LEVEL " + Build.VERSION.SDK_INT.toString())
        showAnswerButton.setOnClickListener {
            val answerText =when{
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
            flag = true
        }
        if (flag){
            val answerText =when{
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }

    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext,CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue)
            }
        }
        var flag = false

    }
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putBoolean("KEY", flag)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        flag = savedInstanceState.getBoolean("KEY")
    }
}