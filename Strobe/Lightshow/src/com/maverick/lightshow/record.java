package com.maverick.lightshow;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.view.View;


public class record extends Thread {
	private boolean stopped=false;
	private static int AUDIO_BUFFER_SAMPLEREAD_SIZE=AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
	private static View bass;
	private int color1=Color.BLACK;
	int n=AUDIO_BUFFER_SAMPLEREAD_SIZE;
	
	record(View x){
		bass=x;
		start();
	}
	
	public void close(){
		stopped=true;
	}
	
	public boolean isRunning(){
		return !stopped;
	}
	
	@Override
	public void run(){
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		
		AudioRecord microphone=new AudioRecord(AudioSource.MIC,44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,AUDIO_BUFFER_SAMPLEREAD_SIZE*10);
		microphone.startRecording();
		int rec=microphone.getRecordingState();
		short[] buffer;
		short[][] buffers=new short[256][160];
		int ix=0;
		
		while(isRunning()){
			buffer=buffers[ix++%buffers.length];
			
			n=microphone.read(buffer, 0, buffer.length);
			
			double[] fft=new double[n/2];
    		fft=micHandler(buffer);
    		//fft=FFT(fft);
    		fft=micMag(fft);
    		fftCalc(fft);
			
		}
	
	
	}

	
	private double[] micHandler(short[] data){
		//TODO write data handler for raw data to be prepared for fft func
		//Should return an fft array to be later converted into freq array of mag
		
	    double[] micBufferData = new double[AudioRecord.getMinBufferSize(44100,16,2)];
	    final int bytesPerSample = 2; // As it is 16bit PCM
	    final double amplification = 100.0; // choose a number as you like
	    for (int index = 0, floatIndex = 0; index < n - bytesPerSample + 1; index += bytesPerSample, floatIndex++) {
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
		
		return FFT(micBufferData);
	}
	
	
	private double[] micMag(double[] audioDataDoubles){

        double[] re = new double[n/2];
        double[] im = new double[n/2];
        double[] magnitude = new double[n/2];
		
		
        for(int i = 0; i <n/4; i++){
            re[i] = audioDataDoubles[i*2];
            im[i] = audioDataDoubles[(i*2)+1];
            magnitude[i] = Math.sqrt((re[i] * re[i]) + (im[i]*im[i]));
        }
		
		return magnitude;
	}
	
	private double[] FFT(double[] x){
		double pi=Math.PI;
		int size=n;
		int y=size/2;
		double[] even=new double[y];
		double[] odd=new double[y];
		double[] real=new double[y];
		double[] fin=new double[y];
		double mult=((-2*pi)/y);
		double[] hold=new double[2];
		double[] mag=new double[y];
		double exp=((-2*Math.PI)/(size));
		double e,o;
		double iExp=exp*exp;

		
		for(int i=0;i<(y/2)-1;i++){
			e=0;
			o=0;
			for(int k=0;k<=y-1;k++){
				e+=x[2*k]*Math.exp(iExp*i*k);
				o+=x[(2*k)+1]*Math.exp(iExp*i*k);
			}
			fin[i]=e+exp*i*o;
			fin[i+(y/2)]=e-exp*i*o;
			
		}
		
		return fin;
	}
	
	private void fftCalc(double[] mag){
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
		
}
