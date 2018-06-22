package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.common.collect.MapMaker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private DataWrapper dataWrapper;
    private DataWrapper filteredDataWrapper;
    private Context context;
    private GetAutoCompleteList autoTask;
    String responseList;
    ArrayAdapter mAdapter;
    ListView mListView;
    TextView mEmptyView;
    Collection<AutoCompleteWrapper> autoCompleteWrapper;
    AutoItemSelectedWrapper autoItemSelectedWrapper;
    String type_of_asset;
    List<String> autoList = new ArrayList<>();
    String locationString;
    Filters filters;
    List<MarkerOptions> markers;
    String httpResponse = null;
    boolean filtered;
    Menu toolbarMenu;
    public static int color = 0xFF1C1C1C;
    public static int textColor = 0xFFFFFFFF;

    public MapsActivity() { }

    public MapsActivity(Context context) {
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        filtered = false;
        String jsonStr = null;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle(Html.fromHtml("<font color='#ffffff'>DeepEstate </font>"));
        setSupportActionBar(myToolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autoTask = new GetAutoCompleteList(this);
        markers = new ArrayList<MarkerOptions>();
        filters = null;

        Intent intent1 = getIntent();

        if(intent1.hasExtra("assetsList")) {
            DataWrapperAndBoolean dataWrapperAndBoolean = (DataWrapperAndBoolean) getIntent().getSerializableExtra("assetsList");
            filtered = dataWrapperAndBoolean.filtered;
            dataWrapper = dataWrapperAndBoolean.dataWrapper;
            if (filtered)
                filteredDataWrapper = dataWrapperAndBoolean.filteredDataWrapper;

            type_of_asset = dataWrapper.data.get(0).property_type;
        }
        else if(intent1.hasExtra("type")) {

            type_of_asset = getIntent().getStringExtra("type");
            System.out.print("type of asset " + type_of_asset + "\n");

            List<String> params = new ArrayList<>();

            params.add(type_of_asset);
            try {
                httpResponse = new GetLocations(this).execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = getIntent();

            if (httpResponse != null) {
                httpResponse = httpResponse.replace("I/System.out:", "");
                intent.putExtra("String", httpResponse);
            }

            dataWrapper = new DataWrapper();
            dataWrapper = DataWrapper.fromJson(httpResponse);
        } else if(intent1.hasExtra("Filters")) {
            filters = (Filters) intent1.getSerializableExtra("Filters");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                type_of_asset = data.getStringExtra("assetType");
            }
        }
        else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    filters = (Filters) b.getSerializable("Filters");
                    FilterLocations();
                    updateMap();
                }
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
            }
        }
    }


    public LatLng DrawMarkersOnMap(DataWrapper dataWrapper) {

        double lat, lon;
        String title;
        MarkerOptions marker = null;

        double avgLat = 0, avgLon = 0;
        if (dataWrapper != null) {


            int size = dataWrapper.data.size();
            mMap.clear();
            markers = new ArrayList<MarkerOptions>();

            for (int i = 0; i < dataWrapper.data.size(); i++) {

                lat = dataWrapper.data.get(i).address.location.lat;
                lon = dataWrapper.data.get(i).address.location.lon;
                avgLat += lat;
                avgLon += lon;
                if (dataWrapper.data.get(i).address.he.house_number == null)
                title = dataWrapper.data.get(i).address.he.street_name + " "
                        + dataWrapper.data.get(i).address.he.house_number + ", "
                        + dataWrapper.data.get(i).address.he.city_name;
                else
                    title = dataWrapper.data.get(i).address.he.street_name + " ,"
                            + dataWrapper.data.get(i).address.he.city_name;

                LatLng location = new LatLng(lat, lon);
                marker = new MarkerOptions().position(location).title(title);
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
                marker.position(location);
                markers.add(marker);
                mMap.addMarker(marker);
            }

            return new LatLng(avgLat/size, avgLon/size);
        }
        return  null;
    }

    public DataWrapper FilterLocations() { // validate user choose all the filters or not !!!!!!!!!!!!!!!!!!!!
        boolean in;
        int counter = 0;
        filtered = false;

        filteredDataWrapper = DataWrapper.copyDataWrapper(dataWrapper);

        if (filters != null && dataWrapper != null) {

            for (int i = 0; i < dataWrapper.data.size(); i++) {
                in = true;
                long price = dataWrapper.data.get(i).price;
                double rooms = dataWrapper.data.get(i).additional_info.rooms;
                int floor = dataWrapper.data.get(i).additional_info.floor.on_the;
                String type = dataWrapper.data.get(i).property_type;

                if (filters.getMin_price() != -1 && price < filters.getMin_price())
                    in = false;
                if (filters.getMax_price() != -1 && price > filters.getMax_price())
                    in = false;
                if (filters.getRooms() != -1 && rooms != filters.getRooms())
                    in = false;
                if (filters.getMin_floor() != -1 && floor < filters.getMin_floor())
                    in = false;
                if (filters.getMax_floor() != -1 && floor > filters.getMax_floor())
                    in = false;
                if(filters.getType() != null && !type.equals(filters.getType()))
                    in = false;


                if (!in) {
                    filteredDataWrapper = removeDataIFromDataWrapper(filteredDataWrapper,dataWrapper.data.get(i));
                    filtered = true;
                }
            }

        }

        if (filtered) {
            MenuItem cancelfilter = toolbarMenu.findItem(R.id.cancel_filter);
            MenuItem filter = toolbarMenu.findItem(R.id.filter);
            cancelfilter.setVisible(true);
            filter.setVisible(false);
        }
        return filteredDataWrapper;
    }

    public void UnFilterLocations() {
        filtered = false;
        MenuItem cancelfilter = toolbarMenu.findItem(R.id.cancel_filter);
        MenuItem filter = toolbarMenu.findItem(R.id.filter);
        cancelfilter.setVisible(false);
        filter.setVisible(true);
    }

    public DataWrapper removeDataIFromDataWrapper(DataWrapper dataWrapper,Data data) {
        for (int i = 0; i < dataWrapper.data.size(); i++) {
            if (data.equal(dataWrapper.data.get(i)))
                dataWrapper.data.remove(i);
        }
        return dataWrapper;
    }


    public void updateMap() {
        mMap.clear();
        if (filtered)
            DrawMarkersOnMap(filteredDataWrapper);
        else
            DrawMarkersOnMap(dataWrapper);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 20; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng avgLocation;
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (filtered)
            avgLocation = DrawMarkersOnMap(filteredDataWrapper);
        else {
            avgLocation = DrawMarkersOnMap(dataWrapper);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(avgLocation));

        CameraUpdate center = CameraUpdateFactory.newLatLng(avgLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.action_bar_search_example_menu, menu);

        MenuItem searchMenu  = menu.findItem(R.id.app_bar_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setQueryHint("חפש נכס");

        menu.findItem(R.id.map_activity).setVisible(false);
        if(!filtered) {
            menu.findItem(R.id.cancel_filter).setVisible(false);
            menu.findItem(R.id.filter).setVisible(true);
        } else {
            menu.findItem(R.id.cancel_filter).setVisible(true);
            menu.findItem(R.id.filter).setVisible(false);
        }

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        MapsActivity.this.toolbarMenu = menu;
        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                String placeId = locationToPlaceId(queryString, (ArrayList<AutoCompleteWrapper>) autoCompleteWrapper);
                TypeAndPlaceId typeAndPlaceId = new TypeAndPlaceId(type_of_asset,placeId);
                try {
                    locationString = new GetAutoItemSelectedJsonTask(MapsActivity.this).execute(typeAndPlaceId).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                autoItemSelectedWrapper = AutoItemSelectedWrapper.fromJson(locationString);

                List<String> locationsLatLng = new ArrayList<>();
                locationsLatLng.add(type_of_asset);
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.location.lat));
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.location.lng));
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.viewport.northeast.lat));
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.viewport.northeast.lng));

                try {
                    httpResponse = new GetLocations(MapsActivity.this).execute(locationsLatLng).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                dataWrapper = new DataWrapper();
                dataWrapper = DataWrapper.fromJson(httpResponse);

                if (dataWrapper.data.size() > 0) {
                    if(filtered) {
                        FilterLocations();
                    }
                    updateMap();
                }
                else {
                    mMap.clear();
                    Toast.makeText(MapsActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // your text view here
                if (!TextUtils.isEmpty(newText)) {
                    responseList = GetAutoCompleteList(newText);
                    autoCompleteWrapper = new ArrayList<AutoCompleteWrapper>();
                    autoCompleteWrapper = AutoCompleteWrapper.fromJson(responseList);
                    autoList = AutoComplateDataToString((ArrayList<AutoCompleteWrapper>) autoCompleteWrapper);
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_dropdown_item_1line, autoList);
                    searchAutoComplete.setAdapter(newsAdapter);
                    System.out.print("check auto");

                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        });

        MenuItem shareMenuItem = menu.findItem(R.id.app_bar_menu_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareMenuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareActionProvider.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    public List<String> AutoComplateDataToString (ArrayList<AutoCompleteWrapper> autoCompleteWrapper) {

        List<String> autoCompleteList = new ArrayList<>();

        for (int i = 0; i < autoCompleteWrapper.size(); i++) {
            autoCompleteList.add(autoCompleteWrapper.get(i).description);
        }
        return autoCompleteList;
    }

    public String GetAutoCompleteList(String substr) {

        String httpResponse = null;

        try {
            substr =  URLEncoder.encode(substr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            httpResponse = new GetAutoCompleteList(this).execute(substr).get();
            System.out.print("get auto complete list --------> " + httpResponse);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return httpResponse;

    }

    public String locationToPlaceId(String location, ArrayList<AutoCompleteWrapper> autoCompleteWrapper) {

        for (int i = 0; i <autoCompleteWrapper.size(); i++) {
            if (autoCompleteWrapper.get(i).description.equals(location))
                return autoCompleteWrapper.get(i).place_id;
        }
        return null;
    }

    public List<Asset> dataWrappersToAssets() {

        List<Asset> assetsList = new ArrayList<>();

        for (int i = 0; i < dataWrapper.data.size(); i++) {
            String address;

            if(dataWrapper.data.get(i).address.he.house_number == null || dataWrapper.data.get(i).address.he.house_number.equals("null"))
                address = dataWrapper.data.get(i).address.he.city_name + " " + dataWrapper.data.get(i).address.he.street_name;
            else
                address = dataWrapper.data.get(i).address.he.city_name  + " ," + dataWrapper.data.get(i).address.he.house_number + " " + dataWrapper.data.get(i).address.he.street_name;

            String type = dataWrapper.data.get(i).property_type;
            String purpose = dataWrapper.data.get(i).search_option;
            String price = String.valueOf(dataWrapper.data.get(i).price);
            String imageUrl = dataWrapper.data.get(i).thumbnail;
            Asset asset = new Asset(address, type,purpose, price, imageUrl);
            assetsList.add(asset);
        }

        return assetsList;
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                return true;

            case R.id.list_activity:
                Intent intent = new Intent(MapsActivity.this, AssetsListActivity.class);
                DataWrapperAndBoolean dab = new DataWrapperAndBoolean(dataWrapper, filteredDataWrapper ,filtered);
                intent.putExtra("assetsList", dab);
                startActivity(intent);
                return true;

            case R.id.filter:
                Intent intent2 = new Intent(MapsActivity.this, FiltersActivity.class);
                intent2.putExtra("maps","maps");
                startActivityForResult(intent2, 2);
                return true;

            case R.id.cancel_filter:
                UnFilterLocations();
                updateMap();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Data asset = null;
        for (int i = 0; i < dataWrapper.data.size(); i++) {
            if (marker.getPosition().latitude == dataWrapper.data.get(i).address.location.lat
                    && marker.getPosition().longitude == dataWrapper.data.get(i).address.location.lon)
                asset = dataWrapper.data.get(i);
        }

        Gson gS = new Gson();
        String target = gS.toJson(asset);
        Intent intent = new Intent(MapsActivity.this, AssetActivity.class);
        intent.putExtra("Asset", target);
        startActivity(intent);

        return false;
    }
}
