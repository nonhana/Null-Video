package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description 用于点击进入某个视频
 */
@Data
public class VideoDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String authorId;

    private String authorAvatar;

    private String authorName;

    /**
     *
     */
    private String videoCoverUrl;

    /**
     *
     */
    private String videoUrl;

    /**
     *
     */
    private String videoDescription;

    /**
     *
     */
    private List<String> videoTags;

    /**
     * 收藏数
     */
    private Integer videoFavourNum;
    /**
     * 是否收藏了 0-收藏 1-没收藏
     */
    private Integer isFavour;

    /**
     * 视频分享数
     */
    private Integer videoShareNum;

    /**
     * 点赞数
     */
    private Integer videoThumbNum;

    /**
     * 是否点赞 0-点赞 1-没点赞
     */
    private Integer isThumb;

    /**
     * 播放量
     */
    private String videoPlayNum;

    /**
     * 视频评论数
     */
    private Integer videoCommentNum;


    /**
     *
     */
    private String createTime;

}
