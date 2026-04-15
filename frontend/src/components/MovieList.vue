<template>
  <div class="watchlist-wrapper">

    <div class="watchlist-card">

      <!-- BRAND -->
      <div class="brand">
        <span class="brand-icon">🎬</span>
        <span class="brand-name">Movie Watchlist</span>
      </div>

      <!-- FORM -->
      <form class="movie-form" @submit.prevent="createMovie">

        <div class="field">
          <label>Title</label>
          <input v-model="newMovie.title" type="text" placeholder="Movie title" required />
        </div>

        <div class="field">
          <label>Genre</label>
          <input v-model="newMovie.genre" type="text" placeholder="Action, Drama..." required />
        </div>

        <div class="row">
          <div class="field">
            <label>Year</label>
            <input v-model.number="newMovie.releaseYear" type="number" min="1900" max="2100" step="1" placeholder="YYYY" required />
          </div>

          <div class="field">
            <label>Rating</label>
            <input v-model.number="newMovie.rating" type="number" min="1" max="5" placeholder="1-5" required />
          </div>
        </div>

        <button type="submit" class="submit-btn">Add Movie</button>

      </form>

      <!-- LIST -->
      <div class="divider"></div>

      <div v-if="movies.length === 0" class="empty">
        No movies yet 🎥
      </div>

      <div v-for="movie in movies" :key="movie.id" class="movie-card">
        <div class="movie-header">
          <h3>{{ movie.title }}</h3>
          <button class="delete-btn" @click="removeMovie(movie.id)">✕</button>
        </div>

        <p class="meta">
          {{ movie.genre }} • {{ movie.releaseYear }}
        </p>

        <p class="rating">⭐ {{ movie.rating }}/5</p>
      </div>

    </div>

  </div>
</template>

<script>
import { getMovies, addMovie, deleteMovie } from '../services/api';

export default {
  data() {
    return {
      movies: [],
      newMovie: {
        title: '',
        genre: '',
        releaseYear: null,
        rating: null,
      },
    };
  },

  mounted() {
    this.loadMovies();
  },

  methods: {
    async loadMovies() {
      const res = await getMovies();
      this.movies = res.data;
    },

    async createMovie() {
      try {
        await addMovie(this.newMovie);
        this.newMovie = { title: '', genre: '', releaseYear: null, rating: null };
        await this.loadMovies();
      } catch (err) {
        console.log(err);
      }
    },

    async removeMovie(id) {
      await deleteMovie(id);
      await this.loadMovies();
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500&family=DM+Serif+Display&display=swap');

.watchlist-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #0e0e11;
  font-family: 'DM Sans', sans-serif;
}

.watchlist-card {
  width: 420px;
  background: #17171c;
  border: 1px solid #2a2a33;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 24px 64px rgba(0,0,0,0.5);
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
}

.brand-icon {
  width: 34px;
  height: 34px;
  background: #e8ff47;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0e0e11;
  font-size: 16px;
}

.brand-name {
  font-family: 'DM Serif Display', serif;
  font-size: 20px;
  color: #f0f0f0;
}

.movie-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label {
  font-size: 12px;
  color: #888;
  text-transform: uppercase;
}

input {
  padding: 10px 12px;
  background: #0e0e11;
  border: 1px solid #2a2a33;
  border-radius: 9px;
  color: #f0f0f0;
  outline: none;
}

input:focus {
  border-color: #e8ff47;
}

.row {
  display: flex;
  gap: 10px;
}

.row .field {
  flex: 1;
}

.submit-btn {
  margin-top: 6px;
  padding: 11px;
  background: #e8ff47;
  border: none;
  border-radius: 9px;
  color: #0e0e11;
  font-weight: 500;
  cursor: pointer;
}

.submit-btn:hover {
  opacity: 0.9;
}

.divider {
  margin: 18px 0;
  height: 1px;
  background: #2a2a33;
}

.empty {
  text-align: center;
  color: #777;
  font-size: 13px;
  padding: 10px 0;
}

.movie-card {
  background: #0e0e11;
  border: 1px solid #2a2a33;
  border-radius: 12px;
  padding: 12px 14px;
  margin-bottom: 10px;
  transition: transform 0.15s;
}

.movie-card:hover {
  transform: translateY(-2px);
}

.movie-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.movie-header h3 {
  margin: 0;
  font-size: 15px;
  color: #f0f0f0;
}

.delete-btn {
  background: none;
  border: none;
  color: #ff6b6b;
  cursor: pointer;
  font-size: 14px;
}

.meta {
  color: #888;
  font-size: 12px;
  margin: 6px 0 0;
}

.rating {
  color: #e8ff47;
  font-size: 13px;
  margin-top: 6px;
}
</style>