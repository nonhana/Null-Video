<template>
    <div class="search">
        <n-input-group>
            <n-input class="search-input" />
            <n-button class="search-button"  ghost>
                <img src="@/assets/svgs/search.svg" alt="">
            </n-button>
        </n-input-group>
    </div>
</template>
  
<script setup lang="ts">
import { NInputGroup, NInput, NButton } from 'naive-ui'
</script>
  
<style scoped lang="less">
.search-input,
.search-button {
    border-radius: @border-radius;
    box-shadow: @shadow-inner;
}
</style>
  