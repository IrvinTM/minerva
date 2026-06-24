package com.uesopeneel.minervaapp.navigation

sealed class Screen {
  object Login : Screen()
  object Dashboard : Screen()
}
