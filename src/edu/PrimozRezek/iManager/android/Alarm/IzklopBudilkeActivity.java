package edu.PrimozRezek.iManager.android.Alarm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.PrimozRezek.iManager.android.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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




public class IzklopBudilkeActivity extends Activity 
{
	TextView txtViewStevilo;
    
	//vklop ekrana
	PowerManager powMaN;
	WakeLock wakeL;

	//TextView txtViev1;
	public SensorManager SenMan;
	public MediaPlayer mPlayer = null;
	String PotDoMuzike= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Paganini.mp3";
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
				
				if((Gx>5 && Gy>5) || (Gx<-5 && Gy<-5) || (Gx<-5 && Gy>5) || (Gx>5 && Gy<-5)) 
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
        setContentView(R.layout.budilka);
        
        txtViewStevilo = (TextView) findViewById(R.id.textViewKolikoKratSe);
        
        SenMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        SenMan.registerListener(SenLis, SenMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        //vklop ekrana
        powMaN = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeL = powMaN.newWakeLock((PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YourServie");
        wakeL.acquire();

        //odklep ekrana
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        
      //nastavitve nastavim da je budilka izklopljena
		SharedPreferences budilka_vklopljena = getSharedPreferences("BUDILKA_LAST_SET", 0);
		SharedPreferences.Editor editor2 = budilka_vklopljena.edit();
		editor2.putBoolean("budilka_vklopljena", false);
		editor2.commit();

		STEVILO_UDARCEV=5;
		txtViewStevilo.setText(STEVILO_UDARCEV+"");
		
        startPlaying();
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
    	//shranivdat(rezultati);
    	SenMan.unregisterListener(SenLis);
    	mPlayer.release();
    	//wakeL.release();
    	super.onStop();
    }
    
    private void startPlaying() 
    {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(PotDoMuzike);
            mPlayer.setLooping(true);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            //txtViev1.setText("napaka: ");
        }
    }
    
    @Override
    protected void onDestroy() {
    	wakeL.release();
    	super.onDestroy();
    }
    
    
    
    
    
}