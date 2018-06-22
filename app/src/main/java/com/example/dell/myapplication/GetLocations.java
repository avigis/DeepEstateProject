package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetLocations extends AsyncTask<List<String>,Void,String> {

    public static final String REQUEST_METHOD = "GET";
    //public static final int READ_TIMEOUT = 15000;
    //public static final int CONNECTION_TIMEOUT = 15000;
    private Activity activity;
    private Context context;

    public GetLocations(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(List<String>... paramslist){
        JSONObject obj = null;
        //https://phoenix.onmap.co.il/v1/properties/search?option=buy&section=residence&loc[]=32.0733809800609&loc[]=34.752965733532754&loc[]=32.09706457001979&loc[]=35.02161578846437
        //https://phoenix.onmap.co.il/v1/properties/search?option=buy&section=residence&loc[]=32.0733809800609&loc[]=34.752965733532754&loc[]=32.09706457001979&loc[]=35.02161578846437
        String stringUrl;
        String result;
        String inputLine;
        if (paramslist[0].size() == 1)
            stringUrl = "https://phoenix.onmap.co.il/v1/properties/search?option=" + paramslist[0].get(0) + "&section=residence&loc[]=32.180047542894975&loc[]=34.921855450000066&loc[]=32.22065575048685&loc[]=35.02322149309089&$limit=25&$skip=0";
        else
            stringUrl = "https://phoenix.onmap.co.il/v1/properties/search?option=" + paramslist[0].get(0) + "&section=residence&loc[]=" + paramslist[0].get(1) + "&loc[]=" + paramslist[0].get(2) + "&loc[]=" + paramslist[0].get(3) + "&loc[]=" + paramslist[0].get(4) + "&$limit=25&$skip=0";

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            //connection.setReadTimeout(READ_TIMEOUT);
            //connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Host", "phoenix.onmap.co.il");
            connection.setRequestProperty("Origin", "https://www.onmap.co.il");
            connection.setRequestProperty("Referer", "https://www.onmap.co.il/homes/buy/c_32.073381,34.752966/t_32.109448,35.021616/z_12");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");

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

        try {

            obj = new JSONObject(result);
            Log.d("My App", obj.toString());
            return obj.toString(2);

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            return null;
        }

    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }



}
