package com.beans.attijariRh;


import java.util.Date;


public class Enfant {
	private String idEnfant;
	private String nom;
	private String prenom;
	private String ncmim;
	private Date datenaissance;
	
	
	
	public String getIdEnfant()
	{
		return idEnfant;
	}
	public void setIdEnfant(String idEnfant)
	{
		this.idEnfant = idEnfant;
	}
	public String getNom()
	{
		return nom;
	}
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	public String getPrenom()
	{
		return prenom;
	}
	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}
	public Date getDatenaissance()
	{
		return datenaissance;
	}
	public void setDatenaissance(Date datenaissance)
	{
		this.datenaissance = datenaissance;
	}
	
	public String getNcmim()
	{
		return ncmim;
	}
	public void setNcmim(String ncmim)
	{
		this.ncmim = ncmim;
	}
	
}