package com.example.todolist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.databinding.RecycleRowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Adapter  extends RecyclerView.Adapter<Adapter.ListHolder> {
    private FirebaseFirestore firestoreU;
    ArrayList<Data> arrayList;

    public Adapter(ArrayList<Data> arrayList) {

        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        firestoreU=FirebaseFirestore.getInstance();
        RecycleRowBinding recycleRowBinding= RecycleRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ListHolder(recycleRowBinding);


    }


    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {

        holder.recycleRowBinding.textView.setText(arrayList.get(position).planTitle);
        holder.recycleRowBinding.checkBox.setChecked(arrayList.get(position).checkBox);
        holder.recycleRowBinding.checkBox.setOnClickListener(v -> {
             if(holder.recycleRowBinding.checkBox.isChecked()){
                 firestoreU.collection("Plans").document(arrayList.get(position).id).update("checkBox",true);
             }
             else{
                 firestoreU.collection("Plans").document(arrayList.get(position).id).update("checkBox",false);
             }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(holder.itemView.getContext(),CreatePlans.class);
                intent.putExtra("info","old");
                intent.putExtra("planTitle",arrayList.get(position).planTitle);
                intent.putExtra("planText",arrayList.get(position).planText);
                intent.putExtra("clock",arrayList.get(position).start_clock);
                intent.putExtra("minute",arrayList.get(position).start_minute);
                intent.putExtra("day",arrayList.get(position).start_day);
                intent.putExtra("month",arrayList.get(position).start_month);
                intent.putExtra("year",arrayList.get(position).start_year);
                intent.putExtra("Email",arrayList.get(position).email);
                intent.putExtra("id",arrayList.get(position).id);

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class ListHolder extends  RecyclerView.ViewHolder{

        RecycleRowBinding recycleRowBinding;

        public ListHolder(RecycleRowBinding recycleRowBinding) {
            super(recycleRowBinding.getRoot());
            this.recycleRowBinding=recycleRowBinding;

        }

    }



}
