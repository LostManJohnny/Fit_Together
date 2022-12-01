package com.example.fittogether.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittogether.Models.Class.Exercise;
import com.example.fittogether.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final Object TAG = "ExerciseAdapter.java";
    ArrayList<Exercise> exerciseList;
    Context context;

    public ExerciseAdapter(Context context, ArrayList<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG + ":viewType():", "" + viewType);

        return new ExerciseGeneralViewHolder(
                LayoutInflater
                        .from(context)
                        .inflate(
                                R.layout.view_exercise_general,
                                parent,
                                false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Exercise ex = exerciseList.get(position);

        Log.i(TAG + ":holder.class():", holder.getClass().toString());


    }

    @Override
    public int getItemViewType(int position) {
        return exerciseList.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}

/**
 * ViewHolder for a Rep Weight exercise type
 */
class ExerciseRepWeightViewHolder extends RecyclerView.ViewHolder {
    TextView tv_Name;
    EditText et_Reps, et_Sets, et_Weight;

    public ExerciseRepWeightViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Name = itemView.findViewById(R.id.tv_ex2_name);
        et_Reps = itemView.findViewById(R.id.et_ex2_reps);
        et_Sets = itemView.findViewById(R.id.et_ex2_sets);
        et_Weight = itemView.findViewById(R.id.et_ex2_weight);
    }
}

/**
 * ViewHolder for a Rep only exercise type
 */
class ExerciseRepOnlyViewHolder extends RecyclerView.ViewHolder {
    TextView tv_Name;
    EditText et_Reps, et_Sets;

    public ExerciseRepOnlyViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Name = itemView.findViewById(R.id.tv_ex1_name);
        et_Reps = itemView.findViewById(R.id.et_ex1_reps);
        et_Sets = itemView.findViewById(R.id.et_ex1_sets);
    }
}

/**
 * ViewHolder for all exercise types
 */
class ExerciseGeneralViewHolder extends RecyclerView.ViewHolder {
    TextView tv_Name, tv_duration, tv_measure;

    public ExerciseGeneralViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Name = itemView.findViewById(R.id.tv_name);
        tv_duration = itemView.findViewById(R.id.tv_duration);
        tv_measure = itemView.findViewById(R.id.tv_duration_measure);
    }
}

/**
 * ViewHolder for OneRepMax exercise type
 */
class ExerciseOneRepMaxViewHolder extends RecyclerView.ViewHolder {
    TextView tv_Name, tv_weightUnit;
    EditText et_Weight;

    public ExerciseOneRepMaxViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Name = itemView.findViewById(R.id.tv_ex5_name);
        tv_weightUnit = itemView.findViewById(R.id.tv_ex5_weight_unit);
        et_Weight = itemView.findViewById(R.id.et_ex5_weight);
    }
}

/**
 * ViewHolder for Rep Time exercise type
 */
class ExerciseRepTimeViewHolder extends RecyclerView.ViewHolder {
    TextView tv_Name;
    EditText et_Reps, et_Sets, et_Minutes, et_Seconds;

    public ExerciseRepTimeViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Name = itemView.findViewById(R.id.tv_ex3_name);
        et_Reps = itemView.findViewById(R.id.et_ex3_reps);
        et_Minutes = itemView.findViewById(R.id.et_ex3_minutes);
        et_Seconds = itemView.findViewById(R.id.et_ex3_seconds);
    }
}

/**
 * ViewHolder for Time only exercise type
 */
class ExerciseTimeOnlyViewHolder extends RecyclerView.ViewHolder {
    TextView tv_Name;
    EditText et_Sets, et_Minutes, et_Seconds;

    public ExerciseTimeOnlyViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_Name = itemView.findViewById(R.id.tv_ex4_name);
        et_Minutes = itemView.findViewById(R.id.et_ex4_minutes);
        et_Seconds = itemView.findViewById(R.id.et_ex4_seconds);
    }
}
