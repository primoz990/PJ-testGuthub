package edu.PrimozRezek.iManager.android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class uraActivity extends Activity implements OnClickListener {
	
	private static final int Vreme_ACTIVITY_ID = 1;
	//gumbi za preklaplanje med activity
	Button gumbUra;
	Button gumbVreme;
	Button gumbRss;
	Button gumbKoledar;
	Button gumbNastavitve;
	
	TextView text1;	
	
	
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ura);
        
        
        gumbUra = (Button) findViewById(R.id.gumbUra);
        gumbVreme = (Button) findViewById(R.id.gumbVreme);
        gumbRss = (Button) findViewById(R.id.gumbRss);
        gumbKoledar = (Button) findViewById(R.id.gumbKoledar);
        gumbNastavitve = (Button) findViewById(R.id.gumbNastavitve);
        
        text1 = (TextView) findViewById(R.id.textView1);
        
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
			Toast toast = Toast.makeText(this,"URA!" , Toast.LENGTH_LONG);
        	toast.show();
		break;
		case R.id.gumbVreme:
        	
			
			
			Intent vremeAct = new Intent(this, vremeActivity.class);
			//this.startActivityForResult(vremeAct, Vreme_ACTIVITY_ID); ZA pobratne informacije
			this.startActivity(vremeAct);
        	
		break;
		case R.id.gumbRss:
			Intent rssAct = new Intent(this, RssActivity.class);
			this.startActivity(rssAct);
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
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode==Vreme_ACTIVITY_ID)
		{
			/*if(resultCode==0)  //dsf
				{
				
				finish();
				}
			if(resultCode==-1) 
			{
				VnosnoPolje.setText("");
				infoStevec.setText("");
				if(custom==true)
				{
					sp.setEnabled(true);
					zg.setEnabled(true);
				}
			}*/
		}
	}
	
	
	
	
    
    
}