package com.sky.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class AutoFillAspect {
  @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
  public void autoFillPointcut() {}

  @Before("autoFillPointcut()")
  public void autoFill() {
    log.info("开始进行公共字段自动填充...");
  }
}
