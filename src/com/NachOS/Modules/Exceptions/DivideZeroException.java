package com.NachOS.Modules.Exceptions;

public class DivideZeroException extends Exception {
    public DivideZeroException(String message){
        super(message);
    }
    public DivideZeroException(String message, Throwable throwable){
        super(message, throwable);
    }
}