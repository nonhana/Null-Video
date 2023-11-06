<template>
  <div class="login-background">
    <Card width="37.5rem" height="28.125rem">
      <div class="head">
        <div class="title">
          <div
            class="title-bottom"
            :class="presentStatus[0] ? '' : 'register'"
          ></div>
          <div
            class="title-item"
            :class="presentStatus[0] ? 'selected' : ''"
            @click="
              () => {
                isLogining = true
                presentStatus = [true, false]
              }
            "
          >
            <span>登录</span>
          </div>
          <div
            class="title-item"
            :class="presentStatus[1] ? 'selected' : ''"
            @click="
              () => {
                isLogining = false
                presentStatus = [false, true]
              }
            "
          >
            <span>注册</span>
          </div>
        </div>
        <div class="close" @click="userStore.hideLoginWindow()">
          <closeSVG />
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

      <div class="button">
        <Button v-if="isLogining" @click="login" width="15rem">登录</Button>
        <Button v-else @click="register" width="15rem">注册</Button>
      </div>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { registerAPI, loginAPI, getUserInfoAPI } from '@/api/user/user'
import { useUserStore } from '@/stores/user'
import closeSVG from '@nullSvg/close.svg'
import myInput from '@nullVideo/form/input/input.vue'
import Button from '@nullVideo/button/button.vue'
import { FormItemRule, FormRules, useMessage } from 'naive-ui'
import Card from '@nullVideo/card/card.vue'

const userStore = useUserStore()

const message = useMessage()

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
const login = async () => {
  const res = await loginAPI({
    userAccount: loginForm.value.username,
    userPassword: loginForm.value.password
  })
  if (res.code === 0) {
    userStore.token = res.data
    localStorage.setItem('token', res.data)
    const sourceUserInfo = (await getUserInfoAPI({})).data
    console.log(sourceUserInfo)
    userStore.setUserInfo({
      user_id: sourceUserInfo.userId,
      user_avatar: sourceUserInfo.userAvatar,
      user_signature: sourceUserInfo.userProfile ?? '',
      user_name: sourceUserInfo.userName,
      user_collectnum: sourceUserInfo.videoTotalFavourNum,
      user_fansnum: sourceUserInfo.followerNum,
      user_follownum: sourceUserInfo.followingNum,
      user_likenum: sourceUserInfo.videoTotalThumbNum
    })
    message.success('登录成功，2s后刷新页面')
    userStore.hideLoginWindow()
    setTimeout(() => {
      window.location.reload()
    }, 2000)
  }
}
// 注册
const register = async () => {
  const registerRes = await registerAPI({
    userAccount: registerForm.value.username,
    userPassword: registerForm.value.password,
    checkPassword: registerForm.value.confirmPassword
  })
  if (registerRes.code === 0) {
    const loginRes = await loginAPI({
      userAccount: registerForm.value.username,
      userPassword: registerForm.value.password
    })
    if (loginRes.code === 0) {
      userStore.token = loginRes.data
      localStorage.setItem('token', loginRes.data)
      const sourceUserInfo = (await getUserInfoAPI({})).data
      userStore.setUserInfo({
        user_id: sourceUserInfo.userId,
        user_avatar: sourceUserInfo.userAvatar,
        user_signature: sourceUserInfo.userProfile ?? '',
        user_name: sourceUserInfo.userName,
        user_collectnum: sourceUserInfo.userCollectNum ?? 0,
        user_fansnum: sourceUserInfo.followerNum,
        user_follownum: sourceUserInfo.followingNum,
        user_likenum: sourceUserInfo.videoTotalThumbsNum
      })
      message.success('注册成功')
      userStore.hideLoginWindow()
    }
  }
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
  z-index: 999;

  .head {
    display: flex;
    justify-content: space-between;

    .title {
      position: relative;
      padding: 0.375rem;
      display: flex;
      justify-content: space-between;
      border-radius: @border-radius;
      background: #e5e5e5;
      box-shadow: @shadow-inner;

      .title-bottom {
        position: absolute;
        width: calc(50% - 0.375rem);
        height: calc(100% - 0.75rem);
        background-color: #fff;
        box-shadow: @shadow-outer;
        border-radius: @border-radius;
        z-index: 999;
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
        z-index: 1000;
      }

      .title-item:hover {
        color: @text;
      }

      .selected {
        color: @text;
      }
    }

    .close {
      cursor: pointer;
      height: 2rem;

      :deep(svg path) {
        fill: @text-secondary;
        transition: all 0.3s;
      }

      &:hover :deep(svg path) {
        fill: @text;
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

      div {
        font-size: 0.75rem;
      }
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
