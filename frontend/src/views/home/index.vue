<template>
  <div class="home">
    <n-grid :cols="48" x-gap="16px" style="height: 100%;">
      <n-gi :span="14">
        <Card>1</Card>
      </n-gi>
      <n-gi :span="18">
        <Card>1</Card>
      </n-gi>
      <n-gi :span="16">
        <Card>1</Card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { NGrid } from 'naive-ui';
import Card from "@nullVideo/card/card.vue"
</script>

<style scoped lang="less">
.home {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: start;
}
</style>
