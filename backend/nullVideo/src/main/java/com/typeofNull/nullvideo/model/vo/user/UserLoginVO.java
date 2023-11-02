package com.typeofNull.nullvideo.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/28
 * @Description
 */
@Data
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String userAccount;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private Integer followerNum;

    private Integer followingNum;

    private String gender;

    private Integer videoTotalThumbNum;

    private Integer videoNum;
    /**
     * 0-普通用户 1-管理员
     */
    private Integer userRole;

}
