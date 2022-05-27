package com.fyp22.medkitpharmacy.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.fyp22.medkitpharmacy.Admin.Common.MyAdminPreferences;
import com.fyp22.medkitpharmacy.Customer.Common.MyCustomerPreferences;
import com.fyp22.medkitpharmacy.Pharmacy.Common.MyPharmacyPreferences;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utilities {
    public static final String FAILED="FAILED";
    public static final String SAVE="SAVE";
    public static final String SAVED="SAVED";
    public static final String activity_name ="ACTIVITY NAME";

    /////////------------------ITEM CATEGORIES---------------------///////////
    public static final String SYRUP="Syrup";
    public static final String TABLET="Tablet";

    /////--------------------ORDER STATUS------------------////////
    public static final String order_complete = "Complete";
    public static final String order_pending = "Pending";
    public static final String order_dispatched = "Dispatched";

    /////--------------------DATE-TIME-FORMAT---------------------/////
    public static final String DATE_TIME_FORMAT="dd-MM-yyyy";
    @SuppressLint("NewApi")
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.DATE_TIME_FORMAT, Locale.ENGLISH);


    /////--------------------PASSWORD-PATTERN---------------------/////
    public static final Pattern PASSWORD_PATTERN=
            Pattern.compile("^"
                    +"(?=.*[0-9])"          //minimum 0ne number
                    +"(?=.*[a-z])"          //minimum 0ne lower-case character
                    +"(?=.*[A-Z])"          //minimum 0ne Upper-case character
                    +"(?=.*[a-zA-Z])"       //any character
                    +"(?=\\S+$)"            //no white spaces
                    +".{8,}"                //minimum length 8 characters
                    +"$");
    public static final Pattern PASSWORD_UPPERCASE_PATTERN=Pattern.compile("(?=.*[A-Z])"+".{1,}");
    public static final Pattern PASSWORD_LOWERCASE_PATTERN=Pattern.compile("(?=.*[a-z])"+".{1,}");
    public static final Pattern PASSWORD_NUMBER_PATTERN=Pattern.compile("(?=.*[0-9])"+".{1,}");
    public static final Pattern PASSWORD_SPECIALCHARACTER_PATTERN=Pattern.compile("(?=.*[@#$%^&+=])"+".{1,}");

    /////--------------------SHARED PREFERENCES------------------////////
    public static final String pharmacy_preferences = "PHARMACY_PREFERENCES";
    public static final String customer_preferences = "CUSTOMER_PREFERENCES";
    public static final String admin_preferences = "ADMIN_PREFERENCES";


    /////--------------------INTENTS------------------////////
    public static final String intent_order = "ORDER_OBJECT";
    public static final String intent_admin = "ADMIN_OBJECT";
    public static final String intent_pharmacy = "PHARMACY_OBJECT";
    public static final String intent_customer = "CUSTOMER_OBJECT";
    public static final String intent_medicine = "MEDICINE_OBJECT";
    public static final String intent_medicine_list = "MEDICINE_LIST";
    public static final String intent_cart_item_list = "CART_ITEM_LIST";
    public static final String intent_order_item_list = "ORDER_ITEM_LIST";
    public static final String intent_stock_item = "STOCK ITEM";
    public static final String intent_availability = "AVAILABILITY_OBJECT";
    public static final String intent_complaint = "COMPLAINT_OBJECT";
    public static final String intent_item_type = "ITEM_TYPE";
    public static final String intent_image_url = "IMAGE_URL";
    public static final String intent_image_url_list = "IMAGE_URL_LIST";
    public static final String intent_user_category = "USER_CATEGORY";
    public static final String intent_saved_status = "ITEM_SAVED_STATUS";
    public static String intent_stock ="STOCK";
    public static String intent_index ="INDEX";
    public static String intent_quantity ="QUANTITY";
    public static String intent_address="ADDRESS";

    /////--------------------USERS------------------////////
    public static final String user_pharmacy = "Pharmacy";
    public static final String user_customer = "Customer";
    public static final String user_admin = "Admin";


    /////--------------------DATABASE NODES------------------////////
    public static final String node_admin = "Admin";
    public static final String node_pharmacies = "Pharmacies";
    public static final String node_customers = "Customers";
    public static final String node_medicine = "Medicines";
    public static final String node_orders = "Orders";
    public static final String node_cart = "Cart";
    public static final String node_complaints = "Complaints";
    public static final String node_stock_items = "StockItems";



    /////--------------------SHARED PREFERENCES KEYS------------------////////
    public static final String key_id = "ID";
    public static final String key_name = "NAME";
    public static final String key_email = "EMAIL";
    public static final String key_contact = "CONTACT";
    public static final String key_password = "PASSWORD";
    public static final String key_image = "IMAGE";
    public static final String key_city = "CITY";
    public static final String key_cnic = "CNIC";
    public static final String key_exp_date = "EXPIRY DATE";
    public static final String key_estb_date = "DATE ESTABLISHED";
    public static final String key_address = "ADDRESS";
    public static final String key_blood_group = "BLOOD GROUP";
    public static final String key_medication_list = "MEDICATION LIST";


    /////--------------------UTILITY STRINGS------------------////////
    public static final String login_utility = "LOGIN";
    public static final String login_failed_utility = "LOGIN FAILED";
    public static final String save_admin_failed_utility = "SAVE ADMIN FAILED";
    public static final String save_customer_failed_utility = "SAVE CUSTOMER FAILED";
    public static final String save_pharmacy_failed_utility = "SAVE PHARMACY FAILED";
    public static final String null_object_utility = "Null Object Passed";
    public static final String LOGIN = "LOGIN";
    public static String type_tablets="Tablets";
    public static String type_syrups="Syrups";
    public static String type_capsules="Capsules";
    public static String type_all="All";
    public static String pending_utility="Pending";


    public static void createAlertDialog(Context context, String title, String text){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public static void createAlertDialog(Context context, String title, String text, DialogInterface.OnDismissListener dismissListener){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setOnDismissListener(dismissListener)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getCurrentDate(){
        return LocalDateTime.now().format(formatter).toString();
    }

    public static void createLogoutDialog(Context context, String userCategory){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Logout "+userCategory)
                .setMessage("Continue logging out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (userCategory)
                        {
                            case user_pharmacy:
                            {
                                MyPharmacyPreferences.setLogin(false);
                                System.out.println("PHARMACY LOGGED OUT: "+MyAdminPreferences.getLogin());
                                Intent intent = new Intent(context, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_pharmacy);
                                context.startActivity(intent);
                                break;
                            }
                            case user_customer:
                            {
                                MyCustomerPreferences.setLogin(false);
                                System.out.println("CUSTOMER LOGGED OUT: "+MyAdminPreferences.getLogin());
                                Intent intent = new Intent(context, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                                context.startActivity(intent);
                                break;
                            }
                            case user_admin:
                            {
                                MyAdminPreferences.setLogin(false);
                                System.out.println("ADMIN LOGGED OUT: "+MyAdminPreferences.getLogin());
                                Intent intent = new Intent(context, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_admin);
                                context.startActivity(intent);
                                break;
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static boolean isInteger(String str){
        if(str.matches("-?\\d+")){
            return true;
        }
        return false;
    }
}
