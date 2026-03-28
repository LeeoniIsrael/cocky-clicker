package com.leeonisrael.cockyclicker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leeonisrael.cockyclicker.ui.components.CockyButton
import com.leeonisrael.cockyclicker.ui.components.HypeCounter
import com.leeonisrael.cockyclicker.ui.components.UpgradeList
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    val hypePerSecond = viewModel.calculateHypePerSecond()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HypeCounter(
                totalHype = gameState.totalHype,
                hypePerSecond = hypePerSecond
            )

            Spacer(modifier = Modifier.height(32.dp))

            CockyButton(onTap = { viewModel.onTap() })

            Spacer(modifier = Modifier.height(32.dp))

            UpgradeList(
                gameState = gameState,
                viewModel = viewModel
            )
        }
    }
}
