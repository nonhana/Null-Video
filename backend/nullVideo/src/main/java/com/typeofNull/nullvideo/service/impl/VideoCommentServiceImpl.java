package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.VideoComment;
import com.typeofNull.nullvideo.service.VideoCommentService;
import com.typeofNull.nullvideo.mapper.VideoCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video_comment(视频评论表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{

}




