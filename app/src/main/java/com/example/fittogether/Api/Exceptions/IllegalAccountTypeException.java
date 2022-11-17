package com.example.fittogether.Api.Exceptions;

public class IllegalAccountTypeException extends Exception{

    public IllegalAccountTypeException(String message){
        super(message);
    }

    public IllegalAccountTypeException(){
        this("Unknown account type.");
    }
}
