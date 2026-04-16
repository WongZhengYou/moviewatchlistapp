import axios from "axios";

const API = "http://localhost:8080";

const api = axios.create({
  baseURL: API,
});

// attach token automatically
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export const login = (data) => api.post("/auth/login", data);
export const register = (data) => api.post("/auth/register", data);

export const getMovies = () => api.get("/api/movies");
export const addMovie = (movie) => api.post("/api/movies", movie);
export const deleteMovie = (id) => api.delete(`/api/movies/${id}`);
export const updateMovie = (id,movie) => api.put(`/api/movies/${id}`,movie);