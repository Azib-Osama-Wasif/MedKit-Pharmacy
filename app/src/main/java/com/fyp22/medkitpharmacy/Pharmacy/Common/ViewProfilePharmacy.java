package com.fyp22.medkitpharmacy.Pharmacy.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Pharmacy.Dashboard.DashboardPharmacy;
import com.fyp22.medkitpharmacy.Common.SelectUserType;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfilePharmacy extends AppCompatActivity {

    Button make_changes;
    private CircleImageView profileImage;
    private TextView name, email, email2, phone, estDate, address,city, password, changePassword;

    Pharmacy pharmacy;
    MyPharmacyPreferences map;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pharmacy_profile);
        make_changes=findViewById(R.id.edit_btn);
        profileImage = findViewById(R.id.receiver_profile_image);
        name = findViewById(R.id.name_tv);
        email = findViewById(R.id.email_tv);
        email2 = findViewById(R.id.tv_email_2);
        phone = findViewById(R.id.tv_phone);
        estDate = findViewById(R.id.tv_cnic);
        address = findViewById(R.id.tv_address);
        city = findViewById(R.id.tv_city);
        password = findViewById(R.id.tv_password);
        changePassword = findViewById(R.id.change_password);

        map = new MyPharmacyPreferences(this);
        pharmacy = map.getPharmacy();
        auth = FirebaseAuth.getInstance();

        name.setText(pharmacy.getName());
        email.setText(pharmacy.getEmail());
        email2.setText(pharmacy.getEmail());
        address.setText(pharmacy.getAddress());
        estDate.setText(pharmacy.getDateEstablished());
        phone.setText(pharmacy.getContact());
        password.setText(pharmacy.getPassword());
        city.setText(pharmacy.getCity());
        Picasso.get().load(pharmacy.getImage()).into(profileImage);

        make_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfilePharmacy.this, EditProfilePharmacy.class));
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setTransformationMethod(null);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("A password reset link will be sent to "+ pharmacy.getEmail());

                passwordResetDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(pharmacy.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ViewProfilePharmacy.this, "Reset Link Sent To Your Email.", Toast.LENGTH_LONG).show();
                                MyPharmacyPreferences.setLogin(false);
                                Intent intent = new Intent(ViewProfilePharmacy.this, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_pharmacy);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewProfilePharmacy.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewProfilePharmacy.this, DashboardPharmacy.class));
        finish();
    }
}