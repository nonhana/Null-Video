<template>
  <n-button class="my-button" :style="{ width, height }" type="primary">
    <slot></slot>
  </n-button>
</template>

<script setup lang="ts">
import { NButton } from 'naive-ui'

defineProps({
  width: {
    type: String
  },
  height: {
    type: String
  }
})
</script>

<style scoped lang="less">
.my-button {
  position: relative;
  border-radius: @border-radius;
  box-shadow: @shadow-outer;
  padding: 1rem;
}
</style>
