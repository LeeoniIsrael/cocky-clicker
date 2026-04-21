package com.leeonisrael.cockyclicker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.ui.components.CockyButton
import com.leeonisrael.cockyclicker.ui.components.HypeCounter
import com.leeonisrael.cockyclicker.ui.components.UpgradeList
import com.leeonisrael.cockyclicker.ui.theme.CockyClickerTheme
import com.leeonisrael.cockyclicker.ui.theme.DarkGarnet
import com.leeonisrael.cockyclicker.ui.theme.DeepGarnet
import com.leeonisrael.cockyclicker.ui.theme.Garnet
import com.leeonisrael.cockyclicker.ui.theme.Gold
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    val hypePerTap = remember(gameState.ownedUpgrades, gameState.prestigeFeathers) {
        viewModel.calculateHypePerTap()
    }
    val hypePerSecond = remember(gameState.ownedUpgrades, gameState.prestigeFeathers) {
        viewModel.calculateHypePerSecond()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp
        val isSmall = maxWidth < 380.dp

        val buttonSize: Dp = when {
            isTablet -> 240.dp
            isSmall  -> 160.dp
            else     -> 200.dp
        }
        val counterFontSize = if (isSmall) 36 else 48

        if (isTablet) {
            // ── Two-column tablet layout ──────────────────────────────────
            Column(modifier = Modifier.fillMaxSize()) {
                GameHeader(isMuted = gameState.isMuted, onToggleMute = viewModel::toggleMute)
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left: counter + button
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        HypeCounter(
                            gameState = gameState,
                            hypePerTap = hypePerTap,
                            hypePerSecond = hypePerSecond,
                            counterFontSize = counterFontSize
                        )
                        Spacer(Modifier.height(16.dp))
                        CockyButton(
                            onTap = viewModel::onTap,
                            hypePerTap = hypePerTap,
                            size = buttonSize
                        )
                        Spacer(Modifier.height(16.dp))
                        AscendSection(
                            gameState = gameState,
                            onAscend = viewModel::ascend
                        )
                    }
                    // Right: upgrades
                    UpgradeList(
                        gameState = gameState,
                        viewModel = viewModel,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            // ── Single-column phone layout ────────────────────────────────
            Column(modifier = Modifier.fillMaxSize()) {
                GameHeader(isMuted = gameState.isMuted, onToggleMute = viewModel::toggleMute)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    HypeCounter(
                        gameState = gameState,
                        hypePerTap = hypePerTap,
                        hypePerSecond = hypePerSecond,
                        counterFontSize = counterFontSize
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CockyButton(
                            onTap = viewModel::onTap,
                            hypePerTap = hypePerTap,
                            size = buttonSize
                        )
                    }
                    AscendSection(
                        gameState = gameState,
                        onAscend = viewModel::ascend,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    UpgradeList(
                        gameState = gameState,
                        viewModel = viewModel,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun GameHeader(
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Garnet, DarkGarnet, DeepGarnet)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "🐓  COCKY CLICKER",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Gold,
            letterSpacing = 1.sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        IconButton(
            onClick = onToggleMute,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = Gold
            )
        }
    }
}

@Composable
private fun AscendSection(
    gameState: GameState,
    onAscend: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!gameState.canAscend) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(DarkGarnet, Garnet, DarkGarnet)),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "✨ READY TO ASCEND",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Gold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "+${gameState.ascendFeathersAvailable} Prestige Feather${if (gameState.ascendFeathersAvailable != 1) "s" else ""}  •  Resets hype & upgrades",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onAscend,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = DeepGarnet
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "🪶  ASCEND",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(
    name = "Small phone (360dp)",
    device = "spec:width=360dp,height=640dp,dpi=320",
    showBackground = true
)
@Composable
private fun GameScreenSmallPreview() {
    CockyClickerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                "GameScreen Preview — 360dp",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(
    name = "Standard phone (411dp)",
    device = "spec:width=411dp,height=891dp,dpi=420",
    showBackground = true
)
@Composable
private fun GameScreenStandardPreview() {
    CockyClickerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                "GameScreen Preview — 411dp",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(
    name = "Tablet (600dp)",
    device = "spec:width=600dp,height=1024dp,dpi=240",
    showBackground = true
)
@Composable
private fun GameScreenTabletPreview() {
    CockyClickerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                "GameScreen Preview — 600dp tablet",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
