<template>
  <q-btn flat :disable="isVerify === 1" align="left" class="full-width">
    <span class="text-body1 text-bold">
      <q-icon :name="getIconName()" :color="getIconColor()" />
      点击验证
    </span>
    <q-popup-proxy ref="captchaPopup">
      <captcha-index
        class="no-selection text-bold"
        type="SLIDER"
        title="滑动验证"
        :titleFontSize="18"
        titleColor="#E6A23C"
        :borderRadius="5"
        :bgImageWidth="300"
        :bgImageHeight="300"
        @closeCaptcha="closeCaptcha"
        ref="captchaIndex"
        @refreshCaptcha="refreshCaptcha"
        @validateCaptcha="validateCaptcha"
      />
    </q-popup-proxy>
  </q-btn>
</template>

<script setup lang="ts">
import CaptchaIndex from '@/captcha/CaptchaIndex.vue';
import noti from '@utils/notification';
import { ref } from 'vue';

const isVerify = ref<-1 | 0 | 1>(-1);
const captchaPopup = ref();
const emits = defineEmits(['success']);

function getIconName() {
  switch (isVerify.value) {
    case -1:
      return 'sentiment_satisfied';
    case 0:
      return 'sentiment_very_dissatisfied';
    case 1:
      return 'sentiment_very_satisfied';
  }
}

function getIconColor() {
  switch (isVerify.value) {
    case -1:
      return 'primary';
    case 0:
      return 'negative';
    case 1:
      return 'positive';
  }
}

// 关闭验证码弹框
function closeCaptcha() {
  captchaPopup.value.hide();
}

// 验证码验证
function validateCaptcha(isSuccess: unknown) {
  if (isSuccess) {
    isVerify.value = 1;
    closeCaptcha();
    noti.success('验证成功~');
    emits('success');
  } else {
    isVerify.value = 0;
    noti.fail('验证不通过，请重试');
  }
}

// 验证码刷新
function refreshCaptcha() {
  isVerify.value = -1;
}

defineExpose({
  hide: function () {
    closeCaptcha();
  },
  show: function () {
    captchaPopup.value.show();
  },
  reset: function () {
    refreshCaptcha();
    closeCaptcha();
  },
});
</script>
