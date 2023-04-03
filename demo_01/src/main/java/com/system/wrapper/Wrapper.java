package com.system.wrapper;

import com.system.constant.ResponseConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xrx
 * @since 2023/3/28
 * description 返回结果封装类
 */
@Data
public class Wrapper<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public Wrapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Wrapper(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功信息
     *
     * @param <E>
     * @return
     */
    public static <E> Wrapper<E> success() {
        return new Wrapper<>(ResponseConstant.SUCCESS_CODE, ResponseConstant.SUCCESS_MESSAGE);
    }

    public static <E> Wrapper<E> success(E obj) {
        return new Wrapper<>(ResponseConstant.SUCCESS_CODE, ResponseConstant.SUCCESS_MESSAGE, obj);
    }

    /**
     * 返回错误信息
     *
     * @param <E>
     * @return
     */
    public static <E> Wrapper<E> error() {
        return new Wrapper<>(ResponseConstant.ERROR_CODE, ResponseConstant.ERROR_MESSAGE);
    }

    /**
     * 返回自定义信息
     *
     * @param <E>
     * @return
     */
    public static <E> Wrapper<E> info(int code, String msg) {
        return new Wrapper<>(code, msg);
    }
}
