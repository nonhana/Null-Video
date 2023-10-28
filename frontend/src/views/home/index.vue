<template>
  <div class="home">
    <n-grid :cols="48" x-gap="16px" style="height: 100%">
      <n-gi :span="14">
        <Card>视频信息</Card>
      </n-gi>
      <n-gi :span="18">
        <Card>音视频播放</Card>
      </n-gi>
      <n-gi :span="16">
        <Card>
          <Comment />
        </Card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { NGrid } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'
import Comment from '@nullVideo/comment/comment.vue'
</script>

<style scoped lang="less">
.home {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: start;
}
</style>
