export interface registerAPIParams {
  checkPassword: string
  userAccount: string
  userPassword: string
}

export interface loginAPIParams {
  userAccount: string
  userPassword: string
}

export interface getUserInfoAPIParams {
  user_id?: string
}

export interface updateInfoAPIParams {
  userId: number
  gender: string
  userAvatar: null
  userName: string
  userProfile: string
}

export interface followActionAPIParams {
  /**
   * 要关注的用户Id
   */
  followingId: number
  /**
   * 使用者Id
   */
  userId: number
}

export interface delFanAPIParams {
  /**
   * 关注者Id（粉丝）
   */
  followerId: number
  /**
   * 被关注者用户Id
   */
  userId: number
}
