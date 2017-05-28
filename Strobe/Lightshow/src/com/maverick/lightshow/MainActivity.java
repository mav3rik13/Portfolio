package com.maverick.lightshow;

import android.content.Context;
import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Arrays;
import android.app.Activity;  
import android.os.Bundle;  
import android.widget.ListView;  
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;



public class MainActivity extends ActionBarActivity {

	public static Camera cam = null;
	public static int LEDstate = 0;
	private String[] vA;
	private ArrayAdapter<String> adapter ;  
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> data = new ArrayList<String>();
	public static String user; 
	private static boolean mPhone=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView songView = (ListView)findViewById(R.id.PhoneMusicList);
		fillList();

	        
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1, list);
		songView.setAdapter(adapter);
		songView.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> adapter, View view, int position,
	                long arg3) {
	        	int item = position;
	        	user=data.get(position);
	        	
	        }
	        //Intent intent = new Intent(this, Lghts.class);
			//startActivity(intent);
	    });
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}



	public void startLights(View view){
		Intent intent = new Intent(this, Lghts.class);
		startActivity(intent);
	}
	
	public void back(View view){
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
	}
	
	public void check(View view){
		mPhone=true;
	}
	
	public void fillList(){
		Uri musicUri;
		ContentResolver musicResolver = getContentResolver();
		
		musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		
		
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor !=null && musicCursor.moveToFirst()){
			
			  int titleColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.TITLE);
			  int idColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.DATA);
			  int artistColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.ARTIST);
			  
			  do {
			    String thisId = musicCursor.getString(idColumn);
			    String thisTitle = musicCursor.getString(titleColumn);
			    String thisArtist = musicCursor.getString(artistColumn);
			    int h =1;
			    if(h==1){
			    list.add(thisTitle);
			    data.add(thisId);
			  }
			  }
			  while (musicCursor.moveToNext());
			}


	}
	
	public static void setMic(boolean set){
		mPhone=set;
	}
	
	public static String getData(){
		return user;
	}
	
	public boolean getMic(){
		return mPhone;
	}
	}
	

