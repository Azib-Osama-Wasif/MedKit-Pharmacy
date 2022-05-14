package com.fyp22.medkitpharmacy.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Admin.Common.MyAdminPreferences;
import com.fyp22.medkitpharmacy.Admin.Dashboard.DashboardAdmin;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Dashboard.Dashboard;
import com.fyp22.medkitpharmacy.Customer.Common.Register;
import com.fyp22.medkitpharmacy.Pharmacy.Dashboard.DashboardPharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Common.RegisterPharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    static Context context;

    Intent intent;
    String userType;

    TextInputEditText email;
    TextInputEditText password;

    Button login;
    TextView title;
    TextView signUp;
    TextView signUpText;
    public static ProgressBar progressBar;

    DatabaseHandler databaseHandler;
    static MyAdminPreferences adminPreferences;
    static MyPharmacyPreferences pharmacyPreferences;
    static MyCustomerPreferences customerPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        title=findViewById(R.id.title);
        email=findViewById(R.id.emailET);
        password=findViewById(R.id.passwordET);
        login=findViewById(R.id.loginBTN);
        signUp=findViewById(R.id.signUpText);
        signUpText=findViewById(R.id.signUpText1);

        context=this;
        intent=getIntent();
        userType=intent.getStringExtra(Utilities.intent_user_category);
        title.setText("Login "+userType);
        databaseHandler= new DatabaseHandler(this);
        adminPreferences = new MyAdminPreferences(this);
        pharmacyPreferences = new MyPharmacyPreferences(this);
        customerPreferences = new MyCustomerPreferences(this);

        if(userType.equals(Utilities.user_admin)){
            signUpText.setVisibility(View.GONE);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType)
                {
                    case Utilities.user_pharmacy:
                    {
                        startActivity(new Intent(Login.this, RegisterPharmacy.class));
                        break;
                    }
                    case Utilities.user_customer:
                    {
                        startActivity(new Intent(Login.this, Register.class));
                        break;
                    }
                    case Utilities.user_admin:
                    {
                        Toast.makeText(context, "ADMIN CANNOT SIGN UP", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    switch (userType) {
                        case Utilities.user_pharmacy: {
                            databaseHandler.loginPharmacy(email.getText().toString(), password.getText().toString());
                            break;
                        }
                        case Utilities.user_customer: {
                            databaseHandler.loginCustomer(email.getText().toString(), password.getText().toString());
                            break;
                        }
                        case Utilities.user_admin:
                        {
                            databaseHandler.loginAdmin(email.getText().toString(), password.getText().toString());
                            break;
                        }
                    }
                }
                else{
                    Toast.makeText(Login.this, "PLEASE FILL ALL FIELDS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void getPharmacyLoginData(){
        progressBar.setVisibility(View.INVISIBLE);
//        Intent intent = new Intent(context, DashboardPharmacy.class);
//        intent.putExtra(Utilities.intent_pharmacy, pharmacyPreferences.getPharmacy());
        context.startActivity(new Intent(context, DashboardPharmacy.class));
        if (MyPharmacyPreferences.getLogin()){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static void getAdminLoginData(){
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, DashboardAdmin.class);
        intent.putExtra(Utilities.intent_admin, adminPreferences.getAdmin());
        context.startActivity(intent);
        if (MyAdminPreferences.getLogin()){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static void getCustomerLoginData(){

        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, Dashboard.class);
        intent.putExtra(Utilities.intent_customer, customerPreferences.getCustomer());
        context.startActivity(intent);
        if (MyCustomerPreferences.getLogin()){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SelectUserType.class));
    }
}