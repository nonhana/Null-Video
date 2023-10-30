<template>
  <div
    class="comment-list"
    v-for="comment in props.commentList"
    :key="comment.comment_id"
    :style="{
      border: comment.isChild ? '' : '1px solid #D4D4D4',
      padding: comment.isChild ? '' : '1rem',
      marginBottom: comment.isChild ? '' : '1rem'
      // boxShadow: comment.isChild ? '' : 'inset 0px 4px 10px 0px rgba(0, 0, 0, 0.3)'
    }"
  >
    <div class="flex">
      <div class="comment-content">
        <img class="comment-header" :src="comment.user.header" alt="" />
        <div>
          <div class="comment-name">
            {{ comment.user.name }}
            <div class="comment-to" v-if="comment.comment_to">
              <img src="@/assets/svgs/arrow-comment-to.svg" alt="" />
              <div>{{ comment.comment_to.name }}</div>
            </div>
          </div>
          <div class="comment-text">
            {{ comment.comment }}
          </div>
          <div class="comment-operate">
            <div class="comment-response" @click="showReply(comment)">回复</div>
            <div
              class="comment-delete"
              v-if="comment.user.id"
              @click="deleteComment(comment.comment_id)"
            >
              删除
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showReplyBox === comment.comment_id">
      <commentBox />
    </div>

    <ul
      v-if="
        comment.children.length && showCommentChildren.get(comment.comment_id)
      "
    >
      <comment-list :commentList="comment.children" />
    </ul>

    <div
      class="show-comment-children"
      v-if="!comment.isChild && comment.children.length"
      @click="
        showCommentChildren.set(
          comment.comment_id,
          !showCommentChildren.get(comment.comment_id)
        )
      "
    >
      <div v-if="!showCommentChildren.get(comment.comment_id)">
        <img
          src="@/assets/svgs/arrow-down.svg"
          alt=""
          style="transform: scale(0.8)"
        />
        <span>展开 {{ comment.children.length }} 条回复</span>
      </div>
      <div v-if="showCommentChildren.get(comment.comment_id)">
        <img
          src="@/assets/svgs/arrow-up.svg"
          alt=""
          style="transform: scale(0.8)"
        />
        <span>收起</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, defineProps, reactive } from 'vue'
import commentList from './commentList.vue'
import commentBox from './commentBox.vue'
// import { listReviewsAPI, deleteReviewAPI } from '@/api/reviews'
// import { Review } from '@/api/reviews'
import { useNavigation } from '@/hooks/useNavigation'
const { getCurrentParams } = useNavigation()

const props = defineProps<{
  commentList: any[]
}>()

const showCommentChildren = reactive<Map<string, boolean>>(new Map())
const replyTo = ref('')
const showReplyBox = ref('')

// 显示回复框
const showReply = (comment: any) => {
  if (showReplyBox.value == comment.comment_id) {
    showReplyBox.value = ''
    return
  }
  replyTo.value = comment.user.name
  showReplyBox.value = comment.comment_id
}

// 删除评论
const deleteComment = (id: string) => {
  // console.log(id, "delete",props.commentList)
  for (let i = 0; i < props.commentList.length; i++) {
    if (props.commentList[i].comment_id === id) {
      props.commentList.splice(i, 1)
      return
    }
    if (props.commentList[i].children) {
      for (let j = 0; j < props.commentList[i].children.length; j++) {
        if (props.commentList[i].children[j].comment_id === id) {
          props.commentList[i].children.splice(j, 1)
          return
        }
      }
    }
  }
}

onMounted(async () => {
  props.commentList.forEach((comment: any) => {
    showCommentChildren.set(comment.comment_id, false)
  })

  const params = {
    type: getCurrentParams.type as string,
    id: getCurrentParams.id as string
  }
  console.log(props.commentList, 1)
  // const res = await listReviewsAPI(params)
  // commentList.value = res.data
})
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

  ul {
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
