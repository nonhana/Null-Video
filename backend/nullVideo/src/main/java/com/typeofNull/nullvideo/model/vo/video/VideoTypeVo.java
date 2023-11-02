package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andy
 * @data 2023/10/31
 * @Description 用于视频分类展示
 */
@Data
public class VideoTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String videoTypeId;

    private String videoTypeName;

    private List<VideoTypeTopicVO> videoTypeTopic;
}
