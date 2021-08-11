package com.example.shareit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView categoryText;
    TextView countryText;
    TextView stagesText;
    TextView commentText;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        countryText = findViewById(R.id.AdmindetailCountryText);
        commentText = findViewById(R.id.AdmindetailCommentText);
        imageView = findViewById(R.id.imageView2);
        categoryText = findViewById(R.id.AdmindetailCategoryText);

        stagesText = findViewById(R.id.AdmindetailStagesText);

        Intent intent = getIntent();

        String imageToIntent = intent.getStringExtra("image_url");
        Picasso.get().load(imageToIntent).into(imageView);
        String countryToIntent = intent.getStringExtra("Cauntry");
        countryText.setText(countryToIntent);
        String commentToIntent = intent.getStringExtra("Comment");
        commentText.setText(commentToIntent);
        String stagesToIntent = intent.getStringExtra("stages");
        stagesText.setText(stagesToIntent);
        String categoryToIntent = intent.getStringExtra("category");
        categoryText.setText(categoryToIntent);
    }
}