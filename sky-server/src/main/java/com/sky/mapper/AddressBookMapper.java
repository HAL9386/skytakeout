package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

  /**
   * 条件查询
   *
   * @param addressBook 地址簿查询条件
   * @return 地址簿列表
   */
  List<AddressBook> list(AddressBook addressBook);

  /**
   * 新增
   *
   * @param addressBook 地址信息
   */
  @Insert("insert into address_book" +
    "        (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code," +
    "         district_name, detail, label, is_default)" +
    "        values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
    "                #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
  void insert(AddressBook addressBook);

  /**
   * 根据id查询
   *
   * @param id 地址ID
   * @return 地址信息
   */
  @Select("select * from address_book where id = #{id}")
  AddressBook getById(Long id);

  /**
   * 根据id修改
   *
   * @param addressBook 地址信息
   */
  void update(AddressBook addressBook);

  /**
   * 根据 用户id修改 是否默认地址
   *
   * @param addressBook 地址信息
   */
  @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
  void updateIsDefaultByUserId(AddressBook addressBook);

  /**
   * 根据id删除地址
   *
   * @param id 地址ID
   */
  @Delete("delete from address_book where id = #{id}")
  void deleteById(Long id);
}
