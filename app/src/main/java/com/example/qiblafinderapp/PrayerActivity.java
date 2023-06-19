package com.example.qiblafinderapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;

public class PrayerActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        RecyclerView recyclerView = findViewById(R.id.recycleView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();
    }
    private void loadData() {
        long millis = Calendar.getInstance().getTimeInMillis();
        String customURL = "https://api.aladhan.com/v1/calendarByCity/2017/4?city=Multan&country=Pakistan%Pakistan&method=2";
        String url = customURL.replace("2017/4", convertDate(String.valueOf(millis), "yyyy/MM")).replace("Multan", "Islamabad");

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("OK")) {
                        ArrayList<NamazModel> namazModels = new ArrayList<>();

                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject timingObject = dataArray.getJSONObject(i).getJSONObject("timings");
                            JSONObject dateObject = dataArray.getJSONObject(i).getJSONObject("date");

                            String fajr = timingObject.getString("Fajr");
                            String sunrise = timingObject.getString("Sunrise");
                            String dhuhr = timingObject.getString("Dhuhr");
                            String asr = timingObject.getString("Asr");
                            String sunset = timingObject.getString("Sunset");
                            String maghrib = timingObject.getString("Maghrib");
                            String isha = timingObject.getString("Isha");
                            String imsak = timingObject.getString("Imsak");
                            String midnight = timingObject.getString("Midnight");
                            String firstThird = timingObject.getString("Firstthird");
                            String lastThird = timingObject.getString("Lastthird");
                            String date = dateObject.getString("readable");

                            NamazModel namazModel = new NamazModel(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha, imsak, midnight, firstThird, lastThird, date);

                            namazModels.add(namazModel);
                        }
                        PrayerTimeAdapter adapter = new PrayerTimeAdapter(PrayerActivity.this, namazModels);
                        RecyclerView recyclerView = findViewById(R.id.recycleView1);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(PrayerActivity.this, "General Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

