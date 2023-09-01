package com.example.loginregister.exception;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException(){}
    public ExpiredJwtException(String message){
        super(message);
    }
}
