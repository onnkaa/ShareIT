package com.example.shareit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity  {

    Bitmap selectedImage;
    ImageView imageView;
    EditText commentText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Uri imageData;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    Spinner sp_parent, sp_child, sp_category;

    ArrayList<String> arrayList_category;
    ArrayAdapter<String> arrayAdapter_category;

    ArrayList<String> arrayList_parent;
    ArrayAdapter<String> arrayAdapter_parent;

    ArrayList<String> arrayList_adana, arrayList_adiyaman, arrayList_afyonkarahisar, arrayList_agri, arrayList_aksaray, arrayList_amasya, arrayList_ankara;
    ArrayAdapter<String> arrayAdapter_child;



    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        toolbar = findViewById(R.id.toolbar5);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageView = findViewById(R.id.imageView);
        commentText = findViewById(R.id.editTextTextPersonName);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        sp_parent   =   (Spinner)findViewById(R.id.countrySpinner);
        sp_child    =   (Spinner)findViewById(R.id.stagesSpinner);
        sp_category =   (Spinner)findViewById(R.id.categorySpinner);

        arrayList_category  =   new ArrayList<>();
        arrayList_category.add("Food");
        arrayList_category.add("clothes");
        arrayList_category.add("Computer");
        arrayList_category.add("Phone");
        arrayList_category.add("Other");

        arrayAdapter_category   =   new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_category);
        sp_category.setAdapter(arrayAdapter_category);

        arrayList_parent    =   new ArrayList<>();
        arrayList_parent.add("Adana");
        arrayList_parent.add("Adıyaman");
        arrayList_parent.add("Afyonkarahisar");
        arrayList_parent.add("Ağrı");
        arrayList_parent.add("Aksaray");
        arrayList_parent.add("Amasya");
        arrayList_parent.add("Ankara");

        arrayAdapter_parent =   new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_parent);
        sp_parent.setAdapter(arrayAdapter_parent);

        //child spinner process starts
        arrayList_adana =   new ArrayList<>();
        arrayList_adana.add("Aladağ");
        arrayList_adana.add("Ceyhan");
        arrayList_adana.add("Çukurova");

        arrayList_adiyaman  =   new ArrayList<>();
        arrayList_adiyaman.add("Besni");
        arrayList_adiyaman.add("Çelikhan");
        arrayList_adiyaman.add("Gölbaşı");

        sp_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    arrayAdapter_child  =   new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_adana);
                }
                if (position==1){
                    arrayAdapter_child  =   new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_adiyaman);
                }
                sp_child.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void upload(View view){
        if (imageData != null) {

            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL
                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();
                            String country = sp_parent.getSelectedItem().toString();
                            String stages = sp_child.getSelectedItem().toString();
                            String category = sp_category.getSelectedItem().toString();
                            String comment = commentText.getText().toString();

                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("useremail",userEmail);
                            postData.put("downloadurl",downloadUrl);
                            postData.put("country",country);
                            postData.put("stages",stages);
                            postData.put("category",category);
                            postData.put("comment",comment);
                            postData.put("date", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(UploadActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }

    }
    public void SelectImage(View view){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Intent intentToGallery  =   new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery  =   new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageData = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    imageView.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





}