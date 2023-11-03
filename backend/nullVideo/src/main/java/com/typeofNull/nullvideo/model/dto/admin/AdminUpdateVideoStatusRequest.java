package com.typeofNull.nullvideo.model.dto.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andy
 * @data 2023/10/31
 * @Description
 */
@Data
public class AdminUpdateVideoStatusRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String videoId;

    /**
     * 1-审核通过 2-不通过
     */
    private Integer videoStatus;
}
