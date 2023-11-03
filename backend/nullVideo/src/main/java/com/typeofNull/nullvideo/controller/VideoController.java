package com.typeofNull.nullvideo.controller;

import cn.hutool.core.util.StrUtil;
import com.typeofNull.nullvideo.common.BaseResponse;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.common.ResultUtils;
import com.typeofNull.nullvideo.model.dto.video.*;
import com.typeofNull.nullvideo.model.vo.video.*;
import com.typeofNull.nullvideo.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    /**
     * 发表视频
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addVideo(@RequestBody VideoUploadRequest videoUploadRequest){
        if(videoUploadRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoUrl = videoUploadRequest.getVideoUrl();
        String videoCoverUrl = videoUploadRequest.getVideoCoverUrl();
        String userIdStr = videoUploadRequest.getUserId();
        if(StrUtil.hasBlank(videoUrl,videoCoverUrl,userIdStr)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.addVideo(videoUploadRequest);
        return ResultUtils.success(isSuccess);
    }


    /**
     * 获取主页视图下的视频VO
     * @param userId
     * @return
     */
    @GetMapping("/get/page")
    public BaseResponse<List<VideoShowVO>> getVideoShowOnPage(String authorId,@RequestParam(required = false) String userId,@RequestParam(defaultValue = "0") Integer begin){
        if(StrUtil.isBlank(authorId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        if(begin==null||begin<0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        List<VideoShowVO> videoShowVO = videoService.getVideoShowVO(Long.parseLong(authorId),userId,begin);
        return ResultUtils.success(videoShowVO);
    }

    /**
     * 获取点赞或收藏的视频 0-喜欢 1-收藏
     * @param userId
     * @param option
     * @return
     */
    @GetMapping("/get/my/thumbOrFavour")
    public BaseResponse<List<VideoShowVO>> getFavourOrThumbVideo(String userId,Integer option){
        if(option==null||option<0||option>1){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if(StrUtil.isBlank(userId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        List<VideoShowVO> thumbOrFavourVideo = videoService.getThumbOrFavourVideo(Long.parseLong(userId), option);
        return ResultUtils.success(thumbOrFavourVideo);
    }
    /**
     * 获取内容管理主页下的视频VO
     * @param userId
     * @param begin
     * @return
     */
    @GetMapping("/get/my")
    public BaseResponse<List<VideoVO>> getVideoShowOnMine(String userId,@RequestParam(defaultValue = "0") Integer begin){
        if(StrUtil.isBlank(userId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        if(begin==null||begin<0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        List<VideoVO> videoShowOnMine = videoService.getVideoShowOnMine(Long.parseLong(userId), begin);
        return ResultUtils.success(videoShowOnMine);
    }

    /**
     * 修改视频信息
     * @param videoUpdateRequest
     * @return
     */
    @PostMapping("/update/info")
    public BaseResponse<Boolean> updateVideoInfo(@RequestBody VideoUpdateRequest videoUpdateRequest){
        if(videoUpdateRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoIdStr = videoUpdateRequest.getVideoId();
        if(StrUtil.isBlank(videoIdStr)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.updateVideoInfo(videoUpdateRequest);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 点击进入某个具体视频
     * @param videoId
     * @param userId
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<VideoDetailVO> getVideoContent(String videoId,@RequestParam(required = false) String userId){
        if(StrUtil.isBlank(videoId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long videoIdReal = Long.parseLong(videoId);

        VideoDetailVO videoDetail = videoService.getVideoDetail(videoIdReal,userId);
        return ResultUtils.success(videoDetail);
    }

    /**
     * 删除视频
     * @param videoRemoveRequest
     * @return
     */
    @DeleteMapping("/remove")
    public BaseResponse<Boolean> removevVideo(VideoRemoveRequest videoRemoveRequest){
        if(videoRemoveRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userIdStr = videoRemoveRequest.getUserId();
        String videoIdStr = videoRemoveRequest.getVideoId();
        if(StrUtil.hasBlank(userIdStr,videoIdStr)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.removeVideo(Long.parseLong(userIdStr), Long.parseLong(videoIdStr));
        return ResultUtils.success(isSuccess);
    }


    /**
     * 获取视频Tag
     * @param searchTag
     * @return
     */
    @GetMapping("/get/tags")
    public BaseResponse<List<VideoTagVO>> getVideoTag(String searchTag){
        List<VideoTagVO> videoTag = videoService.getVideoTag(searchTag);
        return ResultUtils.success(videoTag);
    }


    /**
     * 创建视频Tag
     * @param videoAddTagRequest
     * @return
     */
    @PostMapping("/add/tags")
    public BaseResponse<VideoTagVO> addVideoTag(@RequestBody VideoAddTagRequest videoAddTagRequest){
        if (videoAddTagRequest==null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoTagName = videoAddTagRequest.getVideoTagName();
        if(StrUtil.isBlank(videoTagName)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"创建内容不可为空");
        }
        VideoTagVO videoTagVO = videoService.addTag(videoTagName);
        return ResultUtils.success(videoTagVO);
    }

    /**
     * 获取视频类型
     * @return
     */
    @GetMapping("/get/videoType")
    public BaseResponse<List<VideoTypeVo>> getVideoType(){
        List<VideoTypeVo> videoType = videoService.getVideoType();
        return ResultUtils.success(videoType);
    }


    /**
     * 点赞/取消 视频
     * @param videoThumbRequest
     * @return
     */
    @PostMapping("/thumb")
    public BaseResponse<Boolean> userThumbVideo(@RequestBody VideoThumbRequest videoThumbRequest){
        if(videoThumbRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoIdStr = videoThumbRequest.getVideoId();
        String userIdStr = videoThumbRequest.getUserId();
        if (StrUtil.hasBlank(videoIdStr,userIdStr)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.thumbVideo(Long.parseLong(videoIdStr), Long.parseLong(userIdStr));
        return ResultUtils.success(isSuccess);
    }

    /**
     * 收藏/取消 视频
     * @param videoFavourRequest
     * @return
     */
    @PostMapping("/favour")
    public BaseResponse<Boolean> favourVideo(@RequestBody VideoFavourRequest videoFavourRequest){
        if(videoFavourRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoIdStr = videoFavourRequest.getVideoId();
        String userIdStr = videoFavourRequest.getUserId();
        if (StrUtil.hasBlank(videoIdStr,userIdStr)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.favourVideo(Long.parseLong(videoIdStr), Long.parseLong(userIdStr));
        return ResultUtils.success(isSuccess);
    }



    /**
     * 添加评论
     * @param videoAddCommentRequest
     * @return
     */
    @PostMapping("/add/comment")
    public BaseResponse<Boolean> commentVideo(@RequestBody VideoAddCommentRequest videoAddCommentRequest){
        if(videoAddCommentRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.addVideoComment(videoAddCommentRequest);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 获取视频评论
     * @param videoId
     * @return
     */
    @GetMapping("/get/comment")
    public BaseResponse<List<VideoCommentVO>> getVideoComment(String videoId,@RequestParam(required = false) String userId){
        if(StrUtil.isBlank(videoId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        List<VideoCommentVO> videoComment = videoService.getVideoComment(Long.parseLong(videoId), userId);
        return ResultUtils.success(videoComment);
    }

    /**
     * 点赞/取消 评论
     * @param videoThumbCommentRequest
     * @return
     */
    @PostMapping("/thumb/comment")
    public BaseResponse<Boolean> thumbComment(@RequestBody VideoThumbCommentRequest videoThumbCommentRequest){
        if(videoThumbCommentRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoCommentIdStr = videoThumbCommentRequest.getVideoCommentId();
        String userIdStr = videoThumbCommentRequest.getUserId();
        if (StrUtil.hasBlank(videoCommentIdStr,userIdStr)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.thumbVideoComment(Long.parseLong(videoCommentIdStr), Long.parseLong(userIdStr));
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除评论
     * @param userId
     * @param videoCommentId
     * @return
     */
    @DeleteMapping("/remove/comment")
    public BaseResponse<Boolean> removeVideoComment(String userId,String videoCommentId){
        if(StrUtil.hasBlank(userId,videoCommentId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = videoService.removeVideoComment(Long.parseLong(userId), Long.parseLong(videoCommentId));
        return ResultUtils.success(isSuccess);
    }
}
