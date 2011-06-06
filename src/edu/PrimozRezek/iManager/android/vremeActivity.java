package edu.PrimozRezek.iManager.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class vremeActivity extends Activity implements OnClickListener  
{
	public static final int id=3;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;

	TextView textViev1;
	TextView textViev2;
	Button gumbOsvezi;
	ProgressDialog progressBar1;
		
	//OsveziVreme vreme;
		
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.vreme);
	        
	        g = new Gumbi( this, id);
	        mainLL = (LinearLayout) findViewById(R.id.mainLL);
	        mainLL.addView(g,0);

	        
	        textViev1 = (TextView) findViewById(R.id.textView1);
	        textViev2 = (TextView) findViewById(R.id.textView2);
	        gumbOsvezi = (Button) findViewById(R.id.button1);
	        gumbOsvezi.setOnClickListener(this);
	        
	        //vreme= new OsveziVreme();
	        
	    }

	    
	    @Override
		public void onResume() 
		{
	    	super.onResume();
	    	

	    	
	    	
	    	
		}



		@Override
		public void onClick(View v) 
		{
			String stran = new String();
			 
			try
			{
			stran= executeHttpGet("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml");

			String temperatura = stran.substring(stran.indexOf("<t>")+3, stran.indexOf("</t>"));
			String stanje = stran.substring(stran.indexOf("<nn_shortText>")+14, stran.indexOf("</nn_shortText>"));
			String smerVetra = stran.substring(stran.indexOf("<dd_longText>")+13, stran.indexOf("</dd_longText>"));
			String hitrostVetraKMH = stran.substring(stran.indexOf("<ff_val_kmh>")+12, stran.indexOf("</ff_val_kmh>"));
			
			textViev1.setText("Maribor:\n "+stanje+", "+temperatura+"°C\n "+smerVetra+", "+hitrostVetraKMH+"km/h");
			
			}
			catch (Exception eee) 
			{
				textViev1.setText("Napaka! Potrebujete dostop do interneta!");
			}
			
			
			try
			{
			stran= executeHttpGet("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_d1-d2_text.html");
			
			
			String napoved = stran.substring(stran.indexOf("<p>")+3, stran.lastIndexOf("</p>"));
			String nova= new String();
			char[] vmes=new char[6];
			for (int i=0; i<napoved.length()-5; i++)
			{
				
				vmes[0]= napoved.charAt(i);
				vmes[1]= napoved.charAt(i+1);
				vmes[2]= napoved.charAt(i+2);
				vmes[3]= napoved.charAt(i+3);
				vmes[4]= napoved.charAt(i+4);
				vmes[5]= napoved.charAt(i+5);
				
				
				if(vmes[0]=='<' && vmes[1]=='p' && vmes[2]=='>')
				{
					i+=2;
				}
				else if (vmes[0]=='<' && vmes[1]=='/' && vmes[2]=='p' && vmes[3]=='>')
				{
					i+=3;
					
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='2' && vmes[3]=='6' && vmes[4]=='9' && vmes[5]==';')
				{
					nova+='č';
					i+=5;
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='8' && vmes[4]=='2' && vmes[5]==';')
				{
					nova+='ž';
					i+=5;
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='5' && vmes[4]=='3' && vmes[5]==';')
				{
					nova+='š';
					i+=5;
				}
				else
				{
				
			    nova+=napoved.charAt(i);
				}

			}

			nova+=vmes[1];
			nova+=vmes[2];
			nova+=vmes[3];
			nova+=vmes[4];
			nova+=vmes[5];
			
			textViev2.setText("Napoved:\n "+nova);
			}
			catch (Exception eee) 
			{
				textViev2.setText("Napaka! Potrebujete dostop do interneta!");
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
			/*
		    private class OsveziVreme extends AsyncTask<String, Integer, String>
		    {
		    	@Override
				protected void onPreExecute() 
		    	{
		    		
		    		progressBar1 = new ProgressDialog(vremeActivity.this);
		    		progressBar1.setMessage("Prenos poteka");
		    		progressBar1.setIndeterminate(false);
		    		progressBar1.setMax(100);
		    		progressBar1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		    		progressBar1.setCancelable(true);
		    		progressBar1.show();
		    	}

		    	
		    	
		    	
		        @Override
		        protected String doInBackground(String... url) 
		        {
		        	String stran = new String();
					 
					try
					{
					stran= executeHttpGet();

					String temperatura = stran.substring(stran.indexOf("<t>")+3, stran.indexOf("</t>"));
					String stanje = stran.substring(stran.indexOf("<nn_shortText>")+14, stran.indexOf("</nn_shortText>"));
					String smerVetra = stran.substring(stran.indexOf("<dd_longText>")+13, stran.indexOf("</dd_longText>"));
					String hitrostVetraKMH = stran.substring(stran.indexOf("<ff_val_kmh>")+12, stran.indexOf("</ff_val_kmh>"));
					
					textViev1.setText("Maribor:\n "+stanje+", "+temperatura+"°C\n "+smerVetra+", "+hitrostVetraKMH+"km/h");
					
					}
					catch (Exception eee) 
					{
						textViev1.setText("Napaka! Potrebujete dostop do interneta!");
					}
		        	
		        
		            return null;
		        }
		        
		        protected void onProgressUpdate(Integer... args)
		        {
		        	progressBar1.setProgress(args[0]);
		        }
		        protected void onPostExecute(String arg) {
					
					progressBar1.cancel();
				}
		        

		    }
		    
			
			*/

		//pridobim HTML
		
	    
	
	
}

