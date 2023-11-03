package com.typeofNull.nullvideo.model.dto.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/3
 * @Description
 */
@Data
public class VideoThumbCommentRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoCommentId;

    private String userId;
}
