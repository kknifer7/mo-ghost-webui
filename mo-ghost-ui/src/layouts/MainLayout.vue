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
</template>

<script setup lang="ts">
import { ref } from 'vue';
import MenuItem from 'src/components/menu/MenuItem.vue';
import menuList from 'src/components/menu/menus';
import { securityService } from '@api/service';
import loadingUtils from '@utils/loading';
import { useRouter } from 'vue-router';
import noti from '@utils/notification';
import { Cookies } from 'quasar';

const leftDrawerOpen = ref(false);
const router = useRouter();

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
</script>
