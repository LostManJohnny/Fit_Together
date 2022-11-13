package com.example.fittogether.Exceptions;

public class InvalidEmailException extends Exception{

    public InvalidEmailException(String message){
        super(message);
    }
    public InvalidEmailException(){
        this("Invalid or null email is trying to be used.");
    }
}
