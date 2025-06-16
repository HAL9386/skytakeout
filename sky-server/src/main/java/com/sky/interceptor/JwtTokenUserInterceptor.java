package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtTokenUserInterceptor implements HandlerInterceptor {
  private final JwtProperties jwtProperties;

  public JwtTokenUserInterceptor(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  public boolean preHandle(@Nonnull HttpServletRequest request,
                           @Nonnull HttpServletResponse response,
                           @Nullable Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    String token = request.getHeader(jwtProperties.getUserTokenName());
    try {
      log.info("微信小程序端jwt校验: {}", token);
      Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
      Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
      log.info("当前微信端用户id: {}", userId);
      BaseContext.setCurrentId(userId);
      return true;
    } catch (Exception ex) {
      log.error("jwt校验失败: {}", token);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
  }

  public void afterCompletion(@Nonnull HttpServletRequest request,
                              @Nonnull HttpServletResponse response,
                              @Nullable Object handler,
                              @Nullable Exception ex) {
    BaseContext.removeCurrentId();
    log.info("已清理线程中的用户id, threadId: {}", Thread.currentThread().getId());
  }
}
