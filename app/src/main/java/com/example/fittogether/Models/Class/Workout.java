package com.example.fittogether.Models.Class;

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
}
