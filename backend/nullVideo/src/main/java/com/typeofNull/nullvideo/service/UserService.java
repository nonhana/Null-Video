package com.typeofNull.nullvideo.service;

import cn.hutool.http.server.HttpServerRequest;
import com.typeofNull.nullvideo.model.dto.user.UserUpdateRequest;
import com.typeofNull.nullvideo.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.typeofNull.nullvideo.model.vo.user.UserLoginVO;
import com.typeofNull.nullvideo.model.vo.user.UserRegiserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 徐小帅
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-27 21:22:46
*/
public interface UserService extends IService<User> {

    UserRegiserVO userRegister(String userAccount, String userPassword, String checkPassword);

    String userLogin(String userAccount,String userPassword);

    UserLoginVO getUserLoginVo(String token);

    boolean userLogout(HttpServletRequest request);

    boolean userUpdateData(UserUpdateRequest userUpdateRequest);

    boolean userFollowing(String userIdStr,String followingIdStr);

    boolean userRemoveFollower(String userIdStr,String followerIdStr);
}
