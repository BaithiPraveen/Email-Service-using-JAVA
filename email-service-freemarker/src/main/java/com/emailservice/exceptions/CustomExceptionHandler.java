package com.emailservice.exceptions;

public class CustomExceptionHandler extends RuntimeException{

    public CustomExceptionHandler(String msg)
    {
        super(msg);
    }
}
