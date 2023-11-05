package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/3
 * @Description
 */
@Data
public class VideoCommentToVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoCommentToUserId;

    private String videoCommentToUserName;

    private String videoCommentToUserAvatar;

}
