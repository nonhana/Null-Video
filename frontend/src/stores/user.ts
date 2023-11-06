import { defineStore } from 'pinia'
import { UserInfo } from '@/utils/types'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: <UserInfo>{},
    token: <string>'',
    isLoginWindowShow: false // 登录窗口显示状态
  }),
  actions: {
    setUserInfo(userInfo: UserInfo) {
      this.userInfo = userInfo
    },
    logout() {
      this.token = ''
      this.userInfo = <UserInfo>{}
      localStorage.clear()
    },
    showLoginWindow() {
      this.isLoginWindowShow = true
    },
    hideLoginWindow() {
      this.isLoginWindowShow = false
    }
  },
  persist: true
})
