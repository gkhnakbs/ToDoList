package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todolist.databinding.ActivityCreatePlansBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;


public class CreatePlans extends AppCompatActivity {
    private FirebaseFirestore firestoreU;
    private FirebaseAuth authU;
    private FirebaseStorage storageU;
    private StorageReference storageReferenceU;
    private  FirebaseDatabase uReferance;


    int years_start, months_start, days_start,years_open, months_open, days_open;
    int start_clock, start_minute;
    String title, planText,email;
   private ActivityCreatePlansBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreatePlansBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnDelete.setVisibility(View.INVISIBLE);
        binding.btnDelete.setEnabled(false);

       Intent intent= getIntent();
       String info= intent.getStringExtra("info");
       if(info.equals("new")){

       }else if (info.equals("old")){
           binding.txtPlanTitle.setText(intent.getStringExtra("planTitle"));
           binding.txtPlanText.setText(intent.getStringExtra("planText"));
           binding.btnSavePlan.setEnabled(false);
           binding.btnSavePlan.setVisibility(View.INVISIBLE);
           binding.btnDelete.setEnabled(true);
           binding.btnDelete.setVisibility(View.VISIBLE);
       }

        authU=FirebaseAuth.getInstance();
        firestoreU=FirebaseFirestore.getInstance();
        storageU=FirebaseStorage.getInstance();
        storageReferenceU= storageU.getReference();
        uReferance= FirebaseDatabase.getInstance();




        Calendar calendar= Calendar.getInstance();
        //Asagidaki degiskenler telefon hangi tarihteyse o tarihi alir.
        days_open= calendar.get(Calendar.DAY_OF_MONTH);
        months_open= calendar.get(Calendar.MONTH);
        years_open= calendar.get(Calendar.YEAR);

        if(binding.txtPlanText.getText().equals(" ") || binding.txtPlanTitle.getText().equals(" ")){
            binding.btnSavePlan.setEnabled(false);
        }


    }


    DatePickerDialog.OnDateSetListener setListener;


    protected  Dialog onCreateDialog(int id){
        return  new DatePickerDialog(this,setListener,years_open,months_open,days_open);
    }


    public void start_date(View view){

        setListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                years_start=year;
                months_start=month+1;
                days_start=dayOfMonth;

                System.out.println(years_start);
                System.out.println(months_start);
                System.out.println(days_start);

            }
        };
        showDialog(1);
    }


    public  void start_clock(View view){
        Calendar calendar1= Calendar.getInstance();
        int hours= calendar1.get(Calendar.HOUR_OF_DAY);
        int minutes= calendar1.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                start_clock=hourOfDay;
                start_minute=minute;
                //binding.txtPlanTitle.setText("bas"+start_clock+":"+start_minute);
            }
        },hours,minutes,true);
        timePickerDialog.show();
    }

    public  void savePlan(View view){
        planText=binding.txtPlanText.getText().toString();
        title=binding.txtPlanTitle.getText().toString();
        int day=days_start;
        int month=months_start;
        int year=years_start;

        HashMap<String,Object> planData= new HashMap<>();
        planData.put("Email",authU.getCurrentUser().getEmail());
        //planData.put("planClock",0);
        //planData.put("planMinute",0);
        planData.put("planDay",day);
        planData.put("planMonth",month);
        planData.put("planYear",year);
        planData.put("planTitle",title);
        planData.put("planText",planText);
        planData.put("checkBox",false);
        firestoreU.collection("Plans").add(planData) .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(CreatePlans.this,PlanList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CreatePlans.this, "upss bro!", Toast.LENGTH_SHORT).show();
            }
        });





    }

    public  void btn_delete(View view){
        Intent intent=getIntent();
        String id= intent.getStringExtra("id");



        firestoreU.collection("Plans").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent= new Intent(CreatePlans.this,PlanList.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }
}