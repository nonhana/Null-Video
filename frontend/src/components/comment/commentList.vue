<template>
  <div class="comment-list" v-for="comment in receivedCommentData" :key="comment.videoCommentId" :style="{
    border: comment.isChild ? '' : '1px solid #D4D4D4',
    padding: comment.isChild ? '' : '1rem',
    marginBottom: comment.isChild ? '' : '1rem'
    // boxShadow: comment.isChild ? '' : 'inset 0px 4px 10px 0px rgba(0, 0, 0, 0.3)'
  }">
    <div class="flex">
      <div class="comment-content">
        <img class="comment-header" :src="comment.videoCommentUserAvatar" alt="" />
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
            <div class="comment-response" @click="showReply(comment)">回复</div>
            <div class="comment-delete" v-if="comment.user.id" @click="deleteComment(comment.videoCommentId)">
              删除
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showReplyBox === comment.videoCommentId">
      <commentBox />
    </div>

    <ul v-if="comment.videoCommentChildren.length && showCommentChildren.get(comment.videoCommentId)
      ">
      <comment-list :commentData="comment.videoCommentChildren" />
    </ul>

    <div class="show-comment-children" v-if="!comment.isChild && comment.videoCommentChildren.length" @click="
      showCommentChildren.set(
        comment.videoCommentId,
        !showCommentChildren.get(comment.videoCommentId)
      )
      ">
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
import { delCommentAPI, likeCommentAPI } from '@/api/comment/comment'
import { getCommentAPIResponse } from '@/api/comment/types'




const { commentData } = defineProps<{
  commentData: getCommentAPIResponse[]
}>()

let receivedCommentData = ref<getCommentAPIResponse[]>([])
const showCommentChildren = ref<Map<string, boolean>>(new Map())
const replyTo = ref('')
const showReplyBox = ref('')

// 显示回复框
const showReply = (comment: getCommentAPIResponse) => {
  if (showReplyBox.value == comment.videoCommentId) {
    showReplyBox.value = ''
    return
  }
  replyTo.value = comment.videoCommentUserName
  showReplyBox.value = comment.videoCommentId
}

// // 删除评论
const deleteComment = (id: string) => {
  for (let i = 0; i < receivedCommentData.value.length; i++) {
    if (receivedCommentData.value[i].videoCommentId === id) {
      receivedCommentData.value.splice(i, 1)
      return
    }
    if (receivedCommentData.value[i].videoCommentChildren) {
      for (let j = 0; j < receivedCommentData.value[i].videoCommentChildren.length; j++) {
        if (receivedCommentData.value[i].videoCommentChildren[j].videoCommentId === id) {
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
    receivedCommentData.value = newVal
    console.log(receivedCommentData, '@')
    receivedCommentData.value.forEach((comment: getCommentAPIResponse) => {
      showCommentChildren.value.set(comment.videoCommentId, false)
    })
  }, { immediate: true, deep: true });
</script>
<style scoped lang="less">
.comment-list {
  position: relative;
  width: 100%;
  background-color: #fff;
  border-radius: @border-radius;

  .comment-content {
    display: flex;

    .comment-header {
      width: 2.5rem;
      height: 2.5rem;
      border-radius: 100%;
    }

    >div {
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

  ul {
    margin: 0 0 0 3.5rem;
    padding: 0;
    list-style-type: none;

    >div {
      margin-top: 0.5rem;
    }
  }

  .show-comment-children>div {
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

  .show-comment-children>div :hover {
    color: @bg-color-primary;
  }
}
</style>
