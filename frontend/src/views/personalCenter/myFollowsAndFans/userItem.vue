<template>
  <div class="PersonalCenterUserItem-wrapper">
    <div class="info">
      <div class="avatar">
        <img :src="userInfo.user_avatar" alt="avatar" />
      </div>
      <div class="name">
        <span>{{ userInfo.user_name }}</span>
        <span>{{ userInfo.user_signature }}</span>
      </div>
    </div>
    <Button
      style="font-weight: bold"
      :type="status ? '' : 'info'"
      @click="follow"
      >{{ status ? '取关' : '关注' }}</Button
    >
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { followActionAPI } from '@/api/user/user'
import type { UserInfo } from '@/utils/types'
import Button from '@nullVideo/button/button.vue'
import { useMessage } from 'naive-ui'

const userStore = useUserStore()
const message = useMessage()

const props = defineProps<{
  userInfo: UserInfo
  followStatus: 0 | 1
}>()

const status = ref<boolean>(props.followStatus === 0 ? true : false)

// 关注操作
const follow = async () => {
  status.value = !status.value
  const res = await followActionAPI({
    userId: userStore.userInfo.user_id!,
    followingId: props.userInfo.user_id!
  })
  if (res.code === 0) {
    message.success('操作成功')
  }
}
</script>

<style scoped lang="less">
.PersonalCenterUserItem-wrapper {
  position: relative;
  padding-right: 1rem;
  width: 100%;
  height: 4.75rem;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .info {
    display: flex;
    align-items: center;
    justify-content: flex-start;

    .avatar {
      width: 4rem;
      height: 4rem;
      border-radius: 50%;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .name {
      margin-left: 1.5rem;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      span {
        &:nth-child(1) {
          font-size: 20px;
          font-weight: normal;
          color: #3d3d3d;
        }

        &:nth-child(2) {
          font-size: 16px;
          color: #6d757a;
        }
      }
    }
  }
}
</style>
