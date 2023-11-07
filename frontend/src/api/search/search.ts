// src/api/search/search.ts
/* 存放搜索相关接口 */
import myAxios from '../axios'
import { getFollowFanListAPIParams, searchVideoAPIParams } from './types'

// 查看关注/粉丝列表
export const getFollowFanListAPI = (params: getFollowFanListAPIParams) => {
  return myAxios({
    url: '/api/user/get/follow',
    method: 'GET',
    params
  })
}

// 搜索
export const searchVideoAPI = (params: searchVideoAPIParams) => {
  return myAxios({
    url: '/api/search/all',
    method: 'GET',
    params
  })
}
