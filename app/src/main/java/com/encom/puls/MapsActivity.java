package com.encom.puls;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

//import org.apache.commons.logging.Log;

public class MapsActivity extends FragmentActivity {
    private Preview mPreview;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Marker myPosition;
    GPSTracker gps;
    Camera mCamara;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gps = new GPSTracker(this);
        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
        setUpMapIfNeeded();
        Button btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
                // check if GPS enabled
                if(gps.canGetLocation()){
                    gps.getLocation();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String provider=" None";
                    if(gps.isGPSEnabled){
                        provider = " GPS";
                    }
                    if(gps.isNetworkEnabled){
                        provider = " Network";
                    }
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude +"\nProvider: " + provider, Toast.LENGTH_LONG).show();
                    myPosition.setPosition(new LatLng(latitude, longitude));
                    mMap.getMyLocation();
                    mMap.moveCamera(CameraUpdateFactory.zoomIn());
                    gps.stopUsingGPS();
                }else{
                    gps.showSettingsAlert();
                }

            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myPosition.setPosition(latLng);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
               myPosition = setUpMap();
               myPosition.setPosition(new LatLng(gps.getLatitude(),gps.getLongitude()));
            }
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private Marker setUpMap() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_streetview_pegman_64);
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(getString( R.string.you_location )).icon(icon).draggable(true));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent();

                return false;
            }
        });
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        return marker;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.take_photo:

                intent.setClass(MapsActivity.this, CamaraActivity.class);
                startActivity(intent);
                return true;
            case R.id.salir:
                intent.setClass(MapsActivity.this, CamaraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("CERRAR", true);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
