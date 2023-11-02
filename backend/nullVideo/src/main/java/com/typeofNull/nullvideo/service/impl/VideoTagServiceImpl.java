package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.VideoTag;
import com.typeofNull.nullvideo.service.VideoTagService;
import com.typeofNull.nullvideo.mapper.VideoTagMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video_tag(视频标签表)】的数据库操作Service实现
* @createDate 2023-10-30 18:42:43
*/
@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag>
    implements VideoTagService{

}




