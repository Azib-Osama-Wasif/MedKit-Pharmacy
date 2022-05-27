package com.fyp22.medkitpharmacy.Customer.Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp22.medkitpharmacy.Common.DatabaseHandler;
import com.fyp22.medkitpharmacy.Common.Login;
import com.fyp22.medkitpharmacy.Common.Utilities;
import com.fyp22.medkitpharmacy.Models.Customer;
import com.fyp22.medkitpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Register extends AppCompatActivity {

    Button register;
    TextInputEditText nameET, emailET, cnicET,cityET, addressET, phoneNoET, passwordET, confirmPasswordET;
    ImageView camera;
    ImageView mProfileImage;
    Uri mImageUri;
    ProgressDialog loadingBar;

    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseHandler databaseHandler;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference(Utilities.user_customer);
        databaseHandler = new DatabaseHandler(this);

        camera=findViewById(R.id.camera_icon);
        mProfileImage =findViewById(R.id.imageView);
        register=findViewById(R.id.register_btn);
        nameET = (TextInputEditText) findViewById(R.id.nameET);
        emailET = (TextInputEditText) findViewById(R.id.emailET);
        cnicET = (TextInputEditText) findViewById(R.id.cnicET);
        cityET = findViewById(R.id.cityET);
        addressET = (TextInputEditText) findViewById(R.id.addressET);
        phoneNoET= (TextInputEditText) findViewById(R.id.phoneNoET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);

        loadingBar = new ProgressDialog(this);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileChooser();
            }
        });
        mProfileImage.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Register.this, "FILL ALL FIELDS!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(validName() || validPhone() || validateEmailAddress() || validPassword() || validConfirmPassword() || validCnic(cnicET.getText().toString())){
                    return;
                }
                if(mImageUri !=null) {
                    loadingBar.setTitle("RECEIVER REGISTRATION");
                    loadingBar.setMessage("Please wait while we are registering Your Data");
                    loadingBar.show();

                    auth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                uploadProfile();
                            } else {
                                Utilities.createAlertDialog(Register.this,"Something went wrong",task.getException().toString());
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Register.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

                                Customer customer = new Customer();
                                customer.setId(auth.getCurrentUser().getUid());
                                customer.setName(nameET.getText().toString());
                                customer.setEmail(emailET.getText().toString());
                                customer.setPassword(passwordET.getText().toString());
                                customer.setContact(phoneNoET.getText().toString());
                                customer.setCnic(cnicET.getText().toString());
                                customer.setAddress(addressET.getText().toString());
                                customer.setImage(downloadUrl);

                                databaseHandler
                                        .getCustomersReference()
                                        .child(auth.getCurrentUser().getUid().toString())
                                        .setValue(customer)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user = auth.getCurrentUser();
                                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Utilities.createAlertDialog(Register.this, "Notice", "A verification email has been sent to " + emailET.getText().toString(), new DialogInterface.OnDismissListener() {
                                                                @Override
                                                                public void onDismiss(DialogInterface dialogInterface) {
                                                                    Intent intent = new Intent(Register.this, Login.class);
                                                                    intent.putExtra(Utilities.intent_user_category,Utilities.user_customer);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });
                                                            loadingBar.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Utilities.createAlertDialog(Register.this, "Error", e.getMessage().toString());
                                                            loadingBar.dismiss();
                                                        }
                                                    });
                                                } else {
                                                    Utilities.createAlertDialog(Register.this, "Something went wrong", task.getException().toString());
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
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            Toast.makeText(Register.this, mImageUri.toString(), Toast.LENGTH_SHORT).show();
            Picasso.get().load(mImageUri).into(mProfileImage);
        }
    }

    private boolean emptyCheck()
    {
        if(emailET.getText().toString().isEmpty()
                || nameET.getText().toString().isEmpty()
                || addressET.getText().toString().isEmpty()
                || cityET.getText().toString().isEmpty()
                || passwordET.getText().toString().isEmpty()
                || confirmPasswordET.getText().toString().isEmpty()
                || phoneNoET.getText().toString().isEmpty()
                || cnicET.getText().toString().isEmpty()){
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
            passwordET.setError("Password is weak.Mininmum one Upper-Case character is required.");
            return false;
        }
        else if(!Utilities.PASSWORD_LOWERCASE_PATTERN.matcher(password).matches())
        {
            passwordET.setError("Password is weak.Mininmum one Lower-Case character is required.");
            return false;
        }
        else if(!Utilities.PASSWORD_SPECIALCHARACTER_PATTERN.matcher(password).matches())
        {
            passwordET.setError("Password is weak.Mininmum one Special character is required.");
            return false;
        }
        else if(!Utilities.PASSWORD_NUMBER_PATTERN.matcher(password).matches())
        {
            passwordET.setError("Password is weak.Mininmum one digit character is required.");
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
        String confirmpassword= confirmPasswordET.getText().toString().trim();
        String password= confirmPasswordET.getText().toString().trim();
        if(confirmpassword.isEmpty() )
        {
            confirmPasswordET.setError("No Field can be empty kindly confirm your password");
        } else if(!confirmpassword.equals(password))
        {
            confirmPasswordET.setError("Password don't match kindly try again");
            return false;
        }
        return true;
    }
    public boolean validCnic(String cnic){
        if(Utilities.isInteger(cnic)){
            if(cnic.length()==13){
                return true;
            }
            else{
                cnicET.setError("length must be 13");
                return false;
            }
        }
        else{
            cnicET.setError("Write integers only");
            return false;
        }
    }
}