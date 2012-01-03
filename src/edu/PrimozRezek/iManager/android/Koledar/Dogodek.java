package edu.PrimozRezek.iManager.android.Koledar;

public class Dogodek
{
	public int id;
	public String naslov;
	public String opis;
	public long zacetek;
	public long konec;
	public String lokacija;
	public int celodnevni; 	//samo 0 ali 1
	public int alarm; 			//samo 0 ali 1
	public int opozorilo;
	
	public Dogodek() 
	{
		
	}
	
	public Dogodek(String naslov, String opis, long zacetek, long konec, String lokacija, int celodnevni, int alarm, int opozorilo) 
	{
		this.naslov = naslov;
		this.opis = opis;
		this.zacetek = zacetek;
		this.konec = konec;
		this.lokacija = lokacija;
		this.celodnevni = celodnevni;
		this.alarm = alarm;
		this.opozorilo = opozorilo;
	}
	
	public Dogodek(int id, String naslov, long zacetek, long konec) 
	{
		this.id = id;
		this.naslov = naslov;
		this.zacetek = zacetek;
		this.konec = konec;
	
	}

	
	
}
