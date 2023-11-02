package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.VideoTagRelation;
import com.typeofNull.nullvideo.service.VideoTagRelationService;
import com.typeofNull.nullvideo.mapper.VideoTagRelationMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video_tag_relation(视频标签对应表)】的数据库操作Service实现
* @createDate 2023-10-30 18:42:43
*/
@Service
public class VideoTagRelationServiceImpl extends ServiceImpl<VideoTagRelationMapper, VideoTagRelation>
    implements VideoTagRelationService{

}




