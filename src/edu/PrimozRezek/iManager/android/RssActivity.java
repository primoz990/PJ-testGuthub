package edu.PrimozRezek.iManager.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RssActivity extends Activity implements OnClickListener  
{
	public class Novica
	{
		String naslov;
		String url;
		
		public Novica() 
		{
			this.naslov="";
			this.url="";
		}
		public Novica(String naslov, String url) 
		{
			this.naslov = naslov;
			this.url = url;
		}
		public String getNaslov() 
		{
			return naslov;
		}
		public void setNaslov(String naslov) 
		{
			this.naslov = naslov;
		}
		public String getUrl() 
		{
			return url;
		}
		public void setUrl(String url) 
		{
			this.url = url;
		}
		
		
		
	}
	
	
	
	
	public static final int id=4;
	//gumbi za preklaplanje med activity
	Gumbi g;

	LinearLayout mainLL;
	TextView txtViev1, txtG, txtViewNazadnjeOsvezeno;
	
	Button gumbOsvezi;

	
	public NodeList linki;
	public NodeList novice;
	TextView[] poljeTxtViev;
	ImageView[] iv;
	
	LinearLayout LLG;
	int velikostNovic=0;
	
	ProgressDialog dialogWait;

	public List SeznamNovic = new ArrayList<Novica>();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);
        
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);  
        
        txtViev1=(TextView) findViewById(R.id.textViewRSS1);
        txtViewNazadnjeOsvezeno=(TextView) findViewById(R.id.textViewNazadnjeOsvezeneNovice);
        gumbOsvezi = (Button) findViewById(R.id.buttonOsveziRSS);

        naloziNoviceIzDat();
        
        
	}
    
    public void naloziNoviceIzDat()
    {
    	//nazadnje osveženo:
    	SharedPreferences nazadnje_osvezeno = getSharedPreferences("NOVICE_OSVEZITEV", 0);
        txtViewNazadnjeOsvezeno.setText("Nazadnje osveženo: "+nazadnje_osvezeno.getString("nazadnje_osvezene_novice", " "));
    	
    	
    	try
        {
    		preberiizdat("/mnt/sdcard/iManager/Novice.txt");
        }catch (Exception e) 
        {
        	txtViev1.setText("Ni novic, Osveži.");
		}
        
        
        if(SeznamNovic.size()>0)
        {
        	
            poljeTxtViev = new TextView[velikostNovic];
    		LLG = (LinearLayout) findViewById(R.id.linearLayout1RSS); //new LinearLayout(this);
    		LLG.removeAllViewsInLayout(); //počistim
    		
    		iv = new ImageView[velikostNovic];//črte med texView-i
    		
            for (int i = 0; i < SeznamNovic.size(); i++)//od 1 naprej ker je prvi naslov UM-FERI - Novice... 
            {
            	Novica nov = (Novica) SeznamNovic.get(i);

                poljeTxtViev[i] = new TextView(RssActivity.this);
                poljeTxtViev[i].setText(nov.getNaslov());
                poljeTxtViev[i].setHeight(50);
                poljeTxtViev[i].setGravity(Gravity.CENTER_VERTICAL);
                poljeTxtViev[i].setId(i);

                iv[i] = new ImageView(RssActivity.this);//črte med texView-i
	            iv[i].setImageResource(R.drawable.crta);//črte med texView-i
	            LLG.addView(iv[i]);//črte med texView-i
                
                LLG.addView(poljeTxtViev[i]);
                
                
                poljeTxtViev[i].setOnClickListener(new View.OnClickListener() 
                {
    				@Override
    				public void onClick(View v) 
    				{	
    					//odprem novico v brskalniku
    					Novica nov2 = (Novica) SeznamNovic.get(v.getId());
    					Intent brskalnik2 = new Intent(Intent.ACTION_VIEW, Uri.parse(nov2.getUrl()));
    					startActivity(brskalnik2);
    					
    				}
    			});

            }

        }
        else
        {
        	txtViev1.setText("Ni novic, Osveži.");
        }


        
    }
    
    public String preberiizdat(String file) throws Exception //http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml
	{
		String izhod="";
		try
		{
			  FileInputStream fstream = new FileInputStream(file);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   
			  {
				  izhod+=strLine+"\n";
				  //dodajam novice v seznam
				  //Log.e(velikostNovic+" ", strLine.substring(strLine.indexOf("<|>")+3, strLine.length()));
				  SeznamNovic.add(new Novica(strLine.substring(0, strLine.indexOf("<|>")), strLine.substring(strLine.indexOf("<|>")+3, strLine.length())));
				  velikostNovic++;
			  }

			  in.close();
		}catch (Exception e)
		{
			  throw(e);
		}

		return izhod;
	}
    
    public void shraniNoviceVDat()
	{
		String textNovic="";
		
		for (int i=0; i<SeznamNovic.size(); i++)
		{
			Novica n = (Novica) SeznamNovic.get(i);
			textNovic+= n.getNaslov()+"<|>"+n.getUrl()+"\n";
		}
		
		shranivdat(textNovic, "Novice.txt");
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.buttonOsveziRSS) 
		{
			OsveziNovice on = new OsveziNovice();
			on.execute();
		}

	}
	

	private class OsveziNovice extends AsyncTask<Void, Void, String>  //prrametri, progres, rezultati
	{
		@Override
		protected void onPreExecute() 
		{
			dialogWait = ProgressDialog.show(RssActivity.this, "Osveževanje", "Prosim počakajte...", true);
		}
		
		
		protected String doInBackground(Void... voids) 
		{
			//snamem podatke iz interneta
			try 
			{
				novice = PoisciNaNetu("http://www.feri.uni-mb.si/rss/novice.xml", "//title");
				linki = PoisciNaNetu("http://www.feri.uni-mb.si/rss/novice.xml", "//link");
			} catch (Exception e) {
				return "NOK";
			}
			
			
	        return "OK";
		}

		protected void onPostExecute(String rezultat) 
		{

			//ko potegnem novice iz interneta jih izpišem in na njih naredim evente
			if(rezultat=="OK")
			{
				poljeTxtViev = new TextView[novice.getLength()];
				LLG = (LinearLayout) findViewById(R.id.linearLayout1RSS); //new LinearLayout(this);
				LLG.removeAllViewsInLayout(); //počistim
				
		        iv = new ImageView[novice.getLength()];//črte med texView-i
		        
				 
				SeznamNovic.clear();
				
		        for (int i = 1; i < novice.getLength(); i++)//od 1 naprej ker je prvi naslov UM-FERI - Novice... 
		        {
		            Node node = novice.item(i);
		            Node link = linki.item(i);
	
		            poljeTxtViev[i-1] = new TextView(RssActivity.this);
		            poljeTxtViev[i-1].setText(node.getTextContent());
		            poljeTxtViev[i-1].setHeight(50);
		            poljeTxtViev[i-1].setGravity(Gravity.CENTER_VERTICAL);
		            poljeTxtViev[i-1].setId(i);
		            
		            SeznamNovic.add(new Novica(node.getTextContent(), link.getTextContent())); //hranim novice v seznam
	
		            iv[i-1] = new ImageView(RssActivity.this);//črte med texView-i
		            iv[i-1].setImageResource(R.drawable.crta);//črte med texView-i
		            LLG.addView(iv[i-1]);//črte med texView-i
		           
		            LLG.addView(poljeTxtViev[i-1]);
		            
		            poljeTxtViev[i-1].setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) 
						{	
							//odprem novico v brskalniku
							Intent brskalnik = new Intent(Intent.ACTION_VIEW, Uri.parse(linki.item(v.getId()).getTextContent()));
							startActivity(brskalnik);
							
						}
					});
	
		        }
		        txtViev1.setText("UM FERI - Novice na oglasni deski ");
		        
		        
		    	txtViewNazadnjeOsvezeno.setText(" ");
		    	//SHRANIM DATUM
		    	SharedPreferences nazadnje_osvezeno = getSharedPreferences("NOVICE_OSVEZITEV", 0);
		        SharedPreferences.Editor editor = nazadnje_osvezeno.edit();
		        editor.putString("nazadnje_osvezene_novice", trenutniCasString());
		        editor.commit();
		        
		        shraniNoviceVDat();
		        
		        Toast.makeText(RssActivity.this,"Končano",Toast.LENGTH_SHORT).show();
			}
			else 	
			{
				txtViev1.setText("Napaka! Potrebujete dostop do interneta!");
				Toast.makeText(RssActivity.this,"Ni povezave!",Toast.LENGTH_LONG).show();
			}
	        
	        //ko smo z delom zaključili še prekinemo dialog
			dialogWait.cancel();
	        
		}

	}
	
	public String trenutniCasString()
	{
		Time now = new Time();
		now.setToNow();
    	Calendar a= Calendar.getInstance();
    	String minuta = now.minute+""; 
    	if(minuta.length()<2) minuta="0"+minuta;//vodilna ničla
    	
    	return (a.getTime().getDate()+"."+((int)(a.getTime().getMonth())+1)+" "+now.hour+":"+minuta);
	}
	
	
	public void shranivdat(String vsebina, String path) //http://www.roseindia.net/java/beginners/java-write-to-file.shtml
	 {
		try
		{
			File pot = new File("/mnt/sdcard/iManager/");
			pot.mkdirs();  
			
			FileWriter fstream = new FileWriter(pot.getPath()+"/"+path);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(vsebina);
			out.close();
		}catch (Exception e)
		{
			System.err.println("Napaka: " + e.getMessage());
		}
	 }
   
	
	public NodeList PoisciNaNetu(String url, String xPathIzraz) throws Exception
	{
	   NodeList nodes = null;
       try
       {
	        InputSource inputSource = new InputSource(url);
	        XPath xpath = XPathFactory.newInstance().newXPath();
	       
	        try
	        {
	            nodes = (NodeList) xpath.evaluate(xPathIzraz, inputSource, XPathConstants.NODESET);
	        }catch (Exception e) 
	        {
	        	//txtViev1.setText("Napaka v xPath iskanju\n"+e.toString());
	        	throw(e);
			}

       }catch (Exception e) 
       {
    	   throw(e);
       		//txtViev1.setText("Napaka: Potrebujete dostop do interneta!");
       }
       return nodes;
	}
	
	//meni
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0,0,0,"Nastavitve").setIcon(R.drawable.nastavitve2);
    	menu.add(0,1,1,"Izhod").setIcon(R.drawable.izhod2);

    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    	case 0: //nastavitve

    		Intent nastavitve = new Intent(RssActivity.this, NastavitveActivity.class);
    		startActivity(nastavitve);

    	return true;
    	case 1: //izhod
    		finish();
    	return true;
    	}
    	
    	return false;
    }
	
	
//	PUBLIC VOID OSVEZINOVICE()
//	{
//		NODELIST NOVICE = POISCINANETU("HTTP://WWW.FERI.UNI-MB.SI/RSS/NOVICE.XML", "//TITLE");
//		LINKI = POISCINANETU("HTTP://WWW.FERI.UNI-MB.SI/RSS/NOVICE.XML", "//LINK");
//		
//		
//		
//		POLJETXTVIEV = NEW TEXTVIEW[NOVICE.GETLENGTH()];
//		LLG = (LINEARLAYOUT) FINDVIEWBYID(R.ID.LINEARLAYOUT1RSS); //NEW LINEARLAYOUT(THIS);
//        //LLG.SETORIENTATION(LINEARLAYOUT.VERTICAL);
//		
//        FOR (INT I = 1; I < NOVICE.GETLENGTH(); I++)//OD 1 NAPREJ KER JE PRVI NASLOV UM-FERI - NOVICE... 
//        {
//            NODE NODE = NOVICE.ITEM(I);
//
//            POLJETXTVIEV[I-1] = NEW TEXTVIEW(THIS);
//            POLJETXTVIEV[I-1].SETTEXT(NODE.GETTEXTCONTENT());
//            POLJETXTVIEV[I-1].SETHEIGHT(40);
//            POLJETXTVIEV[I-1].SETID(I);
//
//            LLG.ADDVIEW(POLJETXTVIEV[I-1]);
//            
//            X=I;
//            POLJETXTVIEV[I-1].SETONCLICKLISTENER(NEW VIEW.ONCLICKLISTENER() {
//				
//				@OVERRIDE
//				PUBLIC VOID ONCLICK(VIEW V) 
//				{	
//					//ODPREM NOVICO V BRSKALNIKU
//					INTENT BRSKALNIK = NEW INTENT(INTENT.ACTION_VIEW, URI.PARSE(LINKI.ITEM(V.GETID()).GETTEXTCONTENT()));
//					STARTACTIVITY(BRSKALNIK);
//					
//				}
//			});
// 
//        }
// 
//	}

}
