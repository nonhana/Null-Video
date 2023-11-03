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
              <div
                v-for="(videoType, index) in videoTypes"
                :key="videoType.id"
                :class="{ selected: videoType.selected }"
                @click="typeSelect(index)"
              >
                {{ videoType.name }}
              </div>
            </div>
          </Card>
        </div>
        <div class="video-change">
          <div class="video-up" @click="videoChange(-1)">
            <img src="@/assets/svgs/video-change.svg" alt="" />
          </div>
          <div class="video-down" @click="videoChange(1)">
            <img src="@/assets/svgs/video-change.svg" alt="" />
          </div>
        </div>
      </n-gi>
      <n-gi :span="22">
        <Card :background-color="'#000'">
          <video ref="videoPlayer" class="video-js" />
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
import { onMounted, onBeforeMount, onBeforeUnmount, ref, reactive } from 'vue'
import videoInfo from './videoInfo.vue'
import { NGrid } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'
import Comment from '@nullVideo/comment/comment.vue'
// video.js相关依赖
import videojs from 'video.js'
import 'video.js/dist/video-js.css' // 引入视频样式文件
// import 'videojs-contrib-hls'
// import 'videojs-resolution-switcher/lib/videojs-resolution-switcher.css'
// import 'videojs-resolution-switcher'
import Player from 'video.js/dist/types/player'
const defaultVideoTypes = [
  {
    name: '全部',
    id: '123123',
    selected: true
  },
  {
    name: '政治',
    id: '12313423123',
    selected: true
  },
  {
    name: '娱乐',
    id: '35',
    selected: true
  },
  {
    name: '体育',
    id: '74568',
    selected: true
  },
  {
    name: '学习',
    id: '435',
    selected: true
  },
  {
    name: '艺术',
    id: '123213213213',
    selected: true
  },
  {
    name: '美食',
    id: '4545745',
    selected: true
  }
]

let videoTypes: { name: string; selected: boolean; id: string }[] = reactive(
  JSON.parse(
    localStorage.getItem('videoTypes') || JSON.stringify(defaultVideoTypes)
  )
)

// 切换 type 状态，同时存入本地数据
const typeSelect = (index: number) => {
  const flag = videoTypes[index].selected
  if (index === 0) {
    if (!flag) {
      videoTypes.forEach((item) => {
        item.selected = true
      })
    } else {
      videoTypes[0].selected = false
    }
  } else {
    if (flag) {
      videoTypes[0].selected = false
    }
    videoTypes[index].selected = !videoTypes[index].selected
  }
  localStorage.setItem('videoTypes', JSON.stringify(videoTypes))
}

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

onBeforeMount(() => {
  // 挂载前判断类型结构是否正确
  if (
    JSON.parse(localStorage.getItem('videoTypes') || '[]').length !==
    defaultVideoTypes
  ) {
    localStorage.setItem('videoTypes', JSON.stringify(defaultVideoTypes))
  }
})

onMounted(() => {
  console.log(
    JSON.parse(
      localStorage.getItem('videoTypes') || JSON.stringify(defaultVideoTypes)
    )
  )
  // 加载视频列表
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

    div {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 2rem;
      padding: 1rem;
      border-radius: @border-radius;
      color: @text-secondary;
      font-size: 0.875rem;
      font-weight: bold;
      transition: all 0.3s;
      background-color: @bg-color-secondary;
      cursor: pointer;

      &:hover {
        color: @text;
      }
    }

    .selected {
      color: #fff;
      box-shadow: @shadow-outer;
      background-color: @bg-color-primary;
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
