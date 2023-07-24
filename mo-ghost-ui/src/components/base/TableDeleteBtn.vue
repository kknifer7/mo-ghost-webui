<template>
  <q-btn
    flat
    dense
    :loading="loading"
    :ripple="false"
    @click="onBtnClick"
    :color="color"
    :label="label"
    class="text-bold"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';

type Props = {
  loading: boolean;
};
defineProps<Props>();
const emits = defineEmits(['confirm']);
const color = ref('negative');
const label = ref('删除');
let readyForDeleting = false;

function onBtnClick() {
  if (readyForDeleting) {
    readyForDeleting = false;
    change();
    emits('confirm');
  } else {
    setTimeout(() => {
      if (!readyForDeleting) {
        return;
      }
      readyForDeleting = false;
      change();
    }, 5000);
    readyForDeleting = true;
    change();
  }
}

function change() {
  if (readyForDeleting) {
    color.value = 'warning';
    label.value = '确认';
  } else {
    color.value = 'negative';
    label.value = '删除';
  }
}
</script>
