package com.example.fittogether.Models.Class.Exercises;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseRepWeight extends Exercise{
    private double weight;
    private int reps;
    private int sets;

    public ExerciseRepWeight() {
        this("");
    }

    public ExerciseRepWeight(String name) {
        this(name, ExerciseType.REP_WEIGHT);
    }

    public ExerciseRepWeight(String name, ExerciseType type) {
        super(name, type);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
}
