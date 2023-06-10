package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);


        auth=FirebaseAuth.getInstance();
        FirebaseUser userinfo=auth.getCurrentUser();
        if (userinfo!=null){
            Toast.makeText(this,"Hosgeldiniz "+userinfo.getEmail(),Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,PlanList.class);
            startActivity(intent);
            finish();
        }






    }
    // veritabanindan gelcek veriler
    // login button onClick method
    public void login(View view){
        TextView emailText = findViewById(R.id.emailText);
        TextView passwordText = findViewById(R.id.passwordText);
        Intent intent= new Intent(MainActivity.this,PlanList.class);

        if(emailText!=null&&passwordText!=null) {

            String email = emailText.getText().toString();
            intent.putExtra("email",email);
            String password = passwordText.getText().toString();
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(this, authResult -> {
                startActivity(intent);
                finish();
            }).addOnFailureListener(this, error -> {
                error.printStackTrace();
                Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            });
        }

    }

    // signup button onClick method
    public void signup(View view){
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
        finish();


    }
    public void sifremiUnuttum(View view){

        if(!binding.emailText.getText().toString().trim().equals("")){
        auth.sendPasswordResetEmail(binding.emailText.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Sifre yenileme maili gonderilmistir.Lutfen mailinizi kontrol ediniz.",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }else{
            Toast.makeText(this,"Lutfen yukari mailinizi yazin ve daha sonra tekrar deneyin",Toast.LENGTH_LONG).show();
        }

    }
}
