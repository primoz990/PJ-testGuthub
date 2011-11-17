package edu.PrimozRezek.iManager.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

	public void shranivdat(String doc) //http://www.roseindia.net/java/beginners/java-write-to-file.shtml
	 {
		try
		{
			FileWriter fstream = new FileWriter("/data/izhodHTML.html");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(doc);
			out.close();
		}catch (Exception e)
		{
			System.err.println("Napaka: " + e.getMessage());
		}
	 }
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);
        
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);
        
        textViev2=(TextView) findViewById(R.id.textView2);
     
        
        
        
       // String html = getHTML("http://www.feri.uni-mb.si/rss/novice.xml");
       // shranivdat(html);
        TagNode xml = xmlCleaner("http://www.feri.uni-mb.si/rss/novice.xml");
        TagNode Naslov= findInfo(xml, "//title", 0);
        textViev2.setText(Naslov.getText().toString());
		//String novice="";
        
		/*for(int i=0; i<5; i++)
		{
			TagNode naslov = findInfo(xml, "//title", i);
			TagNode link = findInfo(xml, "//link", i);
			TagNode opis = findInfo(xml, "//description", i);
			TagNode avtor = findInfo(xml, "//author", i);
			
			novice += naslov.getText().toString();
			novice += link.getText().toString();
			novice += opis.getText().toString();
			novice += avtor.getText().toString()+" = ";
			novice +="\n----------------------------\n";
		}*/
        
		//TagNode bbb = findInfo(xml, "//*", 0);
		//novice = bbb.getText().toString();
		
		
		
        //textViev2.setText(xml.getText().toString());
		
		
        /*
        
        
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
	    
	    */
	}




	@Override
	public void onClick(View v) 
	{
		
		
		
	}
	public TagNode xmlCleaner2(String fileName)  //html->xml
	{
		CleanerProperties props = new CleanerProperties();
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);
		TagNode tagNode;
		try 
		{
			tagNode = new HtmlCleaner(props).clean(new File(fileName));
			return tagNode;
			
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getHTML(String urlToRead)   //http://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
	{
	      URL url;
	      HttpURLConnection conn;
	     
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line+"\n";
	         }
	         rd.close();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	}
    
	
	public TagNode xmlCleaner(String url)  //html->xml
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
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
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


}
