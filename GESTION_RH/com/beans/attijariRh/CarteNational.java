package com.beans.attijariRh;



import java.util.Date;

public class CarteNational {
	private String CIN;
	private Date dateExperation;
	public Personnel personnel;
	public String getCIN()
	{
		return CIN;
	}
	public void setCIN(String cIN)
	{
		CIN = cIN;
	}
	public Date getDateExperation()
	{
		return dateExperation;
	}
	public void setDateExperation(Date dateExperation)
	{
		this.dateExperation = dateExperation;
	}
	public Personnel getPersonnel()
	{
		return personnel;
	}
	public void setPersonnel(Personnel personnel)
	{
		this.personnel = personnel;
	}
	
}