<template>
  <div class="VideoList-wrapper">
    <div v-for="video in videoList" :key="video.video_id">
      <PersonalCenterVideoItem :video-item="video" />
    </div>
    <!-- 占位div -->
    <div
      class="placeholder"
      v-for="index in videoList.length % 4 !== 0
        ? 4 - (videoList.length % 4)
        : 0"
      :key="index"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import type { VideoItemInfo } from "@/utils/types";
import PersonalCenterVideoItem from "@/components/Little/Items/PersonalCenterVideoItem.vue";

// 视频列表
const videoList = ref<VideoItemInfo[]>([
  {
    video_id: 1,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 2,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 3,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 4,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 5,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 6,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 7,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
  {
    video_id: 8,
    video_cover: "https://dummyimage.com/230X330",
    video_viewnum: 100,
  },
]);
</script>

<style scoped lang="less">
.VideoList-wrapper {
  position: relative;
  width: 100%;
  height: 48rem;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  overflow-y: scroll;
  .placeholder {
    position: relative;
    margin: 0 0.5rem;
    width: 14.375rem;
    height: 20.625rem;
  }
}
</style>
