package Impression_Dossier;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.attijari.DemandeRemboursement.ConnexionBDD;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.serviceRH.EncryptionFile;

import dao.SingletonConnexionBDD;

public class DossiersMutuelle extends ConnexionBDD {

	private Connection connection;

	public String getFiliale (){
		String filiale = "";
		try{
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String loginVdocOfUser = getWorkflowModule().getLoggedOnUser().getLogin();
			String req = "SELECT FilialeIdFiliale FROM Personnel where loginVdoc = ?";
			PreparedStatement st1 = connection.prepareStatement(req);
			st1.setString(1, loginVdocOfUser);
			ResultSet rs = st1.executeQuery();
			
			while (rs.next())
			{
				filiale = rs.getString(1);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return filiale ;
	}
	
	
	public String getfilRapport(IWorkflowModule im){
		String filiale = "";
		try{
			
			connection = ConnectionDefinition(im).getConnection();
			String loginVdocOfUser = im.getLoggedOnUser().getLogin();
			String req = "SELECT f.Libelle FROM Filiale f, Personnel P  where P.FilialeIdFiliale = f.IdFiliale and P.loginVdoc = ?";
			PreparedStatement st1 = connection.prepareStatement(req);
			st1.setString(1, loginVdocOfUser);
			ResultSet rs = st1.executeQuery();
			
			while (rs.next())
			{
				filiale = rs.getString(1);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return filiale ;
		
	}
	
	
	public void sendEmail(String Assisstante, String Collaborateur, String MsgEmail, IWorkflowInstance iw,ILinkedResource ligne)
	   {    
	      // Recipient's email ID needs to be mentioned.
	      String to = Collaborateur;

	  	// Sender's email ID needs to be mentioned
			String from = Assisstante;
			
			// Assuming you are sending email from 192.168.1.22
			String host = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SMTPADRESS");
			
	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);
	  
	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

	         // Set Subject: header field
	         String refDoc = (String) getWorkflowInstance().getValue("sys_Reference");
	         String refDM = (String) ligne.getValue("IDRM_NumDoss");
	         message.setSubject("Traitement des envoies au CMIM N°="+refDoc);


	         Multipart multipart = new MimeMultipart();
	         BodyPart messagebp = new MimeBodyPart();
	         MsgEmail = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"+
	"<tbody>"+
		"<tr>"+
			"<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Dossier numéro : "+refDM+" envoyé au CMIM </td>"+
		"</tr>"+
		"<tr>"+
			"<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>"+
			"<p>Votre dossier a &eacute;t&eacute; envoy&eacute; au CMIM.</p>"+
			"<div class='section section'>"+
			"<table border='0' cellpadding='0' cellspacing='0' width='100%'>"+
				"<tbody>"+
					"<tr style='display:none;'>"+
						"<td class='section-header'>"+
						"<table border='0' cellpadding='0' cellspacing='0' width='100%'>"+
							"<tbody>"+
								"<tr>"+
									"<td class='text-style1'>&nbsp;</td>"+
									"<td class='section-help'>&nbsp;</td>"+
								"</tr>"+
							"</tbody>"+
						"</table>"+
						"</td>"+
					"</tr>"+
					"<tr>"+
						"<td class='section-content'>"+
						"<table cellpadding='0' cellspacing='0' class='table-border' width='100%'>"+
							"<tbody>"+
								"<tr>"+
									"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Processus</td>"+
									"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Traitement des envoies au CMIM</td>"+
								"</tr>"+
								"<tr>"+
									"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Demandeur</td>"+
									"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String)ligne.getValue("IDRM_Demandeur")+"</td>"+
								"</tr>"+
								"<tr>"+
									"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Etape</td>"+
									"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String)iw.getValue("sys_CurrentSteps")+"</td>"+
								"</tr>"+
								"<tr>"+
									"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>"+
									"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"+(String)iw.getValue("sys_Reference")+"</td>"+
								"</tr>"+
								"<tr>"+
									"<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Statut en cours</td>"+
									"<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Demande de remboursement envoyée au CMIM</td>"+
								"</tr>"+
							"</tbody>"+
						"</table>"+
						"</td>"+
					"</tr>"+
					"<tr>"+
						"<td style='display:none'>&nbsp;</td>"+
					"</tr>"+
				"</tbody>"+
			"</table>"+
			"</div>"+
			"</td>"+
		"</tr>"+
	"</tbody>"+
"</table>"+

"<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";
	         message.setContent(MsgEmail, "text/html");
	        
	         
	         // Send message

	         Transport.send(message);
	         
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	   }
	

	
	public String getCMIM(String NumDossier) {
		String CMIM = "";
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query = "select BeneficiaireNCMIM from DossierBeneficiaire where DossierMutuelleNumDossier = ?";
			st = connection.prepareStatement(query);
			st.setString(1, NumDossier);
			rs = st.executeQuery();
			while (rs.next()) {
				CMIM += rs.getString(1) + "\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CMIM;
	}

	public String GetDemandeur(String Matricule) {
		String Demandeur = null;

		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query = "select nom, prenom from personnel where matricule = ?";
			st = connection.prepareStatement(query);
			st.setString(1, Matricule);
			rs = st.executeQuery();
			while (rs.next()) {

				Demandeur = rs.getString(1) + " " + rs.getString(2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Demandeur;
	}

	@SuppressWarnings("unchecked")
	public void RemplirTab() {
		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("IDRM_Tab");

		if (assoc.size() == 0) {
			try {
				connection = ConnectionDefinition("RH_ATTIJARI")
						.getConnection();
				String query = "SELECT distinct  dm.NumDossier, dm.type, DB.BeneficiaireNCMIM, dm.honoraires,"
						+ "dm.pharmacie, dm.divers, dm.totalARembourser,"
						+ "DB.TypeBeneficiare, P.Nom+' '+P.Prenom as 'Demandeur', C.Nom+' '+C.Prenom, dm.statutValidation, P.Email, P.NCMIM "
						
						+ "FROM DossierMutuelle dm, personnel P, DossierBeneficiaire DB, Conjoint C "
						
						+ "WHERE P.matricule = dm.Personnelmatricule and dm.NumDossier= DB.DossierMutuelleNumDossier "
						+ "and C.NCMIM = DB.BeneficiaireNCMIM and (dm.statutValidation = 'En cours' and dm.etat = 'Receptionner')  and P.FilialeIdFiliale = ? "
						
						+ "union SELECT distinct DM.NumDossier, DM.type, DB.BeneficiaireNCMIM, DM.honoraires, DM.pharmacie,"
						+ "DM.divers, DM.totalARembourser, DB.TypeBeneficiare,"
						+ "P.Nom+' '+P.Prenom as 'Demandeur', E.Nom+' '+E.Prenom, DM.statutValidation, P.Email, P.NCMIM "
						
						+ "FROM DossierMutuelle DM, personnel p, DossierBeneficiaire DB, Enfant E "
						
						+ "WHERE P.matricule = DM.Personnelmatricule and DM.NumDossier = DB.DossierMutuelleNumDossier "
						+ "and E.NCMIM = DB.BeneficiaireNCMIM and (DM.statutValidation = 'En cours' "
						+ "and DM.etat = 'Receptionner') and P.FilialeIdFiliale = ? "
						
						+ "union SELECT distinct DM.NumDossier, DM.type, DB.BeneficiaireNCMIM, DM.honoraires, "
						+ "DM.pharmacie, DM.divers, DM.totalARembourser, DB.TypeBeneficiare,"
						+ "P.Nom+' '+P.Prenom as 'Demandeur',P.Nom+' '+P.Prenom,  DM.statutValidation, P.Email, P.NCMIM "
						
						+ "FROM DossierMutuelle DM, personnel p, DossierBeneficiaire DB"
						+ " WHERE P.matricule = DM.Personnelmatricule "
						+ "and DM.NumDossier = DB.DossierMutuelleNumDossier"
						+ " and P.NCMIM = DB.BeneficiaireNCMIM and (DM.statutValidation = 'En cours' and DM.etat = 'Receptionner') and P.FilialeIdFiliale = ?";

				st = connection.prepareStatement(query);
				st.setString(1, getFiliale());
				st.setString(2, getFiliale());
				st.setString(3, getFiliale());
				ResultSet rst = st.executeQuery();
				while (rst.next()) {
					ILinkedResource associa = getWorkflowInstance().createLinkedResource("IDRM_Tab");

					associa.setValue("IDRM_NumDoss", rst.getString(1));
					associa.setValue("IDRM_Type", rst.getString(2));
					// associa.setValue("IDRM_DateDemande", rst.getString(3));
					associa.setValue("IDRM_CMIM_Beneficiare", rst.getString(3));
					associa.setValue("IDRM_Demandeur", rst.getString(9));
					associa.setValue("IDM_Honoraire", rst.getFloat(4));
					associa.setValue("IDM_Pharmacie", rst.getFloat(5));
					associa.setValue("IDM_Divers", rst.getFloat(6));
					associa.setValue("IDM_Total", rst.getFloat(7));
					associa.setValue("IDM_Type_Beneficiare", rst.getString(8));
					associa.setValue("IDM_Beneficiare", rst.getString(10));
					associa.setValue("IDM_Statut", rst.getString(11));
					associa.setValue("IDM_Email", rst.getString(12) );
					associa.setValue("IDM_EmailBis", rst.getString(12)+";" );
					associa.setValue("IDRM_CMIM", rst.getString(13) );

					getWorkflowInstance().addLinkedResource(associa);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onAfterLoad() {
		RemplirTab();
		
		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("IDRM_Tab");
		if(assoc.size()==0){
			
		}
		return super.onAfterLoad();
	}

	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals("Imprimer")){
			List<IAttachment> listRapports = (List<IAttachment>) getWorkflowInstance().getValue("IDRM_RapportImp");
			if(listRapports==null){
				getResourceController().alert("Veuillez générer le compte rendu !!!");
				return false;
			}
			else if(listRapports!=null && listRapports.size()==0){
				getResourceController().alert("Veuillez générer le compte rendu !!!");
				return false;
			}
		}
		return super.onBeforeSubmit(action);
	}
	
	@Override
	public boolean onAfterSubmit(IAction action) {

		//genererPDF();
		
		if(action.getName().equals("Imprimer")){
		String Ref = (String) getWorkflowInstance().getValue("sys_Reference");
		String email_assi = getWorkflowInstance().getCreatedBy().getEmail();
		
		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("IDRM_Tab");
		if (assoc.size() != 0) {
			for (ILinkedResource ligne : assoc) {

				String NumDoss = (String) ligne.getValue("IDRM_NumDoss");
				String Statut = (String) ligne.getValue("IDM_Statut");
				String Email_Collab = (String) ligne.getValue("IDM_Email");
				updateDossier(NumDoss);
				/*String Msg ="Nom de processus : Impression des dossiers mutuelles. \n Reference de document : "+Ref+"\n Numéro de dossier : "+ NumDoss
						+" \n votre dossier a bien été reçu et envoyer au CMIM pour demander le remboursement ";*/
				sendEmail(email_assi,Email_Collab,"",getWorkflowInstance(),ligne);
			}
		}
		}

		return super.onAfterSubmit(action);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *generer Bulletin de Transfer
	 *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	public void genererRapportImpression(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource ilr) {
		instance = getWorkflowInstance();

		try {
			String ref = (String) iw.getValue("sys_Reference");
			connection = ConnectionDefinition(im).getConnection();
			Map<String, Object> Parametr = new HashMap<String, Object>();
			String Filiale = getfilRapport(im);
			// Parametr.put("","Intermediation");
			String observations = (String) iw.getValue("IDRM_Observation");
			FileManager path = new FileManager();
			Parametr.put("Filiale", Filiale);
			Parametr.put("observations", observations);
			String nomFichier = "DM_" + ref ;
			String nomFichierPDF = "DM_" + ref + ".pdf";
			
			
			iw.setValue("IDRM_RapportImp", new ArrayList());
			File f = new File("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierPDF);
			f.createNewFile();
			GenererPDF.generer("DossiersMutuelle", path.getOutDir(),nomFichier, Parametr, connection, iw);
			im.addAttachment(iw, "IDRM_RapportImp", nomFichierPDF, f.getAbsolutePath());
			
			EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierPDF);
			EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierPDF);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void genererPDF() {
		instance = getWorkflowInstance();

		try {
			String ref = (String) instance.getValue("sys_Reference");
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			Map<String, Object> Parametr = new HashMap<String, Object>();

			// Parametr.put("","Intermediation");

			FileManager path = new FileManager();
			GenererPDF.generer("DossiersMutuelle", path.getOutDir(),"Dossiers Mutuelle" + ref, Parametr, connection, instance);
			//IAttachment idisk = getWorkflowModule().addAttachment(getWorkflowInstance(), "IDRM_RapportImp", "Dossiers Mutuelle" + ref, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + "Dossiers Mutuelle" + ref);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDossier(String NumDoss) {
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query = "update DossierMutuelle set etat ='Imprimer' , statutValidation = 'En cours' where NumDossier = ?";
			st = connection.prepareStatement(query);
			st.setString(1, NumDoss);
			// st.setString(1, Statut);
			st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
