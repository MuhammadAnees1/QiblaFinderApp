package com.example.qiblafinderapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private ImageView imageView;
    BottomNavigationView bottomNavigationView;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private LocationManager locationManager;
    private float[] gravity;
    private float[] geomagnetic;
    private float azimuth;
    private Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                        return true;
                    case R.id.menu_compass:

                        Intent iv = new Intent(getApplicationContext(), CompassActivity.class);
                        startActivity(iv);

                        return true;
                    case R.id.menu_calender:

                        Intent ive = new Intent(getApplicationContext(), PrayerActivity.class);
                        startActivity(ive);
                        return true;

                    case R.id.menu_services:

                        Intent intent = new Intent(getApplicationContext(), ServiesActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if location permission is granted
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request location permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                } else {
                    // Location permission already granted, get the current location
                    getCurrentLocation();
                }
                Log.d("Location", "Clicked on imageView");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        // Request GPS updates
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        // Remove GPS updates
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            gravity = event.values.clone();
        } else if (event.sensor == magnetometer) {
            geomagnetic = event.values.clone();
        }
        if (gravity != null && geomagnetic != null) {
            float[] rotationMatrix = new float[9];
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]);
                // Move the image based on the azimuth value
                moveImage();
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        moveImage();
        Log.d("Location", "New location received: " + location.getLatitude() + ", " + location.getLongitude());
        Log.d("Location", "currentLocation = " + currentLocation);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changes if needed
    }
    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled if needed
    }
    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled if needed
    }
    private void moveImage() {
        if (imageView != null && currentLocation != null) {
            // Calculate the Qibla direction based on the current location
            double qiblaDirection = getQiblaDirection(currentLocation.getLatitude(), currentLocation.getLongitude());
            // Calculate the desired rotation based on the azimuth and Qibla direction
            float rotation = (float) (azimuth - qiblaDirection);

            Log.d("moveImage()", "Lat: " + qiblaDirection);

            // Apply the rotation to the image view
            imageView.setRotation(rotation);
        }
    }
    private double getQiblaDirection(double latitude, double longitude) {
        double kaabaLatitude = 21.4225; // Latitude of the Kaaba
        double kaabaLongitude = 39.8262; // Longitude of the Kaaba

        double longitudeDifference = kaabaLongitude - longitude;
        double y = Math.sin(Math.toRadians(longitudeDifference));
        double x = Math.cos(Math.toRadians(latitude)) * Math.tan(Math.toRadians(kaabaLatitude)) - Math.sin(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitudeDifference));
        double qiblaDirection = Math.toDegrees(Math.atan2(y, x));
        qiblaDirection = (qiblaDirection + 360) % 360;

        Log.d("Location", "Qibla direction calculated: " + qiblaDirection);

        return qiblaDirection;
    }
    private void getCurrentLocation() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling ActivityCompat#requestPermissions here to request the missing permissions and handle the case where the user grants the permission. See the documentation for ActivityCompat#requestPermissions for more details.
                return;
            } else {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    currentLocation = lastKnownLocation;
                    moveImage();
                    Log.d("Location", "Current location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
                } else {
                    // Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, get the current location
                getCurrentLocation();
                Log.d("Location", "Location permission granted");
            } else {
                // Location permission denied, handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                Log.d("Location", "Location permission denied");
            }
        }
    }
}