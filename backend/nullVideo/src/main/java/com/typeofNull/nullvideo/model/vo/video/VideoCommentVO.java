package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andy
 * @data 2023/11/3
 * @Description
 */
@Data
public class VideoCommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoCommentId;

    private String videoCommentUserId;

    private Boolean isChild;

    private String videoCommentUserAvatar;

    private String videoCommentUserName;

    private String videoCommentCreateTime;

    private String videoCommentContent;

    private Integer videoCommentThumbNum;

    private Integer isThumb;

    private VideoCommentToVO videoCommentTo;

    private List<VideoCommentVO> videoCommentChildren;
}
