package com.typeofNull.nullvideo.model.vo.video;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/30
 * @Description 用于个人内容管理部分视频展示
 */
@Data
public class VideoVO extends VideoShowVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 视频点赞数
     */
    private Integer videoThumbNum;

    /**
     * 视频评论数
     */
    private Integer videoCommentNum;

    /**
     * 视频权限  私密/公开
     */
    private String videoRole;

    /**
     * 视频状态 -审核中/已发布/审核不通过/已下架
     */
    private String videoStatus;

    /**
     * 发布时间
     */
    private String createTime;
}
