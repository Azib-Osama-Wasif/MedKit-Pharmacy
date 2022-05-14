package com.fyp22.medkitpharmacy.Pharmacy.Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Login;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Pharmacy;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class RegisterPharmacy extends AppCompatActivity {

    Button register;
    TextInputEditText nameET, emailET, cityET, dateET, addressET, phoneNoET, passwordET, confirmPasswordET;
    ImageView camera;
    ImageView mProfileImage;
    Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseHandler databaseHandler;
    ProgressDialog loadingBar;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pharmacy);

        auth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference(Utilities.user_pharmacy);
        databaseHandler = new DatabaseHandler(this);

        camera=findViewById(R.id.camera_icon);
        mProfileImage =findViewById(R.id.imageView);
        register=findViewById(R.id.register_btn);
        nameET = (TextInputEditText) findViewById(R.id.nameET);
        emailET = (TextInputEditText) findViewById(R.id.emailET);
        dateET = (TextInputEditText) findViewById(R.id.dateET);
        cityET = findViewById(R.id.cityET);
        addressET = (TextInputEditText) findViewById(R.id.addressET);
        phoneNoET= (TextInputEditText) findViewById(R.id.phoneNoET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);

        loadingBar = new ProgressDialog(this);

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileChooser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emptyCheck())
                {
                    Toast.makeText(RegisterPharmacy.this, "PLEASE FILL ALL FIELDS", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validName() && validPhone() && validateEmailAddress() && validConfirmPassword())
                    if(mImageUri !=null) {
                        loadingBar.setTitle("PHARMACY REGISTRATION");
                        loadingBar.setMessage("Please wait while we are registering Your Data");
                        loadingBar.show();

                        auth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    uploadProfile();
                                } else {
                                    Utilities.createAlertDialog(RegisterPharmacy.this,"Something went wrong",task.getException().toString());
                                    loadingBar.dismiss();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(RegisterPharmacy.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void uploadProfile() {
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));
        fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();

                                Pharmacy pharmacy = new Pharmacy();
                                pharmacy.setId(auth.getCurrentUser().getUid());
                                pharmacy.setName(nameET.getText().toString());
                                pharmacy.setEmail(emailET.getText().toString());
                                pharmacy.setPassword(passwordET.getText().toString());
                                pharmacy.setContact(phoneNoET.getText().toString());
                                pharmacy.setCity(cityET.getText().toString());
                                pharmacy.setAddress(addressET.getText().toString());
                                pharmacy.setImage(downloadUrl);

                                databaseHandler
                                        .getPharmacyReference()
                                        .child(auth.getCurrentUser().getUid().toString())
                                        .setValue(pharmacy)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    user = auth.getCurrentUser();
                                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Utilities.createAlertDialog(RegisterPharmacy.this, "Notice", "A verification email has been sent to " + emailET.getText().toString());
                                                            startActivity(new Intent(RegisterPharmacy.this, Login.class).putExtra(Utilities.intent_user_category,Utilities.user_pharmacy));
                                                            loadingBar.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Utilities.createAlertDialog(RegisterPharmacy.this, "Error", e.getMessage().toString());
                                                            loadingBar.dismiss();
                                                        }
                                                    });
                                                }
                                                else{
                                                    Utilities.createAlertDialog(RegisterPharmacy.this,"Something went wrong",task.getException().toString());
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPharmacy.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                            dateET.setText(newDateString);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void openProfileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mProfileImage);
        }
    }

    public void showCityPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                cityET.setText(item.getTitle().toString());
                return true;
            }
        });
        popup.inflate(R.menu.city_popup_menu);
        popup.show();
    }

    private boolean emptyCheck()
    {
        if(emailET.getText().toString().isEmpty()
                || nameET.getText().toString().isEmpty()
                || passwordET.getText().toString().isEmpty()
                || confirmPasswordET.getText().toString().isEmpty()
                || phoneNoET.getText().toString().isEmpty()
                || dateET.getText().toString().isEmpty()
                || cityET.getText().toString().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateEmailAddress()
    {
        String email= emailET.getText().toString().trim();
        if(email.isEmpty())             //Using method isEmpty()
        {
            emailET.setError("Email is required. Can't be empty");                 //Setting up an error
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailET.setError("Invalid Email Address. Enter valid Email Address");
            return false;
        }
        else
        {
            emailET.setError(null);
            return true;
        }
    }

    private boolean validPassword()
    {
        String password= passwordET.getText().toString().trim();


        if(password.isEmpty() )
        {
            passwordET.setError("Password is required. Can't be empty");
            return false;
        }
        else if(!Utilities.PASSWORD_UPPERCASE_PATTERN.matcher(password).matches())
        {
            passwordET.setError("Password is weak.Minimum one Upper-Case character is required.");
            return false;
        }
        else if(!Utilities.PASSWORD_LOWERCASE_PATTERN.matcher(password).matches())
        {
            passwordET.setError("Password is weak.Minimum one Lower-Case character is required.");
            return false;
        }
        else if(!Utilities.PASSWORD_NUMBER_PATTERN.matcher(password).matches())
        {
            passwordET.setError("Password is weak. Minimum one digit is required.");
            return false;
        }
        else
        {
            passwordET.setError(null);
            return true;
        }

    }

    private boolean validName()
    {
        String name= nameET.getText().toString().trim();
        if(name.isEmpty())
        {
            nameET.setError("Name is required. Can't be empty");
            return false;
        }
        return true;
    }

    private boolean validPhone()
    {
        String phone= phoneNoET.getText().toString().trim();
        if(phone.isEmpty())
        {
            phoneNoET.setError("Phone Number is required. Can't be empty");
            return false;
        }else if(phone.length()!=11)
        {
            phoneNoET.setError("Enter a valid 11 digit phone number");
            return false;
        }
        return true;
    }

    private boolean validConfirmPassword(){
        String confirmPassword= confirmPasswordET.getText().toString().trim();
        String password= passwordET.getText().toString().trim();
        if(confirmPassword.isEmpty() )
        {
            confirmPasswordET.setError("No Field can be empty kindly confirm your password");
        } else if(!confirmPassword.equals(password))
        {
            confirmPasswordET.setError("Password don't match");
            return false;
        }
        return true;
    }

}