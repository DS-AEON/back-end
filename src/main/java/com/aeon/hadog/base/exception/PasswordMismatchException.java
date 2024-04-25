package com.aeon.hadog.base.exception;

import com.aeon.hadog.base.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordMismatchException extends RuntimeException {
    private final ErrorCode errorCode;
}
