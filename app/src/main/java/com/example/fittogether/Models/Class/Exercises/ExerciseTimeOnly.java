package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseTimeOnly extends Exercise {
    private double time;

    public ExerciseTimeOnly() {
        this("");
    }

    public ExerciseTimeOnly(String name) {
        this(name, ExerciseType.TIME_ONLY);
    }

    public ExerciseTimeOnly(ExerciseType type) {
        super(type);
    }

    public ExerciseTimeOnly(String name, ExerciseType type) {
        super(name, type);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
