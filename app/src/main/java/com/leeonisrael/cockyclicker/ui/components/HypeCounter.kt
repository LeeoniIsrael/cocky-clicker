package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.util.NumberFormatter

@Composable
fun HypeCounter(
    gameState: GameState,
    hypePerTap: Long,
    hypePerSecond: Long,
    counterFontSize: Int = 48
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Total hype display
        Text(
            text = NumberFormatter.format(gameState.totalHype),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = counterFontSize.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        )
        Text(
            text = "HYPE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 4.sp
        )

        Spacer(Modifier.height(4.dp))

        // Rate display
        if (hypePerTap > 1 || hypePerSecond > 0) {
            Text(
                text = buildString {
                    append(NumberFormatter.format(hypePerTap))
                    append(" / tap")
                    if (hypePerSecond > 0) {
                        append("   •   ")
                        append(NumberFormatter.format(hypePerSecond))
                        append(" / sec")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Prestige feathers display
        if (gameState.prestigeFeathers > 0) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "🪶 ${gameState.prestigeFeathers} Prestige Feather${if (gameState.prestigeFeathers != 1) "s" else ""}" +
                        "  •  +${(gameState.prestigeMultiplier * 100 - 100).toInt()}% multiplier",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Prestige progress bar
        val prestigeProgress = (gameState.totalHypeEarned.toFloat() / GameState.PRESTIGE_THRESHOLD)
            .coerceIn(0f, 1f)
        if (gameState.totalHypeEarned > 0) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Prestige: ${NumberFormatter.format(gameState.totalHypeEarned)} / ${NumberFormatter.format(GameState.PRESTIGE_THRESHOLD)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { prestigeProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(6.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
