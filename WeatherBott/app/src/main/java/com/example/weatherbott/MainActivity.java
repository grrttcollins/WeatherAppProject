package com.example.weatherbott;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView result, TVtemp, TVfeel, TVDesc, opening, butitfeelslike, thereAre, clothing;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String APIKEY = "834214d71427fecbb0bbdfcb43def862";
    DecimalFormat df = new DecimalFormat("##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.EditTextCity);
        TVtemp = findViewById(R.id.TextViewTemp);
        TVfeel = findViewById(R.id.TextViewFeel);
        TVDesc = findViewById(R.id.weatherDescription);
        result = findViewById(R.id.TextViewResult);
        opening = findViewById(R.id.OpeningLines);
        butitfeelslike = findViewById(R.id.ButItFeelsLike);
        thereAre = findViewById(R.id.thereAre);
        clothing = findViewById(R.id.clothing);
    }

    public void pullWeatherData(View view) {
        String currCity = city.getText().toString().trim();
        String apicallurl = "";

        if (city.equals("")) {
            result.setText("Search for a City");
        } else {
            apicallurl = url + "?q=" + currCity + "&APPID=" + APIKEY;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apicallurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jresponse = new JSONObject(response);
                    JSONArray jArray = jresponse.getJSONArray("weather");
                    JSONObject jweather = jArray.getJSONObject(0);
                    JSONObject jMain = jresponse.getJSONObject("main");
                    JSONObject jWind = jresponse.getJSONObject("wind");
                    JSONObject jSys = jresponse.getJSONObject("sys");

                    TVtemp.setTextColor(Color.rgb(165, 224, 178));
                    TVfeel.setTextColor(Color.rgb(165, 224, 178));
                    TVDesc.setTextColor(Color.rgb(165, 224, 178));
                    clothing.setTextColor(Color.rgb(165, 224, 178));

                    String desc = jweather.getString("description").toUpperCase();
                    String countryName = jSys.getString("country");
                    String cityName = jresponse.getString("name");
                    String wind = jWind.getString("speed");
                    double tempF = (jMain.getDouble("temp") - 273.15) * 1.8 + 32;
                    double feelsLike = (jMain.getDouble("feels_like") - 273.15) * 1.8 + 32;
                    float pressure = jMain.getInt("pressure");
                    int humidity = jMain.getInt("humidity");

                    output += "\n Humidity: " + humidity + "%"
                            + "\n Wind Speed: " + wind + "m/s";
                    String tvClothes = "";
                    if(tempF < 34){
                        tvClothes = "Bundle up! Its really cold!";
                    }
                    else if(tempF > 34 && tempF < 50){
                        tvClothes = "How perfect!";
                    }
                    else{
                        tvClothes = "Dress lightly. Its hot out there!";
                    }

                    opening.setText("in " + cityName + " (" + countryName + "), it is");
                    TVtemp.setText(df.format(tempF) + "°F");
                    butitfeelslike.setText("but it feels like");
                    TVfeel.setText(df.format(feelsLike) + "°F");
                    thereAre.setText("Current conditions include");
                    TVDesc.setText(desc);
                    clothing.setText(tvClothes);
                    result.setText(output);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}