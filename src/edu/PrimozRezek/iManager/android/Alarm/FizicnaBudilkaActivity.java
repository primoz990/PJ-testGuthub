package edu.PrimozRezek.iManager.android.Alarm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import edu.PrimozRezek.iManager.android.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;




public class FizicnaBudilkaActivity extends Activity 
{
	TextView txtViewStevilo;
    
	//vklop ekrana
	PowerManager powMaN;
	WakeLock wakeL;
	Window window;

	//TextView txtViev1;
	public SensorManager SenMan;
	public MediaPlayer mPlayer = null;
	
	double gravityX=0;
	double gravityY=0;
	double gravityZ=0;
	String rezultati="";
	//ZVOK: http://developer.android.com/guide/topics/media/audio-capture.html
	
	//nastavitve
	int stevecUdarcev=0;;
	int STEVILO_UDARCEV;
	
	public SensorEventListener SenLis = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) 
		{
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				   double alpha = 0.8;

				   gravityX = alpha * gravityX + (1 - alpha) * event.values[0];
				   gravityY = alpha * gravityY + (1 - alpha) * event.values[1];
				   gravityZ = alpha * gravityZ + (1 - alpha) * event.values[2];

		          double Gx= event.values[0] - gravityX;
		          double Gy = event.values[1] - gravityY;
		          double Gz = event.values[2] - gravityZ;
				
				//txtViev1.setText("Gx: "+(int)Gx+"\nGy: "+(int)Gy+"\nGz: "+(int)Gz);
				rezultati+="Gx: "+(int)Gx+"  Gy: "+(int)Gy+"  Gz: "+(int)Gz+"\n";
				
				//if((Gx>3 && Gy>3) || (Gx<-3 && Gy<-3) || (Gx<-3 && Gy>3) || (Gx>3 && Gy<-3)) 
				if(Gx>4)
				{
					stevecUdarcev++;
					if(stevecUdarcev<STEVILO_UDARCEV)
					{
						txtViewStevilo.setText((STEVILO_UDARCEV-stevecUdarcev)+" ");//prikaziStevilo();
					}
					else
					{
					wakeL.release();
					System.exit(0);
					}
				}

			}
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
	public void prikaziStevilo()
	{
		txtViewStevilo.setText(STEVILO_UDARCEV-stevecUdarcev);
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fizicna_budilka);
        
        
        
        txtViewStevilo = (TextView) findViewById(R.id.textViewKolikoKratSe);
        
        SenMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        SenMan.registerListener(SenLis, SenMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        //vklop ekrana
        powMaN = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeL = powMaN.newWakeLock((PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YourServie");
        wakeL.acquire();

        //odklep ekrana
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);  

        //preberem nastavitve
        SharedPreferences bud_ali_odst = getSharedPreferences("BUDILKA_ALI_ODSTEVALNIK", 0);
        String kajIzkljapljamo = bud_ali_odst.getString("ali_budilka_ali_odstevalnik", "budilka");
        
        SharedPreferences zadnje_nastavitve = getSharedPreferences("NASTAVITVE_APP_LAST_SET", 0);
        STEVILO_UDARCEV = zadnje_nastavitve.getInt("st_udarcev_"+kajIzkljapljamo, 0); //preberemo st_udarcev za budilko ali za odstevalnik
        Uri potdomuzike22 = Uri.parse(zadnje_nastavitve.getString("glasba_"+kajIzkljapljamo, "napaka")); //preberemo izbrano glasbo za budliko ali odstevalnik
        
		txtViewStevilo.setText(STEVILO_UDARCEV+"");
		
        startPlaying(potdomuzike22);
    }
    
    @Override
    protected void onResume() 
    {
    	super.onResume();          //REGISTRIRAM SENZOR
    	SenMan.registerListener(SenLis, SenMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    	
    	
    }
    
    @Override
    protected void onStop() 
    {
    	SenMan.unregisterListener(SenLis);
    	mPlayer.release(); 
    	wakeL.release();
    	super.onStop();
    }
    

    
private void startPlaying(Uri potDomuzike) 
  {
      mPlayer = new MediaPlayer();
      try {
    	  mPlayer.setDataSource(this, potDomuzike);
          mPlayer.setLooping(true);
          mPlayer.prepare();
          mPlayer.start();
      } catch (IOException e) 
      {
    	  startPlayingDefault();

      }
  }
	
    //če ni izbrane glasbe predvajamo svojo
	private void startPlayingDefault() 
	  {
		mPlayer = new MediaPlayer();
			try 
		    {
				//http://androidforums.com/developer-101/205744-play-sound-assets.html
				AssetFileDescriptor descriptor = getAssets().openFd("paganini.mp3");
				mPlayer.setDataSource( descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
				descriptor.close();
				
		        mPlayer.setLooping(true);
				mPlayer.prepare();
				mPlayer.start();
		    } catch (IllegalStateException e1) 
		    {
		    	Log.e("Napaka", "Pri nalaganju glasbe");
	        
	        } catch (IOException e2) 
	        {
	        	Log.e("Napaka", "Pri nalaganju glasbe");
	        }
				
			
	  }
    
    
    
    
    
}