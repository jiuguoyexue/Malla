package org.example.jiuguomall.service.impl;

import org.example.jiuguomall.dto.UserDTO;
import org.example.jiuguomall.entity.Role;
import org.example.jiuguomall.entity.User;
import org.example.jiuguomall.exception.BusinessException;
import org.example.jiuguomall.mapper.RoleMapper;
import org.example.jiuguomall.mapper.UserMapper;
import org.example.jiuguomall.mapper.UserRoleMapper;
import org.example.jiuguomall.service.UserService;
import org.example.jiuguomall.util.JwtUtil;
import org.example.jiuguomall.vo.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(UserDTO userDTO) {

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setStatus(User.STATUS_ENABLED);
        user.setDeleted(User.DELETED_NO);

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            // 精确捕获唯一索引异常
            throw new BusinessException("用户名已存在");
        }

        // 绑定 BUYER 角色
        Role buyerRole = roleMapper.selectByRoleCode("BUYER");
        userRoleMapper.insert(user.getId(), buyerRole.getId());

        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (!User.STATUS_ENABLED.equals(user.getStatus())) {
            throw new RuntimeException("用户已被禁用");
        }

        return user;
    }

    /**
     * JWT 登录
     */
    @Override
    public Map<String, Object> loginWithToken(String username, String password) {
        // 1. 调用 login 校验用户名密码
        User user = login(username, password);

        // 2. 获取用户角色列表
        List<String> roles = getUserRoles(user.getId());

        // 3. 生成 JWT token
        String token = JwtUtil.generateToken(user.getUsername(), roles);

        // 4. 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        result.put("roles", roles);

        return result;
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
        List<User> users = userMapper.selectByPage(offset, size);
        int total = userMapper.count();
        return new PageResult<>(users, (long) total, page, size);
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) throw new RuntimeException("用户不存在");

        BeanUtils.copyProperties(userDTO, user);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userMapper.update(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        if (result == 0) throw new RuntimeException("删除失败，用户不存在");
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    @Override
    public User changeStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) throw new RuntimeException("用户不存在");
        if (!status.equals(User.STATUS_ENABLED) && !status.equals(User.STATUS_DISABLED)) {
            throw new RuntimeException("状态无效");
        }
        if (status.equals(user.getStatus())) return user;
        user.setStatus(status);
        userMapper.update(user);
        return user;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (email == null || email.isEmpty()) return false;
        List<User> users = userMapper.selectAll();
        return users.stream().anyMatch(u -> email.equals(u.getEmail()));
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        return userRoleMapper.selectRoleCodesByUserId(userId);
    }
}
