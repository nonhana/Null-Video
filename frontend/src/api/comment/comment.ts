
import myAxios from '../axios'
import {
    addCommentAPIParams,
    getCommentAPIParams,
    delCommentAPIParams,
    likeCommentAPIParams
} from './types'


// 添加评论
export const addCommentAPI = (data: addCommentAPIParams) => {
    return myAxios({
        url: '/api/video/add/comment',
        method: 'POST',
        data
    })
}

// 查看评论
export const getCommentAPI = (params: getCommentAPIParams) => {
    return myAxios({
        url: '/api/video/get/comment',
        method: 'GET',
        params
    })
}

// 删除评论
export const delCommentAPI = (params: delCommentAPIParams) => {
    return myAxios({
        url: '/api/video/remove/comment',
        method: 'DELETE',
        params
    })
}

// 点赞评论
export const likeCommentAPI = (data: likeCommentAPIParams) => {
    return myAxios({
        url: '/api/video/thumb/comment',
        method: 'POST',
        data
    })
}