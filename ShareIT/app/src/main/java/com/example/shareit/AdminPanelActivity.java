package com.example.shareit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AdminPanelActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> AdminuserEmailFromFB;
    ArrayList<String> AdminuserImageFromFB;
    ArrayList<String> AdminuserCountryFromFB;
    ArrayList<String> AdminuserCategoryFromFB;
    ArrayList<String> AdminuserCommentFromFB;
    ArrayList<String> AdminuserStagesFromFB;

    AdminPanelRecylerAdapter adminPanelRecylerAdapter;

    private SwipeRefreshLayout swipeRefresh;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        swipeRefresh = findViewById(R.id.swipe_refresh_admin);
        swipeRefresh.setOnRefreshListener(this);

        AdminuserEmailFromFB = new ArrayList<>();
        AdminuserImageFromFB = new ArrayList<>();
        AdminuserCategoryFromFB = new ArrayList<>();
        AdminuserCountryFromFB = new ArrayList<>();
        AdminuserCommentFromFB = new ArrayList<>();
        AdminuserStagesFromFB = new ArrayList<>();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFirestore2();

        RecyclerView recyclerView = findViewById(R.id.AdminrecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminPanelRecylerAdapter = new AdminPanelRecylerAdapter(AdminuserEmailFromFB,AdminuserCountryFromFB,AdminuserCategoryFromFB,AdminuserImageFromFB,
                AdminuserCommentFromFB,AdminuserStagesFromFB);
        recyclerView.setAdapter(adminPanelRecylerAdapter);




    }
    public void getDataFromFirestore2(){
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        //Filtreleme
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(AdminPanelActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object> data = snapshot.getData();

                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");
                        String category = (String) data.get("category");
                        String country = (String) data.get("country");
                        String stages = (String) data.get("stages");

                        AdminuserCategoryFromFB.add(category);
                        AdminuserEmailFromFB.add(userEmail);
                        AdminuserImageFromFB.add(downloadUrl);
                        AdminuserCountryFromFB.add(country);
                        AdminuserCommentFromFB.add(comment);
                        AdminuserStagesFromFB.add(stages);


                        adminPanelRecylerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(AdminPanelActivity.this,AdminPanelActivity.class);
                startActivity(intent);
                finish();
                swipeRefresh.setRefreshing(false);

            }
        }.start();
    }
}