package com.janewaitara.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
/**
 * Whenever an orientation is changed in android, the current activity is destroyed and a new one is create
 * Thats why oncreate is called when the orientation changes(which results to configuration change - results in an activity flow being inactive)
 *
 *
 * To save properties ,we use onSaveInstanceState  , we will save the score and time left
 *
 * */

class MainActivity : AppCompatActivity() {

    internal  lateinit var tapMeButton: Button
    internal  lateinit var gameScoreText: TextView
    internal  lateinit var timeLeftText: TextView

    internal var score = 0

    internal var hasGameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 10000
    internal val countDownInterval: Long = 1000
    internal  var timeLeftOnTimer: Long = 60000 //property to hold time left for saving

    companion object{
       private val TAG = MainActivity::class.java.simpleName

        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "Oncreate called.Score is: $score") //helped know that onCreate is called when screen orientation is changed

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreText = findViewById(R.id.gameScoreTextView)
        timeLeftText = findViewById(R.id.timeLeftText)

        tapMeButton.setOnClickListener { view->
            incrementScore()
        }

        resetGame()

    }

    override fun onSaveInstanceState(outState: Bundle) { //bundle is a dictionary(map) used by android to pass values from different activities
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY,score)
        outState.putLong(TIME_LEFT_KEY,timeLeftOnTimer)

        countDownTimer.cancel()

        Log.d(TAG,"onSaveInstanceState: Saving score: $score and Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG,"onDestroy is called")
    }


    private fun incrementScore() {
        if (!hasGameStarted){

          startGame()
        }
        score += 1
        val newScore = getString(R.string.your_score, score)
        gameScoreText.text = newScore

    }

    private fun resetGame(){
        score = 0

        gameScoreText.text = getString(R.string.your_score,score)

        val initialTimeLeft = initialCountDown / 1000 //converting to seconds
        timeLeftText.text = getString(R.string.time_left,initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown,countDownInterval){
            override fun onFinish() {

                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
               val timeLeft = millisUntilFinished/ 1000
                timeLeftText.text = getString(R.string.time_left, timeLeft)
            }

        }

        hasGameStarted = false
    }

    private fun startGame(){

            countDownTimer.start()
            hasGameStarted = true
    }

    private fun endGame(){
        Toast.makeText(this,getString(R.string.gameOverMessage, score),Toast.LENGTH_LONG).show()
        resetGame()
    }

}
