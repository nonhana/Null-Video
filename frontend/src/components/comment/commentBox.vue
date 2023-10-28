<template>
  <div>
    <n-input type="textarea" v-model="content" placeholder="善言结善语" />
    <div>
      <n-button @click="submitComment">回复 </n-button>
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { ref } from 'vue'
// import { submitReviewAPI } from '@/api/reviews'
import { useNavigation } from "@/hooks/useNavigation"
import { NInput, NButton } from 'naive-ui';
const { getCurrentParams } = useNavigation()
const content = ref('')

// 提交评论
const submitComment = async () => {
  try {
    const params = {
      type: getCurrentParams.type as string,
      id: getCurrentParams.id as string,
      content: content.value
    }
    // const res = await submitReviewAPI(params)
    // console.log(res.data)
    console.log("提交成功")
  } catch (err) {
    console.log("error", err)
  }
}
</script>
  
<style  scoped lang="less"></style>
  