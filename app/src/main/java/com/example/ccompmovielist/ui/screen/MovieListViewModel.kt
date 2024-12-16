package com.example.ccompmovielist.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.ccompmovielist.ui.data.MovieItem

class MovieListViewModel : ViewModel() {
    private val _movieList = mutableStateListOf<MovieItem>()

    fun getAllMovies(): List<MovieItem> {
        return _movieList
    }
    fun addMovie(movie: MovieItem) {
        _movieList.add(movie)
    }
    fun removeMovie(movie: MovieItem) {
        _movieList.remove(movie)
    }
    fun changeWatchedStatus(movie: MovieItem, watched: Boolean) {
        val index = _movieList.indexOf(movie)

        val newMovie = movie.copy(
            title = movie.title,
            description = movie.description,
            releaseDate = movie.releaseDate,
            note = movie.note,
            genre = movie.genre,
            watched = watched
        )

        _movieList[index] = newMovie
    }
}