# Cocky Clicker 🐓🤙

A University of South Carolina-themed idle/clicker game built natively for Android using **Kotlin** and **Jetpack Compose**.

## 🚀 Features

* **Tap & Idle Mechanics:** Generate "Hype" manually by tapping, or build passive Hype-per-second (HPS) through upgrades.
* **Dynamic Upgrade System:** Purchase tap and passive upgrades. Costs scale exponentially based on ownership.
* **Offline Progression:** Calculates time away and awards Hype earned while the app is closed.
* **Prestige System (Ascension):** Reset your progress in exchange for "Prestige Feathers," which provide permanent global multipliers to all future Hype generation.
* **Milestone Tracking:** An achievement system that tracks lifetime stats, total playtime, and unlocks based on gameplay thresholds.
* **Low-Latency Audio:** Integrates Android's `SoundPool` API for rapid-fire, overlapping sound effects without thread-blocking or UI lag.
* **Robust State Persistence:** Utilizes `SharedPreferences` and `Gson` serialization to ensure game state survives Android process death, with strict type-safety checks to prevent data corruption.

## 🛠️ Tech Stack & Architecture

* **UI Framework:** Jetpack Compose (100% declarative UI)
* **Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF)
* **Concurrency:** Kotlin Coroutines & `StateFlow` (for asynchronous tickers and reactive UI updates)
* **Time Tracking:** Implements Delta Time ($\Delta t$) calculations via `System.currentTimeMillis()` to prevent thread drift and ensure playtime and offline progression tracking.
* **Storage:** SharedPreferences with custom Gson TypeTokens to handle generic type erasure.

## 📦 Installation & Setup

Clone the repository:
   ```bash
   git clone (https://github.com/LeeoniIsrael/cocky-clicker.git)
  ```

## 🛠️ Setup & Installation

1. Open the project in **Android Studio** (Ladybug or newer recommended).
2. Allow Gradle to sync and download dependencies (including Gson).
3. Build and run on an Android Emulator or physical device.

*Note: If you encounter a `local.properties` or SDK path error on your first pull, go to **File > Sync Project with Gradle Files** to regenerate your local environment variables.*

## 🎵 Audio Setup

Game sound files are open-source and are taken from the following link:
https://incompetech.com/music/royalty-free/music.html

## 👥 Developers

| Name                | Email                    |
| ------------------- | ------------------------ |
| **Leeon Israel**    | leeoniisrael@gmail.com   |
| **Alex Rishmawi**   | alexrishmd1@gmail.com    |
