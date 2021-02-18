package com.example.njudesigns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordCode extends AppCompatActivity {

    EditText emailId;
    Button verifing;
    FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(PasswordCode.this, Login.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_password_code);
        emailId = findViewById(R.id.email);
        verifing = findViewById(R.id.verify);
        firebaseAuth = FirebaseAuth.getInstance();

        if (CheckConnectivity.isInternetAvailable(PasswordCode.this)){
            verifing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = emailId.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PasswordCode.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PasswordCode.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

        }

        else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

}
