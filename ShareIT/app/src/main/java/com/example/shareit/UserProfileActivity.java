package com.example.shareit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> m_userEmailFromFB;
    ArrayList<String> m_userImageFromFB;
    ArrayList<String> m_userCountryFromFB;
    ArrayList<String> m_userCategoryFromFB;
    ArrayList<String> m_userCommentFromFB;
    ArrayList<String> m_userStagesFromFB;

    UserRecyclerAdapter userRecyclerAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_userEmailFromFB = new ArrayList<>();
        m_userImageFromFB = new ArrayList<>();
        m_userCategoryFromFB = new ArrayList<>();
        m_userCountryFromFB = new ArrayList<>();
        m_userCommentFromFB = new ArrayList<>();
        m_userStagesFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore3();

        RecyclerView recyclerView = findViewById(R.id.user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userRecyclerAdapter = new UserRecyclerAdapter(m_userEmailFromFB,m_userCountryFromFB,m_userCategoryFromFB,m_userImageFromFB,
                m_userCommentFromFB,m_userStagesFromFB);
        recyclerView.setAdapter(userRecyclerAdapter);


    }
    public void getDataFromFirestore3(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String filtreEmail = firebaseUser.getEmail();

        CollectionReference collectionReference = firebaseFirestore.collection("PostsAdmin");
        collectionReference.whereEqualTo("useremailAdmin",filtreEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(UserProfileActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String comment = (String) data.get("commentAdmin");
                        String userEmail = (String) data.get("useremailAdmin");
                        String downloadUrl = (String) data.get("downloadurlAdmin");
                        String category = (String) data.get("categoryAdmin");
                        String country = (String) data.get("countryAdmin");
                        String stages = (String) data.get("stagesAdmin");

                        m_userCategoryFromFB.add(category);
                        m_userEmailFromFB.add(userEmail);
                        m_userImageFromFB.add(downloadUrl);
                        m_userCountryFromFB.add(country);
                        m_userCommentFromFB.add(comment);
                        m_userStagesFromFB.add(stages);

                        userRecyclerAdapter.notifyDataSetChanged();
                        System.out.println("imageurl " + downloadUrl);

                    }

                }
            }
        });

    }

}