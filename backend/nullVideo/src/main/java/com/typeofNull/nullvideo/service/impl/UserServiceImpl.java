package com.typeofNull.nullvideo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.exception.BusinessException;
import com.typeofNull.nullvideo.mapper.VideoMapper;
import com.typeofNull.nullvideo.model.dto.user.UserUpdateRequest;
import com.typeofNull.nullvideo.model.entity.*;
import com.typeofNull.nullvideo.model.vo.user.UserLoginVO;
import com.typeofNull.nullvideo.model.vo.user.UserRegiserVO;
import com.typeofNull.nullvideo.service.*;
import com.typeofNull.nullvideo.mapper.UserMapper;
import com.typeofNull.nullvideo.util.UserUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.typeofNull.nullvideo.constant.UserConstant.*;

/**
* @author 徐小帅
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private VideoFolderService videoFolderService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private VideoService videoService;

    @Resource
    private FollowsService followsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegiserVO userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验参数
        if(userAccount.length()<=4 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度不能少于4位");
        }
        if(userPassword.length()<=8 || checkPassword.length()<=8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度不能少于8位");
        }
        if(userAccount.length()>16 || userPassword.length()>16 || checkPassword.length()>16){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码长度不能超过16位");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        synchronized (userAccount.intern()){
            //查找账号是否存在
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getUserAccount,userAccount);
            //邮箱
            userLambdaQueryWrapper.or().eq(User::getEmail,userAccount);
            long count = this.count(userLambdaQueryWrapper);
            if(count>0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
            }
            //加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //创建用户数据
            User user = new User();
            user.setUserPassword(encryptPassword);
            user.setUserAccount(userAccount);
            String userName=USER_NAME_PREFIX+RandomUtil.randomString(6);
            user.setUserName(userName);
            boolean isSave = this.save(user);
            if(!isSave){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"注册失败");
            }
            Long userId = user.getId();
            //创建个人信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            boolean userInfoSave = userInfoService.save(userInfo);
            if(!userInfoSave){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"注册失败");
            }
            //创建收藏夹
            VideoFolder videoFolder = new VideoFolder();
            videoFolder.setVideoFolderName("默认收藏夹");
            videoFolder.setUserId(user.getId());
            videoFolder.setIsMain(0);
            videoFolder.setVideoFolderRole(0);
            boolean videoFolderSava=videoFolderService.save(videoFolder);
            if(!videoFolderSava){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"注册失败");
            }
            //注册完毕，自动登入,返回信息+token
            UserLoginVO userLoginVOInfo = getUserLoginVOInfo(user);
            UserRegiserVO userRegiserVO = new UserRegiserVO();
            BeanUtil.copyProperties(userLoginVOInfo,userRegiserVO);
            String token = UUID.randomUUID( true).toString();
            userRegiserVO.setToken(token);
            String realToken=UserUtil.getRedisTokenKey(token);
            String userJsonStr = JSONUtil.toJsonStr(user);
            stringRedisTemplate.opsForValue().set(realToken,userJsonStr,LOGIN_CODE_TTL, TimeUnit.MINUTES);
            return userRegiserVO;
        }
    }

    @Override
    public String userLogin(String userAccount, String userPassword) {
        //校验
        if (userAccount.length() <=4 || userAccount.length()>16 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8 || userPassword.length()>16 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount,userAccount)
                                .eq(User::getUserPassword,encryptPassword).or();
        userLambdaQueryWrapper.eq(User::getEmail,userAccount)
                                .eq(User::getUserPassword,encryptPassword);
        User user = this.getOne(userLambdaQueryWrapper);
        if(user==null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"密码错误或账号不存在");
        }
        //信息正确,发放token
        String uuid = UUID.randomUUID( true).toString();
        String token= USER_LOGIN_STATE+":"+uuid;
        String userJsonStr = JSONUtil.toJsonStr(user);
        stringRedisTemplate.opsForValue().set(token,userJsonStr,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return uuid;
    }

    @Override
    public UserLoginVO getUserLoginVo(String token) {
        String realToken=UserUtil.getRedisTokenKey(token);
        String userJsonStr = stringRedisTemplate.opsForValue().get(realToken);
        if(StrUtil.isBlank(userJsonStr)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"登录信息已过期");
        }
        User user = JSONUtil.toBean(userJsonStr, User.class);
        if(user.getId()==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已注销");
        }
        UserLoginVO userLoginVOInfo = getUserLoginVOInfo(user);
        return userLoginVOInfo;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        String token = request.getHeader(TOKEN);
        String realToken=UserUtil.getRedisTokenKey(token);
        String userJsonStr = stringRedisTemplate.opsForValue().get(realToken);
        if(StrUtil.isBlank(userJsonStr)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户未在登入态无法退出");
        }
        Boolean isSuccess = stringRedisTemplate.delete(realToken);
        return isSuccess;
    }

    @Override
    @Transactional
    public boolean userUpdateData(UserUpdateRequest userUpdateRequest) {
        String userIdStr = userUpdateRequest.getUserId();
        long userId = Long.parseLong(userIdStr);
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_id",userId);
        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
        //改详情
        BeanUtil.copyProperties(userUpdateRequest,userInfo,"gender");
        Integer genderNum=("男").equals(userUpdateRequest.getGender())?1:userUpdateRequest.getGender()==null?null:0;
        userInfo.setGender(genderNum);
        boolean userInfoSuccess = userInfoService.updateById(userInfo);
        if(!userInfoSuccess){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"修改失败");
        }
        //改user
        User user = this.getById(userId);
        user.setUserName(userUpdateRequest.getUserName());
        boolean userSuccess = this.updateById(user);
        return userSuccess;
    }

    @Override
    @Transactional
    public boolean userFollowing(String userIdStr, String followingIdStr) {
        long userId = Long.parseLong(userIdStr);
        long followingId = Long.parseLong(followingIdStr);
        User followingUser = this.getById(followingId);
        User user = this.getById(userId);
        //看一下双方用户是不是假数据
        if(followingUser==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"关注用户不存在");
        }
        if(user==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户不存在");
        }
        //查看之前是否关注
        LambdaQueryWrapper<Follows> followsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followsLambdaQueryWrapper.eq(Follows::getFollowerId,userId);
        followsLambdaQueryWrapper.eq(Follows::getFollowingId,followingId);
        long count = followsService.count(followsLambdaQueryWrapper);
        boolean isSuccess=false;
        if(count>0){ //之前关注了,现在要取消关注
            //先处理关注表，再将关注者粉丝减1
            followsService.remove(followsLambdaQueryWrapper);
            isSuccess = userInfoService.update().eq("user_id", followingId)
                    .setSql("follower=follower-1").update();
        }else{ //之前没关注，现在要关注了
            //新增关注表，再将关注者粉丝+1
            Follows follows = new Follows(userId, followingId);
            boolean isSave = followsService.save(follows);
            if(isSave){
                isSuccess = userInfoService.update().eq("user_id", followingId)
                        .setSql("follower=follower+1").update();
            }
        }
        return isSuccess;
    }

    @Override
    public boolean userRemoveFollower(String userIdStr, String followerIdStr) {
        long userId = Long.parseLong(userIdStr);
        long followerId = Long.parseLong(followerIdStr);
        if(judgeUserExist(userId)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户不存在");
        }
        if(judgeUserExist(followerId)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"粉丝用户不存在");
        }
        LambdaQueryWrapper<Follows> followsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followsLambdaQueryWrapper.eq(Follows::getFollowerId,followerId);
        followsLambdaQueryWrapper.eq(Follows::getFollowingId,userId);
        Follows one = followsService.getOne(followsLambdaQueryWrapper);
        if(one!=null){//存在就删
            boolean isSuccess = followsService.removeById(one);
            return isSuccess;
        }//不存在就抛异常
        throw new BusinessException(ErrorCode.OPERATION_ERROR,"不能移除未关注的用户");
    }

    /**
     * 用于获得完整的用户信息
     * @param user
     * @return
     */
    private UserLoginVO getUserLoginVOInfo(User user){
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getUserId,user.getId());
        UserInfo userInfo = userInfoService.getOne(userInfoLambdaQueryWrapper);
        //拼详细信息
        //处理个人基本信息
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(user.getId()+"");
        BeanUtil.copyProperties(user,userLoginVO);
        BeanUtil.copyProperties(userInfo,userLoginVO,"gender");
        String gender=userInfo.getGender()==null?"未知":userInfo.getGender()==0?"女":"男";
        userLoginVO.setGender(gender);
        userLoginVO.setFollowerNum(userInfo.getFollower());
        userLoginVO.setFollowingNum(userInfo.getFollowing());
        //处理视频信息
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(Video::getUserId,userInfo.getUserId());
        List<Video> videoList = videoService.list(videoLambdaQueryWrapper);
        Integer sumThumb=0;
        for(Video v:videoList){
            sumThumb+=v.getVideoThumbNum();
        }
        userLoginVO.setVideoTotalFavourNum(sumThumb);
        userLoginVO.setVideoNum(videoList.size());
        return userLoginVO;
    }

    /**
     * 判断用户是否存在
     * @param userId
     * @return
     */
    private boolean judgeUserExist(Long userId){
        User user = this.getById(userId);
        return user==null;
    }
}




