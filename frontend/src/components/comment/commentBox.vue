<template>
  <div class="comment-box">
    <n-input-group style="height: 3.5rem">
      <n-input
        class="comment-box-input"
        type="textarea"
        v-model="content"
        placeholder="善言善语,文明交流"
        maxlength="50"
        show-count
        :autosize="{
          minRows: 2,
          maxRows: 2
        }"
      />
      <n-button ghost class="comment-box-reply" @click="submitComment"
        >回复
      </n-button>
    </n-input-group>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { addCommentAPI } from '@/api/comment/comment'
import { useUserStore } from '@/stores/user'

const { videoId, commentId } = defineProps<{
  videoId: string
  commentId: string
}>()

const userStore = useUserStore()

const content = ref('')

// 提交评论
const submitComment = async () => {
  await addCommentAPI({
    videoId,
    userId: userStore.userInfo.user_id || '7',
    videoCommentId: commentId,
    videoCommentContent: '123'
  })
}
</script>

<style scoped lang="less">
.comment-box {
  height: 100%;
  margin-left: 3.5rem;
  &-input {
    height: 100%;
    border-radius: @border-radius;
  }

  &-reply {
    height: 100%;
    border-radius: @border-radius;
  }
}
</style>
