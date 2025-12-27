
-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS mall_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 2. 切换数据库
USE mall_db;
-- 3. 创建基础表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-删除' )
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


#权限相关（JWT 必备）

CREATE TABLE IF NOT EXISTS role (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    role_code VARCHAR(50) UNIQUE COMMENT 'ADMIN BUYER',
    role_name VARCHAR(50) COMMENT '角色名称'
    ) ENGINE=InnoDB COMMENT='角色表';

#用户角色关联表
CREATE TABLE IF NOT EXISTS user_role (
                                         user_id BIGINT NOT NULL,
                                         role_id BIGINT NOT NULL,
                                         PRIMARY KEY (user_id, role_id)
    ) ENGINE=InnoDB COMMENT='用户角色关联表';



#插入合适数据
#暂时只有管理员和买家
#后期再加商家吧
INSERT INTO role (role_code, role_name) VALUES
                                            ('ADMIN', '管理员'),
                                            ('BUYER', '普通用户');

#admin 分配 ADMIN + BUYER（推荐）
INSERT INTO user_role (user_id, role_id) VALUES
                                             (1, 1), -- ADMIN
                                             (1, 2); -- BUYER