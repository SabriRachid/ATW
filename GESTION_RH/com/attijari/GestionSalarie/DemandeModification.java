package com.attijari.GestionSalarie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.client.IOption;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.structs.Period;

import dao.SingletonConnexionBDD;

public class DemandeModification extends ConnexionBDD{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Date dateDebutExp;
	public static Date dateFinExp;


	public String getIdFil (String Libelle){

		String IDFiliale = null; 

		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req= "select IdFiliale from Filiale where Libelle = ?  ";
			st =connection.prepareStatement(req);
			st.setString(1, Libelle);
			ResultSet rs = st.executeQuery();

			while (rs.next()){
				IDFiliale = rs.getString(1);
			}

		}catch(Exception e){
			e.printStackTrace();

		}


		return IDFiliale;


	}

	public String copy_PJDiplome(ILinkedResource test1, String sysname, String nom, String Prenom, String ref) throws IOException
	{
		try{
			ILinkedResource instance1 = test1;
			List<IAttachment> docfp = (List<IAttachment>) instance1.getValue(sysname);
			String filename = "";
			if (docfp.size() > 0)
			{
				filename = nom + "_" + Prenom +ref+".pdf" ;
				File newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//Diplomes//" + filename);
				newFile.createNewFile();
				InputStream is = docfp.get(0).getInputStream();
				OutputStream os = new FileOutputStream(newFile);
				byte[] buffer = new byte[is.available()];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
			}


			return  filename;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}


	}

	//methode pour selectionner l'id d'un poste 
	public String selectIdPoste(String LibellePoste)
	{

		String IdPoste = null;
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select IdPoste from Poste where LibellePoste=?";
			st = connection.prepareStatement(req);
			st.setString(1, LibellePoste);
			rs = st.executeQuery();
			while (rs.next())
			{

				IdPoste = rs.getString(1);
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IdPoste;
	}

	//methode pour selectionner l'id d'une direction 
	public String selectIdDirection(String LibelleDir)
	{

		String IdDirection = null;
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select IdDirection from Direction where Libelle=?";
			st = connection.prepareStatement(req);
			st.setString(1, LibelleDir);
			rs = st.executeQuery();
			while (rs.next())
			{

				IdDirection = rs.getString(1);
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IdDirection;
	}

	public String copyFile(String sysname, String nom, String Prenom) throws IOException
	{
		instance = getWorkflowInstance();
		List<IAttachment> docfp = (List<IAttachment>) instance.getValue(sysname);
		String filename = null;
		if (docfp.size() > 0)
		{
			filename = nom.trim() + "_" + Prenom.trim() + ".jpg";
			File newFile = new File("C://VDocPlatform//ImageFicheSalarie//" + filename);
			newFile.createNewFile();
			InputStream is = docfp.get(0).getInputStream();
			OutputStream os = new FileOutputStream(newFile);
			byte[] buffer = new byte[is.available()];
			int length;
			while ((length = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
		}
		return filename;

	}

	public String getMat(){
		String Matricule = null ; 
		String chemin = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("Chemin");
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(chemin));
			String line;
			while ((line = in.readLine()) != null)
			{
				// Afficher le contenu du fichier
				Matricule = line;
				// System.out.println (line);
			}
			in.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Matricule;

	}
	//methode pour modifier les informations personnel 
	public void updatePersonnel (String Matricule, String IdPodte, String IdDirection, String nom, String prenom, Date DateN, String adresse, String Email, String Nationalite, Date dateEmbaucheGR,
			Date dateEmbaucheFil, String Photo, String SituationF, String NCNSS, String NCIMR, String NCMIM, float SBA, float SBM, float bonus, float Avantage, float NbrEnfant, float NbreConge,
			float congeDispo, String vdocLogin, String lieuNaissance, String IdFiliale, String RIB, String Superieur, String ProfilUser, boolean Statut){


		try{

			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();

			String req = "update Personnel set PosteIdPoste= ?,DirectionIdDirection= ?, Nom=?, Prenom=?, DateNaissance=?, "
					+ "Adresse=?, Email= ?, Nationalite=? , DateEmbaucheGroupe= ?, DateEmbaucheFiliale= ?,Photo= ?,SituationFamiliale= ?,"
					+ "NCNSS=?, NCIMR= ?,NCMIM= ?,SBA=?,SBM=?,Bonus= ?, Avantage= "
					+ "?,NombreEnfant= ?,NombreJoursConge= ?,NombrJoursDispo= ?, loginVdoc = ?, "
					+ "FilialeIdFiliale=?, lieuNaissance=? , RIB = ? ,supMatricule = ?, profilUser= ?, Statut =?  where Matricule = ? ";
			st = connection.prepareStatement(req);

			//	st.setString(1, Matricule);
			// st.setString(2, supMat);
			st.setString(1, IdPodte);
			st.setString(2, IdDirection);
			st.setString(3, nom);
			st.setString(4, prenom);
			java.sql.Date DateNaiss = new java.sql.Date(DateN.getTime());
			st.setDate(5, DateNaiss);
			//		st.setDate(6, DateN);
			st.setString(6, adresse);
			st.setString(7, Email);
			st.setString(8, Nationalite);
			java.sql.Date datefil = new java.sql.Date(dateEmbaucheFil.getTime());
			java.sql.Date dateemb = new java.sql.Date(dateEmbaucheGR.getTime());
			st.setDate(9, dateemb);
			st.setDate(10, datefil);
			st.setString(11, copyFile("P_GS_Photo", nom, prenom));
			st.setString(12, SituationF);
			st.setString(13, NCNSS);
			st.setString(14, NCIMR);
			st.setString(15, NCMIM);
			st.setFloat(16, SBA);
			st.setFloat(17, SBM);
			st.setFloat(18, bonus);
			st.setFloat(19, Avantage);
			st.setFloat(20, NbrEnfant);
			st.setFloat(21, NbreConge);
			st.setFloat(22, congeDispo);
			st.setString(23, vdocLogin);
			st.setString(24, IdFiliale);
			st.setString(25, lieuNaissance);
			st.setString(26, RIB);
			st.setString(27,Superieur );
			st.setString(28, ProfilUser);
			st.setBoolean(29, Statut);
			st.setString(30, Matricule);

			//st.setBoolean(24, SexeMasculin);

			st.executeUpdate();


		}catch(Exception e){

			e.printStackTrace();
		}	}
	//methode pour modifier un utilisateur dans l'annuaire 
	public void updateUser( String matricule, String idHierMang, String idMngr){


		try
		{
			connection = ConnectionDefinition("Vdoc").getConnection();
			String req = "update vdoc_user set  modified=?, "
					+ "hierarchicalManager_id=(select id from dbo.vdoc_user where employeeNumber=?), "
					+ "   manager_id=(select id from dbo.vdoc_user where employeeNumber=?) "
					+ "where employeeNumber =? ";
			st = connection.prepareStatement(req);

			Date dateC = new Date();
			java.sql.Date dateS = new java.sql.Date (dateC.getTime());
			st.setDate(1, dateS);
			st.setString(2, idHierMang);
			st.setString(3, idMngr);
			st.setString(4, matricule);

			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//methode pour Modifier la carte national
	public void updateCarteNational(Date dateExperation, String MatriculePerso)
	{

		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update CarteNational set  DateExperation=? where Personnelmatricule = ? ";
			st = connection.prepareStatement(req);

			java.sql.Date DateExp = new java.sql.Date(dateExperation.getTime());
			st.setDate(1, DateExp);
			st.setString(2, MatriculePerso);

			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//Methode pour modifier les telephones 
	public void updateTelephone(String Tel1, String Tel2, String TelUrgences, String PersonneUrgence, String MatriculePerso)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Telephone set Tel1=?, Tel2=?, TelUrgences=? ,TelContactUrgence=? where Personnelmatricule =? ";
			st = connection.prepareStatement(req);
			st.setString(1, Tel1);
			st.setString(2, Tel2);
			st.setString(3, TelUrgences);
			st.setString(4,PersonneUrgence);
			st.setString(5, MatriculePerso);
			st.executeUpdate();

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//methode pour modifier le passeport 
	public void updatePassePort( Date dateExperation, String MatriculePerso)
	{

		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Passeport set  DateExperation=? where Personnelmatricule = ? ";
			st = connection.prepareStatement(req);

			java.sql.Date DateExp = new java.sql.Date(dateExperation.getTime());
			st.setDate(1, DateExp);
			st.setString(2, MatriculePerso);

			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//methode pour modifier conjoint 
	public void updateConjoint(String CIN, String nom, String prenom, Date dateN,String NCMIM ,String MatriculePerso)
	{
		//, String MatriculePerso
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Conjoint set Cin=?, Nom=?, Prenom= ?, DateNaissance=? , NCMIM = ? where personnelMatricule = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, CIN);
			st.setString(2, nom);
			st.setString(3, prenom);
			java.sql.Date DateNaiss = new java.sql.Date(dateN.getTime());
			st.setDate(4, DateNaiss);
			st.setString(5, NCMIM);
			st.setString(6, MatriculePerso);

			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//Methode pour ajouter un conjoint 
	public void addConjoint(String cin, String nom, String prenom, Date dateN, String MatriculePerso, String CMIM)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Conjoint values(?,?,?,?,?,?)";
			st = connection.prepareStatement(req);

			st.setString(1, cin);
			st.setString(2, nom);
			st.setString(3, prenom);
			java.sql.Date DateNai = new java.sql.Date(dateN.getTime());
			st.setDate(4, DateNai);
			st.setString(5, MatriculePerso);
			st.setString(6, CMIM);

			st.executeUpdate();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	// methode pour inserer un diplome dans la base de donnes
	public void addDiplome(ILinkedResource test, String NSystem,String IdDiplome, String specialité, Date dateObtention, String typeDiplome, String Institut, String MatriculePerso,String nom,String Prenom, String ref)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Diplome values(?,?,?,?,?,?,?)";
			st = connection.prepareStatement(req);

			st.setString(1, IdDiplome);
			st.setString(2, MatriculePerso);
			st.setString(3, specialité);
			java.sql.Date DateDiplome = new java.sql.Date(dateObtention.getTime());
			st.setDate(4, DateDiplome);
			st.setString(5, typeDiplome);
			st.setString(6, Institut);
			st.setString(7, copy_PJDiplome(test, NSystem, nom, Prenom,ref) );

			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//methode pour modifier les diplomes 
	public void UpdateDiplome(ILinkedResource test, String NSystem, String specialité, Date dateObtention, String typeDiplome, String Institut ,  String IdDiplome,String nom,String Prenom, String ref){
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Diplome set Specialite = ?,DateObtention =?, TypeDiplome = ?,Institut = ?,PJ_Diplome = ?  where IdDiplome = ?";
			st = connection.prepareStatement(req);



			st.setString(1, specialité);
			java.sql.Date DateDiplome = new java.sql.Date(dateObtention.getTime());
			st.setDate(2, DateDiplome);
			st.setString(3, typeDiplome);
			st.setString(4, Institut);
			st.setString(5, IdDiplome);
			st.setString(6, copy_PJDiplome(test, NSystem, nom, Prenom, ref));


			st.executeUpdate();

		}catch(Exception e){

			e.printStackTrace();

		}


	}
	//methode pour ajouter un enfant 
	public void addEnfant(String idEnfant, String nom, String prenom, Date dateN, String CMIM)
	{

		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Enfant values(?,?,?,?,?)";
			st = connection.prepareStatement(req);

			st.setString(1, idEnfant);
			st.setString(2, nom);
			st.setString(3, prenom);
			java.sql.Date DateNai = new java.sql.Date(dateN.getTime());
			st.setDate(4, DateNai);
			st.setString(5, CMIM);

			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//methode pour modifier un enfant 
	public void updateEnfant(String Nom, String Prenom, Date dateN, String IdEnfant)
	{
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Enfant set Nom=?, Prenom= ?, Datenaissance=?  where IdEnfant = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, Nom);
			st.setString(2, Prenom);
			java.sql.Date DateNaissance = new java.sql.Date(dateN.getTime());
			st.setDate(3, DateNaissance);
			st.setString(4, IdEnfant);

			st.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}

	}	
	// =======================================================================
	//
	// =======================================================================
	// methode pour ajouter dans la table PersConjEnf	
	public void addPersConjEnfa( String Matricule, String IdEnfant, String CinConjoint)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into PersConjEnfa values(?,?,?) ";
			st = connection.prepareStatement(req);
			st.setString(1, Matricule);
			st.setString(2, IdEnfant);
			st.setString(3, CinConjoint);
			st.executeUpdate();

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// =======================================================================
	//
	// =======================================================================
	// methode pour inserer une experience dans la base de donnees
	public void addExperience(String IdExper,  Date dateD, Date dateF, String sociétéAcceuil, String Mission, String TypeExper, String Profil, String MatriculePerso)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Experience values(?,?,?,?,?,?,?,?)";
			st = connection.prepareStatement(req);

			st.setString(1, IdExper);
			st.setString(2, MatriculePerso);
			java.sql.Date DateDe = new java.sql.Date(dateD.getTime());
			st.setDate(3, DateDe);
			java.sql.Date DateFI = new java.sql.Date(dateF.getTime());
			st.setDate(4, DateFI);
			st.setString(5, sociétéAcceuil);
			st.setString(6, TypeExper);
			st.setString(7, Profil);
			st.setString(8, Mission);

			st.executeUpdate();
		}

		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// =======================================================================
	//
	// =======================================================================
	//Methode pour ajouter les remuneration 
	public void addRemuneration(float SBA, float SBM, float Avantage, float Bonus, float autreRemun, int Annee, String Matricule){
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Remuneration values(?,?,?,?,?,?,?)";
			st = connection.prepareStatement(req);
			st.setFloat(1, SBA);
			st.setFloat(2, SBM);
			st.setFloat(3, Bonus);
			st.setFloat(4, Avantage);
			st.setFloat(5, autreRemun);
			st.setFloat(6, Annee);
			st.setString(7, Matricule);

			st.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	//methode pour modifier les remunerations
	//		public void UpdateRemun (float SBA, float SBM, float Avantage, float Bonus, float autreRemun, int Annee, String Matricule){
	//			try{
	//			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
	//			String req = "update Remuneration set SBA = ?, SBM=?, Bonus=?, Avantage = ?, autres= ?, anneeRemunetation=? where Personnelmatricule = ?";
	//			st = connection.prepareStatement(req);
	//			st.setFloat(1, SBA);
	//			st.setFloat(2, SBM);
	//			st.setFloat(3, Avantage);
	//			st.setFloat(4, Bonus);
	//			st.setFloat(5, autreRemun);
	//			st.setInt(6, Annee);
	//			st.setString(7, Matricule);
	//			
	//			st.executeUpdate();
	//			
	//			}catch(Exception e){
	//				e.printStackTrace();
	//			}
	//			
	//			
	//		}

	public void removeEX(String tabVdoc, String ChampVdoc, String Mat) {

		ArrayList<String> IdExper = getIdExperience(Mat);
		List assoc = (List) getWorkflowInstance().getLinkedResources(tabVdoc);
		for (String ListeExper : IdExper) {
			boolean trouve=false;
			String IdExpVdoc = "";

			if (assoc.size() != 0) {
				for (Iterator IT = assoc.iterator(); IT.hasNext();) {
					ILinkedResource associa = (ILinkedResource) IT.next();
					IdExpVdoc = (String) associa.getValue(ChampVdoc);
					if (IdExpVdoc != null) {
						if (IdExpVdoc.equals(ListeExper)) {
							trouve=true;
						}
						
					}
				}
			}
			
			if(trouve==false)
				removeExperience(ListeExper);
		}
	}
	
	
	public void removeDM(String tabVdoc, String ChampVdoc, String Mat){
		
		ArrayList<String> IdDip = getidDiplome(Mat);
		List assoc = (List) getWorkflowInstance().getLinkedResources(tabVdoc);
		for (String Listediplome : IdDip) {
			boolean trouve=false;
			String IdDipVdoc = "";

			if (assoc.size() != 0) {
				for (Iterator IT = assoc.iterator(); IT.hasNext();) {
					ILinkedResource associa = (ILinkedResource) IT.next();
					IdDipVdoc = (String) associa.getValue(ChampVdoc);
					if (IdDipVdoc != null) {
						if (IdDipVdoc.equals(Listediplome)) {
							trouve=true;
						}
						
					}
				}
			}
			
			if(trouve==false)
				removeDiplome(Listediplome);
		}
	}
	
	public void removeEN(String tabVdoc, String ChampVdoc, String Mat){
		
		ArrayList<String> IdEnf = getidEnfant(Mat);
		List assoc = (List) getWorkflowInstance().getLinkedResources(tabVdoc);
		for (String ListeEnfant : IdEnf) {
			boolean trouve=false;
			String IdEnfVDOC = "";

			if (assoc.size() != 0) {
				for (Iterator IT = assoc.iterator(); IT.hasNext();) {
					ILinkedResource associa = (ILinkedResource) IT.next();
					IdEnfVDOC = (String) associa.getValue(ChampVdoc);
					if (IdEnfVDOC != null) {
						if (IdEnfVDOC.equals(ListeEnfant)) {
							trouve=true;
						}
						
					}
				}
			}
			
			if(trouve==false)
				removeEnfant(ListeEnfant);
		}
	}
	
	public ArrayList<String> getIdExperience (String Matricule){
		ArrayList<String> ListExper = new ArrayList<>();
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select IdExperience from Experience where Personnelmatricule = ?  ";
			st = connection.prepareStatement(req);
			st.setString(1, Matricule);
			ResultSet rs = st.executeQuery();
			while (rs.next()){
				ListExper.add(rs.getString(1));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ListExper;
		
	}
	
	public ArrayList<String> getidDiplome(String Matricule){
		ArrayList<String> ListDipl = new ArrayList<>();
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select IdDiplome from Diplome where Personnelmatricule = ?  ";
			st = connection.prepareStatement(req);
			st.setString(1, Matricule);
			ResultSet rs = st.executeQuery();
			while (rs.next()){
				ListDipl.add(rs.getString(1));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ListDipl;
	}
	
	public ArrayList<String> getidEnfant(String Matricule){
		
	ArrayList<String> ListEnf = new ArrayList<>();
	try {
		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String req = "select EnfantIdEnfant from PersConjEnfa where Personnelmatricule = ?  ";
		st = connection.prepareStatement(req);
		st.setString(1, Matricule);
		ResultSet rs = st.executeQuery();
		while (rs.next()){
			ListEnf.add(rs.getString(1));
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return ListEnf;
	}
	
	public void removeExperience(String idExpe){
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "DELETE FROM Experience WHERE IdExperience= ? ";
			st = connection.prepareStatement(req);
			st.setString(1, idExpe);
			st.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void removeDiplome(String idDipl){
		
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "DELETE FROM Diplome WHERE IdDiplome= ? ";
			st = connection.prepareStatement(req);
			st.setString(1, idDipl);
			st.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void removeEnfant(String idEnfant){
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "DELETE FROM Enfant WHERE IdEnfant= ? ";
			st = connection.prepareStatement(req);
			st.setString(1, idEnfant);
			st.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	// =======================================================================
	//Methode pour modifier les experiences 
	// =======================================================================
	
	public void updateExperiences (String IdExperience, Date dateD, Date dateF, String sociétéAcceuil, String Mission, String TypeExper, String Profil){
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Experience set DateDebut= ?, DateFin=? ,SocieteAcceuil= ?, TypeExperience= ?,Profil =?,Mission=? where IdExperience = ? ";
			st = connection.prepareStatement(req);


			java.sql.Date DateDe = new java.sql.Date(dateD.getTime());
			st.setDate(1, DateDe);
			java.sql.Date DateFI = new java.sql.Date(dateF.getTime());
			st.setDate(2, DateFI);
			st.setString(3, sociétéAcceuil);
			st.setString(4, TypeExper);
			st.setString(5, Profil);
			st.setString(6, Mission);
			st.setString(7, IdExperience);

			st.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	// =======================================================================
	//
	// =======================================================================
	public String getFilile (String IdFil){
		String LibelleFiliae = null ; 
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req= "select Libelle from Filiale where  IdFiliale = ?  ";
			st =connection.prepareStatement(req);

			st.setString(1, IdFil);

			ResultSet rs = st.executeQuery();
			while (rs.next()){
				LibelleFiliae = rs.getString(1);
			}

		}catch(Exception e){
			e.printStackTrace();

		}

		return LibelleFiliae;

	}
	// =======================================================================
	//
	// =======================================================================
	public String getCinConj(String Matricule){
		String CIN = null;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "  select Cin from Conjoint where Personnelmatricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, Matricule);
			ResultSet rs = st.executeQuery();
			while(rs.next()){

				CIN= rs.getString(1);
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return CIN;

	}
	// =======================================================================
	//
	// =======================================================================
	void DeletTabl (String NomSys){

		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(NomSys);
		getWorkflowInstance().deleteLinkedResources(assoc);
	}
	// =======================================================================
	//
	// =======================================================================
	public String GetIdENF (String Matricule){
		String IdEnfant = null; 
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "  select EnfantIdEnfant from PersConjEnfa where Personnelmatricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, Matricule);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				IdEnfant = rs.getString(1);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return IdEnfant ;
	}
	// =======================================================================
	// METHODE DE CHARGEMENT DES DONNÉES PÉRSONNELS
	// =======================================================================
	public void remplir (){
	
		try{
			
			String MatriculeUpdate = (String) getWorkflowInstance().getValue("P_GS_Collaborateur");
			getWorkflowInstance().setValue("P_GS_MatriculeUpdate", MatriculeUpdate);
			
			//String MatriculeUpdate = (String)getWorkflowInstance().getValue("P_GS_MatriculeUpdate");
			// Select personne
			getWorkflowInstance().setValue("P_GS_Photo", null);
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select  Nom, Prenom, dateNaissance, Adresse, Email, Nationalite, DateEmbaucheGroupe, DateEmbaucheFiliale,"
					+ " Photo, SituationFamiliale, NCNSS, NCIMR, NCMIM, SBA, SBM, Bonus, Avantage, NombreJoursConge," + ""
					+ " NombrJoursDispo,reliquatAnneEnCours ,loginVdoc, sexeMasculin, FilialeIdFiliale, lieuNaissance, DirectionIdDirection, RIB, supMatricule,profilUser, Statut from "
					+ "Personnel where matricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, MatriculeUpdate);
			rs = st.executeQuery();
			while (rs.next())
			{
				getWorkflowInstance().setValue("P_GS_Nom", rs.getString(1));
				getWorkflowInstance().setValue("P_GS_Prenom", rs.getString(2));
				getWorkflowInstance().setValue("DateDeNaissance", rs.getDate(3));
				getWorkflowInstance().setValue("P_GS_Adrs_Perso", rs.getString(4));
				getWorkflowInstance().setValue("P_GS_Mail", rs.getString(5));
				getWorkflowInstance().setValue("P_GS_ListeNationalite", rs.getString(6));
				getWorkflowInstance().setValue("P_GS_D_embuche_groupe", rs.getDate(7));
				getWorkflowInstance().setValue("P_GS_D_embuche_filiale", rs.getDate(8));
				//C:\\Tomcat\\webapps\\FicheSalarie\\images\\
				File doc = new File("C://VDocPlatform//ImageFicheSalarie//"+rs.getString(9));
				IAttachment att = getWorkflowModule().addAttachment(getWorkflowInstance(), "P_GS_Photo", doc);
				List<IAttachment> photo = (List<IAttachment>) getWorkflowInstance().getValue("P_GS_Photo");
				photo.clear();
				photo.add(att);
				getWorkflowInstance().setValue("P_GS_Photo", photo);
				getWorkflowInstance().setValue("P_GS_S_LFamilliale", rs.getString(10));
				getWorkflowInstance().setValue("P_GS_CNSS", rs.getString(11));
				getWorkflowInstance().setValue("P_GS_CIMR", rs.getString(12));
				getWorkflowInstance().setValue("P_GS_CMIM", rs.getString(13));
				getWorkflowInstance().setValue("P_GS_SBA", rs.getString(14));
				getWorkflowInstance().setValue("P_GS_SBM", rs.getString(15));
				getWorkflowInstance().setValue("P_GS_Bonus", rs.getString(16));
				getWorkflowInstance().setValue("P_GS_Avantage", rs.getString(17));
				//	getWorkflowInstance().setValue("P_GS_Nbre_Enfants", rs.getFloat(18));
				getWorkflowInstance().setValue("P_GS_nbreConge", rs.getFloat(19));
				getWorkflowInstance().setValue("P_MS_reliquat", rs.getString(20));
				getWorkflowInstance().setValue("P_GS_VdocLogin", rs.getString(21));
				//			getWorkflowInstance().setValue("P_GS_Sexe", rs.getString(21));
				//String Test = rs.getString(23);
				getWorkflowInstance().setValue("P_GS_Filiale", getFilile(rs.getString(23)));
				getWorkflowInstance().setValue("P_GS_LieuNaiss", rs.getString(24));
				getWorkflowInstance().setValue("GS_P_Descriptif_poste", rs.getString(25));
				getWorkflowInstance().setValue("P_GS_RIB", rs.getString(26));
				getWorkflowInstance().setValue("P_GS_Sup", rs.getString(27));
				getWorkflowInstance().setValue("P_GS_ProfilUser", rs.getString(28));
				boolean statut = rs.getBoolean(29);
				if(statut == true){
					getWorkflowInstance().setValue("P_MS_Statut", "Active");
				}else{
					getWorkflowInstance().setValue("P_MS_Statut", "Non active");
				}
			}
			
			
			// =======================================================================
			// CHARGER LES INFOS DE(S) CONJOINT(S)
			// =======================================================================
			
		
			String req8 = "  select Cin, Nom, Prenom, DateNaissance, NCMIM  from Conjoint where personnelMatricule like ?";
			st = connection.prepareStatement(req8);
			st.setString(1, MatriculeUpdate);
			ResultSet rs8 = st.executeQuery();
			while (rs8.next())
			{
				getWorkflowInstance().setValue("P_GS_CINConjoint", rs8.getString(1));
				getWorkflowInstance().setValue("P_GS_Nom_Conjoint", rs8.getString(2));
				getWorkflowInstance().setValue("P_G", rs8.getString(3));
				getWorkflowInstance().setValue("P_GS_DDN_Conjoint", rs8.getDate(4));
				getWorkflowInstance().setValue("P_GS_CMIM_Conjoint", rs8.getString(5));
			}
			
			// =======================================================================
			// CHARGER les INFOS CARTE NATIONALE
			// =======================================================================
		
		
			String req1 = "select CIN, DateExperation from CarteNational where Personnelmatricule = ?";
			st = connection.prepareStatement(req1);
			st.setString(1, MatriculeUpdate);
			ResultSet rs1 = st.executeQuery();
			while (rs1.next())
			{
				getWorkflowInstance().setValue("P_GS_CIN", rs1.getString(1));
				getWorkflowInstance().setValue("P_GS_date_expr_CIN", rs1.getDate(2));
			}
			// =======================================================================
			// SELECT PASSEPORT
			// =======================================================================
			String req2 = "select numPassport, DateExperation from Passeport where  Personnelmatricule = ? ";
			st = connection.prepareStatement(req2);
			st.setString(1, MatriculeUpdate);
			ResultSet rs2 = st.executeQuery();
			while (rs2.next())
			{
				getWorkflowInstance().setValue("P_GS_passePort", rs2.getString(1));
				getWorkflowInstance().setValue("P_GS_date_Exper_passeport", rs2.getDate(2));
			}
			// =======================================================================
			// SELECT TELEPHONE
			// =======================================================================
			String req3 = "select Tel1, Tel2, TelUrgences, TelContactUrgence from Telephone where Personnelmatricule = ? ";
			st = connection.prepareStatement(req3);
			st.setString(1, MatriculeUpdate);
			ResultSet rs3 = st.executeQuery();
			while (rs3.next())
			{
				getWorkflowInstance().setValue("P_GS_tel1", rs3.getString(1));
				getWorkflowInstance().setValue("P_GS_Tel2", rs3.getString(2));
				getWorkflowInstance().setValue("P_GS_Tel_Urgence", rs3.getString(3));
				getWorkflowInstance().setValue("P_GS_Personne_urgence", rs3.getString(4));
			}
			// =======================================================================
			// SELECT EXPERIENCE
			// =======================================================================
			DeletTabl("P_GS_TAB_Experience");
			List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
			if (assoc.size() == 0)
			{
				String req4 = "select DateDebut, DateFin, SocieteAcceuil, TypeExperience, Profil, Mission, IdExperience  from Experience where Personnelmatricule = ? ";
				st = connection.prepareStatement(req4);
				st.setString(1, MatriculeUpdate);
				ResultSet rs4 = st.executeQuery();
				while (rs4.next())
				{
					ILinkedResource associa = getWorkflowInstance().createLinkedResource("P_GS_TAB_Experience");
					com.axemble.vdoc.sdk.structs.Period periode = null;
					Period per = new Period(new java.util.Date(rs4.getDate(1).getTime()), new java.util.Date(rs4.getDate(2).getTime()));
					associa.setValue("P_GS_TAB_Experience_periode", per);
					associa.setValue("P_GS_TAB_Experience_Societe", rs4.getString(3));
					associa.setValue("P_GS_TAB_Experience_Type", rs4.getString(4));
					associa.setValue("P_GS_TAB_Experience_Profil", rs4.getString(5));
					associa.setValue("P_GS_TAB_Experience_Taches", rs4.getString(6));
					associa.setValue("P_GS_IdExperience", rs4.getString(7));
					getWorkflowInstance().addLinkedResource(associa);
				}
			}
			// =======================================================================
			// select diplome
			// =======================================================================
			DeletTabl("P_GS_TAB_diplome");

			List Diplomes = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
			if (Diplomes.size() == 0)
			{
				String req5 = "select Specialite, DateObtention, TypeDiplome, Institut, IdDiplome ,PJ_Diplome " 
						+ "from Diplome where Personnelmatricule = ? ";
				st = connection.prepareStatement(req5);
				st.setString(1, MatriculeUpdate);
				ResultSet rs5 = st.executeQuery();
				while (rs5.next())
				{
					ILinkedResource AllDiplomes = getWorkflowInstance().createLinkedResource("P_GS_TAB_diplome");
					AllDiplomes.setValue("P_GS_TAB_diplomes_Specialte", rs5.getString(1));
					AllDiplomes.setValue("P_GS_TAB_diplomes_dateObte", new java.util.Date(rs5.getDate(2).getTime()) );
					AllDiplomes.setValue("P_GS_TAB_diplomes_Type", rs5.getString(3));
					AllDiplomes.setValue("P_GS_TAB_diplomes_Institut", rs5.getString(4));
					String idp= rs5.getString(5);
					AllDiplomes.setValue("P_GS_IdDiplome", rs5.getString(5));
					File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//Diplomes//"+rs5.getString(6));
					IAttachment att = getWorkflowModule().addAttachment(AllDiplomes, "P_GS_TAB_diplomes_Diplome", doc);
					List<IAttachment> photo = (List<IAttachment>) AllDiplomes.getValue("P_GS_TAB_diplomes_Diplome");
					photo.clear();
					photo.add(att);
					AllDiplomes.setValue("P_GS_TAB_diplomes_Diplome", photo);
					getWorkflowInstance().addLinkedResource(AllDiplomes);
				}
			}
			// =======================================================================
			//select remuneration 
			// =======================================================================
			List Remuneration = (List) getWorkflowInstance().getLinkedResources("Tab_Remun");
			if(Remuneration.size() == 0){
				String req9 = "select SBA, SBM, Bonus, Avantage, autres, anneeRemunetation  from Remuneration where Personnelmatricule = ? "; 
				st = connection.prepareStatement(req9);
				st.setString(1,MatriculeUpdate );
				ResultSet rs6 = st.executeQuery();
				while (rs6.next()){
					ILinkedResource AllRemuni = getWorkflowInstance().createLinkedResource("Tab_Remun");
					AllRemuni.setValue("Tab_Remun_SBA", rs6.getFloat(1));
					AllRemuni.setValue("Tab_Remun_SBM", rs6.getFloat(2));
					AllRemuni.setValue("Tab_Remun_Bonus", rs6.getFloat(3));
					AllRemuni.setValue("Tab_Remun_Avantage", rs6.getFloat(4));
					AllRemuni.setValue("Tab_Autres", rs6.getFloat(5));
					int nbr = (int) rs6.getFloat(6);
					AllRemuni.setValue("TAB_Rem_Anne", nbr+"" );
					AllRemuni.setValue("Tab_statut", "ancien");
					getWorkflowInstance().addLinkedResource(AllRemuni);
				}
			}
			// =======================================================================
			// select Enfant 
			// =======================================================================
			DeletTabl("P_GS_TAB_Enfants");
			List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
			if (Enfants.size() == 0)
			{
				String req6 ="select E.nom, E.prenom, E.Datenaissance, E.IdEnfant, E.NCMIM "
						+ "from Enfant E, PersConjEnfa PCF"
						+ " where E.IdEnfant = PCF.EnfantIdEnfant and PCF.Personnelmatricule = ? ";
				st = connection.prepareStatement(req6);
				st.setString(1, MatriculeUpdate);
				ResultSet rs6 = st.executeQuery();
				while(rs6.next()){
					ILinkedResource AllEnfants = getWorkflowInstance().createLinkedResource("P_GS_TAB_Enfants");
					AllEnfants.setValue("P_GS_TAB_Enfant_Nom", rs6.getString(1));
					AllEnfants.setValue("P_GS_TAB_Enfant_Prenom", rs6.getString(2));
					AllEnfants.setValue("P_GS_TAB_Enfant_DateNa", new java.util.Date(rs6.getDate(3).getTime()));
					AllEnfants.setValue("P_GS_IdEnfant", rs6.getString(4));
					AllEnfants.setValue("P_GS_CMIM_Enfant", rs6.getString(5));
					getWorkflowInstance().addLinkedResource(AllEnfants);
				}
			}
			// =======================================================================
			// select poste
			// =======================================================================
			String req6 = "select P.LibellePoste, P.Descriptif from Poste P, Personnel Ps where Ps.PosteIdPoste = P.IdPoste and matricule = ?";
			st = connection.prepareStatement(req6);
			st.setString(1, MatriculeUpdate);
			ResultSet rs6 = st.executeQuery();
			while (rs6.next())
			{
				getWorkflowInstance().setValue("P_GS_Poste", rs6.getString(1));
				getWorkflowInstance().setValue("GS_P_Descriptif_poste", rs6.getString(2));
			}
			// =======================================================================
			// select direction
			// =======================================================================
			String req7 = "select Libelle from Direction D, Personnel P where D.IdDirection= P.DirectionIdDirection and matricule = ?";
			st = connection.prepareStatement(req7);
			st.setString(1, MatriculeUpdate);
			ResultSet rs7 = st.executeQuery();
			while (rs7.next())
			{
				String direction  = rs7.getString(1);
				getWorkflowInstance().setValue("P_GS_Direc", rs7.getString(1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// =======================================================================
	//
	// =======================================================================
	public void Privilege (){

		IUser iUser = (IUser) getWorkflowModule().getLoggedOnUser();

		String NomPrenom = iUser.getFullName();
		String NomPrenomVDOC = getWorkflowInstance().getValue("P_GS_Prenom")+" "+getWorkflowInstance().getValue("P_GS_Nom");

		if(NomPrenom.equals(NomPrenomVDOC)){

			getResourceController().setEditable("P_GS_Nom", false);
			getResourceController().setEditable("P_GS_Prenom", false);
			getResourceController().setEditable("P_GS_CIN", false);
			getResourceController().setEditable("P_GS_passePort", false);
			getResourceController().setEditable("P_GS_Poste", false);
			getResourceController().setEditable("GS_P_Descriptif_poste", false);
			getResourceController().setEditable("P_GS_Direction", false);
			getResourceController().setEditable("P_GS_D_embuche_filiale", false);
			getResourceController().setEditable("P_GS_D_embuche_groupe", false);
			getResourceController().setEditable("P_GS_CINConjoint", false);
			getResourceController().setEditable("P_GS_Nom_Conjoint", false);
			getResourceController().setEditable("P_GS_PrenomConjoint", false);
			getResourceController().setEditable("P_GS_Nbre_Enfants", false);
			getResourceController().setEditable("P_GS_DDN_Conjoint", false);
			getResourceController().setEditable("P_GS_SBA", false);
			getResourceController().setEditable("P_GS_SBM", false);
			getResourceController().setEditable("P_GS_Bonus", false);
			getResourceController().setEditable("P_GS_Avantage", false);
			getResourceController().setEditable("P_GS_nbreConge", false);
			getResourceController().setEditable("P_GS_TAB_Enfants", false);
		}else{
			getResourceController().setEditable("P_GS_Nom", false);
			getResourceController().setEditable("P_GS_Prenom", false);
			getResourceController().setEditable("P_GS_CIN", false);
			
		}

	}
	// =======================================================================
	//
	// =======================================================================

	@Override
	public boolean onAfterLoad()
	{
		try
		{
			IUser connectedUser = getWorkflowModule().getLoggedOnUser();
			IUser rhAfc =  getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"));
			IUser rhAti =  getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI"));
			if(connectedUser.equals(rhAti)||connectedUser.equals(rhAfc)){
				getWorkflowInstance().setValue("P_GS_Collaborateur",null);
				getResourceController().setEditable("P_GS_Collaborateur", true);
				getResourceController().setMandatory("P_GS_Collaborateur",true);
			}
			else{
				getWorkflowInstance().setValue("P_GS_Collaborateur",connectedUser.getEmployeeNumber());
				getResourceController().setEditable("P_GS_Collaborateur", false);
			}
			
			
			remplir();
	//	Privilege();  
		}catch (WorkflowModuleException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	// =======================================================================
	//
	// =======================================================================
	
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		if(property.getName().equals("P_GS_Collaborateur")){
			remplir();
			Privilege();  
		}
		
		super.onPropertyChanged(property);
	}
	
	// =======================================================================
	//
	// =======================================================================
	@Override
	public boolean onAfterSubmit(IAction action) {
//		List<IAttachment> photo = (List<IAttachment>) getWorkflowInstance().getValue("P_GS_Photo");
//		String Picture = null;
//		if (!photo.isEmpty())
//		{
//			Picture = photo.get(0).getShortName();
//		}
//
//		String MatriculeUpdate = getMat();
//
//		String sexe = (String) getWorkflowInstance().getValue("P_GS_Sexe");
//		//		String Matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
//		String nom = (String) getWorkflowInstance().getValue("P_GS_Nom");
//		String Prenom = (String) getWorkflowInstance().getValue("P_GS_Prenom");
//		String adresse = (String) getWorkflowInstance().getValue("P_GS_Adrs_Perso");
//		Date DateN = (Date) getWorkflowInstance().getValue("DateDeNaissance");
//		String nationalite = (String) getWorkflowInstance().getValue("P_GS_ListeNationalite");
//		//		String CIN = (String) getWorkflowInstance().getValue("P_GS_CIN");
//		String CNSS = (String) getWorkflowInstance().getValue("P_GS_CNSS");
//		String Tel1 = (String) getWorkflowInstance().getValue("P_GS_tel1");
//		String Tel2 = (String) getWorkflowInstance().getValue("P_GS_Tel2");
//		//		String PassePort = (String) getWorkflowInstance().getValue("P_GS_passePort");
//		String STFamilliale = (String) getWorkflowInstance().getValue("P_GS_S_LFamilliale");
//		String Poste = (String) getWorkflowInstance().getValue("P_GS_Poste");
//		//		String DescPoste = (String) getWorkflowInstance().getValue("GS_P_Descriptif_poste");
//		String DirectionRatt = (String) getWorkflowInstance().getValue("P_GS_Direc");
//		String Mail = (String) getWorkflowInstance().getValue("P_GS_Mail");
//		Date dateEmbGr = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_filiale");
//		Date dateEmbFl = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_groupe");
//		String nomConjoint = (String) getWorkflowInstance().getValue("P_GS_Nom_Conjoint");
//		Date DateNConjoint = (Date) getWorkflowInstance().getValue("P_GS_DDN_Conjoint");
//		float nbreEnfant1 = (float) getWorkflowInstance().getValue("P_GS_Nbre_Enfants");
//		float nbreEnfant = Float.parseFloat(nbreEnfant1+"");
//		//		String PrenomEnfant = (String) getWorkflowInstance().getValue("P_GS_Prenom_Enfants");
//		//		Date DateNEnfant = (Date) getWorkflowInstance().getValue("P_GS_DDN_Enfants");
//		Date dateExpCIN = (Date) getWorkflowInstance().getValue("P_GS_date_expr_CIN");
//		String CMIM = (String) getWorkflowInstance().getValue("P_GS_CMIM");
//		Date dateExpPassePort = (Date) getWorkflowInstance().getValue("P_GS_date_Exper_passeport");
//		String CIMR = (String) getWorkflowInstance().getValue("P_GS_CIMR");
//		String TelCasURgence = (String) getWorkflowInstance().getValue("P_GS_Tel_Urgence");
//		String PersonneUrgence = (String) getWorkflowInstance().getValue("P_GS_Personne_urgence");
//		String RIB = (String) getWorkflowInstance().getValue("P_GS_RIB") ;
//
//		Object Avantage1 = (Object) getWorkflowInstance().getValue("P_GS_Avantage");
//		float Avantage = Float.parseFloat(Avantage1.toString());
//
//		Object SBA1 = (Object) getWorkflowInstance().getValue("P_GS_SBA");
//		float SBA = Float.parseFloat(SBA1.toString());
//
//		Object Bonus1 = (Object) getWorkflowInstance().getValue("P_GS_Bonus");
//		float Bonus = Float.parseFloat(Bonus1.toString());
//
//		Object SBM1 = (Object) getWorkflowInstance().getValue("P_GS_SBM");
//		float SBM = Float.parseFloat(SBM1.toString());
//
//		// Date DateObtentionDiplom = (Date)getWorkflowInstance().getValue("P_GS_TAB_diplomes_dateObte");
//		// String Speciaite =(String)getWorkflowInstance().getValue("P_GS_TAB_diplomes_Specialte");
//		// String TypeDiplome =(String)getWorkflowInstance().getValue("P_GS_TAB_diplomes_Type");
//		// String PExperience = (String)getWorkflowInstance().getValue("P_GS_TAB_Experience_periode");
//		// String SocieteAccueil =(String)getWorkflowInstance().getValue("P_GS_TAB_Experience_Societe");
//		// String Tache =(String)getWorkflowInstance().getValue("P_GS_TAB_Experience_Taches");
//		String Suprieur = (String)getWorkflowInstance().getValue("P_GS_Sup");
//		String Manager = (String)getWorkflowInstance().getValue("P_GS_Managers");
//		float NbreConge = (float) getWorkflowInstance().getValue("P_GS_nbreConge");
//		String VdocLogin = (String) getWorkflowInstance().getValue("P_GS_VdocLogin");
//		String CINconjoint = (String) getWorkflowInstance().getValue("P_GS_CINConjoint");
//		String Prenomconjoint = (String) getWorkflowInstance().getValue("P_G");
//		String CMIM_Conjoint = (String) getWorkflowInstance().getValue("P_GS_CMIM_Conjoint");
//		String LieuNaissance = (String) getWorkflowInstance().getValue("P_GS_LieuNaiss");
//		String filiale = (String) getWorkflowInstance().getValue("P_GS_Filiale");
//		String ProfilUser = (String) getWorkflowInstance().getValue("P_GS_ProfilUser");
//		String StatutColabborateur = (String) getWorkflowInstance().getValue("P_MS_Statut");
//
//		boolean sexeM;
//
//		if ("Féminin".equals(sexe))
//		{
//			sexeM = false;
//		}
//		else
//		{
//			sexeM = true;
//		}
//		
//		
//		boolean Statut;
//		if("Active".equals(StatutColabborateur)){
//			Statut = true;
//		}else{
//			Statut = false;
//		}
//
//		//String MatriculUpdate = "Mat001"; //(String)getWorkflowInstance().getValue("P_GS_MatriculeUpdate");
//
//		updatePersonnel(MatriculeUpdate, selectIdPoste(Poste), selectIdDirection(DirectionRatt), nom, Prenom, DateN, adresse, Mail,
//				nationalite, dateEmbGr, dateEmbFl, Picture, STFamilliale, CNSS, CIMR, CMIM,SBA ,
//				SBM ,Bonus ,Avantage , nbreEnfant, NbreConge, NbreConge, VdocLogin,  LieuNaissance ,getIdFil(filiale), RIB, Suprieur,ProfilUser, Statut);
//
//		updateUser( MatriculeUpdate,  Suprieur, Manager);
//
//		updateCarteNational(  dateExpCIN ,MatriculeUpdate);
//
//		updateTelephone( Tel1, Tel2, TelCasURgence, PersonneUrgence, MatriculeUpdate);
//
//		updatePassePort( dateExpPassePort, MatriculeUpdate);
//
//		String CINCONJOINT = getCinConj(MatriculeUpdate);
//
//		if(STFamilliale.equals("Marié")){
//			if(CINCONJOINT==null){
//
//				addConjoint(CINconjoint, nomConjoint, Prenomconjoint, DateNConjoint, MatriculeUpdate, CMIM_Conjoint);
//			}
//			else{
//				updateConjoint(CINconjoint, nomConjoint, Prenomconjoint, DateNConjoint, CMIM_Conjoint,MatriculeUpdate );
//			}
//		}
//
//
//		//la modification des experiences 
//		removeEX("P_GS_TAB_Experience", "P_GS_IdExperience", MatriculeUpdate);
//		List assoc = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
//		if (assoc.size() != 0){
//
//			for (Iterator IT = assoc.iterator(); IT.hasNext();)
//			{
//
//				ILinkedResource associa = (ILinkedResource) IT.next();
//				com.axemble.vdoc.sdk.structs.Period periode = (Period) associa.getValue("P_GS_TAB_Experience_periode");
//				dateDebutExp = periode.getStartDate();
//				dateFinExp = periode.getEndDate();
//				String SocieteAcceuil = (String) associa.getValue("P_GS_TAB_Experience_Societe");
//				String TypeExperience = (String) associa.getValue("P_GS_TAB_Experience_Type");
//				String Profil = (String) associa.getValue("P_GS_TAB_Experience_Profil");
//				String Mission = (String) associa.getValue("P_GS_TAB_Experience_Taches");
//				if(associa.getValue("P_GS_IdExperience")==null){
//					int i = 100;
//					String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//					addExperience("Experience_"+ref + i, dateDebutExp, dateFinExp, SocieteAcceuil, Mission, TypeExperience, Profil, MatriculeUpdate);
//					i++;
//					////sourceController().alert(dateDebutExp + " " + dateFinExp);
//				}else {
//					String IdExperience = (String)associa.getValue("P_GS_IdExperience");
//					updateExperiences(IdExperience, dateDebutExp, dateFinExp, SocieteAcceuil, Mission, TypeExperience, Profil);
//				}
//			}
//		}
//		
//		
//
//
//		//Modifier Les diplomes 
//
//		removeDM("P_GS_TAB_diplome", "P_GS_IdDiplome", MatriculeUpdate);
//		List Diplomes = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
//		if (Diplomes.size() != 0)
//		{
//			int i = 100;
//			String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//			for (Iterator IT = Diplomes.iterator(); IT.hasNext();)
//			{
//				ILinkedResource AllDiplomes = (ILinkedResource) IT.next();
//				String Specialite = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Specialte");
//				Date DateObtentionDiplom = (Date) AllDiplomes.getValue("P_GS_TAB_diplomes_dateObte");
//				String TypeDiplome = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Type");
//				String Institut = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Institut");
//
//				if(AllDiplomes.getValue("P_GS_IdDiplome")==null){
//					String refD = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//					addDiplome(AllDiplomes, "P_GS_TAB_diplomes_Diplome", ref + i, Specialite, DateObtentionDiplom, TypeDiplome, Institut, MatriculeUpdate, nom, Prenom,ref + i);
//					//addDiplome(AllDiplomes,"P_GS_TAB_diplomes_Diplome",ref + i, MatriculeUpdate, Specialite, DateObtentionDiplom, TypeDiplome, Institut, nom, Prenom);
//					i++;
//				}else {
//					String IdDiplome = (String)AllDiplomes.getValue("P_GS_IdDiplome");
//					UpdateDiplome(AllDiplomes, "P_GS_TAB_diplomes_Diplome", Specialite, DateObtentionDiplom, TypeDiplome, Institut, IdDiplome, nom, Prenom,ref + i);
//					//UpdateDiplome( AllDiplomes, Specialite, DateObtentionDiplom, TypeDiplome, Institut , IdDiplome, nom, Prenom);
//
//				}
//
//			}
//		}
//
//		//Modifier les enfants 
//		removeEN("P_GS_TAB_Enfants", "P_GS_IdEnfant", MatriculeUpdate);
//		List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
//		if (Enfants.size() != 0)
//		{
//			int i = 100;
//
//			String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//
//			for (Iterator IT = Enfants.iterator(); IT.hasNext();)
//			{
//				ILinkedResource AllEnfants = (ILinkedResource) IT.next();
//				String Nom = (String) AllEnfants.getValue("P_GS_TAB_Enfant_Nom");
//				String Pernom = (String) AllEnfants.getValue("P_GS_TAB_Enfant_Prenom");
//				Date DateNaissance = (Date) AllEnfants.getValue("P_GS_TAB_Enfant_DateNa");
//				String NCMIM = (String) AllEnfants.getValue("P_GS_CMIM_Enfant");
//
//
//				if(AllEnfants.getValue("P_GS_IdEnfant")==null){
//					String refE = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//					addEnfant("ENF" + ref + i, Pernom, Nom, DateNaissance, NCMIM);
//					addPersConjEnfa(MatriculeUpdate, "ENF" + ref + i, CINconjoint);
//					i++;
//
//				}else{
//					updateEnfant(Nom, Pernom, DateNaissance, GetIdENF(MatriculeUpdate) );
//				}			}
//		}
//
//		//Modification des remuniration 
//
//		List Remuneration = (List) getWorkflowInstance().getLinkedResources("Tab_Remun");
//		if (Remuneration.size() != 0){
//
//			for (Iterator IT = Remuneration.iterator(); IT.hasNext();){
//				ILinkedResource AllRemun = (ILinkedResource) IT.next();
//				String StatutRemu = (String) AllRemun.getValue("Tab_statut");
//				float Tab_SBA = (float)AllRemun.getValue("Tab_Remun_SBA");
//				float Tab_SBM = (float)AllRemun.getValue("Tab_Remun_SBM");
//				float Tab_Bonus = (float)AllRemun.getValue("Tab_Remun_Bonus");
//				float Tab_Avantage = (float)AllRemun.getValue("Tab_Remun_Avantage");
//				float Tab_autres = (float)AllRemun.getValue("Tab_Autres");
//
//				Object annee1 = (Object)AllRemun.getValue("TAB_Rem_Anne");
//				float annee2 = Float.parseFloat(annee1.toString());
//				int annee = (int) annee2;
//				if(StatutRemu==(null)){
//					addRemuneration(Tab_SBA, Tab_SBM, Tab_Avantage, Tab_Bonus, Tab_autres, annee, MatriculeUpdate);
//				}
//
//
//			}
//
//
//		}
//
//
		return super.onAfterSubmit(action);
	}
}
