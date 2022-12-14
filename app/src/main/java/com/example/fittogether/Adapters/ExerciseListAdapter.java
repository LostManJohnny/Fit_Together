package com.example.fittogether.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittogether.ActivityWorkout;
import com.example.fittogether.Models.Class.Exercise;
import com.example.fittogether.R;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<AddExerciseHolder> {
    ArrayList<Exercise> exercises;
    Context context;
    String workoutName;

    public ExerciseListAdapter(Context context, ArrayList<Exercise> exercises, String workoutName) {
        this.context = context;
        this.exercises = exercises;
        this.workoutName = workoutName;
    }

    @NonNull
    @Override
    public AddExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddExerciseHolder(
                LayoutInflater
                        .from(context)
                        .inflate(
                                R.layout.view_add_exercise,
                                parent,
                                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AddExerciseHolder holder, int position) {
        Exercise ex = exercises.get(position);
        holder.name.setText(ex.getName());
        holder.target.setText(ex.getTarget());
        holder.add.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActivityWorkout.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", workoutName);
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}

class AddExerciseHolder extends RecyclerView.ViewHolder{
    TextView name, target;
    Button add;

    public AddExerciseHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tv_exercise_name);
        target = itemView.findViewById(R.id.tv_exercise_target);
        add = itemView.findViewById(R.id.btn_add);
    }
}