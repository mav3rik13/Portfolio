package com.example.eric.bazaar;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public abstract class Register extends AsyncTask {
    String s,t,e;


    protected Object doInBackground(String... set) throws IOException {
        String USER_AGENT = "Mozilla/5.0";

        String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/registration.php?inputName="+s+"&inputPassword="+t+"&inputEmail="+e;
        URL obj = new URL(url);
        HttpURLConnection con;
        con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        return null;
    }


    protected abstract void onPostExecute();
}
