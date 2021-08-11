package com.example.shareit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdminDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView AdminPersonHandEmail;
    TextView AdmincategoryText;
    TextView AdmincountryText;
    TextView AdminstagesText;
    TextView AdmincommentText;
    EditText AdminPersonEmail;
    Button handOverButton;
    FirebaseFirestore firebaseFirestore;

    String adminImageToIntent;
    String adminEmailToIntent;
    String adminCountryToIntent;
    String adminCommentToIntent;
    String adminStagesToIntent;
    String adminCategoryToIntent;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView2);
        AdmincategoryText = findViewById(R.id.AdmindetailCategoryText);
        AdmincountryText = findViewById(R.id.AdmindetailCountryText);
        AdminstagesText = findViewById(R.id.AdmindetailStagesText);
        AdmincommentText = findViewById(R.id.AdmindetailCommentText);
        AdminPersonEmail = findViewById(R.id.AdminPersonEmail);
        AdminPersonHandEmail = findViewById(R.id.adminDetailHandPersonEmail);
        handOverButton = findViewById(R.id.HandOverBtn);

        Intent intent = getIntent();

        adminImageToIntent = intent.getStringExtra("adminDownloadurl");
        Picasso.get().load(adminImageToIntent).into(imageView);

        adminEmailToIntent = intent.getStringExtra("adminUseremail");
        AdminPersonHandEmail.setText(adminEmailToIntent);

        adminCountryToIntent = intent.getStringExtra("adminCountry");
        AdmincountryText.setText(adminCountryToIntent);

        adminCommentToIntent = intent.getStringExtra("adminComment");
        AdmincommentText.setText(adminCommentToIntent);
        adminStagesToIntent = intent.getStringExtra("adminStages");
        AdminstagesText.setText(adminStagesToIntent);
        adminCategoryToIntent = intent.getStringExtra("adminCategory");
        AdmincategoryText.setText(adminCategoryToIntent);

    }

    public void handOverClick(View view){

        String adminDetailPersonEmail = AdminPersonEmail.getText().toString();
        String adminDetailCountry = AdmincountryText.getText().toString();
        String adminDetailStages = AdminstagesText.getText().toString();
        String adminDetailCategory = AdmincategoryText.getText().toString();
        String adminDetailComment = AdmincommentText.getText().toString();
        String adminDetailImage = adminImageToIntent.toString();

        HashMap<String, Object> postDataAdmin = new HashMap<>();
        postDataAdmin.put("useremailAdmin",adminDetailPersonEmail);
        postDataAdmin.put("downloadurlAdmin",adminDetailImage);
        postDataAdmin.put("countryAdmin",adminDetailCountry);
        postDataAdmin.put("stagesAdmin",adminDetailStages);
        postDataAdmin.put("categoryAdmin",adminDetailCategory);
        postDataAdmin.put("commentAdmin",adminDetailComment);
        postDataAdmin.put("dateAdmin", FieldValue.serverTimestamp());

        firebaseFirestore.getInstance().collection("PostsAdmin").add(postDataAdmin).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(AdminDetailActivity.this,AdminPanelActivity.class);

                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminDetailActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });






        firebaseFirestore.getInstance().collection("Posts")
                .whereEqualTo("downloadurl",adminImageToIntent.toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList){
                            batch.delete(snapshot.getReference());
                        }
                        batch.commit()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(AdminDetailActivity.this,AdminPanelActivity.class);

                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}