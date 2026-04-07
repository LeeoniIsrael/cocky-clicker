package com.leeonisrael.cockyclicker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leeonisrael.cockyclicker.util.NumberFormatter
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@Composable
fun StatsScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()

    val formattedTime = remember(gameState.totalPlayTime) {
        val totalSeconds = gameState.totalPlayTime / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Stats",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Total Hype Earned All Time: ${NumberFormatter.format(gameState.totalHypeEarned)}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Total Time Played: $formattedTime",
                style = MaterialTheme.typography.bodyLarge,
            )

            // TODO: Add more detailed stats like time played, clicks per minute, etc.
        }
    }
}
