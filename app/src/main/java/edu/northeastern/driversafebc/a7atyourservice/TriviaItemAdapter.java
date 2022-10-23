package edu.northeastern.driversafebc.a7atyourservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.driversafebc.R;
import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;


public class TriviaItemAdapter extends RecyclerView.Adapter<TriviaItemViewHolder> {

    private final List<Trivia> triviaList;
    private final Context context;

    public TriviaItemAdapter(Context context, List<Trivia> triviaList) {
        this.context = context;
        this.triviaList = triviaList;
    }

    @NonNull
    @Override
    public TriviaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trivia_item, parent, false);
        return new TriviaItemViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TriviaItemViewHolder holder, int position) {
        holder.bind(triviaList.get(position));
    }

    @Override
    public int getItemCount() {
        return triviaList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateTriviaList(List<Trivia> triviaList) {
        this.triviaList.clear();
        this.triviaList.addAll(triviaList);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearTriviaList() {
        this.triviaList.clear();
        notifyDataSetChanged();
    }

}
