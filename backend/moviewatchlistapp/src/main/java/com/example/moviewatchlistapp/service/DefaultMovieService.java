package com.example.moviewatchlistapp.service;

import org.springframework.stereotype.Service;
import com.example.moviewatchlistapp.model.Movie;
import java.util.List;
import com.example.moviewatchlistapp.repository.MovieRepository;

@Service
public class DefaultMovieService implements MovieService {
    private final MovieRepository movieRepository;

    public DefaultMovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        // Implementation to retrieve all movies from the database
        return movieRepository.findAll();
    }

    @Override
    public Movie addMovie(Movie movie) {
        // Implementation to add a new movie to the database
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        // Implementation to delete a movie from the database by its ID
        movieRepository.deleteById(id);
    }
    
}
