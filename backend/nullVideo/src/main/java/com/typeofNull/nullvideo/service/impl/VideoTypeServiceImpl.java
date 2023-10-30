package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.VideoType;
import com.typeofNull.nullvideo.service.VideoTypeService;
import com.typeofNull.nullvideo.mapper.VideoTypeMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video_type(视频类目表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class VideoTypeServiceImpl extends ServiceImpl<VideoTypeMapper, VideoType>
    implements VideoTypeService{

}




