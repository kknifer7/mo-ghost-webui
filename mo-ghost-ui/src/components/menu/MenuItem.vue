<template>
  <q-item
    clickable
    v-ripple
    :to="path"
    v-if="!children || children.length === 0"
  >
    <q-item-section avatar>
      <q-icon :name="icon" />
    </q-item-section>
    <q-item-section> {{ label }} </q-item-section>
    <q-separator :key="'sep' + id" v-if="separator" />
  </q-item>

  <q-expansion-item clickable default-opened :icon="icon" :label="label" v-else>
    <template v-for="item in children" :key="item.id">
      <menu-item
        :id="item.id"
        :icon="item.icon"
        :label="item.label"
        :separator="item.separator"
        :children="item.children"
        :path="item.path"
        style="padding-left: 3rem"
      />
    </template>
    <q-separator :key="id" v-if="separator" />
  </q-expansion-item>
</template>

<script setup lang="ts">
import { Menu } from '@base/models';

withDefaults(defineProps<Menu>(), {
  separator: false,
});
</script>
