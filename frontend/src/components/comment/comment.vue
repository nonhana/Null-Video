<template>
  <div class="comment">
    <CommentList :comment-data="commentList" />
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import CommentList from './commentList.vue'
const commentList = ref<any[]>([])

// mock评论数据
function generateReview(count = 0, isChild = false): unknown {
  const id = Math.random().toString(36).substring(7)
  const name = Math.random().toString(36).substring(7)
  const comment = Math.random().toString(36).substring(7)
  const time = new Date().toISOString()
  const header = `https://picsum.photos/id/${Math.floor(
    Math.random() * 100
  )}/100`
  const children = []
  if (count < 5 && !isChild) {
    for (let i = 0; i < Math.floor(Math.random() * 3); i++) {
      children.push(generateReview(count + 1, true))
    }
  }
  return {
    comment_id: id,
    comment_to: isChild
      ? {
          id: Math.random().toString(36).substring(7),
          name: Math.random().toString(36).substring(7)
        }
      : null,
    user: {
      id,
      name,
      header
    },
    comment,
    time,
    children,
    isChild
  }
}

onMounted(() => {
  // 获取mock数据
  for (let i = 0; i < 3; i++) {
    commentList.value.push(generateReview())
  }
})
</script>

<style scoped lang="less">
.comment {
  width: 100%;
}
</style>
