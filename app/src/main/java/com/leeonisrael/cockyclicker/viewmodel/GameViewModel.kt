package com.leeonisrael.cockyclicker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leeonisrael.cockyclicker.model.GameState
import com.leeonisrael.cockyclicker.model.Upgrade
import com.leeonisrael.cockyclicker.model.UpgradeRegistry
import com.leeonisrael.cockyclicker.util.Constants
import com.leeonisrael.cockyclicker.util.SoundManager
import kotlinx.coroutines.Job
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

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("cocky_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _offlineHypeReceived = MutableStateFlow<Long?>(null)
    val offlineHypeReceived: StateFlow<Long?> = _offlineHypeReceived.asStateFlow()

    val soundManager = SoundManager(application)

    private var tickerJob: Job? = null
    private var autoHypeJob: Job? = null

    init {
        loadProgress()
    }

    // ─── Lifecycle ────────────────────────────────────────────────────────────

    fun resumeGame() {
        calculateOfflineProgress()
        startStatsTicker()
        startAutoHypeTicker()
    }

    fun pauseAndSave() {
        tickerJob?.cancel()
        autoHypeJob?.cancel()
        _gameState.update { it.copy(lastKnownTime = System.currentTimeMillis()) }
        prefs.edit().putString("game_state", gson.toJson(_gameState.value)).commit()
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }

    // ─── Persistence ──────────────────────────────────────────────────────────

    fun loadProgress() {
        val json = prefs.getString("game_state", null)
        if (json != null) {
            try {
                val type = object : TypeToken<GameState>() {}.type
                val loaded: GameState = gson.fromJson(json, type)
                val sanitizedUpgrades = loaded.ownedUpgrades
                    .mapValues { (_, v) -> (v as? Number)?.toInt() ?: 0 }

                @Suppress("UNCHECKED_CAST")
                val sanitizedMilestones: Set<String> =
                    (loaded.completedMilestones as? Set<*>)
                        ?.filterIsInstance<String>()
                        ?.toSet()
                        ?: emptySet()

                _gameState.value = loaded.copy(
                    ownedUpgrades = sanitizedUpgrades,
                    completedMilestones = sanitizedMilestones,
                    prestigeFeathers = loaded.prestigeFeathers.coerceAtLeast(0)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        soundManager.isMuted = _gameState.value.isMuted
        resumeGame()
    }

    // ─── Tickers ──────────────────────────────────────────────────────────────

    private fun startAutoHypeTicker() {
        autoHypeJob?.cancel()
        autoHypeJob = viewModelScope.launch {
            while (isActive) {
                delay(Constants.AUTO_HYPE_INTERVAL_MS)
                val hps = calculateHypePerSecond()
                val hpt = calculateHypePerTap()
                if (hps > 0) {
                    _gameState.update {
                        it.copy(
                            totalHype = it.totalHype + hps,
                            totalHypeEarned = it.totalHypeEarned + hps,
                            sessionHypeEarned = it.sessionHypeEarned + hps,
                            hypePerSecond = hps,
                            hypePerTap = hpt
                        )
                    }
                    checkMilestones()
                } else {
                    _gameState.update {
                        it.copy(
                            hypePerSecond = hps,
                            hypePerTap = hpt
                        )
                    }
                }
            }
        }
    }

    private fun startStatsTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _gameState.update { it.copy(totalPlayTime = it.totalPlayTime + 1000) }
            }
        }
    }

    // ─── Offline progress ─────────────────────────────────────────────────────

    private fun calculateOfflineProgress() {
        val state = _gameState.value
        val elapsed = System.currentTimeMillis() - state.lastKnownTime
        if (elapsed <= 0) return

        val seconds = elapsed / 1000.0
        val hps = calculateHypePerSecond()
        val gained = calculateOfflineHype(seconds, hps)
        if (gained > 0) {
            _gameState.update {
                it.copy(
                    totalHype = it.totalHype + gained,
                    totalHypeEarned = it.totalHypeEarned + gained,
                    sessionHypeEarned = it.sessionHypeEarned + gained,
                    lastKnownTime = System.currentTimeMillis()
                )
            }
            _offlineHypeReceived.value = gained
        }
    }

    fun calculateOfflineHype(secondsAway: Double, hypePerSecond: Long, scaleFactor: Double = 100.0): Long {
        if (secondsAway <= 0) return 0
        val cappedSeconds = secondsAway.coerceAtMost(Constants.MAX_OFFLINE_SECONDS)
        return (hypePerSecond * scaleFactor * ln(cappedSeconds + 1.0)).toLong()
    }

    fun dismissOfflineHype() {
        _offlineHypeReceived.value = null
    }

    // ─── Core actions ─────────────────────────────────────────────────────────

    fun onTap() {
        val hpt = calculateHypePerTap()
        val hps = calculateHypePerSecond()
        _gameState.update {
            it.copy(
                totalHype = it.totalHype + hpt,
                totalHypeEarned = it.totalHypeEarned + hpt,
                sessionHypeEarned = it.sessionHypeEarned + hpt,
                totalTaps = it.totalTaps + 1,
                hypePerTap = hpt,
                hypePerSecond = hps
            )
        }
        soundManager.playTap()
        checkMilestones()
    }

    fun buyUpgrade(upgrade: Upgrade) {
        val owned = _gameState.value.ownedUpgrades[upgrade.id] ?: 0
        val cost = calculateUpgradeCost(upgrade, owned)
        if (_gameState.value.totalHype >= cost) {
            _gameState.update {
                val newHpt = calculateHypePerTap()
                val newHps = calculateHypePerSecond()
                it.copy(
                    totalHype = it.totalHype - cost,
                    ownedUpgrades = it.ownedUpgrades + (upgrade.id to owned + 1),
                    totalUpgradesPurchased = it.totalUpgradesPurchased + 1,
                    hypePerTap = newHpt,
                    hypePerSecond = newHps
                )
            }
            soundManager.playUpgrade()
            checkMilestones()
        }
    }

    fun ascend() {
        val state = _gameState.value
        if (!state.canAscend) return
        val feathers = state.ascendFeathersAvailable
        _gameState.update { current ->
            GameState(
                totalHype = 0,
                totalHypeEarned = 0,
                sessionHypeEarned = 0,
                totalTaps = current.totalTaps,
                totalUpgradesPurchased = 0,
                ownedUpgrades = emptyMap(),
                lastKnownTime = System.currentTimeMillis(),
                totalPlayTime = current.totalPlayTime,
                prestigeFeathers = current.prestigeFeathers + feathers,
                completedMilestones = current.completedMilestones,
                isMuted = current.isMuted
            )
        }
        soundManager.isMuted = _gameState.value.isMuted
        soundManager.playPrestige()
        checkMilestones()
        pauseAndSave()
    }

    fun toggleMute() {
        _gameState.update { it.copy(isMuted = !it.isMuted) }
        soundManager.isMuted = _gameState.value.isMuted
    }

    fun updatePlayTime(sessionMillis: Long) {
        _gameState.update {
            it.copy(
                totalPlayTime = it.totalPlayTime + sessionMillis,
                lastKnownTime = System.currentTimeMillis()
            )
        }
    }

    // ─── Calculations ─────────────────────────────────────────────────────────

    fun calculateUpgradeCost(upgrade: Upgrade, ownedCount: Int): Long =
        (upgrade.baseCost * Constants.COST_MULTIPLIER.pow(ownedCount)).roundToLong()

    fun calculateHypePerTap(): Long {
        val state = _gameState.value
        val bonus = UpgradeRegistry.tapUpgrades.sumOf { upgrade ->
            val owned = state.ownedUpgrades[upgrade.id] ?: 0
            owned * upgrade.hypePerTap
        }
        return ((Constants.BASE_HYPE_PER_TAP + bonus) * state.prestigeMultiplier).toLong()
    }

    fun calculateHypePerSecond(): Long {
        val state = _gameState.value
        val base = UpgradeRegistry.passiveUpgrades.sumOf { upgrade ->
            val owned = state.ownedUpgrades[upgrade.id] ?: 0
            owned * upgrade.hypePerSecond
        }
        return (base * state.prestigeMultiplier).toLong()
    }

    // ─── Milestones ───────────────────────────────────────────────────────────

    private fun checkMilestones() {
        val state = _gameState.value
        val hpt = calculateHypePerTap()
        val hps = calculateHypePerSecond()
        val totalOwned = state.ownedUpgrades.values.sum()
        val completed = state.completedMilestones
        val newly = mutableSetOf<String>()

        fun check(id: String, cond: Boolean) { if (cond && id !in completed) newly.add(id) }

        check("first_100",       state.totalHypeEarned >= 100)
        check("first_1k",        state.totalHypeEarned >= 1_000)
        check("first_10k",       state.totalHypeEarned >= 10_000)
        check("first_100k",      state.totalHypeEarned >= 100_000)
        check("first_1m",        state.totalHypeEarned >= 1_000_000)
        check("first_10m",       state.totalHypeEarned >= 10_000_000)
        check("first_upgrade",   totalOwned >= 1)
        check("ten_upgrades",    totalOwned >= 10)
        check("twenty_upgrades", totalOwned >= 20)
        check("hype_machine",    hps >= 100)
        check("speed_clicker",   hpt >= 50)
        check("first_ascension", state.prestigeFeathers >= 1)
        check("five_feathers",   state.prestigeFeathers >= 5)
        check("ten_feathers",    state.prestigeFeathers >= 10)
        check("legendary_crow",  (state.ownedUpgrades["legendary_crow"] ?: 0) >= 1)
        check("mythic_plumage",  (state.ownedUpgrades["mythic_plumage"] ?: 0) >= 1)
        check("cockadrome",      (state.ownedUpgrades["the_cockadrome"] ?: 0) >= 1)

        if (newly.isNotEmpty()) {
            _gameState.update { it.copy(completedMilestones = it.completedMilestones + newly) }
            soundManager.playMilestone()
        }
    }
}
