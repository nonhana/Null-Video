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

    <div class="video-tags">
      <div v-for="tag in tags" :key="tag.id" :style="{ background: tag.color }">
        {{ tag.name }}
      </div>
    </div>

    <div class="video-operation">
      <div>
        <likeSVG />
        <div>
          {{ videoData.like_num }}
        </div>
      </div>
      <div>
        <collectionSVG />
        <div>
          {{ videoData.collection_num }}
        </div>
      </div>
      <div>
        <shareSVG />
        <div>
          {{ videoData.share_num }}
        </div>
      </div>
      <div>
        <commentSVG />
        <div>
          {{ videoData.comment_num }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getUserInfoAPI, getVideoInfoAPI } from '@/api/user/user'
import Button from '@nullVideo/button/button.vue'
import likeSVG from '@nullSvg/like.svg'
import collectionSVG from '@nullSvg/collection.svg'
import shareSVG from '@nullSvg/share.svg'
import commentSVG from '@nullSvg/comment.svg'

const route = useRoute()

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

const tags: { name: string; id: string; color: string }[] = reactive([
  {
    name: '吐槽',
    id: '123123',
    color: '#ff8200'
  },
  {
    name: '无语死了',
    id: '123123123',
    color: '#4a91ee'
  }
])

watch(
  () => route,
  async (newV, _) => {
    let currentItem: any
    if (newV.query.type === 'personal') {
      const res = await getVideoInfoAPI({
        videoId: newV.params.video_id as string
      })
      if (res.code === 0) {
        currentItem = res.data
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

  .video-tags {
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
      // box-shadow: @shadow-outer;
      color: #fff;
      font-size: 0.875rem;
      font-weight: bold;
      margin-right: 1rem;
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
  }
}
</style>
