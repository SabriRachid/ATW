package com.attijari.GestionSalarie;

public class Personnel
{
	public String Matricule;
	public String Nom;
	public String Prenom;
	public String Poste;
	
	
	public Personnel()
	{
		super();
	}


	public Personnel(String matricule, String nom, String prenom, String poste)
	{
		super();
		Matricule = matricule;
		Nom = nom;
		Prenom = prenom;
		Poste = poste;
	}


	public String getMatricule()
	{
		return Matricule;
	}


	public void setMatricule(String matricule)
	{
		Matricule = matricule;
	}


	public String getNom()
	{
		return Nom;
	}


	public void setNom(String nom)
	{
		Nom = nom;
	}


	public String getPrenom()
	{
		return Prenom;
	}


	public void setPrenom(String prenom)
	{
		Prenom = prenom;
	}


	public String getPoste()
	{
		return Poste;
	}


	public void setPoste(String poste)
	{
		Poste = poste;
	} 
	
	
	
	
}
