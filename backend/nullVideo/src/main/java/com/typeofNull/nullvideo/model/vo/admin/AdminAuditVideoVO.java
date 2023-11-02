package com.typeofNull.nullvideo.model.vo.admin;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andy
 * @data 2023/10/31
 * @Description
 */
@Data
public class AdminAuditVideoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String videoId;

    private String AuthorId;

    private String AuthorName;

    private String AuthorAvatar;

    private String videoName;

    private String videoUrl;

    private String videoCoverUrl;

    private String videoDescription;

    private String videoTypeName;

    private List<String> videoTags;

    private String createTime;
}
