package com.home.moneymanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.mtp.MtpConstants;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.home.moneymanager.Data.Product;
import com.home.moneymanager.Data.User;
import com.home.moneymanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadDataActivity extends AppCompatActivity {
    EditText productName, productCost;
    FirebaseAuth mAuth;
    DatabaseReference database;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        productCost = findViewById(R.id.cost);
        productName = findViewById(R.id.product);
        mAuth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserInfo();
            }
        });

    }

    private String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
//        System.out.println(formatter.format(date));
        //Toast.makeText(UploadDataActivity.this,formatter.format(date) , Toast.LENGTH_SHORT).show();
        return formatter.format(date);
    }

    // uploading user info
    private void uploadUserInfo() {
        String txtProductName = productName.getText().toString();
        String txtCost = productCost.getText().toString();
        String currentTime = getDate();
        database = FirebaseDatabase.getInstance().getReference("UserInfo");
       // User user = new User(txtUserName, txtPassword);
        Product product = new Product(txtProductName, txtCost, currentTime);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        database.child(uid).child("Product").push().setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                productName.getText().clear();
                productCost.getText().clear();
                Toast.makeText(UploadDataActivity.this, "Product Data Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDataActivity.this, "Product Data Not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }
}