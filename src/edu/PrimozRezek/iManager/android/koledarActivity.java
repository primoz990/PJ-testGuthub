package edu.PrimozRezek.iManager.android;
 
//VIR:
// http://w2davids.wordpress.com/android-simple-calendar/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.PrimozRezek.iManager.android.BAZA.DBAdapterKoledar;
import edu.PrimozRezek.iManager.android.BAZA.KoledarBaza;


public class koledarActivity extends Activity implements OnClickListener {
	
	public static final int id=2;

	List<KoledarBaza> dogodki = new ArrayList<KoledarBaza>();
	DBAdapterKoledar db;
	Gumbi g;
	LinearLayout mainLL;
	
	TextView text1;	
	
	//Button dodajDog, pretvValut;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.koledar);

        g = new Gumbi( this, id);
        db = new DBAdapterKoledar(this);
        
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        text1 = (TextView) findViewById(R.id.textViewIzpisDogodkov);
        
        //dodajDog = (Button) findViewById(R.id.buttonDodajDogodek);
        //pretvValut = (Button) findViewById(R.id.Pretvornikbutton);
        
        
    }


	@Override
	public void onStart()
	{
		super.onStart();
		
		dogodki.clear();
		fillFromDB();
		
        //izpišem bazo dogodkov
        text1.setText("\nDogodki:\n\n");
        for(int i=0; i<dogodki.size(); i++) 
        {
        	text1.append("- "+
        		dogodki.get(i).datum.toString()+" "+
        		dogodki.get(i).NaslovDogodka+" "
        		+dogodki.get(i).OpisDogodka+" "
        		+dogodki.get(i).prioriteta+"\n");
        }

	}
    
    
	//@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.buttonDodajDogodek:
	
				PrenosPodatkov p = new PrenosPodatkov();
				p.setIzbranDatumIzKoledarja(new Date(112, 11, 31, 23, 59));

				Intent dodajAct = new Intent(this, CalendarActivity.class);
				this.startActivity(dodajAct);
				
			break;

		}

		
	}
	
	public void fillFromDB() {
		db.open();
		Cursor c = db.getAll();
		KoledarBaza tmp;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			tmp = new KoledarBaza();
			
			//pretvorba iz string v DATE
			/*
			String s=  c.getString(DBAdapterKoledar.POS_DATUM);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dat= new Date(90, 6, 17);
			
			try { 
				dat = (Date)sdf.parse(s); } 
			catch (Exception ee) {
				// TODO: handle exception
			}
			text1.append(c.getString(DBAdapterKoledar.POS_DATUM));
			*/
			
			
			tmp.setDatum(c.getString(DBAdapterKoledar.POS_DATUM));
			tmp.setNaslovDogodka(c.getString(DBAdapterKoledar.POS_NASLOV));
			tmp.setOpisDogodka(c.getString(DBAdapterKoledar.POS_OPIS));
			tmp.setOpisDogodka(c.getString(DBAdapterKoledar.POS_PRIORITETA));
			
			dogodki.add(tmp); 
		}
		c.close();
		db.close();
	}
	

	
	
	
}
