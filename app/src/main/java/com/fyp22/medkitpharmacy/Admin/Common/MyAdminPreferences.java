package com.fyp22.medkitpharmacy.Admin.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Admin;

public class MyAdminPreferences {

    static Context context;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    static String undefined="Undefined";

    public MyAdminPreferences(Context context) {
        this.context=context;
        this.preferences = context.getSharedPreferences(Utilities.admin_preferences, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public static void setLogin(boolean login){

        editor.putBoolean(Utilities.LOGIN,login);
        editor.commit();
    }

    public static boolean getLogin(){
        return preferences.getBoolean(Utilities.LOGIN,false);
    }


    public static void saveAdmin(Admin admin){

        if(!(admin ==null)) {

            editor.putString(Utilities.key_id, admin.getId());
            editor.putString(Utilities.key_name, admin.getName());
            editor.putString(Utilities.key_email, admin.getEmail());
            editor.putString(Utilities.key_address, admin.getAddress());
            editor.putString(Utilities.key_contact, admin.getContact());
            editor.putString(Utilities.key_cnic, admin.getCnic());
            editor.putString(Utilities.key_image, admin.getImage());
            editor.putString(Utilities.key_password, admin.getPassword());
            editor.putString(Utilities.key_city, admin.getCity());

            setLogin(true);

            editor.commit();
        }
        else{
            Utilities.createAlertDialog(context, Utilities.save_admin_failed_utility,Utilities.null_object_utility);
        }
    }
    public static Admin getAdmin(){

        Admin admin = new Admin();
        admin.setId(preferences.getString(Utilities.key_id, undefined ));
        admin.setName(preferences.getString(Utilities.key_name, undefined));
        admin.setEmail(preferences.getString(Utilities.key_email, undefined));
        admin.setAddress(preferences.getString(Utilities.key_address, undefined));
        admin.setContact(preferences.getString(Utilities.key_contact, undefined));
        admin.setCnic(preferences.getString(Utilities.key_cnic, undefined));
        admin.setImage(preferences.getString(Utilities.key_image, undefined));
        admin.setCity(preferences.getString(Utilities.key_city, undefined));
        admin.setPassword(preferences.getString(Utilities.key_password, undefined));

        return admin;
    }
}
