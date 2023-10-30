<template>
  <div class="home">
    <n-grid :cols="48" x-gap="16px" style="height: 100%">
      <n-gi :span="11">
        <Card>
          <Button height="3rem" width="10rem" @click="videoChange(-1)"
            >上个视频</Button
          >
          <br />
          <br />
          <br />
          <Button height="3rem" width="10rem" @click="videoChange(1)"
            >下个视频</Button
          >
        </Card>
      </n-gi>
      <n-gi :span="21">
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
import { onMounted, onBeforeUnmount, ref, reactive } from 'vue'
import VideoInfo from './videoInfo.vue'
import videojs from 'video.js'
import 'video.js/dist/video-js.css' // 引入视频样式文件
import Player from 'video.js/dist/types/player'
import { NGrid } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'
import Comment from '@nullVideo/comment/comment.vue'
import Button from '@nullVideo/button/button.vue'

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

// controlBar: {
//         // 设置控制条组件
//         currentTimeDisplay: true,
//         timeDivider: true,
//         durationDisplay: true,
//         remainingTimeDisplay: false,
//         volumePanel: {
//           inline: false
//         },
//         children: [
//           { name: 'playToggle' }, // 播放/暂停按钮
//           { name: 'currentTimeDisplay' }, // 视频当前已播放时间
//           { name: 'progressControl' }, // 播放进度条
//           { name: 'durationDisplay' }, // 视频播放总时间
//           {
//             // 倍速播放
//             name: 'playbackRateMenuButton',
//             playbackRates: [0.5, 1, 1.5, 2, 2.5]
//           },
//           {
//             name: 'volumePanel', // 音量控制
//             inline: false // 不使用水平方式
//           },
//           { name: 'FullscreenToggle' } // 全屏
//         ]
//       }

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

  .video-js {
    height: 100%;
    width: 100%;
    border-radius: @border-radius;
    font-size: 1rem;
    color: @text;
  }

  ::v-deep .video-js {
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

  ::v-deep .video-js .vjs-big-play-button {
    display: none;
  }
}
</style>
