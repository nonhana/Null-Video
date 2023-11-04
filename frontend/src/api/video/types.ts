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
