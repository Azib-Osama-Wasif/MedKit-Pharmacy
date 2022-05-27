package com.fyp22.medkitpharmacy.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class SelectUserType extends AppCompatActivity {

    TextInputEditText userCategory;
    Button next;
    TextView register;
    MyAdminPreferences adminPreferences;
    MyPharmacyPreferences pharmacyPreferences;
    MyCustomerPreferences customerPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        userCategory= findViewById(R.id.user_category_input);
        next=findViewById(R.id.next);
        register=findViewById(R.id.register);

        adminPreferences =new MyAdminPreferences(this);
        customerPreferences =new MyCustomerPreferences(this);
        pharmacyPreferences = new MyPharmacyPreferences(this);


//        if(customerPreferences.getLogin()) {
//            Intent intent = new Intent(SelectUserType.this, Dashboard.class);
//            startActivity(intent);
//            return;
//        }
//        if(pharmacyPreferences.getLogin()) {
//            Intent intent = new Intent(SelectUserType.this, DashboardPharmacy.class);
//            startActivity(intent);
//            return;
//        }
//        if(adminPreferences.getLogin()) {
//            Intent intent = new Intent(SelectUserType.this, DashboardAdmin.class);
//            startActivity(intent);
//            return;
//        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userCategory.getText().toString())
                {
                    case Utilities.user_pharmacy:
                    {
                        startActivity(new Intent(SelectUserType.this, RegisterPharmacy.class));
                        break;
                    }
                    case Utilities.user_customer:
                    {
                        startActivity(new Intent(SelectUserType.this, Register.class));
                        break;
                    }
                    case Utilities.user_admin:
                    {
                        Toast.makeText(SelectUserType.this, "ADMIN CANNOT SIGN UP", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userCategory.getText().toString())
                {
                    case Utilities.user_pharmacy:
                    {
                        if(pharmacyPreferences.getLogin()){
                            startActivity(new Intent(SelectUserType.this, DashboardPharmacy.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SelectUserType.this, Login.class);
                            intent.putExtra(Utilities.intent_user_category,userCategory.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    }
                    case Utilities.user_customer:
                    {
                        if(customerPreferences.getLogin()){
                            startActivity(new Intent(SelectUserType.this, Dashboard.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SelectUserType.this, Login.class);
                            intent.putExtra(Utilities.intent_user_category,userCategory.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    }
                    case Utilities.user_admin:
                    {
                        if(adminPreferences.getLogin()){
                            startActivity(new Intent(SelectUserType.this, DashboardAdmin.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SelectUserType.this, Login.class);
                            intent.putExtra(Utilities.intent_user_category,userCategory.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    }
                }
            }
        });
    }

    public void showUserCategories(View v){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                userCategory.setText(item.getTitle().toString());
                return true;
            }
        });
        popup.inflate(R.menu.user_category_popup_menu);
        popup.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}