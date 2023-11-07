package com.typeofNull.nullvideo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.exception.BusinessException;
import com.typeofNull.nullvideo.mapper.VideoMapper;
import com.typeofNull.nullvideo.model.dto.video.VideoAddCommentRequest;
import com.typeofNull.nullvideo.model.dto.video.VideoTagDealRequest;
import com.typeofNull.nullvideo.model.dto.video.VideoUpdateRequest;
import com.typeofNull.nullvideo.model.dto.video.VideoUploadRequest;
import com.typeofNull.nullvideo.model.entity.*;
import com.typeofNull.nullvideo.model.vo.search.SearchUserAndVideoVO;
import com.typeofNull.nullvideo.model.vo.search.SearchUserVO;
import com.typeofNull.nullvideo.model.vo.search.SearchVideoVO;
import com.typeofNull.nullvideo.model.vo.video.*;
import com.typeofNull.nullvideo.service.*;
import com.typeofNull.nullvideo.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.typeofNull.nullvideo.constant.UserConstant.USER_ANONYMITY_AVATAR;
import static com.typeofNull.nullvideo.constant.UserConstant.USER_DEFAULT_AVATAR;
import static com.typeofNull.nullvideo.constant.VideoConstant.*;

/**
* @author 徐小帅
* @description 针对表【video(视频表)】的数据库操作Service实现
* @createDate 2023-10-27 21:22:46
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

    @Resource
    private VideoTagService videoTagService;

    @Resource
    private VideoTagRelationService videoTagRelationService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private VideoTypeService videoTypeService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private VideoThumbService videoThumbService;

    @Resource
    private VideoFolderService videoFolderService;

    @Resource
    private VideoFavourFolderService videoFavourFolderService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<VideoTagVO> getVideoTag(String searchTag) {
        ArrayList<VideoTagVO> videoTagVOS;
        if(StrUtil.isBlank(searchTag)){ //说明是全部都要的
            //按照时间升序，拿最老创建的
            LambdaQueryWrapper<VideoTag> videoTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            videoTagLambdaQueryWrapper.orderByAsc(VideoTag::getCreateTime);
            videoTagLambdaQueryWrapper.last("limit 10");
            List<VideoTag> videoTags = videoTagService.list(videoTagLambdaQueryWrapper);
            videoTagVOS=setVideoTag(videoTags);
        }else{ //说明要特定的
            LambdaQueryWrapper<VideoTag> videoTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            videoTagLambdaQueryWrapper.like(VideoTag::getVideoTagName,searchTag);
            videoTagLambdaQueryWrapper.last("limit 10");
            List<VideoTag> list = videoTagService.list(videoTagLambdaQueryWrapper);
            videoTagVOS=setVideoTag(list);
        }
         return videoTagVOS;
    }

    /**
     * 上传视频
     * @param videoUploadRequest
     * @return
     */
    @Override
    @Transactional
    public boolean addVideo(VideoUploadRequest videoUploadRequest) {
        String userIdStr = videoUploadRequest.getUserId();
        long userId = Long.parseLong(userIdStr);
        //判断用户是否存在
        long count = userService.count(new QueryWrapper<User>().eq("id", userId));
        if(count<=0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        }
        //先处理视频
        Video video = new Video();
        BeanUtil.copyProperties(videoUploadRequest,video,"userId","videoRole");
        video.setUserId(userId);
        String videoRoleStr = videoUploadRequest.getVideoRole();
        //处理权限
        Integer videoRole=0;
        if(!StrUtil.isBlank(videoRoleStr)){
         videoRole=("公开").equals(videoRoleStr)?0:("好友可见").equals(videoRoleStr)?1:2;
        }
        video.setUserId(userId);
        //处理type
        String videoTypeId = videoUploadRequest.getVideoTypeId();
        if(StrUtil.isBlank(videoTypeId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"视频类型不能为空");
        }
        video.setVideoType(Integer.parseInt(videoTypeId));

        boolean isSuccess = this.save(video);
        if(!isSuccess){
            return false;
        }
        //还有videTagsId还没处理
        List<String> videoTagsId = videoUploadRequest.getVideoTagsId();
        if(!(videoTagsId==null||videoTagsId.size()<=0)){ //有传Tag
            for(String videoTagIdStr:videoTagsId){
                VideoTagRelation videoTagRelation = new VideoTagRelation();
                videoTagRelation.setVideoId(video.getId());
                long videTypeId = Long.parseLong(videoTagIdStr);
                videoTagRelation.setVideoTagId(videTypeId);
                boolean save = videoTagRelationService.save(videoTagRelation);
                if(!save){
                    return false;
                }
            }
        }

        return isSuccess;
    }

    @Override
    public List<VideoShowVO> getVideoShowVO(Long authorId,String userId,Integer begin) {
        int pageSize=VIDEO_DEFAULT_PAGESIZE;
        int offset=pageSize*begin;
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(StrUtil.isBlank(userId)||authorId!=Long.parseLong(userId)){ //如果不是作者或者是游客，只能看公开的,审核过的
            videoLambdaQueryWrapper.eq(Video::getVideoRole,0);
            videoLambdaQueryWrapper.eq(Video::getVideoStatus,1);
        }
        videoLambdaQueryWrapper.eq(Video::getUserId,authorId);
        videoLambdaQueryWrapper.orderByDesc(Video::getCreateTime);
        videoLambdaQueryWrapper.last("limit "+pageSize+" offset "+offset);
        List<Video> videoList = this.list(videoLambdaQueryWrapper);
        List<VideoShowVO> videoShowVOS = videoList.stream().map((item) -> {
            VideoShowVO videoShowVO = new VideoShowVO();
            BeanUtil.copyProperties(item, videoShowVO, "videoId", "videoPlayNum");
            videoShowVO.setVideoId(item.getId() + "");
            videoShowVO.setVideoPlayNum(item.getVideoPlayNum() + "");
            return videoShowVO;
        }).collect(Collectors.toList());
        return videoShowVOS;
    }

    @Override
    public List<VideoVO> getVideoShowOnMine(Long userId, Integer begin) {
        int pageSize=VIDEO_MINE_PAGESIZE;
        int offset=pageSize*begin;
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(Video::getUserId,userId);
        videoLambdaQueryWrapper.orderByDesc(Video::getCreateTime);
        videoLambdaQueryWrapper.last("limit "+pageSize+" offset "+offset);
        List<Video> videoList = this.list(videoLambdaQueryWrapper);
        List<VideoVO> videoVOS = videoList.stream().map((item) -> {
            VideoVO videoVO = new VideoVO();
            BeanUtil.copyProperties(item, videoVO, "videoId", "videoPlayNum",
                    "createTime", "videoRole", "videoStatus");
            videoVO.setVideoId(item.getId() + "");
            videoVO.setVideoPlayNum(item.getVideoPlayNum() + "");
            Integer videoRole = item.getVideoRole();
            videoVO.setVideoRole(videoRole == 2 ? "私密" : "公开");
            Integer videoStatus = item.getVideoStatus();
            String videoStatusStr = videoStatus == 0 ? "审核中" : videoStatus == 1 ? "已发布" :videoStatus==2?"审核未通过": "已下架";
            videoVO.setVideoStatus(videoStatusStr);
            String formatTime = DateUtil.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            videoVO.setCreateTime(formatTime);
            QueryWrapper<VideoComment> videoCommentQueryWrapper = new QueryWrapper<>();
            videoCommentQueryWrapper.eq("video_id",item.getId());
            long count = videoCommentService.count(videoCommentQueryWrapper);
            videoVO.setVideoCommentNum((int)count);
            return videoVO;
        }).collect(Collectors.toList());
        return videoVOS;
    }

    @Override
    public VideoDetailVO getVideoDetail(Long videoId,String userId) {
        Video video = this.getById(videoId);
        //播放+1
        this.update().setSql("video_play_num=video_play_num+1").eq("id",videoId).update();
        if(video==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"视频不存在或已删除");
        }
        VideoDetailVO videoDetailVO = new VideoDetailVO();
        BeanUtil.copyProperties(video,videoDetailVO,"videoPlayNum","createTime");
        //先设置Video内容
        videoDetailVO.setVideoPlayNum(video.getVideoPlayNum()+"");
        String formatTime = DateUtil.format(video.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
        videoDetailVO.setCreateTime(formatTime);
        videoDetailVO.setAuthorId(video.getUserId()+"");
        //设置video评论数
        QueryWrapper<VideoComment> videoCommentQueryWrapper = new QueryWrapper<VideoComment>().eq("video_id", videoId);
        long count = videoCommentService.count(videoCommentQueryWrapper);
        videoDetailVO.setVideoCommentNum((int)count);
        //在设置videoTag
        LambdaQueryWrapper<VideoTagRelation> videoTagRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoTagRelationLambdaQueryWrapper.eq(VideoTagRelation::getVideoId, video.getId());
        List<VideoTagRelation> list = videoTagRelationService.list(videoTagRelationLambdaQueryWrapper);
        //拿ID
        List<Long> tagIds = list.stream().map((item) -> {
            Long videoTagId = item.getVideoTagId();
            return videoTagId;
        }).collect(Collectors.toList());
        List<String> tags=new ArrayList<>();
        if(tagIds.size()>0&&tagIds!=null){
            List<VideoTag> videoTags = videoTagService.listByIds(tagIds);
            //拿着id找name
            tags = videoTags.stream().map((item) -> {
                return item.getVideoTagName();
            }).collect(Collectors.toList());
        }
        videoDetailVO.setVideoTags(tags);
        //最后处理User
        User user = userService.getById(video.getUserId());
        videoDetailVO.setAuthorName(user.getUserName());
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", user.getId()));
        videoDetailVO.setAuthorAvatar(userInfo.getUserAvatar());
        if(userId==null){ //未登入，->未收藏，未登入
            videoDetailVO.setIsFavour(1);
            videoDetailVO.setIsThumb(1);
        }else{
            String videoThumbKey=VIDEO_THUMB_KEY+videoId;
            String videoFavourKey=VIDEO_FAVOUR_KEY+videoId;
            Double thumbScore = stringRedisTemplate.opsForZSet().score(videoThumbKey, userId.toString());
            videoDetailVO.setIsThumb(thumbScore==null?1:0);
            Double favourScore = stringRedisTemplate.opsForZSet().score(videoFavourKey, userId.toString());
            videoDetailVO.setIsFavour(favourScore==null?1:0);
        }
        return videoDetailVO;
    }

    @Override
    @Transactional
    public boolean updateVideoInfo(VideoUpdateRequest videoUpdateRequest) {
        Long videoId = Long.parseLong(videoUpdateRequest.getVideoId());
        Video video = getById(videoId);
        BeanUtil.copyProperties(videoUpdateRequest,video,"videoId","videoRole");
        String videoRole = videoUpdateRequest.getVideoRole();
        if(videoRole !=null){ //修改权限
        Integer videoRoleReal=("公开").equals(videoRole)?0:("好友可见").equals(videoRole)?1:2;
        video.setVideoRole(videoRoleReal);
        }
        boolean isSuccess = this.updateById(video);
        if(!isSuccess){
            return false;
        }
        List<VideoTagDealRequest> videoTagDeal = videoUpdateRequest.getVideoTagDeal();
        if(videoTagDeal!=null){ //修改Tag
            for(VideoTagDealRequest v:videoTagDeal){
                Integer operation = v.getOperation();
                String videoTagIdStr = v.getVideoTagId();
                if(StrUtil.isBlank(videoTagIdStr)){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"VideoTagId不能为空");
                }
                long videoTagId = Long.parseLong(videoTagIdStr);
                //处理
                if(operation==0){ //0-删 1-加
                    //构造条件
                    QueryWrapper<VideoTagRelation> relationQueryWrapper = new QueryWrapper<>();
                    relationQueryWrapper.eq("video_id",videoId);
                    relationQueryWrapper.eq("video_tag_id",videoTagId);
                    isSuccess=videoTagRelationService.remove(relationQueryWrapper);
                }else if(operation==1){
                    VideoTagRelation videoTagRelation = new VideoTagRelation();
                    videoTagRelation.setVideoId(videoId);
                    videoTagRelation.setVideoTagId(videoTagId);
                    isSuccess=videoTagRelationService.save(videoTagRelation);
                }else{
                    throw new BusinessException(ErrorCode.PARAMS_ERROR);
                }
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional
    public boolean removeVideo(Long userId, Long videoId) {
        QueryWrapper<VideoTagRelation> videoTagRelationQueryWrapper = new QueryWrapper<>();
        videoTagRelationQueryWrapper.eq("video_id", videoId);
        Video video = getById(videoId);
        if(video.getUserId()!=userId){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"不可删除非自己的视频");
        }
        boolean isSuccess = removeById(video);
        if(!isSuccess){
            return false;
        }
        isSuccess = videoTagRelationService.remove(videoTagRelationQueryWrapper);
        return isSuccess;
    }

    @Override
    public List<VideoTypeVo> getVideoType() {
        QueryWrapper<VideoType> videoTypeQueryWrapper = new QueryWrapper<>();
        videoTypeQueryWrapper.eq("is_parent",0);
        List<VideoType> videoTypes = videoTypeService.list(videoTypeQueryWrapper);
        List<VideoTypeVo> videoTypeVos = videoTypes.stream().map((item) -> { //先找父类再找子类
            Long videoParentId = item.getId();
            VideoTypeVo videoTypeVo = new VideoTypeVo();
            videoTypeVo.setVideoTypeId(videoParentId + "");
            videoTypeVo.setVideoTypeName(item.getVideoTypeName());
            QueryWrapper<VideoType> videoTopicQueryWrapper = new QueryWrapper<>();
            videoTopicQueryWrapper.eq("parent_id", videoParentId);
            List<VideoType> videoTopicList = videoTypeService.list(videoTopicQueryWrapper);
            ArrayList<VideoTypeTopicVO> videoTypeTopicVOS = new ArrayList<>();
            //设置子主题
            for (VideoType v : videoTopicList) {
                VideoTypeTopicVO videoTypeTopicVO = new VideoTypeTopicVO();
                videoTypeTopicVO.setVideoTypeId(v.getId() + "");
                videoTypeTopicVO.setVideoTopicName(v.getVideoTopic());
                videoTypeTopicVO.setVideoTopicDescription(v.getVideoTopicDescription());
                videoTypeTopicVOS.add(videoTypeTopicVO);
            }
            videoTypeVo.setVideoTypeTopic(videoTypeTopicVOS);
            return videoTypeVo;
        }).collect(Collectors.toList());
        return videoTypeVos;
    }

    @Override
    public VideoTagVO addTag(String videoTagName) {
        //先判断之前有没有存在过，没有才加
        QueryWrapper<VideoTag> videoTagQueryWrapper = new QueryWrapper<>();
        videoTagQueryWrapper.eq("video_tag_name",videoTagName);
        long count = videoTagService.count(videoTagQueryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"数据已存在,不可重复添加");
        }
        //创建
        VideoTag videoTag = new VideoTag();
        videoTag.setVideoTagName(videoTagName);
        boolean isSuccess = videoTagService.save(videoTag);
        if(!isSuccess){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        VideoTagVO videoTagVO = new VideoTagVO();
        videoTagVO.setVideoTagId(videoTag.getId()+"");
        videoTagVO.setVideoTagName(videoTagName);
        return videoTagVO;
    }

    @Override
    @Transactional
    public boolean thumbVideo(Long videoId, Long userId) {
        User user = userService.getById(userId);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户数据不存在");
        }
        Video video = this.getById(videoId);
        if(video==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"视频数据不存在");
        }
        String videoThumbKey = VIDEO_THUMB_KEY + videoId;
        boolean isSuccess;
        //判断用户有没有点过赞
        Double score = stringRedisTemplate.opsForZSet().score(videoThumbKey, userId.toString());
        if(score!=null){
            //点过赞，取消，把用户信息移除set
            isSuccess = update().setSql("video_thumb_num=video_thumb_num-1").eq("id", videoId).update();
            QueryWrapper<VideoThumb> videoThumbQueryWrapper = new QueryWrapper<VideoThumb>().eq("video_id", videoId)
                                                                    .eq("user_id", userId);
            boolean remove = videoThumbService.remove(videoThumbQueryWrapper);
            if(isSuccess && remove){
                stringRedisTemplate.opsForZSet().remove(videoThumbKey,userId.toString());
            }
        }else {
            //如果没点过赞，点赞，并把用户信息放到set中
            isSuccess = this.update().setSql("video_thumb_num=video_thumb_num+1").eq("id", videoId).update();
            VideoThumb videoThumb = new VideoThumb(videoId, userId);
            boolean save = videoThumbService.save(videoThumb);
            if(isSuccess && save){
                stringRedisTemplate.opsForZSet().add(videoThumbKey,userId.toString(),System.currentTimeMillis());
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional
    public boolean favourVideo(Long videoId, Long userId) {
        User user = userService.getById(userId);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户数据不存在");
        }
        Video video = this.getById(videoId);
        if(video==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"视频数据不存在");
        }
        String videoFavourKey = VIDEO_FAVOUR_KEY + videoId;
        //找用户收藏夹id
        QueryWrapper<VideoFolder> videoFolderQueryWrapper = new QueryWrapper<VideoFolder>().eq("user_id", userId).eq("is_main", 0);
        VideoFolder videoFolder = videoFolderService.getOne(videoFolderQueryWrapper);
        Long videoFolderId = videoFolder.getId();
        boolean isSuccess;
        //判断用户有没有收藏过
        Double score = stringRedisTemplate.opsForZSet().score(videoFavourKey, userId.toString());
        if(score!=null){
            //收藏过，取消，把用户信息移除set
            //先把收藏数-1
            isSuccess = update().setSql("video_favour_num=video_favour_num-1").eq("id", videoId).update();
            //再把收藏记录删除
            QueryWrapper<VideoFavourFolder> videoFavourFolderQueryWrapper = new QueryWrapper<VideoFavourFolder>().eq("video_folder_id", videoFolderId)
                    .eq("user_id", userId).eq("video_id",videoId);
            boolean remove = videoFavourFolderService.remove(videoFavourFolderQueryWrapper);
            if(isSuccess && remove){
                stringRedisTemplate.opsForZSet().remove(videoFavourKey,userId.toString());
            }
        }else {
            //如果没收藏过，收藏，并把用户信息放到set中
            isSuccess = update().setSql("video_favour_num=video_favour_num+1").eq("id", videoId).update();
            VideoFavourFolder videoFavourFolder = new VideoFavourFolder(videoFolderId, videoId, userId);
            boolean save = videoFavourFolderService.save(videoFavourFolder);
            if(isSuccess && save){
                stringRedisTemplate.opsForZSet().add(videoFavourKey,userId.toString(),System.currentTimeMillis());
            }
        }
        return isSuccess;
    }


    /**
     * 用于查找指定类型的视频
     * @param videoTypeId
     * @return
     */
    public List<SearchVideoVO> getTypeVideo(Long videoTypeId){
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("video_type",videoTypeId);
        videoQueryWrapper.eq("video_status",1);
        videoQueryWrapper.eq("video_role",0);
        List<Video> videoList = this.list(videoQueryWrapper);
        List<SearchVideoVO> searchVideoVOS = videoList.stream().map(item -> {
            SearchVideoVO searchVideoVO = new SearchVideoVO();
            //先处理视频
            BeanUtil.copyProperties(item, searchVideoVO, "videoId",
                    "createTime");
            String formatTime = DateUtil.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            searchVideoVO.setCreateTime(formatTime);
            searchVideoVO.setVideoId(item.getId() + "");
            //再处理用户
            User user = userService.getById(item.getUserId());
            searchVideoVO.setAuthorName(user.getUserName());
            return searchVideoVO;
        }).collect(Collectors.toList());
        return searchVideoVOS;
    }

    @Override
    public SearchUserAndVideoVO searchAll(String searchText) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        //根据喜欢度降序
        videoQueryWrapper.like("video_description",searchText);
        videoQueryWrapper.orderByDesc("video_thumb_num");
        List<Video> videoList = this.list(videoQueryWrapper);
        SearchUserAndVideoVO searchUserAndVideoVO = new SearchUserAndVideoVO();
        List<SearchVideoVO> videoVOS = videoList.stream().map((item) -> {
            SearchVideoVO searchVideoVO = new SearchVideoVO();
            BeanUtil.copyProperties(item, searchVideoVO, "videoId", "createTime");
            searchVideoVO.setVideoId(item.getId() + "");
            User user = userService.getById(item.getUserId());
            searchVideoVO.setAuthorName(user.getUserName());
            String formatTime = DateUtil.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            searchVideoVO.setCreateTime(formatTime);
            return searchVideoVO;
        }).collect(Collectors.toList());
        //放视频
        searchUserAndVideoVO.setSearchVideo(videoVOS);
        //放用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("user_name",searchText);
        userQueryWrapper.eq("user_role",0);
        List<User> userList = userService.list(userQueryWrapper);
        if(userList!=null&&userList.size()!=0){ //如果不空就放
            SearchUserVO searchUserVO=null;
            //找到粉丝数最多的用户
            int maxFollower=0;
            for(User user:userList){
                Long userId = user.getId();
                QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                userInfoQueryWrapper.eq("user_id",userId);
                UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
                if(userInfo.getFollower()>=maxFollower){//如果找到大的就替换
                        searchUserVO = UserUtil.setSearchUserVO(userInfo,user.getUserName());
                        maxFollower=userInfo.getFollower();
                }
            }
                //放总获赞数
            Integer sumThumb=0;
            QueryWrapper<Video> videoQueryWrapperForThumb = new QueryWrapper<>();
            videoQueryWrapperForThumb.eq("user_id",Long.parseLong(searchUserVO.getAuthorId()));
            List<Video> videos = this.list(videoQueryWrapperForThumb);
            for(Video v:videos){
                sumThumb+=v.getVideoThumbNum();
            }
            searchUserVO.setAuthorVideoTotalThumbNum(sumThumb);
            //设置User
            searchUserAndVideoVO.setSearchUser(searchUserVO);
            }else{
            searchUserAndVideoVO.setSearchUser(new SearchUserVO());
        }
        return searchUserAndVideoVO;
    }

    @Override
    public List<SearchVideoVO> searchVideo(String searchText) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("video_status",1);
        videoQueryWrapper.like("video_description",searchText);
        List<Video> videoList = this.list(videoQueryWrapper);
        List<SearchVideoVO> videoVOS = videoList.stream().map((item) -> {
            SearchVideoVO searchVideoVO = new SearchVideoVO();
            BeanUtil.copyProperties(item, searchVideoVO, "videoId", "createTime");
            searchVideoVO.setVideoId(item.getId() + "");
            User user = userService.getById(item.getUserId());
            searchVideoVO.setAuthorName(user.getUserName());
            String formatTime = DateUtil.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            searchVideoVO.setCreateTime(formatTime);
            return searchVideoVO;
        }).collect(Collectors.toList());
        return videoVOS;
    }

    @Override
    public List<VideoShowVO> getThumbOrFavourVideo(Long userId,Integer option) {
        List<VideoShowVO> videoShowVOS ;
        if(option==0){ //找喜欢
            QueryWrapper<VideoThumb> videoThumbQueryWrapper = new QueryWrapper<>();
            videoThumbQueryWrapper.eq("user_id",userId);
            List<VideoThumb> list = videoThumbService.list(videoThumbQueryWrapper);
             videoShowVOS = list.stream().map((item) -> {
                Long videoId = item.getVideoId();
                Video video = this.getById(videoId);
                VideoShowVO videoShowVO = new VideoShowVO();
                BeanUtil.copyProperties(video, videoShowVO, "videoId", "videoPlayNum");
                videoShowVO.setVideoId(video.getId() + "");
                videoShowVO.setVideoPlayNum(video.getVideoPlayNum() + "");
                return videoShowVO;
            }).collect(Collectors.toList());
        }else{ //找收藏
            QueryWrapper<VideoFavourFolder> videoFavourQueryWrapper = new QueryWrapper<>();
            videoFavourQueryWrapper.eq("user_id",userId);
            List<VideoFavourFolder> list = videoFavourFolderService.list(videoFavourQueryWrapper);
            videoShowVOS = list.stream().map((item) -> {
                Long videoId = item.getVideoId();
                Video video = this.getById(videoId);
                VideoShowVO videoShowVO = new VideoShowVO();
                BeanUtil.copyProperties(video, videoShowVO, "videoId", "videoPlayNum");
                videoShowVO.setVideoId(video.getId() + "");
                videoShowVO.setVideoPlayNum(video.getVideoPlayNum() + "");
                return videoShowVO;
            }).collect(Collectors.toList());
        }
        return videoShowVOS;
    }

    @Override

    public VideoCommentVO addVideoComment(VideoAddCommentRequest videoAddCommentRequest) {
        //评论内容不可为空
        if(("").equals(StringUtils.deleteWhitespace(videoAddCommentRequest.getVideoCommentContent()))){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"评论内容不可为空");
        }
        String videoCommentIdStr = videoAddCommentRequest.getVideoCommentId();
        String videoIdStr = videoAddCommentRequest.getVideoId();
        if(StrUtil.isBlank(videoIdStr)){ //校验
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"视频异常");
        }
        String userIdStr = videoAddCommentRequest.getUserId();
        if(StrUtil.isBlank(userIdStr)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户未登入或账号异常");
        }
        long videoId = Long.parseLong(videoIdStr);
        Video video = this.getById(videoId);
        if(video==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"视频已删除或不存在");
        }
        long userId = Long.parseLong(userIdStr);
        VideoComment videoComment = new VideoComment(userId,videoId);
        //判断是一级评论还是二级评论还是三级评论
        if(StrUtil.isBlank(videoCommentIdStr)){//回复的为空，表示是一级评论
            videoComment.setVideoCommentContent(videoAddCommentRequest.getVideoCommentContent());
        }else{ //说明在对二级评论或三级评论 进行评论
            long videoCommentId = Long.parseLong(videoCommentIdStr);
            VideoComment videoTempComment = videoCommentService.getById(videoCommentId);
            if(videoTempComment==null){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"评论已删除不可评论");
            }
            //判断到底是二级还是三级,如果是二级就设置parentId
            if(videoTempComment.getParentId()==null){
                videoComment.setParentId(videoTempComment.getId());
            }else{
                videoComment.setParentId(videoTempComment.getParentId());
            }
            videoComment.setAnswerId(videoTempComment.getId());
            videoComment.setVideoCommentContent(videoAddCommentRequest.getVideoCommentContent());
        }
        boolean isSuccess = videoCommentService.save(videoComment);
        //处理返回视图
        if(!isSuccess){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"评论失败");
        }
        VideoComment videoCommentNow = videoCommentService.getOne(new QueryWrapper<VideoComment>().eq("id", videoComment.getId()));
        Long parentId = videoComment.getParentId();
        VideoCommentVO videoCommentVO;
        if(parentId ==null){ //一级评论直接传
            videoCommentVO = getVideoCommentVO(videoCommentNow, userId + "");
        }else{ //点赞的是二级或三级评论，先找一级评论
            VideoComment firstComment = videoCommentService.getOne(new QueryWrapper<VideoComment>().eq("id", parentId));
            videoCommentVO=getVideoCommentVO(firstComment,userId+"");
        }
        return videoCommentVO;
    }

    @Override
    public List<VideoCommentVO> getVideoComment(Long videoId,String userId) {
        Video video = this.getById(videoId);
        if(video==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"视频已删除或不存在");
        }
        //先找一级评论,再处理二三级
        QueryWrapper<VideoComment> videoCommentQueryWrapper = new QueryWrapper<>();
        videoCommentQueryWrapper.eq("video_id",videoId);
        videoCommentQueryWrapper.eq("video_comment_status",0);
        videoCommentQueryWrapper.orderByDesc("video_comment_thumb");
        videoCommentQueryWrapper.orderByDesc("create_time");
        videoCommentQueryWrapper.isNull("parent_id");
        List<VideoComment> videoCommentList = videoCommentService.list(videoCommentQueryWrapper);
        List<VideoCommentVO> videoCommentVOS = videoCommentList.stream().map(item -> {
            /*
             //处理一级
            VideoCommentVO videoCommentVO = setVideoComment(item,userId);
            videoCommentVO.setIsChild(false);
            videoCommentVO.setVideoCommentTo(null);
            //处理完一级处理二三级，先按照点赞数降序，再按照时间降序
            QueryWrapper<VideoComment> videoCommentQueryWrapperForSecond = new QueryWrapper<>();
            videoCommentQueryWrapperForSecond.eq("video_id", videoId);
            videoCommentQueryWrapperForSecond.eq("parent_id", item.getId());
            videoCommentQueryWrapperForSecond.eq("video_comment_status",0);
            videoCommentQueryWrapperForSecond.orderByDesc("video_comment_thumb");
            videoCommentQueryWrapperForSecond.orderByDesc("create_time");
            List<VideoComment> videoComments = videoCommentService.list(videoCommentQueryWrapperForSecond);
            //处理二/三级评论
            List<VideoCommentVO> otherComment = videoComments.stream().map(secondOrThirdComment -> {
                Long answerId = secondOrThirdComment.getAnswerId();
                Long parentId = secondOrThirdComment.getParentId();
                if (answerId == parentId) {//如果回复的评论是一级评论，否则肯定是三级评论
                    VideoCommentVO secondComment = setVideoComment(secondOrThirdComment,userId);
                    secondComment.setIsChild(true);
                    videoCommentVO.setVideoCommentTo(null);
                    return secondComment;
                } else {//三级评论
                    VideoCommentVO thirdComment = setVideoComment(secondOrThirdComment,userId);
                    thirdComment.setIsChild(true);
                    VideoCommentToVO videoCommentToVO = new VideoCommentToVO();
                    //处理一下回复的用户信息
                    VideoComment answerComment = videoCommentService.getById(answerId);
                    if(answerComment==null){//说明要回复的那条评论已经删掉了
                        videoCommentToVO.setVideoCommentToUserName("评论已删除");
                        videoCommentToVO.setVideoCommentToUserAvatar(USER_ANONYMITY_AVATAR);
                    }else{
                        //评论没有被删
                        User user = userService.getById(answerComment.getUserId());
                        if (user == null) {//用户注销
                            videoCommentToVO.setVideoCommentToUserName("用户已注销");
                            videoCommentToVO.setVideoCommentToUserAvatar(USER_ANONYMITY_AVATAR);
                        } else {
                            videoCommentToVO.setVideoCommentToUserName(user.getUserName());
                            videoCommentToVO.setVideoCommentToUserId(user.getId() + "");
                            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                            userInfoQueryWrapper.eq("user_id",user.getId());
                            UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
                            videoCommentToVO.setVideoCommentToUserAvatar(userInfo.getUserAvatar());
                        }
                    }
                    thirdComment.setVideoCommentTo(videoCommentToVO);
                    return thirdComment;
                }
            }).collect(Collectors.toList());
            videoCommentVO.setVideoCommentChildren(otherComment);
             */
            VideoCommentVO videoCommentVO = getVideoCommentVO(item, userId);
            if(videoCommentVO.getVideoCommentChildren()==null){
                videoCommentVO.setVideoCommentChildren(new ArrayList<VideoCommentVO>());
            }
            return videoCommentVO;
        }).collect(Collectors.toList());
        return videoCommentVOS;
    }

    @Override
    public boolean thumbVideoComment(Long videoCommentId,Long userId) {
        String videoCommentKey=VIDEO_COMMENT_THUMB_KEY+videoCommentId;
        Double score = stringRedisTemplate.opsForZSet().score(videoCommentKey, userId.toString());
        boolean isSuccess;
        if(score!=null){
            //收藏过，取消，把用户信息移除set
            //先把收藏数-1
            isSuccess = videoCommentService.update().setSql("video_comment_thumb=video_comment_thumb-1").eq("id", videoCommentId).update();
            //再把收藏记录删除
            if(isSuccess){
                stringRedisTemplate.opsForZSet().remove(videoCommentKey,userId.toString());
            }
        }else {
            //如果没收藏过，收藏，并把用户信息放到set中
            isSuccess = videoCommentService.update().setSql("video_comment_thumb=video_comment_thumb+1").eq("id", videoCommentId).update();
            if(isSuccess){
                stringRedisTemplate.opsForZSet().add(videoCommentKey,userId.toString(),System.currentTimeMillis());
            }
        }
        return isSuccess;
    }

    @Override
    public boolean removeVideoComment(Long userId, Long videoCommentId) {
        VideoComment videoComment = videoCommentService.getById(videoCommentId);
        if(videoComment==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"评论已删除或不存在");
        }
        Video video = this.getById(videoComment.getVideoId());
        Long authorId = video.getUserId();
        if(authorId==userId|| userId==videoComment.getUserId()){
            boolean isSuccess = videoCommentService.removeById(videoCommentId);
            return isSuccess;
        }else{
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    @Override
    public List<VideoDetailForRandomVO> getRandomVideo(String videoTypeId,String userId) {
        //看看是用于主页还是特定type
        if(StrUtil.isBlank(videoTypeId)){ //首页
            ArrayList<Long> videoIds = new ArrayList<>();
            // 创建一个查询条件对象
            QueryWrapper<Video> queryWrapperForMin = new QueryWrapper<>();
            QueryWrapper<Video> queryWrapperForMax = new QueryWrapper<>();
            // 使用 MyBatis Plus 提供的方法获取最小和最大 ID
            Video minEntity = getOne(queryWrapperForMin.orderByAsc("id").last("LIMIT 1"));
            Video maxEntity = getOne(queryWrapperForMax.orderByDesc("id").last("LIMIT 1"));
            long count = this.count();
            if (minEntity != null && maxEntity != null) { //获得随机id
                Long minId = minEntity.getId();
                Long maxId = maxEntity.getId();
                if(count<10){//没超过10个就每一个都要
                    for (Long i = minId; i <= maxId; i++) {
                        videoIds.add(i);
                    }
                }else{//超过10个数据就random
                    for(int i=0;i<10;i++){
                        videoIds.add(RandomUtil.randomLong(minId,maxId+1l));
                    }
                }
            }
            List<VideoDetailForRandomVO> videoDetailVOS = new ArrayList<>();
            //配对视频
            for(Long videoId:videoIds){
                //如果没找到就跳过
                Video video = this.getOne(new QueryWrapper<Video>().eq("id", videoId));
                if(video==null){
                    continue;
                }
                VideoDetailVO videoDetail = this.getVideoDetail(videoId, userId);
                VideoDetailForRandomVO videoDetailForRandomVO = new VideoDetailForRandomVO();
                BeanUtil.copyProperties(videoDetail,videoDetailForRandomVO);
                videoDetailForRandomVO.setVideoId(videoId+"");
                videoDetailVOS.add(videoDetailForRandomVO);
            }
                return videoDetailVOS;
        }else{ //具体Type
            QueryWrapper<VideoType> videoTypeQueryWrapper = new QueryWrapper<>();
            long realVideoTypeId = Long.parseLong(videoTypeId);
            //找type分类下的
            videoTypeQueryWrapper.eq("id",realVideoTypeId).or()
                    .eq("parent_id",realVideoTypeId);
            List<VideoType> videoTypes = videoTypeService.list(videoTypeQueryWrapper);
            if (videoTypes.isEmpty()) {
                // 处理 typeIds 为空的情况，例如抛出异常或返回默认值
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"typeId不能为空");
            }
            //获得typeId
            List<Long> typeIds = videoTypes.stream().map(item -> item.getId()).collect(Collectors.toList());
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.in("video_type",typeIds);
            videoQueryWrapper.orderByDesc("RAND()");
            videoQueryWrapper.last("limit 10");
            //随机获取video
            List<Video> videos = this.list(videoQueryWrapper);
            //对video进行封装
            List<VideoDetailForRandomVO> videoDetailVOS = videos.stream().map(item -> {
                VideoDetailVO videoDetail = this.getVideoDetail(item.getId(), userId);
                VideoDetailForRandomVO videoDetailForRandomVO = new VideoDetailForRandomVO();
                BeanUtil.copyProperties(videoDetail,videoDetailForRandomVO);
                videoDetailForRandomVO.setVideoId(item.getId()+"");
                return videoDetailForRandomVO;
            }).collect(Collectors.toList());
            return videoDetailVOS;
        }
    }


    /**
     * 根据父评论设置子评论
     * @param FirstComment
     * @param userId
     * @return
     */
    private VideoCommentVO getVideoCommentVO(VideoComment FirstComment,String userId){
        //处理一级
        VideoCommentVO videoCommentVO = setVideoComment(FirstComment,userId);
        videoCommentVO.setIsChild(false);
        videoCommentVO.setVideoCommentTo(null);
        //处理完一级处理二三级，先按照点赞数降序，再按照时间降序
        QueryWrapper<VideoComment> videoCommentQueryWrapperForSecond = new QueryWrapper<>();
        videoCommentQueryWrapperForSecond.eq("video_id", FirstComment.getVideoId());
        videoCommentQueryWrapperForSecond.eq("parent_id", FirstComment.getId());
        videoCommentQueryWrapperForSecond.eq("video_comment_status",0);
        videoCommentQueryWrapperForSecond.orderByAsc("create_time");
        List<VideoComment> videoComments = videoCommentService.list(videoCommentQueryWrapperForSecond);
        //处理二/三级评论
        List<VideoCommentVO> otherComment = videoComments.stream().map(secondOrThirdComment -> {
            Long answerId = secondOrThirdComment.getAnswerId();
            Long parentId = secondOrThirdComment.getParentId();
            if (answerId == parentId) {//如果回复的评论是一级评论，否则肯定是三级评论
                VideoCommentVO secondComment = setVideoComment(secondOrThirdComment,userId);
                secondComment.setIsChild(true);
                secondComment.setVideoCommentTo(null);
                secondComment.setVideoCommentChildren(new ArrayList<VideoCommentVO>());
                return secondComment;
            } else {//三级评论
                VideoCommentVO thirdComment = setVideoComment(secondOrThirdComment,userId);
                thirdComment.setIsChild(true);
                thirdComment.setVideoCommentChildren(new ArrayList<VideoCommentVO>());
                VideoCommentToVO videoCommentToVO = new VideoCommentToVO();
                //处理一下回复的用户信息
                VideoComment answerComment = videoCommentService.getById(answerId);
                if(answerComment==null){//说明要回复的那条评论已经删掉了
                    videoCommentToVO.setVideoCommentToUserName("评论已删除");
                    videoCommentToVO.setVideoCommentToUserAvatar(USER_ANONYMITY_AVATAR);
                }else{
                    //评论没有被删
                    User user = userService.getById(answerComment.getUserId());
                    if (user == null) {//用户注销
                        videoCommentToVO.setVideoCommentToUserName("用户已注销");
                        videoCommentToVO.setVideoCommentToUserAvatar(USER_ANONYMITY_AVATAR);
                    } else {
                        videoCommentToVO.setVideoCommentToUserName(user.getUserName());
                        videoCommentToVO.setVideoCommentToUserId(user.getId() + "");
                        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                        userInfoQueryWrapper.eq("user_id",user.getId());
                        UserInfo userInfo = userInfoService.getOne(userInfoQueryWrapper);
                        videoCommentToVO.setVideoCommentToUserAvatar(userInfo.getUserAvatar());
                    }
                }
                thirdComment.setVideoCommentTo(videoCommentToVO);
                return thirdComment;
            }
        }).collect(Collectors.toList());
        videoCommentVO.setVideoCommentChildren(otherComment);
        return videoCommentVO;
    }


    /**
     * VideoCommentVO转换
     * @param videoComment
     * @return
     */
    private VideoCommentVO setVideoComment(VideoComment videoComment,String userId){
        VideoCommentVO videoCommentVO = new VideoCommentVO();
        videoCommentVO.setVideoCommentId(videoComment.getId()+"");
        videoCommentVO.setVideoCommentUserId(videoComment.getUserId()+"");
        String formatTime = DateUtil.format(videoComment.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
        videoCommentVO.setVideoCommentCreateTime(formatTime);
        videoCommentVO.setVideoCommentThumbNum(videoComment.getVideoCommentThumb());
        videoCommentVO.setVideoCommentContent(videoComment.getVideoCommentContent());
        //处理用户
        User user = userService.getById(videoComment.getUserId());
        String userName=user==null?"用户已注销":user.getUserName();
        videoCommentVO.setVideoCommentUserName(userName);
        if(user!=null){ //头像给默认头像
            UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", user.getId()));
            videoCommentVO.setVideoCommentUserAvatar(userInfo.getUserAvatar());
        }else{
            videoCommentVO.setVideoCommentUserAvatar(USER_DEFAULT_AVATAR);
        }
        //处理是否点赞
        if(StrUtil.isNotBlank(userId)){
            String videoCommentKey=VIDEO_COMMENT_THUMB_KEY+videoComment.getId();
            Double score = stringRedisTemplate.opsForZSet().score(videoCommentKey, userId);
            videoCommentVO.setIsThumb(score==null?1:0);
        }else{
            videoCommentVO.setIsThumb(1);

        }
        return videoCommentVO;
    }

    /**
     * VideoTagVO转换
     * @param videoTags
     * @return
     */
    private ArrayList<VideoTagVO> setVideoTag(List<VideoTag> videoTags){
        ArrayList<VideoTagVO> videoTagVOS = new ArrayList<>();
        for(VideoTag item:videoTags) {
            VideoTagVO videoTagVO = new VideoTagVO();
            videoTagVO.setVideoTagId(item.getId()+"");
            videoTagVO.setVideoTagName(item.getVideoTagName());
            videoTagVOS.add(videoTagVO);
        }
        return videoTagVOS;
    }
}




