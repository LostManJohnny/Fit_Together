package com.example.fittogether.Models.Class;

import com.example.fittogether.Api.Exceptions.IllegalExerciseTypeException;
import com.example.fittogether.Models.Enums.ExerciseType;

import java.util.ArrayList;

public class Workout {
    private final String name;
    private ArrayList<ListExercise> listExercises;

    public Workout(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ListExercise> getExercises() {
        return listExercises;
    }

    public void addExercise(ListExercise listExercise){
        this.listExercises.add(listExercise);
    }

    public int size(){
        return listExercises.size();
    }

    public static ExerciseType getExerciseType(String type)
            throws IllegalExerciseTypeException {

        switch(type){
            case "REP_WEIGHT":
                return ExerciseType.REP_WEIGHT;
            case "REP_TIME":
                return ExerciseType.REP_TIME;
            case "TIME_ONLY":
                return ExerciseType.TIME_ONLY;
            case "REP_ONLY":
                return ExerciseType.REP_ONLY;
            case "ONE_REP_MAX":
                return ExerciseType.ONE_REP_MAX;
        }
        throw new IllegalExerciseTypeException("No exercise type by name (" + type + ")");
    }

    public void setExercises(ArrayList<Exercise> listExercises) {
    }
}
