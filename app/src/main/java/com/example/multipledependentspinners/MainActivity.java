package com.example.multipledependentspinners;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerCountry, spinnerCity, spinnerLocation; // Declare a Spinner object reference for location.
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    // Create an ArrayList to hold all the locations in a city.
    ArrayList<String> locationList = new ArrayList<>();
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> cityAdapter;
    // Declare an Adapter for location. This adapter creates a bridge between
    // data-source, in our case locationList, and the Spinner object.
    ArrayAdapter<String> locationAdapter;
    RequestQueue requestQueue;
    Button btnSubmit; // Declare a Button object reference.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerCity = findViewById(R.id.spinnerCity);
        // Get the handle for spinnerLocation by calling findViewById()
        spinnerLocation = findViewById(R.id.spinnerLocation);
        // Get the handle for btnSubmit
        btnSubmit = findViewById(R.id.btnSubmit);
        String url = "http://10.0.2.2/android/populate_country.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("countries");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String countryName = jsonObject.optString("country_name");
                        countryList.add(countryName);
                        countryAdapter = new ArrayAdapter<>(MainActivity.this,
                                android.R.layout.simple_spinner_item, countryList);
                        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCountry.setAdapter(countryAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        spinnerCountry.setOnItemSelectedListener(this);

        // Attach OnItemSelectedListener with spinnerCity
        spinnerCity.setOnItemSelectedListener(this);
        // Attach OnClickListener with btnSubmit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values of all three Spinners in Strings.
                String country_name = spinnerCountry.getSelectedItem().toString().trim();
                String city_name = spinnerCity.getSelectedItem().toString().trim();
                String location_name = spinnerLocation.getSelectedItem().toString().trim();
                // Instantiate an Intent object, put above values in it as extra,
                // and launch second Activity called "Show" with the Intent.
                Intent intent = new Intent(MainActivity.this, Show.class);
                intent.putExtra("country_name", country_name);
                intent.putExtra("city_name", city_name);
                intent.putExtra("location_name", location_name);
                startActivity(intent);
                // After calling startActivity(), you can call finish() method
                // to close MainActivity.
                // finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerCountry){
            cityList.clear();
            String selectedCountry = adapterView.getSelectedItem().toString();
            String url = "http://10.0.2.2/android/populate_city.php?country_name="+selectedCountry;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("cities");
                        for(int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String cityName = jsonObject.optString("city_name");
                            cityList.add(cityName);
                            cityAdapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, cityList);
                            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCity.setAdapter(cityAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
        }
        // Copy the if() block, once a city in spinnerCity is selected
        // the if condition becomes true.
        if(adapterView.getId() == R.id.spinnerCity){
            // First clear the location list.
            locationList.clear();
            // Get the selected City and store in a String.
            String selectedCity = adapterView.getSelectedItem().toString();
            // Construct and store the url in a String.
            String url = "http://10.0.2.2/android/populate_location.php?city_name="+selectedCity;
            // Instantiate requestQueue
            requestQueue = Volley.newRequestQueue(this);
            // Instantiate a JsonObjectRequest object
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // Since we are sending an array in JSON format from
                        // populate_location.php, store the response in a JSONArray.
                        JSONArray jsonArray = response.getJSONArray("locations");
                        // Iterate through jsonArray
                        for(int i=0; i<jsonArray.length();i++){
                            // Get JSON Objects one by one, get value for location_name,
                            // add to locationList, create adapter with that,
                            // and set this adapter to spinnerLocation.
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String locationName = jsonObject.optString("location_name");
                            locationList.add(locationName);
                            locationAdapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, locationList);
                            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerLocation.setAdapter(locationAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Add this jsonObjectRequest to requestQueue
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
/*
Photo courtesy:
Victoria Memorial: https://pixabay.com/photos/victoria-memorial-calcutta-landmark-166487/
Indian Museum: https://pixabay.com/photos/indian-museum-kolkata-museum-indian-3382432/
Vidyasagar Setu: https://pixabay.com/photos/bridge-calcutta-kolkata-hooghly-167041/
*/