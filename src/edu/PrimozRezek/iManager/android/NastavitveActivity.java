package edu.PrimozRezek.iManager.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class NastavitveActivity extends Activity implements OnClickListener, SeekBar.OnSeekBarChangeListener
{
	public MediaPlayer mPlayer = null;
	private static final int REQUEST_CODE_IZBOR_MUZIKE = 0;
	private static final int REQUEST_CODE_IZBOR_MUZIKE_ZA_ODSTEVALNIK=1;
	RadioButton RBnavadna, RBfizicna, RBmatematicna, RBnavadniOdst, RBfizicniOdst, RBmatOdst;
	SeekBar SBfizicna, SBmatematicna, SBfizicniOdst, SBmatematicniOdst;
	TextView TVtezavnost, TVstUdarcev, TVtezavnostOdst, TVstUdarcevOdst;
	

	int izbor_budilke=0;
	int izbor_odstevalnika=0;
	String glasba_budilka="";
	String glasba_odstevalnik="";  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nastavitve);
		
		TVstUdarcev = (TextView) findViewById(R.id.textViewStUdarcev);
		TVtezavnost = (TextView) findViewById(R.id.TextViewTezavnost);
		TVtezavnostOdst = (TextView) findViewById(R.id.TextViewTezavnostOdstevalnika);
		TVstUdarcevOdst = (TextView) findViewById(R.id.TextViewStUdarcevOdstevalnika);
		
		RBnavadna = (RadioButton)findViewById(R.id.radioNavadna);
		RBfizicna = (RadioButton)findViewById(R.id.radioFizicna);
		RBmatematicna = (RadioButton)findViewById(R.id.radioMatematicna);
		RBnavadniOdst = (RadioButton)findViewById(R.id.RadioButtonNavadniOdstevalnik);
		RBfizicniOdst = (RadioButton)findViewById(R.id.RadioButtonFizicniOdstevalnik);
		RBmatOdst = (RadioButton)findViewById(R.id.RadioButtonMatematicniOdstevalnik);
		
		SBfizicna = (SeekBar) findViewById(R.id.seekBarFizicna);
		SBmatematicna = (SeekBar) findViewById(R.id.SeekBarMatematicna);
		SBmatematicniOdst = (SeekBar) findViewById(R.id.SeekBarTezavnostOdstevalnika);
		SBfizicniOdst = (SeekBar) findViewById(R.id.SeekBarFizicniOdstevalnik);
		
		SBfizicna.setMax(50);
		SBmatematicna.setMax(2);
		SBfizicniOdst.setMax(50);
		SBmatematicniOdst.setMax(2);
		
		SBfizicna.setOnSeekBarChangeListener(this);
		SBmatematicna.setOnSeekBarChangeListener(this);
		SBmatematicniOdst.setOnSeekBarChangeListener(this);
		SBfizicniOdst.setOnSeekBarChangeListener(this);
		
		
		NaloziGui();
	}
	
	public void NaloziGui()
	{
		SharedPreferences zadnje_nastavitve = getSharedPreferences("NASTAVITVE_APP_LAST_SET", 0);
		
		//izbor budilke
		int izb_bud = zadnje_nastavitve.getInt("izbor_budilka", 0);
		if(izb_bud==0)
		{
			RBnavadna.setChecked(true);
			RBfizicna.setChecked(false);
			RBmatematicna.setChecked(false);
			izbor_budilke=0;
		}
		else if(izb_bud==1)
		{
			RBnavadna.setChecked(false);
			RBfizicna.setChecked(true);
			RBmatematicna.setChecked(false);
			izbor_budilke=1;
		}
		else if(izb_bud==2)
		{
			RBnavadna.setChecked(false);
			RBfizicna.setChecked(false);
			RBmatematicna.setChecked(true);
			izbor_budilke=2;
		}
		
		//izbor odštevalnika
		int izb_ods = zadnje_nastavitve.getInt("izbor_odstevalnik", 0);
		if(izb_ods==0)
		{
			RBnavadniOdst.setChecked(true);
			RBfizicniOdst.setChecked(false);
			RBmatOdst.setChecked(false);
			izbor_odstevalnika=0;
		}
		else if(izb_ods==1)
		{
			RBnavadniOdst.setChecked(false);
			RBfizicniOdst.setChecked(true);
			RBmatOdst.setChecked(false);
			izbor_odstevalnika=1;
		}
		else if(izb_ods==2)
		{
			RBnavadniOdst.setChecked(false);
			RBfizicniOdst.setChecked(false);
			RBmatOdst.setChecked(true);
			izbor_odstevalnika=2;
		}
		
		//ostale nastavitve
		SBfizicna.setProgress(zadnje_nastavitve.getInt("st_udarcev_budilka", 0));
		SBfizicniOdst.setProgress(zadnje_nastavitve.getInt("st_udarcev_odstevalnik", 0));
		SBmatematicna.setProgress(zadnje_nastavitve.getInt("tezavnost_budilka", 0));
		SBmatematicniOdst.setProgress(zadnje_nastavitve.getInt("tezavnost_odstevalnik", 0));
		
		glasba_budilka = zadnje_nastavitve.getString("glasba_budilka", null);
		glasba_odstevalnik = zadnje_nastavitve.getString("glasba_odstevalnik", null);

	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) 
	{
		if(seekBar.getId()==R.id.seekBarFizicna)
		{
			TVstUdarcev.setText("Število udarcev: "+ seekBar.getProgress());
		}
		else if (seekBar.getId()==R.id.SeekBarMatematicna)
		{
			TVtezavnost.setText("Težavnost: "+(seekBar.getProgress()+1)+"/3");
		}
		else if (seekBar.getId()==R.id.SeekBarFizicniOdstevalnik)
		{
			TVstUdarcevOdst.setText("Število udarcev: "+ seekBar.getProgress());
		}
		else if (seekBar.getId()==R.id.SeekBarTezavnostOdstevalnika)
		{
			TVtezavnostOdst.setText("Težavnost: "+(seekBar.getProgress()+1)+"/3");
		}
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}
	

	@Override
	protected void onStop() 
	{
		//TUKAJ SHRANIM SPREMENJENE NASTAVITVE
		
		int st_ud_bud = SBfizicna.getProgress();
		int st_ud_ods = SBfizicniOdst.getProgress();
		int tezavnost_bud = SBmatematicna.getProgress();
		int tezavnost_odst = SBmatematicniOdst.getProgress();
		
		Log.e("tezavnost shranjevanje", "budilka: "+tezavnost_bud+"  odst: "+tezavnost_odst);
		
		SharedPreferences nastavitve = getSharedPreferences("NASTAVITVE_APP_LAST_SET", 0);
        SharedPreferences.Editor editor = nastavitve.edit();
        editor.putInt("izbor_budilka", izbor_budilke);
        editor.putInt("izbor_odstevalnik", izbor_odstevalnika);
        editor.putInt("st_udarcev_budilka", st_ud_bud);
        editor.putInt("st_udarcev_odstevalnik", st_ud_ods);
        editor.putInt("tezavnost_budilka", tezavnost_bud);
        editor.putInt("tezavnost_odstevalnik", tezavnost_odst);
        editor.putString("glasba_budilka", glasba_budilka);
        editor.putString("glasba_odstevalnik", glasba_odstevalnik);
        editor.commit();
		
		Toast.makeText(this, "Nastavitve shranjene", Toast.LENGTH_SHORT).show();

		super.onStop();
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.radioNavadna:
			RBnavadna.setChecked(true);
			RBfizicna.setChecked(false);
			RBmatematicna.setChecked(false);
			izbor_budilke=0;
			
		break;
		case R.id.radioFizicna:
			RBnavadna.setChecked(false);
			RBfizicna.setChecked(true);
			RBmatematicna.setChecked(false);
			izbor_budilke=1;
			
			break;
		case R.id.radioMatematicna:
			RBnavadna.setChecked(false);
			RBfizicna.setChecked(false);
			RBmatematicna.setChecked(true);
			izbor_budilke=2;
			
		break;
		case R.id.RadioButtonNavadniOdstevalnik:
			RBnavadniOdst.setChecked(true);
			RBfizicniOdst.setChecked(false);
			RBmatOdst.setChecked(false);
			izbor_odstevalnika=0;
			
		break;
		case R.id.RadioButtonFizicniOdstevalnik:
			RBnavadniOdst.setChecked(false);
			RBfizicniOdst.setChecked(true);
			RBmatOdst.setChecked(false);
			izbor_odstevalnika=1;
			
			break;
		case R.id.RadioButtonMatematicniOdstevalnik:
			RBnavadniOdst.setChecked(false);
			RBfizicniOdst.setChecked(false);
			RBmatOdst.setChecked(true);
			izbor_odstevalnika=2;
			
		break;
		case R.id.buttonIzberiMuzikoBudilke:
			
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		    intent.setType("audio/ringtones/mp3");
		    startActivityForResult(Intent.createChooser(intent, "Izberi zvonenje"), REQUEST_CODE_IZBOR_MUZIKE);
			
		break;
		case R.id.ButtonGlasbaOdstevalnika:
			
			Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		    intent2.setType("audio/ringtones/mp3");
		    startActivityForResult(Intent.createChooser(intent2, "Izberi zvonenje"), REQUEST_CODE_IZBOR_MUZIKE_ZA_ODSTEVALNIK);
			
		break;
		case R.id.buttonUstvariBackup:
			
			
			
			
		break;
		
		}
		
	}
	

//	private void startPlaying(Uri potDomuzike) 
//    {
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(this, potDomuzike);
//            mPlayer.setLooping(true);
//            mPlayer.prepare();
//            mPlayer.start();
//        } catch (IOException e) {
//            //txtViev1.setText("napaka: ");
//        }
//    }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK) 
		{
			if (requestCode == REQUEST_CODE_IZBOR_MUZIKE)
			{
			
				glasba_budilka = data.getData().toString();
				//startPlaying(Uri.parse(potDomuzike));
				//shrani v sharedpref!!

			}
			if(requestCode == REQUEST_CODE_IZBOR_MUZIKE_ZA_ODSTEVALNIK)
			{
				glasba_odstevalnik = data.getData().toString();
			}
		}
	}


	

}
