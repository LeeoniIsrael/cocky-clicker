package com.leeonisrael.cockyclicker.model

enum class UpgradeCategory {
    TAP_MULTIPLIER,
    AUTO_HYPE
}

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val baseCost: Long,
    val hypePerTap: Long = 0,
    val hypePerSecond: Long = 0,
    val category: UpgradeCategory
)
