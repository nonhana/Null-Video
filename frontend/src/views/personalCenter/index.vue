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
  justify-content: space-between;
  align-items: flex-start;
}
</style>
