package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.model.Upgrade
import com.leeonisrael.cockyclicker.model.UpgradeRegistry
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@Composable
fun UpgradeList(
    gameState: GameState,
    viewModel: GameViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Upgrades",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(UpgradeRegistry.upgrades) { upgrade ->
            val ownedCount = gameState.ownedUpgrades.getOrDefault(upgrade.id, 0)
            val cost = viewModel.calculateUpgradeCost(upgrade, ownedCount)
            val canAfford = gameState.totalHype >= cost

            UpgradeItem(
                upgrade = upgrade,
                ownedCount = ownedCount,
                nextCost = cost,
                canAfford = canAfford,
                onBuy = { viewModel.buyUpgrade(upgrade) }
            )
        }
    }
}
