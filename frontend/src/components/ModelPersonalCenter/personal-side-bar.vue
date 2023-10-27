<template>
  <div class="PersonalSideBar-wrapper">
    <div class="info">
      <div class="header">
        <div class="avatar">
          <img :src="userInfo!.user_avatar" alt="user_avatar" />
        </div>
        <div class="name">
          <span>{{ userInfo!.user_name }}</span>
          <span>{{ userInfo!.user_id }}</span>
        </div>
        <n-button round type="info">关注</n-button>
      </div>
      <div class="data">
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>获赞</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>收藏</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>关注</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>粉丝</span>
        </div>
      </div>
      <div class="signature">
        <span>{{ userInfo!.user_signature }}</span>
      </div>
    </div>
    <el-divider />
    <div class="menu">
      <div class="menu-item">
        <img :src="arrowRight" alt="arrowRight" />
        <img :src="myVideos" alt="myVideos" />
        <span>发布视频</span>
      </div>
      <div class="menu-item">
        <img :src="arrowRight" alt="arrowRight" />
        <img :src="myCollections" alt="myCollections" />
        <span>收藏列表</span>
      </div>
      <div class="menu-item">
        <img :src="arrowRight" alt="arrowRight" />
        <img :src="myFollowsAndFans" alt="myFollowsAndFans" />
        <span>关注/粉丝</span>
      </div>
      <div class="menu-item">
        <img :src="arrowRight" alt="arrowRight" />
        <img :src="myInfo" alt="myInfo" />
        <span>个人信息</span>
      </div>
      <div class="menu-item">
        <img :src="arrowRight" alt="arrowRight" />
        <img :src="exit" alt="exit" />
        <span>退出登录</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores/user";
import type { UserInfo } from "@/utils/types";
import arrowRight from "@/assets/svgs/arrow-right.svg";
import myVideos from "@/assets/svgs/my-videos.svg";
import myCollections from "@/assets/svgs/my-collections.svg";
import myFollowsAndFans from "@/assets/svgs/my-follows-and-fans.svg";
import myInfo from "@/assets/svgs/my-info.svg";
import exit from "@/assets/svgs/exit.svg";

const route = useRoute();

const userStore = useUserStore();

const userInfo = ref<UserInfo>();

watch(
  () => route.params.user_id,
  (user_id) => {
    if (Number(user_id) === userStore.userInfo.user_id) {
      userInfo.value = userStore.userInfo;
    } else {
      userInfo.value = {
        user_id: 2,
        user_name: "JaneDoe",
        user_signature: "Another signature.",
        user_avatar: "https://dummyimage.com/400X400",
        user_likenum: 111,
        user_collectnum: 222,
        user_follownum: 333,
        user_fansnum: 444,
      };
    }
  },
  { immediate: true, deep: true }
);
</script>

<style scoped lang="less">
.PersonalSideBar-wrapper {
  position: relative;
  width: 22rem;
  height: 53rem;
  padding: 1rem;
  border-radius: 1rem;
  background: #f2f2f2;
  transition: all 0.3s;
  &:hover {
    box-shadow: 0rem 0.25rem 0.625rem 0rem rgba(0, 0, 0, 0.3);
  }
  .info {
    width: 100%;
    height: 15.625rem;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: space-between;
    .header {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .avatar {
        width: 4rem;
        height: 4rem;
        border-radius: 2rem;
        img {
          width: 4rem;
        }
      }
      .name {
        display: flex;
        flex-direction: column;
        span {
          &:nth-child(1) {
            font-family: Source Han Sans;
            font-size: 24px;
            color: #3d3d3d;
          }
          &:nth-child(2) {
            font-family: Source Han Sans;
            font-size: 16px;
            color: #6d757a;
          }
        }
      }
    }
    .data {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      &-item {
        span {
          font-family: Source Han Sans;
          font-size: 16px;
          font-weight: bold;
          color: #3d3d3d;
          &:nth-child(2) {
            font-weight: normal;
          }
        }
      }
    }
    .signature {
      width: 100%;
      padding: 0.75rem 1rem;
      border-radius: 1rem;
      border: 1px solid #d4d4d4;
      font-family: Source Han Sans;
      font-size: 1rem;
      color: #3d3d3d;
    }
  }
  .menu {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    &-item {
      width: 349px;
      height: 66.18px;
      border-radius: 16px;
      background: #f2f2f2;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-family: Source Han Sans;
      font-size: 18px;
      color: #3d3d3d;
      img {
        &:nth-child(1) {
          width: 16px;
          height: 16px;
        }
        &:nth-child(2) {
          width: 32px;
          height: 32px;
        }
      }
    }
  }
}
</style>
