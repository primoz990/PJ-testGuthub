package edu.PrimozRezek.iManager.android.Alarm;

import java.io.IOException;
import java.util.Random;

import edu.PrimozRezek.iManager.android.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MatematicnaBudilkaActivity extends Activity implements OnClickListener
{
	TextView TVracun, TVnapaka;
	EditText ETrezultat;
	
	//vklop ekrana
	PowerManager powMaN;
	WakeLock wakeL;
	
	//zvok
	public MediaPlayer mPlayer = null;
	
	int a, b, rezultat;
	int operator;
	int TEZAVNOST;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matematicna_budilka);
		
		TVracun = (TextView) findViewById(R.id.textViewRacun);
		TVnapaka = (TextView) findViewById(R.id.textViewNapacno);
		ETrezultat = (EditText) findViewById(R.id.editTextRezultatRacuna);
		
        //vklop ekrana
        powMaN = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeL = powMaN.newWakeLock((PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YourServie");
        wakeL.acquire();

        //odklep ekrana
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED); 
        
      //preberem nastavitve
        SharedPreferences bud_ali_odst = getSharedPreferences("BUDILKA_ALI_ODSTEVALNIK", 0);
        String kajIzkljapljamo = bud_ali_odst.getString("ali_budilka_ali_odstevalnik", "budilka");
        
        SharedPreferences zadnje_nastavitve = getSharedPreferences("NASTAVITVE_APP_LAST_SET", 0);
        TEZAVNOST = zadnje_nastavitve.getInt("tezavnost_"+kajIzkljapljamo, 0); //preberemo tezavnost za budilko ali za odstevalnik
        Uri potdomuzike22 = Uri.parse(zadnje_nastavitve.getString("glasba_"+kajIzkljapljamo, "napaka")); //preberemo izbrano glasbo za budliko ali odstevalnik
        
		startPlaying(potdomuzike22);
		
		sestaviRacun(TEZAVNOST);
		
		
		
	}
	
	public void sestaviRacun(int tezavnost)
	{
		rezultat = 0;
		a= generirajRandomStevilo(5, 10);
		b= generirajRandomStevilo(10, 20);
		operator = generirajRandomStevilo(0, 1);
		
		
		if(tezavnost==1)
		{
			TVracun.setText(b + "  -  " + a + "  =  "); //obrnjeno zaradi predznaka
			rezultat = b-a;

		}
		else if (tezavnost==2) 
		{
			TVracun.setText(a + "  *  " + b + "  =  ");
			rezultat = a*b;
		}
		else //drugače najlažje
		{
			TVracun.setText(a + "  +  " + b + "  =  ");
			rezultat = a+b;
		}
		
	}
	
	public int generirajRandomStevilo(int spodnja_meja, int zgornja_meja)
	{
		return spodnja_meja + (new Random()).nextInt(zgornja_meja-spodnja_meja);
	}
	
	
	@Override
	public void onClick(View v) 
	{
		
		String odgovorStr = ETrezultat.getText().toString();
		if(odgovorStr.length()==0) odgovorStr="-8";
		int odgovor = Integer.parseInt(odgovorStr);
		
		if(rezultat == odgovor)
		{
			wakeL.release();
			mPlayer.release();
			System.exit(0);
		}
		else
		{
			TVnapaka.setText("NAPAČEN REZULTAT!");
			sestaviRacun(TEZAVNOST);
		}
		
	}
	
	@Override
	protected void onStop() 
	{
		wakeL.release();
    	mPlayer.release();
		super.onStop();
	}
	
	private void startPlaying(Uri potDomuzike) 
	  {
	      mPlayer = new MediaPlayer();
	      try {
	    	  mPlayer.setDataSource(this, potDomuzike);
	          mPlayer.setLooping(true);
	          mPlayer.prepare();
	          mPlayer.start();
	      } catch (IOException e) 
	      {
	    	  startPlayingDefault();

	      }
	  }
		
	    //če ni izbrane glasbe predvajamo svojo
		private void startPlayingDefault() 
		  {
			mPlayer = new MediaPlayer();
				try 
			    {
					//http://androidforums.com/developer-101/205744-play-sound-assets.html
					AssetFileDescriptor descriptor = getAssets().openFd("paganini.mp3");
					mPlayer.setDataSource( descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
					descriptor.close();
					
			        mPlayer.setLooping(true);
					mPlayer.prepare();
					mPlayer.start();
			    } catch (IllegalStateException e1) 
			    {
			    	Log.e("Napaka", "Pri nalaganju glasbe");
		        
		        } catch (IOException e2) 
		        {
		        	Log.e("Napaka", "Pri nalaganju glasbe");
		        }
					
				
		  }

}
