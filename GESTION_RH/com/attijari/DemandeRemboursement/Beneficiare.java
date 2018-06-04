package com.attijari.DemandeRemboursement;

public class Beneficiare{
	
	public String NCMIM;
	public String nom;
	public String prenom;
	
	public Beneficiare()
	{
		super();
	}

	public Beneficiare(String nCMIM, String nom, String prenom)
	{
		super();
		NCMIM = nCMIM;
		this.nom = nom;
		this.prenom = prenom;
	}

	public String getNCMIM()
	{
		return NCMIM;
	}

	public void setNCMIM(String nCMIM)
	{
		NCMIM = nCMIM;
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
	
	
	
	
	
	
	
	
}
