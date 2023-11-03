<template>
  <div class="index">
    <div class="title">
      <span>收藏列表</span>
    </div>
    <div class="content">
      <CollectionList />
    </div>
  </div>
</template>

<script setup lang="ts">
import CollectionList from '@/views/personalCenter/myCollections/collectionList.vue'
</script>

<style scoped lang="less">
.index {
  display: flex;
  flex-direction: column;
  height: 100%;

  .title {
    font-size: 1.5rem;
    font-weight: bold;
    color: @text;
  }

  .content {
    margin-top: 1rem;
    flex-grow: 1;
    width: 100%;
  }
}
</style>
