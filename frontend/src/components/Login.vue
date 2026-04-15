<template>
  <div class="auth-wrapper">
    <div class="auth-card">

      <div class="brand">
        <span class="brand-icon">▶</span>
        <span class="brand-name">Watchlist</span>
      </div>

      <div class="tab-row">
        <button
          class="tab"
          :class="{ active: mode === 'login' }"
          @click="setMode('login')"
        >Sign in</button>
        <button
          class="tab"
          :class="{ active: mode === 'register' }"
          @click="setMode('register')"
        >Create account</button>
        <div class="tab-indicator" :class="mode === 'register' ? 'right' : 'left'"></div>
      </div>

      <transition name="fade" mode="out-in">
        <form :key="mode" class="auth-form" @submit.prevent="mode === 'login' ? loginUser() : registerUser()">

          <div class="field">
            <label>Username</label>
            <input
              v-model="username"
              type="text"
              placeholder="Enter your username"
              autocomplete="username"
              :class="{ error: fieldError === 'username' }"
            />
          </div>

          <div class="field">
            <label>Password</label>
            <div class="password-wrap">
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="Enter your password"
                autocomplete="current-password"
                :class="{ error: fieldError === 'password' }"
              />
              <button type="button" class="toggle-pw" @click="showPassword = !showPassword">
                {{ showPassword ? '🙈' : '👁' }}
              </button>
            </div>
          </div>

          <transition name="slide-msg">
            <p v-if="message" class="message" :class="messageType">{{ message }}</p>
          </transition>

          <button type="submit" class="submit-btn" :disabled="loading">
            <span v-if="loading" class="spinner"></span>
            <span v-else>{{ mode === 'login' ? 'Sign in' : 'Create account' }}</span>
          </button>

        </form>
      </transition>

    </div>
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
      messageType: "error",
      fieldError: null,
      showPassword: false,
      loading: false,
    };
  },

  methods: {
    setMode(m) {
      this.mode = m;
      this.message = "";
      this.fieldError = null;
    },

    async loginUser() {
      this.loading = true;
      this.message = "";
      this.fieldError = null;
      try {
        const res = await login({ username: this.username, password: this.password });
        if (res.data === "INVALID") {
          this.message = "Wrong username or password.";
          this.messageType = "error";
          return;
        }
        this.$emit("logged-in", res.data);
      } catch {
        this.message = "Login failed. Please try again.";
        this.messageType = "error";
      } finally {
        this.loading = false;
      }
    },

    async registerUser() {
      this.loading = true;
      this.message = "";
      this.fieldError = null;
      try {
        await register({ username: this.username, password: this.password });
        this.message = "Account created! You can now sign in.";
        this.messageType = "success";
        setTimeout(() => this.setMode("login"), 1500);
      } catch (err) {
        const msg = err.response?.data;
        if (msg === "USERNAME_EXISTS") {
          this.message = "That username is already taken.";
          this.fieldError = "username";
        } else if (msg === "USERNAME_EMPTY") {
          this.message = "Username cannot be empty.";
          this.fieldError = "username";
        } else if (msg === "PASSWORD_EMPTY") {
          this.message = "Password cannot be empty.";
          this.fieldError = "password";
        } else {
          this.message = "Registration failed. Please try again.";
        }
        this.messageType = "error";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=DM+Serif+Display&family=DM+Sans:wght@400;500&display=swap');

.auth-wrapper {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0e0e11;
  font-family: 'DM Sans', sans-serif;
}

.auth-card {
  width: 380px;
  background: #17171c;
  border: 1px solid #2a2a33;
  border-radius: 16px;
  padding: 36px 32px 40px;
  box-shadow: 0 24px 64px rgba(0,0,0,0.5);
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 32px;
}

.brand-icon {
  width: 32px;
  height: 32px;
  background: #e8ff47;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  color: #0e0e11;
  flex-shrink: 0;
  line-height: 32px;
  text-align: center;
}

.brand-name {
  font-family: 'DM Serif Display', serif;
  font-size: 20px;
  color: #f0f0f0;
  letter-spacing: -0.3px;
}

.tab-row {
  position: relative;
  display: flex;
  background: #0e0e11;
  border-radius: 10px;
  padding: 4px;
  margin-bottom: 28px;
  border: 1px solid #2a2a33;
}

.tab {
  flex: 1;
  padding: 8px 0;
  font-size: 13.5px;
  font-family: 'DM Sans', sans-serif;
  font-weight: 500;
  color: #666;
  background: none;
  border: none;
  cursor: pointer;
  position: relative;
  z-index: 1;
  transition: color 0.2s;
}

.tab.active {
  color: #0e0e11;
}

.tab-indicator {
  position: absolute;
  top: 4px;
  bottom: 4px;
  width: calc(50% - 4px);
  background: #e8ff47;
  border-radius: 7px;
  transition: left 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.tab-indicator.left  { left: 4px; }
.tab-indicator.right { left: calc(50%); }

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label {
  font-size: 12.5px;
  font-weight: 500;
  color: #888;
  letter-spacing: 0.3px;
  text-transform: uppercase;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 11px 14px;
  background: #0e0e11;
  border: 1px solid #2a2a33;
  border-radius: 9px;
  color: #f0f0f0;
  font-size: 14px;
  font-family: 'DM Sans', sans-serif;
  outline: none;
  transition: border-color 0.15s;
  box-sizing: border-box;
}

input:focus {
  border-color: #e8ff47;
}

input.error {
  border-color: #ff5a5a;
}

input::placeholder {
  color: #444;
}

.password-wrap {
  position: relative;
}

.password-wrap input {
  padding-right: 44px;
}

.toggle-pw {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 15px;
  padding: 0;
  line-height: 1;
  opacity: 0.6;
  transition: opacity 0.15s;
}

.toggle-pw:hover { opacity: 1; }

.message {
  font-size: 13px;
  padding: 10px 14px;
  border-radius: 8px;
  margin: 0;
  line-height: 1.5;
}

.message.error {
  background: rgba(255, 90, 90, 0.12);
  color: #ff8080;
  border: 1px solid rgba(255, 90, 90, 0.2);
}

.message.success {
  background: rgba(100, 220, 140, 0.1);
  color: #64dc8c;
  border: 1px solid rgba(100, 220, 140, 0.2);
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #e8ff47;
  color: #0e0e11;
  border: none;
  border-radius: 9px;
  font-size: 14px;
  font-weight: 500;
  font-family: 'DM Sans', sans-serif;
  cursor: pointer;
  margin-top: 4px;
  transition: opacity 0.15s, transform 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 44px;
}

.submit-btn:hover:not(:disabled) { opacity: 0.88; }
.submit-btn:active:not(:disabled) { transform: scale(0.98); }
.submit-btn:disabled { opacity: 0.5; cursor: not-allowed; }

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(14,14,17,0.3);
  border-top-color: #0e0e11;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s, transform 0.15s;
}
.fade-enter-from {
  opacity: 0;
  transform: translateX(8px);
}
.fade-leave-to {
  opacity: 0;
  transform: translateX(-8px);
}

.slide-msg-enter-active,
.slide-msg-leave-active {
  transition: opacity 0.2s, max-height 0.2s, margin 0.2s;
  overflow: hidden;
  max-height: 80px;
}
.slide-msg-enter-from,
.slide-msg-leave-to {
  opacity: 0;
  max-height: 0;
}
</style>