package com.example.eric.bazaar;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class postItem extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final String USER_AGENT = "Mozilla/5.0";
    EditText edtname,edtdesc,edtprice, length;
    String type;
    GoogleApiClient mGoogleApiClient;
    String lat;
    String lng;
    Location mLastLocation;
    LatLng pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        edtname = (EditText) findViewById(R.id.name);
        edtdesc = (EditText) findViewById(R.id.desc);
        edtprice = (EditText) findViewById(R.id.price);
        length = (EditText) findViewById(R.id.len);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void postBuy(View view) throws IOException {
        type="Sell";
        post(view);
    }

    public void postSell(View view) throws IOException {
        type="Buy";
        post(view);
    }

    public void postBid(View view) throws IOException {
        type="Bid";
        post(view);
    }

    public void post(View view) throws IOException {
        String s=edtname.getText().toString();
        String t=edtprice.getText().toString();
        String e=edtdesc.getText().toString();
        if(s.compareTo("")!=0&&t.compareTo("")!=0&&e.compareTo("")!=0) {
            String k = ((MyApplication) this.getApplication()).getuName();
            String r = ((MyApplication) this.getApplication()).getRating();
            lat = Double.toString(pos.latitude);
            lng = Double.toString(pos.longitude);
            DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            String sdate = dtf.format(date);
            int day;
            if(length.getText().toString().compareTo("")==0){
                day=1;
            }else {
                day = Integer.parseInt(length.getText().toString());
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(Calendar.DATE, day);
            date = cal.getTime();
            String edate = dtf.format(date);

            String USER_AGENT = "Mozilla/5.0";

            String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/postItem.php?inputName=" + s + "&inputDescription=" + e + "&inputPrice=" + t + "&seller=" + k + "&type=" + type + "&rating=" + r + "&lat=" + lat + "&lng=" + lng + "&date=" + sdate + "&edate=" + edate;
            url = url.replaceAll(" ", "_");
            URL obj = new URL(url);
            HttpURLConnection con;
            con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);

            Intent intent = new Intent(this, menu.class);
            startActivity(intent);
        }
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
