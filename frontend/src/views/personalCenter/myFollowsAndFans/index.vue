<template>
  <div class="index">
    <div class="title">
      <div class="title-bottom" :class="presentStatus[0] ? '' : 'register'">
      </div>
      <div class="title-item" :class="presentStatus[0] ? 'selected' : ''"
        @click="type = 'follow'; presentStatus = [true, false]">
        <span>关注列表</span>
      </div>
      <div class="title-item" :class="presentStatus[1] ? 'selected' : ''"
        @click="type = 'fan'; presentStatus = [false, true]">
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
  height: 100%;

  .title {
    position: relative;
    padding: 0.5rem;
    display: flex;
    justify-content: space-between;
    border-radius: @border-radius;
    background: #e5e5e5;
    box-shadow: @shadow-inner;

    .title-bottom {
      position: absolute;
      width: calc(50% - 0.5rem);
      height: calc(100% - 1rem);
      background-color: #fff;
      box-shadow: @shadow-outer;
      border-radius: @border-radius;
      z-index: 1;
      transition: all 0.3s;
    }

    .register {
      transform: translateX(100%);
    }


    .title-item {
      padding: 0.5rem;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      font-size: 1.5rem;
      font-weight: bold;
      color: @text-secondary;
      border-radius: @border-radius;
      transition: all 0.3s;
      z-index: 2;
    }

    .title-item:hover {
      color: @text;
    }

    .selected {
      color: @text;
    }
  }

  .content {
    margin-top: 1rem;
    flex-grow: 1;
    width: 100%;
  }
}
</style>
