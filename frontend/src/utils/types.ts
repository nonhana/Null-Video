// src/utils/types.ts
/* 封装并暴露公用类型 */

// 用户信息
export interface UserInfo {
  user_id: number;
  user_name: string;
  user_signature: string;
  user_avatar: string;
  user_likenum: number;
  user_collectnum: number;
  user_follownum: number;
  user_fansnum: number;
}

// 视频Item信息
export interface VideoItemInfo {
  video_id: number;
  video_cover: string;
  video_viewnum: number;
}
