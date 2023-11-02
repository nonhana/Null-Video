package com.typeofNull.nullvideo.model.dto.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description
 */
@Data
public class VideoRemoveRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String videoId;
}
