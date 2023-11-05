package com.typeofNull.nullvideo.util;

import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.typeofNull.nullvideo.constant.SystemConstant.FFMPEG_COMMAND;

/**
 * @author Andy
 * @data 2023/11/4
 * @Description
 */
@Slf4j
public class VideoUtil {


    /**
     * 将mp4转化为.ts文件
     * @param basePath  文件夹目录
     * @param key 文件名
     */
    public static void convertVideoToTs(String basePath,String key){
        File sourceFile = new File(basePath,key);
        if(!sourceFile.exists()){ //如果没有文件就报异常
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作异常请重试");
        }
        File destFile = new File(basePath);
        if(!destFile.exists()){
            destFile.mkdirs();
        }
        // 构建命令
        List<String> commandsBefore = new ArrayList<>();
        commandsBefore.add(FFMPEG_COMMAND); // 设置 ffmpeg 的路径
        commandsBefore.add("-i");               commandsBefore.add(basePath+File.separator+key);
        commandsBefore.add("-c");               commandsBefore.add("copy");
        commandsBefore.add("-bsf:v");           commandsBefore.add("h264_mp4toannexb");
        commandsBefore.add("-f");               commandsBefore.add("mpegts");
        commandsBefore.add("-start_number");    commandsBefore.add("0");
        commandsBefore.add(basePath+File.separator+"index.ts");
        ProcessBuilder processBuilder = new ProcessBuilder(commandsBefore);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            log.info("--------开始转化mp4--------");
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("--------转化mp4失败--------");
        } catch (InterruptedException e) {
            log.info("--------转化mp4失败--------");
            throw new RuntimeException(e);
        }
    }


    /**
     * 给.ts分片生成m3u8
     * @param basePath
     */
    public static void sliceVideo(String basePath){
        List<String> commands = new ArrayList<>();
        commands.add(FFMPEG_COMMAND); // 设置 ffmpeg 的路径
        commands.add("-i")						;commands.add(basePath+File.separator+"index.ts");
        commands.add("-c:v")					;commands.add("libx264");				// 视频编码为H264
        commands.add("-hls_time")				;commands.add("20");	// ts切片大小
        commands.add("-hls_list_size")			;commands.add("0");
        commands.add("-preset")                 ;commands.add("ultrafast"); // 使用极快的编码预设
        commands.add("-g")                      ;commands.add("48"); // 设置关键帧间隔为 48
        commands.add("-hls_segment_filename")	;commands.add(basePath+File.separator+"%06d.ts");				// ts切片文件名称
        commands.add(basePath+File.separator+"index.m3u8");
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        try {
            Process process = processBuilder.start();
            log.info("--------开始分片--------");
            //  读取进程标准输出
            new Thread(() -> {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        log.info(line);
                    }
                } catch (IOException e) {
                }
            }).start();
            // 读取进程异常输出
            new Thread(() -> {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        log.error(line);
                    }
                } catch (IOException e) {
                }
            }).start();
            process.waitFor();
        } catch (IOException e) {
            log.info("--------分片失败--------");
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.info("--------分片失败--------");
            throw new RuntimeException(e);
        }
    }

    /**
     * 用于删除Temp文件夹
     * @param filePath
     */
    public static void deleteTemp(File filePath){
        File[] files = filePath.listFiles();
        for(File file:files){
            if(file.isFile()){
                file.delete();
            }else if(file.isDirectory()){
                deleteTemp(file);
                file.delete();
            }
        }
        filePath.delete();
    }
}

