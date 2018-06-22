package com.example.dell.myapplication;
//package dev.edmt.segmentedbuttongroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.ceryle.segmentedbutton.SegmentedButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class FiltersActivity extends AppCompatActivity {

    Filters filters;
    Map<String,String> map = new HashMap<>();
    String activity = null;
    Spinner typeSpinner;
    EditText editTextMinPrice;
    EditText editTextMaxPrice;
    EditText editTextMinFloor;
    EditText editTextMaxFloor;
    Spinner roomsSpinner;
    CheckBox parkingCheckBox;
    RadioButton radioButtonSale;
    RadioButton radioButtonRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        map.put("קוטג'","cottage");
        map.put("דירה","apartment");
        map.put("פנטהאוז","penthouse");
        map.put("מיני פנטהאוז","mini_penthouse");
        map.put("דופלקס","duplex");
        map.put("דירת גג","rooftop_apartment");
        map.put("דירת גן","garden_apartment");
        map.put("למכירה","buy");
        map.put("להשכרה","rent");

        List<String> rooms = new ArrayList<String>(Arrays.asList("-1","1", "2","3","4","5","6+"));
        List<String> types = new ArrayList<String>(Arrays.asList("הכל","קוטג'", "דירה","פנטהאוז","מיני פנטהאוז","דופלקס","דירת גג","דירת גן"));

//        Switch mySwitch = (Switch) findViewById(R.id.switch1);
//        mySwitch.setTextOff("השכרה");
//        mySwitch.setTextOn("מכירה");

        Intent intent1 = getIntent();


        editTextMinPrice = findViewById(R.id.editText3);
        editTextMaxPrice = findViewById(R.id.editText4);
        typeSpinner = findViewById(R.id.spinner);
        editTextMinFloor = findViewById(R.id.editText);
        editTextMaxFloor = findViewById(R.id.editText2);
        roomsSpinner = findViewById(R.id.spinner2);
        parkingCheckBox = findViewById(R.id.checkBox);
        radioButtonSale = findViewById(R.id.saleRadioButton);
        radioButtonRent = findViewById(R.id.rentRadioButton);

        ArrayAdapter<String> roomsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,rooms);
        roomsSpinner.setAdapter(roomsAdapter);

        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,types);
        typeSpinner.setAdapter(typesAdapter);

        Button sumbmitButton = (Button) findViewById(R.id.submit_button);

        sumbmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterResults();
                Intent intent;

                if(!radioButtonSale.isChecked() && !radioButtonRent.isChecked())
                    Toast.makeText(FiltersActivity.this, "אנא סמן מכירה או השכרה", Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent();
                    intent.putExtra("Filters", filters);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent();
//                intent.putExtra("Filters", filters);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//            }
//        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.saleRadioButton:
                if (checked)
                    Toast.makeText(FiltersActivity.this, "Sale! ", Toast.LENGTH_LONG).show();
                    break;
            case R.id.rentRadioButton:
                if (checked)
                    Toast.makeText(FiltersActivity.this, "Rent! ", Toast.LENGTH_LONG).show();
                    break;
        }
    }

    public void FilterResults() {


        String purpose = null;
//        if (mySwitch.isChecked())
//            purpose = "buy";
//        else
//            purpose = "rent";
        String type = null;
        if (typeSpinner.getSelectedItem() != null && !typeSpinner.getSelectedItem().toString().equals("הכל") )
            type = typeSpinner.getSelectedItem().toString();
        int min_price = -1;
        if(editTextMinPrice != null && !editTextMinPrice.getText().toString().matches(""))
            min_price = Integer.parseInt(editTextMinPrice.getText().toString());
        int max_price = -1;
        if(editTextMaxPrice != null && !editTextMaxPrice.getText().toString().matches(""))
            max_price = Integer.parseInt(editTextMaxPrice.getText().toString());
        int rooms2 = -1;
        if(roomsSpinner.getSelectedItem() != null && !roomsSpinner.getSelectedItem().toString().equals("-1"))
            rooms2 = Integer.parseInt(roomsSpinner.getSelectedItem().toString());
        int min_floor = -1;
        if (editTextMinFloor != null && !editTextMinFloor.getText().toString().matches(""))
            min_floor = Integer.parseInt(editTextMinFloor.getText().toString());
        int max_floor = -1;
        if (editTextMaxFloor != null && !editTextMaxFloor.getText().toString().matches(""))
            max_floor = Integer.parseInt(editTextMaxFloor.getText().toString());
        boolean parking = Boolean.parseBoolean(null);
        if (parkingCheckBox != null) parking = parkingCheckBox.isChecked();

        if(radioButtonSale!= null) {
            if(radioButtonSale.isChecked())
                purpose = "buy";
        }

        if(radioButtonRent!= null) {
            if(radioButtonRent.isChecked())
                purpose = "rent";
        }

        filters = new Filters(purpose, type, min_price, max_price, rooms2, min_floor, max_floor, parking);

    }

}
