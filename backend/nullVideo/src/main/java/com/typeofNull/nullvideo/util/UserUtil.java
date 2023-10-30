package com.typeofNull.nullvideo.util;

import cn.hutool.core.lang.UUID;

import static com.typeofNull.nullvideo.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Andy
 * @data 2023/10/28
 * @Description
 */
public class UserUtil {

    public static String getRedisTokenKey(String uuid){
        String token= USER_LOGIN_STATE+":"+uuid;
        return token;
    }
}
