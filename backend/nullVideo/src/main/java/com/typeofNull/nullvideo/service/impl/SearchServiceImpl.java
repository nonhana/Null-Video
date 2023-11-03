package com.typeofNull.nullvideo.service.impl;

import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.exception.BusinessException;
import com.typeofNull.nullvideo.model.vo.search.SearchUserAndVideoVO;
import com.typeofNull.nullvideo.model.vo.search.SearchUserVO;
import com.typeofNull.nullvideo.model.vo.search.SearchVO;
import com.typeofNull.nullvideo.model.vo.search.SearchVideoVO;
import com.typeofNull.nullvideo.model.vo.user.UserFollowVO;
import com.typeofNull.nullvideo.service.SearchService;
import com.typeofNull.nullvideo.service.UserService;
import com.typeofNull.nullvideo.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Andy
 * @data 2023/11/2
 * @Description
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    private VideoService videoService;

    @Resource
    private UserService userService;
    @Override
    public List<SearchVideoVO> searchSpecialVideo(Long videoTypeId) {
        List<SearchVideoVO> typeVideo = videoService.getTypeVideo(videoTypeId);
        return typeVideo;
    }

    @Override
    public Object searchAll(String searchText, Integer option) {
        switch (option){
            case 0 :{ //查询综合
                SearchUserAndVideoVO searchUserAndVideoVO = videoService.searchAll(searchText);
                return searchUserAndVideoVO;
            }
            case 1:{ //查询视频
                List<SearchVideoVO> searchVideoVOS = videoService.searchVideo(searchText);
                return searchVideoVOS;
            }
            case 2:{ //查询用户
                List<SearchUserVO> searchUserVOS = userService.searchUser(searchText);
                return searchUserVOS;
            }
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }


}
