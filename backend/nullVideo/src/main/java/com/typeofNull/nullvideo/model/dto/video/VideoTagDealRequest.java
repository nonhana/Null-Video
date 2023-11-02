package com.typeofNull.nullvideo.model.dto.video;

import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description
 */
@Data
public class VideoTagDealRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoTagId;
    /**
     * 0——删除  1——增加
     */
    private Integer operation;
}
