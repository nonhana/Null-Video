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
  position: relative;
  display: flex;
  flex-direction: column;
  width: 64rem;
  height: 54rem;
  border-radius: 1rem;
  background: #f2f2f2;
  transition: all 0.3s;
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
  &:hover {
    box-shadow: 0rem 0.25rem 0.625rem 0rem rgba(0, 0, 0, 0.3);
  }
}
</style>
