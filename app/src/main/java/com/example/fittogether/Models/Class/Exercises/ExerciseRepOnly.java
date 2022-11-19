package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseRepOnly extends Exercise {
    private int reps;

    public ExerciseRepOnly() {
        this("");
    }

    public ExerciseRepOnly(String name) {
        this(name, ExerciseType.REP_ONLY);
    }

    public ExerciseRepOnly(String name, ExerciseType type) {
        super(name, type);
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
