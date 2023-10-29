<template>
  <div class="home">
    <n-grid :cols="48" x-gap="16px" style="height: 100%">
      <n-gi :span="14">
        <Card>视频信息</Card>
      </n-gi>
      <n-gi :span="18">
        <Card :background-color="'#000'">
          <video ref="videoPlayer" class="video-js"></video>
        </Card>
      </n-gi>
      <n-gi :span="16">
        <Card>
          <Comment />
        </Card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue';
import videojs from 'video.js';
import 'video.js/dist/video-js.css'; // 引入视频样式文件
import Player from 'video.js/dist/types/player';

import { NGrid } from 'naive-ui';
import Card from "@nullVideo/card/card.vue"
import Comment from "@nullVideo/comment/comment.vue"

const videoPlayer = ref();
let player: Player;

onMounted(() => {
  // 在组件挂载后初始化video.js播放器
  player = videojs(videoPlayer.value, {
    // 在这里设置视频播放器的配置选项
    autoplay: true,
    controls: true,
    sources: [{
      src: './video-test001.mp4',
      type: 'video/mp4'
    }]
  }, () => {
    console.log('播放器已准备好');
  });
});

onBeforeUnmount(() => {
  // 在组件卸载前销毁video.js播放器
  if (player) {
    player.dispose();
  }
});

</script>

<style scoped lang="less">
.home {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: start;

  .video-js {
    height: 100%;
    width: 100%;
    border-radius: 1rem;
  }
}
</style>
