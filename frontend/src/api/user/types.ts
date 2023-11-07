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
  userId?: string
}

export interface updateInfoAPIParams {
  userId: string
  gender?: string
  userAvatar?: string
  userName?: string
  userProfile?: string
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
  videoId: string
  userId?: string
}

export interface delVideoAPIParams {
  userId: number
  videoId: number
}

export interface likeVideoAPIParams {
  userId: string
  videoId: string
}

export interface collectVideoAPIParams {
  userId: string
  videoId: string
}
export interface shareVideoAPIParams {
  userId?: string
  videoId: string
}

export interface getLikeCollectVideoListAPIParams {
  userId: string
  option: 0 | 1
}
