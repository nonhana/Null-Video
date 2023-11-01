// src/api/file-action/file-action.ts
/* 存放文件操作相关接口 */

import myAxios from '../axios'
import { uploadFileAPIParams } from './types'

export const uploadFileAPI = (data: uploadFileAPIParams) => {
  return myAxios({
    url: '/api/file/upload',
    method: 'POST',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
