<template>
  <div class="container">
    <h1>🎬 Movie Watchlist</h1>

    <!-- FORM -->
    <form @submit.prevent="createMovie" class="form">
      <input v-model="newMovie.title" placeholder="Title" required />
      <input v-model="newMovie.genre" placeholder="Genre" required />
      <input v-model.number="newMovie.releaseYear" placeholder="Year" required />
      <input v-model.number="newMovie.rating" placeholder="Rating (1-5)" required />

      <button type="submit">Add Movie</button>
    </form>

    <hr />

    <!-- LIST -->
    <div v-if="movies.length === 0">
      No movies yet.
    </div>

    <div v-for="movie in movies" :key="movie.id" class="card">
      <h3>{{ movie.title }}</h3>
      <p>{{ movie.genre }} | {{ movie.releaseYear }}</p>
      <p>⭐ {{ movie.rating }}</p>

      <button @click="removeMovie(movie.id)">Delete</button>
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
      try {
        const res = await getMovies();
        this.movies = res.data;
      } catch (err) {
        console.log('Load error:', err);
      }
    },

//     async createMovie() {
//   console.log("BUTTON CLICKED"); // 👈 ADD THIS

//   try {
//     console.log("DATA SENT:", this.newMovie); // 👈 ADD THIS

//     await addMovie(this.newMovie);

//     console.log("REQUEST SENT SUCCESS"); // 👈 ADD THIS

//     this.newMovie = {
//       title: '',
//       genre: '',
//       releaseYear: null,
//       rating: null,
//     };

//     await this.loadMovies();
//   } catch (err) {
//     console.log('Add error:', err);
//   }
async createMovie() {
  alert("CLICK WORKS");
  console.log("CLICK WORKS");

  try {
    console.log("DATA:", this.newMovie);
    await addMovie(this.newMovie);
    console.log("REQUEST SENT");
  } catch (err) {
    console.log("ERROR:", err);
  }
},

    async removeMovie(id) {
      try {
        await deleteMovie(id);
        await this.loadMovies();
      } catch (err) {
        console.log('Delete error:', err);
      }
    },
  },
};
</script>

<style>
.container {
  max-width: 600px;
  margin: auto;
  font-family: Arial;
}

.form input {
  display: block;
  margin: 5px 0;
  padding: 8px;
  width: 100%;
}

button {
  margin-top: 10px;
  padding: 8px 12px;
  cursor: pointer;
}

.card {
  border: 1px solid #ddd;
  padding: 10px;
  margin-top: 10px;
}
</style>