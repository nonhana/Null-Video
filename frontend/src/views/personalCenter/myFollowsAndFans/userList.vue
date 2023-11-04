<template>
  <div class="UserList-wrapper">
    <div style="position: relative; margin: 20px auto" v-if="loading">
      <n-spin size="large" />
    </div>
    <div v-else>
      <div v-if="type === 'follow'">
        <div v-if="followList.length !== 0" class="list">
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
          <n-empty description="暂无关注用户" />
        </div>
      </div>
      <div v-else>
        <div v-if="fanList.length !== 0" class="list">
          <div
            class="item"
            v-for="user in fanList"
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
          <n-empty description="暂无粉丝" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { UserInfo } from '@/utils/types'
import { useRoute } from 'vue-router'
import { getFollowFanListAPI } from '@/api/search/search'
import userItem from '@/views/personalCenter/myFollowsAndFans/userItem.vue'

const route = useRoute()

const props = defineProps<{
  type: 'follow' | 'fan'
}>()

// 关注列表
const followList = ref<
  {
    user_info: UserInfo
    follow_status: boolean
  }[]
>([])
// 粉丝列表
const fanList = ref<
  {
    user_info: UserInfo
    follow_status: boolean
  }[]
>([])
// 加载中
const loading = ref<boolean>(false)

onMounted(async () => {
  loading.value = true
  if (props.type === 'follow') {
    const res = await getFollowFanListAPI({
      userId: route.params.user_id as string,
      option: 0
    })
    if (res.code === 0) {
      res.data.forEach((item: any) => {
        followList.value.push({
          user_info: {
            user_id: item.followId,
            user_name: item.followName,
            user_avatar: item.followAvatar,
            user_signature: item.followProfile
          },
          follow_status: item.followStatus
        })
      })
    }
  } else {
    const res = await getFollowFanListAPI({
      userId: route.params.user_id as string,
      option: 1
    })
    if (res.code === 0) {
      res.data.forEach((item: any) => {
        fanList.value.push({
          user_info: {
            user_id: item.followId,
            user_name: item.followName,
            user_avatar: item.followAvatar,
            user_signature: item.followProfile
          },
          follow_status: item.fanStatus
        })
      })
    }
  }
  loading.value = false
})
</script>

<style scoped lang="less">
.UserList-wrapper {
  width: 100%;
  overflow-y: auto;
}
</style>
