package com.example.fittogether.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittogether.Models.Class.Workout;
import com.example.fittogether.R;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Workout> workouts;

    public WorkoutAdapter(Context context, ArrayList<Workout> workouts){
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
                            R.layout.rv_row_workout,
                            parent,
                            false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Workout workout = workouts.get(position);

        if(holder.getClass() == WorkoutViewHolder.class){
            WorkoutViewHolder workoutViewholder = (WorkoutViewHolder) holder;

            workoutViewholder.tv_Title.setText(workout.getName());

            workoutViewholder.tv_Count.setText(workout.size());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class WorkoutViewHolder extends RecyclerView.ViewHolder{
    TextView tv_Title, tv_Count;
    Button btn_Edit;

    public WorkoutViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Title = itemView.findViewById(R.id.tv_WorkoutRowTitle);
        tv_Count = itemView.findViewById(R.id.tv_WorkoutRowTotalExercises);
        btn_Edit = itemView.findViewById(R.id.btn_WorkoutRowEdit);
    }
}
