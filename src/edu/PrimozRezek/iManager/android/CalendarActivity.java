/*
 * Copyright (c) 2010, Lauren Darcey and Shane Conder 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */

/*
 * ******* WARNING WARNING WARNING ************
 * 
 * As stated above, this code is supplied AS-IS. Any damage, loss of data, or other harm 
 * is not our liability. You use this code at your own risk.
 * 
 * You've been warned. 
 * 
 * Since this code has to be run on a handset, you may break your handset. We have not tested it on *your* handset.
 * 
 * You've been warned.
 * 
 * This code is subject to change. In fact, it has changed. Android SDK 2.1 -> 2.2 update changed it.
 * 
 * Please see the article at http://bit.ly/c2kYWk for more information. 
 * 
 */

package edu.PrimozRezek.iManager.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.PrimozRezek.iManager.android.Koledar.Dogodek;
import edu.PrimozRezek.iManager.android.Koledar.Koledar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class CalendarActivity extends Activity implements OnClickListener
{

	static final int TIME_DIALOG_ID_OD = 0;
	static final int TIME_DIALOG_ID_DO = 3;
	static final int DATE_DIALOG_ID_OD = 1;
	static final int DATE_DIALOG_ID_DO = 2;
	
    private static final String DEBUG_TAG = "CalendarActivity";
    EditText ETopis, ETnaslov, ETlokacija;
    DatePicker datePicker;
    Spinner SPNizbiraKoledarja, SPNopozorilo;
    CheckBox CHKBXceliDan, CHKBXopozorilo;
    Button gumbCasOd, gumbCasDo, gumbDatumOd, gumbDatumDo;
	
    Date Od, Do;
    
    
    
	
	
	
	//----------------------------------------Time in Date Dialogi
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() 
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		{
			Od.setDate(dayOfMonth);
			Od.setMonth(monthOfYear);
			Od.setYear(year-1900);
			gumbDatumOd.setText(dayOfMonth+"."+(monthOfYear+1)+"."+year);
		}
	};
	
	private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() 
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		{
			Do.setDate(dayOfMonth);
			Do.setMonth(monthOfYear);
			Do.setYear(year-1900);
			gumbDatumDo.setText(dayOfMonth+"."+(monthOfYear+1)+"."+year);
		}
	};
	
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() 
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
		{
			Od.setHours(hourOfDay);
			Od.setMinutes(minute);
			
			String min = minute+"";
			if(min.length()<2) min="0"+min;
			gumbCasOd.setText(hourOfDay+":"+min);
			
		}
	};
	private TimePickerDialog.OnTimeSetListener mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() 
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
		{
			Do.setHours(hourOfDay);
			Do.setMinutes(minute);
			
			String min = minute+"";
			if(min.length()<2) min="0"+min;
			gumbCasDo.setText(hourOfDay+":"+min);
			
		}
	};
		
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
			case TIME_DIALOG_ID_OD:
				return new TimePickerDialog(this, mTimeSetListener, 8, 0, true);
			case TIME_DIALOG_ID_DO:
				return new TimePickerDialog(this, mTimeSetListener2, 9, 0, true);
				
			case DATE_DIALOG_ID_OD:
				return new DatePickerDialog(this, mDateSetListener, 2011, 11, 31);
			case DATE_DIALOG_ID_DO:
				return new DatePickerDialog(this, mDateSetListener2, 2011, 11, 31);
		}
	return null;
	}

	//-----------------------          ---OnCreate
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodajdogodek);

        ETopis = (EditText) findViewById(R.id.editTextDodajOpis);
        ETnaslov = (EditText) findViewById(R.id.editTextDodajNaslov);

        SPNizbiraKoledarja = (Spinner) findViewById(R.id.spinnerIzbiraKoledarja);
        ETlokacija = (EditText) findViewById(R.id.EditTextLokacija);
        CHKBXceliDan = (CheckBox) findViewById(R.id.checkBoxCeliDan);
        gumbCasOd = (Button)findViewById(R.id.buttonOdCas);
        gumbCasDo = (Button) findViewById(R.id.buttonDoCas);
        gumbDatumOd = (Button) findViewById(R.id.buttonOdDatum);
        gumbDatumDo = (Button) findViewById(R.id.buttonDoDatum);
        CHKBXopozorilo = (CheckBox) findViewById(R.id.checkBoxopozorilo);
        SPNopozorilo = (Spinner) findViewById(R.id.spinnerOpozorilo);
        
        gumbCasOd.setText("8:00");
        gumbCasDo.setText("9:00");
        //to se bo nastavljalo na izbrani datum iz koledar API-ja
        
      //PrenosPodatkov a = new PrenosPodatkov();
        //(a.getIzbranDatumIzKoledarja().getDate()+"."+a.getIzbranDatumIzKoledarja().getMonth()+"."+a.getIzbranDatumIzKoledarja().getYear()+" "+a.getIzbranDatumIzKoledarja().getHours()+":"+a.getIzbranDatumIzKoledarja().getMinutes());
    
        
        gumbDatumOd.setText("1.1.2012");
        gumbDatumDo.setText("1.1.2012");
        
        Od = new Date(2012-1900, 0, 1, 8, 30);
        Do = new Date(2012-1900, 0, 1, 9, 30);
        
 
        ListSelectedCalendars(); //poišče vse možne koledarje in jih napolni v spinner
        
        SPNizbiraKoledarja.setSelection(2); // to bo iz sharedPreferences
        
        
    }

    
    
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.buttonShraniDogodek:
		
		 try {
	            Log.i(DEBUG_TAG, "Starting Calendar Test"); 
	            
	            int IzbranKoledarID = SPNizbiraKoledarja.getSelectedItemPosition()+1; 

	            if (IzbranKoledarID != 0) 
	            {
	            	//pridobim novi dogodek iz GUI
	            	int celdan=0;
	            	if(CHKBXceliDan.isChecked()==true) celdan =1;
	            	int alarm = 0;
	            	if(CHKBXopozorilo.isChecked()==true) alarm =1;
	            	int opozorilo =Integer.parseInt(SPNopozorilo.getSelectedItem().toString().substring(0, 2));

	            	Dogodek dog = new Dogodek(ETnaslov.getText().toString(), ETopis.getText().toString(), Od.getTime(), Do.getTime(), ETlokacija.getText().toString(), celdan, alarm, opozorilo);
	            	
	            	//dodam dogodek v izbran koledar
	                Uri newEvent = MakeNewCalendarEntry(IzbranKoledarID, dog);
	                int eventID = Integer.parseInt(newEvent.getLastPathSegment());
	                ListCalendarEntry(eventID);
	                
	                // uncomment these to show updating and deleting entries

	                //UpdateCalendarEntry(eventID);
	                //ListCalendarEntrySummary(eventID);
	                //DeleteCalendarEntry(eventID);
	                
	                //ListCalendarEntrySummary(eventID);
	                ListAllCalendarEntries(IzbranKoledarID);
	               
	            } else 
	            {
	            	Toast.makeText(this, "Napaka! Koledar ni izbran.", Toast.LENGTH_LONG).show();
	            }

	            Toast.makeText(this, "Dogodek dodan", Toast.LENGTH_SHORT).show();


	        } catch (Exception e) 
	        {
	        	Toast.makeText(this, "Napaka pri dodajanju", Toast.LENGTH_LONG).show();
	        	
	        }
	    break;
		case R.id.buttonOdCas:
			showDialog(TIME_DIALOG_ID_OD);
			
			
			
		break;
		case R.id.buttonDoCas:
			showDialog(TIME_DIALOG_ID_DO);
		break;
		case R.id.buttonOdDatum:
			showDialog(DATE_DIALOG_ID_OD);
		break;
		case R.id.buttonDoDatum:
			showDialog(DATE_DIALOG_ID_DO);
		break;
		}
		
	}
	
	
	
	
    
    private List<Koledar> ListSelectedCalendars() 
    {
    	List koledarji = new ArrayList<Koledar>();
    	
        String[] projection = new String[] { "_id", "name" };
        String selection = "selected=1";
        String path = "calendars";

        Cursor managedCursor = getCalendarManagedCursor(projection, selection,path);

        // lahko imamo vec koledarjev
        if (managedCursor != null && managedCursor.moveToFirst()) 
        {

            Log.i(DEBUG_TAG, "Listing Selected Calendars Only");

            int nameColumn = managedCursor.getColumnIndex("name");
            int idColumn = managedCursor.getColumnIndex("_id");

            do {
            	// dobimo ime koledarja
                String calName = managedCursor.getString(nameColumn);
                String calId = managedCursor.getString(idColumn);
                Log.i(DEBUG_TAG, "Found Calendar '" + calName + "' (ID=" + calId + ")");

                koledarji.add(new Koledar(Integer.parseInt(calId) ,calName));
                
            } while (managedCursor.moveToNext());
        } else {
            Log.i(DEBUG_TAG, "No Calendars");
        }
        
       //napolnim spinner z možnimi izbirami
        String seznamcek[] = new String[koledarji.size()];
        for(int i=0; i<koledarji.size(); i++) 
        {
        	Koledar vmes = (Koledar)koledarji.get(i);
        	
        	//prva dva sta null zato jih popravim
        	if(i==0 && vmes.ime==null) vmes.ime="PC Sync";
        	else if(i==1 && vmes.ime==null) vmes.ime="Exchange";
        	
        	seznamcek[i]= vmes.ime + "   (ID=" + vmes.id+")";
        }
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, seznamcek);
		SPNizbiraKoledarja.setAdapter(adapter);
	
		String[] ure = {"10 minut", "15 minut", "20 minut", "30 minut", "45 minut", "60 minut"};
		ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ure);
		SPNopozorilo.setAdapter(adapter2);

        return koledarji;

    }

    private void ListAllCalendarDetails() 
    {
        Cursor managedCursor = getCalendarManagedCursor(null, null, "calendars");

        if (managedCursor != null && managedCursor.moveToFirst()) 
        {

            Log.i(DEBUG_TAG, "Listing Calendars with Details");

            do {
                Log.i(DEBUG_TAG, "**START Calendar Description**");

                for (int i = 0; i < managedCursor.getColumnCount(); i++) 
                {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "=" + managedCursor.getString(i));
                }
                
                Log.i(DEBUG_TAG, "**END Calendar Description**");
            } while (managedCursor.moveToNext());
        } else {
            Log.i(DEBUG_TAG, "No Calendars");
        }

    }

    private void ListAllCalendarEntries(int calID) 
    {

        Cursor managedCursor = getCalendarManagedCursor(null, "calendar_id=" + calID, "events");

        if (managedCursor != null && managedCursor.moveToFirst()) 
        {
            Log.i(DEBUG_TAG, "Listing Calendar Event Details");

            do {

                Log.i(DEBUG_TAG, "**START Calendar Event Description**");

                for (int i = 0; i < managedCursor.getColumnCount(); i++) 
                {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "=" + managedCursor.getString(i));
                }
                Log.i(DEBUG_TAG, "**END Calendar Event Description**");
            } while (managedCursor.moveToNext());
        } else {
            Log.i(DEBUG_TAG, "No Calendars");
        }

    }

    private void ListCalendarEntry(int eventId) {
        Cursor managedCursor = getCalendarManagedCursor(null, null, "events/" + eventId);
    
        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Calendar Event Details");

            do {
                Log.i(DEBUG_TAG, "**START Calendar Event Description**");

                for (int i = 0; i < managedCursor.getColumnCount(); i++) 
                {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "=" + managedCursor.getString(i));
                }
                
                Log.i(DEBUG_TAG, "**END Calendar Event Description**");
            } while (managedCursor.moveToNext());
        } else {
            Log.i(DEBUG_TAG, "No Calendar Entry");
        }

    }

    private void ListCalendarEntrySummary(int eventId) {
        String[] projection = new String[] { "_id", "title", "dtstart" };
        Cursor managedCursor = getCalendarManagedCursor(projection, null, "events/" + eventId);

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(DEBUG_TAG, "Listing Calendar Event Details");

            do {

                Log.i(DEBUG_TAG, "**START Calendar Event Description**");

                for (int i = 0; i < managedCursor.getColumnCount(); i++) 
                {
                    Log.i(DEBUG_TAG, managedCursor.getColumnName(i) + "="+ managedCursor.getString(i));
                }
                
                Log.i(DEBUG_TAG, "**END Calendar Event Description**");
            } while (managedCursor.moveToNext());
        } else {
            Log.i(DEBUG_TAG, "No Calendar Entry");
        }

    }

    private Uri MakeNewCalendarEntry(int calId, Dogodek dogodek) //http://stackoverflow.com/questions/5976098/how-to-set-reminder-in-android
    {
    	
      ContentValues event = new ContentValues();
      
      ContentResolver cr = getContentResolver();

      event.put("calendar_id", calId);
      event.put("title", dogodek.naslov);
      event.put("description", dogodek.opis);
      event.put("eventLocation", dogodek.lokacija);

      //long startTime = System.currentTimeMillis() + 1000 * 60*11;
      //long endTime = System.currentTimeMillis() + 1000 * 60 * 60 * 2;
      //gumbCasDo.setText(dogodek.zacetek+"\n"+startTime+"\n");

      
      event.put("dtstart", dogodek.zacetek);
      event.put("dtend", dogodek.konec);

      event.put("allDay", dogodek.celodnevni); // 0 for false, 1 for true
      event.put("eventStatus", 1); //tentative (0), confirmed (1) or canceled (2)
      event.put("visibility", 0); //default (0), confidential (1), private (2), or public (3)
      event.put("transparency", 0); //opaque (0) or transparent (1)
      event.put("hasAlarm", dogodek.alarm); // 0 for false, 1 for true

      Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");
      Uri ev = cr.insert(eventsUri, event);
      
      //reminder insert
      Uri REMINDERS_URI = Uri.parse(getCalendarUriBase() + "reminders");
      event = new ContentValues();
      event.put( "event_id", Long.parseLong(ev.getLastPathSegment()));
      event.put( "method", 1 );
      event.put( "minutes", dogodek.opozorilo );
      Uri insertedUri = cr.insert( REMINDERS_URI, event );

      

     
        return insertedUri;
    }



    private int UpdateCalendarEntry(int entryID) {
        int iNumRowsUpdated = 0;

        ContentValues event = new ContentValues();

        event.put("title", "Changed Event Title");
        event.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);

        iNumRowsUpdated = getContentResolver().update(eventUri, event, null,
                null);

        Log.i(DEBUG_TAG, "Updated " + iNumRowsUpdated + " calendar entry.");

        return iNumRowsUpdated;
    }

    private int DeleteCalendarEntry(int entryID) {
        int iNumRowsDeleted = 0;

        Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);
        iNumRowsDeleted = getContentResolver().delete(eventUri, null, null);

        Log.i(DEBUG_TAG, "Deleted " + iNumRowsDeleted + " calendar entry.");

        return iNumRowsDeleted;
    }

    /**
     * @param projection
     * @param selection
     * @param path
     * @return
     */
    private Cursor getCalendarManagedCursor(String[] projection,
            String selection, String path) {
        Uri calendars = Uri.parse("content://calendar/" + path);

        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, projection, selection,
                    null, null);
        } catch (IllegalArgumentException e) {
            Log.w(DEBUG_TAG, "Failed to get provider at ["
                    + calendars.toString() + "]");
        }

        if (managedCursor == null) {
            // try again
            calendars = Uri.parse("content://com.android.calendar/" + path);
            try {
                managedCursor = managedQuery(calendars, projection, selection,
                        null, null);
            } catch (IllegalArgumentException e) {
                Log.w(DEBUG_TAG, "Failed to get provider at ["
                        + calendars.toString() + "]");
            }
        }
        return managedCursor;
    }

    /*
     * Determines if it's a pre 2.1 or a 2.2 calendar Uri, and returns the Uri
     */
    private String getCalendarUriBase() {
        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
            // eat
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
                // eat
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }

        }

        return calendarUriBase;
    }



}