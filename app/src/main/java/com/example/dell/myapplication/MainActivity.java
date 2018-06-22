package com.example.dell.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private SpinKitView spinKitView;
    Integer count =1;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        spinKitView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
//        //DoubleBounce doubleBounce = new DoubleBounce();
//        //progressBar.setIndeterminateDrawable(doubleBounce);
//        progressBar.setVisibility(View.GONE);




        Button buy_button = findViewById(R.id.buy_button);
        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinKitView.setVisibility(View.VISIBLE);
                new MyTask().execute("buy");

            }
        });

        Button rent_button = findViewById(R.id.rent_button);
        rent_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinKitView.setVisibility(View.VISIBLE);
                new MyTask().execute("rent");


            }
        });


    }

    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SystemClock.sleep(2000);
            if(params[0].equals("buy")) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("type", "buy");
                startActivity(intent);
            } else if(params[0].equals("rent")) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("type", "rent");
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void loader) {
            super.onPostExecute(loader);
            spinKitView.setVisibility(View.INVISIBLE);
        }
    }

}
