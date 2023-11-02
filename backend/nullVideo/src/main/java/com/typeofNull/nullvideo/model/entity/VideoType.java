package com.typeofNull.nullvideo.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 视频类目表
 * @TableName video_type
 */
@TableName(value ="video_type")
@Data
public class VideoType implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String videoTypeName;

    /**
     * 
     */
    private String videoTopic;

    /**
     * 
     */
    private String videoTopicDescription;

    /**
     * 表示是否是视频种类大类 0-是 1-不是
     */
    private Integer isParent;


    private Long parentId;

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
        VideoType other = (VideoType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getVideoTypeName() == null ? other.getVideoTypeName() == null : this.getVideoTypeName().equals(other.getVideoTypeName()))
            && (this.getVideoTopic() == null ? other.getVideoTopic() == null : this.getVideoTopic().equals(other.getVideoTopic()))
            && (this.getVideoTopicDescription() == null ? other.getVideoTopicDescription() == null : this.getVideoTopicDescription().equals(other.getVideoTopicDescription()))
            && (this.getIsParent() == null ? other.getIsParent() == null : this.getIsParent().equals(other.getIsParent()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getVideoTypeName() == null) ? 0 : getVideoTypeName().hashCode());
        result = prime * result + ((getVideoTopic() == null) ? 0 : getVideoTopic().hashCode());
        result = prime * result + ((getVideoTopicDescription() == null) ? 0 : getVideoTopicDescription().hashCode());
        result = prime * result + ((getIsParent() == null) ? 0 : getIsParent().hashCode());
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
        sb.append(", videoTypeName=").append(videoTypeName);
        sb.append(", videoTopic=").append(videoTopic);
        sb.append(", videoTopicDescription=").append(videoTopicDescription);
        sb.append(", isParent=").append(isParent);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}