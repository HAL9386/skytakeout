package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义Oss配置类
 */
@Slf4j
@Configuration
public class OssConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
    log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
    return AliOssUtil.builder()
      .endpoint(aliOssProperties.getEndpoint())
      .accessKeyId(aliOssProperties.getAccessKeyId())
      .accessKeySecret(aliOssProperties.getAccessKeySecret())
      .bucketName(aliOssProperties.getBucketName())
      .build();
  }
}
