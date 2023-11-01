<template>
  <div class="home">
    <n-grid :cols="48" x-gap="32" style="height: 100%">
      <n-gi :span="13" style="position: relative">
        <div>
          <Card>
            <videoInfo />
          </Card>
          <Card style="margin-top: 2rem">
            <div class="video-choice">视频类型选择</div>
            <div class="video-types">
              <div v-for="videoType in videoTypes" :key="videoType.id" :style="{ background: videoType.color }">
                {{ videoType.name }}
              </div>
            </div>
          </Card>
        </div>
        <div class="video-change">
          <div class="video-up" @click="videoChange(-1)">
            <img src="@/assets/svgs/video-change.svg" alt="">
          </div>
          <div class="video-down" @click="videoChange(1)">
            <img src="@/assets/svgs/video-change.svg" alt="">
          </div>
        </div>

      </n-gi>
      <n-gi :span="22">
        <Card :background-color="'#000'">
          <video ref="videoPlayer" class="video-js"></video>
        </Card>
      </n-gi>
      <n-gi :span="13">
        <Card>
          <Comment />
        </Card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref, reactive } from 'vue'
import videoInfo from './videoInfo.vue'
import videojs from 'video.js'
import 'video.js/dist/video-js.css' // 引入视频样式文件
import Player from 'video.js/dist/types/player'
import { NGrid } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'
import Comment from '@nullVideo/comment/comment.vue'
import Button from '@nullVideo/button/button.vue'

const videoTypes: { name: string, color: string, id: string }[] = reactive([{
  name: '娱乐',
  id: '123123',
  color: '#ff8200'
}, {
  name: '政治',
  id: '12313423123',
  color: '#4a91ee'
},
{
  name: '政治',
  id: '35',
  color: '#4a91ee'
}, {
  name: '政治',
  id: '74568',
  color: '#4a91ee'
},
{
  name: '政治',
  id: '435',
  color: '#4a91ee'
},
{
  name: '政治',
  id: '123213213213',
  color: '#4a91ee'
},
{
  name: '政治',
  id: '4545745',
  color: '#4a91ee'
}])


const videoQueue: { current: number; queue: string[] } = reactive({
  current: -1,
  queue: []
})
// 获取 video 实例
const videoPlayer = ref()
// 定义播放器对象
let player: Player

// ways:1 播放下一个视频 ways:-1 播放上一个视频
const videoChange = (ways: number) => {
  videoQueue.current += ways
  if (videoQueue.current < 0 || videoQueue.current >= videoQueue.queue.length) {
    videoQueue.current = 0
  }
  player.src(videoQueue.queue[videoQueue.current])
}

onMounted(() => {
  videoQueue.queue.push(
    ...['./video-test001.mp4', './video-test002.mp4', './video-test003.mp4']
  )

  // 在组件挂载后初始化video.js播放器
  player = videojs(
    videoPlayer.value,
    {
      // 在这里设置视频播放器的配置选项
      autoplay: true,
      controls: true,
      playbackRates: [0.5, 1, 1.5, 2],
      controlBar: {
        //音量条竖直
        volumePanel: {
          inline: false,
          CurrentTimeDisplay: true
        }
        // // 暂停按钮隐藏
      }
    },
    () => {
      console.log('播放器已准备好')
      videoChange(1)
    }
  )
})

onBeforeUnmount(() => {
  // 在组件卸载前销毁video.js播放器
  if (player) {
    player.dispose()
  }
})
</script>

<style scoped lang="less">
.home {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: start;


  .video-choice {
    color: @text;
    font-size: 1rem;
    font-weight: bold;
  }

  .video-types {
    display: flex;
    justify-content: start;
    flex-wrap: wrap;
    margin-top: 1rem;
    gap: 1rem;

    &-item {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 2rem;
      padding: 1rem;
      border-radius: @border-radius;
      // box-shadow: @shadow-outer;
      color: #fff;
      font-size: 0.875rem;
      font-weight: bold;
    }
  }

  .video-change {
    position: absolute;
    bottom: 0;

    .video-up,
    .video-down {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 3rem;
      height: 3rem;
      background-color: @bg-color;
      box-shadow: @shadow-outer;
      border-radius: 100%;
      border: 1px solid #d4d4d4;
      font-size: 1rem;
      color: @text;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    .video-down {
      margin-top: 1rem;

      img {
        transform: rotate(180deg);
      }
    }

    .video-up:hover,
    .video-down:hover {
      background-color: @bg-color-secondary;
    }
  }

  .video-js {
    height: 100%;
    width: 100%;
    border-radius: @border-radius;
    font-size: 1rem;
    color: @text;
  }

  :deep(.video-js) {
    .vjs-control-bar {
      width: 98%;
      margin: 0.4rem auto;
      background-color: @bg-color-third;
      border-radius: @border-radius;

      .vjs-menu-content,
      .vjs-volume-vertical {
        background-color: @bg-color-third;
        border-radius: @border-radius @border-radius 0 0;
      }
    }
  }

  :deep(.video-js .vjs-big-play-button) {
    display: none;
  }
}
</style>
