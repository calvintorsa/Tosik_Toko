package com.example.calvin.tosik_toko;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

;

public class CobaPetaActivity extends AppCompatActivity implements OnMapReadyCallback,
        OnMarkerDragListener {

    Marker marker;
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coba_peta);
        if(googleServicesAvaliable()){
            Toast.makeText(this, "Perfect", Toast.LENGTH_LONG).show();
            initMap();
        }else{
            ///
        }
    }

    private void initMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvaliable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvaliable = api.isGooglePlayServicesAvailable(this);
        if(isAvaliable== ConnectionResult.SUCCESS){
            return true;
        }else if (api.isUserResolvableError(isAvaliable)){
            Dialog dialog = api.getErrorDialog(this,isAvaliable,0);
            dialog.show();
        }else{
            Toast.makeText(this, "Cant connect to services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        goToLocation(-6.876239, 107.604286,15);
        mGoogleMap.setOnMarkerDragListener(this);

    }

    private void goToLocation(double lat, double lon,float zoom) {
        LatLng ll = new LatLng(lat,lon);


        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }


    public void geoLocate(View view) throws IOException {
        EditText et = (EditText) findViewById(R.id.editText);
        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();


        double lat = address.getLatitude();
        double lng = address.getLongitude();
        LatLng mark = new LatLng(lat, lng);
       setMarker(locality,lat,lng);

        goToLocation(lat, lng, 15);

    }

    private void setMarker(String locality, double lat, double lng) {
       if(marker != null){
           marker.setVisible(false);
        }


        MarkerOptions options = new MarkerOptions().title("Tarik ke lokasi anda")
                .draggable(true)
                .position(new LatLng(lat, lng));

        marker =  mGoogleMap.addMarker(options);
        marker.showInfoWindow();


    }


    public void setLokasi(View view){
        LatLng ll = marker.getPosition();
        double latitude = ll.latitude;
        double longitude = ll.longitude;

        Intent i = new Intent(CobaPetaActivity.this, registerActivity.class);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {

        } else {

            i.putExtra("email",extras.getString("email"));
            i.putExtra("password",extras.getString("password"));
            i.putExtra("namaToko", extras.getString("namaToko"));
            i.putExtra("alamat",extras.getString("alamat"));
            i.putExtra("noTelpon",extras.getString("noTelpon"));


        }


        i.putExtra("latitude",latitude);
        i.putExtra("longitude",longitude);
        startActivity(i);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));
    }
}
