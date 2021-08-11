package com.example.shareit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    ArrayList<MainModel> mainModels;

    public MainAdapter(Context context, ArrayList<MainModel> mainModels){
        this.context    =   context;
        this.mainModels =   mainModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create View
        View view   = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Set numberImages to ImageView
        holder.imageView.setImageResource(mainModels.get(position).getNumberImages());
        //Set numberWords to TextView
        holder.textView.setText(mainModels.get(position).getNumberWords());
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize Veriable
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign Variable
            imageView   =   itemView.findViewById(R.id.image_view);
            textView    =   itemView.findViewById(R.id.text_view);
        }
    }
}
