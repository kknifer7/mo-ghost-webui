<template>
  <q-btn
    @click="uploadDialog.show()"
    color="positive"
    label="上传"
    icon="upload"
  />
  <q-dialog ref="uploadDialog">
    <file-upload-dialog
      title="文件上传"
      :businessType="BusinessType.SingleRelease"
      @finish="onFinish"
    />
  </q-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import FileUploadDialog from '@/version/FileUploadDialog.vue';
import { BusinessType } from '@/upload/models';

const emits = defineEmits(['finish']);
const uploadDialog = ref();

function onFinish() {
  uploadDialog.value.hide();
  emits('finish');
}
</script>
