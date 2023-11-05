<template>
  <div class="comment">
    <CommentList :comment-data="commentList" :video-id="videoId" />
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
import { ref, onMounted } from 'vue'
import CommentList from './commentList.vue'
import Search from '@nullVideo/form/search/search.vue'
import commentSVG from '@nullSvg/comment.svg'
import { getCommentAPI, addCommentAPI } from '@/api/comment/comment'
import { getCommentAPIResponse } from '@/api/comment/types'

const { videoId, videoCommentUserId } = defineProps<{
  videoId: string
  videoCommentUserId: string
}>()

const commentList = ref<getCommentAPIResponse[]>([])
const commentValue = ref<string>('')

// 更新commentValue
const updateName = (value: string) => {
  commentValue.value = value
}

const getComment = async () => {
  const res = await getCommentAPI({
    videoId,
    userId: '7'
  })
  commentList.value.length = 0
  commentList.value.push(...res.data)
}

const addComment = async () => {
  await addCommentAPI({
    videoId,
    userId: '7',
    videoCommentContent: commentValue.value
  })
  getComment()
}

onMounted(() => {
  getComment()
})
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
