<template>
  <div class="header">
    <n-grid :cols="48">
      <n-gi :span="8" :offset="1">
        <div class="logo" @click="jumpTo('home')">Null Video</div>
      </n-gi>
      <n-gi :span="16" :offset="5">
        <div class="search">
          <Search>
            <searchSVG />
          </Search>
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
        <div class="avatar">
          <n-dropdown
            v-if="userStore.userInfo.user_avatar"
            trigger="hover"
            :options="options"
            @select="handleSelect"
          >
            <img :src="userStore.userInfo.user_avatar" alt="userAvatar" />
          </n-dropdown>
          <div v-else @click="userStore.showLoginWindow()">登录</div>
        </div>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { NGrid } from 'naive-ui'
import Button from '@nullVideo/button/button.vue'
import Search from '@nullVideo/form/search/search.vue'
import searchSVG from '@nullSvg/search.svg'
import { useMessage } from 'naive-ui'

const router = useRouter()
const message = useMessage()

const userStore = useUserStore()

const options = [
  {
    label: '个人中心',
    key: 'personalCenter'
  },
  {
    label: '退出登录',
    key: 'emit'
  }
]

const handleSelect = (key: string) => {
  jumpTo(key)
}

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
      router.push({
        name: 'personalCenter',
        params: {
          user_id: userStore.userInfo.user_id
        }
      })
      break
    case 'emit':
      // 登出操作
      userStore.logout()
      localStorage.clear()
      message.success('退出登录成功，即将跳转至首页')
      router.push('/')
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
  cursor: pointer;

  img,
  div {
    width: 100%;
    height: 100%;
    border-radius: 100%;
  }

  div {
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: @bg-color-primary;
    color: white;
  }
}
</style>
