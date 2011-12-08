package edu.PrimozRezek.iManager.android.Alarm;

import edu.PrimozRezek.iManager.android.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity implements OnClickListener 
{

	private PendingIntent mAlarmSender;

	TimePicker tp1;
	PrenosCasa pc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        //alarm
        mAlarmSender = PendingIntent.getService(AlarmActivity.this, 0, new Intent(AlarmActivity.this, AlarmService_Service.class), 0);
        
        setContentView(R.layout.nastavi_alarm);
		
        
        tp1 = (TimePicker) findViewById(R.id.timePickerAlarm);
        tp1.setIs24HourView(true);
        
        pc= new PrenosCasa();
	}  
	
	
	
	
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.buttonNastaviAlarm:  
			
			pc.setUra(tp1.getCurrentHour());
			pc.setMinuta(tp1.getCurrentMinute());
			
			// We want the alarm to go off 30 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();

            // Schedule the alarm!
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 30*1000, mAlarmSender);

            // Tell the user about what we did.
            Toast.makeText(AlarmActivity.this, "Repeating alarm will go off in 15 seconds and every 15 seconds after based on the elapsed realtime clock "+pc.getMinuta(),
                    Toast.LENGTH_LONG).show();
			
			
			
			break;
		case R.id.buttonCancelAlarm:
			
			// And cancel the alarm.
            AlarmManager am1 = (AlarmManager)getSystemService(ALARM_SERVICE);
            am1.cancel(mAlarmSender);

            // Tell the user about what we did.
            Toast.makeText(AlarmActivity.this, "Repeating alarm has been unscheduled",
                    Toast.LENGTH_LONG).show();
			
			
			
			break;
		}

	}

}
