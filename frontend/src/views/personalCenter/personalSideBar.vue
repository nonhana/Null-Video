<template>
  <Card height="100%">
    <div class="info">
      <div class="header">
        <div class="avatar">
          <img :src="userInfo!.user_avatar" alt="user_avatar" />
        </div>
        <div class="template" @click="openAvatarSelector">
          <span> 更换头像 </span>
        </div>
        <input
          style="display: none"
          ref="avatarSelector"
          type="file"
          @change="fileSelected"
        />
        <!-- 图片裁剪组件 -->
        <imgCropper
          :source-file="avatarSourceFile"
          :cropped-file-type="avatarCroppedFileType"
          :dialog-visible="avatarDialogVisible"
          @upload-image="uploadImage"
          @close-dialog="closeDialog"
        />
        <div class="name">
          <span v-if="!nameInputVisable" @dblclick="showNameInput">{{
            userInfo!.user_name
          }}</span>
          <div v-else>
            <my-input
              type="text"
              placeholder="你的名称"
              :value="userInfo!.user_name!"
              @input="updateName"
              @blur="showNameInput"
            />
          </div>
          <span>id:&emsp;{{ userInfo!.user_id }}</span>
        </div>
        <n-button v-if="!isMyCenter" round type="info">关注</n-button>
      </div>
      <div class="data">
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>获赞</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_collectnum }}</span>
          <span>收藏</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_follownum }}</span>
          <span>关注</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_fansnum }}</span>
          <span>粉丝</span>
        </div>
      </div>
      <div style="width: 100%" @dblclick="showSignatureInput">
        <my-input
          type="textarea"
          placeholder="请输入签名"
          :value="userInfo!.user_signature!"
          :min-rows="1"
          :max-rows="5"
          :disabled="signatureInputVisable"
          @input="updateSignature"
          @blur="showSignatureInput"
        />
      </div>
    </div>
    <n-divider />
    <div class="menu">
      <!-- <div class="menu-item" :class="hovering[0] ? 'menu-hover' : ''" @click="chooseItem(0)" @mouseenter="hoverItem(0, 0)"
        @mouseleave="hoverItem(1)">
        <img :style="{
          left: hovering[0] ? '0.5rem' : '0'
        }" :src="arrowRight" alt="arrowRight" />
        <img :src="myVideos" alt="myVideos" />
        <span>发布视频</span>
      </div> -->
      <div
        v-for="(menu, index) in menuStatus"
        :key="menu.menuName"
        class="menu-item"
        :class="{ 'menu-hover': hovering[index] }"
        @click="chooseItem(index)"
        @mouseenter="hoverItem(0, index)"
        @mouseleave="hoverItem(1)"
      >
        <arrowRightSVG
          class="menu-item-arrow"
          :style="{
            left: hovering[index] ? '0.5rem' : '0'
          }"
        />
        <component class="menu-item-img" :is="menu.menuSvg" />
        <span>{{ menu.menuName }}</span>
      </div>
    </div>
  </Card>
</template>

<script setup lang="ts">
import { ref, watch, reactive, HTMLAttributes } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { UserInfo } from '@/utils/types'
import { getUserInfoAPI, updateInfoAPI } from '@/api/user/user'
import myInput from '@/components/form/input/input.vue'
import imgCropper from '@/components/utils/imgCropper.vue'
import arrowRightSVG from '@nullSvg/arrow-right.svg'
import myVideosSVG from '@nullSvg/my-videos.svg'
import myCollectionsSVG from '@nullSvg/my-collections.svg'
import myFollowsAndFansSVG from '@nullSvg/my-follows-and-fans.svg'
import exitSVG from '@nullSvg/exit.svg'
import { useMessage, useDialog } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'

/* 头像图片相关 */
const avatarDialogVisible = ref<boolean>(false)
let avatarSourceFile: File | null | undefined = null
let avatarCroppedFileType: string = '' // 裁剪后的文件类型
// 点击更换头像时，打开文件选择器
const openAvatarSelector = () => {
  avatarSelector.value!.click()
}
// 选择文件后输出文件信息
const fileSelected = () => {
  const file = avatarSelector.value!.files![0]
  avatarSourceFile = file
  avatarCroppedFileType = avatarSourceFile?.type ?? ''
  avatarDialogVisible.value = true
}
const uploadImage = async (value: { imgURL: string }) => {
  userInfo.value!.user_avatar = 'http://' + value.imgURL
  const res = await updateInfoAPI({
    userId: userInfo.value!.user_id!,
    userAvatar: userInfo.value!.user_avatar
  })
  if (res.code === 0) {
    message.success('头像更新成功')
  }
}
const closeDialog = () => {
  avatarDialogVisible.value = false
}
/* Hooks */
const message = useMessage()
const dialog = useDialog()
const confirmExit = () => {
  dialog.warning({
    title: '警告',
    content: '确定要退出登录嘛？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      userStore.logout()
      localStorage.clear()
      message.success('退出登录成功，2s后跳转首页')
      setTimeout(() => {
        router.push('/')
      }, 2000)
    }
  })
}
/* DOMs */
const avatarSelector = ref<HTMLInputElement>()
/* Routes */
const route = useRoute()
const router = useRouter()
const routeNameWhenMounted = router.currentRoute.value.name
/* Stores */
const userStore = useUserStore()
/* Refs */
const userInfo = ref<UserInfo>()
const hovering = ref<boolean[]>([false, false, false, false])
const isMyCenter = ref<boolean>(false)
const nameInputVisable = ref<boolean>(false)
const signatureInputVisable = ref<boolean>(true)
/* Consts */
const routeNameList = ['myVideos', 'myCollections', 'myFollowsAndFans', 'exit']
const menuStatus: {
  menuName: string
  menuSelected: boolean
  menuSvg: HTMLAttributes
}[] = reactive([
  {
    menuName: '发布视频',
    menuSelected: routeNameWhenMounted === 'myVideos',
    menuSvg: myVideosSVG
  },
  {
    menuName: '收藏列表',
    menuSelected: routeNameWhenMounted === 'myCollections',
    menuSvg: myCollectionsSVG
  },
  {
    menuName: '关注/粉丝',
    menuSelected: routeNameWhenMounted === 'myFollowsAndFans',
    menuSvg: myFollowsAndFansSVG
  },
  {
    menuName: '退出登录',
    menuSelected: routeNameWhenMounted === 'exit',
    menuSvg: exitSVG
  }
])
/* Functions */
// 鼠标移动到菜单项时，箭头向右移动，其他菜单项箭头复位
const hoverItem = (type: number, index?: number) => {
  if (type === 0) {
    hovering.value = hovering.value.map((_, i) => {
      if (i === index) {
        return true
      } else {
        return false
      }
    })
  } else {
    // 根据当前路由，判断是否需要将箭头复位
    hovering.value = hovering.value.map((_, i) => {
      if (routeNameList.findIndex((item) => item === route.name) === i) {
        return true
      } else {
        return false
      }
    })
  }
}
// 点击菜单项时，跳转到对应页面/退出登录
const chooseItem = (index: number) => {
  switch (index) {
    case 0:
      router.push(`/personalCenter/${route.params.user_id}/myVideos`)
      break
    case 1:
      router.push(`/personalCenter/${route.params.user_id}/myCollections`)
      break
    case 2:
      router.push(`/personalCenter/${route.params.user_id}/myFollowsAndFans`)
      break
    case 3:
      confirmExit()
      break
  }
}
// 双击用户名/失去焦点时，显示/隐藏用户名输入框
const showNameInput = () => {
  if (nameInputVisable.value) {
    dialog.warning({
      title: '警告',
      content: '确定更改？',
      positiveText: '确定',
      negativeText: '取消',
      onPositiveClick: async () => {
        const res = await updateInfoAPI({
          userId: userInfo.value!.user_id!,
          userName: userInfo.value!.user_name
        })
        if (res.code === 0) {
          nameInputVisable.value = !nameInputVisable.value
          message.success('更新用户名成功')
        }
      }
    })
  } else {
    nameInputVisable.value = !nameInputVisable.value
  }
}
// 双击签名/失去焦点时，显示/隐藏签名输入框
const showSignatureInput = () => {
  if (!signatureInputVisable.value) {
    dialog.warning({
      title: '警告',
      content: '确定更改？',
      positiveText: '确定',
      negativeText: '取消',
      onPositiveClick: async () => {
        const res = await updateInfoAPI({
          userId: userInfo.value!.user_id!,
          userProfile: userInfo.value!.user_signature
        })
        if (res.code === 0) {
          signatureInputVisable.value = !signatureInputVisable.value
          message.success('更新签名成功')
        }
      }
    })
  } else {
    signatureInputVisable.value = !signatureInputVisable.value
  }
}
// 更新用户名
const updateName = (value: string) => {
  userInfo.value!.user_name = value
}
// 更新签名
const updateSignature = (value: string) => {
  userInfo.value!.user_signature = value
}
/* Watches */
watch(
  () => route.params.user_id,
  async (user_id) => {
    if (user_id === userStore.userInfo.user_id) {
      isMyCenter.value = true
      userInfo.value = userStore.userInfo
    } else {
      isMyCenter.value = false
      const res = await getUserInfoAPI({ userId: user_id as string })
      if (res.code === 0) {
        userInfo.value = {
          user_id: res.data.userId,
          user_avatar: res.data.userAvatar,
          user_signature: res.data.userProfile ?? '',
          user_name: res.data.userName,
          user_collectnum: res.data.userCollectNum ?? 0,
          user_fansnum: res.data.followerNum,
          user_follownum: res.data.followingNum,
          user_likenum: res.data.videoTotalThumbsNum
        }
      }
    }
  },
  { immediate: true, deep: true }
)
watch(
  () => route.name,
  (name) => {
    hovering.value[routeNameList.findIndex((item) => item === name)] = true
  },
  { immediate: true }
)
</script>

<style scoped lang="less">
.info {
  width: 100%;
  height: 14.625rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;

  .header {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .avatar {
      margin-right: 1rem;
      width: 4rem;
      height: 4rem;
      border-radius: 2rem;
      overflow: hidden;
      cursor: pointer;

      img {
        width: 4rem;
      }
    }

    .template {
      position: absolute;
      width: 4rem;
      height: 4rem;
      border-radius: 2rem;
      background: #000;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      opacity: 0;
      transition: all 0.3s;

      span {
        font-family: Source Han Sans;
        font-size: 1rem;
        color: #fff;
      }

      &:hover {
        opacity: 0.5;
      }
    }

    .name {
      margin-right: 2rem;
      display: flex;
      flex-direction: column;

      span {
        &:nth-child(1) {
          font-family: Source Han Sans;
          font-size: 24px;
          color: @text;
        }

        &:nth-child(2) {
          font-family: Source Han Sans;
          font-size: 16px;
          color: @text-secondary;
        }
      }
    }
  }

  .data {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;

    &-item {
      span {
        font-family: Source Han Sans;
        font-size: 16px;
        font-weight: bold;
        color: @text;

        &:nth-child(2) {
          margin-left: 0.25rem;
          font-weight: normal;
        }
      }
    }
  }

  .signature {
    width: 100%;
    padding: 0.75rem 1rem;
    border-radius: 1rem;
    border: 1px solid #d4d4d4;
    background: #fff;
    font-family: Source Han Sans;
    font-size: 1rem;
    color: @text;
  }
}

.menu {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;

  &-item {
    margin: 1rem 0;
    width: 20rem;
    height: 4rem;
    padding: 0 1rem;
    border-radius: 1rem;
    background: @bg-color;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    font-size: 1.2rem;
    color: @text;
    transition: all 0.3s;
    cursor: pointer;

    &-arrow {
      position: relative;
      transition: all 0.3s;
    }

    :deep(&-img) {
      margin: 0 1rem 0 2rem;
      width: 2rem;
      height: 2rem;
    }

    &:hover {
      background: @bg-color-secondary;
      box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    }
  }
}

.menu-hover {
  background: @bg-color-secondary;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
}
</style>
