// src/utils/types.ts
/* 封装并暴露公用类型 */

// 用户信息
export interface UserInfo {
  user_id?: string
  user_name?: string
  user_signature?: string
  user_avatar?: string
  user_likenum?: number
  user_collectnum?: number
  user_follownum?: number
  user_fansnum?: number
}

// 视频Item信息
export interface VideoItemInfo {
  video_id: number
  video_cover: string
  video_viewnum: number
}

// 上传视频时候的相关信息
export interface UploadVideoInfo {
  video_url: string
  video_title: string
  video_description: string
  video_cover: string
  video_type: string
  video_tags: string[]
  video_permission: '公开' | '好友可见' | '仅自己可见' | ''
}

// 主页视频信息
export interface HomeVideoInfo {
  createTime: string
  videoCommentNum: number
  videoCoverUrl: string
  videoDescription: string
  videoId: string
  videoPlayNum: string
  videoRole: string
  videoStatus: string
  videoThumbNum: number
  videoUrl: string
}
