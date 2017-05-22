package com.example.eric.bazaar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class inbox extends AppCompatActivity {
    private final String USER_AGENT = "Mozilla/5.0";
    ArrayList<uMess> results;
    String[] sender;
    String[] rating;
    String[] ratings;
    String[] srat;
    String[] rrat;
    String[] iid;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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
        lv1.setAdapter(new inboxListAdapter(this, image_details));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                uMess nzt = (uMess) lv1.getItemAtPosition(position);

                Intent intent = new Intent(inbox.this, sendMessage.class);
                String message=nzt.getID();
                message+=";"+sender[position+1];
                message+=";"+nzt.gettext();
                message+=";"+nzt.getitem();
                message+=";"+rating[position+1];
                message+=";"+ratings[position+1];
                message+=";"+iid[position+1];
                message+=";"+srat[position+1];
                message+=";"+rrat[position+1];
                //message+=";"+nzt.getUser;();

                intent.putExtra("message", message);
                startActivity(intent);

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList getListData() throws ParserConfigurationException, IOException, SAXException {

        results = new ArrayList<uMess>();
        uMess newsData;
        String user=((MyApplication) this.getApplication()).getuName();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            String url = "http://cis-linux2.temple.edu/~tud17750/bazaar/getMessages.php?user="+user;
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
                sender=split[0].split(";");
                String[] mess=split[1].replace('_',' ').split(";");
                String[] item=split[2].split(";");
                rating=split[3].split(";");
                ratings=split[4].split(";");
                iid=split[5].split(";");
                rrat=split[6].split(";");
                srat=split[7].split(";");


                for(int i=1;i<item.length;i++) {
                    newsData = new uMess();
                    newsData.setitem(item[i]);
                    newsData.settext(mess[i]);
                    newsData.setSender(sender[i]+": "+rating[i]);


                    results.add(newsData);
                }
            }else{
                Context context = getApplicationContext();
                CharSequence text = "Inbox Empty";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }




        return results;


    }
}
