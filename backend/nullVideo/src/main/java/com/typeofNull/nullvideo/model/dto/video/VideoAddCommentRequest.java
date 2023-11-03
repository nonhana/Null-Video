package com.typeofNull.nullvideo.model.dto.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/1
 * @Description
 */
@Data
public class VideoAddCommentRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoId;

    private String userId;

    /**
     * 可以为null,如果是一级评论就不需要传
     */
    private String videoCommentId;

    private String videoCommentContent;
}


