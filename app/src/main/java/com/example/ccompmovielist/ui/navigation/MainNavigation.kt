package com.example.ccompmovielist.ui.navigation

sealed class MainNavigation(val route: String) {
    object MainMovieScreen : MainNavigation("mainmoviescreen")

}