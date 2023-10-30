package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.Follows;
import com.typeofNull.nullvideo.service.FollowsService;
import com.typeofNull.nullvideo.mapper.FollowsMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【follows(关注关系表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, Follows>
    implements FollowsService{

}




