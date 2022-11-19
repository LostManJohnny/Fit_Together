package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseDrop extends Exercise {

    public ExerciseDrop() {
        this("");
    }

    public ExerciseDrop(String name) {
        this(name, ExerciseType.DROP);
    }

    public ExerciseDrop(ExerciseType type) {
        super(type);
    }

    public ExerciseDrop(String name, ExerciseType type) {
        super(name, type);
    }
}
