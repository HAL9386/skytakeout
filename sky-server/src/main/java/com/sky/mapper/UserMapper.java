package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  /**
   * 根据openid查询用户
   *
   * @param openid 微信用户的唯一标识
   * @return 用户信息
   */
  @Select("select * from user where openid = #{openid}")
  User getByOpenId(String openid);

  /**
   * 插入新用户
   *
   * @param user 用户信息
   */
  void insert(User user);

  /**
   * 根据id查询用户
   *
   * @param userId 用户id
   * @return 用户信息
   */
  @Select("select * from user where id = #{userId}")
  User getById(Long userId);
}
