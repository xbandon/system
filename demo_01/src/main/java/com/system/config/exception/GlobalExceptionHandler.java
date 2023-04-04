package com.system.config.exception;

import com.system.wrapper.Wrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xrx
 * @since 2023/4/4
 * description TODO
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Wrapper handle(ServiceException se) {
        logger.error(se.getMessage());
        return Wrapper.info(501, "Token异常");
    }
}
