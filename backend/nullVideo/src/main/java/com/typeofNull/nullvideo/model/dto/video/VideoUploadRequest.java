package com.typeofNull.nullvideo.model.dto.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description
 */
@Data
public class VideoUploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String videoCoverUrl;

    private String videoUrl;

    private String videoDescription;

    private String videoTypeId;

    /**
     *  视频权限 公开/好友可见/仅自己可见
     */
    private String videoRole;

}
