package com.typeofNull.nullvideo.model.vo.search;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/1
 * @Description 用户检索用户的时候展示
 */
@Data
public class SearchUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String authorId;

    private String authorName;

    private String authorAvatar;

    private Integer authorFollowerNum;

    private Integer authorVideoTotalThumbNum;

    private String authorProfile;

}
