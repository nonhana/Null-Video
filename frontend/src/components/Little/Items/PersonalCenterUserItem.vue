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
    <n-button :type="followStatus ? 'default' : 'info'">{{
      followStatus ? "取关" : "关注"
    }}</n-button>
  </div>
</template>

<script setup lang="ts">
import type { UserInfo } from "@/utils/types";

defineProps<{
  userInfo: UserInfo;
  followStatus: boolean;
}>();
</script>

<style scoped lang="less">
.PersonalCenterUserItem-wrapper {
  position: relative;
  width: 59.25rem;
  height: 4.75rem;
  border-radius: 1rem;
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
