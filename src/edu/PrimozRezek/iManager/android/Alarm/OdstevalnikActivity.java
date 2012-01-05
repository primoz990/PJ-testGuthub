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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import edu.PrimozRezek.iManager.android.R;

public class OdstevalnikActivity extends Activity implements OnClickListener 
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
        
        setContentView(R.layout.nastavi_odstevalnik);
		
        tp1 = (TimePicker) findViewById(R.id.timePickerOdstevalnik);
        tp1.setIs24HourView(true);
        
        
        
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      
        //alarm
        mAlarmSender = PendingIntent.getService(OdstevalnikActivity.this, 0, new Intent(OdstevalnikActivity.this, OdstevalnikService_Service.class), 0);
        
        //zadnji shranjeni
        SharedPreferences zadnji_odstevalnik = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
        tp1.setCurrentHour(zadnji_odstevalnik.getInt("odstevalnik_ura", 0));
        tp1.setCurrentMinute(zadnji_odstevalnik.getInt("odstevalnik_minuta", 0));

	}  
	
	private void showNotification(CharSequence text) 
	{
        Notification notification = new Notification(R.drawable.ura, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, OdstevalnikActivity.class), 0);
        
        notification.setLatestEventInfo(this, "Odštevalnik - iManager", text, contentIntent);
        mNM.notify(5432222, notification);
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
		case R.id.buttonNastaviOdstevalnik:  

			int ura = tp1.getCurrentHour();
			int minuta = tp1.getCurrentMinute();
			
			long razlika = CasVMS(ura, minuta);
			long firstTime = SystemClock.elapsedRealtime();

			Time zdaj = new Time();
			zdaj.setToNow();

			long vsota = razlika+CasVMS(zdaj.hour, zdaj.minute)+zdaj.second*1000;

			TimeZone b = TimeZone.getDefault();
			Time ob = new Time();
			ob.clear(b.toString());
			ob.set(vsota);

            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, razlika+firstTime, mAlarmSender);

        	Toast.makeText(OdstevalnikActivity.this, "Alarm čez: "+ura+"h"+minuta+"m", Toast.LENGTH_LONG).show();
        	
        	//vodilne ničle
        	String min = ob.minute+"";
        	String sec = ob.second+"";
        	if(min.length()<2) min="0"+min;
        	if(sec.length()<2) sec="0"+sec;
        	
			showNotification("Proženje ob "+ob.hour+":"+min+":"+sec); 

			
			//nazadnje nastavljen odštevalnik
            SharedPreferences zadnji_odstevalnik = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
	        SharedPreferences.Editor editor = zadnji_odstevalnik.edit();
	        editor.putInt("odstevalnik_ura", ura);
	        editor.putInt("odstevalnik_minuta", minuta);
	        editor.putBoolean("odstevalnik_vklopljen", true);
	        editor.commit();
			
			
			
			this.finish();
			
			break;
		case R.id.buttonCancelOdstevalnik:
			
			// And cancel the alarm.
            AlarmManager am1 = (AlarmManager)getSystemService(ALARM_SERVICE);
            am1.cancel(mAlarmSender);

			Toast.makeText(OdstevalnikActivity.this, "Odštevalnik izklopljen", Toast.LENGTH_SHORT).show();
			mNM.cancel(5432222);
			
			
			//nastavitve nastavim da je izklopljen
			SharedPreferences odstevalnik_vklopljena = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
			SharedPreferences.Editor editor2 = odstevalnik_vklopljena.edit();
			editor2.putBoolean("odstevalnik_vklopljen", false);
			editor2.commit();
			
			
			this.finish();
			
			break;
		}

	}

}
