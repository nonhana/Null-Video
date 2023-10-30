<template>
  <n-input
    :style="{
      width: width ?? '100%',
      height: height ?? 'auto'
    }"
    style="border-radius: 1rem"
    v-model:value="text"
    :type="type ?? 'text'"
    :placeholder="placeholder"
    :autosize="{
      minRows: minRows ?? 1,
      maxRows: maxRows ?? 1
    }"
    @input="input"
    @blur="blur"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  width?: string
  height?: string
  type?: 'text' | 'textarea' | 'password'
  placeholder: string
  value: string
  minRows?: number
  maxRows?: number
}>()

const emits = defineEmits<{
  (e: 'input', value: string): void
  (e: 'blur'): void
}>()

const text = ref<string>(props.value)

const input = (value: string) => {
  emits('input', value)
}
const blur = () => {
  emits('blur')
}
</script>
