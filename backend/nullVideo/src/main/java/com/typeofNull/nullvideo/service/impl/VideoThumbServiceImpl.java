package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.VideoThumb;
import com.typeofNull.nullvideo.service.VideoThumbService;
import com.typeofNull.nullvideo.mapper.VideoThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video_thumb(点赞表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class VideoThumbServiceImpl extends ServiceImpl<VideoThumbMapper, VideoThumb>
    implements VideoThumbService{

}




