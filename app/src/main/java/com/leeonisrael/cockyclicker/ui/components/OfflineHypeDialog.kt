package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import com.leeonisrael.cockyclicker.util.NumberFormatter
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@Composable
fun OfflineHypeDialog(viewModel: GameViewModel) {
    val offlineHype by viewModel.offlineHypeReceived.collectAsState()

    // If there is hype to show, display the dialog
    offlineHype?.let { amount ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissOfflineHype() },
            title = {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "While you were away, your fans stayed busy! You earned ${
                        NumberFormatter.format(
                            amount
                        )
                    } Hype.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.dismissOfflineHype() }) {
                    Text("Go Cocks!")
                }
            }
        )
    }
}