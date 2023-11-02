package com.typeofNull.nullvideo.model.vo.search;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/1
 * @Description
 */
@Data
public class SearchVideoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String videoCoverUrl;

    private String videoUrl;

    private String videoId;

    private String createTime;

    private String authorName;

    private String videoDescription;

    private Integer videoThumbNum;
}
