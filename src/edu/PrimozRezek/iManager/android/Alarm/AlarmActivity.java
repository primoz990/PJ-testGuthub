package edu.PrimozRezek.iManager.android.Alarm;

import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import edu.PrimozRezek.iManager.android.R;

public class AlarmActivity extends Activity implements OnClickListener 
{

//	private PendingIntent mAlarmSender;

	TimePicker tp1;
	NotificationManager mNM; 
	private PendingIntent mAlarmSender;
	
	
	
	
	private void startPlaying() 
    {
    	Intent i = new Intent(this, FizicnaBudilkaActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	this.startActivity(i);
    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        //alarm
//        mAlarmSender = PendingIntent.getService(AlarmActivity.this, 0, new Intent(AlarmActivity.this, AlarmService_Service.class), 0);
        
        setContentView(R.layout.nastavi_alarm);
		
        tp1 = (TimePicker) findViewById(R.id.timePickerAlarm);
        tp1.setIs24HourView(true);
        
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      
        //alarm
        mAlarmSender = PendingIntent.getService(AlarmActivity.this, 0, new Intent(AlarmActivity.this, AlarmService_Service.class), 0);
        
        
        SharedPreferences zadnja_budilka = getSharedPreferences("BUDILKA_LAST_SET", 0);
        //default nastavimo na trenutni čas
        Time zd = new Time();
		zd.setToNow();
        tp1.setCurrentHour(zadnja_budilka.getInt("budilka_ura", zd.hour));
        tp1.setCurrentMinute(zadnja_budilka.getInt("budilka_minuta", zd.minute));
       

	}  
	
	private void showNotification(CharSequence text) 
	{
        Notification notification = new Notification(R.drawable.ura, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmActivity.class), 0);
        
        notification.setLatestEventInfo(this, "Budilka - iManager", text, contentIntent);
        mNM.notify(5432111, notification);
    }
	
	public long CasVMS(int h, int m)
	{
		return (h*60*60*1000)+(m*60*1000);
	}
	
	
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.buttonNastaviAlarm:  


			Time now = new Time();

			now.setToNow();
			int trenUra = now.hour;
			int trenMin = now.minute;
			int trenSec = now.second;

			long ttt = CasVMS(trenUra, trenMin)+(trenSec*1000);
			
			
			int ura = tp1.getCurrentHour();
			int minuta = tp1.getCurrentMinute();
			
			long razlika = CasVMS(ura, minuta)-ttt;
			
			if(razlika<0)
			{
				long enDan = CasVMS(24, 0);
				razlika+=enDan;
			}
			
			TimeZone a = TimeZone.getDefault();
			now.clear(a.toString());
			now.set(razlika); // za izpis čez koliko časa bo alarm
			Toast.makeText(AlarmActivity.this, "Alarm čez: "+now.hour+"h"+now.minute+"m"+now.second+"s", Toast.LENGTH_LONG).show();
			
			//vodilne ničle
        	String min = minuta+"";
        	if(min.length()<2) min="0"+min;
			
			showNotification("Alarm ob "+ura+":"+min);   
	        
	        long firstTime = SystemClock.elapsedRealtime();
	        
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime+razlika, mAlarmSender);

            
            //nazadnje nastavljena budilka
            SharedPreferences zadnja_budilka = getSharedPreferences("BUDILKA_LAST_SET", 0);
	        SharedPreferences.Editor editor = zadnja_budilka.edit();
	        editor.putInt("budilka_ura", ura);
	        editor.putInt("budilka_minuta", minuta);
	        editor.putBoolean("budilka_vklopljena", true);
	        editor.commit();
            
            this.finish();
	
			
			
			break;
		case R.id.buttonCancelAlarm:
			
			// And cancel the alarm.
            AlarmManager am1 = (AlarmManager)getSystemService(ALARM_SERVICE);
            am1.cancel(mAlarmSender);

			Toast.makeText(AlarmActivity.this, "Alarm izklopljen", Toast.LENGTH_SHORT).show();
			mNM.cancel(5432111);
			
			//nastavitve nastavim da je budilka izklopljena
			SharedPreferences budilka_vklopljena = getSharedPreferences("BUDILKA_LAST_SET", 0);
			SharedPreferences.Editor editor2 = budilka_vklopljena.edit();
			editor2.putBoolean("budilka_vklopljena", false);
			editor2.commit();
			
			
			this.finish();
			
			break;

			
		}

	}

}
