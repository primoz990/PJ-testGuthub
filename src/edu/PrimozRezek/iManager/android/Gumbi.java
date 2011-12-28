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
	Button gumbValute;
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
	        gumbValute = (Button) findViewById(R.id.gumbValute);
	        
	        switch (id) {
			case uraActivity.id:
				gumbValute.setEnabled(true);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(false);
				break;
			case koledarActivity.id:
				gumbValute.setEnabled(true);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(false);
				gumbUra.setEnabled(true);
				break;
			case vremeActivity.id:
				gumbValute.setEnabled(true);
				gumbRss.setEnabled(true);
				gumbVreme.setEnabled(false);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(true);
				break;
			case RssActivity.id:
				gumbValute.setEnabled(true);
				gumbRss.setEnabled(false);
				gumbVreme.setEnabled(true);
				gumbKoledar.setEnabled(true);
				gumbUra.setEnabled(true);
				break;
			case PretvornikValutActivity.id:
				gumbValute.setEnabled(false);
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
	        gumbValute.setOnClickListener(this);
		
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
		case R.id.gumbValute:
			a.finish();
//			Intent koledaAct = new Intent(getContext(), koledarActivity.class);
//			getContext().startActivity(koledaAct);
			Intent valuteAct = new Intent(getContext(), PretvornikValutActivity.class);
			getContext().startActivity(valuteAct);
		break;
			
		
		}
		
		
	}
	

}
