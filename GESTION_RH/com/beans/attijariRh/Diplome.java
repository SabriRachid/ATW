package com.beans.attijariRh;

import java.util.Date;

public class Diplome {
	private String idDiplome;
	private String specialite;
	private Date dateObtention;
	private String typeDiplome;
	private String institut;
	private String chemin;
	
	
	public Personnel personnel;
	
	public String getIdDiplome()
	{
		return idDiplome;
	}
	public void setIdDiplome(String idDiplome)
	{
		this.idDiplome = idDiplome;
	}
	public String getSpecialite()
	{
		return specialite;
	}
	public void setSpecialite(String specialite)
	{
		this.specialite = specialite;
	}
	public Date getDateObtention()
	{
		return dateObtention;
	}
	public void setDateObtention(Date dateObtention)
	{
		this.dateObtention = dateObtention;
	}
	public String getTypeDiplome()
	{
		return typeDiplome;
	}
	public void setTypeDiplome(String typeDiplome)
	{
		this.typeDiplome = typeDiplome;
	}
	public String getInstitut()
	{
		return institut;
	}
	public void setInstitut(String institut)
	{
		this.institut = institut;
	}
	public Personnel getPersonnel()
	{
		return personnel;
	}
	public void setPersonnel(Personnel personnel)
	{
		this.personnel = personnel;
	}
	public String getChemin()
	{
		return chemin;
	}
	public void setChemin(String chemin)
	{
		this.chemin = chemin;
	}
	
}