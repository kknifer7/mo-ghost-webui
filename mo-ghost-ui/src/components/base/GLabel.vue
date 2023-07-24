<template>
  <span :class="`${contentClass} no-selection`" @click="onClick">
    {{ clip ? stringUtils.clip(content, maxLength) : content }}
    <q-tooltip v-if="tip" :class="props.tipClass">
      {{ tipContent ? tipContent : content }}
    </q-tooltip>
  </span>
</template>

<script setup lang="ts">
import clipboardUtils from '@utils/clipboard';
import stringUtils from '@utils/string';

type Props = {
  content: string;
  contentClass?: string;
  clip?: boolean;
  copy?: boolean;
  maxLength?: number;
  tip?: boolean;
  tipContent?: string;
  tipClass?: string;
};
const props = withDefaults(defineProps<Props>(), {
  clip: true,
  copy: true,
  maxLength: 16,
  tip: true,
  tipClass: 'text-body1 bg-info',
});

function onClick() {
  if (props.copy) {
    clipboardUtils.save(props.content);
  }
}
</script>
