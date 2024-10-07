package com.example.ahorcado

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val words = listOf("PERRO", "GATO")
    private var currentWord = ""
    private var guessedWord = ""
    private var errors = 0
    private var score = 0
    private val maxErrors = 6

    private lateinit var tvWord: TextView
    private lateinit var tvErrors: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvHangman: TextView
    private lateinit var gridLayout: GridLayout
    private lateinit var btnRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWord = findViewById(R.id.tvWord)
        tvErrors = findViewById(R.id.tvErrors)
        tvScore = findViewById(R.id.tvScore)
        tvHangman = findViewById(R.id.tvHangman)
        gridLayout = findViewById(R.id.gridLayout)
        btnRestart = findViewById(R.id.btnRestart)

        startNewGame()

        btnRestart.setOnClickListener {
            startNewGame()
        }
    }

    private fun startNewGame() {
        currentWord = words.random()
        guessedWord = "_".repeat(currentWord.length)
        errors = 0
        score = 0
        updateUI()
        setupKeyboard()
    }

    private fun setupKeyboard() {
        gridLayout.removeAllViews()

        val alphabet = ('A'..'Z')
        for (letter in alphabet) {
            val button = Button(this).apply {
                text = letter.toString()
                setOnClickListener { guessLetter(letter) }
            }
            gridLayout.addView(button)
        }

        btnRestart.visibility = View.GONE
    }

    private fun guessLetter(letter: Char) {
        if (guessedWord.contains(letter) || errors >= maxErrors) return

        if (!currentWord.contains(letter)) {
            errors++
        } else {
            score++
            currentWord.forEachIndexed { index, c ->
                if (c == letter) {
                    guessedWord = guessedWord.replaceRange(index, index + 1, letter.toString())
                }
            }
        }

        updateUI()
        checkGameOver()
    }

    private fun updateUI() {
        tvWord.text = guessedWord
        tvErrors.text = "Errores: $errors/$maxErrors"
        tvScore.text = "Puntos: $score"
        tvHangman.text = getHangmanDrawing(errors)
    }

    private fun checkGameOver() {
        if (errors >= maxErrors) {
            tvWord.text = "¡Perdiste! La palabra era: $currentWord"
            btnRestart.visibility = View.VISIBLE
        } else if (!guessedWord.contains("_")) {
            tvWord.text = "¡Ganaste!"
            btnRestart.visibility = View.VISIBLE
        }
    }

    private fun getHangmanDrawing(errors: Int): String {
        return when (errors) {
            0 -> ""
            1 -> "O"
            2 -> "O\n |"
            3 -> "O\n/|"
            4 -> "O\n/|\\"
            5 -> "O\n/|\\\n/"
            6 -> "O\n/|\\\n/ \\"
            else -> "O\n/|\\\n/ \\"
        }
    }
}