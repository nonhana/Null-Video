<template>
  <div class="index">
    <div>
      <personal-side-bar />
    </div>
    <div>
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import personalSideBar from '@/views/personalCenter/personalSideBar.vue'
</script>

<style scoped lang="less">
.index {
  position: relative;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  > div:nth-child(1) {
    margin-right: 2rem;
  }
}
</style>
