package com.couple.space.dto;

import lombok.Data;

/**
 * 统一API响应格式
 * 所有接口返回的数据都使用这个格式
 */
@Data
public class ApiResponse<T> {
    /**
     * 业务状态
     * success: 成功
     * fail: 失败
     */
    private String state;
    
    /**
     * 响应消息
     * 成功时返回成功信息
     * 失败时返回错误信息
     */
    private String msg;
    
    /**
     * 响应数据
     * 成功时返回具体数据
     * 失败时返回null
     */
    private T data;
    
    /**
     * 创建成功响应
     * @param data 响应数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setState("success");
        response.setMsg("操作成功");
        response.setData(data);
        return response;
    }
    
    /**
     * 创建失败响应
     * @param message 错误信息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> fail(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setState("fail");
        response.setMsg(message);
        response.setData(null);
        return response;
    }
} 