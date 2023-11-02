package com.typeofNull.nullvideo.model.vo.search;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andy
 * @data 2023/11/1
 * @Description
 */
@Data
public class SearchUserAndVideoVO extends SearchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户部分
    private SearchUserVO searchUser;

    //视频部分
    private List<SearchVideoVO> searchVideo;



}
