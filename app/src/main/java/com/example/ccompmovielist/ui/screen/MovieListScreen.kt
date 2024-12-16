package com.example.ccompmovielist.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun MovieListScreen(movieListViewModel: MovieListViewModel = viewModel(),
                    navController: NavHostController) {
    Text(text = "Movie list")
}