import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/movies",
});

export const getMovies = () => api.get("");        // NOT "/"
export const addMovie = (movie) => api.post("", movie);
export const deleteMovie = (id) => api.delete(`/${id}`);