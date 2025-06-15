package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api(tags = "店铺相关接口")
@Slf4j
@RequestMapping("/admin/shop")
@RestController("adminShopController")
public class ShopController {
  private static final String STATUS_KEY = "SHOP_STATUS";

  private final RedisTemplate<String, Object> redisTemplate;

  public ShopController(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 设置店铺状态
   *
   * @param status 店铺状态 1:营业中 0:打烊中
   * @return 操作结果
   */
  @ApiOperation("设置店铺状态")
  @PutMapping("/{status}")
  public Result<Object> setStatus(@PathVariable Integer status) {
    log.info("设置店铺状态：{}", status == 1 ? "营业中" : "已打烊");
    redisTemplate.opsForValue().set(STATUS_KEY, status);
    return Result.success();
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
    log.info("管理端查询店铺状态：{}", status == 1 ? "营业中" : "已打烊");
    return Result.success(status);
  }
}
