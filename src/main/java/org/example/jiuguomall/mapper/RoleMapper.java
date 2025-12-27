package org.example.jiuguomall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jiuguomall.entity.Role;

@Mapper
public interface RoleMapper {

    Role selectByRoleCode(@Param("roleCode") String roleCode);

}
