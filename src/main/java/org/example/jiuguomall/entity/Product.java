package org.example.jiuguomall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表实体类
 */
public class Product {

    /*主键*/
    private Long id;

    /*商品名称*/
    private String name;

    /*商品描述*/
    private String description;

    /*价格*/
    private BigDecimal price;

    /*库存*/
    private Integer stock;

    /*分类ID*/
    private Long categoryId;

    /*主图*/
    private String mainImage;

    /*子图列表（JSON数组）*/
    private String subImages;

    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;

    /*销量*/
    private Integer saleCount;

    /*创建时间*/
    private LocalDateTime createTime;

    /*更新时间*/
    private LocalDateTime updateTime;

    /*逻辑删除：0-未删除，1-删除*/
    private Integer deleted;

    // 状态常量
    public static final Integer STATUS_OFF_SHELF = 0;
    public static final Integer STATUS_ON_SHELF = 1;

    // 删除状态常量
    public static final Integer DELETED_NO = 0;
    public static final Integer DELETED_YES = 1;

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    // toString方法
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                ", saleCount=" + saleCount +
                ", createTime=" + createTime +
                '}';
    }

    // equals和hashCode方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // 构造方法
    public Product() {
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // 业务方法
    public boolean isOnShelf() {
        return STATUS_ON_SHELF.equals(status);
    }

    public boolean hasStock() {
        return stock != null && stock > 0;
    }

    public boolean canDeductStock(int quantity) {
        return hasStock() && stock >= quantity;
    }
}
