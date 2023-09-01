package com.example.loginregister.exception;

public class UsernameDuplicatedException extends RuntimeException{
    public UsernameDuplicatedException(){}
    public UsernameDuplicatedException(String message){
        super(message);
    }
}
