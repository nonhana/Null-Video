<template>
  <div class="search" :style="{ height }">
    <n-input-group>
      <n-input
        :style="{ height }"
        class="search-input"
        :placeholder="placeholder"
        v-model:value="inputValue"
        @input="input"
      />
      <n-button
        :style="{ height }"
        class="search-button"
        ghost
        @click="clickEvent()"
      >
        <slot></slot>
      </n-button>
    </n-input-group>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { NInputGroup, NInput, NButton } from 'naive-ui'

const { height, placeholder, value } = defineProps<{
  height?: string
  placeholder?: string
  value: string
  clickEvent: () => void
}>()

const emits = defineEmits<{
  (e: 'input', value: string): void
}>()

const inputValue = ref(value)

const input = (value: string) => {
  emits('input', value)
}
</script>

<style scoped lang="less">
.search-input,
.search-button {
  // height: 100%;
  border-radius: @border-radius;
  box-shadow: @shadow-inner;
}
</style>
