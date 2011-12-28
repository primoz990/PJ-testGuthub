package edu.PrimozRezek.iManager.android;



import java.util.Calendar;
import java.util.TimeZone;

import edu.PrimozRezek.iManager.android.Alarm.AlarmActivity;
import edu.PrimozRezek.iManager.android.Alarm.AlarmService_Service;
import edu.PrimozRezek.iManager.android.Alarm.OdstevalnikActivity;
import edu.PrimozRezek.iManager.android.Alarm.OdstevalnikService_Service;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TimePicker.OnTimeChangedListener;


public class uraActivity extends Activity implements OnClickListener {
	

	public static final int id=1;

	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	AnalogClock ura;
	TextView textViev1;
	CheckBox checkBoxBudilka, checkBoxOdstevalnik;
	
    
	PendingIntent mAlarmSender; //za vklop budilke preko checkboxa
	PendingIntent mAlarmSender2;//za vklop odstevalnika preko checkboxa
	
	NotificationManager mNM;	 //obveščanje vklopa in izklopa	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.ura);  
        
        textViev1 = (TextView) findViewById(R.id.textView1);
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL); 
        mainLL.addView(g,0);
        ura = new AnalogClock(this);
        
        checkBoxBudilka = (CheckBox) findViewById(R.id.checkBoxBudilka);
        checkBoxOdstevalnik = (CheckBox) findViewById(R.id.checkBoxOdstevalnik);

		
        
        //prikaz datuma
        Calendar a= Calendar.getInstance();
        int x= a.getTime().getDate();
        int c = a.getTime().getMonth()+1;


        textViev1.setText(x+"/"+c);
        
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);	

    }
    

    @Override
    protected void onResume() 
    {
    	//nastavljena budilka
    	SharedPreferences zadnja_budilka = getSharedPreferences("BUDILKA_LAST_SET", 0);
        checkBoxBudilka.setChecked(zadnja_budilka.getBoolean("budilka_vklopljena", false));
        
        //vodilna ničla
        String min = zadnja_budilka.getInt("budilka_minuta", 30)+"";
        if(min.length()<2) min="0"+min;

        checkBoxBudilka.setText(zadnja_budilka.getInt("budilka_ura", 8)+":"+ min);
        
        
        //nastavljen odštevalnik
        SharedPreferences zadnji_odst = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
        checkBoxOdstevalnik.setChecked(zadnji_odst.getBoolean("odstevalnik_vklopljen", false));
        checkBoxOdstevalnik.setText(zadnji_odst.getInt("odstevalnik_ura", 1) + "h " + zadnji_odst.getInt("odstevalnik_minuta", 20)+"m");

    	super.onResume();
    }
    

	
	



	@Override
	public void onClick(View v) 
	{

		switch(v.getId())
		{
		case R.id.buttonBujenje: 
			

			Intent dodajAct = new Intent(this, AlarmActivity.class);
			this.startActivity(dodajAct);

		break;
		case R.id.buttonPocitek: 

			Intent odstevalnik = new Intent(this, OdstevalnikActivity.class);
			this.startActivity(odstevalnik);

		break;
		case R.id.checkBoxBudilka:
			
			
			mAlarmSender = PendingIntent.getService(this, 0, new Intent(this, AlarmService_Service.class), 0);

			if(checkBoxBudilka.isChecked()==false) //če je vklopljena že katera budilka jo izklopim
			{

				AlarmManager am1 = (AlarmManager)getSystemService(ALARM_SERVICE);
	            am1.cancel(mAlarmSender);
	
				Toast.makeText(this, "Alarm izklopljen", Toast.LENGTH_SHORT).show();
				mNM.cancel(5432111);
				
				//nastavitve nastavim da je budilka izklopljena
				SharedPreferences budilka_vklopljena = getSharedPreferences("BUDILKA_LAST_SET", 0);
				SharedPreferences.Editor editor2 = budilka_vklopljena.edit();
				editor2.putBoolean("budilka_vklopljena", false);
				editor2.commit();
			}
			else  //drugače vklopim nazadnje nastavljeno budilko
			{
				Time now = new Time();

				now.setToNow();
				int trenUra = now.hour;
				int trenMin = now.minute;
				int trenSec = now.second;

				long ttt = CasVMS(trenUra, trenMin)+(trenSec*1000);
				
				
				SharedPreferences zadnja_budilka = getSharedPreferences("BUDILKA_LAST_SET", 0);
				int ura = zadnja_budilka.getInt("budilka_ura", 8);
				int minuta = zadnja_budilka.getInt("budilka_minuta", 30);
				
				
				long razlika = CasVMS(ura, minuta)-ttt;
				
				if(razlika<0)
				{
					long enDan = CasVMS(24, 0);
					razlika+=enDan;
				}
				
				TimeZone a = TimeZone.getDefault();
				now.clear(a.toString());
				now.set(razlika); // za izpis čez koliko časa bo alarm
				Toast.makeText(this, "Alarm čez: "+now.hour+"h"+now.minute+"m"+now.second+"s", Toast.LENGTH_LONG).show();
				
				//vodilne ničle
	        	String min = minuta+"";
	        	if(min.length()<2) min="0"+min;
				
				showNotification(5432111, "Budilka - iManager", "Alarm ob "+ura+":"+min);   
		        
		        long firstTime = SystemClock.elapsedRealtime();
		        
	            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
	            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime+razlika, mAlarmSender);
	            
	            //vklopim budilko
				SharedPreferences budilka_vklopljena = getSharedPreferences("BUDILKA_LAST_SET", 0);
				SharedPreferences.Editor editor2 = budilka_vklopljena.edit();
				editor2.putBoolean("budilka_vklopljena", true);
				editor2.commit();
	            
			}
			
		break;
		case R.id.checkBoxOdstevalnik:
			
			mAlarmSender2 = PendingIntent.getService(this, 0, new Intent(this, OdstevalnikService_Service.class), 0);
	        
			
			
			if(checkBoxOdstevalnik.isChecked()==false) //če je vklopljen že kateri odstevalnik ga  izklopim
			{
				AlarmManager am1 = (AlarmManager)getSystemService(ALARM_SERVICE);
	            am1.cancel(mAlarmSender2);

				Toast.makeText(this, "Odštevalnik izklopljen", Toast.LENGTH_SHORT).show();
				mNM.cancel(5432222);
				
				
				//nastavitve nastavim da je izklopljen
				SharedPreferences odstevalnik_vklopljena = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
				SharedPreferences.Editor editor2 = odstevalnik_vklopljena.edit();
				editor2.putBoolean("odstevalnik_vklopljen", false);
				editor2.commit();

			}
			else //drugače ga vklopim
			{
				
				//nastavljen odštevalnik od prej
		        SharedPreferences zadnji_odst = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
				int ura = zadnji_odst.getInt("odstevalnik_ura", 1);
				int minuta = zadnji_odst.getInt("odstevalnik_minuta", 20);
				
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
	            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, razlika+firstTime, mAlarmSender2);

	        	Toast.makeText(this, "Alarm čez: "+ura+"h"+minuta+"m", Toast.LENGTH_LONG).show();
	        	
	        	//vodilne ničle
	        	String min = ob.minute+"";
	        	String sec = ob.second+"";
	        	if(min.length()<2) min="0"+min;
	        	if(sec.length()<2) sec="0"+sec;
	        	
				showNotification(5432222, "Odštevalnik - iManager" ,"Proženje ob "+ob.hour+":"+min+":"+sec); 

				
				//nazadnje nastavljen odštevalnik
	            SharedPreferences zadnji_odstevalnik = getSharedPreferences("ODSTEVALNIK_LAST_SET", 0);
		        SharedPreferences.Editor editor = zadnji_odstevalnik.edit();
		        editor.putBoolean("odstevalnik_vklopljen", true);
		        editor.commit();
			}

			
		break;

		}


	}
	
	
	public long CasVMS(int h, int m)
	{
		return (h*60*60*1000)+(m*60*1000);
	}
	
	
	private void showNotification(int id, CharSequence title, CharSequence text) 
	{
        Notification notification = new Notification(R.drawable.ura, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmActivity.class), 0);
        
        notification.setLatestEventInfo(this, title, text, contentIntent);
        mNM.notify(id, notification);
    }
	
	
	
	
	
}
