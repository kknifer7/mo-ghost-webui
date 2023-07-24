<template>
  <q-table
    :columns="columns"
    :rows="rows"
    :loading="loading"
    v-model:pagination="pagination"
    @request="onRequest"
    row-key="id"
    loading-label="加载中..."
    no-results-label="无数据..."
    no-data-label="无数据..."
  >
    <template v-slot:top>
      <div class="q-table__top relative-position row items-center">
        <div class="q-table__control row justify-between">
          <div class="q-table__title no-selection">
            独立文件（带💠为可编辑项）
          </div>
        </div>
      </div>

      <q-space />

      <div class="row q-gutter-sm flex center">
        <file-add-form @finish="onFileAddFinish" />
        <q-btn
          @click="onResetBtnClick"
          label="刷新"
          color="primary"
          icon="refresh"
        />
      </div>
    </template>

    <template v-slot:body="props">
      <tr :props="props">
        <q-td key="originName" :props="props">
          <g-label :content="props.row.originName" :copy="false" :tip="false" />
          <q-popup-edit
            auto-save
            @save="onRenameSave(props.row)"
            v-model="props.row.originName"
            v-slot="scope"
          >
            <q-input
              v-model="scope.value"
              :rules="[
                (val) => stringUtils.isNotBlank(val) || '请输入文件名',
                (val) => val.length <= 64 || '文件名不能超过64位',
              ]"
              dense
              autofocus
              counter
            />
          </q-popup-edit>
        </q-td>

        <q-td key="path" :props="props">
          <g-label :content="props.row.path" />
        </q-td>

        <q-td key="size" :props="props">
          {{ props.row.size }}
        </q-td>

        <q-td key="remark" :props="props">
          {{ props.row.remark }}
          <q-popup-edit
            auto-save
            @save="onRowSave(props.row)"
            v-model="props.row.remark"
            v-slot="scope"
          >
            <q-input
              v-model="scope.value"
              :rules="[(val) => val.length <= 64 || '备注不能超过64位']"
              dense
              autofocus
              counter
            />
          </q-popup-edit>
        </q-td>

        <q-td key="ctime" :props="props">
          <g-label :content="props.row.ctime" :max-length="11" />
        </q-td>

        <q-td key="mtime" :props="props">
          <g-label :content="props.row.mtime" :max-length="11" />
        </q-td>

        <q-td key="option" :props="props" class="q-gutter-xs">
          <q-btn
            @click="onDownloadBtnClick(props.row)"
            class="text-bold"
            :loading="downloadLoading[props.row.id]"
            flat
            dense
            :ripple="false"
            label="下载"
            color="primary"
          />
          <table-delete-btn
            :loading="downloadLoading[props.row.id]"
            @confirm="onDeleteConfirm(props.row.id)"
          />
        </q-td>
      </tr>
    </template>
  </q-table>
</template>

<script setup lang="ts">
import { PagedDataFetcher } from '@api/data-fetcher';
import { nextTick, onMounted, ref } from 'vue';
import { Pagination } from '@base/models';
import { MoFile, columns } from './models';
import { moFileService } from '@api/service';
import TableDeleteBtn from '@base/TableDeleteBtn.vue';
import GLabel from '@base/GLabel.vue';
import FileAddForm from '@/version/FileAddForm.vue';
import fileUtils from '@utils/file';
import stringUtils from '@utils/string';

function onFileAddFinish() {
  dataFetcher.onDataChanged();
}

async function onRenameSave(row: MoFile) {
  await nextTick();
  dataFetcher.dataUpdate(moFileService.rename, {
    id: row.id,
    name: row.originName,
  });
}

function onDeleteConfirm(id: number) {
  dataFetcher.dataUpdate(moFileService.delete, id);
}

const downloadLoading = ref([] as boolean[]);
async function onDownloadBtnClick(row: MoFile) {
  let data;

  downloadLoading.value[row.id] = true;
  data = await moFileService.download(row.id);
  fileUtils.exportFile(row.originName, data.data, '');
  setTimeout(() => (downloadLoading.value[row.id] = false), 2000);
}

async function onRowSave(row: MoFile) {
  await nextTick();
  dataFetcher.dataUpdate(moFileService.updateRemark, row);
}

const rows = ref<Array<MoFile>>();
const loading = ref(false);
const pagination = ref();
const dataFetcher = PagedDataFetcher.of(
  moFileService.findSingleList,
  pagination,
  loading,
  rows
);

function onResetBtnClick() {
  onRequest({ pagination: pagination.value });
}

async function onRequest(props: { pagination: Pagination }) {
  await dataFetcher.onRequest(props);
}

onMounted(() => {
  pagination.value = Pagination.ofDefault();
  dataFetcher.onRequest({ pagination: pagination.value, filter: null });
});
</script>
