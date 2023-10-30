package com.typeofNull.nullvideo.model.vo.vide;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description 用于点击进入某个视频
 */
@Data
public class VideoDetailVO implements Serializable {

    private String userId;

    private String videoId;

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
     * 视频类型
     */
    private String videoType;

    /**
     *
     */
    private Object videoTags;

    /**
     * 收藏数
     */
    private Integer videoFavourNum;

    /**
     * 视频分享数
     */
    private Integer videoShareNum;

    /**
     * 点赞数
     */
    private Integer videoThumbNum;

    /**
     * 播放量
     */
    private Long videoPlayNum;

    /**
     * 视频评论数
     */
    private Integer videoCommentNum;

    /**
     * o-审核中 1-审核通过 2-审核不通过 3-下架
     */
    private Integer videoStatus;

    /**
     * 视频权限
     */
    private Integer videoRole;


    /**
     *
     */
    private String createTime;

}
