package com.example.eric.bazaar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class itemPage extends AppCompatActivity {
    TextView Name;
    TextView Desc;
    TextView Price;
    TextView Seller;
    Button edit;
    EditText mess;
    private final String USER_AGENT = "Mozilla/5.0";
    String[] split;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");



        split=message.split(";");


        if(split[6].equals("Bid")){
            setContentView(R.layout.bid_page);
            Name = (TextView) findViewById(R.id.Name);
            Desc = (TextView) findViewById(R.id.Description);
            Price = (TextView) findViewById(R.id.Price);
            mess = (EditText) findViewById(R.id.Message);
            Seller= (TextView) findViewById(R.id.seller);
            Price.setText("Current bid: $"+split[3]);

        }
        else{
            setContentView(R.layout.activity_item_page);

            Name = (TextView) findViewById(R.id.Name);
            Desc = (TextView) findViewById(R.id.Description);
            Price = (TextView) findViewById(R.id.Price);
            mess = (EditText) findViewById(R.id.Message);
            Seller= (TextView) findViewById(R.id.seller);
            Price.setText("$"+split[3]);
        }

        Name.setText(split[1]+": "+ split[6]+"  Expires "+split[7]);
        Desc.setText(split[2]);
        Seller.setText(split[4]+": "+split[5]);
        edit= (Button) findViewById(R.id.button16);
        if(split[4].compareTo(((MyApplication) this.getApplication()).getuName())!=0){
            edit.setVisibility(View.GONE);
        }
    }

    public void sendMessage(View view) throws ParserConfigurationException, IOException {
        String message=mess.getText().toString();
        message=message.replace(' ','_');

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        String user=((MyApplication) this.getApplication()).getuName();
        String rating =((MyApplication) this.getApplication()).getRating();

        String rec=split[4].replace(":", "");
        rec=rec.replace(".", "");


        String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/sendMessage.php?text="+message+"&item="+split[1]+"&receiver="+rec+"&sender="+user+"&rating="+rating;

        url=url.replace(" ","");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);

        Intent intent = new Intent(this, menu.class);
        startActivity(intent);

    }

    public void bid(View view)throws ParserConfigurationException, IOException{
        String bid=mess.getText().toString();

        double b=Double.parseDouble(bid);
        double p=Double.parseDouble(split[3]);


        if(b>p+1) {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            String user = ((MyApplication) this.getApplication()).getuName();
            String rating = ((MyApplication) this.getApplication()).getRating();


            String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/bid.php?sender=" + user + "&bid=" + bid + "&item=" + split[0];

            url = url.replace(" ", "");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);

            Intent intent = new Intent(this, menu.class);
            startActivity(intent);
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = "Please bid at least a dollar higher than the current bid";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void remove(View view) throws IOException {
        String USER_AGENT = "Mozilla/5.0";

        String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/removeItem.php?id="+split[0];
        url=url.replaceAll(" ", "_");
        URL obj = new URL(url);
        HttpURLConnection con;
        con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        Intent intent = new Intent(this, menu.class);
        startActivity(intent);
    }

}

