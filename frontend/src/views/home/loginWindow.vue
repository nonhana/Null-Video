<template>
  <div class="loginWindow-wrapper">
    <div class="head">
      <div class="title">
        <div
          class="title-item"
          :class="presentStatus[0] ? 'hover' : ''"
          @mouseenter="changeStatus(0)"
          @mouseleave="refreshStatus"
          @click="isLogining = true"
        >
          <span>登录</span>
        </div>
        <div
          class="title-item"
          :class="presentStatus[1] ? 'hover' : ''"
          @mouseenter="changeStatus(1)"
          @mouseleave="refreshStatus"
          @click="isLogining = false"
        >
          <span>注册</span>
        </div>
      </div>
      <div class="close" @click="emits('close')">
        <img :src="close" alt="close" />
      </div>
    </div>
    <div v-if="isLogining" class="form">
      <div class="form-item">
        <span>账号:</span>
        <my-input
          width="20.875rem"
          height="2.5rem"
          type="text"
          placeholder="请输入用户名或邮箱"
          :value="loginForm.username"
          @input="loginForm.username = $event"
        />
      </div>
      <div class="form-item">
        <span>密码:</span>
        <my-input
          width="20.875rem"
          height="2.5rem"
          type="text"
          placeholder="请输入密码"
          :value="loginForm.password"
          @input="loginForm.password = $event"
        />
      </div>
    </div>
    <div v-else class="form">
      <div class="form-item">
        <span>账号:</span>
        <my-input
          width="20.875rem"
          height="2.5rem"
          type="text"
          placeholder="请输入用户名或邮箱"
          :value="registerForm.username"
          @input="registerForm.username = $event"
        />
      </div>
      <div class="form-item">
        <span>密码:</span>
        <my-input
          width="20.875rem"
          height="2.5rem"
          type="text"
          placeholder="请输入密码"
          :value="registerForm.password"
          @input="loginForm.password = $event"
        />
      </div>
      <div class="form-item">
        <span>确认密码:</span>
        <my-input
          width="20.875rem"
          height="2.5rem"
          type="text"
          placeholder="请再输入一遍密码"
          :value="registerForm.confirmPassword"
          @input="registerForm.confirmPassword = $event"
        />
      </div>
    </div>
    <div class="radios">
      <n-radio
        :checked="rememberUsername"
        value="account"
        @change="radioChoose"
      >
        记住账号
      </n-radio>
      <n-radio
        :checked="rememberPassword"
        value="password"
        @change="radioChoose"
      >
        记住密码
      </n-radio>
    </div>
    <div class="button">
      <my-button v-if="isLogining" round type="primary" @click="login"
        >登录</my-button
      >
      <my-button v-else type="primary" round @click="register">注册</my-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import close from '@/assets/svgs/close.svg'
import myInput from '@nullVideo/form/input/input.vue'
import myButton from '@nullVideo/button/button.vue'

const emits = defineEmits<{
  (e: 'close'): void
}>()

const presentStatus = ref<boolean[]>([true, false])
const changeStatus = (index: number) => {
  presentStatus.value = [false, false]
  presentStatus.value[index] = true
}
const refreshStatus = () => {
  if (isLogining.value) {
    presentStatus.value = [true, false]
  } else {
    presentStatus.value = [false, true]
  }
}

// 是否为登录状态
const isLogining = ref<boolean>(true)
// 是否记住账号
const rememberUsername = ref<boolean>(false)
// 是否记住密码
const rememberPassword = ref<boolean>(false)
// 登录表单
const loginForm = ref({
  username: '',
  password: ''
})
// 注册表单
const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

// 选择单选框
const radioChoose = (e: Event) => {
  const target = e.target as HTMLInputElement
  if (target.value === 'account') {
    rememberUsername.value = true
    rememberPassword.value = false
  } else {
    rememberUsername.value = false
    rememberPassword.value = true
  }
}
// 登录
const login = () => {
  console.log('loginForm', loginForm.value)
}
// 注册
const register = () => {
  console.log('registerForm', registerForm.value)
}
</script>

<style scoped lang="less">
.loginWindow-wrapper {
  position: relative;
  width: 35.5rem;
  padding: 1rem;
  border-radius: @border-radius;
  background: @bg-color;
  transition: all 0.3s;
  .head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .title {
      width: 15rem;
      padding: 0.5rem;
      display: flex;
      justify-content: space-between;
      font-size: 24px;
      font-weight: bold;
      color: @text;
      border-radius: @border-radius;
      background: #e5e5e5;
      box-shadow: @shadow-inner;
      .title-item {
        width: 6.5rem;
        height: 3rem;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        border-radius: @border-radius;
        transition: all 0.3s;
      }
      .hover {
        background: #ffffff;
        box-shadow: @shadow-outer;
      }
    }
    .close {
      width: 32px;
      height: 32px;
      cursor: pointer;
      img {
        width: 100%;
        height: 100%;
      }
    }
  }
  .form {
    position: relative;
    margin: 2rem auto 0;
    width: 26.625rem;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    &-item {
      margin-bottom: 3rem;
      width: 100%;
      height: 2.5rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 1.25rem;
      color: #3d3d3d;
    }
  }
  .radios {
    position: relative;
    margin: 0 auto;
    width: 26.625rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .button {
    margin: 2rem auto 0;
    position: relative;
    width: 100%;
    display: flex;
    justify-content: center;
  }

  &:hover {
    box-shadow: @shadow-outer;
  }
}
</style>
