<template>
  <div class="index">
    <div class="title">
      <div
        class="title-item"
        :class="presentStatus[0] ? 'hover' : ''"
        @mouseenter="changeStatus(0)"
        @mouseleave="refreshStatus"
        @click="type = 'follow'"
      >
        <span>关注列表</span>
      </div>
      <div
        class="title-item"
        :class="presentStatus[1] ? 'hover' : ''"
        @mouseenter="changeStatus(1)"
        @mouseleave="refreshStatus"
        @click="type = 'fan'"
      >
        <span>粉丝列表</span>
      </div>
    </div>
    <div class="content">
      <user-list :type="type" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import userList from '@/views/personalCenter/myFollowsAndFans/userList.vue'

const type = ref<'follow' | 'fan'>('follow')
const presentStatus = ref<boolean[]>([true, false])

const changeStatus = (index: number) => {
  presentStatus.value = [false, false]
  presentStatus.value[index] = true
}
const refreshStatus = () => {
  if (type.value === 'follow') {
    presentStatus.value = [true, false]
  } else {
    presentStatus.value = [false, true]
  }
}
</script>

<style scoped lang="less">
.index {
  display: flex;
  flex-direction: column;
  .title {
    width: 17rem;
    margin: 1rem;
    padding: 0.5rem;
    display: flex;
    justify-content: space-between;
    font-size: 24px;
    font-weight: bold;
    color: @text;
    border-radius: @border-radius;
    background: #e5e5e5;
    box-shadow: @shadow-inner;

    .title-item {
      width: 7.5rem;
      height: 3rem;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      border-radius: @border-radius;
      transition: all 0.3s;
    }

    .hover {
      background: #ffffff;
      box-shadow: @shadow-outer;
    }
  }

  .content {
    width: 100%;
    display: flex;
    justify-content: center;
  }
}
</style>
