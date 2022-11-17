package com.example.fittogether.Models.Enums;

public enum ExerciseType {
    REP_WEIGHT, // Count reps and weight for each rep
    REP_ONLY, // Only count number of reps
    REP_BODY, // Only count number of reps for body weight
    TIME_ONLY, // Timer only
    FAILURE, // Rep to failure
    DROP, // Drop set
    ONE_REP_MAX // One rep max
}

//TODO: Consider creating RepType and SetType