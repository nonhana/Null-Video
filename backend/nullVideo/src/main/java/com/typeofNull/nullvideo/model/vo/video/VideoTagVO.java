package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description 用于展示选择Tag页面
 */
@Data
public class VideoTagVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String videoTagId;
    
    private String videoTagName;
    
//    private Integer videoTagHot;

}
