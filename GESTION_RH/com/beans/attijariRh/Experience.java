package com.beans.attijariRh;



import java.util.Date;

public class Experience {
	private String idExperience;
	private Date dateDebut;
	private Date dateFin;
	private String societeAcceuil;
	private String tache;
	private String typeExperience;
	private String profil;
	public Personnel personnel;
	
	
	
	public String getIdExperience()
	{
		return idExperience;
	}
	public void setIdExperience(String idExperience)
	{
		this.idExperience = idExperience;
	}
	public Date getDateDebut()
	{
		return dateDebut;
	}
	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}
	public Date getDateFin()
	{
		return dateFin;
	}
	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}
	public String getSocieteAcceuil()
	{
		return societeAcceuil;
	}
	public void setSocieteAcceuil(String societeAcceuil)
	{
		this.societeAcceuil = societeAcceuil;
	}
	public String getTache()
	{
		return tache;
	}
	public void setTache(String tache)
	{
		this.tache = tache;
	}
	public String getTypeExperience()
	{
		return typeExperience;
	}
	public void setTypeExperience(String typeExperience)
	{
		this.typeExperience = typeExperience;
	}
	public String getProfil()
	{
		return profil;
	}
	public void setProfil(String profil)
	{
		this.profil = profil;
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