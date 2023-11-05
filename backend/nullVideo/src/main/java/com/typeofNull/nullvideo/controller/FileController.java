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
import com.typeofNull.nullvideo.common.ResultUtils;
import com.typeofNull.nullvideo.util.VideoUtil;
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
import static com.typeofNull.nullvideo.constant.VideoConstant.VIDEO_BASE_STORAGE;

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

        String contentType = multipartFile.getContentType();
        String key = createNewFileName(contentType); //key作为
        String baseDir = UUID.randomUUID().toString();
        String basePath=VIDEO_BASE_STORAGE+baseDir;
        File fileDir = new File(basePath);
        //判断是不是视频
        try{
            //表示最后返回的m3u8或者照片的url
            String url="";
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            multipartFile.transferTo(new File(basePath,key));
            if(contentType.endsWith("mp4")){  //表示传的是视频
             //转换为ts
             VideoUtil.convertVideoToTs(basePath,key);
             //分片
             VideoUtil.sliceVideo(basePath);
            }
            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            String uploadToken = auth.uploadToken(bucketName);

            //批量传
            File[] files = fileDir.listFiles();
            String uploadFileDir="image/";
            for(File file:files){
                String fileName = file.getName();
                if(fileName.endsWith("mp4")){ //mp4不传
                    continue;
                }
                if(fileName.endsWith("ts")||fileName.endsWith("m3u8")){
                    uploadFileDir="video/"+baseDir+"/";
                }
                //数据流
//                byte[] bytes = file.get;
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                Response putRes = uploadManager.put(file, uploadFileDir+fileName, uploadToken);
                DefaultPutRet putRet = new Gson().fromJson(putRes.bodyString(), DefaultPutRet.class);
                if(!fileName.endsWith("ts")){
                    url=BASE_URL+putRet.key;
                }
            }
                map.put("url",url);
                map.put("code",0+"");
                map.put("message","上传成功");
                //传完之后就把原来的给删了
                VideoUtil.deleteTemp(fileDir);
                return ResultUtils.success(map);
        }catch (QiniuException e) {
            e.printStackTrace();
            return ResultUtils.error(FILE_UPLOAD_ERROR);
        }catch (IOException e) {
                throw new RuntimeException(e);
        }
    }


    private String createNewFileName(String fileType) {
        String suffix = fileType.substring(fileType.lastIndexOf("/")+1);
        // 生成目录
        String name = UUID.randomUUID().toString();
        return StrUtil.format("{}.{}",  name, suffix);
    }


}
