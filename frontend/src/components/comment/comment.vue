<template>
  <div class="comment">
    <CommentList
      :comment-data="commentList"
      :video-id="videoId"
      :comment-callback="commentCallback"
    />
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
import CommentList from './commentList.vue'
import Search from '@nullVideo/form/search/search.vue'
import commentSVG from '@nullSvg/comment.svg'
import { getCommentAPI, addCommentAPI } from '@/api/comment/comment'
import { getCommentAPIResponse } from '@/api/comment/types'
import { useUserStore } from '@/stores/user'

const { videoId } = defineProps<{
  videoId: string
}>()

const userStore = useUserStore()

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
    videoId,
    userId: userStore.userInfo.user_id as string
  })
  if (res.code === 0) {
    commentList.value.length = 0
    commentList.value.push(...res.data)
  }
}

const addComment = async () => {
  const res = await addCommentAPI({
    videoId,
    userId: userStore.userInfo.user_id as string,
    videoCommentContent: commentValue.value
  })
  if (res.code === 0) commentList.value.unshift(res.data)
}

watch(
  () => videoId,
  () => {
    getComment()
  },
  { immediate: true }
)
</script>

<style scoped lang="less">
.comment {
  position: relative;
  width: 100%;
  height: 100%;

  .comment-input {
    width: 100%;
    position: absolute;
    bottom: 0;
  }
}
</style>
