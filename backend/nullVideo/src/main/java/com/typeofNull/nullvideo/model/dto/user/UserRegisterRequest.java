package com.typeofNull.nullvideo.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/27
 * @Description
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
