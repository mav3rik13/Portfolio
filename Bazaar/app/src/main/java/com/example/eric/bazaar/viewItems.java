package com.example.eric.bazaar;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class viewItems extends AppCompatActivity {
    private final String USER_AGENT = "Mozilla/5.0";
    ArrayList<uItem> results;
    String message;
    String[] split;
    String[] user;
    String[] rating;
    String[] type;
    String[] iId;
    String[] mSplit;
    String search;
    String[] end;
    String order;
    int s=0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] split;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");
        split=message.split(";");
        message=split[0];
        order=split[1];
        if(message.contains(":")){
            mSplit=message.split(":");
            search=mSplit[1];
            message=mSplit[0];
            s=1;
        }


        ArrayList image_details = null;
        try {
            image_details = getListData();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        final ListView lv1 = (ListView) findViewById(R.id.custom_list);
        lv1.setAdapter(new CustomListAdapter(this, image_details));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                uItem nzt = (uItem) lv1.getItemAtPosition(position);

                Intent intent = new Intent(viewItems.this, itemPage.class);
                String message=iId[Integer.parseInt(nzt.getID())];
                message+=";"+nzt.getName();
                message+=";"+nzt.getdesc();
                message+=";"+nzt.getprice();
                message+=";"+user[Integer.parseInt(nzt.getID())];
                message+=";"+rating[Integer.parseInt(nzt.getID())];
                message+=";"+type[Integer.parseInt(nzt.getID())];
                message+=";"+end[Integer.parseInt(nzt.getID())];


                intent.putExtra("message", message);
                startActivity(intent);

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList getListData() throws ParserConfigurationException, IOException, SAXException {

        results = new ArrayList<uItem>();
        uItem newsData;
        String url;


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

        if(!message.equals("All")) {
            url = "http://cis-linux2.temple.edu/~tud17750/bazaar/getItems.php?type=" + message+"&order="+order;
        }
            else{
            url = "http://cis-linux2.temple.edu/~tud17750/bazaar/getAllItems.php?order="+order;
        }
            url= url.replaceAll(" ","%20");
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
                String[] item=split[0].split(":");
                String[] desc=split[1].replace('_',' ').split(":");
                user=split[2].split(":");
                String[] price=split[3].split(":");
                rating=split[4].split(":");
                type=split[7].split(":");
                iId=split[8].split(":");
                end=split[9].split(":");

                newsData = new uItem();

                for(int i=1;i<item.length;i++) {
                    newsData = new uItem();
                    newsData.setName(item[i]+": "+type[i]+"     Expires"+end[i]);
                    newsData.setdesc(desc[i]);
                    newsData.setprice(price[i]);
                    newsData.setUser(user[i]+": "+rating[i]);
                    newsData.setID(Integer.toString(i));
                    if(s==0) {
                        results.add(newsData);
                    }else{
                        if(item[i].contains(search)||desc[i].contains(search)||user[i].contains(search)){
                            results.add(newsData);
                        }
                    }
                }

            }

        Collections.reverse(Arrays.asList(results));


        return results;


    }







}
