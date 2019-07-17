package com.github.ontio.sourcing.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyException extends Exception {

    private Integer code;

    private String msg;

    private Object result;

}
