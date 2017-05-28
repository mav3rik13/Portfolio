package com.example.eric.bazaar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class menu extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        EditText e = (EditText) findViewById (R.id.editText);
        e.setText(((MyApplication) this.getApplication()).getuName()+": "+((MyApplication) this.getApplication()).getRating());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void post(View view){
        Intent intent = new Intent(this, postItem.class);
        startActivity(intent);
    }

    public void items(View view){
        Intent intent = new Intent(this, buySell.class);
        startActivity(intent);
    }

    public void inbox(View view){
        Intent intent = new Intent(this, inbox.class);
        startActivity(intent);
    }

    public void map(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void myItems(View view){
        Intent intent = new Intent(this, viewItems.class);
        intent.putExtra("message", "All:"+((MyApplication) this.getApplication()).getuName());
        startActivity(intent);
    }
}
