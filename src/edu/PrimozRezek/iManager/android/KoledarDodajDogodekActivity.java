package edu.PrimozRezek.iManager.android;

import java.sql.Date;

import edu.PrimozRezek.iManager.android.BAZA.DBAdapterKoledar;
import edu.PrimozRezek.iManager.android.BAZA.Koledar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class KoledarDodajDogodekActivity extends Activity implements OnClickListener {

	DBAdapterKoledar db;
	
	
	DatePicker datePicker1;
	EditText editText1, editText2;
	Button gumbDodaj;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dodajdogodek);
        
        db = new DBAdapterKoledar(this);
        
        datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        gumbDodaj = (Button) findViewById(R.id.button1);
    }
	


	@Override
	public void onClick(View v) 
	{
		Koledar x = new Koledar();

		String d = datePicker1.getDayOfMonth()+"."+(datePicker1.getMonth()+1)+"."+datePicker1.getYear();	
		x.setDatum(d);
		x.setNaslovDogodka(editText1.getText().toString());
		x.setOpisDogodka(editText2.getText().toString());
		
		
		addDB(x);
		
		finish();
	}
	
	
	
	public void addDB(Koledar x) {
		db.open();
		x.setDbID(db.insertDogodek(x));
		db.close();	
	}
	

}
