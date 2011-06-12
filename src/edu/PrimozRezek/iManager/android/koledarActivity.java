package edu.PrimozRezek.iManager.android;




import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.PrimozRezek.iManager.android.BAZA.DBAdapterKoledar;
import edu.PrimozRezek.iManager.android.BAZA.Koledar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class koledarActivity extends Activity implements OnClickListener {
	
	public static final int id=2;

	List<Koledar> dogodki = new ArrayList<Koledar>();
	DBAdapterKoledar db;
	Gumbi g;
	LinearLayout mainLL;
	
	TextView text1;	
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.koledar);

        g = new Gumbi( this, id);
        db = new DBAdapterKoledar(this);
        
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        text1 = (TextView) findViewById(R.id.textView1);
        

        
    }


	@Override
	public void onStart()
	{
		super.onStart();
		
		dogodki.clear();
		fillFromDB();
		
        //izpišem bazo dogodkov
        text1.setText("\nDogodki:\n\n");
        for(int i=0; i<dogodki.size(); i++) text1.append("- "+dogodki.get(i).datum.toString()+" "+dogodki.get(i).NaslovDogodka+" "+dogodki.get(i).OpisDogodka+"\n");

	}
    
    
	@Override
	public void onClick(View v) 
	{
		Intent dodajAct = new Intent(this, KoledarDodajDogodekActivity.class);
		this.startActivity(dodajAct);
	}
	
	public void fillFromDB() {
		db.open();
		Cursor c = db.getAll();
		Koledar tmp;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			tmp = new Koledar();
			
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
			
			dogodki.add(tmp); 
		}
		c.close();
		db.close();
	}
	
	//BAZA
	/*
	 public void fillFromDB() {
		db.open();
		Cursor c = db.getAll();
		Stevec tmp;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			tmp = new Stevec();
			tmp.setName(c.getString(DBAdapterStevec.POS_NAME));
			tmp.setStanje(c.getInt(DBAdapterStevec.POS_VALUE));
			tmp.setDbID(c.getLong(DBAdapterStevec.POS__ID));
			lista.add(tmp); 
		}
		c.close();
		db.close();
	}
	public void addDB(Stevec s) {
		db.open();
		s.setDbID(db.insertStevc(s));
		db.close();	
	}

	
	//DB dodano
	public void fillFromDBRezultati() {
		db1.open();
		Cursor c = db1.getAll();
		Rezultat tmp;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			tmp = new Rezultat();
			tmp.setIme(c.getString(DBAdapterRezultat.POS_NAME));
			tmp.setTock(c.getInt(DBAdapterRezultat.POS_STANJE));
			tmp.setId(c.getLong(DBAdapterRezultat.POS__ID));
			rezultati.add(tmp); 
		}
		c.close();
		db1.close();
	}
	public void addDBRezultat(Rezultat s) {
		db1.open();
		s.setId(db1.insertRezultat(s));
		db1.close();	
	}	//DB konec
	public void remove(Stevec a) {
		if (a!=null)
		stevci.remove(a);  //Step 4.10 Globalna lista
	}
	 
	 */
	
	
	
}
