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
    <q-tab name="accessKey" label="访问密钥" />
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

    <q-tab-panel name="accessKey">
      <div v-show="!loading" class="no-selection text-h4 text-center">
        <q-icon v-if="hasAccessKey" name="check_circle" class="text-positive" />
        <q-icon v-else name="assignment_late" class="text-primary" size="xl" />
        {{ hasAccessKey ? '已生成访问密钥' : '未生成访问密钥' }}
      </div>

      <div class="row justify-center q-mt-md">
        <q-btn
          :label="hasAccessKey ? '重新生成访问密钥' : '立即生成访问密钥'"
          :color="hasAccessKey ? 'warning' : 'positive'"
        >
          <q-popup-proxy ref="genAccessKeyPopup">
            <div class="q-pa-md no-selection row items-center">
              <q-icon size="sm" name="warning" color="negative" />
              若先前有密钥，会被覆盖，确定要生成密钥？
              <q-btn
                @click="onGenAccessKeyBtnClick"
                :ripple="false"
                color="primary"
                dense
                size="sm"
                icon="done"
              />
            </div>
          </q-popup-proxy>
        </q-btn>

        <q-btn
          v-if="hasAccessKey"
          class="offset-1"
          label="移除访问密钥"
          color="warning"
        >
          <q-popup-proxy ref="removeAccessKeyPopup">
            <div class="q-pa-md no-selection row items-center">
              <q-icon size="sm" name="warning" color="negative" />
              确定要移除访问密钥？
              <q-btn
                @click="onRemoveAccessKeyBtnClick"
                :ripple="false"
                color="primary"
                dense
                size="sm"
                icon="done"
              />
            </div>
          </q-popup-proxy>
        </q-btn>
      </div>

      <q-inner-loading :showing="loading">
        <q-spinner-gears size="50px" color="primary" />
      </q-inner-loading>
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
import { useQuasar } from 'quasar';
import clipboardUtils from '@utils/clipboard';

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

// 访问密钥相关
const hasAccessKey = ref(false);
const loading = ref(false);
const accessKeyLoading = ref(false);
const accessKey = ref('');
const genAccessKeyPopup = ref();
const removeAccessKeyPopup = ref();
const $q = useQuasar();

async function fetchData() {
  loading.value = true;
  hasAccessKey.value = (
    await securityService.getHasAccessKey()
  ).data.data.value;
  loading.value = false;
}

async function onGenAccessKeyBtnClick() {
  genAccessKeyPopup.value.hide();
  accessKeyLoading.value = true;
  accessKey.value = (await securityService.genAccessKey()).data.data.value;
  accessKeyLoading.value = false;
  $q.dialog({
    title: '访问密钥',
    message: `这是您的访问密钥，请注意保存，后续不再显示。<br/><br/><span style="font-weight: bold;">${accessKey.value}</span>`,
    html: true,
    persistent: true,
    ok: '复制密钥并关闭',
  }).onOk(() => {
    clipboardUtils.save(accessKey.value);
    fetchData();
  });
}

async function onRemoveAccessKeyBtnClick() {
  removeAccessKeyPopup.value.hide();
  accessKeyLoading.value = true;
  const resp = await securityService.deleteAccessKey();
  if (resp.data.code === 200) {
    noti.success();
  }
  accessKeyLoading.value = false;
  fetchData();
}

fetchData();
</script>
