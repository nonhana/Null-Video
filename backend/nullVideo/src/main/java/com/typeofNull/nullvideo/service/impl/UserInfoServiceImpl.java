package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.UserInfo;
import com.typeofNull.nullvideo.service.UserInfoService;
import com.typeofNull.nullvideo.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【user_info(用户详情)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




