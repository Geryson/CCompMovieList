package com.example.ccompmovielist.ui.data

data class MovieItem(
    val id: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val note: String,
    val genre: MovieGenre,
    val watched: Boolean
)

enum class MovieGenre {
    ACTION,
    ADVENTURE,
    ANIMATION,
    COMEDY,
    SCI_FI,
}
