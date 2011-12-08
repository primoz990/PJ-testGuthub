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
import java.util.ArrayList;
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
	TextView txtViev1;

	public void shranivdat(String doc) //http://www.roseindia.net/java/beginners/java-write-to-file.shtml
	 {
		try
		{
			FileWriter fstream = new FileWriter("/mnt/sdcard/izhodHTML.xml");
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
        
        txtViev1=(TextView) findViewById(R.id.textViewRSS2);
     

        InputSource inputSource = new InputSource("http://www.feri.uni-mb.si/rss/novice.xml");
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "//title";
        
        
        NodeList nodes = null;
        try
        {
        nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);

        }catch (Exception e) {
        	txtViev1.setText("Napaka v xPath iskanju\n"+e.toString());
		}
        
        for (int i = 1; i < nodes.getLength(); i++)//od 1 naprej ker je prvi naslov UM-FERI - Novice... 
        {
            Node node = nodes.item(i);
            txtViev1.setText(txtViev1.getText()+"\n\n"+node.getTextContent());
        }
        
        
        
	}




	@Override
	public void onClick(View v) 
	{
		
		
		
	}
	

}
