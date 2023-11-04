export interface addCommentAPIParams {
    videoId: string
    userId: string
    videoCommentId: string
    videoCommentContent: string
}

export interface getCommentAPIParams {
    videoId: string
    userId: string
}


export interface delCommentAPIParams {
    userId: string
    videoCommentId: string
}

export interface likeCommentAPIParams {
    userId: string
    videoCommentId: string
}
