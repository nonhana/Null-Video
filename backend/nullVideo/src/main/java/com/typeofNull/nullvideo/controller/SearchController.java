package com.typeofNull.nullvideo.controller;

import cn.hutool.core.util.StrUtil;
import com.typeofNull.nullvideo.common.BaseResponse;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.common.ResultUtils;
import com.typeofNull.nullvideo.model.vo.search.SearchVideoVO;
import com.typeofNull.nullvideo.service.SearchService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Andy
 * @data 2023/11/1
 * @Description
 */
@RequestMapping("/search")
@RestController
public class SearchController {

    @Resource
    private SearchService searchService;
    @GetMapping("/all")
    public BaseResponse<?extends Object> ReversoSearch(String searchText, Integer option){
        if(option==null||option<0||option>=3){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Object searchVO = searchService.searchAll(searchText, option);
        return ResultUtils.success(searchVO);
    }

    /**
     * 查看特定类型的Video
     * @param videoTypeId
     * @return
     */
    @GetMapping("/video/type")
    public BaseResponse<List<SearchVideoVO>> searchSpecialVideo(String videoTypeId){
        if(StrUtil.isBlank(videoTypeId)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long videoTypeReal = Long.parseLong(videoTypeId);
        List<SearchVideoVO> searchVideoVOS = searchService.searchSpecialVideo(videoTypeReal);
        return ResultUtils.success(searchVideoVOS);
    }


}
