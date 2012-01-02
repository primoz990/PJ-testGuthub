package edu.PrimozRezek.iManager.android;


import edu.PrimozRezek.iManager.android.BAZA.DBAdapterKoledar;
import edu.PrimozRezek.iManager.android.BAZA.KoledarBaza;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class KoledarDodajDogodekActivity extends Activity implements OnClickListener {

	DBAdapterKoledar db;
	
	
	DatePicker datePicker1;
	EditText editTextNaslov, editTextOpis;
	Button gumbDodaj;
	Spinner spnPrior;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dodajdogodek);
        
        db = new DBAdapterKoledar(this);
        
       // datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        editTextNaslov = (EditText) findViewById(R.id.editTextDodajNaslov);
        editTextOpis = (EditText) findViewById(R.id.editTextDodajOpis);
        gumbDodaj = (Button) findViewById(R.id.buttonShraniDogodek);
        spnPrior= (Spinner) findViewById(R.id.spinnerIzbiraKoledarja);
    }
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		
		String prioritete[] = {"Normalno", "Pomembno", "Majn pomembno", "Zelo pomembno"}; 
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, prioritete);
		spnPrior.setAdapter(adapter);
	}
	


	@Override
	public void onClick(View v) 
	{
		KoledarBaza x = new KoledarBaza();

		String d = datePicker1.getDayOfMonth()+"."+(datePicker1.getMonth()+1)+"."+datePicker1.getYear();	
		x.setDatum(d);
		x.setNaslovDogodka(editTextNaslov.getText().toString());
		x.setOpisDogodka(editTextOpis.getText().toString());
		x.setPrioriteta(spnPrior.getSelectedItem().toString());
		
		addDB(x);
		finish();
	}
	
	
	
	public void addDB(KoledarBaza x) {
		db.open();
		x.setDbID(db.insertDogodek(x));
		db.close();	
	}
	

}
