<template>
  <q-tabs
    v-model="tab"
    class="text-grey"
    active-color="primary"
    indicator-color="primary"
    align="justify"
    narrow-indicator
  >
    <q-tab name="changePassword" label="修改账号信息" />
    <q-tab name="others" label="其他" />
  </q-tabs>

  <q-tab-panels v-model="tab" animated>
    <q-tab-panel name="changePassword">
      <div class="no-selection text-h4 text-center">修改账号信息</div>
      <div class="row justify-center">
        <div class="q-pa-md col-6">
          <q-form class="q-gutter-sm" @submit="onSubmit" @reset="onReset">
            <q-input
              ref="usernameField"
              v-model="username"
              label="请输入账号"
              outlined
              autofocus
              lazy-rule
              :rules="[
                (val) => stringUtils.isNotBlank(val) || '账号不能为空',
                (val) => val.length <= 16 || '账号不能超过16位',
              ]"
            />
            <q-input
              :type="isPwd ? 'password' : 'text'"
              v-model="password"
              label="请输入密码"
              outlined
              lazy-rule
              :rules="[
                (val) => stringUtils.isNotBlank(val) || '密码不能为空',
                (val) => val.length <= 32 || '密码不能超过32位',
              ]"
            >
              <template v-slot:append>
                <q-icon
                  :name="isPwd ? 'visibility' : 'visibility_off'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>
            </q-input>
            <q-input
              :type="isPwd ? 'password' : 'text'"
              v-model="confirmPassword"
              label="确认密码"
              outlined
              lazy-rule
              :rules="[(val) => val === password || '密码不一致']"
            >
              <template v-slot:append>
                <q-icon
                  :name="isPwd ? 'visibility' : 'visibility_off'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>
            </q-input>
            <div class="row justify-center q-gutter-sm">
              <q-btn type="submit" class="col-4" color="primary" label="提交" />
              <q-btn
                type="reset"
                class="col-4"
                color="secondary"
                label="重置"
              />
            </div>
          </q-form>
        </div>
      </div>
    </q-tab-panel>

    <q-tab-panel name="others">
      <div class="text-h6">敬请期待...</div>
    </q-tab-panel>
  </q-tab-panels>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import stringUtils from '@utils/string';
import loadingUtils from '@utils/loading';
import { securityService } from '@api/service';
import noti from '@utils/notification';

const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const isPwd = ref(true);
const tab = ref('changePassword');
const usernameField = ref();

function onReset() {
  username.value = '';
  password.value = '';
  confirmPassword.value = '';
  usernameField.value.focus();
}

async function onSubmit() {
  loadingUtils.show();
  const resp = await securityService.updateAccount(
    username.value,
    password.value
  );
  if (resp.data && resp.data.code === 200) {
    noti.success();
  }
  loadingUtils.hide();
}
</script>
