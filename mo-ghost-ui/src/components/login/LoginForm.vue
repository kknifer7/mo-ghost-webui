<template>
  <div class="row justify-center">
    <q-avatar>
      <img src="~@img/logo.svg" />
    </q-avatar>
    <span class="text-h4 text-center"> Mo Ghost: 资源发布管理系统 </span>
  </div>
  <div class="q-mt-lg row justify-center">
    <div class="q-pa-md col-4">
      <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
        <q-input
          autocomplete="off"
          color="primary"
          :disable="loginLoading"
          v-model="username"
          label="账号"
        />

        <q-input
          autocomplete="off"
          color="primary"
          :disable="loginLoading"
          type="password"
          v-model="password"
          label="密码"
        />

        <captcha-form ref="captchaForm" @success="onCaptchaVerifySuccess" />

        <div class="row">
          <q-btn
            class="col-6"
            :disable="loginLoading"
            label="登 录"
            type="submit"
            color="primary"
          >
            <q-inner-loading :showing="loginLoading">
              <q-spinner-ios size="40px" color="black" />
            </q-inner-loading>
          </q-btn>
          <q-btn
            class="offset-1 col-5"
            :disable="loginLoading"
            label="重 置"
            type="reset"
            color="info"
          />
        </div>
      </q-form>
    </div>
  </div>

  <q-inner-loading :showing="routerReplaceLoading">
    <q-spinner-gears size="50px" color="light-blue-7" />
  </q-inner-loading>
</template>

<script setup lang="ts">
import { Cookies } from 'quasar';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { securityService } from '@api/service';
import stringUtils from '@utils/string';
import noti from '@utils/notification';
import CaptchaForm from '@/login/CaptchaForm.vue';

const username = ref(Cookies.get('username') as string);
const password = ref('');
const loginLoading = ref();
const routerReplaceLoading = ref(false);
const router = useRouter();
const captchaVerified = ref(false);
const captchaForm = ref();

function onCaptchaVerifySuccess() {
  captchaVerified.value = true;
  onSubmit();
}

const onSubmit = async () => {
  if (stringUtils.isAnyBlank(username.value, password.value)) {
    return;
  }
  if (!captchaVerified.value) {
    captchaForm.value.show();
    return;
  }

  loginLoading.value = true;
  const response = await securityService.login({
    username: username.value,
    password: password.value,
  });
  if (response.data && response.data.code === 200) {
    Cookies.set('login', '1');
    routerReplaceLoading.value = true;
    router.replace('/');
    noti.success('登录成功，欢迎使用~');
  } else {
    captchaVerified.value = false;
    captchaForm.value.reset();
  }
  loginLoading.value = false;
};

const onReset = (): void => {
  username.value = '';
  password.value = '';
};
</script>
