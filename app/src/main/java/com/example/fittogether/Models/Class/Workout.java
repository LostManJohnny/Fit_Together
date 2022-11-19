package com.example.fittogether.Models.Class;

import com.example.fittogether.Api.Exceptions.IllegalExerciseTypeException;
import com.example.fittogether.Models.Enums.ExerciseType;

import java.util.ArrayList;

public class Workout {
    private final String name;
    private ArrayList<Exercise> exercises;

    public Workout(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise){
        this.exercises.add(exercise);
    }

    public int size(){
        return exercises.size();
    }

    public static ExerciseType getExerciseType(String type)
            throws IllegalExerciseTypeException {

        switch(type){
            case "REP_WEIGHT":
                return ExerciseType.REP_WEIGHT;
            case "REP_BODY":
                return ExerciseType.REP_BODY;
            case "REP_TIME":
                return ExerciseType.REP_TIME;
            case "TIME_ONLY":
                return ExerciseType.TIME_ONLY;
            case "REP_ONLY":
                return ExerciseType.REP_ONLY;
            case "FAILURE":
                return ExerciseType.FAILURE;
            case "DROP":
                return ExerciseType.DROP;
            case "ONE_REP_MAX":
                return ExerciseType.ONE_REP_MAX;
        }
        throw new IllegalExerciseTypeException("No exercise type by name (" + type + ")");
    }
}
