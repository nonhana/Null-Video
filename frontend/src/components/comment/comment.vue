<template>
  <div class="comment">
    <div class="comment-list-container">
      <CommentList
        :comment-data="commentList"
        :video-id="videoId"
        :comment-callback="commentCallback"
        :author_id="author_id"
      />
      <!-- 当评论列表为空时，显示空状态提示 -->
      <div class="empty-state" v-if="!commentList.length">
        <!-- 这里使用你的 native-ui 的 empty 组件 -->
        <n-empty />
      </div>
    </div>

    <div class="comment-input">
      <Search
        :value="commentValue"
        @input="updateName"
        :placeholder="'善言善语，文明交流'"
        :click-event="addComment"
      >
        <commentSVG />
      </Search>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import CommentList from './commentList.vue'
import Search from '@nullVideo/form/search/search.vue'
import commentSVG from '@nullSvg/comment.svg'
import { getCommentAPI, addCommentAPI } from '@/api/comment/comment'
import { getCommentAPIResponse } from '@/api/comment/types'
import { useUserStore } from '@/stores/user'

const { author_id } = defineProps<{
  author_id: string
}>()

const route = useRoute()
const userStore = useUserStore()

const videoId = ref<string>(route.params.video_id as string)
const commentList = ref<getCommentAPIResponse[]>([])
const commentValue = ref<string>('')

// 更新commentValue
const updateName = (value: string) => {
  commentValue.value = value
}

// 获取评论框的回调数据
const commentCallback = (comment: getCommentAPIResponse) => {
  console.log('get', comment)
  commentList.value.forEach((item: getCommentAPIResponse, index: number) => {
    // 匹配评论
    if (item.videoCommentId === comment.videoCommentId) {
      commentList.value[index] = comment
    }
  })
}

const getComment = async () => {
  const res = await getCommentAPI({
    videoId: videoId.value,
    userId: userStore.userInfo.user_id as string
  })
  if (res.code === 0) {
    commentList.value.length = 0
    commentList.value.push(...res.data)
  }
}

const addComment = async () => {
  const res = await addCommentAPI({
    videoId: videoId.value,
    userId: userStore.userInfo.user_id as string,
    videoCommentContent: commentValue.value
  })
  if (res.code === 0) commentList.value.unshift(res.data)
}

watch(
  () => route.params.video_id,
  (newV) => {
    console.log('comment', newV)
    videoId.value = newV as string
    getComment()
  },
  { immediate: true }
)
</script>

<style scoped lang="less">
.comment {
  display: flex;
  flex-direction: column;
  justify-content: space-between; // 确保评论列表和评论框分开
  height: 100%;

  .comment-list-container {
    flex: 1; // 让评论列表占据剩余的空间
    overflow-y: auto;
    height: 100%;
  }

  .comment-input {
    // 现在不需要定位了
    width: 100%;
  }

  .empty-state {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%; // 使得空状态提示居中
  }
}
</style>
