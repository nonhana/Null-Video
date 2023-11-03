package com.typeofNull.nullvideo.service;

import cn.hutool.http.server.HttpServerRequest;
import com.typeofNull.nullvideo.model.dto.admin.AdminUpdateVideoStatusRequest;
import com.typeofNull.nullvideo.model.dto.user.UserUpdateRequest;
import com.typeofNull.nullvideo.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.typeofNull.nullvideo.model.vo.admin.AdminAuditVideoVO;
import com.typeofNull.nullvideo.model.vo.search.SearchUserVO;
import com.typeofNull.nullvideo.model.vo.user.UserFollowVO;
import com.typeofNull.nullvideo.model.vo.user.UserLoginVO;
import com.typeofNull.nullvideo.model.vo.user.UserRegiserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 徐小帅
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-27 21:22:46
*/
public interface UserService extends IService<User> {

    UserRegiserVO userRegister(String userAccount, String userPassword, String checkPassword);

    String userLogin(String userAccount,String userPassword);

    UserLoginVO getUserLoginVo(String token,String userId);

    boolean userLogout(HttpServletRequest request);

    boolean userUpdateData(UserUpdateRequest userUpdateRequest);

    boolean userFollowing(String userIdStr,String followingIdStr);

    boolean userRemoveFollower(String userIdStr,String followerIdStr);

    List<AdminAuditVideoVO> getAdminVideoVO(Long userId, Integer begin);

    boolean updateVideoStatus(Long userId,Long videoId,Integer videoStatus);

    List<SearchUserVO> searchUser(String searchText);

    List<UserFollowVO> getFollow(String userId,Integer option);


}
