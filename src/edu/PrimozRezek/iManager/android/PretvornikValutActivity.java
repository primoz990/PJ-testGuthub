package edu.PrimozRezek.iManager.android;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Node;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class PretvornikValutActivity extends Activity implements OnClickListener 
{
	public static final int id=5;
	//gumbi za preklaplanje med activity
	Gumbi g;
	LinearLayout mainLL;
	
	Button bValuta1, bValuta2;
	EditText etValuta1, etValuta2;
	Spinner sValuta1, sValuta2;
	TextView txtView1;
	
	ProgressDialog dialogWait;
	
	public final String SOAP_ACTION = "http://www.webserviceX.NET/ConversionRate";
	public final String METHOD_NAME = "ConversionRate";
	public final String NAMESPACE = "http://www.webserviceX.NET/";
	public final String URL = "http://www.webservicex.net/CurrencyConvertor.asmx";
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.pretvornik_valut);
        
        g = new Gumbi( this, id);
        mainLL = (LinearLayout) findViewById(R.id.mainLL);
        mainLL.addView(g,0);

        etValuta1 = (EditText) findViewById(R.id.editTextValuta1);
        etValuta2 = (EditText) findViewById(R.id.editTextValuta2);
        
        sValuta1 = (Spinner) findViewById(R.id.spinner1);
        sValuta2 = (Spinner) findViewById(R.id.spinner2);
        
        txtView1 = (TextView) findViewById(R.id.textViewPr);
    }
	
	
	private double KliciWebServis(String izValute, String vValuto) 
	{
		SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
		Request.addProperty("FromCurrency", izValute);
		Request.addProperty("ToCurrency", vValuto);
		
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.dotNet = true;
		soapEnvelope.setOutputSoapObject(Request);
		
		HttpTransportSE aht = new HttpTransportSE(URL);
		
		try
		{
			aht.call(SOAP_ACTION, soapEnvelope);
			SoapPrimitive result = (SoapPrimitive)soapEnvelope.getResponse();
			return Double.parseDouble(result.toString());
		}
		catch(Exception e)
		{
		  txtView1.setText("Potrebujete dostop do interneta!!!\nnapaka: "+e.toString());
		}
		
		return -888;
	}
	
	

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
		case R.id.view1:
			
			String valuta1 = (String)sValuta1.getSelectedItem();
			String valuta2 = (String)sValuta2.getSelectedItem();

			String v1 = valuta1.substring(0, 3);
			String v2 = valuta2.substring(0, 3);
			
			if(etValuta1.getText().toString().length() == 0 && etValuta2.getText().toString().length() > 0)
			{

				PretvoriValute pv1 = new PretvoriValute();
				String nacin = "1";
				pv1.execute(v2, v1, nacin);
				
			}
			else if (etValuta2.getText().toString().length() == 0 && etValuta1.getText().toString().length() > 0)
			{
				
				PretvoriValute pv2 = new PretvoriValute();
				String nacin = "2";
				pv2.execute(v1, v2, nacin);

			}
			else txtView1.setText("vnesite (samo) eno valuto!");
			
		break;
		case R.id.editTextValuta1:
			if (etValuta1.getTextSize()>0) 
				{
					etValuta1.setText("");
					txtView1.setText("");
				}
		break;
		case R.id.editTextValuta2:
			if (etValuta2.getTextSize()>0) 
				{
					etValuta2.setText("");
					txtView1.setText("");
				}
		break;
		
		}

		
	}
	
	
	
	private class PretvoriValute extends AsyncTask<String, Void, String>  //prrametri, progres, rezultati
	{
		@Override
		protected void onPreExecute() 
		{
			dialogWait = ProgressDialog.show(PretvornikValutActivity.this, "Osveževanje", "Prosim počakajte...", true);
		}
		
		double faktor;
		String valu1, valu2, nacinn;
		
		protected String doInBackground(String...parametri) 
		{
			//snamem podatke iz interneta
			try 
			{

				valu1 = parametri[0];
				valu2 = parametri[1];
				nacinn = parametri[2];
				faktor = KliciWebServis(valu1, valu2);		
				
			} catch (Exception e) {
				return "NOK";
			}
			
			
	        return "OK"; //OK in vrsta izpisa 1 ali 2
		}
		
		protected void onPostExecute(String rezultat) 
		{
			//ko potegnem iz interneta faktor pretvorbe, izračunam in izpišem drugo valuto
			if(rezultat=="OK" && nacinn=="1")
			{
				if(faktor!=-888)
				{
					double stevilo = Double.parseDouble(etValuta2.getText().toString());
					txtView1.setText(valu1+" -> "+valu2+"\n"+faktor);
					etValuta1.setText(""+(double)Math.round((stevilo * faktor) * 1000) / 1000); //zaokrožim na 3 decimalke
				}else txtView1.setText("Potrebujete dostop do interneta!!!");
			}
			else if(rezultat=="OK" && nacinn=="2")
			{
				if(faktor!=-888)
				{
					double stevilo = Double.parseDouble(etValuta1.getText().toString());
					txtView1.setText(valu1+" -> "+valu2+"\n"+faktor);
					etValuta2.setText(""+(double)Math.round((stevilo * faktor) * 1000) / 1000); //zaokrožim na 3 decimalke
					
				}else txtView1.setText("Potrebujete dostop do interneta!!!");
			}
			else 	
			{
				txtView1.setText("Napaka! Potrebujete dostop do interneta!");
				Toast.makeText(PretvornikValutActivity.this,"Ni povezave!",Toast.LENGTH_LONG).show();
			}
	        
	        //ko smo z delom zaključili še prekinemo dialog
			dialogWait.cancel();
	        
		}

	}
	
	
	

	
	@Override
	public void onStart()
	{
		super.onStart();
		
		String valute[] = {
				"ALL-Albanian Lek",
				"DZD-Algerian Dinar",
				"ARS-Argentine Peso",
				"AWG-Aruba Florin",
				"AUD-Australian Dollar",
				"BSD-Bahamian Dollar",
				"BHD-Bahraini Dinar",
				"BDT-Bangladesh Taka",
				"BBD-Barbados Dollar",
				"BZD-Belize Dollar",
				"BMD-Bermuda Dollar",
				"BTN-Bhutan Ngultrum",
				"BOB-Bolivian Boliviano",
				"BWP-Botswana Pula",
				"BRL-Brazilian Real",
				"GBP-British Pound",
				"BND-Brunei Dollar",
				"BIF-Burundi Franc",
				"XOF-CFA Franc (BCEAO)",
				"XAF-CFA Franc (BEAC)",
				"KHR-Cambodia Riel",
				"CAD-Canadian Dollar",
				"CVE-Cape Verde Escudo",
				"KYD-Cayman Islands Dollar",
				"CLP-Chilean Peso",
				"CNY-Chinese Yuan",
				"COP-Colombian Peso",
				"KMF-Comoros Franc",
				"CRC-Costa Rica Colon",
				"HRK-Croatian Kuna",
				"CUP-Cuban Peso",
				"CYP-Cyprus Pound",
				"CZK-Czech Koruna",
				"DKK-Danish Krone",
				"DJF-Dijibouti Franc",
				"DOP-Dominican Peso",
				"XCD-East Caribbean Dollar",
				"EGP-Egyptian Pound",
				"SVC-El Salvador Colon",
				"EEK-Estonian Kroon",
				"ETB-Ethiopian Birr",
				"EUR-Euro",
				"FKP-Falkland Islands Pound",
				"GMD-Gambian Dalasi",
				"GHC-Ghanian Cedi",
				"GIP-Gibraltar Pound",
				"XAU-Gold Ounces",
				"GTQ-Guatemala Quetzal",
				"GNF-Guinea Franc",
				"GYD-Guyana Dollar",
				"HTG-Haiti Gourde",
				"HNL-Honduras Lempira",
				"HKD-Hong Kong Dollar",
				"HUF-Hungarian Forint",
				"ISK-Iceland Krona",
				"INR-Indian Rupee",
				"IDR-Indonesian Rupiah",
				"IQD-Iraqi Dinar",
				"ILS-Israeli Shekel",
				"JMD-Jamaican Dollar",
				"JPY-Japanese Yen",
				"JOD-Jordanian Dinar",
				"KZT-Kazakhstan Tenge",
				"KES-Kenyan Shilling",
				"KRW-Korean Won",
				"KWD-Kuwaiti Dinar",
				"LAK-Lao Kip",
				"LVL-Latvian Lat",
				"LBP-Lebanese Pound",
				"LSL-Lesotho Loti",
				"LRD-Liberian Dollar",
				"LYD-Libyan Dinar",
				"LTL-Lithuanian Lita",
				"MOP-Macau Pataca",
				"MKD-Macedonian Denar",
				"MGF-Malagasy Franc",
				"MWK-Malawi Kwacha",
				"MYR-Malaysian Ringgit",
				"MVR-Maldives Rufiyaa",
				"MTL-Maltese Lira",
				"MRO-Mauritania Ougulya",
				"MUR-Mauritius Rupee",
				"MXN-Mexican Peso",
				"MDL-Moldovan Leu",
				"MNT-Mongolian Tugrik",
				"MAD-Moroccan Dirham",
				"MZM-Mozambique Metical",
				"MMK-Myanmar Kyat",
				"NAD-Namibian Dollar",
				"NPR-Nepalese Rupee",
				"ANG-Neth Antilles Guilder",
				"NZD-New Zealand Dollar",
				"NIO-Nicaragua Cordoba",
				"NGN-Nigerian Naira",
				"KPW-North Korean Won",
				"NOK-Norwegian Krone",
				"OMR-Omani Rial",
				"XPF-Pacific Franc",
				"PKR-Pakistani Rupee",
				"XPD-Palladium Ounces",
				"PAB-Panama Balboa",
				"PGK-Papua New Guinea Kina",
				"PYG-Paraguayan Guarani",
				"PEN-Peruvian Nuevo Sol",
				"PHP-Philippine Peso",
				"XPT-Platinum Ounces",
				"PLN-Polish Zloty",
				"QAR-Qatar Rial",
				"ROL-Romanian Leu",
				"RUB-Russian Rouble",
				"WST-Samoa Tala",
				"STD-Sao Tome Dobra",
				"SAR-Saudi Arabian Riyal",
				"SCR-Seychelles Rupee",
				"SLL-Sierra Leone Leone",
				"XAG-Silver Ounces",
				"SGD-Singapore Dollar",
				"SKK-Slovak Koruna",
				"SIT-Slovenian Tolar",
				"SBD-Solomon Islands Dollar",
				"SOS-Somali Shilling",
				"ZAR-South African Rand",
				"LKR-Sri Lanka Rupee",
				"SHP-St Helena Pound",
				"SDD-Sudanese Dinar",
				"SRG-Surinam Guilder",
				"SZL-Swaziland Lilageni",
				"SEK-Swedish Krona",
				"TRY-Turkey Lira",
				"CHF-Swiss Franc",
				"SYP-Syrian Pound",
				"TWD-Taiwan Dollar",
				"TZS-Tanzanian Shilling",
				"THB-Thai Baht",
				"TOP-Tonga Pa'anga",
				"TTD-Trinidad&amp;Tobago Dollar",
				"TND-Tunisian Dinar",
				"TRL-Turkish Lira",
				"USD-U.S. Dollar",
				"AED-UAE Dirham",
				"UGX-Ugandan Shilling",
				"UAH-Ukraine Hryvnia",
				"UYU-Uruguayan New Peso",
				"VUV-Vanuatu Vatu",
				"VEB-Venezuelan Bolivar",
				"VND-Vietnam Dong",
				"YER-Yemen Riyal",
				"YUM-Yugoslav Dinar",
				"ZMK-Zambian Kwacha",
				"ZWD-Zimbabwe Dollar"};
		
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, valute);
		adapter.setDropDownViewResource(R.layout.spiner_item);
		sValuta1.setAdapter(adapter);
		sValuta2.setAdapter(adapter);
	}
	


}


