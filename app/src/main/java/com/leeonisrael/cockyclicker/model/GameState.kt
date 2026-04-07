package com.leeonisrael.cockyclicker.model

data class GameState(
    val totalHype: Long = 0,
    val totalHypeEarned: Long = 0,
    val ownedUpgrades: Map<String, Int> = emptyMap(),
    val lastKnownTime: Long = System.currentTimeMillis(),
    val totalPlayTime: Long = 0L
) {
    // Calculated properties based on owned upgrades and base values
    // These will be calculated in the ViewModel or here if we pass the registry
}
