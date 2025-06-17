package com.example.scramblemedaddy4.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.scramblemedaddy4.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    val usedWords = mutableSetOf<String>()
    lateinit var currentWord: String
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(scrambledWord = pickAndScrambleWord())
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