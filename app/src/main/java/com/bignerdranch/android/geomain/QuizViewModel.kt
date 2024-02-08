package com.bignerdranch.android.geomain

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {
    val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )
    val questionAnswer = mutableListOf<Int>()
    var questionBall: Int = 0
    var currentIndex = 0
    var isCheater = false

}