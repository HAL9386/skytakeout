package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "C端地址簿接口")
@Slf4j
@RequestMapping("/user/addressBook")
@RestController
public class AddressBookController {

  private final AddressBookService addressBookService;

  public AddressBookController(AddressBookService addressBookService) {
    this.addressBookService = addressBookService;
  }

  /**
   * 查询当前登录用户的所有地址信息
   *
   * @return 地址簿列表
   */
  @ApiOperation("查询当前登录用户的所有地址信息")
  @GetMapping("/list")
  public Result<List<AddressBook>> list() {
    log.info("查询当前登录用户的所有地址信息");
    AddressBook addressBook = AddressBook.builder()
      .userId(BaseContext.getCurrentId())
      .build();
    List<AddressBook> list = addressBookService.list(addressBook);
    return Result.success(list);
  }

  /**
   * 新增地址
   *
   * @param addressBook 地址信息
   * @return 新增结果
   */
  @ApiOperation("新增地址")
  @PostMapping
  public Result<Object> save(@RequestBody AddressBook addressBook) {
    log.info("新增地址: {}", addressBook);
    addressBookService.save(addressBook);
    return Result.success();
  }

  /**
   * 根据id查询地址
   *
   * @param id 地址ID
   * @return 地址信息
   */
  @ApiOperation("根据id查询地址")
  @GetMapping("/{id}")
  public Result<AddressBook> getById(@PathVariable Long id) {
    log.info("根据id查询地址: {}", id);
    AddressBook addressBook = addressBookService.getById(id);
    return Result.success(addressBook);
  }

  /**
   * 根据id修改地址
   *
   * @param addressBook 地址信息
   * @return 修改结果
   */
  @ApiOperation("根据id修改地址")
  @PutMapping
  public Result<Object> update(@RequestBody AddressBook addressBook) {
    log.info("修改地址: {}", addressBook);
    addressBookService.update(addressBook);
    return Result.success();
  }

  /**
   * 设置默认地址
   *
   * @param addressBook 地址信息
   * @return 设置结果
   */
  @ApiOperation("设置默认地址")
  @PutMapping("/default")
  public Result<Object> setDefault(@RequestBody AddressBook addressBook) {
    log.info("设置默认地址: {}", addressBook);
    addressBookService.setDefault(addressBook);
    return Result.success();
  }

  /**
   * 根据id删除地址
   *
   * @param id 地址ID
   * @return 删除结果
   */
  @ApiOperation("根据id删除地址")
  @DeleteMapping
  public Result<Object> deleteById(Long id) {
    log.info("根据id删除地址: {}", id);
    addressBookService.deleteById(id);
    return Result.success();
  }

  /**
   * 查询默认地址
   */
  @ApiOperation("查询默认地址")
  @GetMapping("default")
  public Result<AddressBook> getDefault() {
    log.info("查询默认地址");
    AddressBook addressBook = AddressBook.builder()
      .userId(BaseContext.getCurrentId())
      .isDefault(1)
      .build();
    List<AddressBook> list = addressBookService.list(addressBook);
    if (list != null && !list.isEmpty()) {
      return Result.success(list.get(0));
    }
    return Result.error("没有查询到默认地址");
  }
}
