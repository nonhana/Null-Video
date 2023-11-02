package com.typeofNull.nullvideo.model.dto.video;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description
 */
@Data
public class VideoUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videDescription;

    private String videoId;

    private List<VideoTagDealRequest> videoTagDeal;

    private String videoCoverUrl;
    /**
     *  视频权限 公开/好友可见/仅自己可见
     */
    private String videoRole;
}
