package com.example.medkitpharmacy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    TextView GotoSignIn;
    Button signBtn;
    EditText usermail,pwd,cnfpwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signBtn = findViewById(R.id.btn_Signup);
        usermail= findViewById(R.id.etEmail);
        pwd=findViewById(R.id.etPassword);
        cnfpwd=findViewById(R.id.etCnfrPassword);
        mAuth=FirebaseAuth.getInstance();

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail=usermail.getText().toString();
                String pass=pwd.getText().toString();
                String confirm=cnfpwd.getText().toString();

                if(!pass.equals(confirm))
                {
                    Toast.makeText(SignActivity.this, "Password should be same", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(mail) && TextUtils.isEmpty(mail)  && TextUtils.isEmpty(pass) && TextUtils.isEmpty(confirm))
                {
                    Toast.makeText(SignActivity.this, "Please input all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SignActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignActivity.this, SignInActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SignActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });



        //---------------moving towards signIn Activity--------------------------
        GotoSignIn = findViewById(R.id.tvalreadyaccount);
        GotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.medkitpharmacy.SignActivity.this, com.example.medkitpharmacy.SignInActivity.class);
                startActivity(intent);

            }

        });





    }
}