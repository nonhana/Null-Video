<template>
  <div class="home">
    <n-grid :cols="48" x-gap="16px" style="height: 100%">
      <n-gi :span="13">
        <div>
          <Card>
            <videoInfo />
          </Card>
          <Card style="margin-top: 2rem">
            <div class="video-types">
              <div v-for="videoType in videoTypes" :key="videoType.id" :style="{ background: videoType.color }">
                {{ videoType.name }}
              </div>
            </div>
            <div>
              <div>
                <Button @click="videoChange(-1)">上个视频</Button>
              </div>
              <div style="margin-top: 1rem">
                <Button @click="videoChange(1)">下个视频</Button>
              </div>
            </div>
          </Card>
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
  id: '123123123',
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

  .video-types {
    display: flex;
    justify-content: start;
    margin-bottom: 1rem;

    div {


      display: flex;
      align-items: center;
      justify-content: center;
      height: 2rem;
      padding: 1rem;
      border-radius: @border-radius;
      box-shadow: @shadow-outer;
      color: #fff;
      font-size: 0.875rem;
      font-weight: bold;
      margin-right: 1rem;
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
