<template>
  <q-card style="width: 50rem">
    <q-form @submit="onSubmit" @reset="onReset">
      <q-card-section class="row">
        <div class="text-h6">{{ title }}</div>
      </q-card-section>

      <q-card-section class="q-gutter-md">
        <q-file
          outlined
          max-file-size="4294967296"
          v-model="model"
          label="选择文件 (最大支持4GB)"
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
import { ref, inject } from 'vue';
import { BusinessType } from '@tools/file-uploader/models';

import { EventBus } from 'quasar';
import { GlobalEvents } from '@base/models';
import loadingUtils from '@utils/loading';
import hash from '@utils/hash';

type Props = {
  title: string;
  fileId?: number;
  businessType: BusinessType;
};
const props = defineProps<Props>();

const emits = defineEmits(['finish']);

const loading = ref(false);
const model = ref<File | null>();

const bus = inject('bus') as EventBus;

async function onSubmit() {
  const file = model.value;

  if (!file) {
    return;
  }
  loadingUtils.show();
  bus.emit(GlobalEvents.NewUploadTask, {
    fileId: props.fileId,
    fileKey: hash.fastCalcHash(file),
    businessType: props.businessType,
    file,
  });
  emits('finish');
}

function onReset() {
  model.value = null;
}
</script>
