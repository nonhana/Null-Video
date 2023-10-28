<template>
  <div class="index">
    <div>
      <PersonalSideBar />
    </div>
    <div>
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import PersonalSideBar from "@/components/ModelPersonalCenter/personal-side-bar.vue";
</script>

<style scoped lang="less">
.index {
  position: relative;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
</style>
