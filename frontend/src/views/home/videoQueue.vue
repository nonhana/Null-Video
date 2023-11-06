<template>
  <div class="videoQueue-wrapper">
    <div class="title">
      <span>视频列表</span>
    </div>
    <div class="list">
      <div
        class="list-item"
        v-for="video in videoQueue"
        :key="video.video_id"
        @click="jumpToVideoPage(video.video_id)"
      >
        <video-item :video-item="video" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { VideoItemInfo } from '@/utils/types'
import videoItem from '@nullVideo/basic/videoItem.vue'

const props = defineProps<{
  videoQueue: VideoItemInfo[]
  current: number
}>()

const emits = defineEmits<{
  (e: 'videoChange', current: number): void
}>()

const jumpToVideoPage = (video_id: string) => {
  const current = props.videoQueue.findIndex(
    (video) => video.video_id === video_id
  )
  console.log(current)
  emits('videoChange', current)
}
</script>

<style scoped lang="less">
.videoQueue-wrapper {
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
  .title {
    color: @text;
    font-size: 1rem;
    font-weight: bold;
  }
  .list {
    display: flex;
    height: 22rem;
    flex-direction: column;
    align-items: center;
    overflow-y: scroll;
  }
}
</style>
