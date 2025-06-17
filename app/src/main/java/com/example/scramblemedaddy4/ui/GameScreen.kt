package com.example.scramblemedaddy4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scramblemedaddy4.R
import com.example.scramblemedaddy4.ui.theme.AppTheme

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    val mediumPad = dimensionResource(R.dimen.medium_padding)
    val largePad = dimensionResource(R.dimen.large_padding)

    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding(),
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
            modifier = Modifier.padding(vertical = largePad)
        )
    }
}

@Composable
private fun GameLayout(
    modifier: Modifier = Modifier,
    currentWord: String
) {
    val mediumPad = dimensionResource(R.dimen.medium_padding)
    val largePad = dimensionResource(R.dimen.large_padding)

    Card (
        modifier = modifier
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(mediumPad),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPad)
        ) {
            Text(
                text = stringResource(R.string.word_count, 0),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(horizontal = mediumPad),
            )
            Text (
                text = currentWord,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp
            )
        }
    }
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