<template>
  <div class="watchlist-wrapper">
    <div class="watchlist-card">

      <!-- HEADER -->
      <div class="header">
        <div class="header-icon">🎬</div>
        <div class="header-text">
          <h1>Watchlist</h1>
          <p>Track movies you want to see</p>
        </div>
      </div>

      <!-- FORM -->
      <form class="movie-form" @submit.prevent="editingId? handleUpdate(): createMovie()">
        <div class="form-card" :class="{ editing: editingId !== null }">
          <div class="form-title" :class="{ 'editing-label': editingId !== null }">
            {{ editingId !== null ? 'Editing movie' : 'Add a movie' }}
          </div>
          <div class="fields">
            <div class="field">
              <label>Title</label>
              <input v-model="newMovie.title" type="text" placeholder="e.g. Oppenheimer" required />
            </div>

            <div class="field">
              <label>Genre</label>
              <input v-model="newMovie.genre" type="text" placeholder="e.g. Drama, Sci-Fi" required />
            </div>

            <div class="row">
              <div class="field">
                <label>Year</label>
                <input v-model.number="newMovie.releaseYear" type="number" min="1900" max="2100" placeholder="2024" required />
              </div>

              <div class="field">
                <label>Rating</label>
                <input v-model.number="newMovie.rating" type="number" min="1" max="5" placeholder="4" required />
              </div>
            </div>
            <div class="form-actions">
              <button class="btn-submit" type="submit">
                {{ editingId !== null ? 'Save Changes' : 'Add Movie' }}
              </button>
              <button v-if="editingId !== null" class="btn-cancel" @click="cancelEdit">Cancel</button>
            </div>
          </div>
        </div>
      </form>

      <!-- LIST -->
      <div class="list-header">
        <span class="list-label">Your movies</span>
        <span class="count-badge">{{ movies.length }} {{ movies.length === 1 ? 'film' : 'films' }}</span>
      </div>

      <div v-if="movies.length === 0" class="empty">
        <div class="empty-icon">🎥</div>
        <div>No movies yet — add your first one!</div>
      </div>

      <div
        v-for="movie in movies"
        :key="movie.id"
        class="movie-card"
        :class="{ 'is-editing': editingId === movie.id }"
      >
        <div class="movie-poster">{{ genreIcon(movie.genre) }}</div>
        <div class="movie-info">
          <div class="movie-top">
            <span class="movie-title">{{ movie.title }}</span>
            <div class="movie-actions">
              <button class="action-btn edit" @click="startEdit(movie)" title="Edit">✏️</button>
              <button class="action-btn delete" @click="removeMovie(movie.id)" title="Delete">✕</button>
            </div>
          </div>
          <div class="movie-meta">
            <span class="genre-tag">{{ movie.genre }}</span>
            <span class="year-tag">{{ movie.releaseYear }}</span>
          </div>
          <div class="stars">{{ stars(movie.rating) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getMovies, addMovie, deleteMovie, updateMovie as updateMovieApi } from '../services/api';

const GENRE_ICONS = {
  action: '💥', comedy: '😂', drama: '🎭', horror: '👻',
  'sci-fi': '🚀', scifi: '🚀', romance: '💕', thriller: '🔪',
  animation: '✨', documentary: '📽', fantasy: '🧙',
};

export default {
  data() {
    return {
      movies: [],
      editingId: null,
      newMovie: { title: '', genre: '', releaseYear: null, rating: null },
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
      if (!this.newMovie.title || !this.newMovie.genre) return;
      try {
        await addMovie(this.newMovie);
        this.resetForm();
        await this.loadMovies();
      } catch (err) {
        console.error('Create error:', err);
      }
    },

    async removeMovie(id) {
      if (this.editingId === id) this.cancelEdit();
      try {
        await deleteMovie(id);
        await this.loadMovies();
      } catch (err) {
        console.error('Delete error:', err);
      }
    },

    startEdit(movie) {
      this.editingId = movie.id;
      this.newMovie = {
        title: movie.title,
        genre: movie.genre,
        releaseYear: movie.releaseYear,
        rating: movie.rating,
      };
    },

    async handleUpdate() {
      try {
        await updateMovieApi(this.editingId, this.newMovie); // fixed: was calling itself recursively
        this.cancelEdit();
        await this.loadMovies();
      } catch (err) {
        console.error('Update error:', err);
      }
    },

    cancelEdit() {
      this.editingId = null;
      this.resetForm();
    },

    resetForm() {
      this.newMovie = { title: '', genre: '', releaseYear: null, rating: null };
    },

    genreIcon(genre) {
      if (!genre) return '🎞';
      const key = genre.toLowerCase().split(',')[0].trim();
      return GENRE_ICONS[key] || '🎞';
    },

    stars(rating) {
      const r = Math.min(5, Math.max(1, parseInt(rating) || 0));
      return '★'.repeat(r) + '☆'.repeat(5 - r);
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,wght@0,300;0,400;0,500;1,300&display=swap');

.watchlist-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  padding: 32px 16px;
  font-family: 'DM Sans', sans-serif;
  color: #f0eff5;

  background:
    radial-gradient(circle at top, #1a1a2e 0%, transparent 50%),
    radial-gradient(circle at bottom, #0c0c0f 0%, #000 100%);
}

.watchlist-card {
  width: 600px;
  background: rgba(20, 20, 26, 0.85);
  backdrop-filter: blur(10px);
  border: 1px solid #2a2a33;
  border-radius: 18px;
  padding: 28px;
  box-shadow: 0 30px 80px rgba(0,0,0,0.7);
}

/* HEADER */
.header {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  margin-bottom: 24px;
}
.header-icon {
  background: #e50914;
  color: white;
}
.header-text h1 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 28px;
  letter-spacing: 1px;
  line-height: 1;
}
.header-text p {
  font-size: 12px;
  color: #6b6a7a;
  margin-top: 2px;
}

/* FORM */
.form-card {
  background: #14141a;
  border: 1px solid #222230;
  border-radius: 14px;
  padding: 30px;
  margin-bottom: 24px;
  transition: border-color 0.2s;
}
.form-card.editing { border-color: #f5c842; }

.form-title {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  color: #6b6a7a;
  margin-bottom: 14px;
}
.form-title.editing-label { color: #f5c842; }

.fields { display: flex; flex-direction: column; gap: 10px; }
.row {
  display: flex;
  gap: 24px;
}

.row .field {
  flex: 1;
  min-width: 0;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;

  padding: 10px;
  border: 1px solid #222230;
  border-radius: 10px;
  background: #14141a;
}

label {
  font-size: 11px;
  color: #6b6a7a;
  text-transform: uppercase;
  letter-spacing: 0.8px;
}

input {
  padding: 10px 14px; 
  background: #0c0c0f;
  border: 1px solid #222230;
  border-radius: 8px;
  color: #f0eff5;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  outline: none;
  transition: border-color 0.15s;
  width: 100%;
  line-height: 1.4; 
  letter-spacing: 0.2px;
}
input:focus { border-color: #f5c842; }
input::placeholder { color: #6b6a7a; }

.form-actions { display: flex; gap: 8px; margin-top: 14px; }

.btn-submit {
  flex: 1;
  padding: 11px;
  background: #f5c842;
  border: none;
  border-radius: 8px;
  color: #0c0c0f;
  font-family: 'DM Sans', sans-serif;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
  transition: opacity 0.15s;
}
.btn-submit:hover { opacity: 0.88; }

.btn-cancel {
  padding: 11px 16px;
  background: #1e1e28;
  border: 1px solid #222230;
  border-radius: 8px;
  color: #6b6a7a;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.15s;
}
.btn-cancel:hover { color: #f0eff5; }

/* LIST HEADER */
.list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
.list-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  color: #6b6a7a;
}
.count-badge {
  background: #1e1e28;
  border: 1px solid #222230;
  border-radius: 20px;
  padding: 2px 10px;
  font-size: 12px;
  color: #6b6a7a;
}

/* EMPTY */
.empty {
  text-align: center;
  padding: 40px 20px;
  color: #6b6a7a;
  font-size: 14px;
  border: 1px dashed #222230;
  border-radius: 12px;
}
.empty-icon { font-size: 32px; margin-bottom: 8px; }

/* MOVIE CARD */
.movie-card {
  background: #14141a;
  border: 1px solid #222230;
  border-radius: 14px;
  padding: 14px 16px;
  margin-bottom: 10px;

  display: flex;
  align-items: flex-start;
  gap: 14px;

  transition: all 0.25s ease;
  cursor: pointer;
}

.movie-card:hover {
  transform: scale(1.02);
  border-color: #e50914;
  box-shadow: 0 10px 25px rgba(229, 9, 20, 0.15);
}
.movie-card.is-editing { border-color: #f5c842; }

.movie-poster {
  width: 45px;
  height: 65px;
  background: linear-gradient(135deg, #2a2a33, #1e1e28);
  border: 1px solid #2a2a33;
  border-radius: 8px;

  display: grid;
  place-items: center;
  font-size: 20px;
}

.movie-info { flex: 1; min-width: 0; }

.movie-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
}
.movie-title {
  font-weight: 500;
  font-size: 15px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.movie-actions { display: flex; gap: 4px; flex-shrink: 0; }
.action-btn {
  width: 28px;
  height: 28px;
  border-radius: 7px;
  border: 1px solid #222230;
  background: #1e1e28;
  cursor: pointer;
  display: grid;
  place-items: center;
  font-size: 12px;
  transition: background 0.15s, border-color 0.15s;
}
.action-btn.edit:hover  { background: rgba(245,200,66,0.1); border-color: #f5c842; }
.action-btn.delete:hover { background: rgba(255,94,94,0.12); border-color: #ff5e5e; }

.movie-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 5px;
  flex-wrap: wrap;
}
.genre-tag,
.year-tag {
  background: rgba(229, 9, 20, 0.1);
  border: 1px solid rgba(229, 9, 20, 0.2);
  color: #ffb3b3;

  padding: 3px 10px;
  border-radius: 999px;
  font-size: 11px;
}
.stars {
  margin-top: 6px;
  font-size: 12px;
  color: #e50914;
  letter-spacing: 2px;
}
</style>
