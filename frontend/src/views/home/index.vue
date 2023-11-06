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
            <div v-if="loading">
              <n-spin />
            </div>
            <div v-else class="video-types">
              <div
                v-for="videoType in videoTypes"
                :key="videoType.id"
                :class="{ selected: videoType.selected }"
                @click="typeSelect(videoType.id)"
              >
                {{ videoType.name }}
              </div>
            </div>
          </Card>
          <Card style="margin-top: 2rem">
            <videoQueueCom
              :video-queue="videoItemQueue"
              :current="videoQueue.current"
              @video-change="emitVideo"
            />
          </Card>
        </div>
        <div class="video-change">
          <div class="video-up" @click="videoChange(-1)">
            <videoChangeSVG />
          </div>
          <div class="video-down" @click="videoChange(1)">
            <videoChangeSVG />
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
          <Comment :video-id="'4'" />
        </Card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import {
  onMounted,
  onBeforeMount,
  onBeforeUnmount,
  ref,
  reactive,
  watch
} from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { VideoItemInfo } from '@/utils/types'
import { getVideoInfoAPI } from '@/api/user/user'
import { getVideoTypeAPI, getRandomVideoAPI } from '@/api/video/video'
import { NGrid, useMessage } from 'naive-ui'
import videoInfo from './videoInfo.vue'
import videoQueueCom from './videoQueue.vue'
import Card from '@nullVideo/card/card.vue'
import Comment from '@nullVideo/comment/comment.vue'
// video.js相关依赖
import videojs from 'video.js'
import 'video.js/dist/video-js.css' // 引入视频样式文件
// import 'videojs-contrib-hls'
// import 'videojs-resolution-switcher/lib/videojs-resolution-switcher.css'
// import 'videojs-resolution-switcher'
import Player from 'video.js/dist/types/player'
import videoChangeSVG from '@nullSvg/video-change.svg'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const message = useMessage()

// 视频类型
const videoTypes = ref<{ name: string; selected: boolean; id: string }[]>([])
// 加载中
const loading = ref<boolean>(false)

// 切换 type 状态，同时存入本地数据
const typeSelect = async (type_id: string) => {
  loading.value = true
  const flag = videoTypes.value.find((item) => item.id === type_id)!.selected
  if (type_id === 'all') {
    if (!flag) {
      videoTypes.value.forEach((item) => {
        item.selected = true
      })
    } else {
      videoTypes.value.forEach((item) => {
        item.selected = false
      })
    }
    const res = await getRandomVideoAPI({
      userId: userStore.userInfo.user_id
    })
    if (res.code === 0) {
      videoQueue.queue = []
      res.data.forEach((item: any) => {
        videoQueue.queue.push(item)
      })
      // 去重
      videoQueue.queue = Array.from(new Set(videoQueue.queue))
      videoQueue.current = 0
      message.success('类型选择成功，相应视频已经加入播放队列~')
    }
  } else {
    if (flag) {
      videoTypes.value[0].selected = false
    }
    videoTypes.value.find((item) => item.id === type_id)!.selected =
      !videoTypes.value.find((item) => item.id === type_id)!.selected
    // 获取当前选中的视频类型
    const selectedTypes = videoTypes.value
      .filter((item) => item.selected)
      .map((item) => item.id)
    // 获取当前选中的视频类型的视频
    const res = await Promise.all(
      selectedTypes.map((item: any) =>
        getRandomVideoAPI({
          videoTypeId: item.id
        })
      )
    )
    videoQueue.queue = []
    res.forEach((item: any) => {
      item.data.forEach((item: any) => {
        videoQueue.queue.push(item)
      })
    })
    // 去重
    videoQueue.queue = Array.from(new Set(videoQueue.queue))
    videoQueue.current = 0
    message.success('类型选择成功，相应视频已经加入播放队列~')
  }
  localStorage.setItem('videoTypes', JSON.stringify(videoTypes.value))
  localStorage.setItem('videoList', JSON.stringify(videoQueue.queue))
  loading.value = false
}

// 视频队列
const videoQueue: { current: number; queue: any[] } = reactive({
  current: 0,
  queue: []
})

// 视频Item队列
const videoItemQueue = ref<VideoItemInfo[]>([])

// 获取 video 实例
const videoPlayer = ref()
// 定义播放器对象
let player: Player

// ways:1 播放下一个视频 ways:-1 播放上一个视频
const videoChange = (ways: number) => {
  videoQueue.current += ways
  if (videoQueue.current >= videoQueue.queue.length) {
    videoQueue.current = 0
  }
}
// 视频切换
const emitVideo = (current: number) => {
  videoQueue.current = current
}

watch(
  () => videoQueue.current,
  (newV, _) => {
    router.push({
      name: 'home',
      params: {
        video_id: videoQueue.queue[newV].videoId
      }
    })
    player.src(videoQueue.queue[newV].videoUrl)
  }
)

onBeforeMount(async () => {
  // 如果本地已经有存储视频类型，则从本地获取
  if (localStorage.getItem('videoTypes')) {
    videoTypes.value = JSON.parse(localStorage.getItem('videoTypes') as string)
  } else {
    // 如果本地没有存储视频类型，则从后端获取
    const res = await getVideoTypeAPI({})
    if (res.code === 0) {
      const result: { name: string; selected: boolean; id: string }[] =
        res.data.map((item: any) => {
          return {
            id: item.videoTypeId,
            name: item.videoTypeName,
            selected: false
          } as { name: string; selected: boolean; id: string }
        })
      result.unshift({
        id: 'all',
        name: '全部',
        selected: true
      })
      videoTypes.value = result
      localStorage.setItem('videoTypes', JSON.stringify(result))
    }
  }
})

onMounted(async () => {
  if (route.query.type === 'personal') {
    const res = await getVideoInfoAPI({
      userId: userStore.userInfo.user_id,
      videoId: route.params.video_id as string
    })
    if (res.code === 0) {
      console.log(res.data)
      videoQueue.queue = []
      videoQueue.queue.push(res.data)
      videoItemQueue.value.push(
        ...res.data.map((item: any) => {
          return {
            video_id: item.videoId,
            video_cover: item.videoCoverUrl,
            video_viewnum: item.videoPlayNum
          }
        })
      )
    }
  } else {
    // 从store中获取视频列表
    videoQueue.queue.push(
      ...JSON.parse(localStorage.getItem('videoList') as string).map(
        (item: any) => item
      )
    )
    videoItemQueue.value.push(
      ...videoQueue.queue.map((item: any) => {
        return {
          video_id: item.videoId,
          video_cover: item.videoCoverUrl,
          video_viewnum: item.videoPlayNum
        }
      })
    )
  }

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

      * {
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
