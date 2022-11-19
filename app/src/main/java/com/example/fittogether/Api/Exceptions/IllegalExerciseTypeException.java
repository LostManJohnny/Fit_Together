package com.example.fittogether.Api.Exceptions;

public class IllegalExerciseTypeException extends Exception {
    public IllegalExerciseTypeException(String msg){

    }
    public IllegalExerciseTypeException(){
        this("No");
    }
}
