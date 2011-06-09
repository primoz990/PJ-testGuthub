package edu.PrimozRezek.iManager.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.R.drawable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
	TextView textViev3;
	TextView textViev4;
	TextView textViev5;
	Button gumbOsvezi;
	ProgressDialog progressBar1;
	
	ImageView imageView1;
	ImageView imageView2;
	ImageView imageView3;
	ImageView imageView4;
	
		
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
	        textViev3 = (TextView) findViewById(R.id.textView3);
	        textViev4 = (TextView) findViewById(R.id.textView4);
	        textViev5 = (TextView) findViewById(R.id.textView5);
	        
	        gumbOsvezi = (Button) findViewById(R.id.button1);
	        imageView1 = (ImageView) findViewById(R.id.imageView1);
	        imageView2 = (ImageView) findViewById(R.id.imageView2);
	        imageView3 = (ImageView) findViewById(R.id.imageView3);
	        imageView4 = (ImageView) findViewById(R.id.imageView4);
	        gumbOsvezi.setOnClickListener(this);
	        
	        onClick(gumbOsvezi);
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
			//trenutne info
			stran= executeHttpGet("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml");

			String temperatura = stran.substring(stran.indexOf("<t>")+3, stran.indexOf("</t>"));
			String stanje = stran.substring(stran.indexOf("<nn_shortText>")+14, stran.indexOf("</nn_shortText>"));
			String smerVetra = stran.substring(stran.indexOf("<dd_longText>")+13, stran.indexOf("</dd_longText>"));
			String hitrostVetraKMH = stran.substring(stran.indexOf("<ff_val_kmh>")+12, stran.indexOf("</ff_val_kmh>"));
			
			textViev1.setText("Maribor:\n "+stanje+", "+temperatura+"°C\n "+smerVetra+", "+hitrostVetraKMH+"km/h");

			//trenutna slika
			stran= executeHttpGet("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_si_latest.html");
			
			int zacetek= stran.indexOf("Ljubljana");
			String link = stran.substring(stran.indexOf("src=", zacetek)+5, stran.indexOf(".png", zacetek)+4);
			
			imageView1.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+link));
		
			
			
			
			
			//NAPOVED
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
					i+=3;
				}
				else if (vmes[0]=='<' && vmes[1]=='/' && vmes[2]=='p' && vmes[3]=='>')
				{
					i+=4;
					
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='2' && vmes[3]=='6' && vmes[4]=='9' && vmes[5]==';')
				{
					nova+='č';
					i+=6;
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='8' && vmes[4]=='2' && vmes[5]==';')
				{
					nova+='ž';
					i+=6;
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='5' && vmes[4]=='3' && vmes[5]==';')
				{
					nova+='š';
					i+=6;
				}
				if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='8' && vmes[4]=='1' && vmes[5]==';')
				{
					nova+='Ž';
					i+=6;
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='3' && vmes[3]=='5' && vmes[4]=='2' && vmes[5]==';')
				{
					nova+='Š';
					i+=6;
				}
				else if (vmes[0]=='&' && vmes[1]=='#' && vmes[2]=='2' && vmes[3]=='6' && vmes[4]=='8' && vmes[5]==';')
				{
					nova+='Č';
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
			
			textViev2.setText("\nNapoved:\n "+nova);
			
			
			// Napoved TEXT
			
			stran= executeHttpGet("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml");
			
			String[] dan = new String[3];
			String[] situacija = new String[3];
			String[] Tmin = new String[3];
			String[] Tmax = new String[3];
			zacetek=0;
			for(int i=0; i<4; i++)
			{
				zacetek= stran.indexOf("<valid_day>", zacetek+1);
				
				if(i>0) //prvo napoved izpustimo
				{
					dan[i-1] = 	stran.substring(zacetek+11, stran.indexOf("CEST</valid_day>", zacetek));
					situacija[i-1]= stran.substring(stran.indexOf("<nn_shortText>", zacetek)+14, stran.indexOf("</nn_shortText>", zacetek));
					Tmin[i-1]= 	stran.substring(stran.indexOf("<tn>", zacetek)+4, stran.indexOf("</tn>", zacetek));
					Tmax[i-1]= 	stran.substring(stran.indexOf("<tx>", zacetek)+4, stran.indexOf("</tx>", zacetek));
				}
				
				textViev3.setText(dan[0]+"\n"+situacija[0]+"\n"+Tmin[0]+"-"+Tmax[0]+"°C");
				textViev4.setText(dan[1]+"\n"+situacija[1]+"\n"+Tmin[1]+"-"+Tmax[1]+"°C");
				textViev5.setText(dan[2]+"\n"+situacija[2]+"\n"+Tmin[2]+"-"+Tmax[2]+"°C");
				
			}

			
			
			//Napoveed SLIKICE
			
			stran= executeHttpGet("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.html");
			
			String[] linki = new String[3];
			zacetek=0;
			for(int i=0; i<4; i++)
			{
				zacetek= stran.indexOf("<img src=", zacetek+1);
				
				if(i>0) //prvo napoved izpustimo
				{
					linki[i-1] = stran.substring(zacetek+10, stran.indexOf(".png", zacetek)+4);
				}
			}
			
			imageView2.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+linki[0]));
			imageView3.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+linki[1]));
			imageView4.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+linki[2]));
			
			
			
			
			}
			catch (Exception eee) 
			{
				textViev1.setText("Napaka! Potrebujete dostop do interneta!");
			}
			
			

			
			
			
			
			
	
		}
		
		public Bitmap getBitmapFromURL(String src) {
	        try {
	            Log.e("src",src);
	            URL url = new URL(src);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            Log.e("Bitmap","returned");
	            return myBitmap;
	        } catch (IOException e) {
	            e.printStackTrace();
	            Log.e("Exception",e.getMessage());
	            return null;
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

