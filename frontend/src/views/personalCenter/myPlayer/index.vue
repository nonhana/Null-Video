<template>
  <div class="index">
    <div class="title">
      <span>视频播放</span>
      <n-button @click="back">返回</n-button>
    </div>
    <div class="content">
      <video ref="videoPlayer" class="video-js" />
      <div class="info">
        <div>
          <span> <viewBlackSVG />播放： </span>
          <span>{{ videoInfo!.videoPlayNum }}</span>
        </div>
        <div>
          <span> <likeSVG />点赞： </span
          ><span>
            {{ videoInfo!.videoThumbNum }}
          </span>
        </div>
        <div>
          <span> <collectionSVG />收藏： </span
          ><span>
            {{ videoInfo!.videoFavourNum }}
          </span>
        </div>
        <div>
          <span> <shareSVG />分享： </span
          ><span>
            {{ videoInfo!.videoShareNum }}
          </span>
        </div>
        <div>
          <span> <commentSVG />评论： </span
          ><span>
            {{ videoInfo!.videoCommentNum }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getVideoInfoAPI } from '@/api/user/user'
import type { HomeVideoInfo } from '@/utils/types'
// video.js相关依赖
import videojs from 'video.js'
import 'video.js/dist/video-js.css' // 引入视频样式文件
import Player from 'video.js/dist/types/player'
import viewBlackSVG from '@nullSvg/view-black.svg'
import likeSVG from '@nullSvg/like.svg'
import collectionSVG from '@nullSvg/collection.svg'
import shareSVG from '@nullSvg/share.svg'
import commentSVG from '@nullSvg/comment.svg'

const route = useRoute()
const router = useRouter()

// 获取 video 实例
const videoPlayer = ref()
// 视频相关信息
const videoInfo = ref<HomeVideoInfo>({
  videoId: route.params.video_id as string,
  authorAvatar: '',
  authorId: '',
  authorName: '',
  createTime: '',
  isFavour: 0,
  isThumb: 0,
  videoCommentNum: 0,
  videoCoverUrl: '',
  videoDescription: '',
  videoFavourNum: 0,
  videoPlayNum: '',
  videoShareNum: 0,
  videoTags: [],
  videoThumbNum: 0,
  videoUrl: ''
})

// 定义播放器对象
let player: Player

const back = () => {
  router.push({
    name: 'myVideos',
    params: {
      user_id: route.params.user_id
    }
  })
}

watch(
  () => route.params.video_id,
  async (newV, _) => {
    if (newV !== undefined) {
      const res = await getVideoInfoAPI({
        videoId: newV as string
      })
      if (res.code === 0) {
        videoInfo.value = res.data

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
            player.src({
              src: videoInfo.value!.videoUrl
            })
          }
        )
      }
    }
  },
  { immediate: true }
)
</script>

<style scoped lang="less">
.index {
  display: flex;
  flex-direction: column;
  height: 100%;

  .title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    font-size: 1.5rem;
    font-weight: bold;
    color: @text;
  }

  .content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    flex-grow: 1;
    margin-top: 1rem;
    width: 100%;
    height: 100%;
    .info {
      border: 3px solid #999999;
      border-style: dashed;
      border-radius: 1rem;
      width: 15rem;
      display: flex;
      flex-direction: column;
      > div {
        padding: 1rem 2rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
        span {
          display: flex;
          justify-content: center;
          align-items: center;
        }
        svg {
          width: 2rem;
          height: 2rem;
          margin-right: 1rem;
        }
      }
    }
  }

  .video-js {
    height: 100%;
    width: 46rem;
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
