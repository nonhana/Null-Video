package com.typeofNull.nullvideo.service;

import com.typeofNull.nullvideo.model.dto.video.VideoAddCommentRequest;
import com.typeofNull.nullvideo.model.dto.video.VideoUpdateRequest;
import com.typeofNull.nullvideo.model.dto.video.VideoUploadRequest;
import com.typeofNull.nullvideo.model.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.typeofNull.nullvideo.model.vo.search.SearchUserAndVideoVO;
import com.typeofNull.nullvideo.model.vo.search.SearchVideoVO;
import com.typeofNull.nullvideo.model.vo.video.*;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【video(视频表)】的数据库操作Service
* @createDate 2023-10-27 21:22:46
*/
public interface VideoService extends IService<Video> {


    List<VideoTagVO> getVideoTag(String searchTag);

    boolean addVideo(VideoUploadRequest videoUploadRequest);

    List<VideoShowVO> getVideoShowVO(Long authorId, String userId, Integer begin);

    List<VideoVO> getVideoShowOnMine(Long userId, Integer begin);

    VideoDetailVO getVideoDetail(Long videoId, String userId);

    boolean updateVideoInfo(VideoUpdateRequest videoUpdateRequest);

    boolean removeVideo(Long userId, Long videoId);

    List<VideoTypeVo> getVideoType();

    VideoTagVO addTag(String videoTagName);

    boolean thumbVideo(Long videoId, Long userId);

    boolean favourVideo(Long videoId, Long userId);

    List<SearchVideoVO> getTypeVideo(Long videoTypeId);

    SearchUserAndVideoVO searchAll(String searchText);

    List<SearchVideoVO> searchVideo(String searchText);

    List<VideoShowVO> getThumbOrFavourVideo(Long userId,Integer option);

    boolean addVideoComment(VideoAddCommentRequest videoAddCommentRequest);

    List<VideoCommentVO> getVideoComment(Long videoId,String userId);

    VideoCommentVO thumbVideoComment(Long videoCommentId,Long userId);

    boolean removeVideoComment(Long userId,Long videoCommentId);

    List<VideoDetailVO> getRandomVideo(String videoTypeId,String userId);
}