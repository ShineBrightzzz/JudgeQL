package com.hainam.judgeql.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.shared.response.ApiResponse;
import com.hainam.judgeql.shared.response.MetaPage;
import com.hainam.judgeql.user.dto.response.UserResponse;
import com.hainam.judgeql.user.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String sortDir){
        MetaPage metaPage = new MetaPage();
        metaPage.setPage(page);
        metaPage.setSize(size);
        metaPage.setSort(sort);
        metaPage.setSortDir(sortDir);
        
        List<UserResponse> allUsers = userService.getAllUsers(metaPage);
        return ResponseEntity.ok(ApiResponse.success(allUsers, "Lấy thông tin tất cả người dùng thành công", metaPage));
    }
}
