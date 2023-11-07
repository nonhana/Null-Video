package com.typeofNull.nullvideo.aop;

import cn.hutool.core.util.StrUtil;
import com.typeofNull.nullvideo.common.ErrorCode;
import com.typeofNull.nullvideo.exception.BusinessException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.typeofNull.nullvideo.constant.UserConstant.*;

/**
 * @author Andy
 * @data 2023/10/28
 * @Description 登入拦截器
 */

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }

        //token是否为空
        String token = request.getHeader(TOKEN);
        if(token==null|| StrUtil.isBlank(token)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String realToken=USER_LOGIN_STATE+":"+token;
        //用户是否在登入态
        String userJsonStr = stringRedisTemplate.opsForValue().get(realToken);
        if(StrUtil.isBlank(userJsonStr)){
            throw new BusinessException(ErrorCode.AUTHOR_ERROR);
        }
        //用户在登入状态刷新token
        stringRedisTemplate.expire(realToken,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return true;
    }

}
