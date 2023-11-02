package com.typeofNull.nullvideo.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 视频表
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long userId;

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
    private Integer videoType;


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
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        Video other = (Video) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getVideoCoverUrl() == null ? other.getVideoCoverUrl() == null : this.getVideoCoverUrl().equals(other.getVideoCoverUrl()))
            && (this.getVideoUrl() == null ? other.getVideoUrl() == null : this.getVideoUrl().equals(other.getVideoUrl()))
            && (this.getVideoDescription() == null ? other.getVideoDescription() == null : this.getVideoDescription().equals(other.getVideoDescription()))
            && (this.getVideoType() == null ? other.getVideoType() == null : this.getVideoType().equals(other.getVideoType()))
            && (this.getVideoFavourNum() == null ? other.getVideoFavourNum() == null : this.getVideoFavourNum().equals(other.getVideoFavourNum()))
            && (this.getVideoShareNum() == null ? other.getVideoShareNum() == null : this.getVideoShareNum().equals(other.getVideoShareNum()))
            && (this.getVideoThumbNum() == null ? other.getVideoThumbNum() == null : this.getVideoThumbNum().equals(other.getVideoThumbNum()))
            && (this.getVideoPlayNum() == null ? other.getVideoPlayNum() == null : this.getVideoPlayNum().equals(other.getVideoPlayNum()))
            && (this.getVideoStatus() == null ? other.getVideoStatus() == null : this.getVideoStatus().equals(other.getVideoStatus()))
            && (this.getVideoRole() == null ? other.getVideoRole() == null : this.getVideoRole().equals(other.getVideoRole()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getVideoCoverUrl() == null) ? 0 : getVideoCoverUrl().hashCode());
        result = prime * result + ((getVideoUrl() == null) ? 0 : getVideoUrl().hashCode());
        result = prime * result + ((getVideoDescription() == null) ? 0 : getVideoDescription().hashCode());
        result = prime * result + ((getVideoType() == null) ? 0 : getVideoType().hashCode());
        result = prime * result + ((getVideoFavourNum() == null) ? 0 : getVideoFavourNum().hashCode());
        result = prime * result + ((getVideoShareNum() == null) ? 0 : getVideoShareNum().hashCode());
        result = prime * result + ((getVideoThumbNum() == null) ? 0 : getVideoThumbNum().hashCode());
        result = prime * result + ((getVideoPlayNum() == null) ? 0 : getVideoPlayNum().hashCode());
        result = prime * result + ((getVideoStatus() == null) ? 0 : getVideoStatus().hashCode());
        result = prime * result + ((getVideoRole() == null) ? 0 : getVideoRole().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", videoCoverUrl=").append(videoCoverUrl);
        sb.append(", videoUrl=").append(videoUrl);
        sb.append(", videoDescription=").append(videoDescription);
        sb.append(", videoType=").append(videoType);
        sb.append(", videoFavourNum=").append(videoFavourNum);
        sb.append(", videoShareNum=").append(videoShareNum);
        sb.append(", videoThumbNum=").append(videoThumbNum);
        sb.append(", videoPlayNum=").append(videoPlayNum);
        sb.append(", videoStatus=").append(videoStatus);
        sb.append(", videoRole=").append(videoRole);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}