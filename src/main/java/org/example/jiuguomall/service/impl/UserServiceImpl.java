package org.example.jiuguomall.service.impl;

import org.example.jiuguomall.entity.User;
import org.example.jiuguomall.dto.UserDTO;
import org.example.jiuguomall.mapper.UserMapper;
import org.example.jiuguomall.service.UserService;
import org.example.jiuguomall.vo.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 现有方法保持不变...
    @Override
    public User register(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (checkUsernameExists(userDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户对象
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 密码加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                userDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptedPassword);

        // 设置默认状态
        user.setStatus(User.STATUS_ENABLED);
        user.setDeleted(User.DELETED_NO);

        // 插入数据库
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                password.getBytes(StandardCharsets.UTF_8));
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查状态
        if (!User.STATUS_ENABLED.equals(user.getStatus())) {
            throw new RuntimeException("用户已被禁用");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public PageResult<User> getUsersByPage(int page, int size) {
        int offset = (page - 1) * size;
        List<User> userList = userMapper.selectByPage(offset, size);
        int total = userMapper.count();

        return new PageResult<>(userList, (long) total, page, size);
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        BeanUtils.copyProperties(userDTO, user);

        // 如果有新密码，加密
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String encryptedPassword = DigestUtils.md5DigestAsHex(
                    userDTO.getPassword().getBytes(StandardCharsets.UTF_8));
            user.setPassword(encryptedPassword);
        }

        userMapper.update(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("删除失败，用户不存在");
        }
    }

    @Override
    public boolean checkUsernameExists(String username) {
        User user = userMapper.selectByUsername(username);
        return user != null;
    }

    // ================ 实现新增的方法 ================

    @Override
    public User changeStatus(Long id, Integer status) {
        // 1. 验证用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 验证状态值是否有效
        if (!isValidStatus(status)) {
            throw new RuntimeException("无效的状态值: " + status + "，有效值: 0(禁用), 1(启用)");
        }

        // 3. 如果状态相同，直接返回
        if (status.equals(user.getStatus())) {
            System.out.println("用户状态未改变，当前状态已经是: " + status);
            return user;
        }

        // 4. 更新状态
        user.setStatus(status);
        userMapper.update(user);

        System.out.println("用户状态更新成功，ID: " + id + "，新状态: " +
                (User.STATUS_ENABLED.equals(status) ? "启用" : "禁用"));

        return user;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // 这里需要先在UserMapper中添加根据邮箱查询的方法
        // 临时实现：查询所有用户检查邮箱
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            if (email.equals(user.getEmail())) {
                return true;
            }
        }
        return false;

        // 更好的实现：在UserMapper中添加方法
        // return userMapper.selectByEmail(email) != null;
    }

    // ================ 私有辅助方法 ================

    /**
     * 验证状态值是否有效
     */
    private boolean isValidStatus(Integer status) {
        return User.STATUS_ENABLED.equals(status) ||
                User.STATUS_DISABLED.equals(status);
    }

    /**
     * 验证邮箱格式（可选）
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // 简单的邮箱格式验证
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * 验证手机号格式（可选）
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // 手机号可以为空
        }
        // 简单的手机号格式验证
        return phone.matches("^1[3-9]\\d{9}$");
    }
}
