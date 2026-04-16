package com.example.moviewatchlistapp.service;

import com.example.moviewatchlistapp.model.Movie;
import java.util.List;


public interface MovieService {
    List<Movie> getAllMovies();
    Movie addMovie(Movie movie);
    void deleteMovie(Long id);
    Movie updateMovie(Long id, Movie movie);
}
