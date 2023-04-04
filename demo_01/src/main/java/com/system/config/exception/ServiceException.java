package com.system.config.exception;

import lombok.Data;

/**
 * @author xrx
 * @since 2023/4/4
 * description TODO
 */
@Data
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
