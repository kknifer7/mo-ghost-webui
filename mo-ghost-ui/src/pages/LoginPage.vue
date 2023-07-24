<template>
  <Particles
    id="tsparticles"
    :particlesInit="particlesInit"
    :options="particles"
  />
  <div id="login-page">
    <div id="login-form" class="no-selection">
      <login-form />
    </div>
  </div>
</template>

<script setup lang="ts">
import LoginForm from '@/login/LoginForm.vue';
import { particles, particlesInit } from 'src/boot/particles';
</script>

<style scoped>
#login-form {
  position: relative;
  top: 20%;
}
#login-page {
  /* background: url(../assets/img/6328967_0_final.png) no-repeat; */
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}
</style>
