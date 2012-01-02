package edu.PrimozRezek.iManager.android;

import java.util.Date;

public class PrenosPodatkov 
{
	public static Date izbranDatumIzKoledarja;
	

	public static Date getIzbranDatumIzKoledarja() 
	{
		return izbranDatumIzKoledarja;
	}

	public static void setIzbranDatumIzKoledarja(Date izbranDatumIzKoledarja) 
	{
		PrenosPodatkov.izbranDatumIzKoledarja = izbranDatumIzKoledarja;
	}

	
}
