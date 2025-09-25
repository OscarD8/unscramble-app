package com.example.wordscrambler.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordscrambler.R
import com.example.wordscrambler.ui.theme.AppTheme

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    val smallPad = dimensionResource(R.dimen.small_padding)
    val mediumPad = dimensionResource(R.dimen.medium_padding)
    val largePad = dimensionResource(R.dimen.large_padding)

    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        GameLayout(
            currentWord = gameUiState.scrambledWord,
            userGuess = gameViewModel.userGuess,
            wordCount = gameUiState.wordCount,
            isGuessWrong = gameUiState.incorrectUserGuess,
            onGuessChange = {gameViewModel.updateUserGuess(it)},
            onKeyboardAction = {gameViewModel.checkUserGuess()},
            guessStatus = gameUiState.incorrectUserGuess,
            modifier = Modifier.padding(vertical = largePad)
        )
        Button(
            onClick = {gameViewModel.checkUserGuess()},
            modifier = Modifier
                .width(300.dp)
        ) {
            Text(
                text = stringResource(R.string.submit),
                style = MaterialTheme.typography.labelMedium
            )
        }
        OutlinedButton(
            onClick = {gameViewModel.handleSkip()},
            modifier = Modifier
                .padding(top = mediumPad)
                .width(300.dp)
            ) {
            Text(
                text = stringResource(R.string.skip),
                style = MaterialTheme.typography.labelMedium
            )
        }
        GameStatus(
            score = gameUiState.score,
            modifier = Modifier.padding(top = largePad)
        )
    }
    if (gameUiState.isGameOver) {
        GameOver(
            score = gameUiState.score,
            onPlayAgain = {gameViewModel.resetGame()}
        )
    }
}

@Composable
private fun GameLayout(
    modifier: Modifier = Modifier,
    currentWord: String,
    wordCount: Int,
    userGuess: String,
    guessStatus: Boolean,
    onGuessChange: (String) -> Unit,
    onKeyboardAction: () -> Unit,
    isGuessWrong: Boolean
) {
    val smallPad = dimensionResource(R.dimen.small_padding)
    val mediumPad = dimensionResource(R.dimen.medium_padding)
    val largePad = dimensionResource(R.dimen.large_padding)

    Card (
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(mediumPad),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPad).width(300.dp)
        ) {
            Text(
                text = stringResource(R.string.word_count, wordCount),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    .padding(horizontal = smallPad),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = currentWord,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
            )
            Text(
                text = stringResource(R.string.instructions),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = userGuess,
                onValueChange = onGuessChange,
                isError = guessStatus,
                singleLine = true,
                label = {
                    if (isGuessWrong) {
                        Text(stringResource(R.string.wrong_guess))
                    } else {
                        Text(stringResource(R.string.enter_your_word))
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardAction() }
                )
            )
        }
    }
}

@Composable
private fun GameStatus(
    score: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.score, score),
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(dimensionResource(R.dimen.medium_padding)),
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}

@Composable
private fun GameOver(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current

    AlertDialog(
        onDismissRequest = {activity?.finish()},
        title = {Text(stringResource(R.string.congratulations))},
        text = {Text(stringResource(R.string.you_scored, score))},
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = { activity?.finish() }
            ) { Text(text = stringResource(R.string.exit))}
        },
        confirmButton = {
            TextButton(
                onClick = onPlayAgain
            ) {
                Text(stringResource(R.string.play_again))
            }
        }
    )
}

@Preview
@Composable
fun LightPreview() {
    AppTheme(darkTheme = false) {
        GameScreen()
    }
}

@Preview
@Composable
fun DarkPreview() {
    AppTheme(darkTheme = true) {
        GameScreen()
    }
}