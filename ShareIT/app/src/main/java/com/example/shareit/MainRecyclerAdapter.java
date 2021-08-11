package com.example.shareit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.PostHolder> {


    private ArrayList<String> userCountryList;
    private ArrayList<String> userCommentList;
    private ArrayList<String> userImageList;
    private ArrayList<String> userStagesList;
    private ArrayList<String> userCategoryList;

    public MainRecyclerAdapter(ArrayList<String> userCountryList, ArrayList<String> userCommentList, ArrayList<String> userImageList, ArrayList<String> userStagesList, ArrayList<String> userCategoryList ) {

        this.userCountryList = userCountryList;
        this.userCommentList = userCommentList;
        this.userImageList = userImageList;
        this.userStagesList = userStagesList;
        this.userCategoryList = userCategoryList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.countryAndText.setText(userCountryList.get(position));
        holder.commentText.setText(userCommentList.get(position));
        Picasso.get().load(userImageList.get(position)).into(holder.imageView);

    }
    @Override
    public int getItemCount() {
        return userCommentList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView countryAndText;
        TextView commentText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            //set onclick listener here
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start new intent
                    Intent i = new Intent(v.getContext(),DetailActivity.class);
                    i.putExtra("image_url",userImageList.get(getAdapterPosition()));
                    i.putExtra("Cauntry",userCountryList.get(getAdapterPosition()));
                    i.putExtra("Comment",userCommentList.get(getAdapterPosition()));
                    i.putExtra("stages",userStagesList.get(getAdapterPosition()));
                    i.putExtra("category",userCategoryList.get(getAdapterPosition()));
                    v.getContext().startActivity(i);
                }
            });

            imageView = itemView.findViewById(R.id.image_view);
            countryAndText = itemView.findViewById(R.id.text_view);
            commentText = itemView.findViewById(R.id.text_view2);

        }
    }
}
