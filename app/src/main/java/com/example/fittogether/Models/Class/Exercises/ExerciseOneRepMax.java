package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseOneRepMax extends Exercise {
    private double weight;

    public ExerciseOneRepMax() {
        this("");
    }

    public ExerciseOneRepMax(String name) {
        this(name, ExerciseType.ONE_REP_MAX);
    }

    public ExerciseOneRepMax(String name, ExerciseType type) {
        super(name, type);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
