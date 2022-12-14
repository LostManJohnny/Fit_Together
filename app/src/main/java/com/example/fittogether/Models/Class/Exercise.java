package com.example.fittogether.Models.Class;

import androidx.annotation.Nullable;

import com.example.fittogether.Models.Enums.ExerciseType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

public class Exercise {
    private String name;
    private ExerciseType type;
    private ArrayList<ExerciseSet> setList;
    String target;

    //region Constructors

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

    //endregion

    // region Helpers
    public int getTotalSets(){
        return setList.size();
    }
    // endregion

    // region Getters
    public String getName() {
        return name;
    }

    public ExerciseType getType() {
        return type;
    }

    public ArrayList<ExerciseSet> getSetList() {
        return setList;
    }

    public String getTarget() {
        return target;
    }
    // endregion

    // region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }

    public void setType(String type){
        this.type = ExerciseType.valueOf(type);
    }

    public void setSetList(ArrayList<ExerciseSet> setList) {
        this.setList = setList;
    }

    public void setSetList(JsonArray set_list){
        this.setList = new ArrayList<>();

        for(int i=0; i<set_list.size(); i++){
            ExerciseSet new_set = new ExerciseSet();

            JsonObject set = set_list.get(i).getAsJsonObject();

            if(set.has("reps")){
                new_set.setReps(
                        set.get("reps").getAsInt()
                );
            }
            if(set.has("weight")){
                new_set.setWeight(
                        set.get("weight").getAsDouble()
                );
            }
            if(set.has("time")){
                new_set.setTimer(
                        new Timer(set.get("time").getAsString())
                );
            }

            setList.add(new_set);
        }
    }

    public void setTarget(String target) {
        this.target = target;
    }

    //endregion
}
