package com.example.eric.bazaar;

import android.Manifest;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    Location mLastLocation;
    String[] lat;
    String[] lng;
    String[] item;
    LatLng pos;
    private final String USER_AGENT = "Mozilla/5.0";
    String[] split;
    String[] desc;
    String[] usr;
    String[] price;
    String[] rating;
    String[] id;
    String[] type;
    String[] end;
    String[] iId;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        try {
            getListData();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        Marker h;

        id=new String[45];

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(39.9797607, -75.1502969);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Test"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng hold;
        String title;
        for(int i=1;i<item.length;i++){
            title=item[i]+"\n"+usr[i]+": "+rating[i]+"\n"+desc[i];
            hold=new LatLng(Float.parseFloat(lat[i]),Float.parseFloat(lng[i]));
            h=mMap.addMarker(new MarkerOptions().position(hold).title(item[i]));
            id[i]=h.getId();

        }


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String comp=marker.getId();
        int i=1;
        while(comp.compareTo(id[i])!=0){
            i++;
        }
        Intent intent = new Intent(MapsActivity.this, itemPage.class);

        String message= iId[i];
        message+=";"+item[i];
        message+=";"+desc[i];
        message+=";"+price[i];
        message+=";"+usr[i];
        message+=";"+rating[i];
        message+=";"+type[i];
        message+=";"+end[i];

        intent.putExtra("message", message);
        startActivity(intent);
    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            pos= new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));

        mMap.animateCamera(CameraUpdateFactory.zoomTo( 15.0f ));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList getListData() throws ParserConfigurationException, IOException, SAXException {

        ArrayList<uItem> results = new ArrayList<uItem>();
        uItem newsData;
        String url;


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


            url = "http://cis-linux2.temple.edu/~tud17750/bazaar/getAllItems.php";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        StringBuilder xmlStringBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        //System.out.println("Got here");


        while ((inputLine = in.readLine()) != null) {
            xmlStringBuilder.append(inputLine);}

        in.close();
        if (!Objects.equals("Query failed",xmlStringBuilder.toString())) {

            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));


            Document doc = builder.parse(input);

            NodeList name = doc.getElementsByTagName("user");
            String par = name.item(0).getFirstChild().getTextContent();




            par = par.replaceAll("\t", "");

            split = par.split(" ");
            item=split[0].split(":");
            desc=split[1].replace('_',' ').split(":");
            usr=split[2].split(":");
            price=split[3].split(":");
            rating=split[4].split(":");
            lat=split[5].split(":");
            lng=split[6].split(":");
            type=split[7].split(":");
            iId=split[8].split(":");
            end=split[9].split(":");



        }




        return results;


    }
}
