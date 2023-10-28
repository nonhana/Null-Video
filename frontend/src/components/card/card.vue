<template>
  <div class="card" :style="{ width, height }">
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

defineProps({
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '100%'
  }
})
</script>

<style scoped lang="less">
.card {
  border-radius: @border-radius;
  box-shadow: @shadow-outer;
  background: @bg-color;
  padding: 1rem;
}
</style>
