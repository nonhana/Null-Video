<template>
  <div class="video-info">
    <div class="video-title">
      {{ '家人们谁懂啊！' }}
    </div>
    <div class="author">
      <div class="author-header">
        <img src="" alt="" />
      </div>
      <div class="author-info">
        <div class="author-name">{{ 'TZX' }}</div>
        <div class="author-fence">{{ 100 }} 位粉丝</div>
      </div>
      <Button height="2.25rem" width="4.5rem">关注</Button>
    </div>

    <div class="video-intro">
      {{ '谁懂啊，路边遇到🦞头👨想加我vx……' }}
    </div>
  </div>
</template>

<script setup lang="ts">
// import { onMounted, onBeforeUnmount, ref, reactive } from 'vue'
import Button from '@nullVideo/button/button.vue'
</script>
<style scoped lang="less">
.video-info {
  width: 100%;

  .video-title {
    font-size: 1.5rem;
    font-weight: bold;
    color: @text;
  }

  .author {
    margin-top: 1rem;
    display: flex;

    .author-header {
      width: 3rem;
      height: 3rem;
      border-radius: 100%;
      background-color: rgb(8, 123, 255);
    }

    .author-info {
      margin-left: 0.5rem;
      margin-right: 2rem;
      font-size: 0.875rem;

      .author-name {
        color: @text;
      }

      .author-fence {
        color: @text-secondary;
      }
    }
  }

  .video-intro {
    margin: 1rem 0;
    width: 100%;
    padding: 1rem;
    background-color: #fff;
    font-size: 1rem;
    border: 1px solid #d4d4d4;
    border-radius: @border-radius;
    color: @text;
  }
}
</style>
