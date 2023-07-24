<template>
  <q-btn
    v-if="btn"
    @click="addDialog.show()"
    color="positive"
    :label="label"
    :icon="mode === 'add' ? 'add' : 'autorenew'"
  />

  <q-dialog ref="addDialog">
    <single-release-form-dialog :mode="mode" :data="data" @finish="onFinish" />
  </q-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import SingleReleaseFormDialog from '@/single-release/SingleReleaseFormDialog.vue';
import noti from '@utils/notification';
import { SingleRelease } from './models';

type Props = {
  mode: 'add' | 'update';
  btn?: boolean;
  data?: SingleRelease;
};
const props = withDefaults(defineProps<Props>(), {
  btn: false,
});

const emits = defineEmits(['finish']);
const addDialog = ref();
const label = ref(props.mode === 'add' ? '新增' : '更新');

const onFinish = () => {
  addDialog.value.hide();
  noti.success();
  emits('finish');
};

function showDialog() {
  addDialog.value.show();
}

defineExpose({
  showDialog,
});
</script>
