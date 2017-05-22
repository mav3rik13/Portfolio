package com.example.eric.bazaar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private final String USER_AGENT = "Mozilla/5.0";
    EditText edtuserid,edtpass;
    Button btnlogin;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtuserid = (EditText) findViewById(R.id.userName);
        edtpass = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.button3);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void login(View view) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        String s=edtuserid.getText().toString();
        String t=edtpass.getText().toString();
        String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/login.php?inputName="+s+"&inputPassword="+t;
        url=url.replaceAll(" ", "");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
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
            xmlStringBuilder.append(inputLine);
        }

        in.close();
        ByteArrayInputStream input =  new ByteArrayInputStream(
                xmlStringBuilder.toString().getBytes("UTF-8"));
        System.out.println("Got here");

        if (!Objects.equals("Query failed",xmlStringBuilder.toString())) {

            Document doc = null;//Crashes here
            try {
                doc = builder.parse(input);
            } catch (SAXException e) {
                e.printStackTrace();
            }

            NodeList name;
            name = doc.getElementsByTagName("user");
            System.out.println(name.item(0).getFirstChild().getTextContent());

            String par = name.item(0).getFirstChild().getTextContent();
            String uName;
            String ID;
            String Email;

            par = par.replaceAll("\t", "");

            String[] split = par.split(" ");

            ((MyApplication) this.getApplication()).setID(split[0]);
            ((MyApplication) this.getApplication()).setEmail(split[1]);
            ((MyApplication) this.getApplication()).setuName(split[2]);
            ((MyApplication) this.getApplication()).setRating(split[3]);

            Intent intent = new Intent(this, menu.class);
            updateItems();
            startActivity(intent);
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = "Incorrect Password or userName";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }

    public void register(View view){
        Intent intent = new Intent(this, createProfile.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateItems() throws IOException, ParserConfigurationException, SAXException {
        String USER_AGENT = "Mozilla/5.0";

        String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/update.php";
        url=url.replaceAll(" ", "_");
        URL obj = new URL(url);
        HttpURLConnection con;
        con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        //System.out.println("Got here");



        while ((inputLine = in.readLine()) != null) {
            xmlStringBuilder.append(inputLine);
        }
        in.close();
        if (!Objects.equals("Query failed",xmlStringBuilder.toString())) {

            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));


            Document doc = builder.parse(input);

            NodeList name = doc.getElementsByTagName("user");
            String par = name.item(0).getFirstChild().getTextContent();


            par = par.replaceAll("\t", "");


            String[] split = par.split(" ");
            String[] id=split[1].split(":");
            String[] edate=split[0].split(":");
            String[] type=split[2].split(":");

            DateFormat dtf=new SimpleDateFormat("yyyy/MM/dd");
            Date date=new Date();
            String sdate=dtf.format(date);


            for(int i=1;i<id.length;i++){
                if(sdate.compareTo(edate[i])==0){



                    if(type[i].compareTo("Bid")==0){
                        url = "http://cis-linux2.temple.edu/~tud17750/bazaar/getBidInfo.php?id="+id[i];
                        url=url.replaceAll(" ", "_");
                        URL obj2 = new URL(url);
                        HttpURLConnection con2;
                        con2 = (HttpURLConnection) obj2.openConnection();

                        // optional default is GET
                        con2.setRequestMethod("GET");

                        //add request header
                        con2.setRequestProperty("User-Agent", USER_AGENT);

                        DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder1 = factory1.newDocumentBuilder();

                        StringBuilder xmlStringBuilder1 = new StringBuilder();
                        BufferedReader in1 = new BufferedReader(
                                new InputStreamReader(con2.getInputStream()));
                        String inputLine1;
                        StringBuffer response1 = new StringBuffer();
                        //System.out.println("Got here");



                        while ((inputLine1 = in1.readLine()) != null) {
                            xmlStringBuilder1.append(inputLine1);
                        }
                        in.close();
                        if (!Objects.equals("Query failed",xmlStringBuilder1.toString())) {

                            ByteArrayInputStream input1 = new ByteArrayInputStream(xmlStringBuilder1.toString().getBytes("UTF-8"));


                            Document doc1 = builder1.parse(input1);

                            NodeList name1 = doc1.getElementsByTagName("user");
                            String par1 = name1.item(0).getFirstChild().getTextContent();


                            par1 = par1.replaceAll("\t", "");

                            String split1[] = par1.split(" ");
                            String message="User "+split1[1]+" won the auction for "+split1[3]+" for "+split1[2]+" respond to this to contact him";


                            url = "http://cis-linux2.temple.edu/~tud17750/bazaar/sendMessage.php?text=" + message + "&item=" + split1[3] + "&receiver=" + split1[1] + "&sender=" + split1[0] + "&rating=" + split1[4] + "&rrat=0&srat=0";

                            url=url.replace(" ","_");

                            URL obj3 = new URL(url);
                            HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();

                            // optional default is GET
                            con3.setRequestMethod("GET");

                            //add request header
                            con3.setRequestProperty("User-Agent", USER_AGENT);

                            int responseCode3 = con3.getResponseCode();

                            System.out.println("Response Code : " + responseCode3);
                        }

                    }
                    url = "http://cis-linux2.temple.edu/~tud17750/bazaar/removeItem.php?id="+id[i];
                    url=url.replaceAll(" ", "_");
                    URL obj1 = new URL(url);
                    HttpURLConnection con1;
                    con1 = (HttpURLConnection) obj1.openConnection();

                    // optional default is GET
                    con1.setRequestMethod("GET");

                    //add request header
                    con1.setRequestProperty("User-Agent", USER_AGENT);
                    int responseCode5 = con1.getResponseCode();
                }
            }
        }
    }
}
