<template>
  <div class="VideoList-wrapper">
    <div style="position: relative; margin: 20px auto" v-if="loading">
      <n-spin size="large" />
    </div>
    <div v-else>
      <div v-if="videoList.length !== 0" class="list">
        <div v-for="video in videoList" :key="video.video_id">
          <video-item :video-item="video" />
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
      <div v-else>
        <n-empty description="暂无视频" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import type { VideoItemInfo } from '@/utils/types'
import { getMyVideoListAPI } from '@/api/video/video'
import videoItem from '@/views/personalCenter/videoItem.vue'

const route = useRoute()

// 视频列表
const videoList = ref<VideoItemInfo[]>([])
// 正在加载
const loading = ref<boolean>(false)

onMounted(async () => {
  loading.value = true
  const res = await getMyVideoListAPI({
    userId: route.params.user_id as string
  })
  if (res.code === 0) {
    res.data.forEach((item: any) => {
      videoList.value.push({
        video_id: item.videoId,
        video_cover: item.videoCoverUrl,
        video_viewnum: item.videoPlayNum
      })
    })
  }
  loading.value = false
})
</script>

<style scoped lang="less">
.VideoList-wrapper {
  position: relative;
  width: 100%;
  .list {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    overflow-y: scroll;
  }
  .placeholder {
    position: relative;
    margin: 0 0.5rem;
    width: 14.375rem;
    height: 20.625rem;
  }
}
</style>
