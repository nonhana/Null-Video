<template>
  <div class="header">
    <div class="logo">Null Video</div>
    <div class="search">
      <input type="text" />
    </div>
    <div class="input">
      <Button width="8rem" height="2rem">input</Button>
    </div>
    <!-- <div class="message">header</div> -->
    <div class="avatar"></div>
  </div>
</template>

<script setup lang="ts">
import Button from '@nullVideo/button/button.vue'
</script>

<style scoped lang="less">
.header {
  width: 100%;
  height: 5.5rem;
  border-radius: 0 0 1rem 1rem;
  opacity: 1;
  background: #f2f2f2;
  box-shadow: 0px 0.25rem 0.625rem 0px rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
}

.logo {
  font-family: YouSheBiaoTiHei;
  font-size: 1.5rem;
  font-weight: normal;
  line-height: normal;
  letter-spacing: 0em;
  color: #504f4f;
}

.search {}

.input {}

.avatar {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 100%;
  background: #4a91ee;
}
</style>
