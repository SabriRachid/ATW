package com.serviceRH;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;

import com.attijari.GestionSalarie.FileManager;
import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IGroup;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IOrganization;
import com.axemble.vdoc.sdk.interfaces.IResource;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.modules.IPortalModule;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.structs.Period;
import com.beans.attijariRh.Avantage;
import com.beans.attijariRh.CarteNational;
import com.beans.attijariRh.Conjoint;
import com.beans.attijariRh.Filiale;
import com.beans.attijariRh.Indemnite;
import com.beans.attijariRh.Passeport;
import com.beans.attijariRh.Poste;
import com.beans.attijariRh.Telephone;
import com.genererRapport.GenererPDF;

import dao.SingletonConnexionBDD;

public class ServiceFicheSalarie extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4267018823763232923L;
	private static Connection connection;
	private static IWorkflowModule iwm;
	private static IPortalModule ipm;
	
	public ServiceFicheSalarie()
	{
		super();
		iwm = Modules.getWorkflowModule();
		ipm = Modules.getPortalModule();
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(iwm, ipm).getConnection();
		}
		catch (PortalModuleException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
	}
	
	public static boolean ifThisNumberExist(String matricule)
	{
		
		// String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
		int cpt = 0;
		try
		{
			String req = "select COUNT(*) from Personnel where  matricule = ? ";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, matricule);
			
			ResultSet rs1 = st.executeQuery();
			while (rs1.next())
			{
				cpt += rs1.getInt(1);
			}
			
			System.out.println(cpt);
			if (cpt > 0)
				return true;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean ifThisNumberExist(String number, String matricule)
	{
		
		// String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
		int cpt = 0;
		try
		{
			String req = "select count(*) from CarteNational where CIN = ? and Personnelmatricule<> ?"
					+ "union "
					+ "select count(*) from Conjoint  where (cin = ? or NCMIM = ?) and Personnelmatricule <> ? "
					+ "union "
					+ "select COUNT(*) from Personnel where   matricule <> ? and ( NCIMR = ? or NCMIM= ? or NCNSS = ? or RIB = ?) "
					+ "union "
					+ "select count(*) from Passeport where NumPassport = ? and Personnelmatricule <> ?"
					+ "union "
					+ "select count(*) from Enfant where NCMIM = ? and Personnelmatricule <> ?";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, number);
			st.setString(2, matricule);
			
			st.setString(3, number);
			st.setString(4, number);
			st.setString(5, matricule);
			
			st.setString(6, matricule);
			st.setString(7, number);
			st.setString(8, number);
			st.setString(9, number);
			st.setString(10, number);
			
			st.setString(11, number);
			st.setString(12, matricule);
			
			st.setString(13, number);
			st.setString(14, matricule);
			ResultSet rs1 = st.executeQuery();
			while (rs1.next())
			{
				cpt += rs1.getInt(1);
			}
			
			if (cpt > 0)
				return true;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static void getSalarieWithHisMatricule(IWorkflowInstance iw,String matricule){
		
		try
		{
			String req = "select [Matricule] ,[FilialeIdFiliale] ,[DirectionIdDirection],[Nom],[Prenom],[NomdeJeuneFille],[DateNaissance],[Adresse] ,[Email] ,[Nationalite]"
					+ ",[Nationalite2] ,[DateEmbaucheGroupe],[DateEmbaucheFiliale],[Photo],[SituationFamiliale] ,[NCNSS] ,[NCIMR] ,[NCMIM],[RIB],[Agence],[NombreJoursConge]"
					+ " ,[NombreJoursCongeAnnuel],[NombrJoursDispo],[reliquatAnneEnCours]"
					+ " ,[loginVdoc] ,[Etat] ,[sexeMasculin] ,[lieuNaissance] ,[Statut],[Loisir],JustifNationalite2,JustifRIB,JustifSituationFamiliale,TypeJustifSituationFamiliale from Personnel where Matricule =  ?  ";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, matricule);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				String personnelMatricule  = rs.getString(1);iw.setValue("P_GS_MatriculeUpdate", personnelMatricule);
				
				String filiale = rs.getString(2);iw.setValue("P_GS_Filiale", filiale.equals("attijariintermediation")? "Attijari Intermédiation":"Attijari Finance Corp.");
				
				String iddirection = rs.getString(3);iw.setValue("P_GS_Direction", getLibelleDirection(iddirection));
				
				String nom = rs.getString(4);iw.setValue("P_GS_Nom", nom);
				
				String prenom = rs.getString(5);iw.setValue("P_GS_Prenom", prenom);
				
				String nomJeuneFille = rs.getString(6);iw.setValue("P_GS_NomJeuneFille", nomJeuneFille);
				
				Date dateNaissance =  new Date(rs.getDate(7).getTime());iw.setValue("P_GS_DateNaissance", dateNaissance);
				
				String adresse = rs.getString(8);iw.setValue("P_GS_Adrs_Perso", adresse);
				
				String email = rs.getString(9);iw.setValue("P_GS_Mail", email);
				
				String nationalite = rs.getString(10);iw.setValue("P_GS_ListeNationalite", nationalite);
				
				String nationalite2 = rs.getString(11);iw.setValue("P_GS_ListeNationalite2", nationalite2);
				
				Date dateEmbaucheGroupe =  new Date(rs.getDate(12).getTime());iw.setValue("P_GS_D_embuche_groupe", dateEmbaucheGroupe);
				
				Date dateEmbaucheFiliale =  new Date(rs.getDate(13).getTime());iw.setValue("P_GS_D_embuche_filiale", dateEmbaucheFiliale);
				
				String photo = rs.getString(14);
				iw.setValue("P_GS_Photo", null);
				if(photo!=null){
					//EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//PHOTO//"+photo, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//PHOTO//"+photo);

					iw.setValue("P_GS_PhotoImg", "skins//ImageFicheSalarie//"+photo);
					Modules.getWorkflowModule().addAttachment(iw, "P_GS_Photo", photo, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//skins//ImageFicheSalarie//" + photo);
					
				}

				
				String situationFamiliale = rs.getString(15);iw.setValue("P_GS_S_LFamilliale", situationFamiliale);
				
				String nCNSS = rs.getString(16);iw.setValue("P_GS_CNSS", nCNSS);
				String nCIMR = rs.getString(17);iw.setValue("P_GS_CIMR", nCIMR);
				String nCMIM = rs.getString(18);iw.setValue("P_GS_CMIM", nCMIM);
				String rib = rs.getString(19);iw.setValue("P_GS_RIB", rib);
				String agence = rs.getString(20);iw.setValue("P_GS_AGENCE", agence);
				float droitAcquis = rs.getFloat(22);iw.setValue("P_GS_nbreConge", droitAcquis);
				float reliquatAnterieur = rs.getFloat(23);iw.setValue("P_GS_ReliquatSoldeAnterieur", reliquatAnterieur);
				float reliquatEncours = rs.getFloat(24);iw.setValue("P_GS_ReliquatAnneEnCours", reliquatEncours);
				String vdocLogin = rs.getString(25);iw.setValue("P_GS_VdocLogin", vdocLogin);
				String etat = rs.getString(26);iw.setValue("P_GS_Statut", etat.equals("Valider")?"Active":"Non active");
				boolean sexeMasculin = rs.getBoolean(27);iw.setValue("P_GS_Sexe", sexeMasculin?"Masculin":"Féminin");
				String lieuNaissance = rs.getString(28);iw.setValue("P_GS_LieuNaiss", lieuNaissance);
				String loisirs = rs.getString(30);iw.setValue("P_GS_Loisirs", loisirs);
				
				String nationalite2Justif = rs.getString(31);
				iw.setValue("P_GS_ListeNationalite2PJ", null);
				if(nationalite2Justif!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFNATIONALITE2//" + nationalite2Justif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFNATIONALITE2//" + nationalite2Justif);
					Modules.getWorkflowModule().addAttachment(iw, "P_GS_ListeNationalite2PJ", nationalite2Justif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFNATIONALITE2//" + nationalite2Justif);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFNATIONALITE2//" + nationalite2Justif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFNATIONALITE2//" + nationalite2Justif);
					
				}
				
				String ribJustif = rs.getString(32);
				iw.setValue("P_GS_RIBPJ", null);
				if(ribJustif!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFRIB//" + ribJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFRIB//" + ribJustif);
					Modules.getWorkflowModule().addAttachment(iw, "P_GS_RIBPJ", ribJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFRIB//" + ribJustif);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFRIB//" + ribJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFRIB//" + ribJustif);
				}
				
				String situationFamilialeJustif = rs.getString(33);
				iw.setValue("P_GS_JUSTIFSITUATIONFAMILIALE", null);
				//iw.setValue("P_GS_JUSTIFSITUATIONFAMILIALEBIS", null);
				if(situationFamilialeJustif!=null){
					String typeJustifSF = rs.getString(34);
					String rep = "";
					if(typeJustifSF!=null){
						if(typeJustifSF.equals("actemariage")){
							rep = "ACTEDEMARIAGE";
						}
						else if(typeJustifSF.equals("actedivorce")){
							rep = "ACTEDIVORCE";
						}
						else if(typeJustifSF.equals("actedeces")){
							rep = "ACTEDECES";
						}					
						if(situationFamilialeJustif!=null){
							EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+rep+"//" + situationFamilialeJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+rep+"//" + situationFamilialeJustif);
							Modules.getWorkflowModule().addAttachment(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", situationFamilialeJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+rep+"//" + situationFamilialeJustif);
							EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+rep+"//" + situationFamilialeJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+rep+"//" + situationFamilialeJustif);

						}
						//Modules.getWorkflowModule().addAttachment(iw, "P_GS_JUSTIFSITUATIONFAMILIALEBIS", situationFamilialeJustif, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+rep+"//" + situationFamilialeJustif);
					}
					
						
				}
				
			}
			
			
			req = "select * from CarteNational where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			while(rs.next()){
				String cin = rs.getString(2);iw.setValue("P_GS_CIN", cin);
				Date dateExpiration =  rs.getDate(3)!=null?new Date(rs.getDate(3).getTime()):null;iw.setValue("P_GS_date_expr_CIN", dateExpiration);
				String copieCin = rs.getString(4);
				iw.setValue("P_GS_CINPJ", null);
				if(copieCin!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CIN//" + copieCin, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CIN//" + copieCin);
					Modules.getWorkflowModule().addAttachment(iw, "P_GS_CINPJ", copieCin, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CIN//" + copieCin);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CIN//" + copieCin, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CIN//" + copieCin);
				}

			}
			
			req = "select * from Passeport where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			while(rs.next()){
				String numPassport = rs.getString(2);iw.setValue("P_GS_passePort", numPassport);
				Date dateExpiration =  rs.getDate(3)!=null? new Date(rs.getDate(3).getTime()):null;iw.setValue("P_GS_date_Exper_passeport", dateExpiration);
				Date dateDelivrance =  rs.getDate(4)!=null? new Date(rs.getDate(4).getTime()):null; iw.setValue("P_GS_DateDelivrance", dateDelivrance);
				String copiePassport = rs.getString(5);
				iw.setValue("P_GS_passePortPJ", null);
				if(copiePassport!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//PASSEPORT//" + copiePassport, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//PASSEPORT//" + copiePassport);
					Modules.getWorkflowModule().addAttachment(iw, "P_GS_passePortPJ", copiePassport, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//PASSEPORT//" + copiePassport);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//PASSEPORT//" + copiePassport, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//PASSEPORT//" + copiePassport);

				}			
				


			}
			
			req = "select * from Poste where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			while(rs.next()){
				String libelle = rs.getString(2);iw.setValue("P_GS_TPoste", libelle);
				String descriptif = rs.getString(3);iw.setValue("P_GS_Descriptif_poste", descriptif);
			}
			
			req = "select * from Telephone where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			while(rs.next()){
				String tel1 = rs.getString(2);iw.setValue("P_GS_tel1", tel1);
				String tel2 = rs.getString(3);iw.setValue("P_GS_Tel2", tel2);
				String telUrgence = rs.getString(4);iw.setValue("P_GS_Tel_Urgence", telUrgence);
				String telPersonneUrgence = rs.getString(5);iw.setValue("P_GS_Personne_urgence", telPersonneUrgence);
			}
			
			req = "select * from Conjoint where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			while(rs.next()){
				String cin = rs.getString(2);iw.setValue("P_GS_CINConjoint", cin);
				String nom = rs.getString(3);iw.setValue("P_GS_Nom_Conjoint", nom);
				String prenom = rs.getString(4);iw.setValue("P_GS_PrenomConjoint", prenom);
				Date dateNaissance =  rs.getDate(5)!=null?new Date(rs.getDate(5).getTime()):null;iw.setValue("P_GS_DDN_Conjoint", dateNaissance);
				String ncmim = rs.getString(6);iw.setValue("P_GS_CMIM_Conjoint", ncmim);
				String copiecin = rs.getString(7);
				iw.setValue("P_GS_CINConjointPJ", null);
				if(copiecin!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CINCONJOINT//" + copiecin, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CINCONJOINT//" + copiecin);
					Modules.getWorkflowModule().addAttachment(iw, "P_GS_CINConjointPJ", copiecin, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CINCONJOINT//" + copiecin);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CINCONJOINT//" + copiecin, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//CINCONJOINT//" + copiecin);
				}
				
				

			}
			
			
			req = "select * from Competence where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			while(rs.next()){
				String outilsInformatiques = rs.getString(2);iw.setValue("P_GS_OutilsInformatiques", outilsInformatiques);
				String logicielsDeGestion = rs.getString(3);iw.setValue("P_GS_LogicielsGestion", logicielsDeGestion);
			}
			
			
			req = "select * from Diplome where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			
			
			iw.deleteLinkedResources(iw.getLinkedResources("P_GS_TAB_diplome"));
			while(rs.next()){
				ILinkedResource newDiplome = iw.createLinkedResource("P_GS_TAB_diplome");
				String idDiplome = rs.getString(1);newDiplome.setValue("idDiplome", idDiplome);
				String specialite = rs.getString(3);newDiplome.setValue("P_GS_TAB_diplomes_Specialte", specialite);
				Date dateObtention =  rs.getDate(4)!=null?new Date(rs.getDate(4).getTime()):null;newDiplome.setValue("P_GS_TAB_diplomes_dateObte", dateObtention);
				String typeDiplome = rs.getString(5);newDiplome.setValue("P_GS_TAB_diplomes_Type", typeDiplome);
				String institut = rs.getString(6);newDiplome.setValue("P_GS_TAB_diplomes_Institut", institut);
				String pj = rs.getString(7);
				newDiplome.setValue("P_GS_TAB_diplomes_Diplome", null);
				if(pj!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//DIPLOMES//" + pj, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//DIPLOMES//" + pj);
					Modules.getWorkflowModule().addAttachment(newDiplome, "P_GS_TAB_diplomes_Diplome", pj, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//DIPLOMES//" + pj);
					EncryptionFile. crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//DIPLOMES//" + pj, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//DIPLOMES//" + pj);

				}
				
				

				iw.addLinkedResource(newDiplome);
			}
			
			req = "select * from Enfant where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			iw.deleteLinkedResources(iw.getLinkedResources("P_GS_TAB_Enfants"));
			while(rs.next()){
				ILinkedResource newEnfant = iw.createLinkedResource("P_GS_TAB_Enfants");
				String idEnfant = rs.getString(1);newEnfant.setValue("idEnfant", idEnfant);
				String nom = rs.getString(2);newEnfant.setValue("P_GS_TAB_Enfant_Nom", nom);
				String prenom = rs.getString(3);newEnfant.setValue("P_GS_TAB_Enfant_Prenom", prenom);
				Date dateNaissance =  rs.getDate(4)!=null?new Date(rs.getDate(4).getTime()):null;newEnfant.setValue("P_GS_TAB_Enfant_DateNa", dateNaissance);
				String ncmim = rs.getString(5);newEnfant.setValue("P_GS_CMIM_Enfant", ncmim);
				String copieNaissance = rs.getString(7);
				newEnfant.setValue("P_GS_ExtraitActeNaissance", null);
				if(copieNaissance!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//EXTRAITDENAISSANCE//" + copieNaissance, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//EXTRAITDENAISSANCE//" + copieNaissance);
					Modules.getWorkflowModule().addAttachment(newEnfant, "P_GS_ExtraitActeNaissance", copieNaissance, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//EXTRAITDENAISSANCE//" + copieNaissance);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//EXTRAITDENAISSANCE//" + copieNaissance, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//EXTRAITDENAISSANCE//" + copieNaissance);

				}

				iw.addLinkedResource(newEnfant);

			}
			
			
			req = "select * from Experience where Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			iw.deleteLinkedResources(iw.getLinkedResources("P_GS_TAB_Experience"));
			while(rs.next()){
				ILinkedResource newExperience = iw.createLinkedResource("P_GS_TAB_Experience");
				String idExperience = rs.getString(1);newExperience.setValue("idExperience", idExperience);
				Period periode = new Period(new Date(rs.getDate(3).getTime()), new Date(rs.getDate(4).getTime()));newExperience.setValue("P_GS_TAB_Experience_periode", periode);
				String societeAccueil = rs.getString(5);newExperience.setValue("P_GS_TAB_Experience_Societe", societeAccueil);
				String statut = rs.getString(6);newExperience.setValue("P_GS_TAB_Experience_Statut", statut);
				String typecontrat = rs.getString(7);newExperience.setValue("P_GS_TAB_Experience_Type", typecontrat);
				String fonction = rs.getString(8);newExperience.setValue("P_GS_TAB_Experience_Taches", fonction);
				String copieAttestation = rs.getString(9);
				newExperience.setValue("P_GS_TAB_Experience_Attestation", null);
				if(copieAttestation!=null){
					EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//JUSTIFEXPERIENCE//" + copieAttestation, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//JUSTIFEXPERIENCE//" + copieAttestation);
					Modules.getWorkflowModule().addAttachment(newExperience, "P_GS_TAB_Experience_Attestation", copieAttestation, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//JUSTIFEXPERIENCE//" + copieAttestation);
					EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//JUSTIFEXPERIENCE//" + copieAttestation, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+matricule+"//JUSTIFEXPERIENCE//" + copieAttestation);

				}

				iw.addLinkedResource(newExperience);
			}
			
			
			req = "select * from Remuneration,Indemnite,Avantage where Indemnite.idRemuneration = Remuneration.idRemuneration and "
					+ " Avantage.idRemuneration = Remuneration.idRemuneration and  Personnelmatricule =  ? ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			iw.deleteLinkedResources(iw.getLinkedResources("Tab_Remun"));
			while(rs.next()){
				ILinkedResource newRemuneration = iw.createLinkedResource("Tab_Remun");
				//int idRemuneration = rs.getInt(1);newRemuneration.setValue("idRemuneration", idRemuneration);
				float anne = rs.getFloat(2);newRemuneration.setValue("P_GS_TAB_Remunerations_Anne", anne);
				float SBA = rs.getFloat(3);newRemuneration.setValue("P_GS_TAB_Remunerations_SBA", SBA);
				float SBM = rs.getFloat(4);newRemuneration.setValue("P_GS_TAB_Remunerations_SBM", SBM);
				float SNM = rs.getFloat(5);newRemuneration.setValue("P_GS_TAB_Remunerations_SNM", SNM);
				float bonus = rs.getFloat(6);newRemuneration.setValue("P_GS_TAB_Remunerations_BONUS", bonus);
				float remunerationsAutres = rs.getFloat(7);newRemuneration.setValue("P_GS_TAB_Remunerations_AUTRES", remunerationsAutres);
				Date dateDecision =  rs.getDate(9)!=null?new Date(rs.getDate(9).getTime()):null;newRemuneration.setValue("P_GS_TAB_Remunerations_DateDecision", dateDecision);
				
				float logement = rs.getFloat(11);newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_Logement", logement);
				float representation = rs.getFloat(12);newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_Representation", representation);
				float deplacement = rs.getFloat(13);newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_Deplacement", deplacement);
				float transport = rs.getFloat(14);newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_Transport", transport);
				float indemniteAutres = rs.getFloat(15);newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_Autres", indemniteAutres);
				dateDecision =  rs.getDate(16)!=null?new Date(rs.getDate(16).getTime()):null;newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_DateDecision", dateDecision);
				
				float indemniteTout = logement+representation+deplacement+transport+indemniteAutres;
				newRemuneration.setValue("P_GS_TAB_Remunerations_Ind_Total", indemniteTout);
				
				float vehiculefonction = rs.getFloat(18);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_VF", vehiculefonction);
				float vehiculeservice = rs.getFloat(19);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_VS", vehiculeservice);
				float forfaitCarburant = rs.getFloat(20);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_FC", forfaitCarburant);
				float logementFonction = rs.getFloat(21);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_LF", logementFonction);
				float abonnementGSM = rs.getFloat(22);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_AG", abonnementGSM);
				float billetsAvion = rs.getFloat(23);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_BA", billetsAvion);
				float avantageAutres = rs.getFloat(24);newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_AUTRES", avantageAutres);
				dateDecision =  rs.getDate(25)!=null?new Date(rs.getDate(16).getTime()):null;newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_DateDecision", dateDecision);
				
				float avantagesTout = vehiculefonction+vehiculeservice+forfaitCarburant+logementFonction+abonnementGSM+billetsAvion+avantageAutres;
				newRemuneration.setValue("P_GS_TAB_Remunerations_Avant_Total", avantagesTout);
				
				iw.addLinkedResource(newRemuneration);
			}
			
			
			
			req = "select s.LoginVdoc from Personnel s, Personnel p, Superieur sup "
					+ "where p.Matricule =  ?  "
					+ "and sup.SupMatricule = s.Matricule "
					+ "and p.Matricule = sup.PersonnelMatricule ";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			List<IUser> superieurs = new ArrayList<>();
			while(rs.next()){
				String loginSuperieur = rs.getString(1);
				IUser sup = Modules.getWorkflowModule().getUserByLogin(loginSuperieur);
				superieurs.add(sup);
			}
			iw.setValue("P_GS_SupList", null);
			iw.setValue("P_GS_SupList", superieurs);
			
			
			
			
			req = 	  "select * "
					+ "from Langue,PersonnelLangue "
					+ "where PersonnelMatricule = ? "
					+ "and Langue.idLangue = PersonnelLangue.idLangue";
			st = connection.prepareStatement(req);
			st.setString(1, matricule);
			rs = st.executeQuery();
			iw.deleteLinkedResources(iw.getLinkedResources("P_GS_TAB_Langues"));
			while(rs.next()){
				ILinkedResource newLangue = iw.createLinkedResource("P_GS_TAB_Langues");
				String idLangue = rs.getString(1);newLangue.setValue("idLangue", idLangue);
				String libelle = rs.getString(2);newLangue.setValue("P_GS_TAB_Langues_Langue", libelle);
				String maitrise = rs.getString(5);newLangue.setValue("P_GS_TAB_Langues_Maitrise", maitrise);
				iw.addLinkedResource(newLangue);
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPersonnel(IWorkflowInstance iw,List<ILinkedResource> tableauEnfants,List<ILinkedResource> tableauDiplomes,
			List<ILinkedResource> tableauExperiences,List<ILinkedResource> tableauRemunerations,String personnelMatricule, String nom, String nomJeuneFille, String prenom, Date dateNaissance,
			String adresse, String email, String nationalite, String nationalite2, Date dateEmbaucheGroupe, Date dateEmbaucheFiliale, String photo, String situationFamiliale, String nCNSS,
			String nCIMR, String nCMIM, String rib, String agence,float nombreJoursCongeAnnuel, float nombrJoursCongeDispo, float reliquatConge, String vdocLogin, String etat, String loisir, 
			boolean sexeMasculin,String lieuNaissance,boolean statut, String direction, String poste,String descriptifposte, String filiale, Telephone telephone, CarteNational carteNational,
			Passeport passeport,Conjoint conjoint,List<String> superieurs)
	{
		try
		{
			//dossier racine collab
			File dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"");
			dirNewCollab.mkdir();
			
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//ACTEDECES");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//ACTEDEMARIAGE");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//ACTEDIVORCE");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//CIN");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//CINCONJOINT");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//DIPLOMES");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//EXTRAITDENAISSANCE");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFEXPERIENCE");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFNATIONALITE2");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//JUSTIFRIB");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//PASSEPORT");
			dirNewCollab.mkdir();
			dirNewCollab = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//PHOTO");
			dirNewCollab.mkdir();
			
			
			
			java.sql.Date datesql = new java.sql.Date(0);
			String req = " insert into Personnel(Matricule,FilialeIdFiliale,DirectionIdDirection,Nom,Prenom,NomdeJeuneFille,DateNaissance,Adresse,Email,"
					+ "Nationalite,Nationalite2,DateEmbaucheGroupe,DateEmbaucheFiliale,Photo,SituationFamiliale,NCNSS,NCIMR,NCMIM,"
					+ "RIB,Agence,NombreJoursConge,NombreJoursCongeAnnuel,NombrJoursDispo,reliquatAnneEnCours,loginVdoc,Etat,sexeMasculin,"
					+ "lieuNaissance,Statut,JustifNationalite2,JustifRIB,JustifSituationFamiliale,TypeJustifSituationFamiliale) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			PreparedStatement st = connection.prepareStatement(req);
			//
			st.setString(1, personnelMatricule);
			st.setString(2, filiale);
			st.setString(3, direction);
			st.setString(4, nom);
			st.setString(5, prenom);
			st.setString(6, nomJeuneFille);
			datesql = new java.sql.Date(dateNaissance.getTime());
			st.setDate(7, datesql);
			st.setString(8, adresse);
			st.setString(9, email);
			st.setString(10, nationalite);
			st.setString(11, nationalite2);
			datesql.setTime(dateEmbaucheGroupe.getTime());
			st.setDate(12, datesql);
			datesql.setTime(dateEmbaucheFiliale.getTime());
			st.setDate(13, datesql);
			st.setString(14, photo);
			copyImageTo(iw, "P_GS_Photo", personnelMatricule);
			copyPJToVDocWar(iw, "P_GS_Photo", personnelMatricule, "PHOTO");
			
			st.setString(15, situationFamiliale);
			st.setString(16, nCNSS);
			st.setString(17, nCIMR);
			st.setString(18, nCMIM);
			st.setString(19, rib);
			st.setString(20, agence);
			st.setFloat(21, nombreJoursCongeAnnuel);
			st.setFloat(22, nombreJoursCongeAnnuel);
			st.setFloat(23, nombrJoursCongeDispo);
			st.setFloat(24, reliquatConge);
			st.setString(25, vdocLogin);
			st.setString(26, etat);
			st.setBoolean(27, sexeMasculin);
			st.setString(28, lieuNaissance);
			st.setBoolean(29, statut);
			st.setString(30, copyPJToVDocWar(iw, "P_GS_ListeNationalite2PJ", personnelMatricule, "JUSTIFNATIONALITE2"));
			st.setString(31, copyPJToVDocWar(iw, "P_GS_RIBPJ", personnelMatricule, "JUSTIFRIB"));
			
			if(situationFamiliale.equals("Marié")){
				st.setString(32, copyPJToVDocWar(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", personnelMatricule, "ACTEDEMARIAGE"));
				st.setString(33, "actemariage");
			}
				
			else if(situationFamiliale.equals("Divorcé")){
				st.setString(32, copyPJToVDocWar(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", personnelMatricule, "ACTEDIVORCE"));
				st.setString(33, "actedivorce");
			}
				
			else if(situationFamiliale.equals("Veuve")){
				st.setString(32, copyPJToVDocWar(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", personnelMatricule, "ACTEDECES"));
				st.setString(33, "actedeces");
			}
				
			else {
				st.setString(32, null);
				st.setString(33, null);
			}
				
			st.executeUpdate();
			
			addCarteNationalToPersonnel(carteNational.getCIN(),carteNational.getDateExperation(),personnelMatricule,iw);
			addPassePortToPersonnel(passeport.getNumPassport(), personnelMatricule, passeport.getDateDelivrance(), passeport.getDateExperation(),iw);
			addTelephoneToPersonnel(telephone.getTel1(), telephone.getTel2(), telephone.getTelUrgences(), telephone.getTelContactUrgence(), personnelMatricule);
			
			if(situationFamiliale.equals("Marié"))
			addConjointToPersonnel(conjoint.getCin(), conjoint.getNom(), conjoint.getPrenom(), conjoint.getDateNaissance(), personnelMatricule, conjoint.getNcmim(),iw);
			
			
			addPosteToPersonnel(poste, descriptifposte, personnelMatricule);
			
			if(!situationFamiliale.equals("Célibataire"))
			for(ILinkedResource enfant : tableauEnfants){
				String nomEnfant = (String) enfant.getValue("P_GS_TAB_Enfant_Nom");
				String prenomEnfant = (String) enfant.getValue("P_GS_TAB_Enfant_Prenom");
				Date dateNaissanceEnfant = (Date) enfant.getValue("P_GS_TAB_Enfant_DateNa");
				String ncmimEnfant= (String) enfant.getValue("P_GS_CMIM_Enfant");
				addEnfantToPersonnel(nomEnfant, prenomEnfant, dateNaissanceEnfant, ncmimEnfant, personnelMatricule,enfant);
			}
			
			
			for(ILinkedResource experience : tableauExperiences){
				Period periode = (Period) experience.getValue("P_GS_TAB_Experience_periode");
				Date dateDeb = periode.getStartDate();
				Date dateFin = periode.getEndDate();
				String societeAcceuil = (String) experience.getValue("P_GS_TAB_Experience_Societe");
				String fonction = (String) experience.getValue("P_GS_TAB_Experience_Taches");
				String typeContrat = (String) experience.getValue("P_GS_TAB_Experience_Type");
				String statutExperience = (String) experience.getValue("P_GS_TAB_Experience_Statut");
				addExperienceToPersonnel(dateDeb, dateFin, societeAcceuil, fonction, statutExperience, typeContrat, personnelMatricule,experience);
			}
			
			
			for(ILinkedResource remuneration : tableauRemunerations){
				
				float anneRemuneration = (float) remuneration.getValue("P_GS_TAB_Remunerations_Anne");
				float SBA = (float) remuneration.getValue("P_GS_TAB_Remunerations_SBA");
				float SBM = (float) remuneration.getValue("P_GS_TAB_Remunerations_SBM");
				float SNM = (float) remuneration.getValue("P_GS_TAB_Remunerations_SNM");
				float bonus = (float) remuneration.getValue("P_GS_TAB_Remunerations_BONUS");
				float autres = (float) remuneration.getValue("P_GS_TAB_Remunerations_AUTRES");
				Date dateDecision = (Date) remuneration.getValue("P_GS_TAB_Remunerations_DateDecision");
				
				Avantage avantage = new Avantage();
				avantage.setAbonnementsGSM((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_AG"));
				avantage.setAutres((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_AUTRES"));
				avantage.setBilletAvion((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_BA"));
				avantage.setForfaitCarburants((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_FC"));
				avantage.setLogementFonction((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_LF"));
				avantage.setVehiculeFonction((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_VF"));
				avantage.setVehiculeService((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_VS"));
				avantage.setDatedecision((Date) remuneration.getValue("P_GS_TAB_Remunerations_Avant_DateDecision"));
				
				Indemnite indemnite = new Indemnite();
				indemnite.setAutres((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Autres"));
				indemnite.setDeplacement((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Deplacement"));
				indemnite.setLogement((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Logement"));
				indemnite.setRepresentation((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Representation"));
				indemnite.setTransport((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Transport"));
				indemnite.setDateDecision((Date) remuneration.getValue("P_GS_TAB_Remunerations_Ind_DateDecision"));
				
				AddRemuneration(anneRemuneration, SBA, SBM, SNM, bonus, avantage, indemnite, autres,dateDecision , personnelMatricule);
			}
			
			
			for(ILinkedResource diplome : tableauDiplomes){
				Date dateObtention = (Date) diplome.getValue("P_GS_TAB_diplomes_dateObte");
				String specialite = (String) diplome.getValue("P_GS_TAB_diplomes_Specialte");
				String typeDiplome = (String) diplome.getValue("P_GS_TAB_diplomes_Type");
				String institut = (String) diplome.getValue("P_GS_TAB_diplomes_Institut");
				addDiplomeToPersonnel(diplome, "P_GS_TAB_diplomes_Diplome", personnelMatricule, specialite, dateObtention, typeDiplome, institut);
			}
			
			
			for(String superieur : superieurs){
				addSuperieursToPersonnel(superieur, personnelMatricule);
			}
			
			
			
			
			addUserToAnnuaireVDoc(iw);
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updatePersonnel(IWorkflowInstance iw,List<ILinkedResource> tableauLangues,List<ILinkedResource> tableauEnfants,List<ILinkedResource> tableauDiplomes,
			List<ILinkedResource> tableauExperiences,List<ILinkedResource> tableauRemunerations,String personnelMatricule, String nom, String nomJeuneFille, String prenom, Date dateNaissance,
			String adresse, String email, String nationalite, String nationalite2, Date dateEmbaucheGroupe, Date dateEmbaucheFiliale, String photo, String situationFamiliale, String nCNSS,
			String nCIMR, String nCMIM, String rib, String agence,float droitAcqui, float reliquatEncours, float reliquatanterieur, String vdocLogin, String etat, String loisir, 
			boolean sexeMasculin,String lieuNaissance,boolean statut, String direction, String poste,String descriptifposte, String filiale, Telephone telephone, CarteNational carteNational, Passeport passeport,Conjoint conjoint,List<String> superieurs,String outilsInformatiques,String LogicielsGestion)
	{
		try
		{
			java.sql.Date datesql = new java.sql.Date(0);
			String req = "update Personnel set DirectionIdDirection = ? ,Nom = ? ,Prenom = ?,NomdeJeuneFille = ?,DateNaissance = ?,Adresse = ?,"
					+ " Nationalite = ?, Nationalite2 = ?, DateEmbaucheGroupe = ?, DateEmbaucheFiliale = ?, Photo = ?,SituationFamiliale = ?,NCNSS = ?,NCIMR = ?,NCMIM = ?,"
					+ "RIB = ?, Agence = ?, NombreJoursCongeAnnuel = ?, NombrJoursDispo = ?, reliquatAnneEnCours = ?, Etat = ?,sexeMasculin = ?,"
					+ "lieuNaissance = ?,Loisir = ?,JustifNationalite2 = ?,JustifRIB = ?,JustifSituationFamiliale = ?,TypeJustifSituationFamiliale = ? where Matricule = ? ;"
					+ ""
					+ "update Competence set OutilsInformatiques = ? ,LogicielsDeGestion =  ? where PersonnelMatricule = ? ";
			PreparedStatement st = connection.prepareStatement(req);
			//
			st.setString(1, direction);
			st.setString(2, nom);
			st.setString(3, prenom);
			st.setString(4, nomJeuneFille);
			datesql = new java.sql.Date(dateNaissance.getTime());
			st.setDate(5, datesql);
			st.setString(6, adresse);
			st.setString(7, nationalite);
			st.setString(8, nationalite2);
			datesql.setTime(dateEmbaucheGroupe.getTime());
			st.setDate(9, datesql);
			datesql.setTime(dateEmbaucheFiliale.getTime());
			st.setDate(10, datesql);
			st.setString(11, photo);
			copyImageTo(iw, "P_GS_Photo", personnelMatricule);
			copyPJToVDocWar(iw, "P_GS_Photo", personnelMatricule, "PHOTO");
			st.setString(12, situationFamiliale);
			st.setString(13, nCNSS);
			st.setString(14, nCIMR);
			st.setString(15, nCMIM);
			st.setString(16, rib);
			st.setString(17, agence);
			st.setFloat(18, droitAcqui);
			st.setFloat(19, reliquatanterieur);
			st.setFloat(20, reliquatEncours);
			st.setString(21, etat);
			st.setBoolean(22, sexeMasculin);
			st.setString(23, lieuNaissance);
			st.setString(24, loisir);
			
			st.setString(25, nationalite2!=null ? copyPJToVDocWar(iw, "P_GS_ListeNationalite2PJ", personnelMatricule, "JUSTIFNATIONALITE2"): null);
			st.setString(26, copyPJToVDocWar(iw, "P_GS_RIBPJ", personnelMatricule, "JUSTIFRIB"));
			
			
			if(situationFamiliale.equals("Marié")){
				st.setString(27, copyPJToVDocWar(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", personnelMatricule, "ACTEDEMARIAGE"));
				st.setString(28, "actemariage");
			}
				
			else if(situationFamiliale.equals("Divorcé")){
				st.setString(27, copyPJToVDocWar(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", personnelMatricule, "ACTEDIVORCE"));
				st.setString(28, "actedivorce");
			}
				
			else if(situationFamiliale.equals("Veuve")){
				st.setString(27, copyPJToVDocWar(iw, "P_GS_JUSTIFSITUATIONFAMILIALE", personnelMatricule, "ACTEDECES"));
				st.setString(28, "actedeces");
			}
				
			else {
				st.setString(27, null);
				st.setString(28, null);
			}
			
			
			st.setString(29, personnelMatricule);
			
			st.setString(30, outilsInformatiques);
			st.setString(31, LogicielsGestion);
			st.setString(32, personnelMatricule);
			
			
			
			
			
			
			st.executeUpdate();
			
			deleteInfosUserForUpdate(personnelMatricule);
			
			addCarteNationalToPersonnel(carteNational.getCIN(),carteNational.getDateExperation(),personnelMatricule,iw);
			addPassePortToPersonnel(passeport.getNumPassport(), personnelMatricule, passeport.getDateDelivrance(), passeport.getDateExperation(),iw);
			addTelephoneToPersonnel(telephone.getTel1(), telephone.getTel2(), telephone.getTelUrgences(), telephone.getTelContactUrgence(), personnelMatricule);
			
			if(situationFamiliale.equals("Marié"))
			addConjointToPersonnel(conjoint.getCin(), conjoint.getNom(), conjoint.getPrenom(), conjoint.getDateNaissance(), personnelMatricule, conjoint.getNcmim(),iw);
			
			addPosteToPersonnel(poste, descriptifposte, personnelMatricule);
			addCompetenceToPersonnel(outilsInformatiques, LogicielsGestion, personnelMatricule);
			
			for(ILinkedResource langue : tableauLangues){
				String libellelangue = (String) langue.getValue("P_GS_TAB_Langues_Langue");
				String matriseLangue = (String) langue.getValue("P_GS_TAB_Langues_Maitrise");
				addLangueToPersonnel(libellelangue, matriseLangue, personnelMatricule);
			}
			
			if(!situationFamiliale.equals("Célibataire"))
			for(ILinkedResource enfant : tableauEnfants){
				String nomEnfant = (String) enfant.getValue("P_GS_TAB_Enfant_Nom");
				String prenomEnfant = (String) enfant.getValue("P_GS_TAB_Enfant_Prenom");
				Date dateNaissanceEnfant = (Date) enfant.getValue("P_GS_TAB_Enfant_DateNa");
				String ncmimEnfant= (String) enfant.getValue("P_GS_CMIM_Enfant");
				addEnfantToPersonnel(nomEnfant, prenomEnfant, dateNaissanceEnfant, ncmimEnfant, personnelMatricule,enfant);
			}
			
			
			for(ILinkedResource experience : tableauExperiences){
				Period periode = (Period) experience.getValue("P_GS_TAB_Experience_periode");
				Date dateDeb = periode.getStartDate();
				Date dateFin = periode.getEndDate();
				String societeAcceuil = (String) experience.getValue("P_GS_TAB_Experience_Societe");
				String fonction = (String) experience.getValue("P_GS_TAB_Experience_Taches");
				String typeContrat = (String) experience.getValue("P_GS_TAB_Experience_Type");
				String statutExperience = (String) experience.getValue("P_GS_TAB_Experience_Statut");
				addExperienceToPersonnel(dateDeb, dateFin, societeAcceuil, fonction, statutExperience, typeContrat, personnelMatricule,experience);
			}
			
			
			for(ILinkedResource remuneration : tableauRemunerations){
				
				float anneRemuneration = (float) remuneration.getValue("P_GS_TAB_Remunerations_Anne");
				float SBA = (float) remuneration.getValue("P_GS_TAB_Remunerations_SBA");
				float SBM = (float) remuneration.getValue("P_GS_TAB_Remunerations_SBM");
				float SNM = (float) remuneration.getValue("P_GS_TAB_Remunerations_SNM");
				float bonus = (float) remuneration.getValue("P_GS_TAB_Remunerations_BONUS");
				float autres = (float) remuneration.getValue("P_GS_TAB_Remunerations_AUTRES");
				Date dateDecision = (Date) remuneration.getValue("P_GS_TAB_Remunerations_DateDecision");
				
				Avantage avantage = new Avantage();
				avantage.setAbonnementsGSM((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_AG"));
				avantage.setAutres((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_AUTRES"));
				avantage.setBilletAvion((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_BA"));
				avantage.setForfaitCarburants((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_FC"));
				avantage.setLogementFonction((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_LF"));
				avantage.setVehiculeFonction((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_VF"));
				avantage.setVehiculeService((float) remuneration.getValue("P_GS_TAB_Remunerations_Avant_VS"));
				avantage.setDatedecision((Date) remuneration.getValue("P_GS_TAB_Remunerations_Avant_DateDecision"));
				
				Indemnite indemnite = new Indemnite();
				indemnite.setAutres((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Autres"));
				indemnite.setDeplacement((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Deplacement"));
				indemnite.setLogement((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Logement"));
				indemnite.setRepresentation((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Representation"));
				indemnite.setTransport((float) remuneration.getValue("P_GS_TAB_Remunerations_Ind_Transport"));
				indemnite.setDateDecision((Date) remuneration.getValue("P_GS_TAB_Remunerations_Ind_DateDecision"));
				
				AddRemuneration(anneRemuneration, SBA, SBM, SNM, bonus, avantage, indemnite, autres,dateDecision , personnelMatricule);
			}
			
			
			for(ILinkedResource diplome : tableauDiplomes){
				Date dateObtention = (Date) diplome.getValue("P_GS_TAB_diplomes_dateObte");
				String specialite = (String) diplome.getValue("P_GS_TAB_diplomes_Specialte");
				String typeDiplome = (String) diplome.getValue("P_GS_TAB_diplomes_Type");
				String institut = (String) diplome.getValue("P_GS_TAB_diplomes_Institut");
				addDiplomeToPersonnel(diplome, "P_GS_TAB_diplomes_Diplome", personnelMatricule, specialite, dateObtention, typeDiplome, institut);
			}
			
			
			for(String superieur : superieurs){
				addSuperieursToPersonnel(superieur, personnelMatricule);
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void deleteInfosUserForUpdate(String matricule){
		try{
			
			String req = "delete from CarteNational where Personnelmatricule = ? "
					+ "delete from Conjoint where Personnelmatricule = ? "
					+ "delete from Diplome where Personnelmatricule = ? "
					+ "delete from Enfant where Personnelmatricule = ? "
					+ "delete from Experience where Personnelmatricule = ? "
					+ "delete from Passeport where Personnelmatricule = ? "
					+ "delete from Poste where Personnelmatricule = ? "
					+ "delete from Remuneration where Personnelmatricule = ?  "
					+ "delete from Superieur where Personnelmatricule = ? "
					+ "delete from Telephone where Personnelmatricule = ? "
					+ "delete from PersonnelLangue where Personnelmatricule = ? "
					+ "delete from Competence where Personnelmatricule = ? ";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, matricule);
			st.setString(2, matricule);
			st.setString(3, matricule);
			st.setString(4, matricule);
			st.setString(5, matricule);
			st.setString(6, matricule);
			st.setString(7, matricule);
			st.setString(8, matricule);
			st.setString(9, matricule);
			st.setString(10, matricule);
			st.setString(11, matricule);
			st.setString(12, matricule);
			st.executeUpdate();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String copyImageTo(IWorkflowInstance iw,String sysname, String personnelMatricule) throws IOException
	{
		try
		{
			
			List<IAttachment> docfp = (List<IAttachment>) iw.getValue(sysname);
			String filename = "";
			if (docfp.size() > 0)
			{
				
				filename = docfp.get(0).getShortName().contains(personnelMatricule)?docfp.get(0).getShortName():personnelMatricule +"_"+docfp.get(0).getShortName();
				File newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//skins//ImageFicheSalarie//" + filename);
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
				
				newFile = new File("C://Tomcat//webapps//FicheSalarie//ImageFicheSalarie//" + filename);
				newFile.createNewFile();
				is = docfp.get(0).getInputStream();
				os = new FileOutputStream(newFile);
				buffer = new byte[is.available()];
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
//				EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//skins//ImageFicheSalarie//" + filename,"C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//skins//ImageFicheSalarie//" + filename);
//				EncryptionFile.crypter("C://Tomcat//webapps//FicheSalarie//ImageFicheSalarie//" + filename,"C://Tomcat//webapps//FicheSalarie//ImageFicheSalarie//" + filename);

			}
			
			return filename;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void AddRemuneration(float anneRemuneration, float SBA, float SBM, float SNM, float bonus, Avantage avantage, Indemnite indemnite, float autres,Date dateDecision, String personnelMatricule)
	{
		
		try
		{
			String req = "insert into Remuneration (SBA,SBM,SNM,Bonus,autres,anneeRemunetation,Personnelmatricule,DateDecision) values(?,?,?,?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setFloat(1, SBA);
			st.setFloat(2, SBM);
			st.setFloat(3, SNM);
			st.setFloat(4, bonus);
			st.setFloat(5, autres);
			st.setFloat(6, anneRemuneration);
			st.setString(7, personnelMatricule);
			java.sql.Date datesql = dateDecision!=null?new java.sql.Date(dateDecision.getTime()):null;
			st.setDate(8, datesql);
			st.executeUpdate();
			
			String idRemuneration = getIDRemuneration(anneRemuneration, personnelMatricule);
			req = "insert into Avantage (VehiculeDeFonction,VehiculeDeService,ForfaitCarburants,LogementDeFonction,AbonnementGsm,BilletsDavion,Autres,IdRemuneration,DateDecision) values(?,?,?,?,?,?,?,?,?)";
			st = connection.prepareStatement(req);
			st.setFloat(1, avantage.getVehiculeFonction());
			st.setFloat(2, avantage.getVehiculeService());
			st.setFloat(3, avantage.getForfaitCarburants());
			st.setFloat(4, avantage.getLogementFonction());
			st.setFloat(5, avantage.getAbonnementsGSM());
			st.setFloat(6, avantage.getBilletAvion());
			st.setFloat(7, avantage.getAutres());
			st.setString(8, idRemuneration);
			datesql = avantage.getDatedecision()!=null?new java.sql.Date(avantage.getDatedecision().getTime()):null;
			st.setDate(9, datesql);
			st.executeUpdate();
			
			req = "insert into Indemnite (Logement,Representation,Deplacement,Transport,Autres,IdRemuneration,DateDecision) values(?,?,?,?,?,?,?)";
			st = connection.prepareStatement(req);
			st.setFloat(1, indemnite.getLogement());
			st.setFloat(2, indemnite.getRepresentation());
			st.setFloat(3, indemnite.getDeplacement());
			st.setFloat(4, indemnite.getTransport());
			st.setFloat(5, indemnite.getAutres());
			st.setString(6, idRemuneration);
			datesql = indemnite.getDateDecision()!=null?new java.sql.Date(indemnite.getDateDecision().getTime()):null;
			st.setDate(7, datesql);
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addSuperieursToPersonnel(String superieurMatricule, String personnelMatricule)
	{
		try
		{
			String req = "insert into Superieur(PersonnelMatricule,SupMatricule) values(?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, personnelMatricule);
			st.setString(2, superieurMatricule);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addDiplomeToPersonnel(ILinkedResource ligneTableauDiplome, String sysNamePJ, String personnelMatricule, String specialite, Date dateObtention, String typeDiplome, String institut)
	{
		try
		{
			String req = "insert into Diplome(Personnelmatricule,Specialite,DateObtention,TypeDiplome,Institut,PJ_Diplome) values(?,?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, personnelMatricule);
			st.setString(2, specialite);
			java.sql.Date datesql = new java.sql.Date(dateObtention.getTime());
			st.setDate(3, datesql);
			st.setString(4, typeDiplome);
			st.setString(5, institut);
			st.setString(6, copyPJDiplomeToVDocWar(ligneTableauDiplome, sysNamePJ, personnelMatricule));
			
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String copyPJDiplomeToVDocWar(ILinkedResource ligneTableauDiplome, String sysnamePJ, String personnelMatricule) throws IOException
	{
		try
		{
			List<IAttachment> diplomesPJ = (List<IAttachment>) ligneTableauDiplome.getValue(sysnamePJ);
			String filename = null;
			if(diplomesPJ!=null)
			if (diplomesPJ.size() > 0)
			{
				filename =  diplomesPJ.get(0).getShortName().contains(personnelMatricule)?diplomesPJ.get(0).getShortName():personnelMatricule +"_"+diplomesPJ.get(0).getShortName();
				File newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//DIPLOMES//" + filename);
				newFile.createNewFile();
				InputStream is = diplomesPJ.get(0).getInputStream();
				OutputStream os = new FileOutputStream(newFile);
				byte[] buffer = new byte[is.available()];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
				EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//DIPLOMES//" + filename,"C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//DIPLOMES//" + filename);

			}
			
			return filename;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static String copyPJToVDocWar(IWorkflowInstance iWorkflowInstance, String sysnamePJ, String personnelMatricule, String repertoirePJ) throws IOException
	{
		try
		{
			
			List<IAttachment> pjs = (List<IAttachment>) iWorkflowInstance.getValue(sysnamePJ);
			
			String filename = null;
			int i = 0;
			for(IAttachment pj : pjs)
			{
				filename =  pj.getShortName().contains(personnelMatricule)?i+pj.getShortName():personnelMatricule+ i +"_"+pj.getShortName();
				File newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+repertoirePJ+"//" + filename);
				newFile.createNewFile();
				InputStream is = pj.getInputStream();
				OutputStream os = new FileOutputStream(newFile);
				byte[] buffer = new byte[is.available()];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
				EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+repertoirePJ+"//" + filename, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+repertoirePJ+"//" + filename);

				i++;
			}
			
			return filename;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static String copyPJToVDocWar(ILinkedResource ligneTableauDiplome, String sysnamePJ, String personnelMatricule, String repertoirePJ) throws IOException
	{
		try
		{
			List<IAttachment> diplomesPJ = (List<IAttachment>) ligneTableauDiplome.getValue(sysnamePJ);
			String filename = null;
			int i=0;
			for(IAttachment pj : diplomesPJ)
			{
				filename =  pj.getShortName().contains(personnelMatricule)?i+pj.getShortName():personnelMatricule+i +"_"+pj.getShortName();
				File newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+repertoirePJ+"//" + filename);
				newFile.createNewFile();
				InputStream is = diplomesPJ.get(0).getInputStream();
				OutputStream os = new FileOutputStream(newFile);
				byte[] buffer = new byte[is.available()];
				int length;
				while ((length = is.read(buffer)) > 0)
				{
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
				
				EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+repertoirePJ+"//" + filename, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//FicheSalarie//"+personnelMatricule+"//"+repertoirePJ+"//" + filename);

				i++;
			}
			
			return filename;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void addExperienceToPersonnel(Date dateDeb, Date dateFin, String societeAcceuil, String fonction, String statut, String typeContrat, String personnelMatricule,ILinkedResource experience)
	{
		try
		{
			String req = "insert into Experience(DateDebut,DateFin,SocieteAcceuil,Statut,TypeContrat,Mission,Personnelmatricule,Justif) values(?,?,?,?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			java.sql.Date datesql = new java.sql.Date(dateDeb.getTime());
			st.setDate(1, datesql);
			datesql = new java.sql.Date(dateFin.getTime());
			st.setDate(2, datesql);
			st.setString(3, societeAcceuil);
			st.setString(4, statut);
			st.setString(5, typeContrat);
			st.setString(6, fonction);
			st.setString(7, personnelMatricule);
			st.setString(8, copyPJToVDocWar(experience, "P_GS_TAB_Experience_Attestation", personnelMatricule, "JUSTIFEXPERIENCE"));
			st.executeUpdate();
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addEnfantToPersonnel(String nom, String prenom, Date dateNaissance, String CMIM, String personnelMatricule,ILinkedResource enfant)
	{
		
		try
		{
			String req = "insert into Enfant(Nom,Prenom,Datenaissance,NCMIM,PersonnelMatricule,Justif) values(?,?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, nom);
			st.setString(2, prenom);
			java.sql.Date datesql = new java.sql.Date(dateNaissance.getTime());
			st.setDate(3, datesql);
			st.setString(4, CMIM);
			st.setString(5, personnelMatricule);
			st.setString(6, copyPJToVDocWar(enfant, "P_GS_ExtraitActeNaissance", personnelMatricule, "EXTRAITDENAISSANCE"));
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addLangueToPersonnel(String langue, String matrise, String personnelMatricule)
	{
		
		try
		{
			String req = "insert into PersonnelLangue(idLangue,PersonnelMatricule,Maitrise) values(?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setInt(1, getIdLangue(langue));
			st.setString(2, personnelMatricule);
			st.setString(3, matrise);
			
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addConjointToPersonnel(String cin, String nom, String prenom, Date dateNaissance, String personnelMatricule, String nCMIM,IWorkflowInstance iw)
	{
		try
		{
			String req = "insert into Conjoint(Cin,Nom,Prenom,DateNaissance,NCMIM,PersonnelMatricule,CopieCin) values(?,?,?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, cin);
			st.setString(2, nom);
			st.setString(3, prenom);
			java.sql.Date datesql = new java.sql.Date(dateNaissance.getTime());
			st.setDate(4, datesql);
			st.setString(5, nCMIM);
			st.setString(6, personnelMatricule);
			
			st.setString(7, copyPJToVDocWar(iw, "P_GS_CINConjointPJ", personnelMatricule, "CINCONJOINT"));
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void addTelephoneToPersonnel(String tel1, String tel2, String telUrgences, String telContactUrgence, String personnelMatricule)
	{
		
		try
		{
			String req = "insert into Telephone(Tel1,Tel2,TelUrgences,TelContactUrgence,Personnelmatricule) values(?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, tel1);
			st.setString(2, tel2);
			st.setString(3, telUrgences);
			st.setString(4, telContactUrgence);
			st.setString(5, personnelMatricule);
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addPassePortToPersonnel(String numPassort, String personnelMatricule, Date dateDelivrance, Date dateExpiration,IWorkflowInstance iw)
	{
		try
		{
			String req = "insert into Passeport(NumPassport,Personnelmatricule,DateExperation,DateDelivrance,Justif) values(?,?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, numPassort);
			st.setString(2, personnelMatricule);
			java.sql.Date datesql = new java.sql.Date(dateDelivrance.getTime());
			st.setDate(3, datesql);
			datesql = new java.sql.Date(dateExpiration.getTime());
			st.setDate(4, datesql);
			st.setString(5, copyPJToVDocWar(iw, "P_GS_passePortPJ", personnelMatricule, "PASSEPORT"));
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addCarteNationalToPersonnel(String cin, Date dateExpiration, String personnelMatricule,IWorkflowInstance iw)
	{
		try
		{
			String req = "insert into CarteNational(CIN,DateExperation,Personnelmatricule,Justif) values(?,?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, cin);
			java.sql.Date datesql = new java.sql.Date(dateExpiration.getTime());
			st.setDate(2, datesql);
			st.setString(3, personnelMatricule);
			st.setString(4, copyPJToVDocWar(iw, "P_GS_CINPJ", personnelMatricule, "CIN"));
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPosteToPersonnel(String poste, String descriptif, String personnelMatricule)
	{
		try
		{
			String req = "insert into Poste(LibellePoste,Descriptif,Personnelmatricule) values(?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, poste);
			st.setString(2, descriptif);
			st.setString(3, personnelMatricule);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addCompetenceToPersonnel(String outilsInformatiques, String logicielsDeGestion, String personnelMatricule)
	{
		try
		{
			String req = "insert into Competence(OutilsInformatiques,LogicielsDeGestion,Personnelmatricule) values(?,?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, outilsInformatiques);
			st.setString(2, logicielsDeGestion);
			st.setString(3, personnelMatricule);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addSuperieurToPersonnel(String superieurMatricule, String personnelMatricule)
	{
		try
		{
			String req = "insert into Superieur(PersonnelMatricule,SupMatricule) values(?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, personnelMatricule);
			st.setString(2, superieurMatricule);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Poste getPoste(String idPoste)
	{
		
		Poste poste = new Poste();
		try
		{
			String req = "select IdPoste,LibellePoste,Descriptif from Poste where IdPoste = ?  ";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, idPoste);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				poste.setIdPoste(rs.getString(1));
				poste.setLibellePoste(rs.getString(2));
				poste.setDescriptif(rs.getString(3));
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return poste;
		
	}
	
	public static String getIDRemuneration(float anneRemuneration, String personnelMatricule)
	{
		
		String idRemuneration = "";
		try
		{
			String req = "select idRemuneration from Remuneration where anneeRemunetation = ? and Personnelmatricule = ? ";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setFloat(1, anneRemuneration);
			st.setString(2, personnelMatricule);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				idRemuneration = rs.getString(1);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return idRemuneration;
		
	}
	
	public static Filiale getFiliale(String idFiliale)
	{
		
		Filiale filiale = new Filiale();
		try
		{
			
			String req = "select IdFiliale ,Libelle from Filiale where IdFiliale = ?  ";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, idFiliale);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				filiale.setIdFiliale(rs.getString(1));
				filiale.setLibelle(rs.getString(2));
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return filiale;
		
	}

	public static String getIdDirection(String libelleDirection)
	{
		String iddirection = "";
		try
		{
			
			String req = "select idDirection  from Direction where Libelle = ?  ";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, libelleDirection);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				iddirection = rs.getString(1);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return iddirection;
		
	}
	
	public static int getIdLangue(String libelleLangue)
	{
		int idlangue = 0;
		try
		{
			
			String req = "select idLangue  from Langue where libelleLangue = ?  ";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, libelleLangue);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				idlangue = rs.getInt(1);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return idlangue;
		
	}
	
	public static String getLibelleDirection(String idDirection)
	{
		String libelledirection = "";
		try
		{
			
			String req = "select Libelle  from Direction where idDirection = ?  ";
			PreparedStatement st = connection.prepareStatement(req);
			
			st.setString(1, idDirection);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				libelledirection = rs.getString(1);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return libelledirection;
		
	}
	
	public static String getLoginVdoc(String nom,String prenom){
		String loginVdoc = "";
		if(nom!=null && prenom!=null){
			loginVdoc = prenom.charAt(0)+"."+nom;
			if(ifLoginExist(loginVdoc)){
				loginVdoc = prenom.charAt(0)+""+prenom.charAt(1)+"."+nom;
				if(ifLoginExist(loginVdoc)){
					loginVdoc = prenom.charAt(0)+""+prenom.charAt(1)+""+prenom.charAt(2)+"."+nom;
				}
			}
		}
		
		return loginVdoc;
		
	}
	
	public static boolean ifLoginExist(String loginVdoc){
		try{
			String req = "select loginVdoc from Personnel where loginVdoc = ?";
			PreparedStatement st = connection.prepareStatement(req);
			st.setString(1, loginVdoc);
			ResultSet rs = st.executeQuery();
			int i = 0;
			while(rs.next()){
				i++;
			}
			return i != 0;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static List<String> getListFromSelecteur(Object ob){
		List<String> sups = new ArrayList<String>();
		if(ob instanceof String){
			String ch = ob+"";
			ch= ch.replace("[", "");
			ch= ch.replace("]", "");
			String chs[] = ch.split(",");
			for(String c : chs){
				sups.add(c);
			}
					
		}
		else{
			sups = (List<String>) ob;
		}
		return sups;
	}

	public static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
	
	public static void addUserToAnnuaireVDoc(IWorkflowInstance iw){
		IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
		try
		{
			String filiale = Modules.getWorkflowModule().getLoggedOnUser().getOrganization().getName().toLowerCase();
			String organizationname = filiale.equals("attijariintermediation") ? "Attijariintermediation" : "Attijarifinancescorp";
			String groupname = filiale.equals("attijariintermediation") ? "GROUPE_ATI" : "GROUPE_AFC";
			
			IContext context = iDirectoryModule.getContextByLogin("sysadmin");
			
			
			
			String lastName = ((String) iw.getValue("P_GS_Nom")).toUpperCase();
			String firstName = capitalize((String) iw.getValue("P_GS_Prenom"));
			String eMail = ((String) iw.getValue("P_GS_Mail")).toLowerCase();
			String login = ((String) iw.getValue("P_GS_VdocLogin")).toLowerCase();
			String matricule = (String) iw.getValue("P_GS_Matricule");
			String password = "vdoc";
			String skin = filiale.equals("attijariintermediation") ? "Attijari Inter" : "Attijari Finance";
//			String profilafc = "";
//			if(!filiale.equals("attijariintermediation")){
//				profilafc = (String) iw.getValue("P_GS_ProfilCollabAFC");
//			}
			
			
			if (iDirectoryModule.getUserByLogin(login) == null)
            {
				 iDirectoryModule.beginTransaction();
				 IOrganization organization = iDirectoryModule.getOrganization(context, organizationname);
				 IGroup iGroup = iDirectoryModule.getGroup(context, organization, groupname);
				 IUser iUser = iDirectoryModule.createUser(context, login, password, organization);
				 
				 iUser.setLastName(lastName);
	             iUser.setFirstName(firstName);
	             iUser.setEmail(eMail);              
	             iUser.setEmployeeNumber(matricule);
	             iUser.setSkin(skin);
	             iDirectoryModule.addMember(iUser, iGroup);
	             
	             //add to profil AFC
//	             if(!filiale.equals("attijariintermediation")&&profilafc!=null){
//	            	 IGroup iGroupAFC = iDirectoryModule.getGroup(context, organization, profilafc);
//	            	 iDirectoryModule.addMember(iUser, iGroupAFC );
//	 			 }
	             //--------------------
	             iUser.save(context);
	             iDirectoryModule.commitTransaction();
            }
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getLoginOfUserByHisMatricule(String matricule){
		String userLogin = "";
		 try
			{
			 	String req = "select LoginVdoc from Personnel  where Matricule = ?";
				PreparedStatement st = connection.prepareStatement(req);
				st.setString(1, matricule);
				ResultSet rs = st.executeQuery();
				while(rs.next()){
					userLogin=rs.getString(1);
				}
			}catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
			}
		return userLogin;
	}
}
