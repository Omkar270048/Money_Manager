package com.home.moneymanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.home.moneymanager.R;

public class OptionActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String CurrentUserName;

    Button addData, getData, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        mAuth = FirebaseAuth.getInstance();
        addData = findViewById(R.id.btn_add);
        getData = findViewById(R.id.btn_data);
        logout = findViewById(R.id.btn_logout);

        getUser(); // get current user's username

        // upload data button
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionActivity.this, UploadDataActivity.class));
            }
        });


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionActivity.this, ProductListActivity.class));
            }
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(OptionActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OptionActivity.this, RegisterActivity.class));
            }
        });
    }

    //get current user name
    private void getUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    String username = String.valueOf(dataSnapshot.child("username").getValue());
                   // Toast.makeText(OptionActivity.this, username, Toast.LENGTH_SHORT).show();
                    CurrentUserName = username; // setting CurrentUserName variable value
                    //Toast.makeText(HomeActivity.this, CurrentUserName, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OptionActivity.this, "Failed To Get Username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Toast.makeText(HomeActivity.this, "uid :" + uid, Toast.LENGTH_SHORT).show();
    }
}