package org.example.jiuguomall.service;

import org.example.jiuguomall.entity.User;
import org.example.jiuguomall.dto.UserDTO;
import org.example.jiuguomall.vo.PageResult;
import java.util.Map;

import java.util.List;

public interface UserService {
    // 现有方法...
    User register(UserDTO userDTO);

    User login(String username, String password);

    //旧的login，真实项目里不会出现无token登录，login（）一定会被废弃
    // User login(String username, String password);
    Map<String, Object> loginWithToken(String username, String password);

    User getUserById(Long id);
    List<User> getAllUsers();
    PageResult<User> getUsersByPage(int page, int size);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    boolean checkUsernameExists(String username);
    List<String> getUserRoles(Long userId);



    // 添加缺失的方法
    User changeStatus(Long id, Integer status);

    // 可选的额外方法
    boolean checkEmailExists(String email);
}
