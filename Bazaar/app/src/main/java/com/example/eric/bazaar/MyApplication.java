package com.example.eric.bazaar;

import android.app.Application;

/**
 * Created by eric on 4/3/17.
 */

public class MyApplication extends Application {
    private String ID;
    private String uName;
    private String eMail;
    private String item;
    private String rating;

    public void setRating(String s){
        rating=s;
    }

    public String getRating(){
        return rating;
    }

    public void setID(String s){
        ID=s;
    }

    public String getID(){
        return ID;
    }

    public void setuName(String s){
        uName=s;
    }

    public String getuName(){
        return uName;
    }

    public void setEmail(String s){
        eMail=s;
    }

    public String getEmail(){
        return eMail;
    }

    public void setItem(String x){item=x;}

    public String getItem(){return item;}

}

