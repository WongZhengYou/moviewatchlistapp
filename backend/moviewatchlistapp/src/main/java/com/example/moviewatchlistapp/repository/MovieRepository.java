package com.example.moviewatchlistapp.repository;
import com.example.moviewatchlistapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    
}
