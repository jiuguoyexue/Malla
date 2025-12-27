package org.example.jiuguomall.controller;

import org.example.jiuguomall.entity.User;
import org.example.jiuguomall.dto.UserDTO;
import org.example.jiuguomall.service.UserService;
import org.example.jiuguomall.vo.Result;
import org.example.jiuguomall.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;


/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserDTO userDTO) {
        //测试参数在josn格式的body
        System.out.println("用户注册: " + userDTO.getUsername());
        try {
            User user = userService.register(userDTO);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password) {

        return Result.success(
                userService.loginWithToken(username, password)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/test")
    public String adminTest() {
        return "ADMIN OK";
    }
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(
            @PathVariable @Min(value = 1, message = "用户ID必须大于0") Long id) {
        System.out.println("查询用户ID: " + id);
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        System.out.println("获取所有用户");
        try {
            List<User> users = userService.getAllUsers();
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<PageResult<User>> getUsersByPage(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "每页大小必须大于0") Integer size) {
        System.out.println("分页查询用户, page=" + page + ", size=" + size);
        try {
            PageResult<User> pageResult = userService.getUsersByPage(page, size);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(
            @PathVariable @Min(value = 1, message = "用户ID必须大于0") Long id,
            @Valid @RequestBody UserDTO userDTO) {
        System.out.println("更新用户ID: " + id);
        try {
            User user = userService.updateUser(id, userDTO);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(
            @PathVariable @Min(value = 1, message = "用户ID必须大于0") Long id) {
        System.out.println("删除用户ID: " + id);
        try {
            userService.deleteUser(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public Result<User> changeStatus(
            @PathVariable @Min(value = 1, message = "用户ID必须大于0") Long id,
            @RequestParam Integer status) {
        System.out.println("修改用户状态 ID: " + id + ", status: " + status);
        try {
            // 这里需要先在UserService中实现changeStatus方法
            User user = userService.changeStatus(id, status);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username")
    public Result<Boolean> checkUsernameExists(
            @RequestParam @NotBlank(message = "用户名不能为空") String username) {
        System.out.println("检查用户名是否存在: " + username);
        try {
            boolean exists = userService.checkUsernameExists(username);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email")
    public Result<Boolean> checkEmailExists(
            @RequestParam @NotBlank(message = "邮箱不能为空") String email) {
        System.out.println("检查邮箱是否存在: " + email);
        try {
            // 这里需要先在UserService中实现checkEmailExists方法
            boolean exists = userService.checkEmailExists(email);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 全局异常处理（可选）
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        System.err.println("Controller异常: " + e.getMessage());
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
