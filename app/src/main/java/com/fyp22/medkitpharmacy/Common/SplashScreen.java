package com.fyp22.medkitpharmacy.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fyp22.medkitpharmacy.Admin.Common.MyAdminPreferences;
import com.fyp22.medkitpharmacy.Admin.Dashboard.DashboardAdmin;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Customer.Dashboard.Dashboard;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Dashboard.DashboardPharmacy;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;
import com.fyp22.medkitpharmacy.R;

import java.lang.reflect.Field;

public class SplashScreen extends AppCompatActivity {

    MyAdminPreferences map;
    MyPharmacyPreferences mphp;
    MyCustomerPreferences mcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        map =new MyAdminPreferences(this);
        mcp =new MyCustomerPreferences(this);
        mphp =new MyPharmacyPreferences(this);

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(1600);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(mphp.getLogin()){
                        startActivity(new Intent(SplashScreen.this, DashboardPharmacy.class));
                    }
                    else if(mcp.getLogin()){
                        startActivity(new Intent(SplashScreen.this, Dashboard.class));
                    }
                    else if(map.getLogin()){
                        startActivity(new Intent(SplashScreen.this, DashboardAdmin.class));
                    }
                    else{
                        Intent intent = new Intent(SplashScreen.this, SelectUserType.class);
                        startActivity(intent);
                    }
                }
            }
        };
        thread.start();
    }
}