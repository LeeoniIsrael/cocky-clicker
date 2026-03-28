package com.leeonisrael.cockyclicker.model

data class GameState(
    val totalHype: Long = 0,
    val totalHypeEarned: Long = 0,
    val ownedUpgrades: Map<String, Int> = emptyMap()
) {
    // Calculated properties based on owned upgrades and base values
    // These will be calculated in the ViewModel or here if we pass the registry
}
