<template>
  <q-btn
    @click="addDialog.show()"
    color="positive"
    label="上传"
    icon="upload"
  />
  <q-dialog ref="addDialog">
    <FileAddDialog @finish="onFinish" />
  </q-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import FileAddDialog from '@/version/FileAddDialog.vue';

const emits = defineEmits(['finish']);
const addDialog = ref();

const onFinish = () => {
  addDialog.value.hide();
  emits('finish');
};
</script>
