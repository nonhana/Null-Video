<template>
  <div class="index">
    <Card width="71rem" height="100%">
      <transition name="fade" mode="out-in">
        <div v-if="currentStep === 1" key="step1">
          <videoUpload @next-step="currentStep = 2" />
        </div>
        <div v-else key="step2">
          <videoForm @previous-step="currentStep = 1" />
        </div>
      </transition>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import videoUpload from './videoUpload.vue'
import videoForm from './videoForm.vue'
import Card from '@nullVideo/card/card.vue'

// 当前步骤
const currentStep = ref<number>(1)
</script>

<style scoped lang="less">
.index {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;

  * {
    height: 100%;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
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
