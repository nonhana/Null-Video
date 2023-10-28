<template>
  <div class="app">
    <div>
      <CommonHeader />
    </div>
    <div>
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import CommonHeader from "@/components/Little/Common/common-header.vue";
</script>

<style scoped lang="less">
.app {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  > div {
    margin-bottom: 2rem;
    width: 100%;
    display: flex;
    justify-content: center;
  }
}
</style>
