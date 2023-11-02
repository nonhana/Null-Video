<template>
  <div class="login-background">
    <Card width="37.5rem" height="28.125rem">
      <div class="head">
        <div class="title">
          <div class="title-bottom" :class="presentStatus[0] ? '' : 'register'">
          </div>
          <div class="title-item" :class="presentStatus[0] ? 'selected' : ''"
            @click="isLogining = true; presentStatus = [true, false]">
            <span>登录</span>
          </div>
          <div class="title-item" :class="presentStatus[1] ? 'selected' : ''"
            @click="isLogining = false; presentStatus = [false, true]">
            <span>注册</span>
          </div>


        </div>
        <div class="close" @click="emits('close')">
          <img :src="close" alt="close" />
        </div>
      </div>

      <n-form v-if="isLogining" ref="loginRef" :model="loginForm" :rules="loginRules">
        <div class="form">
          <div class="form-item">
            <span>账号:</span>
            <n-form-item path="account">
              <my-input width="20.875rem" height="2.5rem" type="text" placeholder="请输入用户名或邮箱" :value="loginForm.username"
                @input="loginForm.username = $event" />
            </n-form-item>
          </div>
          <div class="form-item">
            <span>密码:</span>
            <n-form-item path="password">
              <my-input width="20.875rem" height="2.5rem" type="password" placeholder="请输入密码" :value="loginForm.password"
                @input="loginForm.password = $event" />
            </n-form-item>
          </div>
          <div class="radios">
            <n-radio :checked="rememberUsername" value="account" @change="radioChoose">
              记住账号
            </n-radio>
            <n-radio :checked="rememberPassword" value="password" @change="radioChoose">
              记住密码
            </n-radio>
          </div>
        </div>


      </n-form>

      <n-form v-else ref="registerRef" :model="registerForm" :rules="registerRules">
        <div class="form">
          <div class="form-item">
            <span>账号:</span>
            <n-form-item path="account">
              <my-input width="20.875rem" height="2.5rem" type="text" placeholder="请输入用户名或邮箱"
                :value="registerForm.username" @input="registerForm.username = $event" />
            </n-form-item>
          </div>
          <div class="form-item">
            <span>密码:</span>
            <n-form-item path="password">
              <my-input width="20.875rem" height="2.5rem" type="password" placeholder="请输入密码"
                :value="registerForm.password" @input="loginForm.password = $event" />
            </n-form-item>
          </div>
          <div class="form-item">
            <span>确认密码:</span>
            <n-form-item path="confirmPassword">
              <my-input width="20.875rem" height="2.5rem" type="password" placeholder="请再次输入密码"
                :value="registerForm.confirmPassword" @input="registerForm.confirmPassword = $event" />
            </n-form-item>
          </div>
        </div>
      </n-form>

      <div class="button">
        <Button v-if="isLogining" @click="login" width="15rem">登录</Button>
        <Button v-else @click="register" width="15rem">注册</Button>
      </div>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import close from '@/assets/svgs/close.svg'
import myInput from '@nullVideo/form/input/input.vue'
import Button from '@nullVideo/button/button.vue'
import { FormItemRule, FormRules } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'

const emits = defineEmits<{
  (e: 'close'): void
}>()

const presentStatus = ref<boolean[]>([true, false])

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
.login-background {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);

  .head {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {
      position: relative;
      padding: 0.5rem;
      display: flex;
      justify-content: space-between;
      border-radius: @border-radius;
      background: #e5e5e5;
      box-shadow: @shadow-inner;

      .title-bottom {
        position: absolute;
        width: calc(50% - 0.5rem);
        height: calc(100% - 1rem);
        background-color: #fff;
        box-shadow: @shadow-outer;
        border-radius: @border-radius;
        z-index: 1;
        transition: all 0.3s;
      }

      .register {
        transform: translateX(100%);
      }

      .title-item {
        padding: 0.5rem;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        font-size: 1.5rem;
        font-weight: bold;
        color: @text-secondary;
        border-radius: @border-radius;
        transition: all 0.3s;
        z-index: 2;
      }

      .title-item:hover {
        color: @text;
      }

      .selected {
        color: @text;
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
    width: 28rem;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;

    &-item {
      margin-bottom: 2rem;
      width: 100%;
      height: 2.5rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 1.25rem;
      color: @text;
    }

    .radios {
      position: relative;
      margin: 0 auto 2rem 0;
      width: 28rem;
      height: 2.5rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }



  .button {
    margin: 2rem auto 0;
    position: relative;
    width: 15.875rem;
    display: flex;
    justify-content: center;

    * {
      font-size: 1.25rem;
      font-weight: bold;
    }
  }
}
</style>
