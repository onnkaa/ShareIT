package com.example.shareit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userCountryFromFB;
    ArrayList<String> userCategoryFromFB;
    ArrayList<String> userStagesFromFB;
    MainRecyclerAdapter mainRecyclerAdapter;

    //initialize Variable
    RecyclerView recyclerView;

    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);


        userCommentFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();
        userCategoryFromFB = new ArrayList<>();
        userCountryFromFB = new ArrayList<>();
        userStagesFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFirestore();

        /*--------------------Hooks---------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*--------------------Tool Bar------------------------*/
        setSupportActionBar(toolbar);

        /*--------------------Navigation Drawer Menu----------*/

        //Hide or show items

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        menu.findItem(R.id.nav_deneme3).setVisible(false);
        //menu.findItem(R.id.nav_adminPanel).setVisible(false);

        //menu.findItem(R.id.nav_profile).setVisible(false);




        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        //Assign Variable
        recyclerView    =   findViewById(R.id.recycler_view);

        mainRecyclerAdapter = new MainRecyclerAdapter(userCountryFromFB,userCommentFromFB,userImageFromFB,userStagesFromFB,userCategoryFromFB);

        //Design RecyclerView
        RecyclerView.LayoutManager layoutManager    =   new StaggeredGridLayoutManager(
                2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Set Adapter to RecyclerView
        recyclerView.setAdapter(mainRecyclerAdapter);
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_add_product:
                Intent intentToAddProduct = new Intent(MainActivity.this,UploadActivity.class);
                startActivity(intentToAddProduct);
                break;
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                Intent intentToLogOut = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentToLogOut);
                finish();
                break;
            case R.id.nav_adminPanel:

                    Intent intentToAdminPanel = new Intent(MainActivity.this,AdminPanelActivity.class);
                    startActivity(intentToAdminPanel);


                break;
            case  R.id.nav_profile:
                Intent intentToProfile = new Intent(MainActivity.this,UserProfileActivity.class);
                startActivity(intentToProfile);

        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    String comment;
    String userEmail;
    String downloadUrl;
    String category;
    String country;
    String stages;
    public void getDataFromFirestore(){
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        //Filtreleme
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(MainActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object> data = snapshot.getData();

                        comment = (String) data.get("comment");
                        userEmail = (String) data.get("useremail");
                        downloadUrl = (String) data.get("downloadurl");
                        category = (String) data.get("category");
                        country = (String) data.get("country");
                        stages = (String) data.get("stages");

                        userCategoryFromFB.add(category);
                        userEmailFromFB.add(userEmail);
                        userImageFromFB.add(downloadUrl);
                        userCommentFromFB.add(comment);
                        userCountryFromFB.add(country);
                        userStagesFromFB.add(stages);

                        mainRecyclerAdapter.notifyDataSetChanged();
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

                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                swipeRefresh.setRefreshing(false);

            }
        }.start();
    }
}