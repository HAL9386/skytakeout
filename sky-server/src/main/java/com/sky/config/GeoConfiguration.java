package com.sky.config;

import com.sky.properties.ShopProperties;
import com.sky.utils.BaiduGeoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GeoConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public BaiduGeoUtil baiduGeoUtil(ShopProperties shopProperties) {
    log.info("开始创建百度地图工具类对象...");
    return BaiduGeoUtil.builder()
      .AK(shopProperties.getBaiduAk())
      .shopAddress(shopProperties.getAddress())
      .shopLat(shopProperties.getLatitude())
      .shopLng(shopProperties.getLongitude())
      .build();
  }
}
