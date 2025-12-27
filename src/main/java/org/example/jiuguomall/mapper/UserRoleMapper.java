package org.example.jiuguomall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    int insert(@Param("userId") Long userId,
               @Param("roleId") Long roleId);

    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
}
