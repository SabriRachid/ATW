package com.beans.attijariRh;

import java.util.Date;

public class Remuneration
{
	private int idRemuneration;
	private int annee;
	private float SBA;
	private float SBM;
	private float SNM;
	private float bonus;
	private float autres;
	private Date dateDecision;
	
	public int getIdRemuneration()
	{
		return idRemuneration;
	}
	public void setIdRemuneration(int idRemuneration)
	{
		this.idRemuneration = idRemuneration;
	}
	public float getSBA()
	{
		return SBA;
	}
	public void setSBA(float sBA)
	{
		SBA = sBA;
	}
	public float getSBM()
	{
		return SBM;
	}
	public void setSBM(float sBM)
	{
		SBM = sBM;
	}
	public float getBonus()
	{
		return bonus;
	}
	public void setBonus(float bonus)
	{
		this.bonus = bonus;
	}
	public float getAutres()
	{
		return autres;
	}
	public void setAutres(float autres)
	{
		this.autres = autres;
	}
	
	public int getAnnee()
	{
		return annee;
	}
	public void setAnnee(int annee)
	{
		this.annee = annee;
	}
	public float getSNM()
	{
		return SNM;
	}
	public void setSNM(float sNM)
	{
		SNM = sNM;
	}
	public Date getDateDecision()
	{
		return dateDecision;
	}
	public void setDateDecision(Date dateDecision)
	{
		this.dateDecision = dateDecision;
	}
	
	
	
}
