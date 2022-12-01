package com.example.fittogether.Models.Enums;

public enum ExerciseType {
    REP_ONLY, // Only count number of reps
    REP_WEIGHT, // Count reps and weight for each rep
    REP_TIME,
    TIME_ONLY, // Timer only
    ONE_REP_MAX // One rep max
    ;
}

//TODO: Consider creating RepType and SetType