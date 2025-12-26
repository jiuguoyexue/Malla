package org.example.jiuguomall.vo;

/**
 * 统一返回结果封装
 * @param <T> 数据类型
 */
public class Result<T> {

    private Integer code;      // 状态码
    private String message;    // 返回消息
    private T data;           // 返回数据
    private Long timestamp;   // 时间戳

    // 成功状态码常量
    public static final Integer SUCCESS_CODE = 200;
    public static final String SUCCESS_MESSAGE = "success";

    // 错误状态码常量
    public static final Integer ERROR_CODE = 500;
    public static final String ERROR_MESSAGE = "error";

    // 构造方法
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // Getter和Setter方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // 静态工厂方法

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回（有数据）
     */
    public static <T> Result<T> success(T data) {
        return success(SUCCESS_MESSAGE, data);
    }

    /**
     * 成功返回（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 错误返回（默认消息）
     */
    public static <T> Result<T> error() {
        return error(ERROR_MESSAGE);
    }

    /**
     * 错误返回（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return error(ERROR_CODE, message);
    }

    /**
     * 错误返回（自定义状态码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 错误返回（自定义状态码、消息和数据）
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 常见错误类型

    /**
     * 参数错误
     */
    public static <T> Result<T> paramError() {
        return paramError("参数错误");
    }

    public static <T> Result<T> paramError(String message) {
        return error(400, message);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized() {
        return unauthorized("未授权访问");
    }

    public static <T> Result<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 禁止访问
     */
    public static <T> Result<T> forbidden() {
        return forbidden("禁止访问");
    }

    public static <T> Result<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound() {
        return notFound("资源不存在");
    }

    public static <T> Result<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 业务逻辑错误
     */
    public static <T> Result<T> businessError(String message) {
        return error(422, message);
    }

    // toString方法
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    // 判断是否成功
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
