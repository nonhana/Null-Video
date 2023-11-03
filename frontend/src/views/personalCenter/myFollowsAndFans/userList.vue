<template>
  <div class="UserList-wrapper">
    <div v-if="type === 'follow'">
      <div
        class="item"
        v-for="user in followList"
        :key="user.user_info.user_id"
      >
        <user-item
          :user-info="user.user_info"
          :follow-status="user.follow_status"
        />
        <hr />
      </div>
    </div>
    <div v-else>
      <div class="item" v-for="user in fanList" :key="user.user_info.user_id">
        <user-item
          :user-info="user.user_info"
          :follow-status="user.follow_status"
        />
        <hr />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { UserInfo } from '@/utils/types'
import userItem from '@/views/personalCenter/myFollowsAndFans/userItem.vue'

defineProps<{
  type: 'follow' | 'fan'
}>()

// 关注列表
const followList = ref<
  {
    user_info: UserInfo
    follow_status: boolean
  }[]
>([
  {
    user_info: {
      user_id: 1,
      user_name: 'JohnDoe',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    follow_status: false
  },
  {
    user_info: {
      user_id: 2,
      user_name: 'JohnDoe',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    follow_status: false
  },
  {
    user_info: {
      user_id: 3,
      user_name: 'JohnDoe',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    follow_status: false
  }
])

// 粉丝列表
const fanList = ref<
  {
    user_info: UserInfo
    follow_status: boolean
  }[]
>([
  {
    user_info: {
      user_id: 1,
      user_name: 'JohnDo12321313123123e',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    follow_status: false
  },
  {
    user_info: {
      user_id: 2,
      user_name: 'John123123123Doe',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    follow_status: false
  },
  {
    user_info: {
      user_id: 3,
      user_name: 'JohnD123123oe',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    follow_status: false
  }
])
</script>

<style scoped lang="less">
.UserList-wrapper {
  width: 100%;
  overflow-y: auto;
}
</style>
