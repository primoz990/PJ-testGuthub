package edu.PrimozRezek.iManager.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RssActivity extends uraActivity implements OnClickListener  
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
		
			Intent uraAct = new Intent(this, uraActivity.class);
			this.startActivity(uraAct);

		break;
		case R.id.gumbVreme:

			Intent vremeAct = new Intent(this, vremeActivity.class);
			this.startActivity(vremeAct);
			
		break;
		case R.id.gumbRss:
			

		break;
		case R.id.gumbKoledar:
			Toast toast4 = Toast.makeText(this,"KOLEDAR!" , Toast.LENGTH_LONG);
        	toast4.show();
		break;
		case R.id.gumbNastavitve:
			Toast toast5 = Toast.makeText(this,"NASTAVITVE!" , Toast.LENGTH_LONG);
        	toast5.show();
		break;
			
		
		}
		
		
	}
    
    


}
