package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetAutoItemSelectedJsonTask extends AsyncTask<TypeAndPlaceId,Void,String> {

    public static final String REQUEST_METHOD = "GET";
    private Context context;
    private Activity activity;

    public GetAutoItemSelectedJsonTask(Context context) {
        this.context = context;
    }

    public GetAutoItemSelectedJsonTask(Activity activity) {
        this.activity = activity;
    }



    @Override
    protected String doInBackground(TypeAndPlaceId... params) {
        JSONObject obj = null;
        //https://phoenix.onmap.co.il/v1/properties/search?option=buy&section=residence&loc[]=32.0733809800609&loc[]=34.752965733532754&loc[]=32.09706457001979&loc[]=35.02161578846437
        String stringUrl = "https://maps.googleapis.com/maps/api/geocode/json?place_id=" + params[0].place_id + "&key=AIzaSyAPcYTTiQO-4mHhzE030pk5b87Dcb83cVw&language=en";
        String result;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            //connection.setReadTimeout(READ_TIMEOUT);
            //connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("authority", "maps.googleapis.com");
            connection.setRequestProperty("path", "/maps/api/geocode/json?place_id=" + params[0].place_id + "&key=AIzaSyAPcYTTiQO-4mHhzE030pk5b87Dcb83cVw&language=en");
            connection.setRequestProperty("scheme", "https");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            //connection.setRequestProperty("accept-encoding", "gzip, deflate, br");
            connection.setRequestProperty("accept-language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7");
            connection.setRequestProperty("if-none-match", "W/\"c78-60Tm6eB/rOnVIfgd3dpmy/qXqRc\"");
            connection.setRequestProperty("origin", "https://www.onmap.co.il");
            connection.setRequestProperty("referer", "https://www.onmap.co.il/homes/" + params[0].type);
            connection.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
            connection.setRequestProperty("x-chrome-uma-enabled", "1");
            connection.setRequestProperty("x-client-data", "CK61yQEIirbJAQiktskBCMS2yQEIqZ3KAQihn8oBCKijygEI2qPKAQ==");

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
            //System.out.print("print json -----> " + obj.toString(2));

            Log.d("My App", obj.toString());
            return obj.toString(2);


        } catch (Throwable t) {
            try {
                JSONArray jsonObject = new JSONArray(result);
                return jsonObject.toString(2);
            } catch (Throwable d) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
                return null;
            }
//            Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
//            return null;
        }
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
//        Intent intent = new Intent();
//        intent.putExtra("autoList", result);


    }

}
