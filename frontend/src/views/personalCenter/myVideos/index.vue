<template>
  <div class="index">
    <div class="title">
      <span>视频列表</span>
    </div>
    <div class="content">
      <video-list />
    </div>
  </div>
</template>

<script setup lang="ts">
import videoList from '@/views/personalCenter/myVideos/videoList.vue'
</script>

<style scoped lang="less">
.index {
  display: flex;
  flex-direction: column;
  .title {
    width: 100%;
    padding: 1rem;
    display: flex;
    justify-content: flex-start;
    font-size: 24px;
    font-weight: bold;
    color: #3d3d3d;
  }
  .content {
    width: 100%;
    display: flex;
    justify-content: center;
  }
}
</style>
