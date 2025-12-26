package org.example.jiuguomall.service;

import org.example.jiuguomall.entity.User;
import org.example.jiuguomall.dto.UserDTO;
import org.example.jiuguomall.vo.PageResult;

import java.util.List;

public interface UserService {
    // 现有方法...
    User register(UserDTO userDTO);
    User login(String username, String password);
    User getUserById(Long id);
    List<User> getAllUsers();
    PageResult<User> getUsersByPage(int page, int size);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    boolean checkUsernameExists(String username);

    // 添加缺失的方法
    User changeStatus(Long id, Integer status);

    // 可选的额外方法
    boolean checkEmailExists(String email);
}
