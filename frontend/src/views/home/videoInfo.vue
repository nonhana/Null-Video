<template>
  <div class="video-info">
    <div class="author">
      <div class="author-header">
        <img :src="userInfo.user_avatar" alt="user_avatar" />
      </div>
      <div class="author-info">
        <div class="author-name">{{ userInfo.user_name }}</div>
        <div class="author-fence">{{ userInfo.user_fans }} 位粉丝</div>
      </div>
      <Button height="2.25rem" width="4.5rem" style="font-weight: bold"
        >关注</Button
      >
    </div>

    <div class="video-intro">
      {{ userInfo.user_signature }}
    </div>

    <div class="video-operation">
      <div
        :class="{
          active: operationsActive[0],
          animation: operationsAnimation[0]
        }"
        @click="likeVideo"
      >
        <likeSVG />
        <div>
          {{ videoData.like_num }}
        </div>
      </div>
      <div
        :class="{
          active: operationsActive[1],
          animation: operationsAnimation[1]
        }"
        @click="collectVideo"
      >
        <collectionSVG />
        <div>
          {{ videoData.collection_num }}
        </div>
      </div>
      <div
        :class="{
          animation: operationsAnimation[3]
        }"
        @click=""
      >
        <shareSVG />
        <div>
          {{ videoData.share_num }}
        </div>
      </div>
      <div
        :class="{
          animation: operationsAnimation[4]
        }"
        @click=""
      >
        <commentSVG />
        <div>
          {{ videoData.comment_num }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {
  getUserInfoAPI,
  getVideoInfoAPI,
  likeVideoAPI,
  collectVideoAPI
} from '@/api/user/user'
import Button from '@nullVideo/button/button.vue'
import likeSVG from '@nullSvg/like.svg'
import collectionSVG from '@nullSvg/collection.svg'
import shareSVG from '@nullSvg/share.svg'
import commentSVG from '@nullSvg/comment.svg'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const userStore = useUserStore()

const userInfo = ref({
  user_id: '',
  user_avatar: '',
  user_name: '',
  user_signature: '',
  user_fans: 0
})
const videoData = ref({
  like_num: 0,
  collection_num: 0,
  share_num: 0,
  comment_num: 0
})

const operationsActive = ref([false, false])
const operationsAnimation = ref([false, false, false, false])

const likeVideo = async () => {
  operationsAnimation.value[0] = false
  const res = await likeVideoAPI({
    userId: userStore.userInfo.user_id as string,
    videoId: route.params.video_id as string
  })

  if (res.code === 0) {
    if (operationsActive.value[0]) {
      videoData.value.like_num--
      operationsActive.value[0] = false
    } else {
      videoData.value.like_num++
      operationsActive.value[0] = true
      operationsAnimation.value[0] = true
    }
  }
}

const collectVideo = async () => {
  operationsAnimation.value[1] = false
  const res = await collectVideoAPI({
    userId: userStore.userInfo.user_id as string,
    videoId: route.params.video_id as string
  })

  if (res.code === 0) {
    if (operationsActive.value[1]) {
      videoData.value.collection_num--
      operationsActive.value[1] = false
    } else {
      videoData.value.collection_num++
      operationsActive.value[1] = true
      operationsAnimation.value[1] = true
    }
  }
}

watch(
  route,
  async (newV, _) => {
    let currentItem: any
    if (newV.query.type === 'personal') {
      const res = await getVideoInfoAPI({
        videoId: newV.params.video_id as string
      })
      if (res.code === 0) {
        currentItem = res.data
        // 获取操作状态
        operationsActive.value = [
          res.data.isThumb === 0,
          res.data.isFavour === 0
        ]
      }
    } else {
      currentItem = JSON.parse(localStorage.getItem('videoList')!).find(
        (item: any) => item.videoId === newV.params.video_id
      )
    }
    videoData.value.like_num = currentItem.videoThumbNum
    videoData.value.collection_num = currentItem.videoFavourNum
    videoData.value.share_num = currentItem.videoShareNum
    videoData.value.comment_num = currentItem.videoCommentNum
    const user_id = currentItem.authorId
    const res = await getUserInfoAPI({
      userId: user_id
    })
    if (res.code === 0) {
      userInfo.value.user_id = user_id
      userInfo.value.user_name = res.data.userName
      userInfo.value.user_avatar = res.data.userAvatar
      userInfo.value.user_signature = res.data.userProfile
      userInfo.value.user_fans = res.data.followerNum
    }
  },
  {
    immediate: true
  }
)
</script>
<style scoped lang="less">
.video-info {
  width: 100%;

  .author {
    display: flex;

    .author-header {
      width: 3rem;
      height: 3rem;
      border-radius: 100%;
      background-color: rgb(8, 123, 255);
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
      }
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

  .video-operation {
    display: flex;
    justify-content: space-between;

    > div {
      cursor: pointer;

      :deep(svg) {
        width: 2rem;
        height: 2rem;
      }

      > div {
        margin-top: -0.75rem;
        text-align: center;
        font-size: 1rem;
        color: @text-secondary;
      }
    }

    > div:hover {
      :deep(svg path) {
        fill: @bg-color-primary;
      }
      > div {
        color: @bg-color-primary;
      }
    }

    .active {
      :deep(svg path) {
        fill: @bg-color-primary;
      }
      > div {
        color: @bg-color-primary;
      }
    }

    .animation {
      svg {
        animation: bounced 0.5s forwards;
      }
    }

    @keyframes bounced {
      0% {
        transform: scale(1);
      }
      30% {
        transform: scale(1.2);
      }
      100% {
        transform: scale(1);
      }
    }
  }
}
</style>
