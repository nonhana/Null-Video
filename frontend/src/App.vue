<template>
  <n-message-provider>
    <n-dialog-provider>
      <n-configProvider :theme-overrides="themeOverrides" style="height: 100%">
        <div class="app">
          <common-header />
          <div class="router">
            <router-view />
          </div>
        </div>
      </n-configProvider>
    </n-dialog-provider>
  </n-message-provider>
</template>

<script setup lang="ts">
import { NConfigProvider, GlobalThemeOverrides } from 'naive-ui'
import commonHeader from '@nullVideo/basic/commonHeader.vue'

// native-ui 全局主题变量
const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#4a91ee',
    primaryColorHover: '#4a91ee',
    primaryColorPressed: '#4a91ee'
  }
}
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
    padding: 1rem;
  }
}
</style>
