package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "店铺相关接口")
@Slf4j
@RequestMapping("/user/shop")
@RestController("userShopController")
public class ShopController {
  private static final String STATUS_KEY = "SHOP_STATUS";

  private final RedisTemplate<String, Object> redisTemplate;

  public ShopController(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 获取店铺状态
   *
   * @return 店铺状态
   */
  @ApiOperation("查询店铺状态")
  @GetMapping("/status")
  public Result<Integer> getStatus() {
    Integer status = (Integer) redisTemplate.opsForValue().get(STATUS_KEY);
    if (status == null) {
      status = 0;
    }
    log.info("用户端查询店铺状态：{}", status == 1 ? "营业中" : "已打烊");
    return Result.success(status);
  }
}
