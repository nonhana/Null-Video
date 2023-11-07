package com.typeofNull.nullvideo.config;

import com.typeofNull.nullvideo.aop.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Andy
 * @data 2023/10/28
 * @Description
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .excludePathPatterns("/user/register","/user/login",
                        "/video/get/page","video/get","/video/get/comment",
                        "/search/video/type","search/all","/video/get/random",
                        "user/get/userInfo").order(0);
    }
}
