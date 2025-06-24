package com.hainam.judgeql.auth.exception;

import org.springframework.http.HttpStatus;

import com.hainam.judgeql.shared.exception.BaseException;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(){
        super("Email đã tồn tại",HttpStatus.CONFLICT);
    }
}
