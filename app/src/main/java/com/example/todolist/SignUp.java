package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
    }
    public void signUp(View view){
        TextView nameInput=findViewById(R.id.nameInput);
        TextView surnameInput=findViewById(R.id.surnameInput);
        TextView emailInput=findViewById(R.id.emailInput);
        TextView passwordInput=findViewById(R.id.passwordInput);
        if(nameInput!=null&&surnameInput!=null&&emailInput!=null&&passwordInput!=null){
            String name=nameInput.getText().toString().trim();
            String surname=surnameInput.getText().toString().trim();
            String email=emailInput.getText().toString().trim();
            String password=passwordInput.getText().toString().trim();
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(this,authResult -> {
                HashMap<String, String> userHashMap=new HashMap<>();
                userHashMap.put("Email",email);
                userHashMap.put("Name",name);
                userHashMap.put("Surname",surname);
                userHashMap.put("Password",password);

                database.collection("Users").add(userHashMap).addOnSuccessListener(this,dataResult ->{
                    Toast.makeText(this,"Kayıt başarıyla Oluşturuldu.Giriş Yapınız.", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(this,error->{
                    Toast.makeText(this,error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this,"Lütfen tekrar giriniz.", Toast.LENGTH_LONG).show();
                });
            }).addOnFailureListener(this,error ->{
                Toast.makeText(this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            });
    

        
        }else{
            Toast.makeText(this,"Lütfen bilgilerinizi tam girdiğinizden emin olunuz.", Toast.LENGTH_LONG).show();
        }



    }



}