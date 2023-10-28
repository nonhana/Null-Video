<template>
  <div class="home">
    <Card></Card>
    <Card></Card>
    <Card></Card>
  </div>
</template>

<script setup lang="ts">
import Card from "@nullVideo/card/card.vue"
</script>

<style scoped lang="less">
.home {
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
  border: solid red 0.1px;
  justify-content: start;
}
</style>
