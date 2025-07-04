package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "C端购物车相关接口")
@Slf4j
@RequestMapping("/user/shoppingCart")
@RestController("userShoppingCartController")
public class ShoppingCartController {
  private final ShoppingCartService shoppingCartService;

  public ShoppingCartController(ShoppingCartService shoppingCartService) {
    this.shoppingCartService = shoppingCartService;
  }

  /**
   * 添加到购物车
   *
   * @param shoppingCartDTO 购物车信息传输信息
   * @return Result
   */
  @ApiOperation("添加到购物车")
  @PostMapping("/add")
  public Result<Object> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
    log.info("添加到购物车：{}", shoppingCartDTO);
    shoppingCartService.add(shoppingCartDTO);
    return Result.success();
  }
}
