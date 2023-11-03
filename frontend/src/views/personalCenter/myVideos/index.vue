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
  height: 100%;

  .title {
    font-size: 1.5rem;
    font-weight: bold;
    color: @text;
  }

  .content {
    flex-grow: 1;
    margin-top: 1rem;
    width: 100%;
    height: 100%;
  }
}
</style>
