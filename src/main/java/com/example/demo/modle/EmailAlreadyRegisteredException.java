package com.example.demo.modle;

public class EmailAlreadyRegisteredException extends Exception{
    public EmailAlreadyRegisteredException(String message){
        super(message);
    }
}
