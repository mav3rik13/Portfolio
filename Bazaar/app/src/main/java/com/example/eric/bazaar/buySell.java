package com.example.eric.bazaar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class buySell extends AppCompatActivity {
    EditText edt;
    String order=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);

        edt=(EditText) findViewById(R.id.search);
    }

    public void buy(View view){

            Intent intent = new Intent(this, viewItems.class);
        if(edt.getText().toString().compareTo("Search")==0){
            intent.putExtra("message", "Buy;"+order);}
        else {
            intent.putExtra("message", "Buy:"+edt.getText().toString()+";"+order);
        }
            startActivity(intent);

    }

    public void sell(View view){

        Intent intent = new Intent(this, viewItems.class);
        if(edt.getText().toString().compareTo("Search")==0){
        intent.putExtra("message", "Sell;"+order);}
        else {
            intent.putExtra("message", "Sell:"+edt.getText().toString()+";"+order);
        }
        startActivity(intent);

    }

    public void bid(View view){
        Intent intent = new Intent(this, viewItems.class);
        if(edt.getText().toString().compareTo("Search")==0){
            intent.putExtra("message", "Bid;"+order);}
        else {
            intent.putExtra("message", "Bid:"+edt.getText().toString()+";"+order);
        }
        startActivity(intent);
    }

    public void all(View view){

        Intent intent = new Intent(this, viewItems.class);
        if(edt.getText().toString().compareTo("Search")==0){
            intent.putExtra("message", "All;"+order);}
        else {
            intent.putExtra("message", "All:"+edt.getText().toString()+";"+order);
        };
        startActivity(intent);

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dec:
                if (checked)
                    order="ORDER BY price DESC";
                    break;
            case R.id.asc:
                if (checked)
                    order="ORDER BY price ASC";
                    break;
        }
    }
}
