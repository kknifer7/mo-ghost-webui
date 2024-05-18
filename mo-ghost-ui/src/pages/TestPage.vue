<template>
  <q-btn label="打开" @click="showDialog" />
  <q-dialog seamless position="bottom" ref="dialog">
    <sharding-list-dialog />
  </q-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import ShardingListDialog from '../components/upload/ShardingListDialog.vue';

const dialog = ref();

function showDialog() {
  dialog.value?.show();
}
</script>
