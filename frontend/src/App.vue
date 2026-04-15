<template>
  <div>
    <Login v-if="!isLoggedIn" @logged-in="handleLogin" />

    <MovieList v-else />
  </div>
</template>

<script>
import Login from "./components/Login.vue";
import MovieList from "./components/MovieList.vue";

export default {
  components: { Login, MovieList },

  data() {
    return {
      isLoggedIn: false,
    };
  },

  methods: {
    handleLogin(token) {
      this.isLoggedIn = true;
      localStorage.setItem("token", token);
    },
  },
};
</script>