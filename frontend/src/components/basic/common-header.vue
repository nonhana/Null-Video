<template>
  <div class="header">
    <n-grid :cols="48">
      <n-gi :span="8" :offset="1">
        <div class="logo" @click="jumpToHome">Null Video</div>
      </n-gi>
      <n-gi :span="16" :offset="5">
        <div class="search">
          <Search height="2.5rem" />
        </div>
      </n-gi>
      <n-gi :span="5" :offset="8">
        <div class="upload">
          <Button width="7.5rem" height="2.5rem">我要创作</Button>
        </div>
      </n-gi>
      <n-gi :span="1" :offset="3">
        <!-- <div class="message">header</div> -->
        <div class="avatar" @click="jumpToPersonalCenter" />
      </n-gi>
    </n-grid>
    <div class="window" v-if="windowVisable">
      <loginWindow @close="windowVisable = false" />
    </div>
    <div v-if="windowVisable" class="template" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { NGrid } from 'naive-ui'
import Button from '@nullVideo/button/button.vue'
import Search from '@nullVideo/form/search/search.vue'
import loginWindow from '@/views/home/loginWindow.vue'

const router = useRouter()

const userStore = useUserStore()

// 登录框是否显示
const windowVisable = ref<boolean>(false)
// 点击logo跳转到首页
const jumpToHome = () => {
  router.push('/')
}
// 点击头像，如果已经登录，跳转到个人中心；如果未登录，弹出登录框
const jumpToPersonalCenter = () => {
  if (userStore.isLogin) {
    router.push({
      name: 'personalCenter',
      params: {
        user_id: userStore.userInfo.user_id
      }
    })
  } else {
    windowVisable.value = true
    console.log(windowVisable.value)
  }
}
</script>

<style scoped lang="less">
.header {
  width: 100%;
  height: 6rem;
  border-radius: 0 0 1rem 1rem;
  opacity: 1;
  background: @bg-color;
  box-shadow: 0px 0.25rem 0.625rem 0px rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
}

.logo {
  font-family: @YouSheBiaoTiHei;
  font-size: 1.5rem;
  font-weight: normal;
  line-height: 2.5rem;
  letter-spacing: 0em;
  color: @text;
  cursor: pointer;
}

.search {
  width: 100%;
}

.upload {
  font-family: @YouSheBiaoTiHei;
}

.avatar {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 100%;
  background: @bg-color-primary;
  cursor: pointer;
}

.window {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}

.template {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 999;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}
</style>
