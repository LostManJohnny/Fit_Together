package com.example.fittogether.Api.Exceptions;

public class NullCurrentUserException extends Exception{
    public NullCurrentUserException(String message){
        super(message);
    }

    public NullCurrentUserException(){
        this("Current Firebase user is null");
    }
}
