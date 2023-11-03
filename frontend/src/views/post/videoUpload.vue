<template>
  <div class="video-upload">
    <div class="title">
      <span>Step1：上传视频文件</span>
      <Button width="7.5rem" height="2.5rem" @click="emits('nextStep')"
        >下一步</Button
      >
    </div>
    <div class="upload">
      <n-upload directory-dnd :max="5" @change="handleChange">
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
  </div>
</template>

<script setup lang="ts">
import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'
import Button from '@nullVideo/button/button.vue'
import type { UploadFileInfo } from 'naive-ui'

const emits = defineEmits<{
  (e: 'nextStep'): void
}>()

// 检测到上传文件
const handleChange = (options: { fileList: UploadFileInfo[] }) => {
  console.log(options.fileList)
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
</style>
