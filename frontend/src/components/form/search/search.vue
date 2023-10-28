<template>
    <div class="search" :style="{ height }">
        <n-input-group>
            <n-input :style="{ height }" class="search-input" :placeholder="placeholder" />
            <n-button :style="{ height }" class="search-button" ghost>
                <img src="@/assets/svgs/search.svg" alt="">
            </n-button>
        </n-input-group>
    </div>
</template>
  
<script setup lang="ts">
import { defineProps } from 'vue';
import { NInputGroup, NInput, NButton } from 'naive-ui'


defineProps({
    height: {
        type: String,
        default: 'auto',
    },
    placeholder: {
        type: String,
        default: '请输入要搜索的内容',
    },

});
</script>
  
<style scoped lang="less">
.search-input,
.search-button {
    // height: 100%;
    border-radius: @border-radius;
    box-shadow: @shadow-inner;
}
</style>
  