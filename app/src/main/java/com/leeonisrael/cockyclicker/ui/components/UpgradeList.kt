package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.model.Upgrade
import com.leeonisrael.cockyclicker.model.UpgradeRegistry
import com.leeonisrael.cockyclicker.ui.theme.DarkGarnet
import com.leeonisrael.cockyclicker.ui.theme.Garnet
import com.leeonisrael.cockyclicker.ui.theme.Gold
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpgradeList(
    gameState: GameState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    var tapExpanded by remember { mutableStateOf(true) }
    var passiveExpanded by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        // ── Hype Tap section ─────────────────────────────────────────────────
        stickyHeader(key = "header_tap") {
            SectionHeader(
                label = "🐓  HYPE TAP",
                expanded = tapExpanded,
                onClick = { tapExpanded = !tapExpanded }
            )
        }
        if (tapExpanded) {
            upgradeItems(
                upgrades = UpgradeRegistry.tapUpgrades,
                gameState = gameState,
                viewModel = viewModel
            )
        }

        // ── Hype/Sec section ─────────────────────────────────────────────────
        stickyHeader(key = "header_passive") {
            SectionHeader(
                label = "⏱  HYPE / SEC",
                expanded = passiveExpanded,
                onClick = { passiveExpanded = !passiveExpanded }
            )
        }
        if (passiveExpanded) {
            upgradeItems(
                upgrades = UpgradeRegistry.passiveUpgrades,
                gameState = gameState,
                viewModel = viewModel
            )
        }
    }
}

private fun LazyListScope.upgradeItems(
    upgrades: List<Upgrade>,
    gameState: GameState,
    viewModel: GameViewModel
) {
    items(upgrades, key = { it.id }) { upgrade ->
        val owned = gameState.ownedUpgrades[upgrade.id] ?: 0
        val cost = viewModel.calculateUpgradeCost(upgrade, owned)
        val canAfford = gameState.totalHype >= cost

        AnimatedVisibility(
            visible = true,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            UpgradeItem(
                upgrade = upgrade,
                ownedCount = owned,
                nextCost = cost,
                canAfford = canAfford,
                onBuy = { viewModel.buyUpgrade(upgrade) }
            )
        }
    }
}

@Composable
private fun SectionHeader(
    label: String,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(listOf(Garnet, DarkGarnet))
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold,
            color = Gold,
            letterSpacing = 1.5.sp
        )
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (expanded) "Collapse" else "Expand",
            tint = Gold
        )
    }
}
