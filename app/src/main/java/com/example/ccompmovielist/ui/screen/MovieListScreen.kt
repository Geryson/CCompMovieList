package com.example.ccompmovielist.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ccompmovielist.ui.data.MovieGenre
import com.example.ccompmovielist.ui.data.MovieItem
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    movieListViewModel: MovieListViewModel = viewModel(),
    navController: NavHostController
) {
    var showAddMovieDialog by rememberSaveable { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    var movieToEdit: MovieItem? by rememberSaveable { mutableStateOf(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie List") },
                actions = {
                    IconButton(onClick = {
                        movieListViewModel.clearAllMovies()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                    IconButton(
                        onClick = {
                            expanded = !expanded
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                    androidx.compose.material3.DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(text = "Demo") },
                            onClick = { }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(text = "Settings") },
                            onClick = { }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddMovieDialog = true
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (showAddMovieDialog) {
                    MovieForm(
                        onDialogClose = { showAddMovieDialog = false },
                        movieListViewModel = movieListViewModel,
                        movieToEdit = movieToEdit
                    )
                }

                if (movieListViewModel.getAllMovies().isEmpty()) {
                    Text(text = "No movies to show")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(movieListViewModel.getAllMovies()) {
                            MovieCard(it,
                                onRemoveMovie = {
                                    movieListViewModel.removeMovie(it)
                                },
                                onMovieCheckChange = {
                                    checked -> movieListViewModel.changeWatchedStatus(it, checked)
                                },
                                onEditMovie = {
                                    showAddMovieDialog = true
                                    movieToEdit = it
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MovieCard(
    movie: MovieItem,
    onMovieCheckChange: (Boolean) -> Unit = {},
    onRemoveMovie: () -> Unit = {},
    onEditMovie: (MovieItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(),
            Arrangement.SpaceEvenly,
            Alignment.CenterVertically) {
            Column(
                modifier = Modifier.wrapContentHeight()
            ) {
                Text(movie.title, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(movie.description)
            }
            Spacer(modifier = Modifier.fillMaxSize(0.3f))
            Checkbox(
                checked = movie.watched,
                onCheckedChange = onMovieCheckChange,
                Modifier.scale(1.5f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.clickable { onRemoveMovie() }.size(35.dp),
                tint = Color.Red,

            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Edit",
                modifier = Modifier.clickable { onEditMovie(movie) }.size(35.dp),
                tint = Color.Blue
            )
        }
    }
}

@Composable
fun MovieForm(
    movieListViewModel: MovieListViewModel = viewModel(),
    onDialogClose: () -> Unit = {},
    movieToEdit: MovieItem? = null
) {
    var newMovieTitle by remember { mutableStateOf(movieToEdit?.title ?: "")}
    var newMovieDescription by remember { mutableStateOf(movieToEdit?.description ?: "") }
    var newMovieReleaseDate by remember { mutableStateOf(movieToEdit?.releaseDate ?: "") }
    var newMovieNote by remember { mutableStateOf(movieToEdit?.note ?: "") }
    var newMovieLink by remember { mutableStateOf(movieToEdit?.link ?: "") }
    var newMovieGenre by remember { mutableStateOf(movieToEdit?.genre ?: MovieGenre.ACTION) }

    Dialog(
        onDismissRequest = onDialogClose
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(6.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(value = newMovieTitle,
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Title") },
                        onValueChange = { newMovieTitle = it }
                    )
                    OutlinedTextField(value = newMovieDescription,
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Description") },
                        onValueChange = { newMovieDescription = it }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = newMovieGenre == MovieGenre.ACTION,
                        onCheckedChange = {if (newMovieGenre == MovieGenre.ACTION) MovieGenre.COMEDY else MovieGenre.ACTION }
                    )
                    Text(text = "Important")
                }

                Row {
                    Button(onClick = {
                        if (movieToEdit == null) {
                            movieListViewModel.addMovie(
                                MovieItem(
                                    id = UUID.randomUUID().toString(),
                                    title = newMovieTitle,
                                    description = newMovieDescription,
                                    releaseDate = newMovieReleaseDate,
                                    note = newMovieNote,
                                    link = newMovieLink,
                                    genre = newMovieGenre,
                                    watched = false
                                )
                            )
                        } else {
                            val movieEdited = movieToEdit.copy(
                                title = newMovieTitle,
                                description = newMovieDescription,
                                releaseDate = newMovieReleaseDate,
                                note = newMovieNote,
                                link = newMovieLink,
                                genre = newMovieGenre,
                                watched = false
                            )

                            movieListViewModel.editMovie(movieToEdit, movieEdited)
                        }

                        onDialogClose()

                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieCardPreview() {
    MovieCard(
        movie = MovieItem(
            id = "1",
            title = "asdfmovie",
            description = "TomSka",
            releaseDate = "2013-01-01",
            note = "Mine Turtle! Hello!",
            link = "https://www.youtube.com/watch?v=tCnj-uiRCn8",
            genre = MovieGenre.COMEDY,
            watched = true
        )
    )
}