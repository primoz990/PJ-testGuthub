package edu.PrimozRezek.iManager.android.Koledar;

import java.util.List;

import edu.PrimozRezek.iManager.android.R;
import edu.PrimozRezek.iManager.android.QuickMenu.ActionItem;
import edu.PrimozRezek.iManager.android.QuickMenu.NewQAAdapter;
import edu.PrimozRezek.iManager.android.QuickMenu.QuickAction;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SeznamDogodkov extends Activity 
{
	ListView lv;
	List seznam;
	
//	QUICKACTION VIR:
//		https://github.com/lorensiuswlt/NewQuickAction
//			http://www.londatiga.net/it/how-to-create-quickaction-dialog-in-android/
	
	private int mSelectedRow = 0;
	private ImageView mMoreIv = null;
	private static final int ID_UREDI = 1;
	private static final int ID_IZBRISI = 2;

	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seznam_dogodkov);
        
        lv = (ListView) findViewById(R.id.l_list);
        
        NewQAAdapter adapter 	= new NewQAAdapter(this);
        
        PrenosPodatkov pr = new PrenosPodatkov();
        seznam = pr.getIzbraniDogodki();
        
        String[] dogodki = new String[seznam.size()+1];
        dogodki[0] = "+ Nov Dogodek";
        for(int i=1; i<=seznam.size();i++)
        {
        	Dogodek k = (Dogodek) seznam.get(i-1);
        	dogodki[i]=k.naslov; 
        	
        }
        
        adapter.setData(dogodki);
        lv.setAdapter(adapter);
        
        ActionItem uredi 		= new ActionItem(ID_UREDI, "Uredi", getResources().getDrawable(R.drawable.ic_nastavitve));
		ActionItem izbrisi 	= new ActionItem(ID_IZBRISI, "Izbrisi", getResources().getDrawable(R.drawable.ic_brisi));
		
		final QuickAction mQuickAction 	= new QuickAction(this);
		
		mQuickAction.addActionItem(uredi);
		mQuickAction.addActionItem(izbrisi);
		
		//setup the action item click listener
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) 
			{
				int id= mSelectedRow-1;
        		Dogodek g = (Dogodek)seznam.get(Integer.parseInt(id+""));
        		
					if (actionId == ID_UREDI) 
					{
						
		        		
		        		PrenosPodatkov pk = new PrenosPodatkov();
		        		pk.setIzbranDogodekZaUrejanje(g.id);
		              
		        		odpriUrejanjeDogodka();
		        		finish();
					} 
					else if(actionId == ID_IZBRISI)
					{
						DeleteCalendarEntry(g.id);
						Toast.makeText(SeznamDogodkov.this, "Dogodek izbrisan", Toast.LENGTH_SHORT).show();
						finish();
						
					}	
	        	
			}
		});
		
		//setup on dismiss listener, set the icon back to normal
		mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() 
		{			
			@Override
			public void onDismiss() 
			{
				mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});
		
		
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				mSelectedRow = position; //set the selected row
				
				if(mSelectedRow==0)
	        	{
	        		odpriDodajanjeNovegaDogodka();
	        		finish();
	        	}
	        	else
	        	{
					mQuickAction.show(view);
					//change the right arrow icon to selected state 
					mMoreIv = (ImageView) view.findViewById(R.id.i_more);
					mMoreIv.setImageResource(R.drawable.ic_list_more_selected);
	        	}
			}
		});

    }
	



	
	public void odpriDodajanjeNovegaDogodka()
	{
		Intent dodajAct = new Intent(this, DodajanjeDogodkaVCalendarActivity.class);
		this.startActivity(dodajAct);
	}
	
	public void odpriUrejanjeDogodka()
	{
		Intent dodajAct = new Intent(this, UrediDogodekActivity.class);
		this.startActivity(dodajAct);
	}
	
	
	//ZA BRISANJE DOGODKA
    private int DeleteCalendarEntry(int entryID) 
    {
        int iNumRowsDeleted = 0;
        Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);
        iNumRowsDeleted = getContentResolver().delete(eventUri, null, null);

        return iNumRowsDeleted;
    }
    
    private String getCalendarUriBase() 
    {
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
