package edu.PrimozRezek.iManager.android;



import java.util.Calendar;

import edu.PrimozRezek.iManager.android.Alarm.AlarmActivity;
import edu.PrimozRezek.iManager.android.Alarm.AlarmService_Service;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
	private static final int TIME_DIALOG_ID = 0;
    public int uraa= 12;
    public int minutaa = 13;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	AnalogClock ura;
	TextView textViev1;
	CheckBox checkBox1, checkBox2, checkBox3;
	

	private PendingIntent mAlarmSender;

	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //alarm
        mAlarmSender = PendingIntent.getService(uraActivity.this, 0, new Intent(uraActivity.this, AlarmService_Service.class), 0);
        
        setContentView(R.layout.ura);
        
        textViev1 = (TextView) findViewById(R.id.textView1);
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        ura = new AnalogClock(this);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		
        
        //prikaz datuma
        Calendar a= Calendar.getInstance();
        int x= a.getTime().getDate();
        int c = a.getTime().getMonth()+1;


        textViev1.setText(x+"/"+c);
        

        //alarm
       
//        Button button = (Button)findViewById(R.id.buttonBujenje);
//        button.setOnClickListener(mStartAlarmListener);
//        button = (Button)findViewById(R.id.buttonPocitek);
//        button.setOnClickListener(mStopAlarmListener);
        
    }
    
//    private OnClickListener mStartAlarmListener = new OnClickListener() {
//        public void onClick(View v) {
//            // We want the alarm to go off 30 seconds from now.
//            long firstTime = SystemClock.elapsedRealtime();
//
//            // Schedule the alarm!
//            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            firstTime, 30*1000, mAlarmSender);
//
//            // Tell the user about what we did.
//            Toast.makeText(uraActivity.this, "Repeating alarm will go off in 15 seconds and every 15 seconds after based on the elapsed realtime clock",
//                    Toast.LENGTH_LONG).show();
//        }
//    };
//
//    private OnClickListener mStopAlarmListener = new OnClickListener() {
//        public void onClick(View v) {
//            // And cancel the alarm.
//            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//            am.cancel(mAlarmSender);
//
//            // Tell the user about what we did.
//            Toast.makeText(uraActivity.this, "Repeating alarm has been unscheduled",
//                    Toast.LENGTH_LONG).show();
//
//        }
//    };
    



    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
    {
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute)   
    	{
    		uraa = hourOfDay;  
    		minutaa = minute;  
    	}
    	
    	
    	
    };
		
		
    
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
	    switch (id) 
	    {
	    case TIME_DIALOG_ID:
	    return new TimePickerDialog(this, mTimeSetListener, 07, 00, true);
	    
	    
	    }
	    return null;
    }
    
    
    
    

    



	@Override
	public void onClick(View v) 
	{
		

        
		switch(v.getId())
		{
		case R.id.buttonBujenje: 
			

			Intent dodajAct = new Intent(this, AlarmActivity.class);
			this.startActivity(dodajAct);
//	        
//			
//			// We want the alarm to go off 30 seconds from now.
//            long firstTime = SystemClock.elapsedRealtime();
//
//            // Schedule the alarm!
//            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            firstTime, 30*1000, mAlarmSender);
//
//            // Tell the user about what we did.
//            Toast.makeText(uraActivity.this, "Repeating alarm will go off in 15 seconds and every 15 seconds after based on the elapsed realtime clock",
//                    Toast.LENGTH_LONG).show();
//            
            
//            
//			showDialog(TIME_DIALOG_ID);
//	        checkBox1.setText(uraa+":"+minutaa);
//	        checkBox1.setChecked(true);
			
	        
			
		break;
		case R.id.buttonPocitek: 
			
//			showDialog(TIME_DIALOG_ID);
//	        checkBox2.setText(uraa+":"+minutaa);
//	        checkBox2.setChecked(true);
			
			
//			// And cancel the alarm.
//            AlarmManager am1 = (AlarmManager)getSystemService(ALARM_SERVICE);
//            am1.cancel(mAlarmSender);
//
//            // Tell the user about what we did.
//            Toast.makeText(uraActivity.this, "Repeating alarm has been unscheduled",
//                    Toast.LENGTH_LONG).show();
//			
			
			
		break;
		case R.id.button4: 
	
			showDialog(TIME_DIALOG_ID);
	        checkBox3.setText(uraa+":"+minutaa);
	        checkBox3.setChecked(true);
	
		break;
		
		
		
		}
		

	}
	
	/*
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode==Vreme_ACTIVITY_ID)
		{
			/*if(resultCode==0)  //dsfbng
				{
				
				finish();
				}
			if(resultCode==-1) 
			{
				VnosnoPolje.setText("");
				infoStevec.setText("");
				if(custom==true)
				{
					sp.setEnabled(true);
					zg.setEnabled(true);
				}
			}
		}
	}
	
	
	*/
	
    
    
}
