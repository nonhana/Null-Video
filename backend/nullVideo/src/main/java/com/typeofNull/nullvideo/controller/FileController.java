package com.typeofNull.nullvideo.controller;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.typeofNull.nullvideo.common.BaseResponse;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.common.ResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.typeofNull.nullvideo.common.ErrorCode.FILE_UPLOAD_ERROR;
import static com.typeofNull.nullvideo.constant.SystemConstant.BASE_URL;
import static com.typeofNull.nullvideo.constant.SystemConstant.VIDEO_MAX_SIZE;

/**
 * @author Andy
 * @data 2023/10/29
 * @Description
 */

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${Qiniu.AccessKey}")
    private String accessKey;

    @Value("${Qiniu.SecretKey}")
    private String secretKey;

    @Value("${Qiniu.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public BaseResponse<Map> fileUploda(@RequestParam MultipartFile multipartFile){
        if(multipartFile.getSize()>VIDEO_MAX_SIZE){
            throw new MultipartException("视频过大");
        }
        HashMap<String, String> map = new HashMap<>();
        Configuration configuration = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(configuration);
        String key = createNewFileName(multipartFile.getOriginalFilename());
        try{
            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            String uploadToken = auth.uploadToken(bucketName);
            //数据流
            byte[] bytes = multipartFile.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Response putRes = uploadManager.put(byteArrayInputStream, key, uploadToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(putRes.bodyString(), DefaultPutRet.class);
            String url=BASE_URL+putRet.key;
            map.put("url",url);
            map.put("code",0+"");
            map.put("message","上传成功");
            return ResultUtils.success(map);
        }catch (QiniuException e) {
            e.printStackTrace();
            return ResultUtils.error(FILE_UPLOAD_ERROR);
        }catch (IOException e) {
                throw new RuntimeException(e);
        }
    }


    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        return StrUtil.format("{}.{}",  name, suffix);
    }


}
