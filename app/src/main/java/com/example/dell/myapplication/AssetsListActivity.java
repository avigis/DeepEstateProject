package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AssetsListActivity extends AppCompatActivity {

    private List<Asset> assetsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssetAdapter mAdapter;
    private Map<String, String> map = new HashMap<String, String>();
    private DataWrapper dataWrapper;
    private DataWrapper filteredDataWrapper;
    private String locationString;
    String responseList;
    Collection<AutoCompleteWrapper> autoCompleteWrapper;
    AutoItemSelectedWrapper autoItemSelectedWrapper;
    List<String> autoList = new ArrayList<>();
    Filters filters;
    boolean filtered;
    Menu toolbarMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        setMap();

        assetsList = new ArrayList<>();
        DataWrapperAndBoolean dab = (DataWrapperAndBoolean) getIntent().getSerializableExtra("assetsList");
        dataWrapper = dab.dataWrapper;
        filtered = dab.filtered;
        if (filtered)
            filteredDataWrapper = dab.filteredDataWrapper;
        //dataWrappersToAssets();

        prepareAssetData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Data data = assetToData(position);
                Data data = dataWrapper.data.get(position);
                Gson gson = new Gson();
                String myJson = gson.toJson(data);
                Intent intent = new Intent(AssetsListActivity.this, AssetActivity.class);
                intent.putExtra("FromAssetListActivity", myJson);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    filters = (Filters) b.getSerializable("Filters");
                    FilterLocations();
                    prepareAssetData();
                }
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void prepareAssetData() {

        if (!filtered) {
            dataWrappersToAssets(dataWrapper);
        } else {
            dataWrappersToAssets(filteredDataWrapper);
        }
        translateAssets();
        mAdapter = new AssetAdapter(assetsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setMap() {
        map.put("cottage","קוטג'");
        map.put("apartment", "דירה");
        map.put("penthouse", "פנטהאוז");
        map.put("mini_penthouse", "מיני פנטהאוז");
        map.put("duplex", "דופלקס");
        map.put("rooftop_apartment", "דירת גג");
        map.put("garden_apartment", "דירת גן");
        map.put("buy", "למכירה");
        map.put("rent", "להשכרה");
    }

    private void translateAssets() {

        for (int i = 0; i < assetsList.size(); i++) {
            String enPurpose = assetsList.get(i).getPurpose();
            String enType = assetsList.get(i).getType();
            assetsList.get(i).setPurpose(map.get(enPurpose));
            assetsList.get(i).setType(map.get(enType));
        }
    }

    public List<Asset> dataWrappersToAssets(DataWrapper dataWrapper) {

        assetsList = new ArrayList<>();

        for (int i = 0; i < dataWrapper.data.size(); i++) {
            String address;
            if(dataWrapper.data.get(i).address.he.house_number == null || dataWrapper.data.get(i).address.he.house_number.equals("null"))
                address = dataWrapper.data.get(i).address.he.city_name + " " + dataWrapper.data.get(i).address.he.street_name;
            else
                address =  dataWrapper.data.get(i).address.he.street_name + " " + dataWrapper.data.get(i).address.he.house_number + ", " + dataWrapper.data.get(i).address.he.city_name;
            String type = dataWrapper.data.get(i).property_type;
            String purpose = dataWrapper.data.get(i).search_option;
            String price = String.valueOf(dataWrapper.data.get(i).price) + " ש''ח";
            String imageUrl = dataWrapper.data.get(i).thumbnail;
            Asset asset = new Asset(address, type,purpose, price,imageUrl);
            assetsList.add(asset);
        }

        return assetsList;
    }

    public Data assetToData(int index) {
        Asset asset = assetsList.get(index);
        Data data = null;
        for (int i = 0; i <dataWrapper.data.size(); i++) {
            if(String.valueOf(dataWrapper.data.get(i).price).equals(asset.getPrice()) &&
                    map.get(dataWrapper.data.get(i).property_type).equals(asset.getType()) &&
                    map.get(dataWrapper.data.get(i).search_option).equals(asset.getPurpose()) &&
                    asset.getAddress().contains(dataWrapper.data.get(i).address.he.street_name)) {
                data = dataWrapper.data.get(i);
                break;
            }
        }
        return data;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.action_bar_search_example_menu, menu);

        MenuItem searchMenu  = menu.findItem(R.id.app_bar_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setQueryHint("חפש נכס");

        AssetsListActivity.this.toolbarMenu = menu;

        menu.findItem(R.id.list_activity).setVisible(false);
        if(filtered) {
            menu.findItem(R.id.cancel_filter).setVisible(true);
            menu.findItem(R.id.filter).setVisible(false);
        } else {
            menu.findItem(R.id.cancel_filter).setVisible(false);
            menu.findItem(R.id.filter).setVisible(true);
        }


        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);


        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                //searchAutoComplete.setText("" + queryString);
                //Toast.makeText(MapsActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
                String placeId = locationToPlaceId(queryString, (ArrayList<AutoCompleteWrapper>) autoCompleteWrapper);
                TypeAndPlaceId typeAndPlaceId = new TypeAndPlaceId(dataWrapper.data.get(0).property_type,placeId);
                System.out.print("location json!!!\n");
                try {
                    locationString = new GetAutoItemSelectedJsonTask(AssetsListActivity.this).execute(typeAndPlaceId).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                autoItemSelectedWrapper = AutoItemSelectedWrapper.fromJson(locationString);

                List<String> locationsLatLng = new ArrayList<>();
                locationsLatLng.add(dataWrapper.data.get(0).search_option);
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.location.lat));
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.location.lng));
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.viewport.northeast.lat));
                locationsLatLng.add(String.valueOf(autoItemSelectedWrapper.results.get(0).geometry.viewport.northeast.lng));

                String httpResponse = null;

                try {
                    httpResponse = new GetLocations(AssetsListActivity.this).execute(locationsLatLng).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                dataWrapper = new DataWrapper();
                dataWrapper = DataWrapper.fromJson(httpResponse);
                //updateMap();
                prepareAssetData();

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
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(AssetsListActivity.this, android.R.layout.simple_dropdown_item_1line, autoList);
                    searchAutoComplete.setAdapter(newsAdapter);
                    System.out.print("check auto");

                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //textView.setText(query);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                return true;

            case R.id.map_activity:
                Intent intent = new Intent(AssetsListActivity.this, MapsActivity.class);
                DataWrapperAndBoolean dataWrapperAndBoolean;
                dataWrapperAndBoolean = new DataWrapperAndBoolean(dataWrapper, filteredDataWrapper, filtered);
                intent.putExtra("assetsList", dataWrapperAndBoolean);
                startActivity(intent);
                return true;

            case R.id.filter:
                Intent intent2 = new Intent(AssetsListActivity.this, FiltersActivity.class);
                intent2.putExtra("list","list");
//                List<Asset> assets =  new ArrayList<>();
//                assets = dataWrappersToAssets();
                startActivityForResult(intent2, 2);
                return true;

            case R.id.cancel_filter:
                UnFilterLocations();
                prepareAssetData();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

    public List<String> AutoComplateDataToString (ArrayList<AutoCompleteWrapper> autoCompleteWrapper) {

        List<String> autoCompleteList = new ArrayList<>();

        for (int i = 0; i < autoCompleteWrapper.size(); i++) {
            autoCompleteList.add(autoCompleteWrapper.get(i).description);
        }
        return autoCompleteList;
    }


}
