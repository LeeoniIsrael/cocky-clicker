package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.model.Upgrade
import com.leeonisrael.cockyclicker.model.UpgradeCategory
import com.leeonisrael.cockyclicker.ui.UpgradeIcons
import com.leeonisrael.cockyclicker.util.NumberFormatter

@Composable
fun UpgradeItem(
    upgrade: Upgrade,
    ownedCount: Int,
    nextCost: Long,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        tonalElevation = if (ownedCount > 0) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            val iconTint = when {
                !canAfford && ownedCount == 0 -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                else -> MaterialTheme.colorScheme.secondary
            }
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = UpgradeIcons.getIcon(upgrade.id),
                    contentDescription = upgrade.name,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp),
                    tint = iconTint
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = upgrade.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (ownedCount > 0)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    if (ownedCount > 0) {
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "×$ownedCount",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                val boostLabel = when {
                    upgrade.hypePerTap > 0 -> {
                        "+${NumberFormatter.format(upgrade.hypePerTap)} hype/tap each"
                    }
                    upgrade.hypePerSecond > 0 -> {
                        "+${NumberFormatter.format(upgrade.hypePerSecond)} hype/sec each"
                    }
                    else -> ""
                }
                if (boostLabel.isNotEmpty()) {
                    Text(
                        text = boostLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    )
                }
                Text(
                    text = upgrade.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = onBuy,
                enabled = canAfford,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                    disabledContentColor = Color.Gray.copy(alpha = 0.5f)
                ),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = NumberFormatter.format(nextCost),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
