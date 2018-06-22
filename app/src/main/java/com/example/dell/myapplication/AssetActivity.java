package com.example.dell.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AssetActivity extends AppCompatActivity {

    Map<String, String> map = new HashMap<String, String>();
    TabHost host;
    Data data;
    String predictions;
    List<Integer> intPreds;
    DataPoint[] dataPoints;
    LineGraphSeries<DataPoint> series;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset);

        //ViewCompat.setLayoutDirection(findViewById(R.id.relativeLayout), ViewCompat.LAYOUT_DIRECTION_RTL);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setBackgroundColor(Color.parseColor("#00000000"));
        graph.setDrawingCacheBackgroundColor(Color.parseColor("#0da783"));
//        graph.setBackgroundColor(Color.parseColor("#0da783"));
//        graph.setDrawBackground(true);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("שנה");
        graph.getGridLabelRenderer().setVerticalAxisTitle("מחיר משוער");


//        graph.getGridLabelRenderer().setTextSize(6f);
//        graph.getGridLabelRenderer().reloadStyles();

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("פרטי הנכס");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("מפה");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("תחזית");
        host.addTab(spec);

        predictions = null;

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if("Tab One".equals(tabId)) {
                    //destroy earth
                }
                if("Tab Two".equals(tabId)) {
                    //destroy mars
                }
                if("Tab Three".equals(tabId)) {
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);
                    layout.setBackgroundResource(R.drawable.white_background);

                    List<String> strings = new ArrayList<>();
                    strings.add(data.address.en.city_name);
                    strings.add(String.valueOf( data.price));
                    try {
                        predictions = new GetPredictions(AssetActivity.this).execute(strings).get();
                        System.out.print("predictions: " + predictions);
//                        intPreds = stringPredsToIntPreds(predictions);
                        //dataPoints = addDataPoints();
                        dataPoints = new DataPoint[] {
                                new DataPoint(2019, 2900),
                                new DataPoint(2020, 3000),
                                new DataPoint(2021, 3150),
                                new DataPoint(2022, 3100),
                                new DataPoint(2024, 3200),
                                new DataPoint(2025, 3180)
                        };
                        series = new LineGraphSeries<>(dataPoints);
                        graph.addSeries(series);
                        // styling series
                        series.setTitle("Random Curve 1");
                        series.setColor(Color.parseColor("#FF0DA783"));
                        series.setDrawDataPoints(true);
                        series.setDataPointsRadius(3);
                        series.setThickness(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }});


        TextView propose = findViewById(R.id.textView11); // for rent / for sale
        TextView address = findViewById(R.id.textView12); // address
        TextView price = findViewById(R.id.textView10); // price
        TextView type_of_asset = findViewById(R.id.textView72); // type of asset
        TextView rooms = findViewById(R.id.textView46); // rooms
        TextView rooms2 = findViewById(R.id.textView74); // rooms
        TextView floor = findViewById(R.id.textView44); // floor
        TextView floor2 = findViewById(R.id.textView73); // floor
        TextView parkings = findViewById(R.id.textView75); // parkings
        TextView parkings2 = findViewById(R.id.textView43); // parkings
        TextView square = findViewById(R.id.textView45); // area square
        TextView square2 = findViewById(R.id.textView76); // area square
        TextView bathrooms = findViewById(R.id.textView77); // bathrooms
        //TextView toilets = findViewById(R.id.textView37); // toilets
        //TextView sunBalconies = findViewById(R.id.textView39); // sunBalconies
        ImageView photo = findViewById(R.id.imageView);

        map.put("cottage","קוטג'");
        map.put("apartment", "דירה");
        map.put("penthouse", "פנטהאוז");
        map.put("mini_penthouse", "מיני פנטהאוז");
        map.put("duplex", "דופלקס");
        map.put("rooftop_apartment", "דירת גג");
        map.put("garden_apartment", "דירת גן");
        map.put("buy", "למכירה");
        map.put("rent", "להשכרה");




// custom paint to make a dotted line
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10);
//        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
//        series.setCustomPaint(paint);



        Gson gS = new Gson();
        Intent intent1 = getIntent();
        data = null;

        if(intent1.hasExtra("Asset")){
            String target = getIntent().getStringExtra("Asset");
            data = gS.fromJson(target, Data.class);
        }else if(intent1.hasExtra("FromAssetListActivity")){
            String jsonDataWrapper = getIntent().getStringExtra("FromAssetListActivity");
            data = gS.fromJson(getIntent().getStringExtra("FromAssetListActivity"), Data.class);
        }


        System.out.print("in AssetActivity bathrooms = " + data.additional_info.bathrooms);

        propose.setText(map.get(data.property_type) + " " + map.get(data.search_option));
        if (data.address.he.house_number == null)
            address.setText(data.address.he.city_name + " , " + " " + data.address.he.street_name);
        else
        address.setText(data.address.he.street_name  + " " + data.address.he.house_number + " , " + data.address.he.city_name  );

        price.setText(String.valueOf(data.price + " ש''ח "));
        rooms.setText(String.valueOf((int) data.additional_info.rooms));
        rooms2.setText(String.valueOf((int) data.additional_info.rooms));
        floor.setText(data.additional_info.floor.on_the + " מתוך " + data.additional_info.floor.out_of);
        floor2.setText(data.additional_info.floor.on_the + " מתוך " + data.additional_info.floor.out_of);
        square.setText(String.valueOf(data.additional_info.area.base));
        square2.setText(String.valueOf(data.additional_info.area.base));
        bathrooms.setText(String.valueOf(data.additional_info.bathrooms));
        type_of_asset.setText(map.get(data.property_type));

        if (data == null)
            System.out.print("data is null");
        if (data.additional_info == null)
            System.out.print("data is null");
        if (data.additional_info.parking == null)
            System.out.print("parking is null");
        if (data.additional_info.parking == null || data.additional_info.parking.aboveground.equals("none")) {
            parkings.setText("0");
            parkings2.setText("0");
        }
        else {
            parkings.setText(data.additional_info.parking.aboveground);
            parkings2.setText(data.additional_info.parking.aboveground);
        }

        if (data.thumbnail != null) {
            Drawable myDrawable = null;
            try {
                myDrawable = new GetImage().execute(data.thumbnail).get();
                photo.setImageDrawable(myDrawable);
            } catch (Exception e) {

                e.printStackTrace();
            }
            if (myDrawable == null) {
                photo.setImageResource(R.drawable.default_asset_image);
            }
        }

        Intent intent = new Intent();
        intent.putExtra("assetType", data.search_option);
        setResult(RESULT_OK, intent);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_search_example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Integer> stringPredsToIntPreds(String strPreds) {
        String[] preds = (strPreds.replace("\n","")).split(",");
        intPreds = new ArrayList<>();
        for(int i = 0; i < preds.length; i++) {
            intPreds.add(Integer.parseInt( preds[i]));
        }
        return intPreds;
    }

    public DataPoint[] addDataPoints() {
        int size = intPreds.size();
        int year = 2019;
        DataPoint[] dataPoints = new DataPoint[size];

        for(int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(year, intPreds.get(i));
            year++;

        }
        return dataPoints;
    }



}



