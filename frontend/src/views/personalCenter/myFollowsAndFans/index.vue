<template>
  <div class="index">
    <div class="title">
      <span>我的关注</span>
    </div>
    <div class="content">
      <user-list />
    </div>
  </div>
</template>

<script setup lang="ts">
import userList from '@/views/personalCenter/myFollowsAndFans/userList.vue'
</script>

<style scoped lang="less">
.index {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 64rem;
  height: 54rem;
  border-radius: 1rem;
  background: @bg-color;
  transition: all 0.3s;
  .title {
    width: 100%;
    padding: 1rem;
    display: flex;
    justify-content: flex-start;
    font-size: 24px;
    font-weight: bold;
    color: @text;
  }
  .content {
    width: 100%;
    display: flex;
    justify-content: center;
  }
  &:hover {
    box-shadow: @shadow-outer;
  }
}
</style>
