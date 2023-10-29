<template>
  <n-button class="my-button" :style="{ width, height }" type="primary">
    <slot></slot>
  </n-button>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import { NButton } from 'naive-ui'

defineProps({
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: 'auto'
  }
})
</script>

<style scoped lang="less">
.my-button {
  position: relative;
  /* Add more styles as needed */
  border-radius: @border-radius;
  box-shadow: @shadow-outer;
}
</style>
