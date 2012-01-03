package edu.PrimozRezek.iManager.android.Koledar;

import java.util.List;

import edu.PrimozRezek.iManager.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SeznamDogodkov extends Activity 
{
	ListView lv;
	List seznam;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seznam_dogodkov);
        
        lv = (ListView) findViewById(R.id.listView1);
        
        PrenosPodatkov pr = new PrenosPodatkov();
        seznam = pr.getIzbraniDogodki();
        
        String[] dogodki = new String[seznam.size()+1];
        dogodki[0] = "+ Nov Dogodek";
        for(int i=1; i<=seznam.size();i++)
        {
        	Dogodek k = (Dogodek) seznam.get(i-1);
        	dogodki[i]=k.naslov;
        }
        
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogodki));
        
        
        lv.setOnItemClickListener(new OnItemClickListener() 
        {
        	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		
        		if(id==0)
        		{
        			odpriDodajanjeNovegaDogodka();
        			finish();
        		}
        		else
        		{
        		id--;
        		Dogodek g = (Dogodek)seznam.get(Integer.parseInt(id+""));
        		
        		PrenosPodatkov pk = new PrenosPodatkov();
        		pk.setIzbranDogodekZaUrejanje(g.id);
              
        		odpriUrejanjeDogodka();
        		Toast.makeText(getApplicationContext(), ((TextView) view).getText()+" "+ id+" "+g.id ,Toast.LENGTH_SHORT).show();
        		finish();
        		}
            }


          });
        
    }
	
	public void odpriDodajanjeNovegaDogodka()
	{
		Intent dodajAct = new Intent(this, DodajanjeDogodkaVCalendarActivity.class);
		this.startActivity(dodajAct);
	}
	
	public void odpriUrejanjeDogodka()
	{
		Intent dodajAct = new Intent(this, UrediDogodekActivity.class);
		this.startActivity(dodajAct);
	}

}
