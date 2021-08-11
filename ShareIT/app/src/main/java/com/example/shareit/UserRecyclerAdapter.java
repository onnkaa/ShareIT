package com.example.shareit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.PostHolder> {

    private ArrayList<String> m_userEmailList;
    private ArrayList<String> m_userCountryList;
    private ArrayList<String> m_userCategoryList;
    private ArrayList<String> m_userImageList;
    private ArrayList<String> m_userCommentList;
    private ArrayList<String> m_userStagesList;

    public UserRecyclerAdapter(ArrayList<String> m_userEmailList, ArrayList<String> m_userCountryList
            , ArrayList<String> m_userCategoryList, ArrayList<String> m_userImageList
            , ArrayList<String> m_userCommentList, ArrayList<String> m_userStagesList) {

        this.m_userEmailList = m_userEmailList;
        this.m_userCountryList = m_userCountryList;
        this.m_userCategoryList = m_userCategoryList;
        this.m_userImageList = m_userImageList;
        this.m_userCommentList = m_userCommentList;
        this.m_userStagesList = m_userStagesList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_recycler_row,parent,false);
        return new UserRecyclerAdapter.PostHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.m_userEmailText.setText(m_userEmailList.get(position));
        holder.m_userCauntryText.setText(m_userCountryList.get(position));
        holder.m_userCategoryText.setText(m_userCategoryList.get(position));
        Picasso.get().load(m_userImageList.get(position)).into(holder.m_userImageView);

    }

    @Override
    public int getItemCount() {
        return m_userCategoryList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        TextView m_userEmailText;
        TextView m_userCauntryText;
        TextView m_userCategoryText;
        ImageView m_userImageView;





        public PostHolder(@NonNull View itemView) {
            super(itemView);

            m_userEmailText = itemView.findViewById(R.id.user_panel_email_recyclerview);
            m_userCauntryText = itemView.findViewById(R.id.user_panel_country_recyclerview);
            m_userCategoryText = itemView.findViewById(R.id.user_panel_category_recyclerview);
            m_userImageView = itemView.findViewById(R.id.user_image);

        }
    }
}
