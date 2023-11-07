package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/5
 * @Description
 */
@Data
public class VideoDetailForRandomVO extends VideoDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String videoId;
}
