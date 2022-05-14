package com.fyp22.medkitpharmacy.Customer.Common;

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

import com.fyp22.medkitpharmacy.Common.SelectUserType;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Customer.Dashboard.Dashboard;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity {

    Button make_changes;
    private CircleImageView profileImage;
    private TextView name, email, email2, phone, cnic, address,city, password, changePassword;

    Customer customer;
    MyCustomerPreferences mcp;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        make_changes=findViewById(R.id.edit_btn);
        profileImage = findViewById(R.id.receiver_profile_image);
        name = findViewById(R.id.name_tv);
        email = findViewById(R.id.email_tv);
        email2 = findViewById(R.id.tv_email_2);
        phone = findViewById(R.id.tv_phone);
        cnic = findViewById(R.id.tv_cnic);
        address = findViewById(R.id.tv_address);
        city = findViewById(R.id.tv_city);

        password = findViewById(R.id.tv_password);
        changePassword = findViewById(R.id.change_password);

        mcp = new MyCustomerPreferences(this);
        customer = mcp.getCustomer();
        auth = FirebaseAuth.getInstance();

        name.setText(customer.getName());
        email.setText(customer.getEmail());
        email2.setText(customer.getEmail());
        address.setText(customer.getAddress());
        cnic.setText(customer.getCnic());
        phone.setText(customer.getContact());
        password.setText(customer.getPassword());
        city.setText(customer.getCity());
        Picasso.get().load(customer.getImage()).into(profileImage);

        make_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfile.this, EditProfile.class));
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
                passwordResetDialog.setMessage("A password reset link will be sent to "+ customer.getEmail());

                passwordResetDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(customer.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ViewProfile.this, "Reset Link Sent To Your Email.", Toast.LENGTH_LONG).show();
                                MyCustomerPreferences.setLogin(false);
                                Intent intent = new Intent(ViewProfile.this, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewProfile.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(ViewProfile.this, Dashboard.class));
        finish();
    }
}