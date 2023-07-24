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
            资源列表（带💠为可编辑项）
          </div>
        </div>
      </div>

      <q-space />

      <div class="row q-gutter-sm flex center">
        <single-release-form
          ref="form"
          :mode="mode"
          :data="data"
          @finish="onUpdateFinish"
        />
        <q-btn
          @click="onAddBtnClick"
          color="positive"
          label="新增"
          icon="add"
        />
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
        <q-td key="fullName" :props="props">
          <g-label :content="props.row.fullName" :copy="false" :tip="false" />
          <q-popup-edit
            auto-save
            @save="onRowSave(props.row)"
            v-model="props.row.fullName"
            v-slot="scope"
          >
            <q-input
              v-model="scope.value"
              :rules="[(val) => val.length <= 64 || '名称不能超过64位']"
              dense
              autofocus
              counter
            />
          </q-popup-edit>
        </q-td>

        <q-td key="fileName" :props="props">
          <span>
            <g-label :content="props.row.fileName" :copy="false" :tip="false" />
            <q-menu class="bg-primary">
              <q-list class="text-white" dense>
                <q-item
                  @click="clipboardUtils.save(props.row.fileName)"
                  clickable
                  v-close-popup
                >
                  <q-item-section>
                    <span><q-icon name="content_copy" />&nbsp;复制名称</span>
                  </q-item-section>
                  <q-tooltip
                    class="text-body1 bg-info"
                    anchor="center right"
                    self="center start"
                  >
                    {{ props.row.fileName }}
                  </q-tooltip>
                </q-item>
                <q-item clickable>
                  <q-item-section>
                    <span><q-icon name="find_replace" />&nbsp;重命名</span>
                    <q-popup-edit
                      auto-save
                      @save="onFileNameChanged(props.row)"
                      v-model="props.row.fileName"
                      v-slot="scope"
                    >
                      <q-input
                        v-model="scope.value"
                        :rules="[
                          (val) => val.length <= 64 || '文件名不能超过64位',
                        ]"
                        dense
                        autofocus
                        counter
                      />
                    </q-popup-edit>
                  </q-item-section>
                </q-item>
                <q-item
                  clickable
                  @click="onChangeBtnClick(props.row)"
                  v-close-popup
                >
                  <q-item-section>
                    <span><q-icon name="autorenew" />&nbsp;更换文件</span>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </span>
        </q-td>

        <q-td key="fileSize" :props="props">
          {{ props.row.fileSize }}
        </q-td>

        <q-td key="releaseStatus" :props="props" class="no-selection">
          <span
            :class="SingleReleaseStatus.get(props.row.releaseStatus).classe"
          >
            {{ SingleReleaseStatus.get(props.row.releaseStatus).label }}
          </span>
          <q-popup-edit
            auto-save
            @save="onRowSave(props.row)"
            v-model="props.row.releaseStatus"
            v-slot="scope"
          >
            <q-select
              v-model="scope.value"
              :options="SingleReleaseStatus.values()"
              dense
              autofocus
              emit-value
              map-options
            />
          </q-popup-edit>
        </q-td>

        <q-td key="totalAccess" :props="props">
          {{ props.row.totalAccess }}
        </q-td>

        <q-td key="remark" :props="props">
          {{ props.row.remark }}
          <q-popup-edit
            auto-save
            @save="onRowSave(props.row)"
            v-model="props.row.remark"
            v-slot="scope"
          >
            <q-input v-model="scope.value" dense autofocus counter />
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
import { SingleRelease, columns } from './models';
import { singleReleaseService, moFileService } from '@api/service';
import { SingleReleaseStatus } from '@data/dict';
import TableDeleteBtn from '@base/TableDeleteBtn.vue';
import GLabel from '@base/GLabel.vue';
import clipboardUtils from '@utils/clipboard';
import fileUtils from '@utils/file';
import SingleReleaseForm from './SingleReleaseForm.vue';

async function onDeleteConfirm(id: number) {
  downloadLoading.value[id] = true;
  await dataFetcher.dataUpdate(singleReleaseService.delete, id);
  downloadLoading.value[id] = false;
}

const mode = ref<'add' | 'update'>('add');
const data = ref();
const form = ref();
function onAddBtnClick() {
  mode.value = 'add';
  data.value = undefined;
  form.value.showDialog();
}
function onChangeBtnClick(row: SingleRelease) {
  mode.value = 'update';
  data.value = row;
  form.value.showDialog();
}
function onUpdateFinish() {
  dataFetcher.onDataChanged();
}

const downloadLoading = ref([] as boolean[]);
async function onDownloadBtnClick(row: SingleRelease) {
  let data;

  downloadLoading.value[row.id] = true;
  data = await moFileService.download(row.fileId);
  fileUtils.exportFile(row.fileName, data.data, '');
  setTimeout(() => (downloadLoading.value[row.id] = false), 2000);
}

async function onRowSave(row: SingleRelease) {
  await nextTick();
  dataFetcher.dataUpdate(singleReleaseService.update, row);
}

async function onFileNameChanged(row: SingleRelease) {
  await nextTick();
  dataFetcher.dataUpdate(moFileService.rename, {
    id: row.fileId,
    name: row.fileName,
  });
}

const rows = ref<Array<SingleRelease>>();
const loading = ref(false);
const pagination = ref();
const dataFetcher = PagedDataFetcher.of(
  singleReleaseService.findList,
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
