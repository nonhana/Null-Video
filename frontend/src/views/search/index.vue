<template>
  <div class="search-page">
    <Card width="71rem" height="100%">
      <!-- 显示搜索关键词和结果数量 -->
      <div class="search-results">
        为 "{{ searchValue }}" 搜索到 {{ searchResults.length }} 个结果：
      </div>
      <div v-if="searchResults.length">
        <div v-for="video in searchResults" :key="video.videoId">
          <VideoItem :video-item="video" />
        </div>
      </div>
      <div class="empty" v-else>
        <n-empty />
      </div>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import Card from '@nullVideo/card/card.vue'
import VideoItem from '@nullVideo/basic/videoItem.vue'
import { searchVideoAPI } from '@/api/search/search.ts'
import { useRoute } from 'vue-router'

const route = useRoute()

const searchResults = ref([])
const searchValue = ref(route.params.search)

watch(
  route,
  async (newV, _) => {
    searchValue.value = newV.params.search as string
    const res = await searchVideoAPI({
      searchText: newV.params.search,
      option: 1
    })
    console.log(res, '@')
    if (res.code === 0) {
      searchResults.value = res.data.searchVideo
    }
  },
  {
    immediate: true
  }
)
</script>

<style scoped lang="less">
.search-page {
  position: relative;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-results {
  font-size: 1.5rem;
  color: @text;
  font-weight: bold;
}

.empty {
  margin-top: 40%;
}
</style>
