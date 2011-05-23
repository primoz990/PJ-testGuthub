package edu.PrimozRezek.iManager.android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class uraActivity extends Activity implements OnClickListener {
	

	public static final int id=1;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ura);
        
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        
    }






	@Override
	public void onClick(View v) 
	{

		
		
	}
	
	/*
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode==Vreme_ACTIVITY_ID)
		{
			/*if(resultCode==0)  //dsfbng
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
			}
		}
	}
	
	
	*/
	
    
    
}