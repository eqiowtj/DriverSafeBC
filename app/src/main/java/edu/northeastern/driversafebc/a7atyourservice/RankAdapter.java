package edu.northeastern.driversafebc.a7atyourservice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.driversafebc.R;
import edu.northeastern.driversafebc.a7atyourservice.pojo.User;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {

    Context context;

    ArrayList<User> list;


    public RankAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = list.get(position);
        holder.userName.setText(user.getUserName());
        holder.totalScore.setText(user.getTotalScore());
        holder.rate.setText(String.valueOf(user.getRate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName, totalScore, rate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.username);
            totalScore = itemView.findViewById(R.id.totalScore);
            rate = itemView.findViewById(R.id.rate);
        }
    }
}
