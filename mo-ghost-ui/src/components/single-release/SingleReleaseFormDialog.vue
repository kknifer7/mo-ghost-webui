<template>
  <q-card style="max-width: 180rem; max-height: 30rem">
    <q-card-section>
      <div class="text-h6 no-selection">
        文件选择{{ mode === 'add' ? '（添加）' : '（更换）' }}
      </div>
    </q-card-section>

    <q-form @submit="onSubmit">
      <q-scroll-area class="q-ma-sm" style="width: 22rem; height: 15rem">
        <q-card-section
          @click="onFileSectionClick(file.id)"
          v-for="file in files"
          :key="file.id"
        >
          <div style="cursor: pointer" class="row items-center">
            <div class="col-10">
              <g-label
                :content="file.originName"
                content-class="text-body1"
                :max-length="20"
                :tip="stringUtils.isNotBlank(file.remark)"
                :tip-content="'备注: ' + file.remark"
                :copy="false"
              />
              <span class="q-ml-sm no-selection text-blue-grey">
                {{ file.size }}
              </span>
            </div>

            <q-radio v-model="selectedFileId" :val="file.id" />
          </div>
          <q-separator />
        </q-card-section>
      </q-scroll-area>

      <q-card-section class="q-gutter-sm" align="center">
        <q-btn :loading="loading" type="submit" color="primary" label="提交" />
        <q-btn :loading="loading" v-close-popup color="info" label="取消" />
      </q-card-section>
    </q-form>
  </q-card>
</template>

<script setup lang="ts">
import { MoFile } from '@/version/models';
import { moFileService, singleReleaseService } from '@api/service';
import { Pagination } from '@base/models';
import noti from '@utils/notification';
import { ref } from 'vue';
import GLabel from '@base/GLabel.vue';
import stringUtils from '@utils/string';
import { SingleRelease } from './models';

type Props = {
  mode: 'add' | 'update';
  data?: SingleRelease;
};
const props = defineProps<Props>();

const emits = defineEmits(['finish']);
const selectedFileId = ref();
const files = ref([] as MoFile[]);
const loading = ref(false);

function onFileSectionClick(fileId: number) {
  selectedFileId.value = fileId;
}

async function onSubmit() {
  if (!selectedFileId.value) {
    noti.fail('请选择文件');
    return;
  }

  loading.value = true;
  const file = files.value.find((f) => f.id === selectedFileId.value) as MoFile;
  props.mode === 'add'
    ? await singleReleaseService.add(file)
    : props.data
    ? await singleReleaseService.replaceFile(props.data, file)
    : console.error('wrong code, props.data undefined');
  loading.value = false;
  emits('finish');
}

async function init() {
  const resp = await moFileService.findSingleList(
    Pagination.FIRST_PAGE_NUMBER - 1,
    Pagination.PAGE_MAX_SIZE
  );

  if (resp.status !== 200 || resp.data.code !== 200) {
    noti.fail('获取文件数据失败，请联系开发者');
    return;
  }
  files.value = resp.data.data.content;
}

init();
</script>
