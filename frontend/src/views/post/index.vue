<template>
  <div class="index">
    <div class="content">
      <transition name="fade" mode="out-in">
        <div v-if="currentStep === 1" key="step1">
          <videoUpload @next-step="currentStep = 2" />
        </div>
        <div v-else key="step2">
          <videoForm @previous-step="currentStep = 1" />
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import videoUpload from './videoUpload.vue'
import videoForm from './videoForm.vue'

// 当前步骤
const currentStep = ref<number>(1)
</script>

<style scoped lang="less">
.index {
  position: relative;
  width: 100%;
  display: flex;
  justify-content: center;
  .content {
    margin-top: 1rem;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
}
</style>
