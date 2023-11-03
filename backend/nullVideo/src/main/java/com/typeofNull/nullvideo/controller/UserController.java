package com.typeofNull.nullvideo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.typeofNull.nullvideo.common.BaseResponse;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.common.ResultUtils;
import com.typeofNull.nullvideo.model.dto.user.*;
import com.typeofNull.nullvideo.model.vo.user.UserFollowVO;
import com.typeofNull.nullvideo.model.vo.user.UserLoginVO;
import com.typeofNull.nullvideo.model.vo.user.UserRegiserVO;
import com.typeofNull.nullvideo.service.UserService;
import com.typeofNull.nullvideo.util.UserUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.typeofNull.nullvideo.constant.UserConstant.TOKEN;

/**
 * @author Andy
 * @data 2023/10/27
 * @Description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<UserRegiserVO> userRegiser(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StrUtil.hasBlank(userAccount,userPassword,checkPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserRegiserVO userRegiserVO = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userRegiserVO);
    }

    /**
     * 下发用户登入token
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> userLoginByAccount(@RequestBody UserLoginRequest userLoginRequest){
        if(userLoginRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"未携带token");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StrUtil.hasBlank(userAccount,userPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String token = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(token);
    }

    /**
     * 用token获取用户信息
     * @param request
     * @return
     */
    @GetMapping ("/getUserInfo")
    public BaseResponse<UserLoginVO> getUserInfoByToken(@RequestParam(required = false) String userId,HttpServletRequest request){
        String token = request.getHeader(TOKEN);
        if(StrUtil.isBlank(token)){ //如果token和userId都为空，报错
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserLoginVO userLoginVo = userService.getUserLoginVo(token,userId);
        return ResultUtils.success(userLoginVo);
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        boolean isSuccess = userService.userLogout(request);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 修改信息
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> userUpdateInfo(@RequestBody UserUpdateRequest userUpdateRequest){
        if(userUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        String userId = userUpdateRequest.getUserId();
        if(StrUtil.isBlank(userId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuceess = userService.userUpdateData(userUpdateRequest);
        return ResultUtils.success(isSuceess);
    }

    /**
     * 关注/取消 用户
     * @param userFollowRequest
     * @return
     */
    @PostMapping("/follow")
    public BaseResponse<Boolean> userFollow(@RequestBody UserFollowRequest userFollowRequest){
        if(userFollowRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userIdStr = userFollowRequest.getUserId();
        String followingIdStr = userFollowRequest.getFollowingId();
        if(StrUtil.hasBlank(userIdStr,followingIdStr)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = userService.userFollowing(userIdStr, followingIdStr);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 查看用户关注和粉丝
     * @param userId
     * @param option
     * @return
     */
    @GetMapping("/get/follow")
    public BaseResponse<List<UserFollowVO>> showFollowVO(String userId, Integer option){
        //0-关注 1-粉丝
        if(option<0&&option>2 || StrUtil.isBlank(userId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        List<UserFollowVO> userFollow = userService.getFollow(userId,option);
        return ResultUtils.success(userFollow);
    }

    /**
     * 移除粉丝
     * @param userRemoveFollowerRequest
     * @return
     */
    @DeleteMapping("/remove/follower")
    public BaseResponse<Boolean> userRemoveFollower(UserRemoveFollowerRequest userRemoveFollowerRequest){
       if(userRemoveFollowerRequest==null){
           return ResultUtils.error(ErrorCode.PARAMS_ERROR);
       }
        String followerIdStr = userRemoveFollowerRequest.getFollowerId();
        String userIdStr = userRemoveFollowerRequest.getUserId();
        if(StrUtil.hasBlank(followerIdStr,userIdStr)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = userService.userRemoveFollower(userIdStr, followerIdStr);
        return ResultUtils.success(isSuccess);
    }
}
