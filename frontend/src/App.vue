<template>
  <n-configProvider :theme-overrides="themeOverrides">
    <div class="app">
      <CommonHeader />
    </div>
    <div>
      <router-view />
    </div>
  </n-configProvider>
</template>

<script setup lang="ts">
import { NConfigProvider, GlobalThemeOverrides } from "naive-ui";
import CommonHeader from "@nullVideo/basic/common-header.vue";

// native-ui 全局主题变量
const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: "#4a91ee",
    primaryColorHover: "#4a91ee",
    primaryColorPressed: "#4a91ee",
  },
};
</script>

<style scoped lang="less">
.app {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  > div {
    margin-bottom: 2rem;
    width: 100%;
    display: flex;
    justify-content: center;
  }
}
</style>
