export interface addCommentAPIParams {
    videoId: string
    userId: string
    videoCommentId?: string
    videoCommentContent: string
}



export interface getCommentAPIParams {
    videoId: string
    userId: string
}

export interface getCommentAPIResponse {
[x: string]: any
    isChild: boolean
    /**
     * 是否点赞 0-点赞 1-没有
     */
    isThumb: number
    videoCommentChildren: getCommentAPIResponse[]
    videoCommentContent: string
    videoCommentCreateTime: string
    videoCommentId: string
    // 点赞数量
    videoCommentThumbNum: number
    videoCommentTo: {
        videoCommentToUserId: string
        videoCommentToUserName: string
        videoCommentToUserAvatar: string
    } | null
    videoCommentUserAvatar: null
    videoCommentUserId: string
    videoCommentUserName: string
}



export interface delCommentAPIParams {
    userId: string
    videoCommentId: string
}

export interface likeCommentAPIParams {
    userId: string
    videoCommentId: string
}
