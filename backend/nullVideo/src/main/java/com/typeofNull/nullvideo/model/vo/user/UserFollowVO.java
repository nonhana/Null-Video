package com.typeofNull.nullvideo.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/11/3
 * @Description 用于展示关注和粉丝的列表
 */
@Data
public class UserFollowVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String followId;

    private String followName;

    private String followAvatar;

    private String followProfile;

    /**
     * 0-关注 1-没关注
     */
    private Integer followStatus;
}
