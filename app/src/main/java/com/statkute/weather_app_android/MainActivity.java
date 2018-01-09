package com.statkute.weather_app_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    TextView tempTextView;
    TextView dateTextView;
    TextView weatherDescTextView;
    TextView cityTextView;
    ImageView weatherImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = (TextView) findViewById(R.id.tempTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        weatherDescTextView = (TextView) findViewById(R.id.weatherDescTextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);
        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);


        dateTextView.setText(getCurrentDate());

        String url = "http://api.openweathermap.org/data/2.5/weather?id=2655603&appid=684f4d1735c0d4fe9b6a8a6c506e2cdc";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //tempTextView.setText("Response: " + response.toString());
                        try {
                            JSONObject mainJSONObject = response.getJSONObject("main");

                            String temp = Integer.toString ((int) Math.round(mainJSONObject.getDouble("temp") - 273.15));
                            tempTextView.setText(temp);

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String weatherDescription = firstWeatherObject.getString("description");
                            weatherDescTextView.setText(weatherDescription);

                            String city = response.getString("name");
                            cityTextView.setText(city);

                            int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                            weatherImageView.setImageResource(iconResourceId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

// Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
}
