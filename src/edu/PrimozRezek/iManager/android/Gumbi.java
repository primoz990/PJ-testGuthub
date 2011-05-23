package edu.PrimozRezek.iManager.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Gumbi extends LinearLayout implements OnClickListener {
	LayoutInflater mInflater;
	View v;
	Button gumbUra;
	Button gumbVreme;
	Button gumbRss;
	Button gumbKoledar;
	Button gumbNastavitve;
	Activity a;
	int id;
	public Gumbi(Activity a, int id) {
		super(a);
		this.id=id;
		this.a=a;
		this.setOrientation(VERTICAL);
		 mInflater = LayoutInflater.from(a);
		 v = mInflater.inflate(R.layout.gumbi, null);
		 this.addView(v);
	        gumbUra = (Button) findViewById(R.id.gumbUra);
	        gumbVreme = (Button) findViewById(R.id.gumbVreme);
	        gumbRss = (Button) findViewById(R.id.gumbRss);
	        gumbKoledar = (Button) findViewById(R.id.gumbKoledar);
	        gumbNastavitve = (Button) findViewById(R.id.gumbNastavitve);
	        
	        switch (id) {
			case uraActivity.id:
				gumbNastavitve.setEnabled(true);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(false);
				break;
			case koledarActivity.id:
				gumbNastavitve.setEnabled(true);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(false);
				gumbUra.setEnabled(true);
				break;
			case vremeActivity.id:
				gumbNastavitve.setEnabled(true);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(false);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(true);
				break;
			case RssActivity.id:
				gumbNastavitve.setEnabled(true);
				gumbRss.setEnabled(false);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(true);
				break;
			case nastavitveActivity.id:
				gumbNastavitve.setEnabled(false);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(true);
				break;
			default:
				break;
			}
	        
	        gumbUra.setOnClickListener(this);
	        gumbVreme.setOnClickListener(this);
	        gumbRss.setOnClickListener(this);
	        gumbKoledar.setOnClickListener(this);
	        gumbNastavitve.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.gumbUra:
			a.finish();
			Intent uraAct = new Intent(getContext(), uraActivity.class);
			getContext().startActivity(uraAct);
		break;
		case R.id.gumbVreme:
			a.finish();
			Intent vremeAct = new Intent(getContext(), vremeActivity.class);
			getContext().startActivity(vremeAct);
        	
		break;
		case R.id.gumbRss:
			a.finish();
			Intent rssAct = new Intent(getContext(), RssActivity.class);
			getContext().startActivity(rssAct);
		break;
		case R.id.gumbKoledar:
			a.finish();
			Intent koledarAct = new Intent(getContext(), koledarActivity.class);
			getContext().startActivity(koledarAct);
		break;
		case R.id.gumbNastavitve:
			a.finish();
			Intent nastavitveAct = new Intent(getContext(), nastavitveActivity.class);
			getContext().startActivity(nastavitveAct);
		break;
			
		
		}
		
		
	}
	

}
