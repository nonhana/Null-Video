<template>
  <div
    class="comment-list"
    v-for="comment in receivedCommentData"
    :key="comment.videoCommentId"
    :style="{
      border: comment.isChild ? '' : '1px solid #D4D4D4',
      padding: comment.isChild ? '' : '1rem',
      marginBottom: comment.isChild ? '' : '1rem'
      // boxShadow: comment.isChild ? '' : 'inset 0px 4px 10px 0px rgba(0, 0, 0, 0.3)'
    }"
  >
    <div class="flex">
      <div class="comment-content">
        <div class="comment-main">
          <img
            class="comment-header"
            :src="comment.videoCommentUserAvatar || ''"
            alt=""
          />
          <div>
            <div class="comment-name">
              {{ comment.videoCommentUserName }}
              <div class="comment-to" v-if="comment.videoCommentTo">
                <arrowCommentToSVG />
                <div>{{ comment.videoCommentTo.videoCommentToUserName }}</div>
              </div>
            </div>
            <div class="comment-text">
              {{ comment.videoCommentContent }}
            </div>

            <div class="comment-operate">
              <div class="comment-response" @click="showReply(comment)">
                回复
              </div>
              <div
                class="comment-delete"
                v-if="comment.videoCommentId"
                @click="deleteComment(comment.videoCommentId)"
              >
                删除
              </div>
            </div>
          </div>
        </div>

        <div
          class="comment-like"
          :class="{
            isThumb: comment.isThumb === 0,
            likeAnimation: comment.videoCommentId === animationId
          }"
          @click="like(comment)"
        >
          <likeSVG />
          <div>{{ comment.videoCommentThumbNum }}</div>
        </div>
      </div>
    </div>

    <div v-if="showReplyBox === comment.videoCommentId" class="comment-box">
      <commentBox
        :video-id="videoId"
        :comment-id="comment.videoCommentId"
        :comment-callback="commentCallbackFromBox"
      />
    </div>

    <div
      class="comment-children"
      v-if="
        comment.videoCommentChildren.length &&
        showCommentChildren.get(comment.videoCommentId)
      "
    >
      <comment-list
        :comment-data-child="comment.videoCommentChildren || []"
        :video-id="videoId"
        :comment-callback="commentCallback"
      />
    </div>

    <div
      class="show-comment-children"
      v-if="!comment.isChild && comment.videoCommentChildren.length"
      @click="
        showCommentChildren.set(
          comment.videoCommentId,
          !showCommentChildren.get(comment.videoCommentId)
        )
      "
    >
      <div v-if="!showCommentChildren.get(comment.videoCommentId)">
        <arrowDownSVG />
        <span>展开 {{ comment.videoCommentChildren.length }} 条回复</span>
      </div>
      <div v-if="showCommentChildren.get(comment.videoCommentId)">
        <arrowUpSVG />
        <span>收起</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, watch } from 'vue'
import commentList from './commentList.vue'
import commentBox from './commentBox.vue'
import arrowDownSVG from '@nullSvg/arrow-down.svg'
import arrowUpSVG from '@nullSvg/arrow-up.svg'
import arrowCommentToSVG from '@nullSvg/arrow-comment-to.svg'
import likeSVG from '@nullSvg/like.svg'
import { delCommentAPI, likeCommentAPI } from '@/api/comment/comment'
import { getCommentAPIResponse } from '@/api/comment/types'
import { useUserStore } from '@/stores/user'

const { commentData, commentDataChild, videoId, commentCallback } =
  defineProps<{
    commentData?: getCommentAPIResponse[]
    commentDataChild?: getCommentAPIResponse[]
    videoId: string
    commentCallback: (comment: getCommentAPIResponse) => void
  }>()

const userStore = useUserStore()

let receivedCommentData = ref<getCommentAPIResponse[]>([])
const showCommentChildren = ref<Map<string, boolean>>(new Map())
const replyTo = ref('')
const showReplyBox = ref('')
const animationId = ref('')

// 点赞评论
const like = async (comment: getCommentAPIResponse) => {
  const res = await likeCommentAPI({
    userId: userStore.userInfo.user_id as string,
    videoCommentId: comment.videoCommentId
  })

  if (res.code === 0) {
    receivedCommentData.value.forEach((item) => {
      if (item.videoCommentId === comment.videoCommentId) {
        // 点过赞,则取消点赞
        if (comment.isThumb === 0) {
          // 释放动画指向
          animationId.value = ''
          item.isThumb = 1
          item.videoCommentThumbNum--
        } else {
          animationId.value = comment.videoCommentId
          item.isThumb = 0
          item.videoCommentThumbNum++
        }
      }
    })
  }
}

// 显示回复框
const showReply = (comment: getCommentAPIResponse) => {
  if (showReplyBox.value === comment.videoCommentId) {
    showReplyBox.value = ''
    return
  }
  replyTo.value = comment.videoCommentUserName
  showReplyBox.value = comment.videoCommentId
}

// 评论回调函数
const commentCallbackFromBox = (comment: getCommentAPIResponse) => {
  showReply(comment)
  commentCallback(comment)
}

// // 删除评论
const deleteComment = (id: string) => {
  delCommentAPI({
    userId: userStore.userInfo.user_id as string,
    videoCommentId: id
  })

  for (let i = 0; i < receivedCommentData.value.length; i++) {
    if (receivedCommentData.value[i].videoCommentId === id) {
      receivedCommentData.value.splice(i, 1)
      return
    }
    if (receivedCommentData.value[i].videoCommentChildren) {
      for (
        let j = 0;
        j < receivedCommentData.value[i].videoCommentChildren.length;
        j++
      ) {
        if (
          receivedCommentData.value[i].videoCommentChildren[j]
            .videoCommentId === id
        ) {
          receivedCommentData.value[i].videoCommentChildren.splice(j, 1)
          return
        }
      }
    }
  }
}

watch(
  () => commentData,
  (newVal) => {
    console.log(commentData)
    receivedCommentData.value = newVal as getCommentAPIResponse[]
    receivedCommentData.value &&
      receivedCommentData.value.forEach((comment: getCommentAPIResponse) => {
        // 防止打开状态被重置
        if (!showCommentChildren.value.get(comment.videoCommentId)) {
          showCommentChildren.value.set(comment.videoCommentId, false)
        }
      })
  },
  { immediate: true, deep: true }
)

watch(
  () => commentDataChild,
  (newVal) => {
    receivedCommentData.value = newVal as getCommentAPIResponse[]
    receivedCommentData.value &&
      receivedCommentData.value.forEach((comment: getCommentAPIResponse) => {
        // 防止打开状态被重置
        if (!showCommentChildren.value.get(comment.videoCommentId)) {
          showCommentChildren.value.set(comment.videoCommentId, false)
        }
      })
  },
  { immediate: true, deep: true }
)
</script>
<style scoped lang="less">
.comment-list {
  position: relative;
  width: 100%;
  background-color: #fff;
  border-radius: @border-radius;

  .comment-content {
    display: flex;
    justify-content: space-between;

    .comment-main {
      display: flex;

      .comment-header {
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 100%;
      }

      > div {
        margin-left: 1rem;

        .comment-name {
          display: flex;
          align-items: center;
          font-size: 0.875rem;
          color: @text-secondary;

          .comment-to {
            display: flex;
            align-items: center;
          }
        }

        .comment-text {
          font-size: 16px;
          color: @text;
        }

        .comment-operate {
          display: flex;

          .comment-response,
          .comment-delete {
            font-size: 0.875rem;
            color: @text-secondary;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.3s;
            margin-right: 1rem;
          }

          .comment-response:hover,
          .comment-delete:hover {
            text-decoration: underline;
            color: @bg-color-primary;
          }
        }
      }
    }

    .comment-like {
      text-align: center;
      font-size: 1rem;
      line-height: 1rem;
      color: @text;
      cursor: pointer;

      svg {
        width: 1.5rem;
        height: 1.5rem;
      }
    }

    .comment-like:hover {
      color: @bg-color-primary;

      :deep(svg path) {
        fill: @bg-color-primary;
      }
    }

    .isThumb {
      color: @bg-color-primary;
      :deep(svg path) {
        fill: @bg-color-primary;
      }
    }

    .likeAnimation {
      svg {
        animation: like 0.5s forwards; 
      }
    }

    @keyframes like {
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

  .comment-children {
    margin: 0 0 0 3.5rem;
    padding: 0;
    list-style-type: none;

    > div {
      margin-top: 0.5rem;
    }
  }

  .show-comment-children > div {
    display: flex;
    align-items: center;
    margin-left: 3.5rem;
    font-size: 0.875rem;
    color: @text-secondary;
    transition: all 0.3s;
    cursor: pointer;

    span {
      margin-left: 0.5rem;
    }
  }

  .show-comment-children > div :hover {
    color: @bg-color-primary;
  }
}
</style>
