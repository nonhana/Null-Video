package com.typeofNull.nullvideo;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.typeofNull.nullvideo.constant.VideoConstant.VIDEO_BASE_STORAGE;
import static com.typeofNull.nullvideo.constant.VideoConstant.VIDEO_TEMP_STORAGE;

@SpringBootTest
@Slf4j
class NullVideoApplicationTests {

    @Test
    void contextLoads() {
        String fileName="myTest.mp4";
        File file1 = new File(VIDEO_BASE_STORAGE + fileName);
        System.out.println(file1.exists());
        System.out.println("路径"+file1.getAbsolutePath());
    }

    @Test
    void testRandom(){
//        int[] ints = RandomUtil.randomInts(10);
//        for(int a:ints) System.out.println(a);
        for(int i=0;i<10;i++){

        long l = RandomUtil.randomLong(0l, 1l+1l);
        System.out.println(l);
        }
    }

    @Test
    void test1(){
        File file = new File(VIDEO_BASE_STORAGE + "result.ts");
        if(file.exists()){
        System.out.println(file.getAbsolutePath());
        }else{
            System.out.println("不存在");
        }
    }
    @Test
    void convertVideo(){
        String fileName="myTest.mp4";
        File file1 = new File(VIDEO_TEMP_STORAGE + fileName);
        System.out.println(file1.exists());
        File file = new File(VIDEO_TEMP_STORAGE, "6defe4bb-ff59-4a4a-aa8e-f02301e94137");
        if(!file.exists()){
            file.mkdirs();
        }
//        // 构建命令
        String ffmpegPath = "D://后端//ffmpeg/bin//ffmpeg";
        List<String> commandsBefore = new ArrayList<>();
        commandsBefore.add(ffmpegPath); // 设置 ffmpeg 的路径
        commandsBefore.add("-i");commandsBefore.add(VIDEO_TEMP_STORAGE+fileName);
        commandsBefore.add("-c");commandsBefore.add("copy");
        commandsBefore.add("-bsf:v");commandsBefore.add("h264_mp4toannexb");
        commandsBefore.add("-f");commandsBefore.add("mpegts");
        commandsBefore.add("-start_number");commandsBefore.add("0");
        commandsBefore.add(file.getAbsolutePath()+"\\result01.ts");
        ProcessBuilder processBuilder = new ProcessBuilder(commandsBefore);
        processBuilder.redirectErrorStream(true);


//        List<String> commands = new ArrayList<>();
//        commands.add(ffmpegPath); // 设置 ffmpeg 的路径
//        commands.add("-i")						;commands.add(VIDEO_BASE_STORAGE+fileName);
//        commands.add("-c:v")					;commands.add("libx264");				// 视频编码为H264
//        commands.add("-hls_time")				;commands.add("10");	// ts切片大小
//        commands.add("-hls_segment_filename")	;commands.add(file.getAbsolutePath()+"\\%06d.ts");				// ts切片文件名称
//        commands.add(file.getAbsolutePath()+"\\index.m3u8");
//        ProcessBuilder processBuilder = new ProcessBuilder(commands);
//        processBuileder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void convertVideo1(){
//        // 构建命令
        String ffmpegPath = "D://后端//ffmpeg/bin//ffmpeg";

        String path="D:\\后端\\test\\newTest\\6defe4bb-ff59-4a4a-aa8e-f02301e94137";

        List<String> commands = new ArrayList<>();
        commands.add(ffmpegPath); // 设置 ffmpeg 的路径
        commands.add("-i")						;commands.add(path+"\\result01.ts");
        commands.add("-c:v")					;commands.add("libx264");				// 视频编码为H264
        commands.add("-hls_time")				;commands.add("20");	// ts切片大小
        commands.add("-preset"); commands.add("ultrafast"); // 使用极快的编码预设
        commands.add("-hls_list_size")				;commands.add("0");	// ts切片大小
        commands.add("-g"); commands.add("48"); // 设置关键帧间隔为 48
        commands.add("-hls_segment_filename")	;commands.add(path+"\\%06d.ts");				// ts切片文件名称
        commands.add(path+"\\index01.m3u8");
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);
            Process process =processBuilder.start();
            //合在一起
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                    log.info(line);
            }
            // 读取进程标准输出
//        new Thread(() -> {
//            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line = null;
//                while ((line = bufferedReader.readLine()) != null) {
//                    log.info(line);
//                }
//            } catch (IOException e) {
//            }
//        }).start();
//        // 读取进程异常输出
//        new Thread(() -> {
//            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//                String line = null;
//                while ((line = bufferedReader.readLine()) != null) {
//                    log.error(line);
//                }
//            } catch (IOException e) {
//            }
//        }).start();

        process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
