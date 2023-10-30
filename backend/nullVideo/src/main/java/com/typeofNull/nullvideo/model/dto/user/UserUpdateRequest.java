package com.typeofNull.nullvideo.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/28
 * @Description
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String userName;

    private String gender;

    private String userProfile;

    private String userAvatar;

}
