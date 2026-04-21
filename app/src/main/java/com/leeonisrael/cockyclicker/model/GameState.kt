package com.leeonisrael.cockyclicker.model

data class GameState(
    val totalHype: Long = 0,
    val totalHypeEarned: Long = 0,
    val ownedUpgrades: Map<String, Int> = emptyMap(),
    val lastKnownTime: Long = System.currentTimeMillis(),
    val totalPlayTime: Long = 0L,
    val prestigeFeathers: Int = 0,
    val completedMilestones: Set<String> = emptySet(),
    val isMuted: Boolean = false
) {
    val prestigeMultiplier: Double get() = 1.0 + (prestigeFeathers * 0.05)
    val canAscend: Boolean get() = totalHypeEarned >= PRESTIGE_THRESHOLD
    val ascendFeathersAvailable: Int get() = (totalHypeEarned / PRESTIGE_THRESHOLD).toInt()

    companion object {
        const val PRESTIGE_THRESHOLD = 10_000_000L
    }
}
