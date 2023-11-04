<template>
  <div class="CollectionList-wrapper">
    <div style="position: relative; height: 200px" v-if="loading">
      <n-spin size="large" />
    </div>
    <div v-else>
      <div v-if="collectionList.length !== 0" class="list">
        <div v-for="video in collectionList" :key="video.video_id">
          <video-item :video-item="video" />
        </div>
        <div
          class="placeholder"
          v-for="index in collectionList.length % 4 !== 0
            ? 4 - (collectionList.length % 4)
            : 0"
          :key="index"
        />
      </div>
      <div v-else>
        <n-empty description="暂无收藏视频" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { VideoItemInfo } from '@/utils/types'
import { useRoute } from 'vue-router'
import { getLikeCollectVideoListAPI } from '@/api/user/user'
import videoItem from '@/views/personalCenter/videoItem.vue'

const route = useRoute()

// 收藏的视频列表
const collectionList = ref<VideoItemInfo[]>([])
// 正在加载
const loading = ref<boolean>(false)

onMounted(async () => {
  loading.value = true
  const res = await getLikeCollectVideoListAPI({
    userId: route.params.user_id as string,
    option: 1
  })
  if (res.code === 0) {
    res.data.forEach((item: any) => {
      collectionList.value.push({
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
.CollectionList-wrapper {
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
