package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * 新增分类
   *
   * @param categoryDTO 分类信息
   */
  @PostMapping
  @ApiOperation("新增分类")
  public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
    log.info("新增分类：{}", categoryDTO);
    categoryService.save(categoryDTO);
    return Result.success();
  }

  /**
   * 分类分页查询
   *
   * @param categoryPageQueryDTO 分页查询条件
   * @return 分页数据
   */
  @GetMapping("/page")
  @ApiOperation("分类分页查询")
  public Result<PageResult<Category>> page(CategoryPageQueryDTO categoryPageQueryDTO) {
    log.info("分页查询：{}", categoryPageQueryDTO);
    PageResult<Category> pageResult = categoryService.pageQuery(categoryPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 删除分类
   *
   * @param id 分类id
   */
  @DeleteMapping
  @ApiOperation("删除分类")
  public Result<String> deleteById(Long id) {
    log.info("删除分类：{}", id);
    categoryService.deleteById(id);
    return Result.success();
  }

  /**
   * 修改分类
   *
   * @param categoryDTO 分类信息
   */
  @PutMapping
  @ApiOperation("修改分类")
  public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
    log.info("修改分类：{}", categoryDTO);
    categoryService.update(categoryDTO);
    return Result.success();
  }

  /**
   * 启用、禁用分类
   *
   * @param status 要设置的状态 0：禁用 1：启用
   * @param id     分类id
   */
  @PostMapping("/status/{status}")
  @ApiOperation("启用禁用分类")
  public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
    log.info("启用禁用分类：{}, id: {}", status, id);
    categoryService.startOrStop(status, id);
    return Result.success();
  }

  /**
   * 根据类型查询分类
   *
   * @param type 1：菜品分类 2：套餐分类
   * @return 分类列表
   */
  @GetMapping("/list")
  @ApiOperation("根据类型查询分类")
  public Result<List<Category>> list(Integer type) {
    log.info("根据类型查询分类：{}", type);
    List<Category> list = categoryService.list(type);
    return Result.success(list);
  }
}
