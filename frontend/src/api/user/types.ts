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
  followingId: number
  userId: number
}

export interface delFanAPIParams {
  followerId: number
  userId: number
}

export interface getVideoInfoAPIParams {
  videoId: number
  userId?: number
}

export interface delVideoAPIParams {
  userId: number
  videoId: number
}

export interface likeVideoAPIParams {}

export interface collectVideoAPIParams {}
