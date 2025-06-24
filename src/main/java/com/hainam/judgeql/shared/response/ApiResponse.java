package com.hainam.judgeql.shared.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private MetaPage meta;

    public static <T> ApiResponse<T> success(T data, String message){
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String message, MetaPage meta){
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = message;
        response.meta = meta;
        return response;
    }

    public static <T> ApiResponse<T> error(String message){
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.data = null;
        return response;
    }

}
