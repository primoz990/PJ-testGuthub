package edu.PrimozRezek.iManager.android.Koledar;

public class Koledar
{
	public int id;
	public String ime;
	
	public Koledar(int id, String ime) 
	{
		this.id = id;
		this.ime = ime;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}
}
