package com.example.dell.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.MyViewHolder> {

    private List<Asset> assetsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView address, type, purpose, price;
        public ImageView imageUrl;

        public MyViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.address);
            type = (TextView) view.findViewById(R.id.type);
            purpose = (TextView) view.findViewById(R.id.purpose);
            price = (TextView) view.findViewById(R.id.price);
            imageUrl = (ImageView) view.findViewById(R.id.imageViewList);
        }
    }

    public AssetAdapter(List<Asset> assetsList) {
        this.assetsList = assetsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asset_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssetAdapter.MyViewHolder holder, int position) {
        Asset asset = assetsList.get(position);
        holder.address.setText(asset.getAddress());
        holder.type.setText(asset.getType());
        holder.purpose.setText(asset.getPurpose());
        holder.price.setText(asset.getPrice());
        Drawable myDrawable = null;
        try {
            myDrawable = new GetImage().execute(asset.getImageUrl()).get();
            holder.imageUrl.setImageDrawable(myDrawable);
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (myDrawable == null) {
            holder.imageUrl.setImageResource(R.drawable.default_asset_image);
        }

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return assetsList.size();
    }
}
