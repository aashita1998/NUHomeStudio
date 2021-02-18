package com.example.njudesigns.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.njudesigns.Details;
import com.example.njudesigns.R;
import com.example.njudesigns.model.Products;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    FragmentActivity activity;
    List<Products> productsList;

    public ProductsAdapter(FragmentActivity activity, List<Products> productsList) {
        this.activity = activity;
        this.productsList = productsList;
    }


    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {

        Picasso.with(activity).load(productsList.get(position).getImageUrl()).into(holder.prodImg);
        //holder.prodName.setText(productsList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView prodImg;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            prodImg = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

            @Override
            public void onClick (View v){
                int position=getAdapterPosition();
                Integer image=productsList.get(position).getImageUrl();
                Intent intent = new Intent(activity, Details.class);
                intent.putExtra("imageUrl", String.valueOf(image));
                activity.startActivity(intent);
        }
        }
    }

