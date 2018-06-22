package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetPredictions extends AsyncTask<List<String>,Void,String> {

public static final String REQUEST_METHOD = "GET";
//public static final int READ_TIMEOUT = 15000;
//public static final int CONNECTION_TIMEOUT = 15000;
private Activity activity;
private Context context;

public GetPredictions(Context context) {
        this.context = context;
        }

@Override
protected String doInBackground(List<String>... params){
        JSONObject obj = null;
        //
        String stringUrl;
        String result;
        String inputLine;
        stringUrl = "http://79.179.75.116:8080/DeepEstate/Main?city="+ params[0].get(0) +"&price=" + params[0].get(1);
        //stringUrl = "http://79.179.75.116:8080/DeepEstate/Main?city=Afula&price=5000";

        try {
        //Create a URL object holding our url
        URL myUrl = new URL(stringUrl);
        //Create a connection
        HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
        //Set methods and timeouts
        connection.setRequestMethod(REQUEST_METHOD);
        //connection.setReadTimeout(READ_TIMEOUT);
        //connection.setConnectTimeout(CONNECTION_TIMEOUT);

        //Connect to our url
        connection.connect();
        //Create a new InputStreamReader
        InputStreamReader streamReader = new
        InputStreamReader(connection.getInputStream());
        //Create a new buffered reader and String Builder
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        //Check if the line we are reading is not null
        while((inputLine = reader.readLine()) != null){
        stringBuilder.append(inputLine).append("\n");
        }
        //Close our InputStream and Buffered reader
        reader.close();
        streamReader.close();
        //Set our result equal to our stringBuilder
        result = stringBuilder.toString();
        }
        catch(IOException e){
        e.printStackTrace();
        result = null;
        }

        return result;

        }

protected void onPostExecute(String result){
        super.onPostExecute(result);
        }



        }

