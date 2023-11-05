import { defineStore } from 'pinia'
import { UserInfo } from '@/utils/types'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: <UserInfo>{},
    token: <string>''
  }),
  actions: {
    setUserInfo(userInfo: UserInfo) {
      this.userInfo = userInfo
    },
    logout() {
      this.token = ''
      this.userInfo = <UserInfo>{}
    }
  },
  persist: true
})
