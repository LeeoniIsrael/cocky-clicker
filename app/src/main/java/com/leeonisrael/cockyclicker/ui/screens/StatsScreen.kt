package com.leeonisrael.cockyclicker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.ui.theme.DarkGarnet
import com.leeonisrael.cockyclicker.ui.theme.DeepGarnet
import com.leeonisrael.cockyclicker.ui.theme.Garnet
import com.leeonisrael.cockyclicker.ui.theme.Gold
import com.leeonisrael.cockyclicker.ui.theme.LightGarnet
import com.leeonisrael.cockyclicker.util.NumberFormatter
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@Composable
fun StatsScreen(viewModel: GameViewModel, onReset: () -> Unit = {}) {
    val gameState by viewModel.gameState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    val formattedTime = remember(gameState.totalPlayTime) {
        val totalSeconds = gameState.totalPlayTime / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    Scaffold(
        topBar = {
            StatsHeader()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(title = "Lifetime Stats", icon = Icons.Default.History) {
                StatRow(label = "Total Hype Earned", value = NumberFormatter.format(gameState.totalHypeEarned))
                StatRow(label = "Total Taps", value = NumberFormatter.format(gameState.totalTaps))
                StatRow(label = "Total Play Time", value = formattedTime)
            }

            StatsCard(title = "Current Session", icon = Icons.Default.BarChart) {
                StatRow(label = "Session Hype", value = NumberFormatter.format(gameState.sessionHypeEarned))
                StatRow(label = "Upgrades Bought", value = gameState.totalUpgradesPurchased.toString())
            }

            StatsCard(title = "Performance", icon = Icons.Default.QueryStats) {
                StatRow(label = "Hype Per Tap", value = NumberFormatter.format(gameState.hypePerTap))
                StatRow(label = "Hype Per Second", value = NumberFormatter.format(gameState.hypePerSecond))
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Garnet,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "HARD RESET PROGRESS",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Progress?") },
            text = { Text("This will PERMANENTLY delete all your stats, upgrades, and prestige feathers. This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetGame()
                        showResetDialog = false
                        onReset()
                    }
                ) {
                    Text("RESET EVERYTHING", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("CANCEL", color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun StatsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Garnet, DarkGarnet, DeepGarnet)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "📊  STATISTICS",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Gold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun StatsCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LightGarnet,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = LightGarnet,
                letterSpacing = 1.sp
            )
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 12.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )

        Column {
            content()
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Gold
        )
    }
}
