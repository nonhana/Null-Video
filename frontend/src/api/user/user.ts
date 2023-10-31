// src/api/user.ts
/* 存放用户相关接口 */

import myAxios from '../axios'
import {
  registerAPIParams,
  loginAPIParams,
  getUserInfoAPIParams,
  updateInfoAPIParams,
  followActionAPIParams,
  delFanAPIParams
} from './types'

// 用户注册
export const registerAPI = (data: registerAPIParams) => {
  return myAxios({
    url: '/api/user/register',
    method: 'POST',
    data
  })
}

// 用户登录
export const loginAPI = (data: loginAPIParams) => {
  return myAxios({
    url: '/api/user/login',
    method: 'POST',
    data
  })
}

// 根据token获取用户信息
export const getUserInfoAPI = (params: getUserInfoAPIParams) => {
  return myAxios({
    url: '/api/user/getUserInfo',
    method: 'GET',
    params
  })
}

// 编辑个人信息
export const updateInfoAPI = (data: updateInfoAPIParams) => {
  return myAxios({
    url: '/api/user/update',
    method: 'POST',
    data
  })
}

// 关注/取关用户
export const followActionAPI = (data: followActionAPIParams) => {
  return myAxios({
    url: '/api/user/follow',
    method: 'POST',
    data
  })
}

// 移除粉丝
export const delFanAPI = (data: delFanAPIParams) => {
  return myAxios({
    url: '/api/user/delFan',
    method: 'POST',
    data
  })
}
