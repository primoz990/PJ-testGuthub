package edu.PrimozRezek.iManager.android;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;

import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.PrimozRezek.iManager.android.RssActivity.Novica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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
	TextView textVievOsvezitev;
	Button gumbOsvezi;
	  
	
	ImageView imageView1;
	ImageView imageView2;
	ImageView imageView3;
	ImageView imageView4;
	
	ProgressDialog dialogWait;
	

	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.vreme);
	        
	        g = new Gumbi( this, id);
	        mainLL = (LinearLayout) findViewById(R.id.mainLL);
	        mainLL.addView(g,0);

	        
	        textViev1 = (TextView) findViewById(R.id.textViewTren1);  
	        textViev2 = (TextView) findViewById(R.id.textViewNapoved2);
	        textViev3 = (TextView) findViewById(R.id.textViewDan3);
	        textViev4 = (TextView) findViewById(R.id.textViewDan4);
	        textViev5 = (TextView) findViewById(R.id.textViewDan5);
	        
	        gumbOsvezi = (Button) findViewById(R.id.button1);
	        imageView1 = (ImageView) findViewById(R.id.imageView1);
	        imageView2 = (ImageView) findViewById(R.id.imageView2);
	        imageView3 = (ImageView) findViewById(R.id.imageView3);
	        imageView4 = (ImageView) findViewById(R.id.imageView4);
	        gumbOsvezi.setOnClickListener(this);
	        
	        textVievOsvezitev = (TextView) findViewById(R.id.textViewNazadnjeOsvezenoVreme);
	        
	        
	        preberiPodatkeIzKartice();
	       
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
	  			  while ((strLine = br.readLine()) != null)   izhod+=strLine+"\n";
	  			  in.close();
	  		}catch (Exception e)
	  		{
	  			  throw(e);
	  		}
	
	  		return izhod;
  	
		}
			

	    public void preberiPodatkeIzKartice() //http://www.roseindia.net/java/beginners/java-read-file-line-by-line.shtml
	    {
	    	//nazadnje osveženo:
	    	SharedPreferences nazadnje_osvezeno = getSharedPreferences("VREME_OSVEZITEV", 0);
	        textVievOsvezitev.setText("Nazadnje osveženo:\n"+nazadnje_osvezeno.getString("nazadnje_osvezeno_vreme", " "));   
	        
	    	
	    		try 
	    		{
					textViev1.setText(preberiizdat("/mnt/sdcard/iManager/Vreme/trenutnaNapoved.txt"));
					textViev2.setText(preberiizdat("/mnt/sdcard/iManager/Vreme/txtNapoved.txt"));
					textViev3.setText(preberiizdat("/mnt/sdcard/iManager/Vreme/prviDan.txt"));
					textViev4.setText(preberiizdat("/mnt/sdcard/iManager/Vreme/drugiDan.txt"));
					textViev5.setText(preberiizdat("/mnt/sdcard/iManager/Vreme/tretjiDan.txt"));
					
					Bitmap slikaTrenutna= BitmapFactory.decodeFile("/mnt/sdcard/iManager/Vreme/trenutnaSlika.PNG");
					imageView1.setImageBitmap(slikaTrenutna);
					Bitmap slikica1= BitmapFactory.decodeFile("/mnt/sdcard/iManager/Vreme/slikica1.PNG");
					imageView2.setImageBitmap(slikica1);
					Bitmap slikica2= BitmapFactory.decodeFile("/mnt/sdcard/iManager/Vreme/slikica2.PNG");
					imageView3.setImageBitmap(slikica2);
					Bitmap slikica3= BitmapFactory.decodeFile("/mnt/sdcard/iManager/Vreme/slikica3.PNG");
					imageView4.setImageBitmap(slikica3);
				} catch (Exception e) 
				{
					textViev1.setText("Ni podatkov, osveži.");
				}
	    	
	    }
	    
	    
	    
	    public void shranivdat(String vsebina, String path) //http://www.roseindia.net/java/beginners/java-write-to-file.shtml
		 {
			try
			{
				File pot = new File("/mnt/sdcard/iManager/Vreme/");
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
	    
	    public void shraniBmp(String file, Bitmap image) //http://stackoverflow.com/questions/649154/android-bitmap-save-to-location
	    {
	    	try 
	    	{
	    		File pot = new File("/mnt/sdcard/iManager/Vreme/");
				pot.mkdirs();
				
	    	    FileOutputStream out = new FileOutputStream(pot.getPath()+"/"+file);
	    	    image.compress(Bitmap.CompressFormat.PNG, 90, out);
	    	} catch (Exception e) 
	    	{
	    	       e.printStackTrace();
	    	}
	    }
	    

	    
	    @Override
		public void onResume() 
		{
	    	super.onResume();

		}

		
		@Override
		public void onClick(View v) 
		{
			OsveziVreme ov  = new OsveziVreme();
			ov.execute();
//			osveziVreme();
		}


		 
		private class OsveziVreme extends AsyncTask<Void, Void, String>  //prrametri, progres, rezultati
		{
			@Override
			protected void onPreExecute() 
			{
				dialogWait = ProgressDialog.show(vremeActivity.this, "", "Osveževanje. Prosim počakajte...", true, false);

			}
			
			Node temperatura;
			Node stanje;
			Node smerVetra;
			Node hitrostVetraKMH;
			TagNode linkTrenutna;
			TagNode napoved;
			NodeList dnevi;
			NodeList situacije;
			NodeList TempMinimalne;
			NodeList TempMaximalne;
			Bitmap trenutnaSlika;
			Bitmap slikica1;
			Bitmap slikica2;
			Bitmap slikica3;
			
			protected String doInBackground(Void... voids) 
			{
				//snamem podatke iz interneta
				try 
				{
					//trenutno stanje //je že xml
					temperatura = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//t", 0);
					stanje = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//nn_shortText", 0);
					smerVetra = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//dd_longText", 0);
					hitrostVetraKMH = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//ff_val_kmh", 0);
		
					//trenutna slika
					linkTrenutna = xmlCleaner("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_si_latest.html");
					TagNode td = findInfo(linkTrenutna, "//td[@class='nn_icon_wwsyn_icon']/img", 0); //4. slika je edvard rusian-letališče MB
					String linkec1= td.getAttributeByName("src");
					trenutnaSlika = getBitmapFromURL("http://meteo.arso.gov.si"+linkec1);
					
					// textovna NAPOVED
					napoved = xmlCleaner("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_d1-d2_text.html");

					// Napoved 3 dnevna
					dnevi = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//valid_day");
					situacije = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//nn_shortText");
					TempMinimalne = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//tn");
					TempMaximalne = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//tx");	
				
					//slikice za 3 dnevno napoved
					TagNode linkiSlik = xmlCleaner("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.html");
					TagNode sl1 = findInfo(linkiSlik, "//img", 1); 
					TagNode sl2 = findInfo(linkiSlik, "//img", 2); 
					TagNode sl3 = findInfo(linkiSlik, "//img", 3); 

					slikica1 = getBitmapFromURL("http://meteo.arso.gov.si"+sl1.getAttributeByName("src"));
					slikica2 = getBitmapFromURL("http://meteo.arso.gov.si"+sl2.getAttributeByName("src"));
					slikica3 = getBitmapFromURL("http://meteo.arso.gov.si"+sl3.getAttributeByName("src"));

					
				} catch (Exception e) 
				{
					return "NOK";
				}
				
				
		        return "OK";
			}
			
			protected void onPostExecute(String rezultat) 
			{

				//ko potegnem vreme iz neta izpišem podatke
				if(rezultat=="OK")
				{
						try{
							//trenutno stanje
							textViev1.setText("Maribor:\n "+stanje.getTextContent()+", "+temperatura.getTextContent()+"°C\n "+smerVetra.getTextContent()+", "+hitrostVetraKMH.getTextContent()+"km/h");
							
							//trenutna slika
							imageView1.setImageBitmap(trenutnaSlika);
			
							// textovna NAPOVED
							String nap="";
							for(int i=0; i>-1; ) //nevem koliko odstavkov je v xml-ju(število lahko variira) zato neskončna zanka ki se prekine ko ne najdemo več odstavkov
							{
								TagNode odstavek=null;
								try
								{
									odstavek= findInfo(napoved, "//p", i);
									i++;
								}catch (Exception e) {
									textViev2.setText("\nNapoved: Napaka v xpath iskanju\n");
								}
								if(odstavek!=null) nap+=" "+odstavek.getText().toString();  
								else i=-5;
							}
							textViev2.setText("\nNapoved:\n"+SparsajSumnike(nap)+" "); 
			
							// Napoved 3 dnevna
							//indexi od 1 naprej ker prvega(danes) izpustimo //pri dnevih pa izločimo CET na koncu niza
							textViev3.setText(dnevi.item(1).getTextContent().substring(0, dnevi.item(1).getTextContent().indexOf(" "))+"\n"+situacije.item(1).getTextContent()+"\n"+TempMinimalne.item(1).getTextContent()+" | "+TempMaximalne.item(1).getTextContent()+"°C");
							textViev4.setText(dnevi.item(2).getTextContent().substring(0, dnevi.item(2).getTextContent().indexOf(" "))+"\n"+situacije.item(2).getTextContent()+"\n"+TempMinimalne.item(2).getTextContent()+" | "+TempMaximalne.item(2).getTextContent()+"°C");
							textViev5.setText(dnevi.item(3).getTextContent().substring(0, dnevi.item(3).getTextContent().indexOf(" "))+"\n"+situacije.item(3).getTextContent()+"\n"+TempMinimalne.item(3).getTextContent()+" | "+TempMaximalne.item(3).getTextContent()+"°C");		
							
							//slikice za 3 dnevno napoved
							imageView2.setImageBitmap(slikica1);
							imageView3.setImageBitmap(slikica2);
							imageView4.setImageBitmap(slikica3);
							
							//shranim slike na SD kartico
					    	shraniBmp("trenutnaSlika.PNG", trenutnaSlika);
					    	shraniBmp("slikica1.PNG", slikica1);
					    	shraniBmp("slikica2.PNG", slikica2);
					    	shraniBmp("slikica3.PNG", slikica3);
					    	//shranim textovne podatke
					    	shranivdat(textViev1.getText().toString(), "trenutnaNapoved.txt");
					    	shranivdat(textViev3.getText().toString(), "prviDan.txt");
					    	shranivdat(textViev4.getText().toString(), "drugiDan.txt");
					    	shranivdat(textViev5.getText().toString(), "tretjiDan.txt");
					    	shranivdat(textViev2.getText().toString(), "txtNapoved.txt"); 
					
					    	
					    	textVievOsvezitev.setText(" ");

					    	//shranim datum
					    	SharedPreferences nazadnje_osvezeno = getSharedPreferences("VREME_OSVEZITEV", 0);
					        SharedPreferences.Editor editor = nazadnje_osvezeno.edit();
					        editor.putString("nazadnje_osvezeno_vreme", trenutniCasString());
					        editor.commit();
							
			
						} catch (Exception e) 
						{
							textViev1.setText("Napaka! vOK");
						}
					
			        Toast.makeText(vremeActivity.this,"Končano",Toast.LENGTH_SHORT).show();
				}
				else if(rezultat=="NOK")	
				{
					textViev1.setText("Napaka! Potrebujete dostop do interneta!");
					Toast.makeText(vremeActivity.this,"Ni povezave!",Toast.LENGTH_LONG).show();
				}
				else Toast.makeText(vremeActivity.this,"Prišlo je do napake",Toast.LENGTH_LONG).show();
		        
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
	    	
	    	return (a.getTime().getDate()+"/"+((int)(a.getTime().getMonth())+1)+"  "+now.hour+":"+minuta);
		}
		
		
//		public void osveziVreme()
//		{
//			try{
//				//trenutno stanje //je že xml
//				Node temperatura = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//t", 0);
//				Node stanje = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//nn_shortText", 0);
//				Node smerVetra = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//dd_longText", 0);
//				Node hitrostVetraKMH = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml", "//ff_val_kmh", 0);
//	
//				textViev1.setText("Maribor:\n "+stanje.getTextContent()+", "+temperatura.getTextContent()+"°C\n "+smerVetra.getTextContent()+", "+hitrostVetraKMH.getTextContent()+"km/h");
//				
//				//trenutna slika
//				TagNode linkTrenutna = xmlCleaner("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observation_si_latest.html");
//				TagNode td = findInfo(linkTrenutna, "//td[@class='nn_icon_wwsyn_icon']/img", 3); //4. slika je edvard rusian-letališče MB
//				String linkec1= td.getAttributeByName("src");
//			
//				imageView1.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+linkec1));
//
//				// textovna NAPOVED
//				TagNode napoved = xmlCleaner("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_d1-d2_text.html");
//				String nap="";
//				for(int i=0; i>-1; ) //nevem koliko odstavkov je v xml-ju(število lahko variira) zato neskončna zanka ki se prekine ko ne najdemo več odstavkov
//				{
//					TagNode odstavek=null;
//					try
//					{
//						odstavek= findInfo(napoved, "//p", i);
//						i++;
//					}catch (Exception e) {
//						textViev2.setText("\nNapoved: Napaka v xpath iskanju\n");
//					}
//					if(odstavek!=null) nap+=" "+odstavek.getText().toString();  
//					else i=-5;
//				}
//				textViev2.setText("\nNapoved:\n"+SparsajSumnike(nap)+" "); 
//
//				// Napoved 3 dnevna
//				NodeList dnevi = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//valid_day");
//				NodeList situacije = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//nn_shortText");
//				NodeList TempMinimalne = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//tn");
//				NodeList TempMaximalne = PoisciNaNetu("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.xml", "//tx");	
//								//indexi od 1 naprej ker prvega(danes) izpustimo //pri dnevih pa izločimo CET na koncu niza
//				textViev3.setText(dnevi.item(1).getTextContent().substring(0, dnevi.item(1).getTextContent().indexOf(" "))+"\n"+situacije.item(1).getTextContent()+"\n"+TempMinimalne.item(1).getTextContent()+" | "+TempMaximalne.item(1).getTextContent()+"°C");
//				textViev4.setText(dnevi.item(2).getTextContent().substring(0, dnevi.item(2).getTextContent().indexOf(" "))+"\n"+situacije.item(2).getTextContent()+"\n"+TempMinimalne.item(2).getTextContent()+" | "+TempMaximalne.item(2).getTextContent()+"°C");
//				textViev5.setText(dnevi.item(3).getTextContent().substring(0, dnevi.item(3).getTextContent().indexOf(" "))+"\n"+situacije.item(3).getTextContent()+"\n"+TempMinimalne.item(3).getTextContent()+" | "+TempMaximalne.item(3).getTextContent()+"°C");
//					
//				//slikice za 3dnevno napoved
//				
//				
//				TagNode linkiSlik = xmlCleaner("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_latest.html");
//				TagNode sl1 = findInfo(linkiSlik, "//img", 1); 
//				TagNode sl2 = findInfo(linkiSlik, "//img", 2); 
//				TagNode sl3 = findInfo(linkiSlik, "//img", 3); 
//				
//				imageView2.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+sl1.getAttributeByName("src")));
//				imageView3.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+sl2.getAttributeByName("src")));
//				imageView4.setImageBitmap(getBitmapFromURL("http://meteo.arso.gov.si"+sl3.getAttributeByName("src")));
//				
//
//			} catch (Exception e) 
//			{
//				textViev1.setText("Napaka! Potrebujete dostop do interneta!");
//			}
//		}
		
		public TagNode xmlCleaner(String url) throws Exception  //html->xml
		{
			CleanerProperties props = new CleanerProperties();
			props.setTranslateSpecialEntities(true);
			props.setTransResCharsToNCR(true);
			props.setOmitComments(true);
			TagNode tagNode;
			try 
			{
				tagNode = new HtmlCleaner(props).clean(new URL(url));
				return tagNode;
				
			} catch (MalformedURLException e) 
			{
				e.printStackTrace();
				throw(e);
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				throw(e);
			}
			
		}
		
		public TagNode findInfo(TagNode node, String XPathExpression, int i) //iskanje ciljnega elementa v XML-ju
		{
			TagNode description_node = null;
			try 
			{
				description_node = (TagNode) node.evaluateXPath(XPathExpression)[i]; // i-ti zadetek
				
			} catch (XPatherException e) 
			{
				e.printStackTrace();
			}
			return description_node;
		}
		
		public TagNode[] findInfo(TagNode node, String XPathExpression) //iskanje ciljnega elementa v XML-ju
		{
			TagNode[] description_node=null;
			try 
			{
				description_node = (TagNode[]) node.evaluateXPath(XPathExpression); // i-ti zadetek
				
			} catch (XPatherException e) 
			{
				e.printStackTrace();
			}
			return description_node;
		}
		
		public String SparsajSumnike(String besedilo)
		{
			String nova="";
			char[] vmes=new char[6];
			for (int i=0; i<besedilo.length()-5; i++)
			{
				
				vmes[0]= besedilo.charAt(i);
				vmes[1]= besedilo.charAt(i+1);
				vmes[2]= besedilo.charAt(i+2);
				vmes[3]= besedilo.charAt(i+3);
				vmes[4]= besedilo.charAt(i+4);
				vmes[5]= besedilo.charAt(i+5);
				
				
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
				
			    nova+=besedilo.charAt(i);
				}
			}

			nova+=vmes[1];
			nova+=vmes[2];
			nova+=vmes[3];
			nova+=vmes[4];
			nova+=vmes[5];
			
			return nova;
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
		        }catch (Exception e) {
		        	textViev1.setText("Napaka v xPath iskanju\n"+e.toString());
		        	throw(e);
				}

	        }catch (Exception e) {
	        	textViev1.setText("Napaka: Potrebujete dostop do interneta!");
	        	throw(e);
			}
	        return nodes;
		}
		
		public Node PoisciNaNetu(String url, String xPathIzraz, int i) throws Exception
		{
			Node node = null;
			NodeList nodes = null;
	        try
	        {
		        InputSource inputSource = new InputSource(url);
		        XPath xpath = XPathFactory.newInstance().newXPath();
		       
		        try
		        {
		        nodes = (NodeList) xpath.evaluate(xPathIzraz, inputSource, XPathConstants.NODESET);
		        node = nodes.item(i);
		        }catch (Exception e) {
		        	textViev1.setText("Napaka v xPath iskanju\n"+e.toString());
		        	throw(e);
				}

	        }catch (Exception e) {
	        	textViev1.setText("Napaka: Potrebujete dostop do interneta!");
	        	throw(e);
			}
	        return node;
		}
		
		
		
		public Bitmap getBitmapFromURL(String src) 
		{
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

	    		Intent nastavitve = new Intent(vremeActivity.this, NastavitveActivity.class);
	    		startActivity(nastavitve);

	    	return true;
	    	case 1: //izhod
	    		finish();
	    	return true;
	    	}
	    	
	    	return false;
	    }
			
}

