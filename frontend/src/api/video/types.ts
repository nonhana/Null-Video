export interface getVideoListAPIParams {
  userId: string
  begin?: number
}

export interface getOthersVideoListAPIParams {
  authorId: string
  userId: string
  begin: number
}

export interface getVideoTypeAPIParams {
  typeId?: number
}

export interface addTagAPIParams {
  videoTagName: string
}

export interface getTagsAPIParams {
  searchTag?: string
}

export interface postVideoAPIParams {
  userId: string
  videoCoverUrl: string
  videoDescription: string
  videoRole: string
  videoTagsId: string[]
  videoTypeId: string
  videoUrl: string
}

export interface updateVideoAPIParams {
  videDescription: string
  videoCoverUrl: string
  videoId: string
  videoRole: string
  videoTagDeal: {
    operation: number
    videoTagId: number
  }[]
}

export interface getRandomVideoAPIParams {
  videoTypeId?: string // 不传代表获取全部类型的视频
  userId?: string // 不传代表还没登录
}
