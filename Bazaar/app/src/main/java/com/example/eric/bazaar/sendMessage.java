package com.example.eric.bazaar;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class sendMessage extends AppCompatActivity {
    RatingBar ratingBar;
    TextView Sender;
    TextView Mess;
    TextView Item;
    EditText text;
    private final String USER_AGENT = "Mozilla/5.0";
    String[] split;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Sender = (TextView) findViewById(R.id.sender);
        Mess = (TextView) findViewById(R.id.mess);
        Item = (TextView) findViewById(R.id.Item);
        text = (EditText) findViewById(R.id.text);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        split=message.split(";");

        Sender.setText(split[1]+": "+split[4]);
        Mess.setText(split[2]);
        Item.setText(split[3]);

        if(split[7].compareTo("0")!=0){
            ratingBar.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void send(View view) throws ParserConfigurationException, IOException, SAXException {
        String message=text.getText().toString();
        message=message.replace(' ','_');
        if(message.compareTo("")==0){
            message="_";
        }
        if(ratingBar.getRating()!=0){
            split[7]="1";
        }

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        String user=((MyApplication) this.getApplication()).getuName();
        String rating=((MyApplication) this.getApplication()).getRating();
        String url;


        url = "http://cis-linux2.temple.edu/~tud17750/bazaar/sendMessage.php?text=" + message + "&item=" + split[3] + "&receiver=" + split[1] + "&sender=" + user + "&rating=" + rating + "&rrat="+ split[7]+"&srat="+split[6];





        url=url.replace(" ","");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);

        if(ratingBar.getRating()!=0) {

            DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder1 = factory1.newDocumentBuilder();
            url = "http://cis-linux2.temple.edu/~tud17750/bazaar/gatRating.php?id="+split[1];
            URL obj3 = new URL(url);
            HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();

            // optional default is GET
            con3.setRequestMethod("GET");

            //add request header
            con3.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode3 = con3.getResponseCode();

            StringBuilder xmlStringBuilder = new StringBuilder();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con3.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            //System.out.println("Got here");


            while ((inputLine = in.readLine()) != null) {
                xmlStringBuilder.append(inputLine);
            }

            String[] r;

            in.close();
            if (!Objects.equals("Query failed",xmlStringBuilder.toString())) {

                ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));


                Document doc = builder1.parse(input);

                NodeList name = doc.getElementsByTagName("user");
                String par = name.item(0).getFirstChild().getTextContent();


                par = par.replaceAll("\t", "");

                r = par.split(" ");

                System.out.println("Response Code : " + responseCode);


                int nRatings = Integer.parseInt(r[1]);
                double nRating = Double.parseDouble(r[0]);
                nRating *= nRatings;
                nRating += ratingBar.getRating();
                nRatings++;
                nRating = nRating / nRatings;

                url = "http://cis-linux2.temple.edu/~tud17750/bazaar/rate.php?receiver="+split[1]+"&rating="+nRating+"&ratings="+nRatings;

                obj = new URL(url);
                HttpURLConnection con2 = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con2.setRequestMethod("GET");

                //add request header
                con2.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode2 = con2.getResponseCode();

                System.out.println("Response Code : " + responseCode2);

            }


            url = "http://cis-linux2.temple.edu/~tud17750/bazaar/messUp.php?id="+split[6];
            URL obj4 = new URL(url);
            HttpURLConnection con4 = (HttpURLConnection) obj4.openConnection();

            // optional default is GET
            con4.setRequestMethod("GET");

            //add request header
            con4.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode4 = con4.getResponseCode();


        }




        Intent intent = new Intent(this, menu.class);
        startActivity(intent);

    }
}
