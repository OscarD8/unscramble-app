package com.example.wordscrambler.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.wordscrambler.data.MAX_WORD_LIMIT
import com.example.wordscrambler.data.SCORE_INCREASE
import com.example.wordscrambler.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    val usedWords = mutableSetOf<String>()
    lateinit var currentWord: String
    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(
            scrambledWord = pickAndScrambleWord()
        )
    }

    private fun updateGameState(updatedScore: Int) {
        if (uiState.value.wordCount == MAX_WORD_LIMIT) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = updatedScore,
                    incorrectUserGuess = false
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore,
                    wordCount = currentState.wordCount.inc(),
                    incorrectUserGuess = false,
                    scrambledWord = pickAndScrambleWord()
                )
            }
        }
    }

    fun updateUserGuess(newGuess: String) {
        userGuess = newGuess
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val scoreIncrease = uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore = scoreIncrease)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    incorrectUserGuess = true
                )
            }
        }
        updateUserGuess("")
    }

    fun handleSkip() {
        updateUserGuess("")
        updateGameState(uiState.value.score)
    }

    private fun pickAndScrambleWord(): String {
        currentWord = allWords.random()

        if (usedWords.contains(currentWord)) {
            return pickAndScrambleWord()
        } else {
            usedWords.add(currentWord)
            return shuffleWord(currentWord)
        }
    }

    private fun shuffleWord(word: String): String {
        var tempWord = word.toCharArray()
        tempWord.shuffle()

        while (String(tempWord) == word) {
            tempWord.shuffle()
        }

        return String(tempWord)
    }
}