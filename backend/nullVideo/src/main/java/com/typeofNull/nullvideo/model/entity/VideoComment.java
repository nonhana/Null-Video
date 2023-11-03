package com.typeofNull.nullvideo.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 视频评论表
 * @TableName video_comment
 */
@TableName(value ="video_comment")
@Data
public class VideoComment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long videoId;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long parentId;

    /**
     * 
     */
    private Long answerId;

    /**
     * 
     */
    private String videoCommentContent;

    /**
     * o-正常 1-被举报 2-下架
     */
    private Integer videoCommentStatus;

    /**
     * 点赞数
     */
    private Integer videoCommentThumb;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public VideoComment(){

    }
    public VideoComment(Long userId,Long videoId){
        this.userId=userId;
        this.videoId=videoId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        VideoComment other = (VideoComment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getVideoId() == null ? other.getVideoId() == null : this.getVideoId().equals(other.getVideoId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getAnswerId() == null ? other.getAnswerId() == null : this.getAnswerId().equals(other.getAnswerId()))
            && (this.getVideoCommentContent() == null ? other.getVideoCommentContent() == null : this.getVideoCommentContent().equals(other.getVideoCommentContent()))
            && (this.getVideoCommentStatus() == null ? other.getVideoCommentStatus() == null : this.getVideoCommentStatus().equals(other.getVideoCommentStatus()))
            && (this.getVideoCommentThumb() == null ? other.getVideoCommentThumb() == null : this.getVideoCommentThumb().equals(other.getVideoCommentThumb()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getVideoId() == null) ? 0 : getVideoId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getAnswerId() == null) ? 0 : getAnswerId().hashCode());
        result = prime * result + ((getVideoCommentContent() == null) ? 0 : getVideoCommentContent().hashCode());
        result = prime * result + ((getVideoCommentStatus() == null) ? 0 : getVideoCommentStatus().hashCode());
        result = prime * result + ((getVideoCommentThumb() == null) ? 0 : getVideoCommentThumb().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", videoId=").append(videoId);
        sb.append(", userId=").append(userId);
        sb.append(", parentId=").append(parentId);
        sb.append(", answerId=").append(answerId);
        sb.append(", videoCommentContent=").append(videoCommentContent);
        sb.append(", videoCommentStatus=").append(videoCommentStatus);
        sb.append(", videoCommentThumb=").append(videoCommentThumb);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}