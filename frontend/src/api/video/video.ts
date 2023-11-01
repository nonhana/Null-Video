// src/api/video/video.ts
/* 存放视频相关接口 */

import myAxios from '../axios'
import {
  getVideoListAPIParams,
  getOthersVideoListAPIParams,
  getVideoTypeAPIParams,
  addTagAPIParams,
  getTagsAPIParams,
  postVideoAPIParams,
  updateVideoAPIParams
} from './types'

export const getMyVideoListAPI = (params: getVideoListAPIParams) => {
  return myAxios({
    url: '/api/video/get/my',
    method: 'GET',
    params
  })
}

export const getOthersVideoListAPI = (params: getOthersVideoListAPIParams) => {
  return myAxios({
    url: '/api/video/get/page',
    method: 'GET',
    params
  })
}

export const getVideoTypeAPI = (params: getVideoTypeAPIParams) => {
  return myAxios({
    url: '/api/video/get/videoType',
    method: 'GET',
    params
  })
}

export const addTagAPI = (data: addTagAPIParams) => {
  return myAxios({
    url: '/api/video/add/tags',
    method: 'POST',
    data
  })
}

export const getTagsAPI = (params: getTagsAPIParams) => {
  return myAxios({
    url: '/api/video/get/tags',
    method: 'GET',
    params
  })
}

export const postVideoAPI = (data: postVideoAPIParams) => {
  return myAxios({
    url: '/api/video/add',
    method: 'POST',
    data
  })
}

export const updateVideoAPI = (data: updateVideoAPIParams) => {
  return myAxios({
    url: '/api/video/update/info',
    method: 'POST',
    data
  })
}
