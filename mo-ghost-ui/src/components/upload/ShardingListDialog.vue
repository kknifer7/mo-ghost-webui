<template>
  <q-card style="width: 50rem">
    <q-form>
      <q-bar class="bg-primary text-white">
        <div class="no-selection">
          <q-icon name="view_list" />
          任务列表
        </div>

        <q-space />

        <q-btn @click="fetchData" dense flat icon="refresh">
          <q-tooltip class="text-caption" :delay="800"> 刷新 </q-tooltip>
        </q-btn>
        <q-btn dense flat icon="cleaning_services">
          <q-popup-proxy ref="cleanPopupProxyRef">
            <div class="q-pa-sm no-selection row items-center">
              <q-icon size="sm" name="info" color="primary" />
              清理所有已完成的上传记录？
              <q-btn
                @click="onCleanBtnClick"
                color="primary"
                :ripple="false"
                dense
                size="sm"
                icon="done"
              />
            </div>
          </q-popup-proxy>
        </q-btn>

        <q-space />

        <q-btn dense flat icon="close" @click="$emit('dialog-close')" />
      </q-bar>

      <q-card-section>
        <transition
          appear
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut"
        >
          <q-list v-if="shardings.length > 0" class="q-pt-sm">
            <q-item
              class="row no-wrap items-center justify-around"
              v-for="item in shardings"
              :key="item.id"
            >
              <span>
                <q-icon size="lg" color="primary" name="folder" />
                <g-label :max-length="10" :content="item.fileName" />
              </span>
              <template v-if="item.doneFlag">
                <q-linear-progress
                  v-if="item.doneFlag"
                  class="q-mb-md"
                  color="positive"
                  :value="1"
                />
                <q-btn
                  class="q-mb-md q-ml-md"
                  color="positive"
                  icon="check_circle_outline"
                  dense
                  flat
                >
                  <q-popup-proxy>
                    <div class="q-pa-md no-selection row items-center">
                      <q-icon size="sm" name="info" color="primary" />
                      删除该上传记录？
                      <q-btn
                        @click="onDeleteBtnClick(item.id)"
                        color="primary"
                        :ripple="false"
                        dense
                        size="sm"
                        icon="done"
                      />
                    </div>
                  </q-popup-proxy>
                </q-btn>
                <q-btn
                  @click="onDeleteBtnClick(item.id)"
                  class="q-mb-md q-ml-md"
                  color="warning"
                  :ripple="false"
                  dense
                  flat
                  icon="highlight_off"
                />
              </template>
              <template v-else>
                <q-linear-progress
                  class="q-mb-md"
                  :value="item.shardIndex / item.shardCount"
                />
                <!-- TODO 继续上传  <q-btn
                  class="q-mb-md q-ml-md"
                  color="primary"
                  :ripple="false"
                  dense
                  flat
                  icon="refresh"
                /> -->
                <q-btn
                  class="q-mb-md q-ml-md"
                  color="negative"
                  :ripple="false"
                  dense
                  flat
                  icon="delete_outline"
                >
                  <q-popup-proxy>
                    <div class="q-pa-md no-selection row items-center">
                      <q-icon size="sm" name="warning" color="negative" />
                      确认终止并删除任务？
                      <q-btn
                        @click="onAbortDeleteBtnClick(item.fileKey)"
                        :ripple="false"
                        color="negative"
                        dense
                        size="sm"
                        icon="done"
                      />
                    </div>
                  </q-popup-proxy>
                </q-btn>
              </template>
            </q-item>
          </q-list>

          <span v-else class="no-selection text-h6 text-secondary">
            暂无内容...
          </span>
        </transition>

        <q-inner-loading :showing="loading" color="primary" />
      </q-card-section>
    </q-form>
  </q-card>
</template>

<script setup lang="ts">
import { uploadService } from '@api/service';
import GLabel from '@base/GLabel.vue';
import { inject, ref } from 'vue';
import { UploadSharding } from './models';
import { FullDataFetcher } from '@api/data-fetcher';
import { EventBus } from 'quasar';
import { GlobalEvents } from '@base/models';
import { useUploaderStore } from 'src/stores/uploader-store';
import loadingUtils from '@utils/loading';
import { MoFileUploadInfo } from '@tools/file-uploader/models';

const shardings = ref<Array<UploadSharding>>([]);
const loading = ref(false);
const dataFetcher = new FullDataFetcher<UploadSharding>(
  uploadService.getAll,
  loading,
  shardings
);
const uploaderStore = useUploaderStore();

async function fetchData() {
  loading.value = true;

  const resp = await uploadService.getAll();

  shardings.value = resp.data.data;
  loading.value = false;
}
fetchData();

function onDeleteBtnClick(id: number) {
  dataFetcher.dataUpdate(uploadService.delete, id);
}

async function onAbortDeleteBtnClick(fileKey: string) {
  uploaderStore.existTaskByFileKey(fileKey)
    ? uploaderStore.cancelTaskByFileKey(fileKey)
    : dataFetcher.dataUpdate(uploadService.deleteByFileKey, fileKey, true);
}

const cleanPopupProxyRef = ref();

function onCleanBtnClick() {
  dataFetcher.dataUpdate(uploadService.deleteAllDone, null);
  cleanPopupProxyRef.value.hide();
}

const bus = inject('bus') as EventBus;

bus.on(GlobalEvents.NewUploadTask, (shardInfo: MoFileUploadInfo) => {
  uploaderStore.sendTask(
    {
      fileId: shardInfo.fileId,
      fileKey: shardInfo.fileKey,
      businessType: shardInfo.businessType,
      file: shardInfo.file,
    },
    () => {
      loadingUtils.hide();
      dataFetcher.onDataChanged(false);
    },
    () => {
      dataFetcher.onDataChanged(false);
      bus.emit(GlobalEvents.FinishUploadTask);
    }
  );
});
</script>
