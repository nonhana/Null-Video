<template>
  <div class="header">
    <n-grid :cols="48">
      <n-gi :span="8" :offset="1">
        <div class="logo" @click="jumpTo('home')">Null Video</div>
      </n-gi>
      <n-gi :span="16" :offset="5">
        <div class="search">
          <Search />
        </div>
      </n-gi>
      <n-gi :span="5" :offset="8">
        <div class="upload">
          <Button width="7.5rem" height="2.5rem" @click="jumpTo('post')"
            >我要创作</Button
          >
        </div>
      </n-gi>
      <n-gi :span="1" :offset="3">
        <div class="avatar" @click="jumpTo('personalCenter')">
          <img
            v-if="userStore.userInfo.user_avatar"
            :src="userStore.userInfo.user_avatar"
            alt="userAvatar"
          />
        </div>
      </n-gi>
    </n-grid>
    <transition name="dialog">
      <div class="window" v-if="windowVisable">
        <loginWindow @close="windowVisable = false" />
      </div>
    </transition>
    <transition name="template">
      <div v-if="windowVisable" class="template" />
    </transition>
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

// 根据传入的name跳转到对应的页面
const jumpTo = (name: string) => {
  switch (name) {
    case 'home':
      router.push('/')
      break
    case 'post':
      router.push('/postVideo')
      break
    case 'personalCenter':
      console.log(userStore.token)
      if (userStore.token !== '') {
        router.push({
          name: 'personalCenter',
          params: {
            user_id: userStore.userInfo.user_id
          }
        })
      } else {
        windowVisable.value = true
      }
      break
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
  overflow: hidden;
  background: @bg-color-primary;
  cursor: pointer;
  img {
    width: 100%;
    height: 100%;
    border-radius: 100%;
  }
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

/* 蒙版渐入渐出 */
.template-enter-active,
.template-leave-active {
  transition: opacity 0.3s;
}
.template-enter-from,
.template-leave-to {
  opacity: 0;
}

/* dialog从上到下，渐入渐出 */
.dialog-enter-active,
.dialog-leave-active {
  transition: all 0.3s;
}
.dialog-enter-from,
.dialog-leave-to {
  transform: translateY(-100%);
  opacity: 0;
}
</style>
