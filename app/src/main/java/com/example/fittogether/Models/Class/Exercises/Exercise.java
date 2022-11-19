package com.example.fittogether.Models.Class.Exercises;

import androidx.annotation.Nullable;

import com.example.fittogether.Models.Enums.ExerciseType;

import java.util.Objects;

public abstract class Exercise {
    private String name;
    private int sets;
    protected ExerciseType type;

    public Exercise() {
        this("");
    }

    public Exercise(String name) {
        this(name, null);
    }

    public Exercise(ExerciseType type) {
        this("", type);
    }

    public Exercise(String name, ExerciseType type) {
        this.name = name;
        this.type = type;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ExerciseType getType() {
        return type;
    }

    public boolean equals(@Nullable Exercise obj) {

        return  obj != null &&
                Objects.equals(obj.getName(), this.name) &&
                obj.getType() == this.type;
    }
}
