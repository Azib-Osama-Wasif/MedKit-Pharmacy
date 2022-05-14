package com.fyp22.medkitpharmacy.Admin.Medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class EditMedicine extends AppCompatActivity {

    TextInputEditText nameET;
    TextInputEditText descriptionET;
    TextInputEditText priceET;
    TextInputEditText typeET;
    TextInputEditText manufacturerET;
    TextInputEditText expDateET;
    Button submit;
    ProgressBar progressBar;

    DatabaseHandler databaseHandler;
    Intent intent;
    Medicine medicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        databaseHandler= new DatabaseHandler(this);
        intent=getIntent();
        medicine = (Medicine) intent.getSerializableExtra(Utilities.intent_medicine);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        nameET=findViewById(R.id.nameET);
        descriptionET=findViewById(R.id.descriptionET);
        priceET=findViewById(R.id.priceET);
        expDateET=findViewById(R.id.dateET);
        manufacturerET=findViewById(R.id.manufacturerET);
        typeET=findViewById(R.id.typeET);
        submit=findViewById(R.id.submit_btn);

        nameET.setText(medicine.getName());
        descriptionET.setText(medicine.getDescription());
        manufacturerET.setText(medicine.getManufacturer());
        typeET.setText(medicine.getType());
        expDateET.setText(medicine.getExpDate());
        priceET.setText(Integer.toString(medicine.getPrice()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameET.getText().toString().isEmpty() ||
                        descriptionET.getText().toString().isEmpty() ||
                        priceET.getText().toString().isEmpty())
                {
                    Toast.makeText(EditMedicine.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);

                    medicine.setName(nameET.getText().toString());
                    medicine.setPrice(Integer.parseInt(priceET.getText().toString()));
                    medicine.setDescription(descriptionET.getText().toString());
                    System.out.println(medicine.getType());
                    System.out.println(medicine.getId());
                    try {
                        databaseHandler.getMedicinesReference().child(medicine.getType()).child(medicine.getId()).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(EditMedicine.this, EditMedicine.class));
                                    Toast.makeText(EditMedicine.this, "Medicine updated successfully", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(EditMedicine.this, "FAILED TO UPDATE", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    catch (Exception exception){
                        Utilities.createAlertDialog(EditMedicine.this, "FAILED", exception.getMessage());
                    }
                }
            }
        });
    }

    public void showTypePopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                typeET.setText(item.getTitle().toString());
                return true;
            }
        });
        popup.inflate(R.menu.medicine_type_popup_menu);
        popup.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}