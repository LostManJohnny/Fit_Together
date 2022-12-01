package com.example.fittogether.Adapters;

import android.annotation.SuppressLint;
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
import com.example.fittogether.Models.Class.WorkoutPreview;
import com.example.fittogether.R;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<WorkoutPreview> workouts;

    public WorkoutAdapter(Context context, ArrayList<WorkoutPreview> workouts){
        this.context = context;
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkoutViewHolder(
                    LayoutInflater
                            .from(context)
                            .inflate(
                                R.layout.view_workout,
                                parent,
                                false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WorkoutPreview workout = workouts.get(position);

        if(holder.getClass() == WorkoutViewHolder.class){
            WorkoutViewHolder workoutViewholder = (WorkoutViewHolder) holder;

            workoutViewholder.tv_Title.setText(workout.getName());

            workoutViewholder.tv_Count.setText(Integer.toString(workout.getSize()));

            workoutViewholder.btn_Edit.setOnClickListener(view -> {
                Intent intent = new Intent(context, ActivityWorkout.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", workout.getName());

                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }
}

class WorkoutViewHolder extends RecyclerView.ViewHolder{
    public TextView tv_Title, tv_Count;
    public Button btn_Edit;

    public WorkoutViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Title = itemView.findViewById(R.id.tv_WorkoutRowTitle);
        tv_Count = itemView.findViewById(R.id.tv_WorkoutRowTotalExercises);
        btn_Edit = itemView.findViewById(R.id.btn_WorkoutRowEdit);
    }
}
