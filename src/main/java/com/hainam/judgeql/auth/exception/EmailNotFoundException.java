package com.hainam.judgeql.auth.exception;

import org.springframework.http.HttpStatus;

import com.hainam.judgeql.shared.exception.BaseException;

public class EmailNotFoundException extends BaseException{
    public EmailNotFoundException(){
        super("Không tìm thấy email",HttpStatus.NOT_FOUND);
    }
}
