package edu.PrimozRezek.iManager.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class vremeActivity extends Activity implements OnClickListener  
{

		
		//gumbi za preklaplanje med activity
		Button gumbUra;
		Button gumbVreme;
		Button gumbRss;
		Button gumbKoledar;
		Button gumbNastavitve;
		
		
		
		
		
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.vreme);
	        
	        
	        gumbUra = (Button) findViewById(R.id.gumbUra);
	        gumbVreme = (Button) findViewById(R.id.gumbVreme);
	        gumbRss = (Button) findViewById(R.id.gumbRss);
	        gumbKoledar = (Button) findViewById(R.id.gumbKoledar);
	        gumbNastavitve = (Button) findViewById(R.id.gumbNastavitve);
	        
	        gumbUra.setOnClickListener(this);
	        gumbVreme.setOnClickListener(this);
	        gumbRss.setOnClickListener(this);
	        gumbKoledar.setOnClickListener(this);
	        gumbNastavitve.setOnClickListener(this);
	        
	        
	    }






		@Override
		public void onClick(View v) 
		{
			switch(v.getId())
			{
			case R.id.gumbUra:
				finish();
				Intent uraAct = new Intent(this, uraActivity.class);
				this.startActivity(uraAct);

			break;
			case R.id.gumbVreme:

				
			break;
			case R.id.gumbRss:
				finish();
				Intent rssAct = new Intent(this, RssActivity.class);
				this.startActivity(rssAct);
				
			break;
			case R.id.gumbKoledar:
				finish();
				Intent koledarAct = new Intent(this, koledarActivity.class);
				this.startActivity(koledarAct);
			break;
			case R.id.gumbNastavitve:
				finish();
				Intent nastavitveAct = new Intent(this, nastavitveActivity.class);
				this.startActivity(nastavitveAct);
			break;
				
			
			}
			
			
		}
	    
	    
	
	
}
