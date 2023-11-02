<template>
  <div class="CollectionList-wrapper">
    <div v-for="video in CollectionList" :key="video.video_id">
      <video-item :video-item="video" />
    </div>
    <!-- 占位div，如果被4整除便无需占位 -->
    <div
      class="placeholder"
      v-for="index in CollectionList.length % 4 !== 0
        ? 4 - (CollectionList.length % 4)
        : 0"
      :key="index"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { VideoItemInfo } from '@/utils/types'
import videoItem from '@/views/personalCenter/videoItem.vue'

// 收藏的视频列表
const CollectionList = ref<VideoItemInfo[]>([
  {
    video_id: 1,
    video_cover: 'https://dummyimage.com/230X330',
    video_viewnum: 100
  },
  {
    video_id: 2,
    video_cover: 'https://dummyimage.com/230X330',
    video_viewnum: 100
  },
  {
    video_id: 3,
    video_cover: 'https://dummyimage.com/230X330',
    video_viewnum: 100
  },
  {
    video_id: 4,
    video_cover: 'https://dummyimage.com/230X330',
    video_viewnum: 100
  },
  {
    video_id: 5,
    video_cover: 'https://dummyimage.com/230X330',
    video_viewnum: 100
  },
  {
    video_id: 6,
    video_cover: 'https://dummyimage.com/230X330',
    video_viewnum: 100
  }
])
</script>

<style scoped lang="less">
.CollectionList-wrapper {
  position: relative;
  width: 100%;
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
