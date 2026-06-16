package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.theme.MinervaTheme
import com.example.ui.viewmodel.PortalViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MinervaTheme {
        // Simple surface to background-fill window edges
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
          val portalViewModel: PortalViewModel = viewModel()
          val currentScreen by portalViewModel.currentScreen.collectAsState()

          // Elegant crossfade transition between portals
          Crossfade(
            targetState = currentScreen,
            animationSpec = tween(durationMillis = 400),
            label = "portal_screen_crossfade"
          ) { screen ->
            when (screen) {
              is Screen.Login -> LoginScreen(viewModel = portalViewModel)
              is Screen.Dashboard -> DashboardScreen(viewModel = portalViewModel)
            }
          }
        }
      }
    }
  }
}
