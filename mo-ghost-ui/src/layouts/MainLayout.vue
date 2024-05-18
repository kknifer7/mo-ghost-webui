<template>
  <q-layout view="lHh lpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />
        <q-toolbar-title class="no-selection">
          <q-avatar>
            <img src="~@img/logo.svg" />
          </q-avatar>
          Mo Ghost
        </q-toolbar-title>
        <q-space />
        <q-btn @click="onToggleShardingList" flat round icon="dvr">
          <q-tooltip :delay="500" class="text-h6"> 任务列表 </q-tooltip>
          <q-badge
            v-show="runningFlag"
            class="q-ma-sm"
            color="red"
            rounded
            floating
          />
        </q-btn>
        <q-space />
        <q-btn @click="onLogoutBtnClick" flat round icon="exit_to_app" />
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" bordered>
      <q-scroll-area class="fit">
        <q-list>
          <template v-for="menuItem in menuList" :key="menuItem.id">
            <menu-item
              class="no-selection"
              :id="menuItem.id"
              :icon="menuItem.icon"
              :label="menuItem.label"
              :separator="menuItem.separator"
              :children="menuItem.children"
              :path="menuItem.path"
            />
          </template>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>

  <q-dialog
    :class="shardingListDialogOpen ? '' : 'invisible'"
    seamless
    position="bottom"
    ref="shardingListDialog"
  >
    <sharding-list-dialog @dialog-close="onToggleShardingList" />
  </q-dialog>
</template>

<script setup lang="ts">
import { inject, onMounted, onUnmounted, ref } from 'vue';
import MenuItem from '@/menu/MenuItem.vue';
import menuList from '@/menu/menus';
import { securityService } from '@api/service';
import loadingUtils from '@utils/loading';
import { useRouter } from 'vue-router';
import noti from '@utils/notification';
import { Cookies, EventBus } from 'quasar';
import ShardingListDialog from '@/upload/ShardingListDialog.vue';
import { GlobalEvents } from '@base/models';
import { useUploaderStore } from 'src/stores/uploader-store';

const shardingListDialog = ref();
const shardingListDialogOpen = ref(false);
const leftDrawerOpen = ref(false);
const router = useRouter();
const runningFlag = ref(false);
const bus = inject('bus') as EventBus;

bus.on(GlobalEvents.NewUploadTask, () => {
  runningFlag.value = true;
  shardingListDialogOpen.value = true;
});
bus.on(GlobalEvents.FinishUploadTask, () => {
  if (useUploaderStore().getWorkingList.size === 0) {
    runningFlag.value = false;
  }
});

function onToggleShardingList() {
  shardingListDialogOpen.value = !shardingListDialogOpen.value;
}

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value;
}

async function onLogoutBtnClick() {
  loadingUtils.show();
  await securityService.logout();
  Cookies.remove('login');
  loadingUtils.hide();
  noti.success('再见~');
  router.replace('/login');
}

const beforeunloadFunc = (e: BeforeUnloadEvent) => {
  if (runningFlag.value) {
    e.returnValue = '尚有未完成的下载，确认要离开页面吗？';
  }

  return '有未完成的下载，确认要离开页面吗？';
};
onMounted(() => {
  shardingListDialog.value.toggle();
  window.addEventListener('beforeunload', beforeunloadFunc);
});

onUnmounted(() => {
  window.removeEventListener('beforeunload', beforeunloadFunc);
});
</script>
