package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetImage extends AsyncTask<String,Void,Drawable> {

    public static final String REQUEST_METHOD = "GET";
    //public static final int READ_TIMEOUT = 15000;
    //public static final int CONNECTION_TIMEOUT = 15000;
    private Activity activity;
    private Context context;

    public GetImage(Context context) {
        this.context = context;
    }

    public GetImage() {

    }

    @Override
    protected Drawable doInBackground(String... params){
        try {
            InputStream is = (InputStream) new URL(params[0]).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }

    }

    protected void onPostExecute(Drawable result){
        super.onPostExecute(result);
        //Intent intent = new Intent(context, MapsActivity.class);

    }
}
