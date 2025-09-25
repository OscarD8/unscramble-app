package com.example.wordscrambler.ui

data class GameUiState (
    val scrambledWord: String = "",
    val wordCount: Int = 1,
    val score: Int = 0,
    val incorrectUserGuess: Boolean = false,
    val isGameOver: Boolean = false
)