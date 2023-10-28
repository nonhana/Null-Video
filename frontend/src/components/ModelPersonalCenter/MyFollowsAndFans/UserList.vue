<template>
  <div class="UserList-wrapper"></div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import type { UserInfo } from "@/utils/types";

// 关注列表
const followList = ref<UserInfo[]>([]);
</script>

<style scoped lang="less">
.UserList-wrapper {
}
</style>
