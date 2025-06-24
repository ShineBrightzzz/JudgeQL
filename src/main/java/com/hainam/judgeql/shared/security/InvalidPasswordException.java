package com.hainam.judgeql.shared.security;

import org.springframework.http.HttpStatus;

import com.hainam.judgeql.shared.exception.BaseException;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException(){
        super("Tài khoản hoặc mật khẩu không đúng", HttpStatus.BAD_REQUEST);
    }
}
