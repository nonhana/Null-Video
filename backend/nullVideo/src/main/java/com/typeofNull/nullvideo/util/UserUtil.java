package com.typeofNull.nullvideo.util;

import cn.hutool.core.lang.UUID;
import com.typeofNull.nullvideo.model.entity.UserInfo;
import com.typeofNull.nullvideo.model.vo.search.SearchUserVO;

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


    /**
     * 转化为searchUserVO
     * @param userInfo
     * @param userName
     * @return
     */
    public static SearchUserVO setSearchUserVO(UserInfo userInfo, String userName){
        SearchUserVO searchUserVO = new SearchUserVO();
        searchUserVO.setAuthorId(userInfo.getUserId()+"");
        searchUserVO.setAuthorName(userName);
        searchUserVO.setAuthorFollowerNum(userInfo.getFollower());
        searchUserVO.setAuthorProfile(userInfo.getUserProfile());
        searchUserVO.setAuthorAvatar(userInfo.getUserAvatar());
        return searchUserVO;
    }
}
