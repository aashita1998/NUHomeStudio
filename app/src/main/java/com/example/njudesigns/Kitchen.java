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

public class Kitchen extends AppCompatActivity {
    StateRec prodItems;
    RequestQueue requestQueue;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Kitchen.this, Dashboard.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        prodItems = findViewById(R.id.kitchen_rec);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            prodItems.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            prodItems.setLayoutManager(new GridLayoutManager(this, 2));
        }
        requestQueue = Volley.newRequestQueue(this);
        List<Products> productsList = new ArrayList<>();
        productsList.add(new Products("1", "Kitchen", R.drawable.kitchen1));
        productsList.add(new Products("2", "Kitchen", R.drawable.kitchen2));
        productsList.add(new Products("3", "Kitchen", R.drawable.kitchen3));
        productsList.add(new Products("4", "Kitchen", R.drawable.kitchen4));
        productsList.add(new Products("5", "Kitchen", R.drawable.kitchen5));
        productsList.add(new Products("6", "Kitchen", R.drawable.kitchen6));
        productsList.add(new Products("7", "Kitchen", R.drawable.kitchen7));
        productsList.add(new Products("8", "Kitchen", R.drawable.kitchen8));
        productsList.add(new Products("9", "Kitchen", R.drawable.kitchen9));
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