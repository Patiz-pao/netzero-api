package com.netzero.version.demo.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class NetZeroException extends RuntimeException {
    private final HttpStatus status;

    public NetZeroException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}