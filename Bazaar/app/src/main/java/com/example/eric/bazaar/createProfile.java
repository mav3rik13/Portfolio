package com.example.eric.bazaar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.sql.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class createProfile extends AppCompatActivity {
    private final String USER_AGENT = "Mozilla/5.0";
    EditText password;
    EditText uName;
    EditText Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        uName  = (EditText)findViewById(R.id.inpName);
        password   = (EditText)findViewById(R.id.inpPass);
        Email   = (EditText)findViewById(R.id.inpEmail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void create(View view) throws IOException {
        String s=uName.getText().toString();
        String t=password.getText().toString();
        String e=Email.getText().toString();

        String USER_AGENT = "Mozilla/5.0";

        String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/registration.php?inputName="+s+"&inputPassword="+t+"&inputEmail="+e;
        url=url.replaceAll(" ", "");
        URL obj = new URL(url);
        HttpURLConnection con;
        con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

