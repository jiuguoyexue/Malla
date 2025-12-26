package org.example.jiuguomall.mapper;

import org.example.jiuguomall.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    // 插入用户
    int insert(User user);

    // 根据ID查询用户（包含已删除的）
    User selectById(@Param("id") Long id);

    // 根据ID查询用户（排除已删除的）
    User selectByIdAndNotDeleted(@Param("id") Long id);

    // 根据用户名查询
    User selectByUsername(@Param("username") String username);

    // 查询所有用户（排除已删除的）
    List<User> selectAll();

    // 分页查询用户
    List<User> selectByPage(@Param("offset") int offset,
                            @Param("pageSize") int pageSize);

    // 更新用户信息
    int update(User user);

    // 逻辑删除用户
    int deleteById(@Param("id") Long id);

    // 物理删除用户（实际项目中慎用）
    int deletePhysically(@Param("id") Long id);

    // 统计用户数量
    int count();

    // 根据条件查询用户
    List<User> selectByCondition(@Param("username") String username,
                                 @Param("email") String email,
                                 @Param("status") Integer status);
}
