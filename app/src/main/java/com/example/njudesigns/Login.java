package com.example.njudesigns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button callsignup, forgotpass, go;
    TextView signtext, logostext;
    TextInputLayout username, password;
    ImageView image;
    SharedPreferences sp;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;



    private Boolean validateUsername() {
        String value = username.getEditText().getText().toString();
        if (value.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = password.getEditText().getText().toString();
        if (value.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
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
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        callsignup = findViewById(R.id.signup);
        forgotpass = findViewById(R.id.forget);
        go = findViewById(R.id.go);
        signtext = findViewById(R.id.signtext);
        logostext = findViewById(R.id.logo_texts);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        image = findViewById(R.id.logo_image);


        sp = getSharedPreferences("login", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)) {
            Intent intent1 = new Intent(Login.this, Dashboard.class);
            startActivity(intent1);
        }

        if (CheckConnectivity.isInternetAvailable(Login.this)) {

            forgotpass.setOnClickListener(v -> {
                Intent intent = new Intent(Login.this, PasswordCode.class);
                startActivity(intent);
            });

            callsignup.setOnClickListener(v -> {
                Intent intent = new Intent(Login.this, signup.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logostext, "logo_text");
                pairs[2] = new Pair<View, String>(signtext, "logo_desc");
                pairs[3] = new Pair<View, String>(username, "username_tran");
                pairs[4] = new Pair<View, String>(password, "password_tran");
                pairs[5] = new Pair<View, String>(go, "button_tran");
                pairs[6] = new Pair<View, String>(callsignup, "login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            });

            go.setOnClickListener((View v) -> {
                if (!validateUsername() | !validatePassword()) {
                    return;
                } else {
                    isUser();
                    sp.edit().putBoolean("logged", true).apply();
                }
            });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
             });

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        //GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

        // Signed in successfully, show authenticated UI.

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(acct);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Intent intent = new Intent(getApplicationContext(), Dashboard.class);

            intent.putExtra("fullname", personName);
            intent.putExtra("username", personGivenName);
            intent.putExtra("email", personEmail);

            startActivity(intent);
            //finish();
        }

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Login.this, Login.class));
        finish();

    }

    private void updateUI(GoogleSignInAccount acct) {
        if (acct != null) {
            Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Dashboard.class));

        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show();
        }
    }


    private void isUser() {
        String userenteredname = username.getEditText().getText().toString().trim();
        String userenteredpassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userenteredname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String passwordDB = snapshot.child(userenteredname).child("password").getValue(String.class);

                    if (passwordDB != null && passwordDB.equals(userenteredpassword)) {

                        username.setError(null);
                        username.setErrorEnabled(false);

                        String fullnameDB = snapshot.child(userenteredname).child("fullname").getValue(String.class);
                        String usernameDB = snapshot.child(userenteredname).child("username").getValue(String.class);
                        String emailDB = snapshot.child(userenteredname).child("email").getValue(String.class);
                        String phoneDB = snapshot.child(userenteredname).child("phone").getValue(String.class);
                        Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                        intent.putExtra("fullname", fullnameDB);
                        intent.putExtra("username", usernameDB);
                        intent.putExtra("email", emailDB);
                        intent.putExtra("phone", phoneDB);
                        intent.putExtra("password", passwordDB);
                        finish();

                        startActivity(intent);
                    } else {
//                        progressBar.setVisibility(View.GONE);
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
//                    progressBar.setVisibility(View.GONE);
                    username.setError("No such user exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}