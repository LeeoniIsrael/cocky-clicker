package com.leeonisrael.cockyclicker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.model.Upgrade
import com.leeonisrael.cockyclicker.model.UpgradeRegistry
import com.leeonisrael.cockyclicker.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToLong

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        startAutoHypeTicker()
    }

    private fun startAutoHypeTicker() {
        viewModelScope.launch {
            while (true) {
                delay(Constants.AUTO_HYPE_INTERVAL_MS)
                val hps = calculateHypePerSecond()
                if (hps > 0) {
                    _gameState.update {
                        it.copy(
                            totalHype = it.totalHype + hps,
                            totalHypeEarned = it.totalHypeEarned + hps
                        )
                    }
                }
            }
        }
    }

    fun onTap() {
        val hpt = calculateHypePerTap()
        _gameState.update {
            it.copy(
                totalHype = it.totalHype + hpt,
                totalHypeEarned = it.totalHypeEarned + hpt
            )
        }
    }

    fun buyUpgrade(upgrade: Upgrade) {
        val currentOwned = _gameState.value.ownedUpgrades.getOrDefault(upgrade.id, 0)
        val cost = calculateUpgradeCost(upgrade, currentOwned)

        if (_gameState.value.totalHype >= cost) {
            _gameState.update {
                it.copy(
                    totalHype = it.totalHype - cost,
                    ownedUpgrades = it.ownedUpgrades + (upgrade.id to currentOwned + 1)
                )
            }
        }
    }

    fun calculateUpgradeCost(upgrade: Upgrade, ownedCount: Int): Long {
        return (upgrade.baseCost * Constants.COST_MULTIPLIER.pow(ownedCount)).roundToLong()
    }

    fun calculateHypePerTap(): Long {
        val bonus = UpgradeRegistry.upgrades
            .filter { it.hypePerTap > 0 }
            .sumOf { upgrade ->
                val owned = _gameState.value.ownedUpgrades.getOrDefault(upgrade.id, 0)
                owned * upgrade.hypePerTap
            }
        return Constants.BASE_HYPE_PER_TAP + bonus
    }

    fun calculateHypePerSecond(): Long {
        return UpgradeRegistry.upgrades
            .filter { it.hypePerSecond > 0 }
            .sumOf { upgrade ->
                val owned = _gameState.value.ownedUpgrades.getOrDefault(upgrade.id, 0)
                owned * upgrade.hypePerSecond
            }
    }
}
