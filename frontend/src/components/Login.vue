<template>
  <div class="auth-box">
    <h1 v-if="mode === 'login'">Login</h1>
    <h1 v-else>Register</h1>

    <input v-model="username" placeholder="Username" />
    <input v-model="password" type="password" placeholder="Password" />

    <button v-if="mode === 'login'" @click="loginUser">
      Login
    </button>

    <button v-else @click="registerUser">
      Register
    </button>

    <p>{{ message }}</p>

    <button @click="toggleMode">
      Switch to {{ mode === 'login' ? 'Register' : 'Login' }}
    </button>
  </div>
</template>

<script>
import { login, register } from "../services/api";

export default {
  data() {
    return {
      username: "",
      password: "",
      mode: "login",
      message: "",
    };
  },

  methods: {
    toggleMode() {
      this.mode = this.mode === "login" ? "register" : "login";
      this.message = "";
    },

    async loginUser() {
      try {
        const res = await login({
          username: this.username,
          password: this.password,
        });

        if (res.data === "INVALID") {
          this.message = "Wrong username or password";
          return;
        }

        this.$emit("logged-in", res.data);
      } catch (err) {
        this.message = "Login failed";
      }
    },

    async registerUser() {
        try {
            const res = await register({
                username: this.username,
                password: this.password,
            });

            console.log("SUCCESS:", res.data);
            this.message = "Registered successfully!";
        } catch (err) {
            const msg = err.response?.data;

            console.log("REGISTER ERROR:", msg);

            if (msg === "USERNAME_EXISTS") {
                this.message = "Username already exists!";
            } else if (msg === "USERNAME_EMPTY") {
                this.message = "Username cannot be empty!";
            } else if (msg === "PASSWORD_EMPTY") {
                this.message = "Password cannot be empty!";
            } else {
                this.message = "Registration failed";
            }
        }
    },
  },
};
</script>

<style>
.auth-box {
  max-width: 300px;
  margin: auto;
}
input {
  display: block;
  margin: 8px 0;
  width: 100%;
  padding: 8px;
}
button {
  margin: 5px 0;
  padding: 8px;
}
</style>