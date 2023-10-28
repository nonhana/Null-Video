<template>
  <div class="PersonalCenterVideoItem-wrapper">
    <div class="viewnum">
      <img :src="views" alt="views" />
      <span>{{ videoItem.video_viewnum }}</span>
    </div>
    <div class="background">
      <img :src="videoItem.video_cover" alt="video_cover" />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { VideoItemInfo } from "@/utils/types";
import views from "@/assets/svgs/views.svg";

defineProps<{
  videoItem: VideoItemInfo;
}>();
</script>

<style scoped lang="less">
.PersonalCenterVideoItem-wrapper {
  position: relative;
  margin: 0 0.5rem 1rem;
  width: 14.375rem;
  height: 20.625rem;
  border-radius: 1rem;
  .viewnum {
    position: relative;
    top: 18rem;
    left: 0.5rem;
    z-index: 1;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    color: #ffffff;
    img {
      width: 2rem;
      height: 2rem;
      margin-right: 0.5rem;
    }
  }
  .background {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 100%;
    border-radius: 1rem;
    overflow: hidden;
    > img {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
