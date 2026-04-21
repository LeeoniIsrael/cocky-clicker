package com.leeonisrael.cockyclicker.model

object UpgradeRegistry {

    val tapUpgrades = listOf(
        Upgrade("sharper_beak",   "Sharper Beak",   "A razored beak for maximum impact!",  15L,       hypePerTap = 1,      category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("iron_talons",    "Iron Talons",    "Talons forged in garnet iron.",         100L,      hypePerTap = 3,      category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("feathered_fury", "Feathered Fury", "Every feather hits like a freight train.", 500L,  hypePerTap = 8,      category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("wing_slap",      "Wing Slap",      "The original smack-down.",              2_500L,   hypePerTap = 20,     category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("rooster_rage",   "Rooster Rage",   "Unbridled Gamecock fury.",              12_000L,  hypePerTap = 50,     category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("golden_spur",    "Golden Spur",    "The spur that wins championships.",     60_000L,  hypePerTap = 150,    category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("cocky_combo",    "Cocky Combo",    "Chained strikes, unstoppable hype.",    300_000L, hypePerTap = 500,    category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("legendary_crow", "Legendary Crow", "A crow heard across all of Columbia.",  1_500_000L, hypePerTap = 2_000, category = UpgradeCategory.TAP_MULTIPLIER),
        Upgrade("mythic_plumage", "Mythic Plumage", "Feathers of pure legend.",              10_000_000L, hypePerTap = 10_000, category = UpgradeCategory.TAP_MULTIPLIER),
    )

    val passiveUpgrades = listOf(
        Upgrade("tiny_chick",      "Tiny Chick",      "Small but always clucking.",           25L,         hypePerSecond = 1,      category = UpgradeCategory.AUTO_HYPE),
        Upgrade("hen_house",       "Hen House",       "A full house of hype generators.",     200L,        hypePerSecond = 5,      category = UpgradeCategory.AUTO_HYPE),
        Upgrade("roost_network",   "Roost Network",   "Connected roosts across campus.",       1_000L,      hypePerSecond = 15,     category = UpgradeCategory.AUTO_HYPE),
        Upgrade("egg_incubator",   "Egg Incubator",   "Hype hatches at industrial scale.",    5_000L,      hypePerSecond = 50,     category = UpgradeCategory.AUTO_HYPE),
        Upgrade("cluck_factory",   "Cluck Factory",   "Non-stop clucking, 24/7.",             25_000L,     hypePerSecond = 150,    category = UpgradeCategory.AUTO_HYPE),
        Upgrade("feather_mill",    "Feather Mill",    "Grinding out hype by the barrel.",     125_000L,    hypePerSecond = 500,    category = UpgradeCategory.AUTO_HYPE),
        Upgrade("rooster_arena",   "Rooster Arena",   "Williams-Brice never sleeps.",         600_000L,    hypePerSecond = 2_000,  category = UpgradeCategory.AUTO_HYPE),
        Upgrade("cocky_empire",    "Cocky Empire",    "The whole state is Gamecock country.",  3_000_000L,  hypePerSecond = 10_000, category = UpgradeCategory.AUTO_HYPE),
        Upgrade("the_cockadrome",  "The Cockadrome",  "Interdimensional Gamecock broadcast.",  20_000_000L, hypePerSecond = 50_000, category = UpgradeCategory.AUTO_HYPE),
    )

    val upgrades: List<Upgrade> get() = tapUpgrades + passiveUpgrades
}
