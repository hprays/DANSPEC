package com.danspec.danspec.common.exception;

import lombok.Getter;

@Getter
public class DanspecException extends RuntimeException {
    private final ErrorCode errorCode;

    public DanspecException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DanspecException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
