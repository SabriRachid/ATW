package com.attijari.GestionSalarie;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.turbine.services.servlet.TurbineServlet;

import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.structs.Period;

import dao.SingletonConnexionBDD;

public class CreationSalarie extends ConnexionBDD
{
	
	/**
	 * 
	 */
	
	
	
	
	public static Date dateDebutExp;
	public static Date dateFinExp;
	
	private static final long serialVersionUID = 1L;
	
	public void DeletTabl(String NomSys)
	{
		
		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(NomSys);
		getWorkflowInstance().deleteLinkedResources(assoc);
	}
	
	public String getFilile (String libelle){
		String Filiae = null ; 
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req= "select IdFiliale from Filiale where Libelle = ?  ";
			st =connection.prepareStatement(req);
			
			st.setString(1, libelle);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()){
				Filiae = rs.getString(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}

		return Filiae;
	
	}
	
	@SuppressWarnings("unchecked")
	public String copyFile(String sysname, String nom, String Prenom) throws IOException
	{
		try{
				instance = getWorkflowInstance();
		List<IAttachment> docfp = (List<IAttachment>) instance.getValue(sysname);
		String filename = "";
		if (docfp.size() > 0)
		{
			filename = nom + "_" + Prenom + ".jpg";
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
		
		// Image myImage = new ImageIcon("C:\\VDocPlatform\\ImageFicheSalarie" + "\\" + filename).getImage();
		//
		// myImage.getHeight(null);
		// myImage.getWidth(null);
		//
		//
		// rc.alert( "height is "+myImage.getHeight(null));
		// rc.alert("width is "+ myImage.getWidth(null));
		return  filename;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public String copy_PJDiplome(ILinkedResource test1, String sysname, String nom, String Prenom,String ref) throws IOException
	{
		try{
			ILinkedResource instance1 = test1;
		List<IAttachment> docfp = (List<IAttachment>) instance1.getValue(sysname);
		String filename = "";
		if (docfp.size() > 0)
		{
			filename = nom + "_ " + Prenom +ref+".pdf" ;
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
	//-------------------------------------------------------------------------
	// Methode pour ajouter personnel
	//-------------------------------------------------------------------------
	public void addPersonnel(String Matricule, String supMat, String IdPodte, String IdDirection, String nom, String prenom, Date DateN, String adresse, String Email, String Nationalite,
			Date dateEmbaucheGR, Date dateEmbaucheFil, String Photo, String SituationF, String NCNSS, String NCIMR, String NCMIM,String RIB, float SBA, float SBM, float bonus, float Avantage, int NbrEnfant,
			float NbreConge,float NbreCongDispo, float reliquatAnneeEnCour, String vdocLogin, boolean SexeMasculin, String Filiale, String LieuNaiss,String Profil, String sup2, boolean statut)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			
			String req = "insert into Personnel values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Valider',?,?,?,?,?,?)";
			st = connection.prepareStatement(req);
//			
			st.setString(1, Matricule);
			st.setString(2, supMat);
			st.setString(3, IdPodte);
			st.setString(4, IdDirection);
			st.setString(5, nom);
			st.setString(6, prenom);
			java.sql.Date DateNaiss = new java.sql.Date(DateN.getTime());
			st.setDate(7, DateNaiss);
			st.setString(8, adresse);
			st.setString(9, Email);
			st.setString(10, Nationalite);
			java.sql.Date datefil = new java.sql.Date(dateEmbaucheFil.getTime());
			java.sql.Date dateemb = new java.sql.Date(dateEmbaucheGR.getTime());
			st.setDate(11, dateemb);
			st.setDate(12, datefil);
			st.setString(13, copyFile("P_GS_Photo", nom, prenom));
			st.setString(14, SituationF);
			st.setString(15, NCNSS);
			st.setString(16, NCIMR);
			st.setString(17, NCMIM);
			st.setString(18, RIB);
			st.setFloat(19, SBA);
			st.setFloat(20, SBM);
			st.setFloat(21, bonus);
			st.setFloat(22, Avantage);
			st.setInt(23, NbrEnfant);
			st.setFloat(24, NbreConge);
			st.setFloat(25, NbreConge);
			st.setFloat(26, NbreCongDispo);
			st.setFloat(27, reliquatAnneeEnCour);
			st.setString(28, vdocLogin);
			st.setBoolean(29, SexeMasculin);
			st.setString(30, Filiale);
			st.setString(31, LieuNaiss);
			st.setString(32, Profil);
			st.setString(33,sup2);
			st.setBoolean(34,statut);
			
			
			st.executeUpdate();
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------
	//methode pour ajouter un utilisateur dans l'annuaire 
	//-------------------------------------------------------------------------
	public void addUser(String email,String matricule,String prenom,String nom,String loginVdoc, String supMatr, String manager){
		try {
			connection = ConnectionDefinition("Vdoc").getConnection();
			String req = "INSERT INTO vdoc_user VALUES(17,(select 'Organization:1/User:' + convert(varchar(255),MAX(id+1)) from vdoc_user),"
					+ "? ,NULL ,NULL,NULL ,? ,1,"
					+ "NULL ,NULL ,NULL,NULL ,NULL,'fr'  ,1  ,'CDI',NULL ,? ,"
					+ "? ,NULL ,NULL ,NULL ,NULL,?  ,? ,"
					+ "(select MAX(jdoId+1)  from dbo.vdoc_user),NULL  ,?,? ,"
					+ "? ,'0',?,NULL ,NULL,NULL,'ugE0COa4CsY0lPmk+kfSa0+DwSY=' ,"
					+ "NULL ,NULL,NULL,NULL ,'Skin Attijari Finance & corp',NULL,NULL ,NULL,NULL,NULL,1,1,NULL,NULL,"
					+ "NULL ,NULL,(select id from dbo.vdoc_user where employeeNumber=?),1,"
					+ "(select id from dbo.vdoc_user where employeeNumber=?),1)";
			
			st = connection.prepareStatement(req);
			Date dateC = new Date();
			java.sql.Date dateS = new java.sql.Date (dateC.getTime());
			st.setDate(1, dateS);
			st.setDate(2, dateS);
			st.setString(3, email);
			st.setString(4, matricule);
			st.setString(5,prenom);
			st.setString(6, nom+" "+prenom);
			st.setString(7, nom);
			st.setDate(8, dateS);
			st.setDate(9, dateS);
			st.setString(10, loginVdoc);
			st.setString(11, supMatr);
			st.setString(12, manager);
			st.executeUpdate();
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	//-------------------------------------------------------------------------
	//methde pour ajouter l'utilisateur dans un groupe de VDoc
	//-------------------------------------------------------------------------
	public void addUserGroup(String Profil){
		
		int managers = 0;
		int associate = 0;
		int siniorassociate = 0;
		try
		{
			connection = ConnectionDefinition("Vdoc").getConnection();
			String req = "select id from dbo.vdoc_group where name = 'Managers' or name='Associates' or name='SiniorsAssociates' order by id";
			st = connection.prepareStatement(req);
			ResultSet rs1 = st.executeQuery();
			int i=0;
			while (rs1.next()){
				if(i==0){
					managers = rs1.getInt(1);
					i++;
				}else if(i==1){
					associate = rs1.getInt(1);
					i++;
				}else if(i==2){
					siniorassociate = rs1.getInt(1);
					i++;
				}
				
				
				
				
			}
			
			
			String req1 = "insert into vdoc_user_vdoc_group values((select MAX(id) from dbo.vdoc_user),?)";
			st = connection.prepareStatement(req1);
			if(Profil.equals("ASSOCIATE")){
				st.setInt(1, associate);
			}else if(Profil.equals("MANAGER")){
				st.setInt(1, managers);
			}else{
				st.setInt(1, siniorassociate);
			}
			
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	//-------------------------------------------------------------------------
	// methode pour supprimer le personnel
	//-------------------------------------------------------------------------
	public void deletePersonnel(String matr)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Personnel set Etat='Supprimer' where matricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, matr);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour inserer un poste dans la base de donner
	//-------------------------------------------------------------------------
	public void addPoste(String Libelle, String Description)
	{
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Personnel values(?,?)";
			st = connection.prepareStatement(req);
			st.setString(1, Libelle);
			st.setString(2, Description);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------
	// methode pour insrer la carte national dans la base de donnees
	//-------------------------------------------------------------------------
	public void CarteNational(String CIN, Date dateExpir, String matricule)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into CarteNational values(?,?,?)";
			st = connection.prepareStatement(req);
			st.setString(1, CIN);
			java.sql.Date DateEx = new java.sql.Date(dateExpir.getTime());
			st.setDate(2, DateEx);
			st.setString(3, matricule);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------
	//methode pour ajouter Les Remuniration
	//-------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public void AddRemuniriation (float SBA, float SBM, float Bonus, float Avantage, float autres, String Matricule){
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Remuneration (SBA,SBM,Bonus,Avantage,autres,anneeRemunetation,Personnelmatricule) values(?,?,?,?,?,?,?)";
			st = connection.prepareStatement(req);
			st.setFloat(1, SBA);
			st.setFloat(2, SBM);
			st.setFloat(3, Bonus);
			st.setFloat(4, Avantage);
			st.setFloat(5, autres);
			Date date = new Date ();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int annee = c.get(Calendar.YEAR);
			st.setInt(6,annee );
			st.setString(7, Matricule);
			
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	//-------------------------------------------------------------------------
	// methode pour supprimer un poste
	//-------------------------------------------------------------------------
	public void deletePoste(String CIN)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "delete from CarteNational where CIN = ?";
			st = connection.prepareStatement(req);
			st.setString(1, CIN);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour inserer une experience dans la base de donnees
	//-------------------------------------------------------------------------
	public void addExperience(String IdExper, String MatriculePerso, Date dateD, Date dateF, String sociétéAcceuil, String Mission, String TypeExper, String Profil)
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
	//-------------------------------------------------------------------------
	// methode pour supprimer un experience
	//-------------------------------------------------------------------------
	public void deleteExperience(String Personnelmatricule)
	{
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "delete from Experience where Personnelmatricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, Personnelmatricule);
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// Methode pour modifier les experiences
	//-------------------------------------------------------------------------
	public void updateExperiences(String IdExperience, Date dateD, Date dateF, String sociétéAcceuil, String Mission, String TypeExper, String Profil)
	{
		try
		{
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
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour inserer un diplome dans la base de donnes
	//-------------------------------------------------------------------------
	public void addDiplome(ILinkedResource test, String NSystem, String IdDiplome, String MatriculePerso, String specialité, Date dateObtention, String typeDiplome, String Institut, String nom,String Prenom, String ref)
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
	//-------------------------------------------------------------------------
	// methode pour modifier les diplomes
	//-------------------------------------------------------------------------
	public void UpdateDiplome(String specialité, Date dateObtention, String typeDiplome, String Institut, String IdDiplome)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Diplome set Specialite = ?,DateObtention =?, TypeDiplome = ?,Institut = ? where IdDiplome = ?";
			st = connection.prepareStatement(req);
			
			st.setString(1, specialité);
			java.sql.Date DateDiplome = new java.sql.Date(dateObtention.getTime());
			st.setDate(2, DateDiplome);
			st.setString(3, typeDiplome);
			st.setString(4, Institut);
			st.setString(5, IdDiplome);
			
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour ajouter un passeport
	//-------------------------------------------------------------------------
	public void addPassePort(String NumPasse, Date dateExpir, String MatriculePerso)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Passeport values(?,?,?)";
			st = connection.prepareStatement(req);
			
			st.setString(1, NumPasse);
			java.sql.Date DateEx = new java.sql.Date(dateExpir.getTime());
			st.setDate(2, DateEx);
			st.setString(3, MatriculePerso);
			
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------
	// methode pour ajouter un conjoint
	//-------------------------------------------------------------------------
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
	//-------------------------------------------------------------------------
	// methode pour ajouter un enfant
	//-------------------------------------------------------------------------
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
	//-------------------------------------------------------------------------
	// methode pour Ajouter le telephone
	//-------------------------------------------------------------------------
	public void addTelephone(String tel1, String tel2, String telUrgences, String telContactUrgence, String MatriculePerso)
	{
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "insert into Telephone(Tel1,Tel2,TelUrgences,TelContactUrgence,Personnelmatricule) values(?,?,?,?,?)";
			st = connection.prepareStatement(req);
			
			st.setString(1, tel1);
			st.setString(2, tel2);
			st.setString(3, telUrgences);
			st.setString(4, telContactUrgence);
			st.setString(5, MatriculePerso);
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour modifier les informations personnel
	//-------------------------------------------------------------------------
	public void updatePersonnel(String Matricule, String IdPodte, String IdDirection, String nom, String prenom, Date DateN, String adresse, String Email, String Nationalite, Date dateEmbaucheGR,
			Date dateEmbaucheFil, String Photo, String SituationF, String NCNSS, String NCIMR, String NCMIM, float SBA, float SBM, float bonus, float Avantage, float NbrEnfant, float NbreConge,
			float congeDispo)
	{
		
		try
		{
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			
			String req = "update Personnel set PosteIdPoste= ?,DirectionIdDirection= ?, Nom=?, Prenom=?, DateNaissance=?, "
					+ "Adresse=?, Email= ?, Nationalite=? , DateEmbaucheGroupe= ?, DateEmbaucheFiliale= ?,Photo= ?,SituationFamiliale= ?,"
					+ "NCNSS=?, NCIMR= ?,NCMIM= ?,SBA=?,SBM=?,Bonus= ?, Avantage= ?,NombreEnfant= ?,NombreJoursConge= ?,NombrJoursDispo= ? where Matricule = ? ";
			st = connection.prepareStatement(req);
			
			// st.setString(1, Matricule);
			// st.setString(2, supMat);
			st.setString(1, IdPodte);
			st.setString(2, IdDirection);
			st.setString(3, nom);
			st.setString(4, prenom);
			java.sql.Date DateNaiss = new java.sql.Date(DateN.getTime());
			st.setDate(5, DateNaiss);
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
			st.setString(23, Matricule);
			// st.setString(26, vdocLogin);
			// st.setBoolean(24, SexeMasculin);
			
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------
	// Methode pour modifier les telephones
	//-------------------------------------------------------------------------
	public void updateTelephone(String Tel1, String Tel2, String TelUrgences, String PersonneUrgence, String MatriculePerso)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Telephone set Tel1=?, Tel2=?, TelUrgences=?,TelContactUrgence=? where Personnelmatricule = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, Tel1);
			st.setString(2, Tel2);
			st.setString(3, TelUrgences);
			st.setString(4, PersonneUrgence);
			st.setString(5, MatriculePerso);
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour Modifier la carte national
	//-------------------------------------------------------------------------
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
	//-------------------------------------------------------------------------
	// methode pour modifier le passeport
	//-------------------------------------------------------------------------
	public void updatePassePort(Date dateExperation, String MatriculePerso)
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
	//-------------------------------------------------------------------------
	// methode pour modifier conjoint
	//-------------------------------------------------------------------------
	public void updateConjoint(String CIN, String nom, String prenom, Date dateN, String MatriculePerso)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Conjoint set Cin=?, Nom=?, Prenom= ?, DateNaissance=?  where personnelMatricule = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, CIN);
			st.setString(2, nom);
			st.setString(3, prenom);
			java.sql.Date DateNaiss = new java.sql.Date(dateN.getTime());
			st.setDate(4, DateNaiss);
			st.setString(5, MatriculePerso);
			
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode selectionner l'id d'un enfant
	//-------------------------------------------------------------------------
	public String getIdEnfant(String NomEnf)
	{
		
		String IdEnfant = null;
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select IdEnfant from Enfant where Nom = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, NomEnf);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				
				IdEnfant = rs.getString(1);
				
			}
			
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
		
		return IdEnfant;
	}
	//-------------------------------------------------------------------------
	// methode pour modifier un enfant
	//-------------------------------------------------------------------------
	public void updateEnfant(String Nom, String Prenom, Date dateN, String IdEnfant)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "update Enfant set Nom=?, Prenom= ?, Datenaissance=?  where IdEnfant = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, Nom);
			st.setString(2, Prenom);
			java.sql.Date DateNaissance = new java.sql.Date(dateN.getTime());
			st.setDate(3, DateNaissance);
			st.setString(4, IdEnfant);
			
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	//-------------------------------------------------------------------------
	// methode pour ajouter dans la table PersConjEnf
	//-------------------------------------------------------------------------
	public void addPersConjEnfa(String Matricule, String IdEnfant, String CinConjoint)
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
	//-------------------------------------------------------------------------
	// methode pour selectionner l'id d'un poste
	//-------------------------------------------------------------------------
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
	//-------------------------------------------------------------------------
	// methode pour selectionner l'id d'une direction
	//-------------------------------------------------------------------------
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
	
	public boolean ExisteCIN (String CIN){
		
		//String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from CarteNational where CIN = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, CIN);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean ExistCMIMAdherent(String CMIMADH){
		
		//String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
				int cpt = 0;
				try{
					connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
					String req1 = "select count(*) as cpt from Personnel where NCMIM = ? ";
					st=connection.prepareStatement(req1);
					st.setString(1, CMIMADH);
					ResultSet rs1= st.executeQuery();
					while (rs1.next()){
						cpt = rs1.getInt(1);
					}
					
					if(cpt>0)
						return false;
					
				}catch(Exception e){
					e.printStackTrace();
				}
				return true;
	}
	
	public boolean ExistCMIMEnfant(String CMIMENF){
		//String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from Enfant where NCMIM = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, CMIMENF);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
		
	}
	
	public boolean ExistCMIMCNJT(String CMIMCNJT){
		
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from Conjoint where NCMIM = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, CMIMCNJT);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
		
	}
	
	public boolean ExistPassPort(String Passport){
		
		
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from Passeport where NumPassport = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, Passport);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean ExistMatricule(String Matricule){
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from Personnel where matricule = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, Matricule);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean ExistCNSS(String NCNSS)
	{int cpt = 0;
	try{
		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String req1 = "select count(*) as cpt from Personnel where NCNSS = ? ";
		st=connection.prepareStatement(req1);
		st.setString(1, NCNSS);
		ResultSet rs1= st.executeQuery();
		while (rs1.next()){
			cpt = rs1.getInt(1);
		}
		
		if(cpt>0)
			return false;
		
	}catch(Exception e){
		e.printStackTrace();
	}
	return true;
		
	}
	
	public boolean ExistCIMR(String CIMR){
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from Personnel where NCNSS = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, CIMR);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean ExistLogin(String LoginVdoc){
		int cpt = 0;
		try{
			connection = ConnectionDefinition("Vdoc").getConnection();
			String req1 = "select count(*) as cpt from vdoc_user where login = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, LoginVdoc);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		return true;
	}
	
	public boolean ExistRIB(String RIB){
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from Personnel where RIB = ? ";
			st=connection.prepareStatement(req1);
			st.setString(1, RIB);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	
	}
	
	@Override
	public boolean onAfterSubmit(IAction action) {
		String Matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
		String Action = (String) getWorkflowInstance().getValue("P_GS_Action");
		String StatutCollaborateur = (String) getWorkflowInstance().getValue("P_GS_Statut");
		
		
		 if (StatutCollaborateur.equals("Non active"))
		{
			
			//String nomPerso = (String) getWorkflowInstance().getValue("P_GS_ListeS");
			
			deletePersonnel(Matricule);
			
		}
	 if(action.getName().equals("Valider")){
			try
			{
				List<IAttachment> photo = (List<IAttachment>) getWorkflowInstance().getValue("P_GS_Photo");
				String Picture = null;
				if (!photo.isEmpty())
				{
					Picture = photo.get(0).getShortName();
				}
				
				String sexe = (String) getWorkflowInstance().getValue("P_GS_Sexe");
				Matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
				String nom = (String) getWorkflowInstance().getValue("P_GS_Nom");
				String Prenom = (String) getWorkflowInstance().getValue("P_GS_Prenom");
				String adresse = (String) getWorkflowInstance().getValue("P_GS_Adrs_Perso");
				Date DateN = (Date) getWorkflowInstance().getValue("DateDeNaissance");
				String nationalite = (String) getWorkflowInstance().getValue("P_GS_ListeNationalite");
				String CIN = (String) getWorkflowInstance().getValue("P_GS_CIN");
				String CNSS = (String) getWorkflowInstance().getValue("P_GS_CNSS");
				String Tel1 = (String) getWorkflowInstance().getValue("P_GS_tel1");
				String Tel2 = (String) getWorkflowInstance().getValue("P_GS_Tel2");
				String PassePort = (String) getWorkflowInstance().getValue("P_GS_passePort");
				String STFamilliale = (String) getWorkflowInstance().getValue("P_GS_S_LFamilliale");
				String Poste = (String) getWorkflowInstance().getValue("P_GS_Poste");
				String DescPoste = (String) getWorkflowInstance().getValue("GS_P_Descriptif_poste");
				String DirectionRatt = (String) getWorkflowInstance().getValue("P_GS_Direction");
				String Mail = (String) getWorkflowInstance().getValue("P_GS_Mail");
				Date dateEmbGr = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_filiale");
				Date dateEmbFl = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_groupe");
				String nomConjoint = (String) getWorkflowInstance().getValue("P_GS_Nom_Conjoint");
				Date DateNConjoint = (Date) getWorkflowInstance().getValue("P_GS_DDN_Conjoint");
				int nbreEnfant1 = (int) getWorkflowInstance().getValue("P_GS_Nbre_Enfants");
			//	float nbreEnfant = Float.parseFloat(nbreEnfant1+"");
				String PrenomEnfant = (String) getWorkflowInstance().getValue("P_GS_Prenom_Enfants");
			//	String CMIM_Enfant = (String) getWorkflowInstance().getValue("P_GS_CMIM_Enfant");
				Date DateNEnfant = (Date) getWorkflowInstance().getValue("P_GS_DDN_Enfants");
				Date dateExpCIN = (Date) getWorkflowInstance().getValue("P_GS_date_expr_CIN");
				String CMIM = (String) getWorkflowInstance().getValue("P_GS_CMIM");
				Date dateExpPassePort = (Date) getWorkflowInstance().getValue("P_GS_date_Exper_passeport");
				String CIMR = (String) getWorkflowInstance().getValue("P_GS_CIMR");
				String TelCasURgence = (String) getWorkflowInstance().getValue("P_GS_Tel_Urgence");
				String PersonneUrgence = (String) getWorkflowInstance().getValue("P_GS_Personne_urgence");
				
				Object Avantage1 = (Object) getWorkflowInstance().getValue("P_GS_Avantage");
				float Avantage = Float.parseFloat(Avantage1.toString());

				Object SBA1 = (Object) getWorkflowInstance().getValue("P_GS_SBA");
				float SBA = Float.parseFloat(SBA1.toString());

				Object Bonus1 = (Object) getWorkflowInstance().getValue("P_GS_Bonus");
				float Bonus = Float.parseFloat(Bonus1.toString());

				Object SBM1 = (Object) getWorkflowInstance().getValue("P_GS_SBM");
				float SBM = Float.parseFloat(SBM1.toString());
				
				Object AutresRem = (Object) getWorkflowInstance().getValue("P_GS_AutresRemunration");
				float AutresRemun = Float.parseFloat(AutresRem.toString());
				
				
//				float Avantage = (float) getWorkflowInstance().getValue("P_GS_Avantage");
//				float SBA = (float) getWorkflowInstance().getValue("P_GS_SBA");
//				float Bonus = (float) getWorkflowInstance().getValue("P_GS_Bonus");
//				float SBM = (float) getWorkflowInstance().getValue("P_GS_SBM");
//				float AutresRemun = (float) getWorkflowInstance().getValue("P_GS_AutresRemunration");
				String CMIM_Conjoint = (String) getWorkflowInstance().getValue("P_GS_CMIM_Conjoint");
				// Date DateObtentionDiplom = (Date)getWorkflowInstance().getValue("P_GS_TAB_diplomes_dateObte");
				// String Speciaite =(String)getWorkflowInstance().getValue("P_GS_TAB_diplomes_Specialte");
				// String TypeDiplome =(String)getWorkflowInstance().getValue("P_GS_TAB_diplomes_Type");
				// String PExperience = (String)getWorkflowInstance().getValue("P_GS_TAB_Experience_periode");
				// String SocieteAccueil =(String)getWorkflowInstance().getValue("P_GS_TAB_Experience_Societe");
				// String Tache =(String)getWorkflowInstance().getValue("P_GS_TAB_Experience_Taches");
				String Suprieur = (String) getWorkflowInstance().getValue("P_GS_Sup");
				float NbreConge = (float) getWorkflowInstance().getValue("P_GS_nbreConge");
				String VdocLogin = (String) getWorkflowInstance().getValue("P_GS_VdocLogin");
				String CINconjoint = (String) getWorkflowInstance().getValue("P_GS_CINConjoint");
				String Prenomconjoint = (String) getWorkflowInstance().getValue("P_GS_PrenomConjoint");
				String libelle_Filiale = (String) getWorkflowInstance().getValue("P_GS_Filiale");
				String Lieu_Naiss = (String) getWorkflowInstance().getValue("P_GS_LieuNaiss");
				String RIB = (String) getWorkflowInstance().getValue("P_GS_RIB");
				String Profiluser= (String) getWorkflowInstance().getValue("P_GS_ProfilUser");
				String Manager = (String) getWorkflowInstance().getValue("P_GS_Managers");
				String Statut = (String) getWorkflowInstance().getValue("P_GS_Statut");
				
				boolean StatutColabborateur;
				if("Active".equals(Statut)){
					StatutColabborateur = true;
				}else{
					StatutColabborateur = false;
				}
				
				boolean sexeM;
				
				if ("Féminin".equals(sexe))
				{
					sexeM = false;
				}
				else
				{
					sexeM = true;
				}
				
				
				// sourceController().alert(Action);
			
					char PremierChar = Prenom.charAt(0);
					String LoginVDOC = Prenom+"."+nom;
					
					
					// String seklaf = selectIdPoste(Poste);
					
					addPersonnel(Matricule, Suprieur, selectIdPoste(Poste), selectIdDirection(DirectionRatt), nom, Prenom, DateN, adresse, Mail, nationalite, dateEmbGr, dateEmbFl, Picture,
							STFamilliale, CNSS, CIMR, CMIM, RIB,SBA, SBM, Bonus, Avantage, nbreEnfant1, NbreConge, NbreConge, NbreConge,VdocLogin, sexeM, getFilile(libelle_Filiale),Lieu_Naiss,Profiluser,Manager,StatutColabborateur);
				
//					addUser(Mail, Matricule, Prenom, nom, VdocLogin, Suprieur, Manager);
//					if(libelle_Filiale.equals("Attijari Finances Corp.")){
//						addUserGroup(Profiluser);
//					}
//				
					
					addTelephone(Tel1, Tel2, TelCasURgence, PersonneUrgence, Matricule);
					
					AddRemuniriation(SBA, SBM, Bonus, Avantage, AutresRemun,  Matricule);
					
					
					
						CarteNational(CIN, dateExpCIN, Matricule);
						
				
					
					
					
					addPassePort(PassePort, dateExpPassePort, Matricule);
					
					if(STFamilliale.equals("Marié")){
						addConjoint(CINconjoint, nomConjoint, Prenomconjoint, DateNConjoint, Matricule, CMIM_Conjoint);

					}
					
										
					// l'ajout des experiences
					
					List assoc = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
					
					if (assoc.size() != 0)
					{
						int i = 10;
						String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
						for (Iterator IT = assoc.iterator(); IT.hasNext();)
						{
							ILinkedResource associa = (ILinkedResource) IT.next();
							com.axemble.vdoc.sdk.structs.Period periode = (Period) associa.getValue("P_GS_TAB_Experience_periode");
							dateDebutExp = periode.getStartDate();
							dateFinExp = periode.getEndDate();
							String SocieteAcceuil = (String) associa.getValue("P_GS_TAB_Experience_Societe");
							String TypeExperience = (String) associa.getValue("P_GS_TAB_Experience_Type");
							String Profil = (String) associa.getValue("P_GS_TAB_Experience_Profil");
							String Mission = (String) associa.getValue("P_GS_TAB_Experience_Taches");
							
							// sourceController().alert(dateDebutExp + " " + dateFinExp);
							addExperience("Experience_" + ref + i, Matricule, dateDebutExp, dateFinExp, SocieteAcceuil, Mission, TypeExperience, Profil);
							i++;
						}
					}
					
					// l'ajout des diplomes
					
					List Diplomes = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
					if (Diplomes.size() != 0)
					{
						int i = 100;
						String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
						for (Iterator IT = Diplomes.iterator(); IT.hasNext();)
						{
							ILinkedResource AllDiplomes = (ILinkedResource) IT.next();
							String Specialite = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Specialte");
							Date DateObtentionDiplom = (Date) AllDiplomes.getValue("P_GS_TAB_diplomes_dateObte");
							String TypeDiplome = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Type");
							String Institut = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Institut");
					
							
							addDiplome(AllDiplomes,"P_GS_TAB_diplomes_Diplome",ref + i, Matricule, Specialite, DateObtentionDiplom, TypeDiplome, Institut, nom, Prenom,ref + i);
							i++;
						}
					}
					
					// l'ajout des enfants
					List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
					if (Enfants.size() != 0)
					{
						int i = 0;
						
						String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
						
						for (Iterator IT = Enfants.iterator(); IT.hasNext();)
						{
							ILinkedResource AllEnfants = (ILinkedResource) IT.next();
							String Nom = (String) AllEnfants.getValue("P_GS_TAB_Enfant_Nom");
							String Pernom = (String) AllEnfants.getValue("P_GS_TAB_Enfant_Prenom");
							Date DateNaissance = (Date) AllEnfants.getValue("P_GS_TAB_Enfant_DateNa");
							String CMIM_Enfant = (String) AllEnfants.getValue("P_GS_CMIM_Enfant");
						
							
							addEnfant("ENF" + ref + i, Pernom, Nom, DateNaissance, CMIM_Enfant);
							addPersConjEnfa(Matricule, "ENF" + ref + i, CINconjoint);
							i++;
							
						}
					}
					
				
				}catch(Exception e)
				{
					e.printStackTrace();
				}}
		
		
		return super.onAfterSubmit(action);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		String Matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
		String PassePort = (String) getWorkflowInstance().getValue("P_GS_passePort");
		String CIN = (String) getWorkflowInstance().getValue("P_GS_CIN");
		String CMIMADH = (String) getWorkflowInstance().getValue("P_GS_CMIM");
//		String CMIMENF = (String) getWorkflowInstance().getValue("");
		String CMIMCNJT = (String) getWorkflowInstance().getValue("P_GS_CMIM_Conjoint");
		String CNSS = (String) getWorkflowInstance().getValue("P_GS_CNSS");
		String CIMR = (String) getWorkflowInstance().getValue("P_GS_CIMR");
		String RIB = (String) getWorkflowInstance().getValue("P_GS_RIB");
		
		if(!ExisteCIN(CIN)){
			getResourceController().alert("Le Numéro CIN que vous avez saisi existe déjà");
			return false;		}
		
		if(!ExistPassPort(PassePort)){
			getResourceController().alert("Le Numéro de Passeport que vous avez saisi existe déjà");
			return false;		}
		
		
		if(! ExistMatricule(Matricule)){
			getResourceController().alert("Le matirucle que vous avez saisi existe déjà");
			return false;		}
		
		if(! ExistCMIMAdherent(CMIMADH)){
			getResourceController().alert("Le Numéro de CMIM que vous avez saisi existe déjà");
			return false;		}
		
		if(! ExistCIMR(CIMR)){
			getResourceController().alert("Le Numéro de CIMR que vous avez saisi existe déjà");
			return false;		}
		
		if(! ExistCNSS(CNSS)){
			getResourceController().alert("Le Numéro de CNSS que vous avez saisi existe déjà");
			return false;		}
		
		if(! ExistRIB(RIB)){
			getResourceController().alert("Le RIB que vous avez saisi existe déjà");
			return false;		}

		if(! ExistCMIMCNJT(CMIMCNJT)){
			getResourceController().alert("Le Numéro de CMIM du conjoint que vous avez saisi existe déjà");
			return false;		}
		
		
		List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
		if (Enfants.size() != 0){
			for (Iterator IT = Enfants.iterator(); IT.hasNext();)	{
				ILinkedResource AllEnfants = (ILinkedResource) IT.next();
				String CMIMENF = (String) AllEnfants.getValue("P_GS_CMIM_Enfant");
				
				if(! ExistCMIMEnfant(CMIMENF)){
					getResourceController().alert("Le Numéro de CMIM  de l'enfant que vous avez saisi existe déjà");
					return false;				}
							}
		}
		
//				else if (Action.equals("Modifier"))
//				{
//					
//					String MatriculUpdate = (String) getWorkflowInstance().getValue("P_GS_MatriculeUpdate");
//					
//					updatePersonnel(MatriculUpdate, selectIdPoste(Poste), selectIdDirection(DirectionRatt), nom, Prenom, DateN, adresse, Mail, nationalite, dateEmbGr, dateEmbFl, Picture,
//							STFamilliale, CNSS, CIMR, CMIM, SBA, SBM, Bonus, Avantage, nbreEnfant, NbreConge, NbreConge);
//					
//					updateCarteNational(dateExpCIN, MatriculUpdate);
//					
//					updateTelephone(Tel1, Tel2, TelCasURgence, PersonneUrgence, MatriculUpdate);
//					
//					updatePassePort(dateExpPassePort, MatriculUpdate);
//					
//					updateConjoint(CINconjoint, nomConjoint, Prenomconjoint, DateNConjoint, MatriculUpdate);
//					
//					// la modification des experiences
//					List assoc = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
//					if (assoc.size() != 0)
//					{
//						
//						for (Iterator IT = assoc.iterator(); IT.hasNext();)
//						{
//							
//							ILinkedResource associa = (ILinkedResource) IT.next();
//							com.axemble.vdoc.sdk.structs.Period periode = (Period) associa.getValue("P_GS_TAB_Experience_periode");
//							dateDebutExp = periode.getStartDate();
//							dateFinExp = periode.getEndDate();
//							String SocieteAcceuil = (String) associa.getValue("P_GS_TAB_Experience_Societe");
//							String TypeExperience = (String) associa.getValue("P_GS_TAB_Experience_Type");
//							String Profil = (String) associa.getValue("P_GS_TAB_Experience_Profil");
//							String Mission = (String) associa.getValue("P_GS_TAB_Experience_Taches");
//							if (associa.getValue("P_GS_IdExperience") == null)
//							{
//								int i = 100;
//								String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//								addExperience("Experience_" + ref + i, MatriculUpdate, dateDebutExp, dateFinExp, SocieteAcceuil, Mission, TypeExperience, Profil);
//								i++;
//								// //sourceController().alert(dateDebutExp + " " + dateFinExp);
//							}
//							else
//							{
//								String IdExperience = (String) associa.getValue("P_GS_IdExperience");
//								updateExperiences(IdExperience, dateDebutExp, dateFinExp, SocieteAcceuil, Mission, TypeExperience, Profil);
//							}
//						}
//					}
//					
//					// Modifier Les diplomes
//					
//					List Diplomes = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
//					if (Diplomes.size() != 0)
//					{
//						int i = 100;
//						String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//						for (Iterator IT = Diplomes.iterator(); IT.hasNext();)
//						{
//							ILinkedResource AllDiplomes = (ILinkedResource) IT.next();
//							String Specialite = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Specialte");
//							Date DateObtentionDiplom = (Date) AllDiplomes.getValue("P_GS_TAB_diplomes_dateObte");
//							String TypeDiplome = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Type");
//							String Institut = (String) AllDiplomes.getValue("P_GS_TAB_diplomes_Institut");
//							
//							if (AllDiplomes.getValue("P_GS_IdDiplome") == null)
//							{
//								String refD = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//								addDiplome("Diplomes_" + refD + i, MatriculUpdate, Specialite, DateObtentionDiplom, TypeDiplome, Institut);
//								i++;
//							}
//							else
//							{
//								String IdDiplome = (String) AllDiplomes.getValue("P_GS_IdDiplome");
//								UpdateDiplome(Specialite, DateObtentionDiplom, TypeDiplome, Institut, IdDiplome);
//								
//							}
//							
//						}
//					}
//					
//					// Modifier les enfants
//					
//					List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
//					if (Enfants.size() != 0)
//					{
//						int i = 100;
//						
//						String ref = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//						
//						for (Iterator IT = Enfants.iterator(); IT.hasNext();)
//						{
//							ILinkedResource AllEnfants = (ILinkedResource) IT.next();
//							String Nom = (String) AllEnfants.getValue("P_GS_TAB_Enfant_Nom");
//							String Pernom = (String) AllEnfants.getValue("P_GS_TAB_Enfant_Prenom");
//							Date DateNaissance = (Date) AllEnfants.getValue("P_GS_TAB_Enfant_DateNa");
//							String CMIM_Enfant = (String) AllEnfants.getValue("P_GS_CMIM_Enfant");
//							
//							if (AllEnfants.getValue("P_GS_IdEnfant") == null)
//							{
//								String refE = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
//								addEnfant("ENF" + ref + i, Pernom, Nom, DateNaissance, CMIM_Enfant);
//								addPersConjEnfa(MatriculUpdate, "ENF" + ref + i, CINconjoint);
//								i++;
//							}
//							else
//							{
//								updateEnfant(Nom, Pernom, DateNaissance, GetIdENF(MatriculUpdate));
//							}
//						}
//					}
//					
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
		
		
		return super.onBeforeSubmit(action);
	}
	// Vider les champs
	public void Vider()
	{
		
		getWorkflowInstance().setValue("P_GS_Sexe", null);
		getWorkflowInstance().setValue("P_GS_Matricule", null);
		getWorkflowInstance().setValue("P_GS_Nom", null);
		getWorkflowInstance().setValue("P_GS_Prenom", null);
		getWorkflowInstance().setValue("P_GS_Adrs_Perso", null);
		getWorkflowInstance().setValue("DateDeNaissance", null);
		getWorkflowInstance().setValue("P_GS_Nationalite", null);
		getWorkflowInstance().setValue("P_GS_CIN", null);
		getWorkflowInstance().setValue("P_GS_CNSS", null);
		getWorkflowInstance().setValue("P_GS_tel1", null);
		getWorkflowInstance().setValue("P_GS_Tel2", null);
		getWorkflowInstance().setValue("P_GS_passePort", null);
		getWorkflowInstance().setValue("P_GS_S_LFamilliale", null);
		getWorkflowInstance().setValue("P_GS_Poste", null);
		getWorkflowInstance().setValue("GS_P_Descriptif_poste", null);
		getWorkflowInstance().setValue("P_GS_Direction", null);
		getWorkflowInstance().setValue("P_GS_Mail", null);
		getWorkflowInstance().setValue("P_GS_D_embuche_filiale", null);
		getWorkflowInstance().setValue("P_GS_D_embuche_groupe", null);
		getWorkflowInstance().setValue("P_GS_Nom_Conjoint", null);
		getWorkflowInstance().setValue("P_GS_DDN_Conjoint", null);
		// getWorkflowInstance().setValue("P_GS_Nbre_Enfants", null);
		getWorkflowInstance().setValue("P_GS_Prenom_Enfants", null);
		getWorkflowInstance().setValue("P_GS_DDN_Enfants", null);
		getWorkflowInstance().setValue("P_GS_date_expr_CIN", null);
		getWorkflowInstance().setValue("P_GS_CMIM", null);
		getWorkflowInstance().setValue("P_GS_date_Exper_passeport", null);
		getWorkflowInstance().setValue("P_GS_CIMR", null);
		getWorkflowInstance().setValue("P_GS_Tel_Urgence", null);
		getWorkflowInstance().setValue("P_GS_Personne_urgence", null);
		getWorkflowInstance().setValue("P_GS_Avantage", null);
		getWorkflowInstance().setValue("P_GS_SBA", null);
		getWorkflowInstance().setValue("P_GS_Bonus", null);
		getWorkflowInstance().setValue("P_GS_SBM", null);
		getWorkflowInstance().setValue("P_GS_Sup", null);
		// getWorkflowInstance().setValue("P_GS_nbreConge", null);
		getWorkflowInstance().setValue("P_GS_VdocLogin", null);
		getWorkflowInstance().setValue("P_GS_CINConjoint", null);
		getWorkflowInstance().setValue("P_GS_PrenomConjoint", null);
		
		List Diplomes = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
		getWorkflowInstance().deleteLinkedResources(Diplomes);
		
		List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
		getWorkflowInstance().deleteLinkedResources(Enfants);
		
		List Experiences = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
		getWorkflowInstance().deleteLinkedResources(Experiences);
		
	}
	
	public String GetIdENF(String Matricule)
	{
		String IdEnfant = null;
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "  select EnfantIdEnfant from PersConjEnfa where Personnelmatricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, Matricule);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				IdEnfant = rs.getString(1);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return IdEnfant;
	}
	
	public String getMatriculeUpdate(String NomPrenom)
	{
		String MatriculeUpdate = null;
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select matricule from Personnel where Nom+' '+Prenom= ? ";
			st = connection.prepareStatement(req);
			st.setString(1, NomPrenom);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				MatriculeUpdate = rs.getString(1);
				
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return MatriculeUpdate;
	}
	
	public void remplir()
	{
		try
		{
			
			// Select personne
			
			getWorkflowInstance().setValue("P_GS_Photo", null);
			getWorkflowInstance().setValue("P_GS_MatriculeUpdate", null);
			String MatriculeUpdate = getMatriculeUpdate((String) getWorkflowInstance().getValue("P_GS_ListeS"));
			getWorkflowInstance().setValue("P_GS_MatriculeUpdate", MatriculeUpdate);
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select Nom, Prenom, dateNaissance, Adresse, Email, Nationalite, DateEmbaucheGroupe, DateEmbaucheFiliale,"
					+ " Photo, SituationFamiliale, NCNSS, NCIMR, NCMIM, SBA, SBM, Bonus, Avantage," + " NombreEnfant, NombreJoursConge," + " NombrJoursDispo, vdocLogin, sexeMasculin from "
					+ "Personnel where matricule = ?";
			st = connection.prepareStatement(req);
			st.setString(1, MatriculeUpdate);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				
				getWorkflowInstance().setValue("P_GS_Nom", rs.getString(1));
				getWorkflowInstance().setValue("P_GS_Prenom", rs.getString(2));
				getWorkflowInstance().setValue("DateDeNaissance", rs.getDate(3));
				getWorkflowInstance().setValue("P_GS_Adrs_Perso", rs.getString(4));
				getWorkflowInstance().setValue("P_GS_Mail", rs.getString(5));
				getWorkflowInstance().setValue("P_GS_Nationalite", rs.getString(6));
				getWorkflowInstance().setValue("P_GS_D_embuche_groupe", rs.getDate(7));
				getWorkflowInstance().setValue("P_GS_D_embuche_filiale", rs.getDate(8));
				
				File doc = new File(rs.getString(9));
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
				getWorkflowInstance().setValue("P_GS_Nbre_Enfants", rs.getFloat(18));
				getWorkflowInstance().setValue("P_GS_nbreConge", rs.getFloat(19));
				getWorkflowInstance().setValue("P_GS_VdocLogin", rs.getString(20));
				// getWorkflowInstance().setValue("P_GS_Sexe", rs.getString(21));
				
			}
			
			// Select Conjoint
			String req8 = "  select Cin, Nom, Prenom, DateNaissance from Conjoint where personnelMatricule = ? ";
			st = connection.prepareStatement(req8);
			st.setString(1, MatriculeUpdate);
			ResultSet rs8 = st.executeQuery();
			while (rs8.next())
			{
				
				getWorkflowInstance().setValue("P_GS_CINConjoint", rs8.getString(1));
				getWorkflowInstance().setValue("P_GS_Nom_Conjoint", rs8.getString(2));
				getWorkflowInstance().setValue("P_GS_PrenomConjoint", rs8.getString(3));
				getWorkflowInstance().setValue("P_GS_DDN_Conjoint", rs8.getDate(4));
				
			}
			
			// Select Carte nationale
			
			String req1 = "select CIN, DateExperation from CarteNational where Personnelmatricule = ? ";
			st = connection.prepareStatement(req1);
			st.setString(1, MatriculeUpdate);
			ResultSet rs1 = st.executeQuery();
			while (rs1.next())
			{
				
				getWorkflowInstance().setValue("P_GS_CIN", rs1.getString(1));
				getWorkflowInstance().setValue("P_GS_date_expr_CIN", rs1.getDate(2));
			}
			
			// Select passeport
			
			String req2 = "select numPassport, DateExperation from Passeport where  Personnelmatricule = ? ";
			st = connection.prepareStatement(req2);
			st.setString(1, MatriculeUpdate);
			ResultSet rs2 = st.executeQuery();
			while (rs2.next())
			{
				
				getWorkflowInstance().setValue("P_GS_passePort", rs2.getString(1));
				getWorkflowInstance().setValue("P_GS_date_Exper_passeport", rs2.getDate(2));
			}
			
			// select telephone
			
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
			
			// select experience
			
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
			// select diplome
			
			DeletTabl("P_GS_TAB_diplome");
			
			List Diplomes = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
			if (Diplomes.size() == 0)
			{
				
				String req5 = "select Specialite, DateObtention, TypeDiplome, Institut,IdDiplome " + "from Diplome where Personnelmatricule = ? ";
				st = connection.prepareStatement(req5);
				st.setString(1, MatriculeUpdate);
				ResultSet rs5 = st.executeQuery();
				
				while (rs5.next())
				{
					ILinkedResource AllDiplomes = getWorkflowInstance().createLinkedResource("P_GS_TAB_diplome");
					
					AllDiplomes.setValue("P_GS_TAB_diplomes_Specialte", rs5.getString(1));
					AllDiplomes.setValue("P_GS_TAB_diplomes_dateObte", new java.util.Date(rs5.getDate(2).getTime()));
					AllDiplomes.setValue("P_GS_TAB_diplomes_Type", rs5.getString(3));
					AllDiplomes.setValue("P_GS_TAB_diplomes_Institut", rs5.getString(4));
					AllDiplomes.setValue("P_GS_IdDiplome", rs5.getString(5));
					getWorkflowInstance().addLinkedResource(AllDiplomes);
				}
				
			}
			
			// select Enfant
			
			DeletTabl("P_GS_TAB_Enfants");
			List Enfants = (List) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
			if (Enfants.size() == 0)
			{
				
				String req6 = "select E.nom, E.prenom, E.Datenaissance, E.IdEnfant " + "from Enfant E, PersConjEnfa PCF" + " where E.IdEnfant = PCF.EnfantIdEnfant and PCF.Personnelmatricule = ? ";
				
				st = connection.prepareStatement(req6);
				st.setString(1, MatriculeUpdate);
				ResultSet rs6 = st.executeQuery();
				while (rs6.next())
				{
					ILinkedResource AllEnfants = getWorkflowInstance().createLinkedResource("P_GS_TAB_Enfants");
					
					AllEnfants.setValue("P_GS_TAB_Enfant_Nom", rs6.getString(1));
					AllEnfants.setValue("P_GS_TAB_Enfant_Prenom", rs6.getString(2));
					AllEnfants.setValue("P_GS_TAB_Enfant_DateNa", new java.util.Date(rs6.getDate(3).getTime()));
					AllEnfants.setValue("P_GS_IdEnfant", rs6.getString(4));
					getWorkflowInstance().addLinkedResource(AllEnfants);
				}
				
			}
			
			// select poste
			
			String req6 = "select P.LibellePoste, P.Descriptif " + "from Poste P, Personnel Ps where Ps.PosteIdPoste = P.IdPoste and matricule = ?";
			st = connection.prepareStatement(req6);
			st.setString(1, MatriculeUpdate);
			ResultSet rs6 = st.executeQuery();
			while (rs6.next())
			{
				
				getWorkflowInstance().setValue("P_GS_Poste", rs6.getString(1));
				getWorkflowInstance().setValue("GS_P_Descriptif_poste", rs6.getString(2));
				
			}
			
			// select direction
			
			String req7 = "select Libelle from Direction D, Personnel P where D.IdDirection= P.DirectionIdDirection and matricule = ?";
			st = connection.prepareStatement(req7);
			st.setString(1, MatriculeUpdate);
			ResultSet rs7 = st.executeQuery();
			while (rs7.next())
			{
				
				getWorkflowInstance().setValue("P_GS_Direction_rattachement", rs7.getString(1));
				
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	@Override
	public void onPropertyChanged(IProperty property)
	{
		if (property.getName().equals("P_GS_ListeS"))
		{
			String Action = (String) getWorkflowInstance().getValue("P_GS_Action");
			if (Action.equals("Modifier"))
			{				remplir();			}
		}
		
		String nom = (String) getWorkflowInstance().getValue("P_GS_Nom");
		String prenom = (String) getWorkflowInstance().getValue("P_GS_Prenom");
		
		if(property.getName().equals("P_GS_Nom")||property.getName().equals("P_GS_Prenom")){
			if((!(nom==null))&&(!(prenom==null))){
				char PremierChar = prenom.charAt(0);
				String LoginVDOC = PremierChar+"."+nom;
				getWorkflowInstance().setValue("P_GS_VdocLogin", LoginVDOC);
				
//				if(ExistLogin(LoginVDOC)==false){
//					char PremierChar1 = prenom.charAt(1);
//					String LoginVDOC1 = PremierChar1+"."+nom;
//					getWorkflowInstance().setValue("P_GS_VdocLogin", LoginVDOC1);
//				}
			}}
		
		
			if(property.getName().equals("P_GS_Nom")||property.getName().equals("P_GS_Prenom")){
				if((!(nom==null))&&(!(prenom==null))){
					char PremierChar = prenom.charAt(0);
					String Email = PremierChar+"."+nom+"@attijari.ma";
					getWorkflowInstance().setValue("P_GS_Mail", Email);
				}		
				
			}
				
				
		
		
		
		
		
		
		
		if (property.getName().equals("P_GS_Photo"))
		{
			
			try
			{
				rc = getResourceController();
				instance = getWorkflowInstance();
				List<IAttachment> docfp = (List<IAttachment>) instance.getValue("P_GS_Photo");
				if (docfp != null)
				{
					String filename = null;
					if (docfp.size() > 0)
					{
						filename = "Photo.jpg";
						File newFile = new File("C:\\VDocPlatform\\ImageFicheSalarie\\" + filename);
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
						
						Image img;
						
						img = ImageIO.read(newFile);
						int widthImg = img.getWidth(null);
						int heightImg = img.getHeight(null);
						
						if (widthImg != 960 || heightImg != 960)
						{
							rc.alert("Veuillez Importer une image valide(960x960)");
							instance.setValue("P_GS_Photo", null);
						}
						newFile.delete();
						
						
						
						
						
						
						
						
						
						BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
						Graphics g = bi.createGraphics(); 
						g.drawImage(img, 0, 0, 960, 960, null); 
						
					
						
		
						
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// if(property.getName().equals("P_GS_Action")){
		//
		// Vider();
		// }
		super.onPropertyChanged(property);
	}
	
}
