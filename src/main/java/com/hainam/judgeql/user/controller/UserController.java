package com.hainam.judgeql.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.shared.response.ApiResponse;
import com.hainam.judgeql.shared.response.MetaPage;
import com.hainam.judgeql.user.dto.response.UserResponse;
import com.hainam.judgeql.user.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        List<UserResponse> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(allUsers, "Lấy thông tin tất cả người dùng thành công"));
    }
}
