package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseRepTime extends Exercise {
    private int reps;
    private double time;

    public ExerciseRepTime() {
        this("");
    }

    public ExerciseRepTime(String name) {
        this(name, ExerciseType.REP_TIME);
    }

    public ExerciseRepTime(String name, ExerciseType type) {
        super(name, type);
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
