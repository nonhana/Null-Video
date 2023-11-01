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
  .title {
    width: 100%;
    padding: 1rem;
    display: flex;
    justify-content: flex-start;
    font-size: 24px;
    font-weight: bold;
    color: #3d3d3d;
  }

  .content {
    width: 100%;
    display: flex;
    justify-content: center;
  }

}
</style>
