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
            访问控制列表（带💠为可编辑项）
          </div>

          <div class="row flex center">
            <q-select
              outlined
              dense
              v-model="filterType"
              :options="PeriodQueryStatus.values()"
              @update:model-value="fetchData"
            >
              <template v-slot:selected>
                {{ filterType.label }}
              </template>
            </q-select>

            <q-input
              class="col-6"
              outlined
              dense
              clearable
              v-model="filterRemark"
              debounce="500"
              label="备注"
              @update:model-value="fetchData"
            >
              <template v-slot:prepend>
                <q-icon
                  @click="fetchData"
                  class="cursor-pointer"
                  name="search"
                />
              </template>
            </q-input>
          </div>
        </div>
      </div>

      <q-space />

      <div class="row q-gutter-sm flex center">
        <q-btn
          :loading="addBtnLoading"
          @click="onAddBtnClick"
          label="新增"
          icon="add"
          color="positive"
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
        <q-td key="remark" :props="props">
          <g-label :content="props.row.remark" :tip="false" :copy="false" />
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

        <q-td key="cdkStatus" :props="props">
          <span :class="SingleReleaseCdkStatus.get(props.row.cdkStatus).classe">
            {{ SingleReleaseCdkStatus.get(props.row.cdkStatus).label }}
          </span>
          <q-popup-edit
            auto-save
            @save="onRowSave(props.row)"
            v-model="props.row.cdkStatus"
            v-slot="scope"
          >
            <q-select
              v-model="scope.value"
              :options="SingleReleaseCdkStatus.values()"
              dense
              autofocus
              emit-value
              map-options
            />
          </q-popup-edit>
        </q-td>

        <q-td class="no-selection" key="code" :props="props">
          <g-label :content="props.row.code" :tip="false" :copy="false" />
          <q-popup-edit
            auto-save
            @save="onRowSave(props.row)"
            v-model="props.row.code"
            v-slot="scope"
          >
            <q-input
              v-model="scope.value"
              :rules="[
                (val) => stringUtils.isNotBlank(val) || '访问标识符不能为空',
                (val) => val.length <= 36 || '访问标识符不能超过36位',
              ]"
              dense
              autofocus
              counter
            />
          </q-popup-edit>
        </q-td>

        <q-td class="no-selection" key="authWord" :props="props">
          <g-label :content="props.row.authWord" :tip="false" :copy="false" />
          <q-menu class="bg-primary">
            <q-list class="text-white" dense>
              <q-item
                @click="clipboardUtils.save(props.row.authWord)"
                :disable="stringUtils.isBlank(props.row.authWord)"
                clickable
                v-close-popup
              >
                <q-item-section>
                  <span><q-icon name="content_copy" />&nbsp;复制名称</span>
                </q-item-section>
                <q-tooltip
                  v-if="stringUtils.isNotBlank(props.row.authWord)"
                  class="text-body1 bg-info"
                  anchor="center right"
                  self="center start"
                >
                  {{ props.row.authWord }}
                </q-tooltip>
              </q-item>
              <q-item
                clickable
                :disable="stringUtils.isBlank(props.row.authWord)"
                @click="onDeleteAuthWordBtnClick(props.row.id)"
                v-close-popup
              >
                <q-item-section>
                  <span><q-icon name="autorenew" />&nbsp;重置</span>
                </q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-td>

        <q-td class="no-selection" key="expireAt" :props="props">
          <g-label :content="props.row.expireAt" :max-length="10" />
          <q-popup-proxy
            @before-hide="onRowSave(props.row)"
            cover
            transition-show="scale"
            transition-hide="scale"
          >
            <div class="q-gutter-md row items-start">
              <q-date
                v-model="props.row.expireAt"
                mask="YYYY-MM-DD HH:mm:ss"
                color="primary"
              />
              <q-time
                v-model="props.row.expireAt"
                mask="YYYY-MM-DD HH:mm:ss"
                color="primary"
              />
            </div>
          </q-popup-proxy>
        </q-td>

        <q-td key="expired" :props="props">
          <q-badge
            class="no-selection"
            :color="props.row.expired ? 'negative' : 'positive'"
            :label="props.row.expired ? '过期' : '有效'"
          />
        </q-td>

        <q-td key="lastAccessAt" :props="props">
          <g-label :content="props.row.lastAccessAt" :max-length="10" />
        </q-td>

        <q-td key="lastAccessIP" :props="props">
          {{ props.row.lastAccessIP }}
        </q-td>

        <q-td key="lastAccessRegion" :props="props">
          {{ props.row.lastAccessRegion }}
        </q-td>

        <q-td key="totalAccess" class="no-selection" :props="props">
          {{ props.row.totalAccess }}
        </q-td>

        <q-td key="option" :props="props" class="q-gutter-xs">
          <q-btn
            @click="onManageBtnClick(props.row)"
            class="text-bold"
            dense
            flat
            :ripple="false"
            color="primary"
            label="管理"
          />
          <table-delete-btn
            :loading="optionBtnLoading"
            @confirm="onDeleteConfirm(props.row.id)"
          />
        </q-td>
      </tr>
    </template>
  </q-table>

  <q-dialog ref="addDialog">
    <single-release-cdk-form-dialog :cdk="cdk!" @finish="onDataChange" />
  </q-dialog>
</template>

<script setup lang="ts">
import { PagedDataFetcher } from '@api/data-fetcher';
import { nextTick, onMounted, ref } from 'vue';
import { Pagination } from '@base/models';
import { SingleReleaseCdk, columns } from './models';
import { singleReleaseCdkService } from '@api/service';
import GLabel from '@base/GLabel.vue';
import { SingleReleaseCdkStatus, PeriodQueryStatus } from '@data/dict';
import SingleReleaseCdkFormDialog from './SingleReleaseCdkFormDialog.vue';
import noti from '@utils/notification';
import TableDeleteBtn from '@base/TableDeleteBtn.vue';
import { date } from 'quasar';
import stringUtils from '@utils/string';
import clipboardUtils from '@utils/clipboard';
import { DictItem } from '@data/modules';

const cdk = ref<SingleReleaseCdk>();
const addDialog = ref();
const addBtnLoading = ref(false);
const optionBtnLoading = ref(false);

function onDeleteAuthWordBtnClick(id: number) {
  dataFetcher.dataUpdate(singleReleaseCdkService.deleteAuthWord, id);
}

function onManageBtnClick(cdkManaging: SingleReleaseCdk) {
  cdk.value = cdkManaging;
  addDialog.value.show();
}

function onDataChange() {
  addDialog.value.hide();
  noti.success();
  dataFetcher.onDataChanged();
}

async function onAddBtnClick() {
  addBtnLoading.value = true;
  await dataFetcher.dataUpdate(singleReleaseCdkService.add, {
    srIds: [],
    expireAt: date.formatDate(new Date(), 'YYYY-MM-DD HH:mm:ss'),
    cdkStatus: 'ACTIVATED',
    remark: '新建访问控制',
  });
  addBtnLoading.value = false;
}

async function onDeleteConfirm(id: number) {
  optionBtnLoading.value = true;
  await dataFetcher.dataUpdate(singleReleaseCdkService.delete, id);
  optionBtnLoading.value = false;
}

async function onRowSave(row: SingleReleaseCdk) {
  await nextTick();
  row.srIds = row.singleReleases.map((sr) => sr.id);
  dataFetcher.dataUpdate(singleReleaseCdkService.update, row);
}

const rows = ref<Array<SingleReleaseCdk>>();
const loading = ref(false);
const pagination = ref();
const dataFetcher = PagedDataFetcher.of(
  singleReleaseCdkService.findList,
  pagination,
  loading,
  rows
);
const filterType = ref<DictItem>(PeriodQueryStatus.first()[1]);
const filterRemark = ref<string>();

function onResetBtnClick() {
  fetchData();
}

async function onRequest(props: { pagination: Pagination; filter: unknown }) {
  await dataFetcher.onRequest(props);
}

async function fetchData() {
  dataFetcher.onRequest({
    pagination: pagination.value,
    filter: {
      tab: filterType.value.value,
      remark: stringUtils.blankToNull(filterRemark.value),
    },
  });
}

onMounted(() => {
  pagination.value = Pagination.ofDefault();
  fetchData();
});
</script>

<style scoped>
.q-badge {
  min-height: 2rem;
}
</style>
