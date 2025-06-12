package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
public class AutoFillAspect {
  @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
  public void autoFillPointcut() {}

  @Before("autoFillPointcut()")
  public void autoFill(JoinPoint joinPoint) {
    log.info("开始进行公共字段自动填充...");
    // 获取到当前被拦截的方法上的数据库操作类型
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
    OperationType operationType = null;
    if (autoFill != null) {
      operationType = autoFill.value();
    }
    // 获取到当前被拦截的方法参数--实体对象
    Object[] args = joinPoint.getArgs();
    if (args == null || args.length == 0) {
      return;
    }
    Object entity = args[0];
    // 准备赋值的数据
    LocalDateTime now = LocalDateTime.now();
    Long currentId = BaseContext.getCurrentId();
    // 根据当前不同的操作类型，为对应的属性通过反射来赋值
    if (operationType == OperationType.INSERT) {
      // 为4个公共字段赋值
      try {
        Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        // 为4个公共字段赋值
        setCreateTime.invoke(entity, now);
        setUpdateTime.invoke(entity, now);
        setCreateUser.invoke(entity, currentId);
        setUpdateUser.invoke(entity, currentId);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    } else if (operationType == OperationType.UPDATE) {
      // 为2个公共字段赋值
      try {
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        // 为2个公共字段赋值
        setUpdateTime.invoke(entity, now);
        setUpdateUser.invoke(entity, currentId);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }
}
