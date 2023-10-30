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
    <n-form
      v-if="isLogining"
      ref="loginRef"
      :model="loginForm"
      :rules="loginRules"
    >
      <div class="form">
        <div class="form-item">
          <span>账号:</span>
          <n-form-item path="account">
            <my-input
              width="20.875rem"
              height="2.5rem"
              type="text"
              placeholder="请输入用户名或邮箱"
              :value="loginForm.username"
              @input="loginForm.username = $event"
            />
          </n-form-item>
        </div>
        <div class="form-item">
          <span>密码:</span>
          <n-form-item path="password">
            <my-input
              width="20.875rem"
              height="2.5rem"
              type="password"
              placeholder="请输入密码"
              :value="loginForm.password"
              @input="loginForm.password = $event"
            />
          </n-form-item>
        </div>
      </div>
    </n-form>

    <n-form
      v-else
      ref="registerRef"
      :model="registerForm"
      :rules="registerRules"
    >
      <div class="form">
        <div class="form-item">
          <span>账号:</span>
          <n-form-item path="account">
            <my-input
              width="20.875rem"
              height="2.5rem"
              type="text"
              placeholder="请输入用户名或邮箱"
              :value="registerForm.username"
              @input="registerForm.username = $event"
            />
          </n-form-item>
        </div>
        <div class="form-item">
          <span>密码:</span>
          <n-form-item path="password">
            <my-input
              width="20.875rem"
              height="2.5rem"
              type="password"
              placeholder="请输入密码"
              :value="registerForm.password"
              @input="loginForm.password = $event"
            />
          </n-form-item>
        </div>
        <div class="form-item">
          <span>确认密码:</span>
          <n-form-item path="confirmPassword">
            <my-input
              width="20.875rem"
              height="2.5rem"
              type="password"
              placeholder="请再次输入密码"
              :value="registerForm.confirmPassword"
              @input="registerForm.confirmPassword = $event"
            />
          </n-form-item>
        </div>
      </div>
    </n-form>
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
import { ref, watch } from 'vue'
import close from '@/assets/svgs/close.svg'
import myInput from '@nullVideo/form/input/input.vue'
import myButton from '@nullVideo/button/button.vue'
import { FormItemRule, FormRules } from 'naive-ui'

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
// 表单验证规则
// 1. 登录
const loginRules: FormRules = {
  account: [
    {
      required: true,
      validator(_, value: string) {
        if (!value) {
          return new Error('请输入账号')
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码'
    }
  ]
}
// 2. 注册
const registerRules: FormRules = {
  account: [
    {
      required: true,
      validator(_, value: string) {
        if (!value) {
          return new Error('请输入账号')
        }
        return true
      },
      trigger: ['input', 'blur']
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码'
    }
  ],
  confirmPassword: [
    {
      required: true,
      message: '请再次输入密码',
      trigger: ['input', 'blur']
    },
    {
      validator: validatePasswordStartWith,
      message: '两次密码输入不一致',
      trigger: 'input'
    },
    {
      validator: validatePasswordSame,
      message: '两次密码输入不一致',
      trigger: ['blur', 'password-input']
    }
  ]
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

function validatePasswordStartWith(_: FormItemRule, value: string): boolean {
  return (
    !!registerForm.value.password &&
    registerForm.value.password.startsWith(value) &&
    registerForm.value.password.length >= value.length
  )
}
function validatePasswordSame(_: FormItemRule, value: string): boolean {
  return value === registerForm.value.password
}
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

watch(isLogining, (newVal, _) => {
  if (newVal) {
    loginForm.value = {
      username: '',
      password: ''
    }
  } else {
    registerForm.value = {
      username: '',
      password: '',
      confirmPassword: ''
    }
  }
})
</script>

<style scoped lang="less">
.loginWindow-wrapper {
  position: relative;
  width: 35.5rem;
  padding: 1rem 1rem 2rem;
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
    width: 15.875rem;
    height: 2.8125rem;
    display: flex;
    justify-content: center;
  }

  &:hover {
    box-shadow: @shadow-outer;
  }
}
</style>
