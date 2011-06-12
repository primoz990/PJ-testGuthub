package edu.PrimozRezek.iManager.android;


import java.sql.Date;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
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

public class uraActivity extends Activity implements OnClickListener {
	

	public static final int id=1;
	private static final int TIME_DIALOG_ID = 0;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	AnalogClock ura;
	TextView textViev1;
	CheckBox checkBox1, checkBox2, checkBox3;
	
	TimePickerDialog TpD1;

	
	
    
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
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		
        
        //prikaz datuma
        Calendar a= Calendar.getInstance();
        int x= a.getTime().getDate();
        int c = a.getTime().getMonth()+1;

        textViev1.setText(x+"/"+c);
        
        
        
    }

    public int uraa;
    public int minutaa;

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
		case R.id.button2: 
			
			showDialog(TIME_DIALOG_ID);
			
			//TpD1= new TimePickerDialog(this, mTimeSetListener, 7, 0, true);
	       // TpD1.show();

	        checkBox1.setText(uraa+":"+minutaa);
	        checkBox1.setChecked(true);
	        
	        
			
		break;
		case R.id.button3: 
			
			showDialog(TIME_DIALOG_ID);
	        checkBox2.setText(uraa+":"+minutaa);
	        checkBox2.setChecked(true);
			
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
