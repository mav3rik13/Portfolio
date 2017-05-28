package com.maverick.lightshow;




import java.io.File;
import java.io.IOException;
import java.lang.Math;

import com.maverick.lightshow.R;
import com.maverick.lightshow.MainActivity;
import com.maverick.lightshow.record;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.media.MediaPlayer;
import android.media.MediaRecorder.AudioSource;
import android.net.Uri;

public class Lghts extends ActionBarActivity {

	private View bass;
	private View treb;
	private Handler mhandler = new Handler();
	private int end=5;
	private boolean flip=true;
	private boolean active=false;
	private int on[];
	private int wait[];
	private int color1=Color.BLACK;
	private int color2=Color.RED;
	private byte[] songData;
    private int capSize;
    private int sampRate;
    private double[] fft;
    private byte[] mAudioBuffer;
    private int bcut;
    private int k=60;
    private int bact=1;
    private Uri uri;
    private int b2i;
    private Byte hold;
    private String data;
    MediaPlayer pl;
    Visualizer v1;
    AudioRecord microphone;
    private double lastMag;
    private int lastIndex;
    private boolean musicPlay = false;
	static int AUDIO_SAMPLE_FREQ = 16000;
	int AUDIO_BUFFER_BYTESIZE = AUDIO_SAMPLE_FREQ * 2 * 3; // = 3000ms
	private static int micFrames= 10;
	final static int AUDIO_BUFFER_SAMPLEREAD_SIZE = micFrames*AudioRecord.getMinBufferSize(100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
    private int[] capRates;
    private static boolean micState=false;
    boolean micOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lghts);
		bass = (View)findViewById(R.id.bass);
		if(micState==true){
			try {
				startMic();
			} catch (Exception e) {

				e.printStackTrace();
			}
			
		}
		else{
			data=MainActivity.getData();
			File file= new File(data); 
			uri= Uri.fromFile(file);
		}
		//treb = (View)findViewById(R.id.treb);
	}
	
	public static int setMic(boolean state){
		micState=state;
		
		return 1;
	}
	
	public static boolean getMic(){
		return micState;
	}
	


	public void goHome(View view){
		if(musicPlay==true){
			pl.stop();
			pl.release();
			musicPlay=false;}
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
	}
	
	public void back(View view){
		if(musicPlay==true){
			pl.stop();
			pl.release();
			musicPlay=false;}
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	
	
	public void flash(View view) {
		if(micState==true){
			return;
		}

		
		if(musicPlay==false){
			musicPlay=true;
			startMusic();}
		//mhandler.post(mRunnable);
		
					
	}
	
	public void startMic() throws Exception{
		record mic= new record(bass);
		
		/*double[] mag=new double[AUDIO_BUFFER_SAMPLEREAD_SIZE/2];

		microphone=new AudioRecord(AudioSource.MIC,44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,AUDIO_BUFFER_SAMPLEREAD_SIZE*2);
		int check=microphone.getState();
		
		
		microphone.setRecordPositionUpdateListener(
			new OnRecordPositionUpdateListener() {
						
		    public void onPeriodicNotification(AudioRecord recorder) {
		    	
		    	}

		    public void onMarkerReached(AudioRecord recorder) {
		    	int mSamplesRead = recorder.read(mAudioBuffer, 0, AUDIO_BUFFER_SAMPLEREAD_SIZE);
		    	if (mSamplesRead > 0) {
		    		fft=new double[AUDIO_BUFFER_SAMPLEREAD_SIZE];
		    		fft=micHandler(mAudioBuffer);
		    		fft=FFT(fft);
		    		fft=micMag(fft);
		    		fftCalc(fft);
		    		}
		      
		    }
		  });
		

	
		    microphone.setNotificationMarkerPosition(AUDIO_BUFFER_SAMPLEREAD_SIZE);
		    int not=microphone.getNotificationMarkerPosition();
		    microphone.startRecording();
		    micOn=true;
		    int rec=microphone.getRecordingState();
		    
		    rec=10;
		    int ix=0;
		    int N=AUDIO_BUFFER_SAMPLEREAD_SIZE;
		    
		    while(micOn) { 
                byte[] buffer = new byte[AUDIO_BUFFER_SAMPLEREAD_SIZE];
	    		fft=new double[AUDIO_BUFFER_SAMPLEREAD_SIZE];

                N = microphone.read(buffer,0,buffer.length);
	    		fft=new double[AUDIO_BUFFER_SAMPLEREAD_SIZE];

                fft=micHandler(buffer);
	    		fft=FFT(fft);
	    		fftCalc(fft);

                
            }*/
		
	}
	
	private void micOff(){
		micOn=false;
	}
	
	
	public void startMusic(){
		pl= MediaPlayer.create(this, uri);
		pl.start();
		
		v1=new Visualizer(pl.getAudioSessionId());
		
		capRates = Visualizer.getCaptureSizeRange();
		v1.setCaptureSize(Visualizer.getMaxCaptureRate());
		sampRate=v1.getSamplingRate();
		capSize=v1.getCaptureSize();
		bcut=((k*sampRate)/capSize/2);
		v1.setDataCaptureListener(
		        new Visualizer.OnDataCaptureListener() {

		                public void onWaveFormDataCapture(Visualizer visualizer,
		                        byte[] bytes, int samplingRate) {
		                	
		                    
		                }

		                public void onFftDataCapture(Visualizer visualizer,
		                        byte[] bytes, int samplingRate) {
		                	fft=new double[bytes.length/2];
		                    fft=audFFT(bytes);
		                	fftCalc(fft);
		                    
		                }
		            }, Visualizer.getMaxCaptureRate(), false, true);
		
		v1.setEnabled(true);

	}
	
	public double[] audFFT(byte[] mbytes){
		double mPoints[]=new double[mbytes.length];
		mPoints[0]=mbytes[0];
		mPoints[1]=mbytes[1];
		double mag[]=new double[mbytes.length/2];
		
		for(int i=0;i<mbytes.length/2;i++){
			double real = mbytes[i*2];
			double imag = mbytes[i*2+1];
			mag[i]=Math.sqrt(real*real+imag*imag);
		}
		
		return mag;
	}
	
	public void fftCalc(double[] mag){
		int hex= 0;
		int hold=0;
		String holdHex;
		int hexL;

		
		for(int i=1;i<=1;i++){
			if(mag[i]>hold){
				hold=(int)(mag[i]*1.5);
				if(i>(int)65){
					i=7;
				}
				
			}
		}
		
		if(hold>65){
			hex=hold;
				
			if(hex > 255){
				hex=255;
			}
			holdHex=Integer.toHexString(hex);
			holdHex=holdHex+"0000";
			hexL=holdHex.length();
			while(hexL<6){
				holdHex="0"+holdHex;
				hexL++;
				}
			holdHex="#"+holdHex;
			
			bass.setBackgroundColor(Color.parseColor(holdHex));
			hex=0x000000;
			}else{
				bass.setBackgroundColor(color1);
			}
		hold=0;

	    }
	
	public static double[] micHandler(byte[] data){
		//TODO write data handler for raw data to be prepared for fft func
		//Should return an fft array to be later converted into freq array of mag
		
	    double[] micBufferData = new double[AudioRecord.getMinBufferSize(44100,16,2)];
	    final int bytesPerSample = 2; // As it is 16bit PCM
	    final double amplification = 100.0; // choose a number as you like
	    for (int index = 0, floatIndex = 0; index < AUDIO_BUFFER_SAMPLEREAD_SIZE - bytesPerSample + 1; index += bytesPerSample, floatIndex++) {
	        double sample = 0;
	        for (int b = 0; b < bytesPerSample; b++) {
	            int v = data[index + b];
	            if (b < bytesPerSample - 1 || bytesPerSample == 1) {
	                v &= 0xFF;
	            }
	            sample += v << (b * 8);
	        }
	        double sample32 = amplification * (sample / 32768.0);
	        micBufferData[floatIndex] = sample32;
	    }
		
		return micBufferData;
	}
	
	
	public static double[] micMag(double[] audioDataDoubles){

        double[] re = new double[AUDIO_BUFFER_SAMPLEREAD_SIZE/2];
        double[] im = new double[AUDIO_BUFFER_SAMPLEREAD_SIZE/2];
        double[] magnitude = new double[AUDIO_BUFFER_SAMPLEREAD_SIZE/2];
		
		
        for(int i = 0; i < AUDIO_BUFFER_SAMPLEREAD_SIZE/2; i++){
            re[i] = audioDataDoubles[i*2];
            im[i] = audioDataDoubles[(i*2)+1];
            magnitude[i] = Math.sqrt((re[i] * re[i]) + (im[i]*im[i]));
        }
		
		return magnitude;
	}
	
	public double[] FFT(double[] x){
		double pi=Math.PI;
		int size=AUDIO_BUFFER_SAMPLEREAD_SIZE;
		int y=size/2;
		double[] even=new double[y];
		double[] odd=new double[y];
		double[] real=new double[y];
		double[] fin=new double[y];
		double mult=((-2*pi)/y);
		double[] hold=new double[2];
		double[] mag=new double[y];
		double exp=((-2*Math.PI)/(y*2));
		double e,o;

		
		for(int i=0;i<y-1;i++){
			e=0;
			o=0;
			for(int k=0;k<=y-1;k++){
				e+=x[2*k]*Math.exp(exp*i*k);
				o+=x[(2*k)+1]*Math.exp(exp*i*k);
			}
			fin[i]=e+exp*o;
			fin[i+y]=e-exp*o;
			
		}
		
		return fin;
	}
	
	public double[] FFTcalc(double[] x, int i, double y){
		double[] fin=new double[2]; 
		double exp=((-2*Math.PI)/(y*2));
		fin[0]=0;
		fin[1]=0;
		int dex;
		
		for(int m=0;m<y;m++){
			dex=m*2;
			fin[1]+=x[dex+1]*Math.exp(exp*m*i);
			fin[0]+=x[dex]*Math.exp(exp*m*i);
			}
		
		
		return fin;
	}
	
	protected void onDestroy (){
		micOff();
		
		stop();
		
	}
	
	private void stop(){
		v1.setEnabled(false);
		pl.pause();
		pl.stop();
		pl.release();
		
	}
	    
}
	
	
	
