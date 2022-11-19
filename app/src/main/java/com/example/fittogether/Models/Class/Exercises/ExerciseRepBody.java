package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseRepBody extends Exercise {
    private int reps;

    public ExerciseRepBody() {
        this("");
    }

    public ExerciseRepBody(String name) {
        this(name, ExerciseType.REP_BODY);
    }

    public ExerciseRepBody(ExerciseType type) {
        super(type);
    }

    public ExerciseRepBody(String name, ExerciseType type) {
        super(name, type);
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
