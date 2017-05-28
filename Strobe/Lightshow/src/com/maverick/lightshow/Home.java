package com.maverick.lightshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class Home extends ActionBarActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

	}
	
	public void startMain(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	public void startMic(View view){
		Lghts.setMic(true);
		Intent intent = new Intent(this, Lghts.class);
		startActivity(intent);
	}
	
	public void startSettings(View view){
		Intent intent = new Intent(this, settings.class);
		startActivity(intent);
		
	}

}
