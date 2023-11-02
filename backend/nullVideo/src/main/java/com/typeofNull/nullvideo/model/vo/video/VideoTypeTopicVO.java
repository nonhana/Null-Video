package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/31
 * @Description
 */
@Data
public class VideoTypeTopicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoTypeId;

    private String videoTopicName;

    private String videoTopicDescription;
}
