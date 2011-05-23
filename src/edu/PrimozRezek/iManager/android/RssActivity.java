package edu.PrimozRezek.iManager.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class RssActivity extends Activity implements OnClickListener  
{
	public static final int id=4;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;

    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);
        
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        
    }






	@Override
	public void onClick(View v) 
	{
		
		
		
	}
    
    


}
