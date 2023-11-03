package com.typeofNull.nullvideo.controller;

import cn.hutool.core.util.StrUtil;
import com.typeofNull.nullvideo.common.BaseResponse;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.common.ResultUtils;
import com.typeofNull.nullvideo.model.dto.admin.AdminUpdateVideoStatusRequest;
import com.typeofNull.nullvideo.model.vo.admin.AdminAuditVideoVO;
import com.typeofNull.nullvideo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Andy
 * @data 2023/10/31
 * @Description
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 用于管理员审核视频界面
     * @param userId
     * @param begin
     * @return
     */
    @GetMapping("/get/video")
    public BaseResponse<List<AdminAuditVideoVO>> getAuditVideo(String userId, @RequestParam(defaultValue = "0") Integer begin){
        if(StrUtil.isBlank(userId)){
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }
        List<AdminAuditVideoVO> adminVideoVO = userService.getAdminVideoVO(Long.parseLong(userId),begin);
        return ResultUtils.success(adminVideoVO);
    }

    /**
     * 管理员审核通过
     * @param adminUpdateVideoStatusRequest
     * @return
     */
    @PostMapping("/update/video/status")
    public BaseResponse<Boolean> adminUpdateVideoStatus(@RequestBody AdminUpdateVideoStatusRequest adminUpdateVideoStatusRequest){
        if(adminUpdateVideoStatusRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String videoIdStr = adminUpdateVideoStatusRequest.getVideoId();
        String userIdStr = adminUpdateVideoStatusRequest.getUserId();
        Integer videoStatus = adminUpdateVideoStatusRequest.getVideoStatus();
        if(StrUtil.hasBlank(videoIdStr,userIdStr)|| (videoStatus<=0&&videoStatus>=3)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean isSuccess = userService.updateVideoStatus(Long.parseLong(userIdStr), Long.parseLong(videoIdStr),videoStatus);
        return ResultUtils.success(isSuccess);
    }
}
