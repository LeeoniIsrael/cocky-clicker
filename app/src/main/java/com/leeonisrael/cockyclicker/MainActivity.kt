package com.leeonisrael.cockyclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.resumeGame()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.pauseAndSave()
                }
                else -> {}
            }
            })

        enableEdgeToEdge()
        setContent {
            CockyClickerApp()
        }
    }
}
