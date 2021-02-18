package com.example.njudesigns;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    TextInputLayout regfullname,regusername,regemail,regphone,regpass;
    Button regbtn,signup_login;
    FirebaseDatabase root;
    DatabaseReference reference;

    private Boolean validateName(){
        String value = regfullname.getEditText().getText().toString();
        if(value.isEmpty()){
            regfullname.setError("Field cannot be empty");
            return false;
        }
        else{
            regfullname.setError(null);
            return true;
        }
    }
    private Boolean validateUsername(){
        String value = regusername.getEditText().getText().toString();
        String noWhitespace = "\\A\\w{2,20}\\z";
        if(value.isEmpty()){
            regusername.setError("Field cannot be empty");
            return false;
        }
        else if(value.length()>15){
            regusername.setError("Username is too long");
            return false;
        }
        else if(!value.matches(noWhitespace)){
            regusername.setError("White spaces are not allowed");
            return false;
        }
        else{
            regusername.setError(null);
            regusername.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail(){
        String value = regemail.getEditText().getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(value.isEmpty()){
            regemail.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailpattern)) {
            regemail.setError("Invalid email address");
            return false;
        } else {
            regemail.setError(null);
            regemail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePhone(){
        String value = regphone.getEditText().getText().toString();
        if(value.isEmpty()){
            regphone.setError("Field cannot be empty");
            return false;
        }
        else{
            regphone.setError(null);
            return true;
        }
    }
    private Boolean validatePassword(){
        String value = regpass.getEditText().getText().toString();
        String passval = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if(value.isEmpty()){
            regpass.setError("Field cannot be empty");
            return false;
        }
        else if (!value.matches(passval)) {
            regpass.setError("Password is too weak");
            return false;
        }
        else{
            regpass.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(signup.this, Login.class));
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_signup);
        regfullname = findViewById(R.id.name);
        regusername = findViewById(R.id.usename);
        regemail = findViewById(R.id.email);
        regphone = findViewById(R.id.phone);
        regpass = findViewById(R.id.pass);
        regbtn = findViewById(R.id.reggo);
        signup_login = findViewById(R.id.Up_login);

        if (CheckConnectivity.isInternetAvailable(signup.this)){
            signup_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentlog = new Intent(signup.this, Login.class);
                    startActivity(intentlog);
                }
            });
            regbtn.setOnClickListener((View v) -> {
                if(!validateName() | !validateUsername() | !validateEmail() | !validatePhone() | !validatePassword()) {
                    return;
                }
                root = FirebaseDatabase.getInstance();
                reference = root.getReference("users");

                String fullname = regfullname.getEditText().getText().toString();
                String username = regusername.getEditText().getText().toString();
                String email = regemail.getEditText().getText().toString();
                String phoneno = regphone.getEditText().getText().toString();
                String password = regpass.getEditText().getText().toString();


                UserHelperClass helperClass = new UserHelperClass(fullname, username, email, phoneno, password);
                reference.child(username).setValue(helperClass);


                Intent intentnew = new Intent(signup.this,Login.class);
                startActivity(intentnew);
                Toast.makeText(this, "SignUp is successfull", Toast.LENGTH_SHORT).show();
            });
        }
        else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}