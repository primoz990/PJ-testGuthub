package edu.PrimozRezek.iManager.android.Koledar;

import java.util.Date;

import edu.PrimozRezek.iManager.android.R;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class UrediDogodekActivity extends Activity implements OnClickListener
{
	
	static final int TIME_DIALOG_ID_OD = 0;
	static final int TIME_DIALOG_ID_DO = 3;
	static final int DATE_DIALOG_ID_OD = 1;
	static final int DATE_DIALOG_ID_DO = 2;
	
    private static final String DEBUG_TAG = "CalendarActivity";
    Date Od, Do;
	
    Button gumbCasOd, gumbCasDo, gumbDatumOd, gumbDatumDo;
    EditText ETopis, ETnaslov, ETlokacija;
    Spinner SPNizbiraKoledarja, SPNopozorilo;
    CheckBox CHKBXceliDan, CHKBXopozorilo;
    TextView txtViewImeKoledarja;
    
    Dogodek prebrani;
    String  calendar_id="";
    int eventID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uredi_dogodek);
		
		gumbCasOd = (Button)findViewById(R.id.buttonOdCasSpremeni);
        gumbCasDo = (Button) findViewById(R.id.buttonDoCasSpremeni);
        gumbDatumOd = (Button) findViewById(R.id.buttonOdDatumSpremeni);
        gumbDatumDo = (Button) findViewById(R.id.buttonDoDatumSpremeni);
        ETnaslov = (EditText) findViewById(R.id.editTextSpremeniNaslov);
        ETopis = (EditText) findViewById(R.id.editTextSpremeniOpis);
        ETlokacija = (EditText) findViewById(R.id.EditTextSpremeniLokacija);
        CHKBXceliDan = (CheckBox) findViewById(R.id.checkBoxSpremeniCeliDan);
        CHKBXopozorilo = (CheckBox) findViewById(R.id.checkBoxSpremeniOpozorilo);
        SPNopozorilo = (Spinner) findViewById(R.id.spinnerSpremeniOpozorilo);
        txtViewImeKoledarja = (TextView) findViewById(R.id.textViewImeKoledarja);
		
        String[] ure = {"10 minut", "15 minut", "20 minut", "30 minut", "45 minut", "60 minut"};
		ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ure);
		SPNopozorilo.setAdapter(adapter2);
		
		Od = new Date(2012, 0, 3, 8, 30);
        Do = new Date(2012, 0, 3, 9, 30);
        
        
        prebrani = new Dogodek();
        PrenosPodatkov pp = new PrenosPodatkov();
        eventID = pp.getIzbranDogodekZaUrejanje();
        
        ListCalendarEntrySummary(eventID);   
        ListEventReminder(eventID);
        txtViewImeKoledarja.setText(ListCalendarName(Integer.parseInt(calendar_id)));

        preveriCeJeDogodekCelodnevni();
		
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{  
		case R.id.buttonUrediDogodek:
			
			//pridobim novi dogodek iz GUI
        	int celdan=0;
        	if(CHKBXceliDan.isChecked()==true) celdan =1;
        	int alarm = 0;
        	if(CHKBXopozorilo.isChecked()==true) alarm =1;
        	int opozorilo =Integer.parseInt(SPNopozorilo.getSelectedItem().toString().substring(0, 2));

        	Dogodek dog = new Dogodek(ETnaslov.getText().toString(), ETopis.getText().toString(), Od.getTime(), Do.getTime(), ETlokacija.getText().toString(), celdan, alarm, opozorilo);
        	
			UpdateCalendarEntry(eventID, dog);
			
			Toast.makeText(this, "Dogodek shranjen", Toast.LENGTH_SHORT).show();
			finish();
			
		break;
		case R.id.buttonIzbrisiDogodek:
			
			DeleteCalendarEntry(eventID);
			Toast.makeText(this, "Dogodek izbrisan", Toast.LENGTH_SHORT).show();
			finish();
			
		break;
		case R.id.buttonOdCasSpremeni:
			showDialog(TIME_DIALOG_ID_OD);
		break;
		case R.id.buttonDoCasSpremeni:
			showDialog(TIME_DIALOG_ID_DO);
		break;
		case R.id.buttonOdDatumSpremeni:
			showDialog(DATE_DIALOG_ID_OD);
		break;
		case R.id.buttonDoDatumSpremeni:
			showDialog(DATE_DIALOG_ID_DO);
		break;
		case R.id.checkBoxSpremeniCeliDan:
			
			preveriCeJeDogodekCelodnevni();
			
		break;
		}
		
	}
	
	public void preveriCeJeDogodekCelodnevni()
	{
		if(CHKBXceliDan.isChecked())
		{
			gumbCasDo.setText("");
			gumbCasOd.setText("");
			
			gumbCasDo.setEnabled(false);
			gumbCasOd.setEnabled(false);
		}
		else
		{
			gumbCasDo.setEnabled(true);
			gumbCasOd.setEnabled(true);
	        
			String minute = Do.getMinutes()+"";
			if(minute.length()==1) minute = "0"+minute;
			gumbCasDo.setText(Do.getHours()+":"+minute);
			minute = Od.getMinutes()+"";
			if(minute.length()==1) minute = "0"+minute;
	        gumbCasOd.setText(Od.getHours()+":"+minute);
		}
	}
	
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
	 *
	 *
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
	private void ListCalendarEntrySummary(int eventId) 
	{
        String[] projection = new String[] { "_id", "calendar_id", "title", "description", "eventLocation", "dtstart", "dtend", "allDay", "hasAlarm" };
        Cursor managedCursor = getCalendarManagedCursor(projection, null, "events/" + eventId);

        if (managedCursor != null && managedCursor.moveToFirst()) {


            int _idColumn = managedCursor.getColumnIndex("_id");
            int calendar_idColumn = managedCursor.getColumnIndex("calendar_id");
            int titleColumn = managedCursor.getColumnIndex("title");
            int descriptionColumn = managedCursor.getColumnIndex("description");
            int eventLocationColumn = managedCursor.getColumnIndex("eventLocation");
            int dtstartColumn = managedCursor.getColumnIndex("dtstart");
            int dtendColumn = managedCursor.getColumnIndex("dtend");
            int allDayColumn = managedCursor.getColumnIndex("allDay");
            int hasAlarmColumn = managedCursor.getColumnIndex("hasAlarm");
            
            //String _id = managedCursor.getString(_idColumn);
            //osnovni podatki
            calendar_id = managedCursor.getString(calendar_idColumn);
            ETnaslov.setText(managedCursor.getString(titleColumn));
            ETopis.setText(managedCursor.getString(descriptionColumn));
            ETlokacija.setText(managedCursor.getString(eventLocationColumn));
            
            //čas
            String dtstart= managedCursor.getString(dtstartColumn);
            String dtend= managedCursor.getString(dtendColumn);
            Od.setTime(Long.parseLong(dtstart));
            Do.setTime(Long.parseLong(dtend));

            gumbDatumOd.setText(Od.getDate()+"."+(Od.getMonth()+1)+"."+(Od.getYear()+1900));
            gumbDatumDo.setText(Do.getDate()+"."+(Do.getMonth()+1)+"."+(Do.getYear()+1900));
            
            String minute = Od.getMinutes()+"";
            if(minute.length()==1) minute="0"+minute;
            gumbCasOd.setText(Od.getHours()+":"+minute);
            minute = Do.getMinutes()+"";
            if(minute.length()==1) minute="0"+minute;
            gumbCasDo.setText(Do.getHours()+":"+minute);
            
            //celiDan
            String allDay= managedCursor.getString(allDayColumn);
            if(Integer.parseInt(allDay)==1) CHKBXceliDan.setChecked(true);
            else  CHKBXceliDan.setChecked(false);
            //alarm
            String hasAlarm= managedCursor.getString(hasAlarmColumn);
            if(Integer.parseInt(hasAlarm)==1)  CHKBXopozorilo.setChecked(true);
            else CHKBXopozorilo.setChecked(false);

        } else 
        {
            Log.i(DEBUG_TAG, "No Calendar Entry");
        }

    }
	
	
	private void ListEventReminder(int eventId) 
	{
        String[] projection = new String[] {"_id", "minutes" };
        Cursor managedCursor = getCalendarManagedCursor(projection, "event_id="+eventId, "reminders/");

        if (managedCursor != null && managedCursor.moveToFirst()) 
        {
            
        	int minutesColumn = managedCursor.getColumnIndex("minutes");
        	int idColumn = managedCursor.getColumnIndex("_id");

            String Minutes = managedCursor.getString(minutesColumn);
            String _id = managedCursor.getString(idColumn);
            reminder_id = Long.parseLong(_id);
            int a = Integer.parseInt(Minutes);
            for(int i=0; i<SPNopozorilo.getCount(); i++) if (a == Integer.parseInt(SPNopozorilo.getItemAtPosition(i).toString().substring(0, 2))) SPNopozorilo.setSelection(i);	
           
            Log.e("REMINDER", "_id: "+_id+"Minute: "+Minutes);
            
        } else {
            Log.i(DEBUG_TAG, "No Reminder!");
        }

    }
	
	long reminder_id;
	
	private String ListCalendarName(int idKoledarja) 
    {
		String[] projection = new String[] { "_id", "name", "_sync_account" };
        Cursor managedCursor = getCalendarManagedCursor(projection, null, "calendars");
        
        if (managedCursor != null && managedCursor.moveToFirst()) 
        {

            int idColumn = managedCursor.getColumnIndex("_id");
            int nameColumn = managedCursor.getColumnIndex("name");
            int syncColumn = managedCursor.getColumnIndex("_sync_account");
            
            do 
            {
                String Id = managedCursor.getString(idColumn);
                String Name = managedCursor.getString(nameColumn);
                String Sync = managedCursor.getString(syncColumn);
                    
                    
                //Log.i(DEBUG_TAG, "Ime koledarja: "+Name + "  Sync Ime koledarja: " + Sync);
                    
                if(Integer.parseInt(Id)==idKoledarja)
                {
                	if(Name==null) return Sync;
                	else return Name;
                }	

            } while (managedCursor.moveToNext());
        } else {
            Log.i(DEBUG_TAG, "No Calendars");
        }
        
        return "Ni koledarja!";

    }
	
    /**
     * @param projection
     * @param selection
     * @param path
     * @return
     */
	private Cursor getCalendarManagedCursor(String[] projection, String selection, String path) {
        Uri calendars = Uri.parse("content://calendar/" + path);

        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, projection, selection,
                    null, null);
        } catch (IllegalArgumentException e) {
            Log.w(DEBUG_TAG, "Failed to get provider at [" + calendars.toString() + "]");
        }

        if (managedCursor == null) {
            // try again
            calendars = Uri.parse("content://com.android.calendar/" + path);
            try {
                managedCursor = managedQuery(calendars, projection, selection, null, null);
            } catch (IllegalArgumentException e) {
                Log.w(DEBUG_TAG, "Failed to get provider at ["+ calendars.toString() + "]");
            }
        }
        return managedCursor;
    }
	
	
	
	
	private int UpdateCalendarEntry(int entryID, Dogodek dogodek) 
	{
   

        ContentValues event = new ContentValues();

        
        event.put("calendar_id", calendar_id);
        event.put("title", dogodek.naslov);
        event.put("description", dogodek.opis);
        event.put("eventLocation", dogodek.lokacija);
        
        event.put("dtstart", dogodek.zacetek);
        event.put("dtend", dogodek.konec);

        event.put("allDay", dogodek.celodnevni); // 0 for false, 1 for true
        event.put("eventStatus", 1); //tentative (0), confirmed (1) or canceled (2)
        event.put("visibility", 0); //default (0), confidential (1), private (2), or public (3)
        event.put("transparency", 0); //opaque (0) or transparent (1)
        event.put("hasAlarm", dogodek.alarm); // 0 for false, 1 for true

        Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);
        
        int i = getContentResolver().update(eventUri, event, null, null);
        
        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase() + "reminders");
        Uri remUri = ContentUris.withAppendedId(REMINDERS_URI, reminder_id);//ID
        event = new ContentValues();
        event.put( "minutes", dogodek.opozorilo );
       
        int j = getContentResolver().update(remUri, event, null, null);

        

        //Log.i(DEBUG_TAG, "Updated " + (i+j) + " calendar entry.");

        return i+j;
    }

    private int DeleteCalendarEntry(int entryID) 
    {
        int iNumRowsDeleted = 0;
        Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);
        iNumRowsDeleted = getContentResolver().delete(eventUri, null, null);

        //Log.i(DEBUG_TAG, "Deleted " + iNumRowsDeleted + " calendar entry.");

        return iNumRowsDeleted;
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
			return new TimePickerDialog(this, mTimeSetListener, Od.getHours(), Od.getMinutes(), true);
		case TIME_DIALOG_ID_DO:
			return new TimePickerDialog(this, mTimeSetListener2, Do.getHours(), Do.getMinutes(), true);
			
		case DATE_DIALOG_ID_OD:
			return new DatePickerDialog(this, mDateSetListener, Od.getYear()+1900, Od.getMonth(), Od.getDate());
		case DATE_DIALOG_ID_DO:
			return new DatePickerDialog(this, mDateSetListener2, Do.getYear()+1900, Do.getMonth(), Do.getDate());
		}
	return null;
	}

	//-----------------------          ---OnCreate

}
