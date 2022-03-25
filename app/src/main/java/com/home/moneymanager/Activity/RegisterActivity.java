package com.home.moneymanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.home.moneymanager.R;
import com.home.moneymanager.Data.User;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText userName, emailInput, passwordInput, confirmPassword;
    Button registerButton, loginButton;
    FirebaseAuth mAuth;
    DatabaseReference database;
    //Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.btn_register);
        loginButton = findViewById(R.id.btn_login);

        // registration function
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUserName, txtEmailInput, txtPasswordInput, txtConfirmPassword;
                txtUserName = userName.getText().toString();
                txtEmailInput = emailInput.getText().toString();
                txtPasswordInput = passwordInput.getText().toString();
                txtConfirmPassword = confirmPassword.getText().toString();

                if(TextUtils.isEmpty(txtUserName)){
                    userName.setError("Enter username");
                }else if(TextUtils.isEmpty(txtEmailInput)){
                    emailInput.setError("Enter Email ID");
                }else if(TextUtils.isEmpty(txtPasswordInput)){
                    passwordInput.setError("Enter password");
                }else if(txtPasswordInput.length() < 8){
                    passwordInput.setError("Enter minimum 8 character");
                }else if(txtPasswordInput.length() > 16){
                    passwordInput.setError("Password should be less than 16 characters");
                }else if(TextUtils.isEmpty(txtConfirmPassword)){
                    userName.setError("Enter confirm password");
                }else if(!txtPasswordInput.equals(txtConfirmPassword)){
                    confirmPassword.setError("Password doesn't match");
                }else{
                    registerUser(txtEmailInput, txtPasswordInput);

                }
            }
        });

        // try to get username
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }


    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registering Successful", Toast.LENGTH_SHORT).show();
                    uploadUserInfo();
                    startActivity(new Intent(RegisterActivity.this, OptionActivity.class));
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("createUser", e.toString());
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //get current user name
//    private void getUser(){
//        FirebaseUser user = mAuth.getCurrentUser();
//        String uid = user.getUid();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(UserInfo);
//        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()){
//                    DataSnapshot dataSnapshot = task.getResult();
//                    String username = String.valueOf(dataSnapshot.child("username").getValue());
//                    Toast.makeText(RegisterActivity.this, username, Toast.LENGTH_SHORT).show();
//                    CurrentUserName = username; // setting CurrentUserName variable value
//                    //Toast.makeText(HomeActivity.this, CurrentUserName, Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(RegisterActivity.this, "Failed To Get Username", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//       // Toast.makeText(HomeActivity.this, "uid :" + uid, Toast.LENGTH_SHORT).show();
//    }

    // uploading user info
    private void uploadUserInfo() {
        String txtUserName = userName.getText().toString();
        String txtPassword = passwordInput.getText().toString();
        String txtEmail = emailInput.getText().toString();
        database = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        User user = new User(txtEmail,txtUserName, txtPassword);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        database.child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                userName.getText().clear();
                passwordInput.getText().clear();
                emailInput.getText().clear();
                confirmPassword.getText().clear();
                Toast.makeText(RegisterActivity.this, "User Data Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "User Data Not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this, OptionActivity.class));
            finish();
        }
    }
}