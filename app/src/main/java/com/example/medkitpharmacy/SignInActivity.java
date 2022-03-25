package com.example.medkitpharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    TextView backtoSignUp,usermail,pwd;
    Button signin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);



                //---------------moving towards signUp Activity--------------------------
        backtoSignUp = findViewById(R.id.tvclickforSignup);
        signin=findViewById(R.id.btn_SignIn);
        mAuth = FirebaseAuth.getInstance();
        usermail=findViewById(R.id.etEmail);
        pwd=findViewById(R.id.etPassword);

        backtoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this, SignActivity.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username=usermail.getText().toString();
                String password=pwd.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignInActivity.this, "Please Enter Your Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignInActivity.this, DashBoardActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });


    }
}