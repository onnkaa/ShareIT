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

public class AdminPanelRecylerAdapter extends RecyclerView.Adapter<AdminPanelRecylerAdapter.PostHolder> {

    private ArrayList<String> AdminuserEmailList;
    private ArrayList<String> AdminuserCountryList;
    private ArrayList<String> AdminproductCategoryList;
    private ArrayList<String> AdminuserImageList;
    private ArrayList<String> AdminuserCommentList;
    private ArrayList<String> AdminuserStagesList;

    public AdminPanelRecylerAdapter(ArrayList<String> AdminuserEmailList, ArrayList<String> AdminuserCountryList, ArrayList<String> AdminproductCategoryList, ArrayList<String> AdminuserImageList,
                                    ArrayList<String> AdminuserCommentList, ArrayList<String> AdminuserStagesList) {
        this.AdminuserEmailList = AdminuserEmailList;
        this.AdminuserCountryList = AdminuserCountryList;
        this.AdminproductCategoryList = AdminproductCategoryList;
        this.AdminuserImageList = AdminuserImageList;
        this.AdminuserCommentList = AdminuserCommentList;
        this.AdminuserStagesList = AdminuserStagesList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_recycler_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.AdminuserEmailText.setText(AdminuserEmailList.get(position));
        holder.AdminuserCountryText.setText(AdminuserCountryList.get(position));
        holder.AdminproductCategoryText.setText(AdminproductCategoryList.get(position));
        Picasso.get().load(AdminuserImageList.get(position)).into(holder.AdminimageView);
    }

    @Override
    public int getItemCount() {
        return AdminuserCountryList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{


        TextView AdminuserEmailText;
        TextView AdminuserCountryText;
        TextView AdminproductCategoryText;
        ImageView AdminimageView;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),AdminDetailActivity.class);
                    intent.putExtra("adminUseremail",AdminuserEmailList.get(getAdapterPosition()));
                    intent.putExtra("adminDownloadurl",AdminuserImageList.get(getAdapterPosition()));
                    intent.putExtra("adminCountry",AdminuserCountryList.get(getAdapterPosition()));
                    intent.putExtra("adminComment",AdminuserCommentList.get(getAdapterPosition()));
                    intent.putExtra("adminStages",AdminuserStagesList.get(getAdapterPosition()));
                    intent.putExtra("adminCategory",AdminproductCategoryList.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });


            AdminuserEmailText = itemView.findViewById(R.id.admin_panel_email_recyclerview);
            AdminuserCountryText = itemView.findViewById(R.id.admin_panel_country_recyclerview);
            AdminproductCategoryText = itemView.findViewById(R.id.admin_panel_category_recyclerview);
            AdminimageView = itemView.findViewById(R.id.imageView4);
        }
    }
}
