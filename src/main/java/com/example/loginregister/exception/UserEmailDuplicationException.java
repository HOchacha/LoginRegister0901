package com.example.loginregister.exception;

public class UserEmailDuplicationException extends RuntimeException{
    public UserEmailDuplicationException (){ }
    public UserEmailDuplicationException(String message){
         super(message);
    }
}
