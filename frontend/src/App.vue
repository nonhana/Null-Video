<template>
  <n-notification-provider>
    <n-message-provider>
      <n-dialog-provider>
        <n-configProvider
          :theme-overrides="themeOverrides"
          style="height: 100%"
        >
          <div class="app">
            <common-header />
            <div class="router">
              <router-view />
            </div>
            <loginWindow v-if="userStore.isLoginWindowShow" />
          </div>
        </n-configProvider>
      </n-dialog-provider>
    </n-message-provider>
  </n-notification-provider>
</template>

<script setup lang="ts">
import { watch } from 'vue'
import { useRoute } from 'vue-router'
import { NConfigProvider, GlobalThemeOverrides } from 'naive-ui'
import commonHeader from '@nullVideo/basic/commonHeader.vue'
import loginWindow from './views/home/loginWindow.vue'
import { useUserStore } from './stores/user'

const userStore = useUserStore()
const route = useRoute()

// native-ui 全局主题变量
const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#4a91ee',
    primaryColorHover: '#4a91ee',
    primaryColorPressed: '#4a91ee'
  },
  Divider: {
    color: '#D4D4D4'
  }
}

watch(
  route,
  async (newV, _) => {
    // 监听登录
    if (newV.params.login === 'login') {
      userStore.showLoginWindow()
    }
  },
  {
    immediate: true
  }
)
</script>

<style scoped lang="less">
.app {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  .router {
    width: 100%;
    height: 100%;
    padding: 2rem;
  }
}
</style>
