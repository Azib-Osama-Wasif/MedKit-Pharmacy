package com.fyp22.medkitpharmacy.Customer.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Customer;

public class MyCustomerPreferences {

    static Context context;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    static String undefined="Undefined";

    public MyCustomerPreferences(Context context) {
        this.context=context;
        this.preferences = context.getSharedPreferences(Utilities.customer_preferences, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public static void setLogin(boolean login){

        editor.putBoolean(Utilities.LOGIN,login);
        editor.commit();
    }

    public static boolean getLogin(){
        return preferences.getBoolean(Utilities.LOGIN,false);
    }


    public static void saveCustomer(Customer customer){

        if(!(customer ==null)) {

            editor.putString(Utilities.key_id, customer.getId());
            editor.putString(Utilities.key_name, customer.getName());
            editor.putString(Utilities.key_email, customer.getEmail());
            editor.putString(Utilities.key_address, customer.getAddress());
            editor.putString(Utilities.key_contact, customer.getContact());
            editor.putString(Utilities.key_cnic, customer.getCnic());
            editor.putString(Utilities.key_image, customer.getImage());
            editor.putString(Utilities.key_password, customer.getPassword());
            editor.putString(Utilities.key_city, customer.getCity());

            setLogin(true);

            editor.commit();
        }
        else{
            Utilities.createAlertDialog(context, Utilities.save_customer_failed_utility,Utilities.null_object_utility);
        }
    }
    public static Customer getCustomer(){

        Customer customer = new Customer();
        customer.setId(preferences.getString(Utilities.key_id, undefined ));
        customer.setName(preferences.getString(Utilities.key_name, undefined));
        customer.setEmail(preferences.getString(Utilities.key_email, undefined));
        customer.setAddress(preferences.getString(Utilities.key_address, undefined));
        customer.setContact(preferences.getString(Utilities.key_contact, undefined));
        customer.setCnic(preferences.getString(Utilities.key_cnic, undefined));
        customer.setImage(preferences.getString(Utilities.key_image, undefined));
        customer.setCity(preferences.getString(Utilities.key_city, undefined));
        customer.setPassword(preferences.getString(Utilities.key_password, undefined));

        return customer;
    }
}
