package edu.PrimozRezek.iManager.android;
 
//VIR:
// http://w2davids.wordpress.com/android-simple-calendar/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.PrimozRezek.iManager.android.BAZA.DBAdapterKoledar;
import edu.PrimozRezek.iManager.android.BAZA.KoledarBaza;
import edu.PrimozRezek.iManager.android.Koledar.DodajanjeDogodkaVCalendarActivity;
import edu.PrimozRezek.iManager.android.Koledar.Dogodek;
import edu.PrimozRezek.iManager.android.Koledar.Koledar;
import edu.PrimozRezek.iManager.android.Koledar.PrenosPodatkov;
import edu.PrimozRezek.iManager.android.Koledar.SeznamDogodkov;


public class koledarActivity extends Activity implements OnClickListener 
{
	// Za prikaz koledarja
	private static final String tag = "SimpleCalendarViewActivity";
	private ImageView calendarToJournalButton;
	//private Button selectedDayMonthYearButton;
	private Button currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	
    int stVsehDogodkov = 0;
    public List OsnutekVsehDogodkov = new ArrayList<Dogodek>();
	
	
	

	public static final int id=2;

	List<KoledarBaza> dogodki = new ArrayList<KoledarBaza>();
	DBAdapterKoledar db;
	Gumbi g;
	LinearLayout mainLL;

	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.koledar);

        g = new Gumbi( this, id);
        
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);

        PreglejVseDogodke();
        
        //Izris Koledarja
        
        _calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		//Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

		//selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
		//selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);

        
		
		
		

        
    }
    
    @Override
    protected void onResume() 
    {
    	super.onResume();
    	
    	stVsehDogodkov=0;
    	OsnutekVsehDogodkov = new ArrayList<Dogodek>();
    	
    	PreglejVseDogodke();
        
        //Izris Koledarja
        
        _calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		//Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

		//selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
		//selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
    	
    	
    }
    

    
    
	@Override
	public void onStart()
	{
		super.onStart();
		


	}
	
	//meni
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0,0,0,"Nastavitve").setIcon(R.drawable.nastavitve2);
    	menu.add(0,1,1,"Izhod").setIcon(R.drawable.izhod2);

    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    	case 0: //nastavitve

    		Intent nastavitve = new Intent(koledarActivity.this, NastavitveActivity.class);
    		startActivity(nastavitve);

    	return true;
    	case 1: //izhod
    		finish();
    	return true;
    	}
    	
    	return false;
    }
    
    
	//@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.prevMonth:
			
				if (month <= 1)
					{
						month = 12;
						year--;
					}
				else
					{
						month--;
					}
				//Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);
			break;
			case R.id.nextMonth:
			
				if (month > 11)
					{
						month = 1;
						year++;
					}
				else
					{
						month++;
					}
				//Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);
			break;


		}

		
	}
	
	
	private void setGridCellAdapterToDate(int month, int year)
	{
		adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	

	
	public void odpriSeznamDogodkov()
	{
		Intent dodajAct = new Intent(this, SeznamDogodkov.class);
		this.startActivity(dodajAct);
	}
	
	
	//Notranji Razred za prikazovanje koledarja
	
	// ///////////////////////////////////////////////////////////////////////////////////////
	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
		{
			private static final String tag = "GridCellAdapter";
			private final Context _context;

			private final List<String> list;
			private static final int DAY_OFFSET = 1;
			private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
			private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			private final int month, year;
			private int daysInMonth, prevMonthDays;
			private int currentDayOfMonth;
			private int currentWeekDay;
			private Button gridcell;
			private TextView num_events_per_day;
			private final HashMap eventsPerMonthMap;
			private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

			// Days in Current Month
			public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
				{
					super();
					this._context = context;
					this.list = new ArrayList<String>();
					this.month = month;
					this.year = year;

					//Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
					Calendar calendar = Calendar.getInstance();
					setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
					setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//					Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
//					Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
//					Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

					// Print Month
					printMonth(month, year);

					// Find Number of Events
					eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
				}
			private String getMonthAsString(int i)
				{
					return months[i];
				}

			private String getWeekDayAsString(int i)
				{
					return weekdays[i];
				}

			private int getNumberOfDaysOfMonth(int i)
				{
					return daysOfMonth[i];
				}

			public String getItem(int position)
				{
					return list.get(position);
				}

			@Override
			public int getCount()
				{
					return list.size();
				}

			/**
			 * Prints Month
			 * 
			 * @param mm
			 * @param yy
			 */
			private void printMonth(int mm, int yy)
				{
					//Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
					// The number of days to leave blank at
					// the start of this month.
					int trailingSpaces = 0;
					int leadSpaces = 0;
					int daysInPrevMonth = 0;
					int prevMonth = 0;
					int prevYear = 0;
					int nextMonth = 0;
					int nextYear = 0;

					int currentMonth = mm - 1;
					String currentMonthName = getMonthAsString(currentMonth);
					daysInMonth = getNumberOfDaysOfMonth(currentMonth);

//					Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

					// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
					GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//					Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

					if (currentMonth == 11)
						{
							prevMonth = currentMonth - 1;
							daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
							nextMonth = 0;
							prevYear = yy;
							nextYear = yy + 1;
//							Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
						}
					else if (currentMonth == 0)
						{
							prevMonth = 11;
							prevYear = yy - 1;
							nextYear = yy;
							daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
							nextMonth = 1;
//							Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
						}
					else
						{
							prevMonth = currentMonth - 1;
							nextMonth = currentMonth + 1;
							nextYear = yy;
							prevYear = yy;
							daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//							Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
						}

					// Compute how much to leave before before the first day of the
					// month.
					// getDay() returns 0 for Sunday.
					int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
					trailingSpaces = currentWeekDay;

//					Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
//					Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
//					Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);


					if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 2)
						{
							++daysInMonth;
						}

					// Trailing Month days
					for (int i = 0; i < trailingSpaces; i++)
						{
//							Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
							list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
						}

					
					
					List dogodkiZaTaMesec = IzlusciDogodkeZatrenutniMesec(currentMonth, yy-1900); //seznam dogodkov za ta mesec
					// Current Month Days
					for (int i = 1; i <= daysInMonth; i++)
						{
//							Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
							if (i == getCurrentDayOfMonth())
								{
									list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy); //današnji dan
									
									//tu pride enako kot  spodaj
								}
							else
								{
								
								list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy); //navadni dan
									//!!! označevanje dogodkov
									    for(int h=0; h<dogodkiZaTaMesec.size(); h++)
									    {
									    	Dogodek oznaci = (Dogodek) dogodkiZaTaMesec.get(h);
									    	Date dateOznaci = new Date(oznaci.zacetek);
									    	
									    	if(i == dateOznaci.getDate())
											{
									    		//če smo naleteli na dogodek, izbrišemo zadnjega, in dodamo novega ki je označen posebaj
									    		list.remove(list.size()-1);
												list.add(String.valueOf(i) + "-WHITEvent" + "-" + getMonthAsString(currentMonth) + "-" + yy); //navadni dan
											}
									    	
									    	
									    }
								
									
								}
							
							
						}

					// Leading Month days
					for (int i = 0; i < list.size() % 7; i++)
						{
//							Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
							list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
						}
				}
			
			
			
			public List<Dogodek> IzlusciDogodkeZatrenutniMesec(int mesec, int leto)
			{
				List trenutni = new ArrayList<Dogodek>();
				
				for(int i=0; i< OsnutekVsehDogodkov.size();i++)
				{
					Dogodek iti = (Dogodek) OsnutekVsehDogodkov.get(i);
					Date datumZacetka = new Date(iti.zacetek);
					
					if(datumZacetka.getMonth()==mesec && datumZacetka.getYear()==leto) trenutni.add(iti);
					else if(datumZacetka.getMonth()==mesec && iti.konec==0 && iti.zacetek>0) trenutni.add(iti); //dogodki ki se letno ponavljajo
					
				}

				return trenutni;
			}
			

			/**
			 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
			 * ALL entries from a SQLite database for that month. Iterate over the
			 * List of All entries, and get the dateCreated, which is converted into
			 * day.
			 * 
			 * @param year
			 * @param month
			 * @return
			 */
			private HashMap findNumberOfEventsPerMonth(int year, int month)
				{
					HashMap map = new HashMap<String, Integer>();
					// DateFormat dateFormatter2 = new DateFormat();
					//						
					// String day = dateFormatter2.format("dd", dateCreated).toString();
					//
					// if (map.containsKey(day))
					// {
					// Integer val = (Integer) map.get(day) + 1;
					// map.put(day, val);
					// }
					// else
					// {
					// map.put(day, 1);
					// }
					return map;
				}

			@Override
			public long getItemId(int position)
				{
					return position;
				}

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
				{
					View row = convertView;
					if (row == null)
						{
							LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
						}

					// Get a reference to the Day gridcell
					gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
					gridcell.setOnClickListener(this);

					// ACCOUNT FOR SPACING

//					Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
					String[] day_color = list.get(position).split("-");
					String theday = day_color[0];
					String themonth = day_color[2];
					String theyear = day_color[3];
					if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
						{
							if (eventsPerMonthMap.containsKey(theday))
								{
									num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
									Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
									num_events_per_day.setText(numEvents.toString());
								}
						}

					// Set the Day GridCell
					gridcell.setText(theday);
					gridcell.setTag(theday + "-" + themonth + "-" + theyear);

//					Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

					if (day_color[1].equals("GREY"))
						{
							gridcell.setTextColor(Color.LTGRAY);
						}
					if (day_color[1].equals("WHITE"))
						{
							gridcell.setTextColor(Color.WHITE);
						}
					if (day_color[1].equals("BLUE"))
						{
							gridcell.setTextColor(getResources().getColor(R.color.static_text_color));
						}
					
					//!!!tukaj barvanje dogodkov
					if (day_color[1].equals("WHITEvent"))
					{
						gridcell.setTextColor(R.color.black);
					}
					
					
					
					
					return row;
				}
			
			
			public List<Dogodek> IzlusciDogodkeZaIzbranDatum(int dan, int mesec, int leto)
			{
				List trenutni = new ArrayList<Dogodek>();
				
				for(int i=0; i< OsnutekVsehDogodkov.size();i++)
				{
					Dogodek iti = (Dogodek) OsnutekVsehDogodkov.get(i);
					Date datumZacetka = new Date(iti.zacetek);
					
					if(datumZacetka.getDate()==dan && datumZacetka.getMonth()==mesec && datumZacetka.getYear()==leto) trenutni.add(iti);
					else if(datumZacetka.getDate()==dan && datumZacetka.getMonth()==mesec && iti.konec==0 && iti.zacetek>0) trenutni.add(iti); //dogodki ki se letno ponavljajo
					
				}

				return trenutni;
			}
			
			
			@Override
			public void onClick(View view)    
				{
					String date_month_year = (String) view.getTag();
					//selectedDayMonthYearButton.setText("Selected: " + date_month_year);  

					try
						{
							Date parsedDate = dateFormatter.parse(date_month_year);
							
							//shranim izbran datum v razredno spremenljivko
							PrenosPodatkov p = new PrenosPodatkov();
							p.setIzbranDatumIzKoledarja(new Date(parsedDate.getYear(), parsedDate.getMonth(), parsedDate.getDate()));
							
							List danasnjiDogodki = IzlusciDogodkeZaIzbranDatum(parsedDate.getDate(), parsedDate.getMonth(), parsedDate.getYear());

							PrenosPodatkov pp = new PrenosPodatkov();
							pp.setIzbraniDogodki(danasnjiDogodki);
							
							odpriSeznamDogodkov();
							

							
//							Log.d(tag, "Parsed Date: " + parsedDate.toString());

						}
					catch (ParseException e)
					{
							e.printStackTrace();
					}
				}

			public int getCurrentDayOfMonth()
				{
					return currentDayOfMonth;
				}

			private void setCurrentDayOfMonth(int currentDayOfMonth)
				{
					this.currentDayOfMonth = currentDayOfMonth;
				}
			public void setCurrentWeekDay(int currentWeekDay)
				{
					this.currentWeekDay = currentWeekDay;
				}
			public int getCurrentWeekDay()
				{
					return currentWeekDay;
				}
		}

	
	//--------------------------------------------Preberem dogodke iz koledarja---------------------------------------
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
	
	String DEBUG_TAG= "PrikazDogodkov";
	
	
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

//            Log.i(DEBUG_TAG, "Listing Selected Calendars Only");

            int nameColumn = managedCursor.getColumnIndex("name");
            int idColumn = managedCursor.getColumnIndex("_id");

            do {
            	// dobimo ime koledarja
                String calName = managedCursor.getString(nameColumn);
                String calId = managedCursor.getString(idColumn);
//                Log.i(DEBUG_TAG, "Found Calendar '" + calName + "' (ID=" + calId + ")");

                koledarji.add(new Koledar(Integer.parseInt(calId) ,calName));
                
            } while (managedCursor.moveToNext());
        } else {
//            Log.i(DEBUG_TAG, "No Calendars");
        }
        
        return koledarji;

    }
	
    public void PreglejVseDogodke()
    {
    	//preberem koledarje
		List koledarji = ListSelectedCalendars();
		
		for (int i=0; i<koledarji.size();i++)
		{
			Koledar k = (Koledar) koledarji.get(i);
			String[] projection = new String[] { "_id", "title", "dtstart", "dtend" };
			Cursor managedCursor = getCalendarManagedCursor(projection, "calendar_id=" + k.id, "events");
		       
			if (managedCursor != null && managedCursor.moveToFirst()) 
	        {
				
	            do 
	            {
//	            	Log.i(DEBUG_TAG, "dogodek: "+stVsehDogodkov);
	                stVsehDogodkov++;
	                
	                //Log.i(DEBUG_TAG, managedCursor.getString(0) + " " + managedCursor.getString(1)+ " " + managedCursor.getString(2)+ " " + managedCursor.getString(3));
	                OsnutekVsehDogodkov.add(new Dogodek(Integer.parseInt(managedCursor.getString(0)) , managedCursor.getString(1), Long.parseLong(managedCursor.getString(2)) , Long.parseLong(managedCursor.getString(3))));

	            } while (managedCursor.moveToNext());
	        } else 
	        {
	            Log.i(DEBUG_TAG, "Ni dogodkov v"+i+".koledarju");
	        }
			
		}
		
		//Toast.makeText(this, "Število eventov: "+stVsehDogodkov+"\nStevilo dog v seznamu: "+OsnutekVsehDogodkov.size(), Toast.LENGTH_LONG).show();
    }

	
	private Cursor getCalendarManagedCursor(String[] projection, String selection, String path) 
	{
        Uri calendars = Uri.parse("content://calendar/" + path);

        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, projection, selection, null, null);
        } catch (IllegalArgumentException e) 
        {
            Log.w(DEBUG_TAG, "Failed to get provider at ["+ calendars.toString() + "]");
        }

        if (managedCursor == null) 
        {
            // try again
            calendars = Uri.parse("content://com.android.calendar/" + path);
            try {
                managedCursor = managedQuery(calendars, projection, selection, null, null);
            } catch (IllegalArgumentException e) 
            {
                Log.w(DEBUG_TAG, "Failed to get provider at ["+ calendars.toString() + "]");
            }
        }
        return managedCursor;
    }

	//------------------------------------------------------------------------------------------------------------------------
	
	
	
}
