package com.beans.attijariRh;


import java.util.List;
import java.util.Date;


public class Personnel {
	private String matricule;
	private String nom;
	private String nomJeuneFille;
	private String prenom;
	private Date dateNaissance;
	private String adresse;
	private String email;
	private String nationalite;
	private String nationalite2;
	private Date dateEmbaucheGroupe;
	private Date dateEmbaucheFiliale;
	private String photo;
	private String situationFamiliale;
	private String nCNSS;
	private String nCIMR;
	private String nCMIM;
	private String rib;
	private String agence;
	private float nombreJoursCongeAnnuel;
	private float nombrJoursCongeDispo;
	private float reliquatConge;
	private String vdocLogin;
	private String etat;
	private String loisir;
	private boolean sexeMasculin;
	private String lieuNaissance;
	private boolean statut;
	
	private Direction direction;
	private Poste poste;
	private Filiale filiale;
	
	private Telephone telephone;
	private CarteNational carteNational;
	private Passeport passeport;
	
	
	private List<Conjoint> conjoints;
	private List<Enfant> enfants ;
	private List<Diplome> diplomes;
	private List<Experience> experiences;
	private List<Remuneration> remunerations;
	private List<Personnel> superieurs;
	
	
	
	
	
	
	
	public Personnel()
	{
		super();
		// TODO Auto-generated constructor stub
	}







	public String getMatricule()
	{
		return matricule;
	}







	public void setMatricule(String matricule)
	{
		this.matricule = matricule;
	}







	public String getNom()
	{
		return nom;
	}







	public void setNom(String nom)
	{
		this.nom = nom;
	}







	public String getNomJeuneFille()
	{
		return nomJeuneFille;
	}







	public void setNomJeuneFille(String nomJeuneFille)
	{
		this.nomJeuneFille = nomJeuneFille;
	}







	public String getPrenom()
	{
		return prenom;
	}







	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}







	public Date getDateNaissance()
	{
		return dateNaissance;
	}







	public void setDateNaissance(Date dateNaissance)
	{
		this.dateNaissance = dateNaissance;
	}







	public String getAdresse()
	{
		return adresse;
	}







	public void setAdresse(String adresse)
	{
		this.adresse = adresse;
	}







	public String getEmail()
	{
		return email;
	}







	public void setEmail(String email)
	{
		this.email = email;
	}







	public String getNationalite()
	{
		return nationalite;
	}







	public void setNationalite(String nationalite)
	{
		this.nationalite = nationalite;
	}







	public String getNationalite2()
	{
		return nationalite2;
	}







	public void setNationalite2(String nationalite2)
	{
		this.nationalite2 = nationalite2;
	}







	public Date getDateEmbaucheGroupe()
	{
		return dateEmbaucheGroupe;
	}







	public void setDateEmbaucheGroupe(Date dateEmbaucheGroupe)
	{
		this.dateEmbaucheGroupe = dateEmbaucheGroupe;
	}







	public Date getDateEmbaucheFiliale()
	{
		return dateEmbaucheFiliale;
	}







	public void setDateEmbaucheFiliale(Date dateEmbaucheFiliale)
	{
		this.dateEmbaucheFiliale = dateEmbaucheFiliale;
	}







	public String getPhoto()
	{
		return photo;
	}







	public void setPhoto(String photo)
	{
		this.photo = photo;
	}







	public String getSituationFamiliale()
	{
		return situationFamiliale;
	}







	public void setSituationFamiliale(String situationFamiliale)
	{
		this.situationFamiliale = situationFamiliale;
	}







	public String getnCNSS()
	{
		return nCNSS;
	}







	public void setnCNSS(String nCNSS)
	{
		this.nCNSS = nCNSS;
	}







	public String getnCIMR()
	{
		return nCIMR;
	}







	public void setnCIMR(String nCIMR)
	{
		this.nCIMR = nCIMR;
	}







	public String getnCMIM()
	{
		return nCMIM;
	}







	public void setnCMIM(String nCMIM)
	{
		this.nCMIM = nCMIM;
	}







	public String getRib()
	{
		return rib;
	}







	public void setRib(String rib)
	{
		this.rib = rib;
	}







	public String getAgence()
	{
		return agence;
	}







	public void setAgence(String agence)
	{
		this.agence = agence;
	}







	public float getNombreJoursCongeAnnuel()
	{
		return nombreJoursCongeAnnuel;
	}







	public void setNombreJoursCongeAnnuel(float nombreJoursCongeAnnuel)
	{
		this.nombreJoursCongeAnnuel = nombreJoursCongeAnnuel;
	}







	public float getNombrJoursCongeDispo()
	{
		return nombrJoursCongeDispo;
	}







	public void setNombrJoursCongeDispo(float nombrJoursCongeDispo)
	{
		this.nombrJoursCongeDispo = nombrJoursCongeDispo;
	}







	public float getReliquatConge()
	{
		return reliquatConge;
	}







	public void setReliquatConge(float reliquatConge)
	{
		this.reliquatConge = reliquatConge;
	}







	public String getVdocLogin()
	{
		return vdocLogin;
	}







	public void setVdocLogin(String vdocLogin)
	{
		this.vdocLogin = vdocLogin;
	}







	public String getEtat()
	{
		return etat;
	}







	public void setEtat(String etat)
	{
		this.etat = etat;
	}







	public Boolean getSexeMasculin()
	{
		return sexeMasculin;
	}







	public void setSexeMasculin(Boolean sexeMasculin)
	{
		this.sexeMasculin = sexeMasculin;
	}







	public Direction getDirection()
	{
		return direction;
	}







	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}







	public Poste getPoste()
	{
		return poste;
	}







	public void setPoste(Poste poste)
	{
		this.poste = poste;
	}







	public Telephone getTelephone()
	{
		return telephone;
	}







	public void setTelephone(Telephone telephone)
	{
		this.telephone = telephone;
	}







	public CarteNational getCarteNational()
	{
		return carteNational;
	}







	public void setCarteNational(CarteNational carteNational)
	{
		this.carteNational = carteNational;
	}







	public Passeport getPasseport()
	{
		return passeport;
	}







	public void setPasseport(Passeport passeport)
	{
		this.passeport = passeport;
	}







	public Filiale getFiliale()
	{
		return filiale;
	}







	public void setFiliale(Filiale filiale)
	{
		this.filiale = filiale;
	}







	public List<Conjoint> getConjoints()
	{
		return conjoints;
	}







	public void setConjoints(List<Conjoint> conjoints)
	{
		this.conjoints = conjoints;
	}







	public List<Diplome> getDiplomes()
	{
		return diplomes;
	}







	public void setDiplomes(List<Diplome> diplomes)
	{
		this.diplomes = diplomes;
	}







	public List<Experience> getExperiences()
	{
		return experiences;
	}







	public void setExperiences(List<Experience> experiences)
	{
		this.experiences = experiences;
	}







	public List<Remuneration> getRemunerations()
	{
		return remunerations;
	}







	public void setRemunerations(List<Remuneration> remunerations)
	{
		this.remunerations = remunerations;
	}







	public List<Enfant> getEnfants()
	{
		return enfants;
	}







	public void setEnfants(List<Enfant> enfants)
	{
		this.enfants = enfants;
	}







	public List<Personnel> getSuperieurs()
	{
		return superieurs;
	}







	public void setSuperieurs(List<Personnel> superieurs)
	{
		this.superieurs = superieurs;
	}







	public String getLoisir()
	{
		return loisir;
	}







	public void setLoisir(String loisir)
	{
		this.loisir = loisir;
	}







	public String getLieuNaissance()
	{
		return lieuNaissance;
	}







	public void setLieuNaissance(String lieuNaissance)
	{
		this.lieuNaissance = lieuNaissance;
	}







	public Boolean getStatut()
	{
		return statut;
	}







	public void setStatut(Boolean statut)
	{
		this.statut = statut;
	}
	
	
	
	
	
}