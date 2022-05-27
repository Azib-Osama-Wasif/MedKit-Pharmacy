package com.fyp22.medkitpharmacy.Admin.Medicine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMedicine extends AppCompatActivity {

    TextInputEditText nameET;
    TextInputEditText descriptionET;
    TextInputEditText priceET;
    TextInputEditText typeET;
    TextInputEditText expDateET;
    TextInputEditText manufacturerET;
    CircleImageView imageView;
    Button submit;

    StorageReference storageReference;
    DatabaseHandler databaseHandler;
    ImageView camera;
    ImageView image;
    Uri mImageUri;

    ProgressDialog loadingBar;
    private static final int PICK_IMAGE_REQUEST = 1;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        databaseHandler= new DatabaseHandler(this);
        storageReference = FirebaseStorage.getInstance().getReference(Utilities.node_medicine);
        camera=findViewById(R.id.camera_icon);
        nameET=findViewById(R.id.nameET);
        typeET =findViewById(R.id.typeET);
        expDateET =findViewById(R.id.dateET);
        manufacturerET =findViewById(R.id.manufacturerET);
        descriptionET=findViewById(R.id.descriptionET);
        priceET=findViewById(R.id.priceET);
        imageView=findViewById(R.id.imageView);
        submit=findViewById(R.id.submit_btn);
        image =findViewById(R.id.imageView);
        loadingBar = new ProgressDialog(this);

        expDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mImageUri !=null) {
                    if(emptyCheck()) {
                        Toast.makeText(AddMedicine.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        loadingBar.hide();
                    }
                    else {
                        loadingBar.setTitle("UPLOADING MEDICINE");
                        loadingBar.setMessage("Please wait while your medicine is uploading");
                        loadingBar.show();
                        uploadMedicine();
                    }
                }
                else
                {
                    Toast.makeText(AddMedicine.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void uploadMedicine()
    {
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));
        fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();

                                Medicine medicine = new Medicine();
                                medicine.setName(nameET.getText().toString());
                                medicine.setType(typeET.getText().toString());
                                medicine.setPrice(Integer.parseInt(priceET.getText().toString()));
                                medicine.setDescription(descriptionET.getText().toString());
                                medicine.setImage(downloadUrl);
                                medicine.setExpDate(expDateET.getText().toString());
                                medicine.setManufacturer(manufacturerET.getText().toString());

                                databaseHandler.getMedicinesReference().child(medicine.getType())
                                        .push()
                                        .setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Toast.makeText(AddMedicine.this, "MEDICINE UPLOADED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();
                                            databaseHandler.getMedicinesReference().child(medicine.getType())
                                                    .addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            Medicine medicine1 = snapshot.getValue(Medicine.class);
                                                            System.out.println("MEDICINE NAME:"+medicine1.getName());
                                                            medicine1.setId(snapshot.getKey());
                                                            System.out.println("MEDICINE ID:"+medicine1.getId());
                                                            databaseHandler
                                                                    .getMedicinesReference()
                                                                    .child(medicine1.getType())
                                                                    .child(medicine1.getId())
                                                                    .setValue(medicine1)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            Intent intent = new Intent(AddMedicine.this, ViewMedicineAdmin.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                        }

                                                        @Override
                                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddMedicine.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void openProfileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(image);
        }
    }

    public boolean emptyCheck(){
        if(nameET.getText().toString().isEmpty() ||
                priceET.getText().toString().isEmpty() ||
                typeET.getText().toString().isEmpty() ||
                descriptionET.getText().toString().isEmpty())
        {
            return true;
        }
        return false;
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

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        try {
                            date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.DATE_TIME_FORMAT, Locale.ENGLISH);
                            LocalDate newDate = LocalDate.of(year,monthOfYear+1,dayOfMonth);
                            String newDateString = newDate.format(formatter);
                            expDateET.setText(newDateString);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}