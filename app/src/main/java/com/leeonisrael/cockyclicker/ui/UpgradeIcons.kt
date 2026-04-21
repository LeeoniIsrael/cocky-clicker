package com.leeonisrael.cockyclicker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.ui.graphics.vector.ImageVector

object UpgradeIcons {
    fun getIcon(upgradeId: String): ImageVector = when (upgradeId) {
        // Tap upgrades
        "sharper_beak"   -> Icons.Default.Create
        "iron_talons"    -> Icons.Default.Build
        "feathered_fury" -> Icons.Default.Bolt
        "wing_slap"      -> Icons.Default.Air
        "rooster_rage"   -> Icons.Default.LocalFireDepartment
        "golden_spur"    -> Icons.Default.EmojiEvents
        "cocky_combo"    -> Icons.Default.Repeat
        "legendary_crow" -> Icons.Default.RecordVoiceOver
        "mythic_plumage" -> Icons.Default.AutoAwesome
        // Passive upgrades
        "tiny_chick"     -> Icons.Default.ChildCare
        "hen_house"      -> Icons.Default.Home
        "roost_network"  -> Icons.Default.Hub
        "egg_incubator"  -> Icons.Default.Biotech
        "cluck_factory"  -> Icons.Default.Factory
        "feather_mill"   -> Icons.Default.Pets
        "rooster_arena"  -> Icons.Default.Stadium
        "cocky_empire"   -> Icons.Default.AccountBalance
        "the_cockadrome" -> Icons.Default.Public
        else             -> Icons.Default.AutoAwesome
    }
}
