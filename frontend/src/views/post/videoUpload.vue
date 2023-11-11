<template>
  <div class="video-upload">
    <div class="title">
      <span>Step1：上传视频文件</span>
      <Button width="7.5rem" height="2.5rem" @click="nextStep">下一步</Button>
    </div>
    <div class="loading" v-if="loading">
      <n-spin />
    </div>
    <div v-if="videoUrl === ''" class="upload">
      <n-upload directory-dnd @change="handleChange">
        <n-upload-dragger>
          <div style="margin-bottom: 12px">
            <n-icon size="48" :depth="3">
              <archive-icon />
            </n-icon>
          </div>
          <n-text style="font-size: 16px">
            点击或者拖动文件到该区域来上传
          </n-text>
          <n-p depth="3" style="margin: 8px 0 0 0">
            请不要上传敏感数据，比如你的银行卡号和密码，信用卡号有效期和安全码
          </n-p>
        </n-upload-dragger>
      </n-upload>
    </div>
    <div v-if="videoUrl !== ''" class="video">
      <video :src="videoUrl" controls preload="auto" style="object-fit: fill" />
    </div>
    <div v-if="videoUrl !== ''" class="button">
      <n-button @click="selectorTrigger">重新上传</n-button>
      <n-button @click="reset">清空数据</n-button>
      <input
        ref="videoSelector"
        style="display: none"
        type="file"
        @change="fileSelected"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { uploadFileAPI } from '@/api/file-action/file-action'
import { useUploadVideoStore } from '@/stores/uploadVideo'
import Button from '@nullVideo/button/button.vue'
import type { UploadFileInfo } from 'naive-ui'
import { useMessage } from 'naive-ui'
import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'

const uploadVideoStore = useUploadVideoStore()
const message = useMessage()

const emits = defineEmits<{
  (e: 'nextStep'): void
}>()

const videoSelector = ref<HTMLInputElement>()
const videoUrl = ref<string>(uploadVideoStore.uploadVideoInfo.video_url)
const loading = ref<boolean>(false)

// 触发选择文件
const selectorTrigger = () => {
  videoSelector.value!.click()
}
// 选择文件后上传
const fileSelected = async () => {
  loading.value = true
  const file = videoSelector.value!.files![0]
  const res = await uploadFileAPI({
    multipartFile: file
  })
  if (res.code === 0) {
    videoUrl.value = res.data.url
    message.success('上传成功')
  }
  loading.value = false
}
// 检测到上传文件
const handleChange = async (options: { fileList: UploadFileInfo[] }) => {
  loading.value = true
  const res = await uploadFileAPI({
    multipartFile: options.fileList[0].file as File
  })
  if (res.code === 0) {
    videoUrl.value = res.data.url
    message.success('上传成功')
  }
  loading.value = false
}
// 下一步，把视频地址传给父组件
const nextStep = () => {
  uploadVideoStore.setVideoUrl(videoUrl.value)
  emits('nextStep')
}
// 清空信息
const reset = () => {
  videoUrl.value = ''
  uploadVideoStore.reset()
}
</script>

<style scoped lang="less">
.video-upload {
  height: 100%;
}

.title {
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    font-size: 1.5rem;
    font-weight: bold;
    color: @text;
  }
}

.upload {
  position: relative;
  width: 40rem;
  margin: 0 auto;
  top: 30%;
  // border-radius: @border-radius;
}

.video {
  position: relative;
  width: 40rem;
  margin: 0 auto;
  top: 20%;
  video {
    width: 100%;
    height: 100%;
  }
}

.button {
  position: relative;
  width: 40rem;
  margin: 0 auto;
  top: 25%;
  text-align: center;
}

.loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 999;
}
</style>
