package com.fyp22.medkitpharmacy.Pharmacy.Common;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class EditProfilePharmacy extends AppCompatActivity {

    private TextInputLayout
            mName,
            mPhone,
            mAddress,
            mCity,
            mDate;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    String name,address,phone, date,city;

    DatabaseHandler databaseHandler;
    ProgressBar progressBar;
    MyPharmacyPreferences mphp;
    Button editButton;
    Pharmacy admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharmacy_profile);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle("Edit Profile");
        editButton = findViewById(R.id.edit_btn);
        databaseHandler= new DatabaseHandler(this);
        mphp = new MyPharmacyPreferences(this);
        admin = mphp.getPharmacy();

        mName = findViewById(R.id.tx_name);
        mPhone = findViewById(R.id.tx_phone);
        mAddress = findViewById(R.id.tx_address);
        mDate = findViewById(R.id.tx_cnic);
        mCity = findViewById(R.id.tx_city);

        mName.getEditText().setText(admin.getName());
        mPhone.getEditText().setText(admin.getContact());
        mDate.getEditText().setText(admin.getDateEstablished());
        mAddress.getEditText().setText(admin.getAddress());
        mCity.getEditText().setText(admin.getCity());

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(     !validName() |
                        !validPhone() |
                        !(validCnic(mDate.getEditText().getText().toString())))
                {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                name = mName.getEditText().getText().toString();
                address = mAddress.getEditText().getText().toString();
                phone = mPhone.getEditText().getText().toString();
                date = mDate.getEditText().getText().toString();
                city = mCity.getEditText().getText().toString();

                admin.setName(name);
                admin.setContact(phone);
                admin.setDateEstablished(date);
                admin.setAddress(address);
                admin.setCity(city);
                admin.setPassword(mphp.getPharmacy().getPassword());

                databaseHandler.getPharmacyReference().child(admin.getId()).setValue(admin)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(EditProfilePharmacy.this, "EDITED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    mphp.savePharmacy(admin);
                                    startActivity(new Intent(EditProfilePharmacy.this, ViewProfilePharmacy.class));
                                }
                            }
                        });
            }
        });
    }

    private boolean validName()
    {
        String name= mName.getEditText().getText().toString().trim();
        if(name.isEmpty())
        {
            mName.setError("Name is required. Can't be empty");
            return false;
        }
        return true;
    }

    private boolean validPhone()
    {
        String phone= mPhone.getEditText().getText().toString().trim();
        if(phone.isEmpty())
        {
            mPhone.setError("Phone Number is required. Can't be empty");
            return false;
        }else if(phone.length()!=11)
        {
            mPhone.setError("Enter a valid 11 digit phone number");
            return false;
        }
        mPhone.setErrorEnabled(false);
        return true;
    }

    public boolean validCnic(String cnic){
        if(Utilities.isInteger(cnic)){
            if(cnic.length()==13){
                return true;
            }
            else{
                mDate.getEditText().setError("length must be 13");
                return false;
            }
        }
        else{
            mDate.getEditText().setError("Write integers only");
            return false;
        }
    }

    public void showCityPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Islamabad:
                    case R.id.Rawalpindi:
                        mCity.getEditText().setText(item.getTitle().toString());
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        popup.inflate(R.menu.city_popup_menu);
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
                            mDate.getEditText().setText(newDateString);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}