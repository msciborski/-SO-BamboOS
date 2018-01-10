package com.NachOS.Modules.Exceptions;

public class IncorrectRegisterException extends Exception{
    public IncorrectRegisterException(String message){
        super(message);
    }
    public IncorrectRegisterException(String message, Throwable throwable){
        super(message, throwable);
    }
}