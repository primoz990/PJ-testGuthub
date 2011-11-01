package edu.PrimozRezek.iManager.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RssActivity extends Activity implements OnClickListener  
{
	public static final int id=4;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	TextView textViev2;


    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);
        
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        textViev2=(TextView) findViewById(R.id.textView2);
     
        
        
        
        
        
        
        try
		{
        String stran = new String();
		stran= executeHttpGet("http://www.feri.uni-mb.si/rss/novice.xml");
		
		String nova= new String();
		
		
		
		int zacetek=0;
		int konec=0;
		for(int i=0; i<5; i++)
		{
		zacetek =	stran.indexOf("<title>", zacetek+1);
		konec = stran.indexOf("</title>", konec+1);
		
		if(i!=0)nova+= stran.substring(zacetek+7, konec);	
		if(i!=0) nova+="\n\n";
		}
		
		
		char[] vmes=new char[6];
		String nova2= new String();
		for (int i=0; i<nova.length()-5; i++)
		{
			
			vmes[0]= nova.charAt(i);
			vmes[1]= nova.charAt(i+1);
			vmes[2]= nova.charAt(i+2);
			vmes[3]= nova.charAt(i+3);
			vmes[4]= nova.charAt(i+4);
			vmes[5]= nova.charAt(i+5);
			
			
			if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='8' && vmes[4]=='1' && vmes[5]==';')
			{
				nova2+='Ž';
				i+=5;
			}
			else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='5' && vmes[4]=='2' && vmes[5]==';')
			{
				nova2+='Š';
				i+=5;
			}
			else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='2' && vmes[3]=='6' && vmes[4]=='8' && vmes[5]==';')
			{
				nova2+='Č';
				i+=5;
			}
			else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='2' && vmes[3]=='6' && vmes[4]=='9' && vmes[5]==';')
			{
				nova2+='č';
				i+=5;
			}
			else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='8' && vmes[4]=='2' && vmes[5]==';')
			{
				nova2+='ž';
				i+=5;
			}
			else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='5' && vmes[4]=='3' && vmes[5]==';')
			{
				nova2+='š';
				i+=5;
			}
			else
			{
			
		    nova2+=nova.charAt(i);
			}

		}

		nova2+=vmes[1];
		nova2+=vmes[2];
		nova2+=vmes[3];
		nova2+=vmes[4];
		nova2+=vmes[5];
		
		
		textViev2.setText("\n"+nova2);
		
	
		}
		catch (Exception eee) 
		{
			textViev2.setText("Napaka! Potrebujete dostop do interneta!");
			//textViev2.setText(eee.toString());
		}

    }

    public String executeHttpGet(String naslov) throws Exception 
	{
		String page;
	    BufferedReader in = null;
	    try {
	        HttpClient client = new DefaultHttpClient();
	        HttpGet request = new HttpGet();
	        request.setURI(new URI(naslov));
	        HttpResponse response = client.execute(request);
	        long dolzina = response.getEntity().getContentLength();
	        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        StringBuffer sb = new StringBuffer("");
	        String line = "";
	        String NL = System.getProperty("line.separator");
	        
	        long total=0;
	        while ((line = in.readLine()) != null) 
	        {
	            sb.append(line + NL);
	            //publishProgress((int)(total*100/dolzina));
	        }
	        
	        
	        in.close();
	        page = sb.toString();
	        //System.out.println(page);
	        
	        
	        } 
	    finally 
		    {
			        if (in != null) 
			        {
			            try {
			                	in.close();
			                } 
			            	catch (IOException e) 
			            	{
			            		e.printStackTrace();
			                }
			        }
		     }
	    
	    return page;
	    
	    
	}




	@Override
	public void onClick(View v) 
	{
		
		
		
	}
    
    


}
