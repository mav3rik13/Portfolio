package com.maverick.lightshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class settings extends ActionBarActivity {
	static private int musicType=0;
	static private int bassColor=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void setMusicType(View view){
	}
	
	public int getMusicType(View view){
		return musicType;
	}
	
	public void setBassColor(View view){
		
	}
	
	public int getBassColor(View view){
		return bassColor;
	}
	
	public void goHome(View view){
		Intent intent=new Intent(this,Home.class);
		startActivity(intent);
	}
	
	

}
