<template>
  <q-card style="max-width: 180rem; max-height: 30rem">
    <q-card-section class="">
      <div class="q-pa-sm row q-gutter-md">
        <div class="text-h6 no-selection">访问控制</div>
      </div>
    </q-card-section>

    <q-form @submit="onSubmit">
      <q-scroll-area
        class="q-ma-sm no-selection"
        style="width: 22rem; height: 15rem"
      >
        <q-card-section v-for="release in releases" :key="release.id">
          <div class="row items-center">
            <div class="col-10 row items-center">
              <g-label
                :content="release.fullName"
                content-class="text-body1"
                :max-length="13"
                :tip="stringUtils.isNotBlank(release.remark)"
                :tip-content="'备注: ' + release.remark"
                :copy="false"
              />
              <q-icon
                style="cursor: pointer"
                @click="onLinkIconClick(release.id)"
                class="q-ml-xs text-indigo-14"
                name="link"
                size="md"
              >
                <q-tooltip class="bg-info">
                  {{
                    singleReleaseCdkService.getDownloadLink(
                      cdk.code,
                      release.id
                    )
                  }}
                </q-tooltip>
              </q-icon>
              <span class="q-ml-sm no-selection">
                <q-badge
                  :color="SingleReleaseStatus.get(release.releaseStatus).color"
                  :label="SingleReleaseStatus.get(release.releaseStatus).label"
                />
              </span>
            </div>

            <q-checkbox class="col-2" v-model="selected" :val="release.id" />
          </div>
          <q-separator />
        </q-card-section>
      </q-scroll-area>

      <q-card-section class="q-gutter-sm" align="center">
        <q-btn type="submit" color="primary" label="提交" />
        <q-btn v-close-popup color="info" label="取消" />
      </q-card-section>
    </q-form>
  </q-card>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import stringUtils from '@utils/string';
import { SingleReleaseStatus } from '@data/dict';
import { SingleReleaseCdk } from '@/access-control/models';
import { SingleRelease } from '@/single-release/models';
import GLabel from '@/base/GLabel.vue';
import loadingUtils from '@utils/loading';
import { singleReleaseService, singleReleaseCdkService } from '@api/service';
import { Pagination } from '@base/models';
import clipboardUtils from '@utils/clipboard';

type Props = {
  cdk: SingleReleaseCdk;
};

const props = defineProps<Props>();

const emits = defineEmits(['finish']);

const releases = ref([] as SingleRelease[]);
const selected = ref([] as number[]);

function onSectionCheck(releaseId: number) {
  const idx = selected.value.indexOf(releaseId);

  if (idx === -1) {
    selected.value.push(releaseId);
  } else {
    selected.value.splice(idx, 1);
  }
}

function onLinkIconClick(releaseId: number) {
  clipboardUtils.save(
    singleReleaseCdkService.getDownloadLink(props.cdk.code, releaseId)
  );
}

async function onSubmit() {
  const data = JSON.parse(JSON.stringify(props.cdk));

  data.srIds = selected.value;
  delete data.singleReleases;
  delete data.totalAccess;
  delete data.lastAccessIP;
  delete data.lastAccessRegion;
  loadingUtils.show();
  await singleReleaseCdkService.update(data);
  loadingUtils.hide();
  emits('finish');
}

async function init() {
  const resp = await singleReleaseService.findList(
    Pagination.FIRST_PAGE_NUMBER - 1,
    Pagination.PAGE_MAX_SIZE
  );

  if (resp.data && resp.data.code === 200) {
    releases.value = resp.data.data.content;
  }
  selected.value = props.cdk.singleReleases.map((sr) => sr.id);
}

init();
</script>
