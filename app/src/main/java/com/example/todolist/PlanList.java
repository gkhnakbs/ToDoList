package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.databinding.ActivityPlanListBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.core.QueryListener;

import java.util.ArrayList;
import java.util.Map;


public class PlanList extends AppCompatActivity {


    Adapter adapter;
    ArrayList<Data> dataArrayList;
    private FirebaseAuth authU;
    private FirebaseFirestore firestoreU;


    private ActivityPlanListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPlanListBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);
        dataArrayList= new ArrayList<>();



        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new Adapter(dataArrayList);
        binding.recycleView.setAdapter(adapter);
        authU= FirebaseAuth.getInstance();
        firestoreU=FirebaseFirestore.getInstance();
        getData();
        getUserData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.crate_plan){
            Intent intent= new Intent(PlanList.this,CreatePlans.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.options_person){
            Toast.makeText(this, "UserOptions open", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(PlanList.this,UserOptions.class);
            startActivity(intent);
        } else if (item.getItemId()==R.id.sign_out) {
            authU.signOut();

            Intent signOutIntent= new Intent(PlanList.this,MainActivity.class);
            startActivity(signOutIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        firestoreU.collection("Plans").whereEqualTo("Email",authU.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            //@SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(PlanList.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(value!=null){
                    dataArrayList.clear();
                    for(DocumentSnapshot snapshot: value.getDocuments()){
                        Map<String,Object> data= snapshot.getData();

                        String id = snapshot.getId();
                        Boolean checkBox= (Boolean) data.get("checkBox");
                        String planText= (String) data.get("planText");
                        String planTitle=(String) data.get("planTitle");
                        String clock=  data.get("planClock").toString();
                        String minute= data.get("planMinute").toString();
                        String day=data.get("planDay").toString();
                        String month= data.get("planMonth").toString();
                        String year= data.get("planYear").toString();
                        int clockType= Integer.parseInt(clock);
                        int minuteType= Integer.parseInt(minute);
                        int dayType= Integer.parseInt(day);
                        int monthType= Integer.parseInt(month);
                        int yearType= Integer.parseInt(year);
                        Data dataPlan= new Data(planTitle,planText,dayType,yearType,monthType,clockType,minuteType,id,checkBox);

                        dataArrayList.add(dataPlan);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }
    public void getUserData(){
        firestoreU.collection("Users").whereEqualTo("Email",authU.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                String name="";
                String surname="";

                if(value!=null){
                    for(DocumentSnapshot snapshot: value.getDocuments()){
                        Map<String,Object> userData= snapshot.getData();
                        name= (String) userData.get("Name");
                        surname=(String) userData.get("Surname");
                     }
                    binding.txtPerson.setText(name+" "+surname);
                }
            }
        });
    }



}