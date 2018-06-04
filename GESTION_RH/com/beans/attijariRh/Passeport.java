package com.beans.attijariRh;



import java.util.Date;

public class Passeport {
	private String numPassport;
	private Date dateExperation;
	private Date dateDelivrance;
	
	
	public String getNumPassport()
	{
		return numPassport;
	}
	public void setNumPassport(String numPassport)
	{
		this.numPassport = numPassport;
	}
	public Date getDateExperation()
	{
		return dateExperation;
	}
	public void setDateExperation(Date dateExperation)
	{
		this.dateExperation = dateExperation;
	}
	public Date getDateDelivrance()
	{
		return dateDelivrance;
	}
	public void setDateDelivrance(Date dateDelivrance)
	{
		this.dateDelivrance = dateDelivrance;
	}
	
	
}