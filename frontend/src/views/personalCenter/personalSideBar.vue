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
      <div
        class="menu-item"
        :class="hovering[0] ? 'menu-hover' : ''"
        @click="chooseItem(0)"
        @mouseenter="hoverItem(0, 0)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[0] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myVideos" alt="myVideos" />
        <span>发布视频</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[1] ? 'menu-hover' : ''"
        @click="chooseItem(1)"
        @mouseenter="hoverItem(0, 1)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[1] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myCollections" alt="myCollections" />
        <span>收藏列表</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[2] ? 'menu-hover' : ''"
        @click="chooseItem(2)"
        @mouseenter="hoverItem(0, 2)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[2] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myFollowsAndFans" alt="myFollowsAndFans" />
        <span>关注/粉丝</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[3] ? 'menu-hover' : ''"
        @click="chooseItem(3)"
        @mouseenter="hoverItem(0, 3)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[3] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myInfo" alt="myInfo" />
        <span>个人信息</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[4] ? 'menu-hover' : ''"
        @click="chooseItem(4)"
        @mouseenter="hoverItem(0, 4)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[4] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="exit" alt="exit" />
        <span>退出登录</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { UserInfo } from '@/utils/types'
import arrowRight from '@/assets/svgs/arrow-right.svg'
import myVideos from '@/assets/svgs/my-videos.svg'
import myCollections from '@/assets/svgs/my-collections.svg'
import myFollowsAndFans from '@/assets/svgs/my-follows-and-fans.svg'
import myInfo from '@/assets/svgs/my-info.svg'
import exit from '@/assets/svgs/exit.svg'

const route = useRoute()
const router = useRouter()

const userStore = useUserStore()

const userInfo = ref<UserInfo>()
const hovering = ref<boolean[]>([false, false, false, false, false])

const routeNameList = [
  'myVideos',
  'myCollections',
  'myFollowsAndFans',
  'myInfo',
  'exit'
]

// 鼠标移动到菜单项时，箭头向右移动，其他菜单项箭头复位
const hoverItem = (type: number, index?: number) => {
  if (type === 0) {
    hovering.value = hovering.value.map((_, i) => {
      if (i === index) {
        return true
      } else {
        return false
      }
    })
  } else {
    // 根据当前路由，判断是否需要将箭头复位
    hovering.value = hovering.value.map((_, i) => {
      if (routeNameList.findIndex((item) => item === route.name) === i) {
        return true
      } else {
        return false
      }
    })
  }
}
// 点击菜单项时，跳转到对应页面
const chooseItem = (index: number) => {
  switch (index) {
    case 0:
      router.push(`/personalCenter/${route.params.user_id}/myVideos`)
      break
    case 1:
      router.push(`/personalCenter/${route.params.user_id}/myCollections`)
      break
    case 2:
      router.push(`/personalCenter/${route.params.user_id}/myFollowsAndFans`)
      break
    case 3:
      router.push(`/personalCenter/${route.params.user_id}/myInfo`)
      break
    case 4:
      router.push(`/personalCenter/${route.params.user_id}/exit`)
      break
  }
}

watch(
  () => route.params.user_id,
  (user_id) => {
    if (Number(user_id) === userStore.userInfo.user_id) {
      userInfo.value = userStore.userInfo
    } else {
      userInfo.value = {
        user_id: 2,
        user_name: 'JaneDoe',
        user_signature: 'Another signature.',
        user_avatar: 'https://dummyimage.com/400X400',
        user_likenum: 111,
        user_collectnum: 222,
        user_follownum: 333,
        user_fansnum: 444
      }
    }
  },
  { immediate: true, deep: true }
)

watch(
  () => route.name,
  (name) => {
    hovering.value[routeNameList.findIndex((item) => item === name)] = true
  },
  { immediate: true }
)
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
      margin: 1rem 0;
      width: 20rem;
      height: 4rem;
      padding: 0 1rem;
      border-radius: 1rem;
      background: #f2f2f2;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 1.2rem;
      color: #3d3d3d;
      transition: all 0.3s;
      cursor: pointer;
      img {
        &:nth-child(1) {
          position: relative;
          width: 1rem;
          height: 1rem;
          transition: all 0.3s;
        }
        &:nth-child(2) {
          margin: 0 2rem 0 2rem;
          width: 2rem;
          height: 2rem;
        }
      }
      &:hover {
        background: #e5e5e5;
        box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
      }
    }
  }
  .menu-hover {
    background: #e5e5e5;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  }
}
</style>
