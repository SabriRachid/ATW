package beans;

import java.util.Date;

public class Conge
{
	private String type;
	private Date dateDeb;
	private Date dateFin;
	private String momDeb;
	private String momFin;
	
	private String momDebEx;
	private String momFinEx;
	
	public Conge(Date dateDeb, Date dateFin, String momDeb, String momFin)
	{
		super();
		this.dateDeb = dateDeb;
		this.dateFin = dateFin;
		this.momFin = momFin;
		this.momDeb = momDeb;
	}
	
	public Conge(String momDeb, String momFin,Date dateDeb, Date dateFin)
	{
		super();
		this.dateDeb = dateDeb;
		this.dateFin = dateFin;
		this.momDeb = momFin;
		this.momFin = momDeb;
	}
	
	

public Conge()
	{
		// TODO Auto-generated constructor stub
	}

	//	public Conge()
	{
		// TODO Auto-generated constructor stub
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Date getDateDeb()
	{
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb)
	{
		this.dateDeb = dateDeb;
	}

	public Date getDateFin()
	{
		return dateFin;
	}

	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}

	public String getMomDeb()
	{
		return momDeb;
	}

	public void setMomDeb(String momDeb)
	{
		this.momDeb = momDeb;
	}

	public String getMomFin()
	{
		return momFin;
	}

	public void setMomFin(String momFin)
	{
		this.momFin = momFin;
	}
	//----------------
	
	public String getMomDebEx()
	{
		return momFin;
	}

	public void setMomDebEx(String momDeb)
	{
		this.momFin = momDeb;
	}

	public String getMomFinEx()
	{
		return momDeb;
	}

	public void setMomFinEx(String momFin)
	{
		this.momDeb = momFin;
	}
	
	
	
}
