package com.example.njudesigns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.njudesigns.adapter.ProductsAdapter;
import com.example.njudesigns.model.Products;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    StateRec prodItems;
    RequestQueue requestQueue;
    GoogleSignInClient mGoogleSignInClient;
    FloatingActionButton fabship;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Dashboard.this, Login.class));
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
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        nav = findViewById(R.id.navmenu);

        drawerLayout = findViewById(R.id.drawer);


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        prodItems = findViewById(R.id.dash_rec);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            prodItems.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            prodItems.setLayoutManager(new GridLayoutManager(this, 2));
        }
        requestQueue = Volley.newRequestQueue(this);
        List<Products> productsList = new ArrayList<>();
        productsList.add(new Products("1", "Living", R.drawable.living10));
        productsList.add(new Products("2", "Living", R.drawable.tv1));
        productsList.add(new Products("3", "Living", R.drawable.kitchen1));
        productsList.add(new Products("4", "Living", R.drawable.bed4));
        productsList.add(new Products("5", "Living", R.drawable.living5));
        productsList.add(new Products("6", "Living", R.drawable.tv4));
        productsList.add(new Products("7", "Living", R.drawable.living7));
        productsList.add(new Products("8", "Living", R.drawable.living8));
        productsList.add(new Products("9", "Living", R.drawable.kitchen8));
        productsList.add(new Products("10", "Living", R.drawable.tv3));
        setProdItems(productsList);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            setProdItems(productsList);
        } else {
            if (this.getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || this.getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("No Internet Connection...Please Check Your Network");
                dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_living:
                       // Toast.makeText(Dashboard.this, "Living", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Dashboard.this,Living.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_tv:
                        //Toast.makeText(Dashboard.this, "Tv", Toast.LENGTH_SHORT).show();
                        Intent Tvintent = new Intent(Dashboard.this,Tvunit.class);
                        startActivity(Tvintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_kitchen:
                        //Toast.makeText(Dashboard.this, "Kitchen", Toast.LENGTH_SHORT).show();
                        Intent kitchenintent = new Intent(Dashboard.this,Kitchen.class);
                        startActivity(kitchenintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_bedroom:
                        //Toast.makeText(Dashboard.this, "Bedroom", Toast.LENGTH_SHORT).show();
                        Intent bedintent = new Intent(Dashboard.this,Bedroom.class);
                        startActivity(bedintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.contact:
                        Intent contact_intent = new Intent(Dashboard.this, Contact.class);
                        startActivity(contact_intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.logout:
                       Intent logintent = new Intent(Dashboard.this,Login.class);
                        startActivity(logintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;
                }
                return true;
            }
        });
        updateNavHeader();
    }
    private void setProdItems(List<Products> productsList) {
        ProductsAdapter adapter = new ProductsAdapter(this, productsList);
        prodItems.setAdapter(adapter);
    }

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.navmenu);
        View headerview = navigationView.getHeaderView(0);
        TextView navuser = headerview.findViewById(R.id.menu_username);

        Intent intent = getIntent();
        String str = intent.getStringExtra("username");
        navuser.setText(str);
    }
}