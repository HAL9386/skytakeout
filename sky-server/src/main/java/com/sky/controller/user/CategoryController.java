package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端分类相关接口")
@Slf4j
@RequestMapping("/user/category")
@RestController("userCategoryController")
public class CategoryController {
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * 根据类型查询分类
   *
   * @param type 分类类型 1:菜品分类 2:套餐分类
   * @return 分类列表
   */
  @ApiOperation("根据类型查询分类")
  @GetMapping("/list")
  public Result<List<Category>> list(Integer type) {
//    log.info("C端查询分类：{}", type == 1 ? "菜品" : "套餐");
    log.info("C端查询分类：{}", type);
    List<Category> list = categoryService.list(type);
    return Result.success(list);
  }
}
