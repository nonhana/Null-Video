package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.Video;
import com.typeofNull.nullvideo.service.VideoService;
import com.typeofNull.nullvideo.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video(视频表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




