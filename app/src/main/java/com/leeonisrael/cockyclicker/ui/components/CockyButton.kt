package com.leeonisrael.cockyclicker.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeonisrael.cockyclicker.ui.theme.DarkGarnet
import com.leeonisrael.cockyclicker.ui.theme.Garnet
import com.leeonisrael.cockyclicker.ui.theme.Gold
import com.leeonisrael.cockyclicker.util.NumberFormatter
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private data class TapBurst(
    val id: Long,
    val text: String,
    val angles: List<Float>,
    val colors: List<Color>,
    val progress: Animatable<Float, AnimationVector1D> = Animatable(0f)
)

@Composable
fun CockyButton(
    onTap: () -> Unit,
    hypePerTap: Long,
    size: Dp = 200.dp
) {
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    var bursts by remember { mutableStateOf<List<TapBurst>>(emptyList()) }

    val density = LocalDensity.current
    val particleTravelPx = with(density) { 90.dp.toPx() }

    // Read all progress values in composable scope so Canvas redraws on animation frames
    val burstSnapshots = bursts.map { b -> b to b.progress.value }

    val garnet = MaterialTheme.colorScheme.primary
    val gold = MaterialTheme.colorScheme.secondary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size + 120.dp)
    ) {
        // ── Particle canvas (drawn behind main button) ──────────────────────
        Box(
            modifier = Modifier
                .size(size + 120.dp)
                .drawBehind {
                    val cx = this.size.width / 2f
                    val cy = this.size.height / 2f
                    burstSnapshots.forEach { (burst, progress) ->
                        burst.angles.forEachIndexed { i, angle ->
                            val dist = progress * particleTravelPx
                            val px = cx + cos(angle) * dist
                            val py = cy + sin(angle) * dist
                            val alpha = (1f - progress).coerceIn(0f, 1f)
                            val radius = 10f * (1f - progress * 0.5f)
                            drawCircle(
                                color = burst.colors[i].copy(alpha = alpha),
                                radius = radius,
                                center = Offset(px, py)
                            )
                        }
                    }
                }
        )

        // ── Floating "+N" labels ────────────────────────────────────────────
        burstSnapshots.forEach { (burst, progress) ->
            Text(
                text = burst.text,
                color = Gold.copy(alpha = (1f - progress).coerceIn(0f, 1f)),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.graphicsLayer {
                    alpha = (1f - progress).coerceIn(0f, 1f)
                    translationY = -progress * 160f
                }
            )
        }

        // ── Main button ─────────────────────────────────────────────────────
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DarkGarnet,
                                Garnet,
                                DarkGarnet
                            )
                        )
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        onTap()

                        // Scale bounce
                        scope.launch {
                            scale.animateTo(
                                1.15f,
                                spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                            scale.animateTo(
                                1f,
                                spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                        }

                        // Particle burst + floating label
                        val numParticles = 8
                        val burst = TapBurst(
                            id = System.nanoTime(),
                            text = "+${NumberFormatter.format(hypePerTap)}",
                            angles = List(numParticles) { i -> (2.0 * PI / numParticles * i).toFloat() },
                            colors = List(numParticles) { i -> if (i % 2 == 0) garnet else gold }
                        )
                        bursts = bursts + burst
                        scope.launch {
                            burst.progress.animateTo(1f, tween(durationMillis = 600))
                            bursts = bursts - burst
                        }
                    }
                }
        ) {
            Text(
                text = "🐓",
                fontSize = when {
                    size >= 220.dp -> 88.sp
                    size >= 180.dp -> 72.sp
                    else -> 56.sp
                }
            )
        }
    }
}
