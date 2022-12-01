package com.example.fittogether.Models.Class;

import com.example.fittogether.Models.Enums.ExerciseType;

public class ExerciseSet {
    int reps;
    double weight;
    Timer timer;

    public ExerciseSet(){
        this.reps = -1;
        this.weight = -1;
        this.timer = null;
    }

    public ExerciseSet(int reps, double weight) {
        this();
        this.reps = reps;
        this.weight = weight;
    }

    public ExerciseSet(int reps){
        this();
        this.reps = reps;
    }

    public ExerciseSet(Timer timer){
        this();
        this.timer = timer;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
