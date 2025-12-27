
use mall_db;

#商品分类表（Category）
CREATE TABLE category (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
                          parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
                          name VARCHAR(50) NOT NULL COMMENT '分类名称',
                          level INT COMMENT '层级：1/2/3',
                          sort INT DEFAULT 0 COMMENT '排序',
                          status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='商品分类表';

#商品主表（SPU）—— product
#这是你“商品信息”的唯一入口
CREATE TABLE product (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SPU ID',
                         name VARCHAR(100) NOT NULL COMMENT '商品名称',
                         category_id BIGINT NOT NULL COMMENT '分类ID',
                         brand_id BIGINT COMMENT '品牌ID（可为空）',
                         description TEXT COMMENT '商品详情',
                         status TINYINT DEFAULT 1 COMMENT '状态：0下架 1上架',
                         audit_status TINYINT DEFAULT 1 COMMENT '审核状态：0待审 1通过 2拒绝',
                         sale_count INT DEFAULT 0 COMMENT '销量',
                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                         update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB COMMENT='商品SPU表';

#SKU 表（真正用于交易）
CREATE TABLE product_sku (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SKU ID',
                             product_id BIGINT NOT NULL COMMENT 'SPU ID',
                             sku_name VARCHAR(100) COMMENT 'SKU名称（如：红色 M）',
                             price DECIMAL(10,2) NOT NULL COMMENT '售价',
                             stock INT NOT NULL DEFAULT 0 COMMENT '库存',
                             status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
                             create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                             update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='商品SKU表';

#商品图片表
CREATE TABLE product_image (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               product_id BIGINT NOT NULL,
                               url VARCHAR(255) NOT NULL COMMENT '图片地址',
                               is_main TINYINT DEFAULT 0 COMMENT '是否主图',
                               sort INT DEFAULT 0 COMMENT '排序'
) ENGINE=InnoDB COMMENT='商品图片表';
