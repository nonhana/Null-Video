<template>
  <n-modal title="图片切割" v-model:show="visible">
    <n-card
      style="width: 600px"
      title="头像裁剪"
      bordered
      size="huge"
      role="dialog"
      aria-modal="true"
    >
      <!-- v-if强制使其重新加载 -->
      <div v-if="visible">
        <vue-picture-cropper
          :box-style="{
            width: '100%',
            maxHeight: '400px',
            backgroundColor: '#f8f8f8',
            margin: 'auto'
          }"
          :img="sourceFileURL"
          :options="{
            viewMode: 1,
            dragMode: 'crop',
            aspectRatio: aspectRatio
          }"
        />
      </div>
      <template #footer>
        <div
          style="
            position: relative;
            width: 100%;
            display: flex;
            justify-content: flex-end;
          "
          v-if="loading"
        >
          <n-spin />
        </div>
        <span v-else class="dialog-footer">
          <n-button @click="visible = false">取 消</n-button>
          <n-button type="primary" @click="confirmCropper">确 定</n-button>
        </span>
      </template>
    </n-card>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { uploadFileAPI } from '@/api/file-action/file-action'
import VuePictureCropper, { cropper } from 'vue-picture-cropper'
import { useMessage } from 'naive-ui'

const message = useMessage()

const props = defineProps<{
  dialogVisible: boolean
  sourceFile: File | null | undefined
  croppedFileType: string
}>()

const emits = defineEmits<{
  (event: 'uploadImage', value: { imgURL: string }): void
  (event: 'closeDialog'): void
}>()

const loading = ref<boolean>(false)
const visible = ref<boolean>(false)
const aspectRatio = ref<number>(1 / 1)

let sourceFileURL: string = ''
let croppedFile: File | null | undefined = null

//获取裁剪图片的file对象
const confirmCropper = async () => {
  loading.value = true
  croppedFile = await cropper?.getFile()

  // 把Blob转换成file，type为croppedFileType
  let Blob = (await cropper?.getBlob()) as Blob
  const uploadFile = new File([Blob], croppedFile?.name ?? '', {
    type: props.croppedFileType,
    lastModified: croppedFile?.lastModified
  })

  if (croppedFile) {
    const res = await uploadFileAPI({ multipartFile: uploadFile })
    if (res.code === 0) {
      message.success('上传成功')
      emits('uploadImage', { imgURL: res.data.url })
      emits('closeDialog')
    }
  }
  loading.value = false
}

watch(
  props,
  (newV, _) => {
    if (props.sourceFile) {
      sourceFileURL = URL.createObjectURL(props.sourceFile)
    }
    aspectRatio.value = 1 / 1 // 默认是1:1
    visible.value = newV.dialogVisible
  },
  { immediate: true, deep: true }
)

watch(visible, (newV, _) => {
  if (!newV) {
    emits('closeDialog')
  }
})
</script>

<style scoped lang="less">
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 10px 16px;
  border-top: 1px solid #ebeef5;
  background-color: #fff;
  border-radius: 0 0 4px 4px;
  button {
    margin-left: 10px;
  }
}
</style>
