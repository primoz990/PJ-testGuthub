package edu.PrimozRezek.iManager.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class koledarActivity extends Activity implements OnClickListener {
	
	public static final int id=2;

	Gumbi g;
	LinearLayout mainLL;
	
	TextView text1;	
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.koledar);

        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        text1 = (TextView) findViewById(R.id.textView1);
        
   
        
        
    }


	@Override
	public void onClick(View v) 
	{

		
	}
}
