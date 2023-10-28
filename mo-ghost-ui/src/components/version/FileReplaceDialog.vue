<template>
  <q-card style="width: 50rem">
    <q-form @submit="onSubmit" @reset="onReset">
      <q-card-section class="row">
        <div class="text-h6">上传文件</div>
      </q-card-section>

      <q-card-section class="q-gutter-md">
        <q-uploader
          ref="uploader"
          color="info"
          style="max-width: 300px"
          method="PUT"
          field-name="file"
          :url="moFileService.getReplaceUrl(props.fileId)"
          label="最大上传限制 (500M)"
          max-file-size="512000000"
          hide-upload-btn
          :multiple="false"
          @rejected="onRejected"
          @failed="onFailed"
          @finish="onFileUploadFinish"
        />
      </q-card-section>

      <q-card-actions align="right">
        <q-btn :loading="loading" label="提交" type="submit" color="info" />
        <q-btn :loading="loading" label="重置" type="reset" color="grey" />
      </q-card-actions>
    </q-form>
  </q-card>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import noti from '@utils/notification';
import { moFileService } from '@api/service';

type Props = {
  fileId: number;
};
const props = defineProps<Props>();

const emits = defineEmits(['finish']);

let isUploadFailed = false;
const uploader = ref();
const loading = ref(false);

const onSubmit = async () => {
  loading.value = true;
  await uploader.value.upload();
  loading.value = false;
};
const onReset = () => {
  uploader.value.reset();
};
const onRejected = () => {
  isUploadFailed = true;
  noti.fail();
};
const onFailed = (obj: { files: Array<object>; xhr: XMLHttpRequest }) => {
  isUploadFailed = true;
  noti.fail(obj.xhr.response.msg);
};
const onFileUploadFinish = () => {
  if (isUploadFailed) {
    isUploadFailed = false;

    return;
  }
  noti.success();
  emits('finish');
};
</script>
