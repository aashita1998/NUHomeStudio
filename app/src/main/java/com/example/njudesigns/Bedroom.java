package com.example.njudesigns;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.njudesigns.adapter.ProductsAdapter;
import com.example.njudesigns.model.Products;

import java.util.ArrayList;
import java.util.List;

public class Bedroom extends AppCompatActivity {
    StateRec prodItems;
    RequestQueue requestQueue;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Bedroom.this, Dashboard.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        prodItems = findViewById(R.id.bedroom_rec);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            prodItems.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            prodItems.setLayoutManager(new GridLayoutManager(this, 2));
        }
        requestQueue = Volley.newRequestQueue(this);
        List<Products> productsList = new ArrayList<>();
        productsList.add(new Products("1", "Bedroom", R.drawable.bed1));
        productsList.add(new Products("2", "Bedroom", R.drawable.bed2));
        productsList.add(new Products("3", "Bedroom", R.drawable.bed3));
        productsList.add(new Products("4", "Bedroom", R.drawable.bed4));
        productsList.add(new Products("5", "Bedroom", R.drawable.bed5));
        productsList.add(new Products("6", "Bedroom", R.drawable.bed6));
        productsList.add(new Products("7", "Bedroom", R.drawable.bed7));
        productsList.add(new Products("8", "Bedroom", R.drawable.bed8));
        productsList.add(new Products("9", "Bedroom", R.drawable.bed9));
        productsList.add(new Products("10", "Bedroom", R.drawable.bed10));
        productsList.add(new Products("11", "Bedroom", R.drawable.bed11));
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

    }

    private void setProdItems(List<Products> productsList) {
        ProductsAdapter adapter = new ProductsAdapter(this, productsList);
        prodItems.setAdapter(adapter);
    }
}