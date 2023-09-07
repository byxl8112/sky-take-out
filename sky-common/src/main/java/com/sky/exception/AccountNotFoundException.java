package com.sky.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 账号不存在异常
 */

public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }

}
