package com.leeonisrael.cockyclicker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.model.Upgrade
import com.leeonisrael.cockyclicker.model.UpgradeRegistry
import com.leeonisrael.cockyclicker.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToLong

class GameViewModel(application : Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("cocky_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val _gameState = MutableStateFlow(GameState())
    private val _offlineHypeReceived = MutableStateFlow<Long?>(null)
    val offlineHypeReceived: StateFlow<Long?> = _offlineHypeReceived.asStateFlow()
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var tickerJob: Job? = null

    init {
        loadProgress()
    }

    fun resumeGame() {
        calculateOfflineProgress()
        startStatsTicker()
        startAutoHypeTicker()
    }

    fun pauseAndSave() {
        tickerJob?.cancel()
        autoHypeJob?.cancel()
        _gameState.update { it.copy(lastKnownTime = System.currentTimeMillis()) }
        val json = gson.toJson(_gameState.value)
        prefs.edit().putString("game_state", json).commit()
    }
    fun loadProgress() {
        val gameStateJson = prefs.getString("game_state", null)
        if (gameStateJson != null) {
            try {
                val type = object: TypeToken<GameState>() {}.type
                val loadedState: GameState = gson.fromJson(gameStateJson, type)
                val sanitizedUpgrades = loadedState.ownedUpgrades.mapValues { entry -> (entry.value as Number).toInt() }
                _gameState.value = loadedState.copy(ownedUpgrades = sanitizedUpgrades)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
            resumeGame()
        }
    private var autoHypeJob: Job? = null
    private fun startAutoHypeTicker() {
        autoHypeJob?.cancel()
        autoHypeJob = viewModelScope.launch {
            while (isActive) {
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

    private fun calculateOfflineProgress() {
        val currentState = _gameState.value
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - currentState.lastKnownTime

        if (timeDifference > 0) {
            val secondsAway = timeDifference / 1000.0
            val hypePerSecond = calculateHypePerSecond()
            val offlineHype = calculateOfflineHype(secondsAway, hypePerSecond)
            if (offlineHype > 0) {
                _gameState.update {
                    it.copy(
                        totalHype = it.totalHype + offlineHype,
                        totalHypeEarned = it.totalHypeEarned + offlineHype,
                        lastKnownTime = currentTime
                    )
                }
                _offlineHypeReceived.value = offlineHype
            }
        }
    }

    private fun startStatsTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _gameState.update {
                    it.copy(totalPlayTime = it.totalPlayTime + 1000)
                }
            }
        }
    }

    fun calculateOfflineHype(
        secondsAway: Double,
        hypePerSecond: Long,
        scaleFactor: Double = 3600.0 //Adjust to find sweet spot
    ): Long {
        if (secondsAway<= 0) return 0

        return (hypePerSecond * scaleFactor * ln(secondsAway + 1.0)).toLong()
    }

    fun updatePlayTime(sessionMillis: Long) {
        _gameState.update {
            it.copy(
                totalPlayTime = it.totalPlayTime + sessionMillis,
                lastKnownTime = System.currentTimeMillis()

            )
        }
    }

    fun dismissOfflineHype() {
        _offlineHypeReceived.value = null
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
        val currentOwned = (_gameState.value.ownedUpgrades[upgrade.id] as? Number)?.toInt() ?: 0
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
                val owned = (_gameState.value.ownedUpgrades[upgrade.id] as? Number)?.toInt() ?: 0
                owned * upgrade.hypePerTap
            }
        return Constants.BASE_HYPE_PER_TAP + bonus
    }

    fun calculateHypePerSecond(): Long {
        return UpgradeRegistry.upgrades
            .filter { it.hypePerSecond > 0 }
            .sumOf { upgrade ->
                val owned = (_gameState.value.ownedUpgrades[upgrade.id] as? Number)?.toInt() ?: 0
                owned * upgrade.hypePerSecond
            }
    }
}
