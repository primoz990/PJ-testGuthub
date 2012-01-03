package edu.PrimozRezek.iManager.android.Koledar;

import java.util.Date;
import java.util.List;

public class PrenosPodatkov 
{
	public static Date izbranDatumIzKoledarja;
	public static List<Dogodek> izbraniDogodki;
	public static int izbranDogodekZaUrejanje;
	

	public static Date getIzbranDatumIzKoledarja() 
	{
		return izbranDatumIzKoledarja;
	}

	public static void setIzbranDatumIzKoledarja(Date izbranDatumIzKoledarja) 
	{
		PrenosPodatkov.izbranDatumIzKoledarja = izbranDatumIzKoledarja;
	}

	public static List<Dogodek> getIzbraniDogodki() 
	{
		return izbraniDogodki;
	}

	public static void setIzbraniDogodki(List<Dogodek> izbraniDogodki) 
	{
		PrenosPodatkov.izbraniDogodki = izbraniDogodki;
	}

	public static int getIzbranDogodekZaUrejanje() 
	{
		return izbranDogodekZaUrejanje;
	}

	public static void setIzbranDogodekZaUrejanje(int id) 
	{
		PrenosPodatkov.izbranDogodekZaUrejanje = id;
	}
	
	

	
}
