<template>
  <div class="header">
    <n-grid :cols="48">
      <n-gi :span="8" :offset="1">
        <div class="logo">Null Video</div>
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
        <div class="avatar"></div>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { NGrid } from 'naive-ui';
import Button from '@nullVideo/button/button.vue'
import Search from '@nullVideo/form/search/search.vue'
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
}
</style>
