package com.fyp22.medkitpharmacy.Admin.Pharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Admin.Medicine.EditMedicine;
import com.fyp22.medkitpharmacy.Admin.Medicine.ViewMedicineAdmin;
import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class PharmacyMedicineDetailAdmin extends AppCompatActivity {

    CircleImageView imageView;
    TextView name;
    TextView name2;
    TextView price;
    TextView type;
    TextView description;
    ImageView editbtn;
    ImageView deletebtn;

    Intent intent;
    Medicine medicine;
    Pharmacy pharmacy;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_medicine_detail_admin);
        getSupportActionBar().setTitle("Medicine Detail");
        imageView=findViewById(R.id.imageView);
        name=findViewById(R.id.name_val);
        name2=findViewById(R.id.name_val2);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        type =findViewById(R.id.type_val);
        editbtn=findViewById(R.id.edit_button);
        deletebtn=findViewById(R.id.delete_button);

        intent=getIntent();
        medicine = (Medicine) intent.getSerializableExtra(Utilities.intent_medicine);
        pharmacy = (Pharmacy) intent.getSerializableExtra(Utilities.intent_pharmacy);
        databaseHandler= new DatabaseHandler(this);

        Picasso.get().load(medicine.getImage()).into(imageView);
        name.setText(medicine.getName());
        name2.setText(medicine.getName());
        type.setText(medicine.getType());
        description.setText(medicine.getDescription());
        price.setText(Integer.toString(medicine.getPrice()));

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PharmacyMedicineDetailAdmin.this, EditMedicine.class);
                intent.putExtra(Utilities.intent_medicine, (Serializable)medicine);
                startActivity(intent);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(PharmacyMedicineDetailAdmin.this)
                        .setTitle("Confirm Operation")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                // Continue with delete operation
                                databaseHandler.getMedicinesReference().child(medicine.getType()).child(medicine.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PharmacyMedicineDetailAdmin.this, "DELETION SUCCESSFUL", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(PharmacyMedicineDetailAdmin.this, ViewMedicineAdmin.class));
                                        }
                                        else{
                                            Toast.makeText(PharmacyMedicineDetailAdmin.this, "DELETION FAILED", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PharmacyMedicineDetailAdmin.this, ViewPharmaciesAdmin.class).putExtra(Utilities.intent_pharmacy, pharmacy));
    }
}