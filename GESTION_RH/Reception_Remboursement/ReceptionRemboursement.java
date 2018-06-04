package Reception_Remboursement;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

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
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IResource;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.serviceRH.EncryptionFile;

import dao.SingletonConnexionBDD;
import Impression_Dossier.FileManager;
import Impression_Dossier.GenererPDF;

public class ReceptionRemboursement extends ConnexionBDD
{
	
	private static final DecimalFormat DFORMAT = new DecimalFormat("###0.00");
	private static final NumberFormat FORMATTER = new RuleBasedNumberFormat(RuleBasedNumberFormat.SPELLOUT);
	private Connection connection;
	
	public void sendEmailAcc(String Assisstante, String Collaborateur, String MsgEmail, IWorkflowInstance iw, ILinkedResource ligne)
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
		
		try
		{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// Set Subject: header field
			String refDoc = (String) getWorkflowInstance().getValue("sys_Reference");
			String refDM = (String) ligne.getValue("DDA_NumDossier");
			String etatDossier = (String) ligne.getValue("DDA_StatutAccord");
			String motifRefus = (String) ligne.getValue("DRM_Motif");
			message.setSubject("Remboursement mutuelle N°="+refDoc);
			
			Multipart multipart = new MimeMultipart();
			BodyPart messagebp = new MimeBodyPart();
			MsgEmail = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + "<tbody>" + "<tr>"
					+ "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Demande accord numéro : "
					+ refDM
					+ " "
					+ etatDossier
					+ "e par la CMIM</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>"
					+ "<p>Votre dossier a &eacute;t&eacute; "
					+ etatDossier
					+ ".</p>"
					+ "<div class='section section'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"
					+ "<tbody>"
					+ "<tr style='display:none;'>"
					+ "<td class='section-header'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"
					+ "<tbody>"
					+ "<tr>"
					+ "<td class='text-style1'>&nbsp;</td>"
					+ "<td class='section-help'>&nbsp;</td>"
					+ "</tr>"
					+ "</tbody>"
					+ "</table>"
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td class='section-content'>"
					+ "<table cellpadding='0' cellspacing='0' class='table-border' width='100%'>"
					+ "<tbody>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Processus</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Remboursement des dossiers de mutuelle</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Demandeur</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) ligne.getValue("DDA_Demandeur")
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Etape</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) iw.getValue("sys_CurrentSteps")
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) iw.getValue("sys_Reference")
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Statut en cours</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Demande d'accord "
					+ etatDossier + "e</td>" + "</tr>";
			
			if (etatDossier.equals("Dossier refusé"))
				MsgEmail += "<tr>"
						+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Motif de refus</td>"
						+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
						+ motifRefus + "</td>" + "</tr>";
			MsgEmail += "</tbody>" + "</table>" + "</td>" + "</tr>" + "<tr>" + "<td style='display:none'>&nbsp;</td>" + "</tr>" + "</tbody>" + "</table>" + "</div>" + "</td>" + "</tr>" + "</tbody>"
					+ "</table>" +
					
					"<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";
			message.setContent(MsgEmail, "text/html");
			
			// Send message
			
			Transport.send(message);
			
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
		}
	}
	
	public void sendEmailRem(String Assisstante, String Collaborateur, String MsgEmail, IWorkflowInstance iw, ILinkedResource ligne)
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
		
		try
		{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// Set Subject: header field
			String refDoc = (String) getWorkflowInstance().getValue("sys_Reference");
			String refDM = (String) ligne.getValue("RRM_NumDossier");
			String etatDossier = (String) ligne.getValue("DMR_Statut");
			String motifRejet = (String) ligne.getValue("DRM_DR_motif");
			message.setSubject("Remboursement mutuelle N°="+refDoc);
			
			Multipart multipart = new MimeMultipart();
			BodyPart messagebp = new MimeBodyPart();
			MsgEmail = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + "<tbody>" + "<tr>"
					+ "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>Dossier numéro : "
					+ refDM
					+ " "
					+ etatDossier
					+ " par la CMIM</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>"
					+ "<p>Votre dossier a &eacute;t&eacute; "
					+ etatDossier
					+ ".</p>"
					+ "<div class='section section'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"
					+ "<tbody>"
					+ "<tr style='display:none;'>"
					+ "<td class='section-header'>"
					+ "<table border='0' cellpadding='0' cellspacing='0' width='100%'>"
					+ "<tbody>"
					+ "<tr>"
					+ "<td class='text-style1'>&nbsp;</td>"
					+ "<td class='section-help'>&nbsp;</td>"
					+ "</tr>"
					+ "</tbody>"
					+ "</table>"
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td class='section-content'>"
					+ "<table cellpadding='0' cellspacing='0' class='table-border' width='100%'>"
					+ "<tbody>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Processus</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Remboursement des dossiers de mutuelle</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Demandeur</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) ligne.getValue("RRM_Demandeur")
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Etape</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) iw.getValue("sys_CurrentSteps")
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'><label for=''>R&eacute;f&eacute;rence</label></td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) iw.getValue("sys_Reference")
					+ "</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Statut en cours</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Demande de remboursement "
					+ etatDossier + "e</td>" + "</tr>";
			
			if (etatDossier.equals("Rejeté"))
				MsgEmail += "<tr>"
						+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Motif de rejet</td>"
						+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
						+ motifRejet + "</td>" + "</tr>";
			MsgEmail += "</tbody>" + "</table>" + "</td>" + "</tr>" + "<tr>" + "<td style='display:none'>&nbsp;</td>" + "</tr>" + "</tbody>" + "</table>" + "</div>" + "</td>" + "</tr>" + "</tbody>"
					+ "</table>" +
					
					"<div style='text-align:right; margin-top:10px; font-size:10px; font-family: Arial;'>@attijari.ma</div>";
			message.setContent(MsgEmail, "text/html");
			
			// Send message
			
			Transport.send(message);
			
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
		}
	}
	
	public void sendEmailRemboursement(String Assisstante, String TexteEmail, String Collaborateurs)
	{
		// Recipient's email ID needs to be mentioned.
		String to = Collaborateurs;
		
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
		
		try
		{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// Set Subject: header field
			message.setSubject("Remboursement mutuelle");
			
			Multipart multipart = new MimeMultipart();
			BodyPart messagebp = new MimeBodyPart();
			messagebp.setText(TexteEmail);
			multipart.addBodyPart(messagebp);
			
			message.setContent(multipart);
			
			// Send message
			
			Transport.send(message);
			
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
		}
	}
	
	public void sendEmailAccord(String Assisstante, String TexteEmail, String Collaborateurs)
	{
		
		// Recipient's email ID needs to be mentioned.
		String to = Collaborateurs;
		
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
		
		try
		{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// Set Subject: header field
			message.setSubject("Remboursement mutuelle");
			
			Multipart multipart = new MimeMultipart();
			BodyPart messagebp = new MimeBodyPart();
			messagebp.setText(TexteEmail);
			multipart.addBodyPart(messagebp);
			
			message.setContent(multipart);
			
			// Send message
			
			Transport.send(message);
			
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
		}
		
	}
	
	public String getfilRapport(IWorkflowModule im)
	{
		String filiale = "";
		try
		{
			
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
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return filiale;
		
	}
	
	public String getFiliale()
	{
		String filiale = "";
		try
		{
			
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String loginVdocOfUser = getWorkflowModule().getLoggedOnUser().getLogin();
			String req = "SELECT FilialeIdFiliale FROM Personnel where loginVdoc = ?";
			PreparedStatement st1 = connection.prepareStatement(req);
			st1.setString(1, loginVdocOfUser);
			ResultSet rs = st1.executeQuery();
			
			while (rs.next())
			{
				filiale = rs.getString(1);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return filiale;
	}
	
	public String getCMIM(String NumDossier)
	{
		String CMIM = "";
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "select BeneficiaireNCMIM from DossierBeneficiaire where DossierMutuelleNumDossier = ?";
			st = connection.prepareStatement(query);
			st.setString(1, NumDossier);
			ResultSet rlts = st.executeQuery();
			while (rlts.next())
			{
				CMIM += rlts.getString(1);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CMIM;
	}
	
	public String GetDemandeur(String Matricule)
	{
		String Demandeur = "";
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "select nom, prenom from personnel where matricule = ?";
			st = connection.prepareStatement(query);
			st.setString(1, Matricule);
			rs = st.executeQuery();
			while (rs.next())
			{
				
				Demandeur = rs.getString(1) + " " + rs.getString(2);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Demandeur;
	}
	
	public void remplirTabDemandeDaccord()
	{
		
		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("RDM_tab_Accord");
		if (assoc.size() == 0)
		{
			try
			{
				connection = ConnectionDefinition("RH_ATTIJARI").getConnection();
				String query = "Select DM.NumDossier, DM.type, DM.DateDemande ,DM.Personnelmatricule, P.RIB, P.Email " + "from  DossierMutuelle DM, Personnel P"
						+ " where DM.etat <> 'Accorder' and  DM.Personnelmatricule = P.matricule and( DM.ProfilDossier = 'Demande daccord' )" + "and P.FilialeIdFiliale = ? ";
				st = connection.prepareStatement(query);
				st.setString(1, getFiliale());
				ResultSet rst = st.executeQuery();
				while (rst.next())
				{
					
					ILinkedResource associa = getWorkflowInstance().createLinkedResource("RDM_tab_Accord");
					
					associa.setValue("DDA_NumDossier", rst.getString(1));
					associa.setValue("DDA_Type", rst.getString(2));
					associa.setValue("DDA_DateDemande", new Date(rst.getDate(3).getTime()));
					associa.setValue("DDA_CMIM", getCMIM(rst.getString(1)));
					String Mat = rst.getString(4);
					associa.setValue("DDA_Demandeur", GetDemandeur(Mat));
					associa.setValue("DDA_RIB", rst.getString(5));
					associa.setValue("DDA_Emailcoll2", rst.getString(6));
					associa.setValue("DDA_Emailcoll2Bis", rst.getString(6) + ";");
					
					getWorkflowInstance().addLinkedResource(associa);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void RemplirTab()
	{
		
		List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("RRM_tab");
		getWorkflowInstance().deleteLinkedResources(assoc);
		
		// if (assoc.size() == 0) {
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "Select DM.NumDossier, DM.type, DM.DateDemande ,DM.Personnelmatricule, P.RIB, DM.Montant, DM.dateRemboursement, DM.statutRapprotValidation, P.Email,DM.motifRejet "
					+ " from  DossierMutuelle DM, Personnel P "
					+ " where DM.Personnelmatricule = P.matricule and ( DM.statutValidation = 'En cours' and DM.etat = 'Imprimer' and ProfilDossier ='Demande de remboursement') "
					+ "and P.FilialeIdFiliale = ?";
			st = connection.prepareStatement(query);
			st.setString(1, getFiliale());
			ResultSet rst = st.executeQuery();
			while (rst.next())
			{
				ILinkedResource associa = getWorkflowInstance().createLinkedResource("RRM_tab");
				
				associa.setValue("RRM_NumDossier", rst.getString(1));
				associa.setValue("RRM_Type", rst.getString(2));
				associa.setValue("RRM_dateDm", new Date(rst.getDate(3).getTime()));
				associa.setValue("RRM_CMIM", getCMIM(rst.getString(1)));
				String Mat = rst.getString(4);
				associa.setValue("RRM_Demandeur", GetDemandeur(Mat));
				associa.setValue("DMR_RIB", rst.getString(5));
				associa.setValue("RRM_MontantRemboursser", rst.getFloat(6));
				java.sql.Date dsql = rst.getDate(7);
				Date date = dsql != null ? new Date(dsql.getTime()) : null;
				associa.setValue("DMR_DateRemboursement", date);
				associa.setValue("DMR_Statut", rst.getString(8)==null?"En cours":rst.getString(8));
				if(rst.getString(8)!=null)
				associa.setValue("DRM_DR_motif",rst.getString(8).equals("Rejeté") ?rst.getString(10):null);
				associa.setValue("DDA_EmailColl", rst.getString(9));
				associa.setValue("DDA_EmailCollBis", rst.getString(9) + ";");
				
				getWorkflowInstance().addLinkedResource(associa);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// }
	}
	
	public void genererRapportImpression(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource ilr)
	{
		instance = getWorkflowInstance();
		
		try
		{
			String ref = (String) iw.getValue("sys_Reference");
			connection = ConnectionDefinition(im).getConnection();
			Map<String, Object> Parametr = new HashMap<String, Object>();
			FileManager path = new FileManager();
			String Filiale = getfilRapport(im);
			String MembreDir = null;
			String PresiDir = null;
			// Ordre Paiment
			float MTNGlobal = iw.getValue("RRM_MntGlobal") != null ? (float) iw.getValue("RRM_MntGlobal") : 0;
			String var = toLetter(new BigDecimal(MTNGlobal));
			
			Parametr.put("montant", MTNGlobal);
			Parametr.put("montantLettre", var);
			Parametr.put("Filiale", Filiale);
			String nomFichierOrd = "OP" + ref;
			String nomFichierOrdPDF = "OP" + ref + ".pdf";
			File f = new File("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDF);
			
			// getWorkflowInstance().setValue("RDM_Virement", null);
			GenererPDF.generer("OrdrePaiment", path.getOutDir(), nomFichierOrd, Parametr, connection, iw);
			iw.setValue("RDM_Virement", null);
			im.addAttachment(iw, "RDM_Virement", nomFichierOrdPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDF);
			EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDF);
			EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDF);
			
			
			// Rapport dossier remboursés
			// Parametr.put("Filiale", Filiale);
			String nomFichierRap = "DMR" + ref;
			String nomFichierRapPDF = "DMR" + ref + ".pdf";
			f = new File("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierRapPDF);
			f.delete();
			iw.setValue("RDM_RapDosRem", null);
			GenererPDF.generer("DossiersMutuelle_Remboursser", path.getOutDir(), nomFichierRap, Parametr, connection, iw);
			im.addAttachment(iw, "RDM_RapDosRem", nomFichierRapPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierRapPDF);
			EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierRapPDF, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierRapPDF);
			EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierRapPDF);
			// Rapport Ordre de virement ()
			
			if (Filiale.equals("Attijari Finances Corporate"))
			{
				MembreDir = im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC")).getFullName();
				PresiDir =  im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("DG_AFC")).getFullName();
				
			}
			else
			{
				MembreDir = im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("RH_ATI")).getFullName();
				PresiDir = 	im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("DG_ATI")).getFullName();
			}
			
			Parametr.put("Total", MTNGlobal);
			Parametr.put("Filiale", Filiale);
			Parametr.put("MbrDirec", MembreDir);
			Parametr.put("PresiDirec", PresiDir);
			String nomFichierOrdP = "OV" + ref;
			String nomFichierOrdPDFP = "OV" + ref + ".pdf";
			// getWorkflowInstance().setValue("DMR_OrdreV", null);
			f = new File("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDFP);
			f.delete();
			iw.setValue("DMR_OrdreV", null);
			GenererPDF.generer("OrderVirementIndv", path.getOutDir(), nomFichierOrdP, Parametr, connection, iw);
			im.addAttachment(iw, "DMR_OrdreV", nomFichierOrdPDFP, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDFP);
			EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDFP, "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDFP);
			EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\Dossier_Mutuelle\\OUT\\" + nomFichierOrdPDFP);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void genererPDFOrdre(String NomRapport, String NomFichier, BigDecimal MNT, String var)
	{
		instance = getWorkflowInstance();
		
		try
		{
			String ref = (String) instance.getValue("sys_Reference");
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			Map<String, Object> Parametr = new HashMap<String, Object>();
			Parametr.put("montant", MNT);
			Parametr.put("montantLettre", var);
			
			FileManager path = new FileManager();
			GenererPDF.generer(NomRapport, path.getOutDir(), NomFichier + ref, Parametr, connection, instance);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void genererPDF(String NomRapport, String NomFichier)
	{
		instance = getWorkflowInstance();
		
		try
		{
			String ref = (String) instance.getValue("sys_Reference");
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			Map<String, Object> Parametr = new HashMap<String, Object>();
			FileManager path = new FileManager();
			GenererPDF.generer("DossiersMutuelle_Remboursser", path.getOutDir(), NomFichier + ref, Parametr, connection, instance);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateDossierDemandeAccord(String numDoss, String Statut)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "update  DossierMutuelle set statutValidation = ?, etat = 'Accorder' where NumDossier = ?";
			st = connection.prepareStatement(query);
			st.setString(1, Statut);
			st.setString(2, numDoss);
			st.executeUpdate();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void updateProfilDoss(String numDoss)
	{
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "update  DossierMutuelle set  ProfilDossier = 'Demande de remboursement' where NumDossier = ?";
			st = connection.prepareStatement(query);
			st.setString(1, numDoss);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateMontant(float Montant, String NumDossier, Date dateRemboursement, String MntLettre)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "update DossierMutuelle set Montant = ?,MntLettre = ? ,etat= 'Remboursé' , statutValidation = 'Remboursé', dateRemboursement= ?  where NumDossier = ?";
			st = connection.prepareStatement(query);
			st.setFloat(1, Montant);
			st.setString(2, MntLettre);
			st.setString(4, NumDossier);
			java.sql.Date datefil = new java.sql.Date(dateRemboursement.getTime());
			st.setDate(3, datefil);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onAfterLoad()
	{
		RemplirTab();
		remplirTabDemandeDaccord();
		
		return super.onAfterLoad();
	}
	
	public void removeDM(String NumDossier,String motif)
	{
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(), getPortalModule()).getConnection();
			String query = "update  DossierMutuelle set etat = 'Supprimé',motifRejet = ? where NumDossier = ?";
			st = connection.prepareStatement(query);
			st.setString(1, motif);
			st.setString(2, NumDossier);
			st.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		if (action.getName().equals("Envoyer"))
		{
			List<ILinkedResource> listDossiersRemboursement = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("RRM_tab");
			List<IAttachment> listRapports = (List<IAttachment>) getWorkflowInstance().getValue("RDM_Virement");
			if(listDossiersRemboursement.size()!=0){
				if(listRapports==null){
				getResourceController().alert("Veuillez générer le compte rendu !!!");
				return false;
				}
				else if(listRapports!=null && listRapports.size()==0){
					getResourceController().alert("Veuillez générer le compte rendu !!!");
					return false;
				}
				
				listRapports = (List<IAttachment>) getWorkflowInstance().getValue("DMR_OrdreV");
				if(listRapports==null){
					getResourceController().alert("Veuillez générer le compte rendu !!!");
					return false;
				}
				else if(listRapports!=null && listRapports.size()==0){
					getResourceController().alert("Veuillez générer le compte rendu !!!");
					return false;
				}
				
				listRapports = (List<IAttachment>) getWorkflowInstance().getValue("RDM_RapDosRem");
				if(listRapports==null){
					getResourceController().alert("Veuillez générer le compte rendu !!!");
					return false;
				}
				else if(listRapports!=null && listRapports.size()==0){
					getResourceController().alert("Veuillez générer le compte rendu !!!");
					return false;
				}
			}
			
		}
		
		
		return super.onBeforeSubmit(action);
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		
		if (action.getName().equals("Envoyer"))
		{
			
			try
			{
				String Ref = (String) getWorkflowInstance().getValue("sys_Reference");
				String NumDoss = "";
				String Statut = "";
				String email_assi = getWorkflowInstance().getCreatedBy().getEmail();
				List<ILinkedResource> assoc = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("RRM_tab");
				if (assoc.size() != 0)
				{
					for (ILinkedResource ligne : assoc)
					{
						NumDoss = (String) ligne.getValue("RRM_NumDossier");
						float Montant = 0f;
						
						Date dateremboursement = (Date) ligne.getValue("DMR_DateRemboursement");
						Statut = (String) ligne.getValue("DMR_Statut");
						String Email_Collab = (String) ligne.getValue("DDA_EmailColl");
						
						float MTNGlobal = 0f;
						
						if (Statut.equals("Remboursé"))
						{
							MTNGlobal = ligne.getValue("RRM_MontantRemboursser") != null ? (float) ligne.getValue("RRM_MontantRemboursser") : 0f;
							String var = toLetter(new BigDecimal(MTNGlobal));
							Montant = (float) ligne.getValue("RRM_MontantRemboursser");
							updateMontant(Montant, NumDoss, dateremboursement, var);
							/*
							 * String TxtEmail =
							 * "Nom de processus : Réception des remboursement. \n Reference de document : "
							 * +Ref+"\n Numéro de dossier : "+ NumDoss +
							 * " \n votre demande a été accpeter et rembourssé de la part de CMIM \n le montant remboursé est de "
							 * +Montant;
							 */
							// sendEmailRemboursement(email_assi,TxtEmail,Email_Collab);
							sendEmailRem(email_assi, Email_Collab, "", getWorkflowInstance(), ligne);
						}
						
						else if (Statut.equals("Rejeté"))
						{
							String motif = (String) ligne.getValue("DRM_DR_motif");
							removeDM(NumDoss,motif);
							/*
							 * String TxtEmail =
							 * "Nom de processus : Réception des remboursement. \n Reference de document : "
							 * +Ref+"\n Numéro de dossier : "+ NumDoss +
							 * " \n votre demande a été rejeter de la part de CMIM";
							 */
							sendEmailRem(email_assi, Email_Collab, "", getWorkflowInstance(), ligne);
						}
						
						// else if (Statut.equals("En cours")){
						// String TxtEmail =
						// "Nom de processus : Réception des remboursement. \n Reference de document : "+Ref+"\n Numéro de dossier : "+
						// NumDoss
						// + " \n nous n'avons aucune réponse a votre demande";
						// sendEmailRemboursement(email_assi,TxtEmail,Email_Collab);
						// }
					}
				}
				
				List<ILinkedResource> assoc1 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("RDM_tab_Accord");
				if (assoc1.size() != 0)
				{
					for (ILinkedResource ligne1 : assoc1)
					{
						NumDoss = (String) ligne1.getValue("DDA_NumDossier");
						Statut = (String) ligne1.getValue("DDA_StatutAccord");
						float Montant = ligne1.getValue("DDA_MntRmbr") == null ? 0 : (float) ligne1.getValue("DDA_MntRmbr");
						
						String Email_Collab2 = (String) ligne1.getValue("DDA_Emailcoll2");
						if ("Dossier accordé".equals(Statut))
						{
							/*
							 * String TxtEmail =
							 * "Nom de processus : Réception des remboursement. \n Reference de document : "
							 * +Ref+"\n Numéro de dossier : "+ NumDoss +
							 * "Bonjour \n votre demande d'accord a été accepter vous pouvez creer un nouveau dossier pour le remboursement \n le montant accordé est de "
							 * +Montant; sendEmailAccord(email_assi,TxtEmail,Email_Collab2);
							 */
							sendEmailAcc(email_assi, Email_Collab2, "", getWorkflowInstance(), ligne1);
							updateDossierDemandeAccord(NumDoss, Statut);
						}
						else if ("Dossier refusé".equals(Statut))
						{
							/*
							 * String TxtEmail =
							 * "Nom de processus : Réception des remboursement. \n Reference de document : "
							 * +Ref+"\n Numéro de dossier : "+ NumDoss +
							 * "Bonjour \n votre demande d'accord a été refuser";
							 * sendEmailAccord(email_assi,TxtEmail,Email_Collab2);
							 */
							sendEmailAcc(email_assi, Email_Collab2, "", getWorkflowInstance(), ligne1);
							updateDossierDemandeAccord(NumDoss, Statut);
						}
						/*
						 * else if("Dossier en cours".equals(Statut)){ String TxtEmail =
						 * "Nom de processus : Réception des remboursement. \n Reference de document : "
						 * +Ref+"\n Numéro de dossier : "+ NumDoss +
						 * "Bonjour \n nous n'avons aucune réponse a votre demande ";
						 * sendEmailAccord(email_assi,TxtEmail,Email_Collab2); }
						 */
						// if(Statut.equals("Dossier Accepté")){
						// updateProfilDoss(NumDoss);
						// }
					}
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		return super.onAfterSubmit(action);
	}
	
	private static String toLetter(BigDecimal num)
	{
		String[] s = DFORMAT.format(num).split(Pattern.quote(String.valueOf(DFORMAT.getDecimalFormatSymbols().getDecimalSeparator())));
		BigInteger intPart = new BigInteger(s[0]);
		if (s.length == 1)
		{
			return FORMATTER.format(intPart);
		}
		else
		{
			BigInteger decPart = new BigInteger(s[1]);
			return FORMATTER.format(intPart)
			// pour les parties fixes il faudrait faire un
			// resourcebundle
					+ " DIRHAM" + (intPart.intValue() > 1 ? "S" : "") + " et " + FORMATTER.format(decPart) + " CENTIME" + (decPart.intValue() > 1 ? "S" : "");
		}
	}
	
}
