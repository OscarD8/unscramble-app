package com.example.wordscrambler

import com.example.wordscrambler.data.MAX_NO_OF_WORDS
import com.example.wordscrambler.data.SCORE_INCREASE
import com.example.wordscrambler.data.getUnscrambledWord
import com.example.wordscrambler.ui.GameViewModel
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value // creating local instance of gameState
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.scrambledWord) // setting correct value based on instance above
        // Below we actually call the logic after creating a fixed snapshot of test data
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        // above we got current state, got correct word and updated guess to that word, then refreshed snapshot
        currentGameUiState = viewModel.uiState.value
        // Assert that checkUserGuess() method updates isGuessedWordWrong correctly
        assertFalse(currentGameUiState.incorrectUserGuess)
        // Assert that the score updated correctly
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        assertEquals(0, currentGameUiState.score)
        assertTrue(currentGameUiState.incorrectUserGuess)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unscrambledWord = getUnscrambledWord(gameUiState.scrambledWord)

        // Assert that current word is scrambled
        assertNotEquals(unscrambledWord, gameUiState.scrambledWord)
        // Assert that current word count is set to 1
        assertTrue(gameUiState.wordCount == 1)
        // Assert that initially the score is 0
        assertTrue(gameUiState.score == 0)
        // Assert that initially the incorrect guess is set to false
        assertFalse(gameUiState.incorrectUserGuess)
        // Assert that game is not over
        assertFalse(gameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        // everything is init so we get the state and start a score tracker
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.scrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            // for the 1 to 10, increase expected score and updateGuess/check it
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess() // this will generate a new word
            // after updating viewmodel, update uiState to capture viewModel new word
            currentGameUiState = viewModel.uiState.value
            // capture that new state and update the correct player word again
            correctPlayerWord = getUnscrambledWord(currentGameUiState.scrambledWord)
            // for each iteration of an updated UI state confirm that the expected score is increasing as required
            assertEquals(expectedScore, currentGameUiState.score)
        }
        // At this point, we have the word count and this should be 10, and gameOver should be true
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.wordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        // so we are doing the classic of getting UI state and iterating through one correct condition
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.scrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        // below we are capturing state after one iteration and getting wordCount of this state then skipping
        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.wordCount
        viewModel.handleSkip()
        currentGameUiState = viewModel.uiState.value
        // assert that score remains unchanged after skip
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
        // assert that word count is increased by 1 after word is skipped
        assertEquals(lastWordCount + 1, currentGameUiState.wordCount)
    }


    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}