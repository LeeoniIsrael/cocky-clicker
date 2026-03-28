package com.leeonisrael.cockyclicker.model

object UpgradeRegistry {
    val upgrades = listOf(
        // Tap Multipliers
        Upgrade("foam_finger", "Foam Finger", "Show your spirit!", 15, hypePerTap = 1, category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("garnet_shaker", "Garnet Shaker", "Shake it up!", 100, hypePerTap = 5, category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("megaphone", "Megaphone", "LOUDER!", 1000, hypePerTap = 25, category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("spurs_up_sign", "Spurs Up Sign", "Spurs Up!", 10000, hypePerTap = 100, category = UpgradeCategory.TAP_MULTIPLIER),

        // Auto-Hype
        Upgrade("tailgate_tent", "Tailgate Tent", "The party starts here.", 50, hypePerSecond = 1, category = UpgradeCategory.AUTO_HYPE),
        Upgrade("band_member", "Band Member", "Playing the fight song.", 500, hypePerSecond = 10, category = UpgradeCategory.AUTO_HYPE),
        Upgrade("sir_big_spur", "Sir Big Spur", "The legendary rooster.", 3000, hypePerSecond = 50, category = UpgradeCategory.AUTO_HYPE),
        Upgrade("wb_light_show", "WB Light Show", "Williams-Brice is lit!", 25000, hypePerSecond = 500, category = UpgradeCategory.AUTO_HYPE),
        Upgrade("sandstorm_dj", "Sandstorm DJ", "DA DA DA DA DA!", 150000, hypePerSecond = 5000, category = UpgradeCategory.AUTO_HYPE)
    )
}
