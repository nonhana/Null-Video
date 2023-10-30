package com.typeofNull.nullvideo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.model.entity.VideoFolder;
import com.typeofNull.nullvideo.service.VideoFolderService;
import com.typeofNull.nullvideo.mapper.VideoFolderMapper;
import org.springframework.stereotype.Service;

/**
* @author 徐小帅
* @description 针对表【video_folder(视频收藏文件夹)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class VideoFolderServiceImpl extends ServiceImpl<VideoFolderMapper, VideoFolder>
    implements VideoFolderService{

}




