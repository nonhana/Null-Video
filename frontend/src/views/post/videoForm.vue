<template>
  <div class="videoForm-wrapper">
    <div class="title">
      <span>Step2：基本信息填写</span>
      <my-button width="7.5rem" height="2.5rem" @click="emits('previousStep')"
        >上一步</my-button
      >
    </div>

    <div class="form">
      <n-form
        ref="formRef"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
        :model="videoForm"
        :rules="rules"
      >
        <n-form-item path="cover" label="视频封面">
          <n-upload
            list-type="image-card"
            @before-upload="fileSelected"
            @preview="fileDetected"
          >
            点击上传
          </n-upload>
          <n-modal
            v-model:show="showModal"
            preset="card"
            style="width: 600px"
            title="图片预览"
          >
            <img :src="previewImageUrl" style="width: 100%" />
          </n-modal>
          <!-- 图片裁剪组件 -->
          <imgCropper
            :source-file="coverSourceFile"
            :cropped-file-type="coverCroppedFileType"
            :dialog-visible="coverDialogVisible"
            @upload-image="uploadImage"
            @close-dialog="closeDialog"
          />
        </n-form-item>
        <n-form-item path="description" label="视频简介">
          <n-input
            v-model:value="videoForm.video_description"
            type="textarea"
            placeholder="请随便介绍一下你要上传的视频~"
            show-count
            :maxlength="100"
            @keydown.enter.prevent
          />
        </n-form-item>
        <n-form-item path="type" label="视频分类">
          <n-select
            style="width: 15rem"
            v-model:value="videoForm.video_type"
            placeholder="选择一个合理的分类~"
            :options="VIDEO_CATEGORY"
          />
        </n-form-item>
        <n-form-item path="tag" label="视频标签">
          <n-select
            style="width: 23rem"
            v-model:value="videoForm.video_tags"
            placeholder="为自己的视频添加标签，让更多人看到~"
            multiple
            :options="VIDEO_TAG"
          />
        </n-form-item>
        <n-form-item path="permission" label="视频权限">
          <n-radio
            :checked="videoForm.video_permission === 0"
            :value="0"
            @change="permissionChoose"
          >
            朋友可见
          </n-radio>
          <n-radio
            :checked="videoForm.video_permission === 1"
            :value="1"
            @change="permissionChoose"
          >
            所有人可见
          </n-radio>
        </n-form-item>
      </n-form>
    </div>

    <div class="footer">
      <my-button width="7.5rem" height="2.5rem" @click="postVideo"
        >立即投稿</my-button
      >
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { VIDEO_CATEGORY, VIDEO_TAG } from '@/utils/constants'
import myButton from '@nullVideo/button/button.vue'
import imgCropper from '@nullVideo/utils/imgCropper.vue'
import { UploadFileInfo, FormRules } from 'naive-ui'

/* 头像图片相关 */
const coverDialogVisible = ref<boolean>(false)
let coverSourceFile: File | null | undefined = null
let coverCroppedFileType: string = '' // 裁剪后的文件类型
// 选择文件后输出文件信息
const fileSelected = (file: File) => {
  coverSourceFile = file
  coverCroppedFileType = coverSourceFile?.type ?? ''
  coverDialogVisible.value = true
}
const uploadImage = (value: { imgURL: string }) => {
  // userInfo.value!.user_cover = value.imgURL
  videoForm.value.video_cover = value.imgURL
}
const closeDialog = () => {
  coverDialogVisible.value = false
}

/* Props/Emits */
const emits = defineEmits<{
  (e: 'previousStep'): void
}>()

/* Refs */
const previewImageUrl = ref<string>('')
const showModal = ref<boolean>(false)
// 视频信息的表单
const videoForm = ref({
  video_title: '',
  video_description: '',
  video_cover: '',
  video_type: [],
  video_tags: [],
  video_permission: 0
})

/* Consts */
const rules: FormRules = {
  description: [
    {
      required: true,
      validator() {
        if (videoForm.value.video_description === '') {
          return new Error('请输入视频描述')
        } else if (videoForm.value.video_description.length < 10) {
          return new Error('视频描述不能少于10个字符')
        } else if (videoForm.value.video_description.length > 100) {
          return new Error('视频描述不能超过100个字符')
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  type: [
    {
      required: true,
      validator() {
        if (videoForm.value.video_type.length === 0) {
          return new Error('请选择视频分类')
        }
        return true
      },
      trigger: ['change']
    }
  ],
  tag: [
    {
      required: true,
      validator() {
        if (videoForm.value.video_tags.length === 0) {
          return new Error('请选择视频标签')
        } else if (videoForm.value.video_tags.length > 5) {
          return new Error('视频标签不能超过5个')
        }
        return true
      },
      trigger: ['change']
    }
  ]
}

/* Functions */
// 检测到上传的封面文件
const fileDetected = (file: UploadFileInfo) => {
  const { url } = file
  console.log(url)
  previewImageUrl.value = url as string
  showModal.value = true
}
// 点击上传视频按钮上传视频
const postVideo = () => {
  console.log(videoForm.value)
}
// 选择视频权限
const permissionChoose = (e: Event) => {
  const target = e.target as HTMLInputElement
  videoForm.value.video_permission = Number(target.value)
}
</script>

<style scoped lang="less">
.videoForm-wrapper {
  position: relative;
  width: 71rem;
  min-height: 40rem;
  padding: 2rem;
  border-radius: 1rem;
  background: @bg-color;
  transition: all 0.3s;
  &:hover {
    box-shadow: @shadow-outer;
  }
  .title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    span {
      font-size: 1.5rem;
      font-weight: bold;
    }
  }
  .form {
    width: 60rem;
    margin: 2rem auto 0;
  }
  .footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 2rem;
  }
}
</style>
