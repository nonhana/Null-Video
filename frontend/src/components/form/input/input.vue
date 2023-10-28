<template>
  <div class="app">
    <n-input-group>
      <n-input :style="{ width: '50%' }" />
      <n-button type="primary" ghost> 搜索 </n-button>
    </n-input-group>
  </div>
</template>

<script setup lang="ts">
import { NInputGroup, NInput, NButton } from 'naive-ui'
</script>

<style scoped lang="less"></style>
