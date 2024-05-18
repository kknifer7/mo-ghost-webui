<template>
  <input
    @change="onUpdate"
    type="file"
    id="file_input"
    name="folder"
    webkitdirectory
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import uploadUtils from '@utils/upload';

const model = ref();
const hashResult = ref('');

// TODO 测试成功，准备接入分片上传
async function onUpdate(evt: Event) {
  const targetElm = evt.target as HTMLInputElement;
  const files = targetElm.files;

  if (!files || files.length === 0) {
    return;
  }
  // uploadUtils.fillFileTree(files);
}
</script>
@utils/hash
