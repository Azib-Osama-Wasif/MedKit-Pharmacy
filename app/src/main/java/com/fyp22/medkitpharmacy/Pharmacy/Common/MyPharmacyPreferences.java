package com.fyp22.medkitpharmacy.Pharmacy.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;

public class MyPharmacyPreferences {
    static Context context;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    static String undefined="Undefined";

    public MyPharmacyPreferences(Context context) {
        this.context=context;
        this.preferences = context.getSharedPreferences(Utilities.pharmacy_preferences, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public static void setLogin(boolean login){

        editor.putBoolean(Utilities.login_utility,login);
        editor.commit();
    }

    public static boolean getLogin(){
        return preferences.getBoolean(Utilities.login_utility,false);
    }


    public static void savePharmacy(Pharmacy pharmacy){

        if(!(pharmacy ==null)) {

            editor.putString(Utilities.key_id, pharmacy.getId());
            editor.putString(Utilities.key_name, pharmacy.getName());
            editor.putString(Utilities.key_email, pharmacy.getEmail());
            editor.putString(Utilities.key_address, pharmacy.getAddress());
            editor.putString(Utilities.key_contact, pharmacy.getContact());
            editor.putString(Utilities.key_password, pharmacy.getPassword());
            editor.putString(Utilities.key_estb_date, pharmacy.getDateEstablished());
            editor.putString(Utilities.key_image, pharmacy.getImage());

            setLogin(true);

            editor.commit();
        }
        else{
            Utilities.createAlertDialog(context, Utilities.save_pharmacy_failed_utility,Utilities.null_object_utility);
        }
    }
    public static Pharmacy getPharmacy(){

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(preferences.getString(Utilities.key_id, undefined ));
        pharmacy.setName(preferences.getString(Utilities.key_name, undefined));
        pharmacy.setEmail(preferences.getString(Utilities.key_email, undefined));
        pharmacy.setAddress(preferences.getString(Utilities.key_address, undefined));
        pharmacy.setContact(preferences.getString(Utilities.key_contact, undefined));
        pharmacy.setDateEstablished(preferences.getString(Utilities.key_estb_date, undefined));
        pharmacy.setImage(preferences.getString(Utilities.key_image, undefined));
        pharmacy.setPassword(preferences.getString(Utilities.key_password, undefined));

        return pharmacy;
    }
}
