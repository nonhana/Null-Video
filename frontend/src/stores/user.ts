import { defineStore } from 'pinia'
import { UserInfo } from '@/utils/types'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: <UserInfo>{
      user_id: 1,
      user_name: 'JohnDoe',
      user_signature: 'This is my signature.',
      user_avatar: 'https://dummyimage.com/400X400',
      user_likenum: 123,
      user_collectnum: 45,
      user_follownum: 67,
      user_fansnum: 89
    },
    token: 'pFTeR5UtKtlZGR0AcEXhQp2tjkoSXCCu',
    isLogin: false
  }),
  actions: {
    setUserInfo(userInfo: UserInfo) {
      this.userInfo = userInfo
    },
    logout() {
      this.isLogin = false
      this.token = ''
      this.userInfo = <UserInfo>{}
    }
  },
  persist: true
})
