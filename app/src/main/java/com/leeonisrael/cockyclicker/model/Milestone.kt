package com.leeonisrael.cockyclicker.model

data class Milestone(
    val id: String,
    val title: String,
    val description: String
)

object MilestoneRegistry {
    val milestones = listOf(
        Milestone("first_100",       "First 100 Clucks",      "Earn your first 100 hype"),
        Milestone("first_1k",        "Four Digits!",          "Earn 1,000 total hype"),
        Milestone("first_10k",       "10K Club",              "Earn 10,000 total hype"),
        Milestone("first_100k",      "Going Viral",           "Earn 100,000 total hype"),
        Milestone("first_1m",        "1M Lifetime Hype",      "Earn 1 million total hype"),
        Milestone("first_10m",       "10M Milestone",         "Earn 10 million total hype"),
        Milestone("first_upgrade",   "First Upgrade",         "Purchase your first upgrade"),
        Milestone("ten_upgrades",    "Upgrade Collector",     "Own 10 upgrades total"),
        Milestone("twenty_upgrades", "Serious Fan",           "Own 20 upgrades total"),
        Milestone("hype_machine",    "Hype Machine",          "Reach 100 hype/sec passively"),
        Milestone("speed_clicker",   "Speed Clicker",         "Reach 50 hype/tap"),
        Milestone("first_ascension", "Ascended",              "Prestige for the first time"),
        Milestone("five_feathers",   "Feather Collector",     "Collect 5 Prestige Feathers"),
        Milestone("ten_feathers",    "Legendary Rooster",     "Collect 10 Prestige Feathers"),
        Milestone("legendary_crow",  "Legendary Crow",        "Purchase the Legendary Crow upgrade"),
        Milestone("mythic_plumage",  "Mythic Plumage",        "Purchase the Mythic Plumage upgrade"),
        Milestone("cockadrome",      "The Cockadrome",        "Purchase The Cockadrome upgrade"),
    )
}
