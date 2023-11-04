<template>
  <div
    class="PersonalCenterVideoItem-wrapper"
    @mouseenter="hover = true"
    @mouseleave="hover = false"
  >
    <div class="viewnum">
      <viewsSVG />
      <span>{{ videoItem.video_viewnum }}</span>
    </div>
    <div :style="{ opacity: hover ? 1 : 0 }" class="template" />
    <div class="background">
      <img :src="videoItem.video_cover" alt="video_cover" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { VideoItemInfo } from '@/utils/types'
import viewsSVG from '@nullSvg/views.svg'

defineProps<{
  videoItem: VideoItemInfo
}>()

const hover = ref<boolean>(false)
</script>

<style scoped lang="less">
.PersonalCenterVideoItem-wrapper {
  position: relative;
  margin: 0 0.5rem 1rem;
  width: 14.375rem;
  height: 20.625rem;
  border-radius: 1rem;
  cursor: pointer;
  .viewnum {
    position: relative;
    top: 18rem;
    left: 0.5rem;
    z-index: 1;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    color: #ffffff;
    z-index: 3;
    img {
      width: 2rem;
      height: 2rem;
      margin-right: 0.5rem;
    }
  }
  .template {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 100%;
    border-radius: 1rem;
    overflow: hidden;
    background-color: rgba(0, 0, 0, 0.5);
    transition: all 0.3s;
    opacity: 0;
    z-index: 2;
  }
  .background {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 1rem;
    overflow: hidden;
    z-index: 1;

    > img {
      height: 100%;
    }
  }
}
</style>
