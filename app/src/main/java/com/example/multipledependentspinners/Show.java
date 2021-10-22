package com.example.multipledependentspinners;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

public class Show extends AppCompatActivity {
    // Declare an ImageView and a RequestQueue object references
    ImageView ivShow;
    RequestQueue requestQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for this Activity with activity_show.xml
        setContentView(R.layout.activity_show);
        // Instantiate requestQueue
        requestQueue = Volley.newRequestQueue(this);
        // Get values from Intent
        String country_name = getIntent().getStringExtra("country_name");
        String city_name = getIntent().getStringExtra("city_name");
        String location_name = getIntent().getStringExtra("location_name");
        // Get the handle for ImageView. We'll show the image here.
        ivShow = findViewById(R.id.ivShow);
        // Construct the url, pass country, city and location names as key-value pairs,
        // and save it in a String.
        String url = "http://10.0.2.2/android/show_image.php?country_name=" + country_name
                + "&city_name=" + city_name + "&location_name=" + location_name;
        // Create a StringRequest object
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // As response from show_image.php we're getting
                // the full image file-name. In onResponse()
                // load the image from C:\xampp\htdocs\android\images folder
                // into ImageView with Glide library.
                Glide.with(Show.this)
                        .load("http://10.0.2.2/android/images/" + response)
                        .into(ivShow);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Don't forget to add stringRequest to requestQueue.
        requestQueue.add(stringRequest);
    }
}
