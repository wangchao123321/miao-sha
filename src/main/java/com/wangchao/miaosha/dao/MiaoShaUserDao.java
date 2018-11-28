package com.wangchao.miaosha.dao;

import com.wangchao.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoShaUserDao {

    @Select("select * from user where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);
}
