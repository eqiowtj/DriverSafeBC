package edu.northeastern.driversafebc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class QuizSelectionItemAdapter extends RecyclerView.Adapter<QuizSelectionItemViewHolder> {

    private final List<QuizSelectionItem> quizSelectionItemList;
    private final Context context;
    private final ActivityResultLauncher<Intent> launcher;

    public QuizSelectionItemAdapter(Context context, List<QuizSelectionItem> quizSelectionItemList, ActivityResultLauncher<Intent> launcher) {
        this.context = context;
        this.quizSelectionItemList = quizSelectionItemList;
        this.launcher = launcher;
    }

    @NonNull
    @Override
    public QuizSelectionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_selection_item, parent, false);
        return new QuizSelectionItemViewHolder(v, context, launcher);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizSelectionItemViewHolder holder, int position) {
        holder.bind(quizSelectionItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return quizSelectionItemList.size();
    }

}
