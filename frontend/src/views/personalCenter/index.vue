<template>
  <div class="index">
    <div>
      <personal-side-bar />
    </div>
    <Card height="100%" width="64rem">
      <router-view />
    </Card>
  </div>
</template>

<script setup lang="ts">
import personalSideBar from '@/views/personalCenter/personalSideBar.vue'
import Card from '@nullVideo/card/card.vue'
</script>

<style scoped lang="less">
.index {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;

  >div {
    height: 100%;
    margin-right: 2rem;
  }
}
</style>
