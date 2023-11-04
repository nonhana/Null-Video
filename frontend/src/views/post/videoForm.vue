<template>
  <div class="title">
    <span>Step2：基本信息填写</span>
    <Button width="7.5rem" height="2.5rem" @click="emits('previousStep')"
      >上一步</Button
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
        <div class="cover">
          <div
            v-if="videoForm.video_cover"
            class="cover-preview"
            @click="openCoverSelector"
          >
            <img :src="videoForm.video_cover" alt="video_cover" />
          </div>
          <div v-else class="cover-template" @click="openCoverSelector">
            <span>点击上传</span>
          </div>
          <input
            style="display: none"
            ref="coverSelector"
            type="file"
            @change="fileSelected"
          />
          <!-- 图片裁剪组件 -->
          <imgCropper
            :source-file="coverSourceFile"
            :cropped-file-type="coverCroppedFileType"
            :dialog-visible="coverDialogVisible"
            @upload-image="uploadImage"
            @close-dialog="closeDialog"
          />
        </div>
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
        <n-tree-select
          style="width: 15rem"
          v-model:value="videoForm.video_type"
          placeholder="选择一个合理的分类~"
          :options="videoTypes"
        />
      </n-form-item>
      <n-form-item path="tag" label="视频标签">
        <n-select
          style="width: 23rem"
          v-model:value="videoForm.video_tags"
          placeholder="为自己的视频添加标签，让更多人看到~"
          multiple
          :options="videoTags"
        />
        <n-input
          style="width: 14rem; margin-left: 1rem"
          type="text"
          v-model:value="newTagText"
          placeholder="输入新标签，按回车添加"
          @keydown.enter="addTag"
        />
      </n-form-item>
      <n-form-item path="permission" label="视频权限">
        <n-radio
          :checked="videoForm.video_permission === '公开'"
          value="公开"
          @change="permissionChoose"
        >
          公开
        </n-radio>
        <n-radio
          :checked="videoForm.video_permission === '好友可见'"
          value="好友可见"
          @change="permissionChoose"
        >
          好友可见
        </n-radio>
        <n-radio
          :checked="videoForm.video_permission === '仅自己可见'"
          value="仅自己可见"
          @change="permissionChoose"
        >
          仅自己可见
        </n-radio>
      </n-form-item>
    </n-form>
  </div>

  <div class="footer">
    <Button width="7.5rem" height="2.5rem" @click="postVideo">立即投稿</Button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { UploadVideoInfo } from '@/utils/types'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useUploadVideoStore } from '@/stores/uploadVideo'
import {
  getTagsAPI,
  addTagAPI,
  postVideoAPI,
  getVideoTypeAPI
} from '@/api/video/video'
import Button from '@nullVideo/button/button.vue'
import imgCropper from '@nullVideo/utils/imgCropper.vue'
import { useMessage, FormRules } from 'naive-ui'
import { TreeSelectOption } from 'naive-ui'

const router = useRouter()
const message = useMessage()
const userStore = useUserStore()
const uploadVideoStore = useUploadVideoStore()

/* 封面图片相关 */
const coverSelector = ref<HTMLInputElement>()
const coverDialogVisible = ref<boolean>(false)
let coverSourceFile: File | null | undefined = null
let coverCroppedFileType: string = '' // 裁剪后的文件类型
// 点击更换头像时，打开文件选择器
const openCoverSelector = () => {
  coverSelector.value!.click()
}
// 选择文件后输出文件信息
const fileSelected = () => {
  const file = coverSelector.value!.files![0]
  coverSourceFile = file
  coverCroppedFileType = coverSourceFile?.type ?? ''
  coverDialogVisible.value = true
}
const uploadImage = (value: { imgURL: string }) => {
  videoForm.value.video_cover = 'http://' + value.imgURL
}
const closeDialog = () => {
  coverDialogVisible.value = false
}

/* Props/Emits */
const emits = defineEmits<{
  (e: 'previousStep'): void
}>()

/* Refs */
// 视频信息的表单
const videoForm = ref<UploadVideoInfo>(uploadVideoStore.uploadVideoInfo)
// 标签列表
const videoTags = ref<
  {
    value: number
    label: string
  }[]
>([])
// 新标签的文本
const newTagText = ref('')
// 类别列表
const videoTypes = ref<TreeSelectOption[]>([])

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
// 点击上传视频按钮上传视频
const postVideo = async () => {
  console.log(videoForm.value)
  if (videoForm.value.video_url === '') {
    message.error('请先返回上一步上传视频')
    return
  } else if (
    videoForm.value.video_cover === '' ||
    videoForm.value.video_description === '' ||
    videoForm.value.video_type === '' ||
    videoForm.value.video_tags.length === 0 ||
    videoForm.value.video_permission === ''
  ) {
    message.error('请填写完整的视频信息')
    return
  }
  const formRes = {
    userId: userStore.userInfo.user_id as string,
    videoCoverUrl: videoForm.value.video_cover as string,
    videoUrl: videoForm.value.video_url as string,
    videoDescription: videoForm.value.video_description as string,
    videoTypeId: videoForm.value.video_type as string,
    videoRole: videoForm.value.video_permission as string,
    videoTagsId: videoForm.value.video_tags as string[]
  }
  const res = await postVideoAPI(formRes)
  if (res.code === 0) {
    uploadVideoStore.reset()
    message.success('投稿成功，2s后跳转到首页')
    setTimeout(() => {
      router.push('/')
    }, 2000)
  }
}
// 选择视频权限
const permissionChoose = (e: Event) => {
  const target = e.target as HTMLInputElement
  videoForm.value.video_permission = <'公开' | '好友可见' | '仅自己可见' | ''>(
    target.value
  )
}
// 添加标签
const addTag = async () => {
  if (newTagText.value === '') {
    message.error('请输入标签内容')
    return
  }
  const res = await addTagAPI({
    videoTagName: newTagText.value
  })
  if (res.code === 0) {
    message.success('添加成功')
    newTagText.value = ''
    getTags()
  }
}
// 获取视频分类
const getVideoType = async () => {
  const res = await getVideoTypeAPI({})
  if (res.code === 0) {
    videoTypes.value = res.data.map((item: any) => {
      return {
        key: item.videoTypeId,
        label: item.videoTypeName,
        children: item.videoTypeTopic.map((topic: any) => {
          return {
            key: topic.videoTypeId,
            label: topic.videoTopicName
          }
        })
      }
    })
  }
}
// 获取标签
const getTags = async () => {
  const res = await getTagsAPI({})
  if (res.code === 0) {
    videoTags.value = res.data.map((item: any) => {
      return {
        value: item.videoTagId,
        label: item.videoTagName
      }
    })
  }
}

onMounted(() => {
  getTags()
  getVideoType()
})
</script>

<style scoped lang="less">
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

.form {
  width: 60rem;
  margin: 2rem auto 0;
  .cover {
    position: relative;
    width: 15rem;
    height: 15rem;
    &-template {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      border: 1px dashed #999999;
      cursor: pointer;
      span {
        font-size: 1rem;
        color: @text;
      }
    }
    &-preview {
      width: 100%;
      height: 100%;
      cursor: pointer;
      img {
        width: 100%;
        height: 100%;
      }
    }
  }
}

.footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 2rem;
}
</style>
