<template>
  <div class="PersonalSideBar-wrapper">
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
              :value="userInfo!.user_name"
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
          <span>{{ userInfo!.user_likenum }}</span>
          <span>收藏</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>关注</span>
        </div>
        <div class="data-item">
          <span>{{ userInfo!.user_likenum }}</span>
          <span>粉丝</span>
        </div>
      </div>
      <div style="width: 100%" @dblclick="showSignatureInput">
        <my-input
          type="textarea"
          placeholder="请输入签名"
          :value="userInfo!.user_signature"
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
      <div
        class="menu-item"
        :class="hovering[0] ? 'menu-hover' : ''"
        @click="chooseItem(0)"
        @mouseenter="hoverItem(0, 0)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[0] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myVideos" alt="myVideos" />
        <span>发布视频</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[1] ? 'menu-hover' : ''"
        @click="chooseItem(1)"
        @mouseenter="hoverItem(0, 1)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[1] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myCollections" alt="myCollections" />
        <span>收藏列表</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[2] ? 'menu-hover' : ''"
        @click="chooseItem(2)"
        @mouseenter="hoverItem(0, 2)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[2] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="myFollowsAndFans" alt="myFollowsAndFans" />
        <span>关注/粉丝</span>
      </div>
      <div
        class="menu-item"
        :class="hovering[3] ? 'menu-hover' : ''"
        @click="chooseItem(3)"
        @mouseenter="hoverItem(0, 3)"
        @mouseleave="hoverItem(1)"
      >
        <img
          :style="{
            left: hovering[3] ? '0.5rem' : '0'
          }"
          :src="arrowRight"
          alt="arrowRight"
        />
        <img :src="exit" alt="exit" />
        <span>退出登录</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { UserInfo } from '@/utils/types'
import myInput from '@/components/form/input/input.vue'
import imgCropper from '@/components/utils/imgCropper.vue'
import arrowRight from '@/assets/svgs/arrow-right.svg'
import myVideos from '@/assets/svgs/my-videos.svg'
import myCollections from '@/assets/svgs/my-collections.svg'
import myFollowsAndFans from '@/assets/svgs/my-follows-and-fans.svg'
import exit from '@/assets/svgs/exit.svg'
import { useMessage, useDialog } from 'naive-ui'

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
  console.log(file)
  avatarSourceFile = file
  avatarCroppedFileType = avatarSourceFile?.type ?? ''
  avatarDialogVisible.value = true
}
const uploadImage = (value: { imgURL: string }) => {
  userInfo.value!.user_avatar = value.imgURL
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
      userStore.isLogin = false
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
      onPositiveClick: () => {
        nameInputVisable.value = !nameInputVisable.value
        message.success('更改成功')
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
      onPositiveClick: () => {
        signatureInputVisable.value = !signatureInputVisable.value
        message.success('更改成功')
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
  (user_id) => {
    console.log(
      Number(user_id),
      userStore.userInfo.user_id,
      Number(user_id) === userStore.userInfo.user_id
    )
    if (Number(user_id) === userStore.userInfo.user_id) {
      isMyCenter.value = true
      userInfo.value = userStore.userInfo
    } else {
      isMyCenter.value = false
      userInfo.value = {
        user_id: 2,
        user_name: 'JaneDoe',
        user_signature: 'Another signature.',
        user_avatar: 'https://dummyimage.com/400X400',
        user_likenum: 111,
        user_collectnum: 222,
        user_follownum: 333,
        user_fansnum: 444
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
.PersonalSideBar-wrapper {
  position: relative;
  width: 22rem;
  height: 53rem;
  padding: 1rem;
  border-radius: 1rem;
  background: @bg-color;
  transition: all 0.3s;
  &:hover {
    box-shadow: 0rem 0.25rem 0.625rem 0rem rgba(0, 0, 0, 0.3);
  }
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
      img {
        &:nth-child(1) {
          position: relative;
          width: 1rem;
          height: 1rem;
          transition: all 0.3s;
        }
        &:nth-child(2) {
          margin: 0 2rem 0 2rem;
          width: 2rem;
          height: 2rem;
        }
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
}
</style>
