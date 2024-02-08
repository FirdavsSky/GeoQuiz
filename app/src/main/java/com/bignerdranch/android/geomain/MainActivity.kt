package com.bignerdranch.android.geomain

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "lifeCicler"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0
private var flag: Boolean = false
private var podzkazki_index: Int = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var questionResult: TextView
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var text_podzkazki: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        questionResult = findViewById(R.id.questionResult)
        cheatButton = findViewById(R.id.cheat_button)
        text_podzkazki = findViewById(R.id.text_podzkazki)
        text_podzkazki.setText("Вы использовали $podzkazki_index подсказок из 3")
        questionResult.setText("Вы ответили плавильно на ${quizViewModel.questionBall} вопросов")


        updateQuestion()
        nextButton.setOnClickListener {
            if (quizViewModel.currentIndex >= quizViewModel.questionBank.size - 1) quizViewModel.currentIndex =
                5 else quizViewModel.currentIndex++ //= (currentIndex + 1) % questionBank.size
            updateQuestion()
            if (!quizViewModel.questionAnswer.contains(quizViewModel.currentIndex)) {
                trueButton.setEnabled(true)
                falseButton.setEnabled(true)
            } else {
                trueButton.setEnabled(false)
                falseButton.setEnabled(false)

            }
            questionResult.setText("Вы ответили плавильно на ${quizViewModel.questionBall} вопросов из ${quizViewModel.questionBank.size}")
            flag = false
        }
        cheatButton.setOnClickListener {
            // Начало CheatActivity
            val answerIsTrue = quizViewModel.questionBank[quizViewModel.currentIndex].answer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        prevButton.setOnClickListener {
            if (quizViewModel.currentIndex == 0) quizViewModel.currentIndex =
                0 else quizViewModel.currentIndex--
            if (!quizViewModel.questionAnswer.contains(quizViewModel.currentIndex)) {
                trueButton.setEnabled(true)
                falseButton.setEnabled(true)
            } else {
                trueButton.setEnabled(false)
                falseButton.setEnabled(false)
            }
            updateQuestion()
            questionResult.setText("Вы ответили плавильно на ${quizViewModel.questionBall} вопросов из ${quizViewModel.questionBank.size}")
            flag = false

        }

        trueButton.setOnClickListener {
            CheatActivity.flag = false
            //Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            checkAnswer(true)
            quizViewModel.questionAnswer.add(quizViewModel.currentIndex)
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
            questionResult.setText("Вы ответили плавильно на ${quizViewModel.questionBall}  из ${quizViewModel.questionBank.size}")
        }
        falseButton.setOnClickListener {
            //Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
            checkAnswer(false)
            quizViewModel.questionAnswer.add(quizViewModel.currentIndex)
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
            questionResult.setText("Вы ответили плавильно на ${quizViewModel.questionBall} вопросов из ${quizViewModel.questionBank.size}")
            CheatActivity.flag = false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.questionBank[quizViewModel.currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.questionBank[quizViewModel.currentIndex].answer
        if (userAnswer == correctAnswer) {
            quizViewModel.questionBall++
        }

        /*var messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }*/

        val messageResId = when {
            //quizViewModel.isCheater -> R.string.judgment_toast
            flag -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            flag = true
            podzkazki_index++
            if (podzkazki_index == 3){
                cheatButton.setEnabled(false)
            }
            text_podzkazki.setText("Вы использовали $podzkazki_index подсказок из 3")
        }
    }

}