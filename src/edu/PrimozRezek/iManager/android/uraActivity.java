package edu.PrimozRezek.iManager.android;


import java.sql.Date;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class uraActivity extends Activity implements OnClickListener {
	

	public static final int id=1;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	AnalogClock ura;
	TextView textViev1;
	
    
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
		
        
        //prikaz datuma
        Calendar a= Calendar.getInstance();
        int x= a.getTime().getDate();
        int c = a.getTime().getMonth();

        textViev1.setText(x+"/"+c);
    }






	@Override
	public void onClick(View v) 
	{
		Date dt = new Date(2001, 2, 2);
        int hours = dt.getHours();
        int minutes = dt.getMinutes();

		
        textViev1.setText(hours+":"+minutes);
		
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