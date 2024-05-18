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
          <q-menu ref="fileNameOpMenu" class="bg-primary">
            <q-list class="text-white" dense>
              <q-item clickable>
                <q-item-section>
                  <span><q-icon name="find_replace" />&nbsp;重命名</span>
                </q-item-section>
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
              </q-item>
              <q-item @click="onFileReplaceItemClick" clickable>
                <q-item-seciton>
                  <span><q-icon name="autorenew" />&nbsp;替换</span>
                </q-item-seciton>
                <q-dialog ref="replaceDialog">
                  <file-upload-dialog
                    :file-id="props.row.id"
                    title="文件替换"
                    :businessType="BusinessType.SingleReleaseFileReplace"
                    @finish="onFileReplaced"
                  />
                </q-dialog>
              </q-item>
            </q-list>
          </q-menu>
        </q-td>

        <q-td key="path" :props="props">
          <g-label :content="props.row.path" />
        </q-td>

        <q-td key="size" :props="props">
          {{ props.row.size }}
        </q-td>

        <q-td key="state" :props="props">
          <q-icon
            v-if="props.row.state === MoFileState.MergeFailed"
            name="highlight_off"
            color="negative"
            size="20px"
          >
            <q-tooltip class="bg-grey-2 text-body1 text-negetive">
              <q-item-label> 合并失败 </q-item-label>
            </q-tooltip>
          </q-icon>
          <q-icon
            v-if="props.row.state === MoFileState.Normal"
            name="check_circle_outline"
            color="positive"
            size="20px"
          >
            <q-tooltip class="bg-grey-2 text-body1 text-positive">
              <q-item-label> 正常 </q-item-label>
            </q-tooltip>
          </q-icon>
          <q-circular-progress
            v-if="props.row.state === MoFileState.Merging"
            indeterminate
            size="20px"
            color="warning"
          >
            <q-tooltip class="bg-grey-2 text-body1 text-warning">
              <q-item-label> 合并中 </q-item-label>
            </q-tooltip>
          </q-circular-progress>
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
import { inject, nextTick, onMounted, ref } from 'vue';
import { GlobalEvents, Pagination } from '@base/models';
import { MoFile, columns, MoFileState } from './models';
import { moFileService } from '@api/service';
import TableDeleteBtn from '@base/TableDeleteBtn.vue';
import GLabel from '@base/GLabel.vue';
import FileAddForm from '@/version/FileAddForm.vue';
import FileUploadDialog from '@/version/FileUploadDialog.vue';
import fileUtils from '@utils/file';
import stringUtils from '@utils/string';
import { EventBus } from 'quasar';
import { BusinessType } from '@/upload/models';

const bus = inject('bus') as EventBus;

bus.on(GlobalEvents.FinishUploadTask, onFileAddFinish);

function onFileAddFinish() {
  dataFetcher.onDataChanged();
}

const replaceDialog = ref();
const fileNameOpMenu = ref();
function onFileReplaceItemClick() {
  replaceDialog.value.show();
}
function onFileReplaced() {
  replaceDialog.value.hide();
  fileNameOpMenu.value.hide();
  onFileAddFinish();
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

const rows = ref<Array<MoFile>>([]);
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
