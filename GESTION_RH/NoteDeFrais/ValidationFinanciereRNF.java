package NoteDeFrais;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.serviceRH.EncryptionFile;

import NoteDeFrais.FileManager;
import NoteDeFrais.GenererPDF;
import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class ValidationFinanciereRNF extends ConnexionBDD
{
	
	/*
	 * ========================================= *
	 * @Plateform :VDoc 14.2
	 * @author r.sabri
	 * @Creation_date :14/07/2017 11h11 =========================================
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(ValidationFinanciereRNF.class);
	private static final DecimalFormat DFORMAT = new DecimalFormat("###0.00");
	private static final NumberFormat FORMATTER = new RuleBasedNumberFormat(RuleBasedNumberFormat.SPELLOUT);
	
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
			String refDM = (String) getWorkflowInstance().getValue("sys_Reference");
			String etatDossier = (String) ligne.getValue("RNF_ValiderRNF");
			String msgSubject = "Demande de remboursement des notes de frais numéro " + refDM + " a été";
			String statut = "";
			if(etatDossier.equals("Oui")) {msgSubject+=" accepté.";statut="Remboursée";} else  {msgSubject+=" refusé.";statut="Non remboursée";}
			message.setSubject(msgSubject);
			
			Multipart multipart = new MimeMultipart();
			BodyPart messagebp = new MimeBodyPart();
			MsgEmail = "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" + "<tbody>" + "<tr>"
					+ "<td style='background-color:#105294; padding:6px 12px; font-size:18px; font-weight:bold; color:#f9f9f9; font-family: Arial;'>"
					+ msgSubject+ " </td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td style='background-color:#dde2e4; padding:10px; font-size:12px; color:#105294; font-family: Arial; font-size: 12px'>"
					+ "<p>Votre demande a &eacute;t&eacute; "+statut+".</p>"
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
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>Remboursement des notes de frais</td>"
					+ "</tr>"
					+ "<tr>"
					+ "<td colspan='25' style='background-color:#4b5e67; padding:8px; text-align:left; font-family: Arial; font-size: 12px; color:#fff; border-bottom: 1px solid #d0d5d7' valign='top' width='25%'>Demandeur</td>"
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ (String) ligne.getValue("RNF_DemandeurRNF")
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
					+ "<td colspan='75' style='background-color:#FFFFFF; padding:8px; text-align:left; font-family: Arial; font-size: 12px; border-bottom: 1px solid #d0d5d7' valign='top' width='75%'>"
					+ statut + "</td>" 
					+ "</tr>"
					+ "</tr>";
			
		
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
	
	// ---------------------------------------------------------------------------------------------------------------
	// ONAFTERLOAD
	// ---------------------------------------------------------------------------------------------------------------
	public boolean onAfterLoad()
	{
		getNoteTableValidationFinancier();
		return super.onAfterLoad();
	}
	
	// ---------------------------------------------------------------------------------------------------------------
	// ONPROPERTYCHANGED
	// ---------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	@Override
	public void onPropertyChanged(IProperty property)
	{
		if (property.getName().equals("RNF_Tab_RemboursementNoteFraisV2"))
		{
			// getResourceController().showBodyBlock("FragRapport", false);
			float total = 0f;
			int sizetab = 0;
			List RNF = (List) getWorkflowInstance().getLinkedResources("RNF_Tab_RemboursementNoteFraisV2");
			if (RNF.size() != 0)
			{
				for (Iterator IT = RNF.iterator(); IT.hasNext();)
				{
					ILinkedResource LINK = (ILinkedResource) IT.next();
					if (((String) LINK.getValue("RNF_ValiderRNF")) != null)
					{
						String Statut = (String) LINK.getValue("RNF_ValiderRNF");
						float Montant = (float) LINK.getValue("RNF_MontantVNF");
						if (Statut.equals("Oui"))
						{
							total += Montant;
						}
					}
					else
					{
						sizetab++;
						// getResourceController().showBodyBlock("FragRapport", false);
					}
				}
				if (sizetab >= 1)
				{
					// getResourceController().showBodyBlock("FragRapport", false);
				}
				else
				{
					// getResourceController().showBodyBlock("FragRapport", true);
					// getResourceController().setHidden("RNF_OrderPaiementVF", true);
					// getResourceController().setHidden("RNF_OrderVirementVF", true);
				}
			}
			else
			{
				// getResourceController().showBodyBlock("FragRapport", false);
			}
			getWorkflowInstance().setValue("RNF_MT_VRNF", total);
			// INITIALISER LES CHAMPS PIÉCES JOINTE
			List<IAttachment> docOP = new ArrayList<>();// (List<IAttachment>)
														// getWorkflowInstance().getValue("RNF_OrderPaiementVF");
			docOP.clear();
			getWorkflowInstance().setValue("RNF_OrderPaiementVF", docOP);
			List<IAttachment> docOV = new ArrayList<>();// (List<IAttachment>)
														// getWorkflowInstance().getValue("RNF_OrderVirementVF");
			docOV.clear();
			getWorkflowInstance().setValue("RNF_OrderVirementVF", docOP);
		}
		super.onPropertyChanged(property);
	}
	
	// ---------------------------------------------------------------------------------------------------------------
	// ISONCHANGESUBSCRIPTIONON
	// ---------------------------------------------------------------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property)
	{
		if (property.getName().equals("RNF_Tab_RemboursementNoteFraisV2"))
		{
			return true;
		}
		return super.isOnChangeSubscriptionOn(property);
	}
	
	// ---------------------------------------------------------------------------------------------------------------
	// GETNOTETABLEVALIDATIONFINANCIER
	// ---------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public void getNoteTableValidationFinancier()
	{
		cnx = null;
		st = null;
		rs = null;
		float Mt = 0f;
		try
		{
			instance = getWorkflowInstance();
			String filiale = null;
			//String NumEmploye = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
			String login = getWorkflowModule().getLoggedOnUser().getLogin();
			// Le numéro '458709' => le matricule de a.wafir
			if (login.equals(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC")))
			{
				filiale = "Attijari Finances Corporate";
			}
			else
			{
				filiale = "Attijari Intermédiation";
			}
			// DÉFINITION DE LA CONNEXION
			cnx = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			if (((Collection) instance.getLinkedResources("RNF_Tab_RemboursementNoteFraisV2")).size() == 0)
			{
				String query = "SELECT SUM(Qte_NF*Mt_NF) as 'Montant',Demandeur_NF,Ref_NF,matricule,RIB,Filiale_NF,TypeMission,Email  \r\n"
						+ " FROM NotesFrais,Personnel WHERE matricule=PersonnelMatricule\r\n" + " AND Recept_NF='Oui' and Rembourcer_NF='Encours' and Filiale_NF=? and NotesFrais.statut='validé'\r\n"
						+ " GROUP BY Demandeur_NF,Ref_NF,matricule,RIB,Filiale_NF,TypeMission,Email";
				st = cnx.prepareStatement(query);
				st.setString(1, filiale);
				rs = st.executeQuery();
				while (rs.next())
				{
					// CRÉATION D'UNE LIGNE
					ILinkedResource linkedResource = instance.createLinkedResource("RNF_Tab_RemboursementNoteFraisV2");
					// POSITIONNEMENT DE QUELQUES VALEURS
					linkedResource.setValue("RNF_RefVNF", rs.getString("Ref_NF"));
					linkedResource.setValue("RNF_DemandeurRNF", rs.getString("Demandeur_NF"));
					linkedResource.setValue("RNF_MontantVNF", rs.getFloat("Montant"));
					linkedResource.setValue("RNF_MatriculeRNF", rs.getString("matricule"));
					linkedResource.setValue("RNF_RIBRNF", rs.getString("RIB"));
					linkedResource.setValue("RNF_TypeMissionRNF", rs.getString("TypeMission"));
					linkedResource.setValue("RNF_Email", rs.getString("Email"));
					Mt += rs.getFloat("Montant");
					// AJOUT DE LA LIGNE AU TABLEAU
					instance.addLinkedResource(linkedResource);
				}
				instance.setValue("RNF_MT_VRNF", Mt);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("CS-ERROR IN GETNOTTABLEVALIDATION  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		finally
		{
			// LIBÉRER RESSOURCES DE LA MÉMOIRE.
			// ConnexionBDD.close(cnx, st,rs);
		}
	}
	
	// ==============================================================================================
	// onAfterSubmit
	// ==============================================================================================
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		if (action.getName().equals("Valider2"))
		{
			cnx = null;
			st = null;
			try
			{
				String filiale = null;
				//String NumEmploye = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
				String emailFinancier = getWorkflowModule().getLoggedOnUser().getEmail();
				String login = getWorkflowModule().getLoggedOnUser().getLogin();
				// Le numéro '458709' => le matricule de a.wafir
				if (login.equals(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC")))
				{
					filiale = "Attijari Finances Corporate";
				}
				else
				{
					filiale = "Attijari Intermédiation";
				}
				// MISE À JOURS DES NOTES DE FRAIS REMBOURSÉES
				Collection associ = (Collection) getWorkflowInstance().getLinkedResources("RNF_Tab_RemboursementNoteFraisV2");
				if (associ.size() != 0)
				{
					for (Iterator iter1 = associ.iterator(); iter1.hasNext();)
					{
						ILinkedResource asso = (ILinkedResource) iter1.next();
						// DÉFINITION DE LA CONNEXION
						cnx = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
						String Query = "UPDATE NotesFrais SET Flag ='Demande remboursé', Rembourcer_NF= ?, Statut=? " + "  WHERE Ref_NF = ? and Recept_NF='Oui' and Statut='Validé' and Filiale_NF=?";
						st = cnx.prepareStatement(Query);
						st.setString(1, (String) asso.getValue("RNF_ValiderRNF"));
						st.setString(2, "Clôturer");
						st.setString(3, (String) asso.getValue("RNF_RefVNF"));
						st.setString(4, filiale);
						st.executeUpdate();
						String emailCollab = (String) asso.getValue("RNF_Email");
						sendEmailAcc(emailFinancier, emailCollab, "", getWorkflowInstance(), asso);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				log.error("CS-ERROR IN ONAFTERSUBMIT  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - " + Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			finally
			{
				// LIBÉRER RESSOURCES DE LA MÉMOIRE.
				ConnexionBDD.close(cnx, st);
			}
		}
		return super.onAfterSubmit(action);
	}
	
	/*
	 * =================================================================================================================
	 * onBeforeSubmit
	 * =================================================================================================================
	 */
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		boolean passer = true;
		if (action.getName().equals("Valider2"))
		{
			try
			{
				int count = 0;
				// RECUPERER LES LIGNES DE LE TABLEAU DES NOTES DE FRAIS
				Collection associ = (Collection) getWorkflowInstance().getLinkedResources("RNF_Tab_RemboursementNoteFraisV2");
				if (associ.size() != 0)
				{
					for (Iterator iter1 = associ.iterator(); iter1.hasNext();)
					{
						// CRÉATION D'UNE LIGNE
						ILinkedResource asso = (ILinkedResource) iter1.next();
						// VÉRIFIER SI LES NOTE DE FRAIS SONT VALIDÉ
						if (asso.getValue("RNF_ValiderRNF") == null)
						{
							count++;
						}
					}
					if (count > 0)
					{
						passer = false;
						getResourceController().alert("Une ou plusieurs notes de frais ne sont pas validées.");
					}
				}
				
				
				 List<IAttachment> ordrePaiements = (List<IAttachment>) getWorkflowInstance().getValue("RNF_OrderPaiementVF");
	    		 List<IAttachment> ordreVirements = (List<IAttachment>) getWorkflowInstance().getValue("RNF_OrderVirementVF");
	    		 if(ordreVirements.size()==0||ordrePaiements.size()==0){
	    			 getResourceController().alert("Veuillez générer l'ordre de virement !!!");
	    			 return false;
	    		 }
			}
			catch (Exception e)
			{
				e.getStackTrace();
				log.info("Error in onBeforeSubmit() method : " + e.getClass() + " - " + e.getMessage());
			}
		}
		return passer;
	}
	
	// ---------------------------------------------------------------------------------------------------------------
	// GENERERRAPPORTIMPRESSION ORDRE PAIEMENT
	// ---------------------------------------------------------------------------------------------------------------
	public void genererRapportImpressionOP(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource asso)
	{
		cnx = null;
		st = null;
		try
		{
			String filiale = null;
			//String NumEmploye = im.getLoggedOnUser().getEmployeeNumber();
			String ref = (String) iw.getValue("sys_Reference");
			cnx = ConnectionDefinition(im).getConnection();
			Map<String, Object> Para = new HashMap<String, Object>();
			FileManager path = new FileManager();
			String login = im.getLoggedOnUser().getLogin();
			// Le numéro '458709' => le matricule de a.wafir
			if (login.equals(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC")))
			{
				filiale = "Attijari Finances Corporate";
			}
			else
			{
				filiale = "Attijari Intermédiation";
			}
			float Total = (float) iw.getValue("RNF_MT_VRNF");
			// MISE À JOURS DES NOTES DE FRAIS REMBOURSÉES
			Collection associ = (Collection) iw.getLinkedResources("RNF_Tab_RemboursementNoteFraisV2");
			if (associ.size() != 0)
			{
				for (Iterator iter1 = associ.iterator(); iter1.hasNext();)
				{
					asso = (ILinkedResource) iter1.next();
					String Query = "UPDATE NotesFrais SET Rembourcer_NF= ?, MT_Ref_Lettre=?\r\n" + "  WHERE Recept_NF='Oui' and Ref_NF=? and Statut='Validé'";
					st = cnx.prepareStatement(Query);
					float MonT = (float) asso.getValue("RNF_MontantVNF");
					String Validation = (String) asso.getValue("RNF_ValiderRNF");
					st.setString(1, Validation); // ETAT DE REMBOURSEMENT DES NOTES DE FRAIS
					st.setString(2, NumToLetter(new BigDecimal(MonT))); // CONVERTION DE MONTANT EN LETTRE
					st.setString(3, (String) asso.getValue("RNF_RefVNF")); // REFERENCE DE NOTE DE FRAIS
					st.executeUpdate();
				}
			}
			// PARAMÉTRE
			Para.put("Filiale", filiale);
			Para.put("Total", Total);
			Para.put("montantLettre", NumToLetter(new BigDecimal(Total)));
			Para.put("NumOrdre", ref);
			// GÉNÉRER L'ORDRE DE PAIEMENT
			GenererPDF.generer("OrdrePaiement", path.getOutDir(), "Ordre de Paiement" + "_" + ref, Para, cnx, iw);
			List<IAttachment> docs = (List<IAttachment>) iw.getValue("RNF_OrderPaiementVF");
			// INITIALISER LE CHAMPS PIÉCE JOINTE
			docs.clear();
			im.addAttachment(iw, "RNF_OrderPaiementVF", "Ordre de Paiement" + "_" + ref + ".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"
					+ "_" + ref + ".pdf");
			 EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf");
	            EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Paiement"+"_"+ref+".pdf");
	            
			// ir.setHidden("RNF_OrderPaiementVF", true);
			st.close();
			cnx.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("CS-ERROR IN genererRapportImpression  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		finally
		{
			// LIBÉRER RESSOURCES DE LA MÉMOIRE.
			ConnexionBDD.close(cnx, st);
		}
	}
	
	/*
	 * =================================================================================================================
	 * genererRapportImpressionOV
	 * =================================================================================================================
	 */
	public void genererRapportImpressionOV(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource asso)
	{
		cnx = null;
		st = null;
		try
		{
			String filiale = null;
			String MembreDir = null;
			String PresiDir = null;
			String NumEmploye = im.getLoggedOnUser().getEmployeeNumber();
			String ref = (String) iw.getValue("sys_Reference");
			cnx = ConnectionDefinition(im).getConnection();
			Map<String, Object> Para = new HashMap<String, Object>();
			FileManager path = new FileManager();
			String login = im.getLoggedOnUser().getLogin();
			// Le numéro '458709' => le matricule de a.wafir
			if (login.equals(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC")))
			{
				filiale = "Attijari Finances Corporate";
				MembreDir = im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("RH_AFC")).getFullName();;
				PresiDir = im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("DG_AFC")).getFullName();
			}
			else
			{
				filiale = "Attijari Intermédiation";
				MembreDir = im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("RH_ATI")).getFullName();
				PresiDir = im.getUserByLogin(iw.getCatalog().getConfiguration().getStringUserProperty("DG_ATI")).getFullName();
			}
			float Total = (float) iw.getValue("RNF_MT_VRNF");
			// MISE À JOURS DES NOTES DE FRAIS REMBOURSÉES
			Collection associ = (Collection) iw.getLinkedResources("RNF_Tab_RemboursementNoteFraisV2");
			if (associ.size() != 0)
			{
				for (Iterator iter1 = associ.iterator(); iter1.hasNext();)
				{
					asso = (ILinkedResource) iter1.next();
					String Query = "UPDATE NotesFrais SET Rembourcer_NF= ?, MT_Ref_Lettre=?\r\n" + "  WHERE Recept_NF='Oui' and Ref_NF=? and Statut='Validé'";
					st = cnx.prepareStatement(Query);
					float MonT = (float) asso.getValue("RNF_MontantVNF");
					String Validation = (String) asso.getValue("RNF_ValiderRNF");
					st.setString(1, Validation); // ETAT DE REMBOURSEMENT DES NOTES DE FRAIS
					st.setString(2, NumToLetter(new BigDecimal(MonT))); // CONVERTION DE MONTANT EN LETTRE
					st.setString(3, (String) asso.getValue("RNF_RefVNF")); // REFERENCE DE NOTE DE FRAIS
					st.executeUpdate();
				}
			}
			// PARAMÉTRE
			Para.put("Filiale", filiale);
			Para.put("Rembourser", "Oui");
			Para.put("Total", Total);
			Para.put("MbrDirec", MembreDir);
			Para.put("PresiDirec", PresiDir);
			// GÉNÉRER L'ORDRE DE VIREMENT
			GenererPDF.generer("OrderDeVirement", path.getOutDir(), "Ordre de Virement" + "_" + ref, Para, cnx, iw);
			List<IAttachment> docs = (List<IAttachment>) iw.getValue("RNF_OrderVirementVF");
			// INITIALISER LE CHAMPS PIÉCE JOINTE
			docs.clear();
			im.addAttachment(iw, "RNF_OrderVirementVF", "Ordre de Virement" + "_" + ref + ".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"
					+ "_" + ref + ".pdf");
			 EncryptionFile.crypter("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf");
	            EncryptionFile.deleteFile("C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + "Ordre de Virement"+"_"+ref+".pdf");
	            
			// ir.setHidden("RNF_OrderVirementVF", true);
			st.close();
			cnx.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("CS-ERROR IN genererRapportImpression  METHOD : " + e.getClass() + " - " + e.getMessage() + "  - " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		finally
		{
			// LIBÉRER RESSOURCES DE LA MÉMOIRE.
			ConnexionBDD.close(cnx, st);
		}
	}
	
	/*
	 * =================================================================================================================
	 * NumToLetter
	 * =================================================================================================================
	 */
	private static String NumToLetter(BigDecimal num)
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
			// POUR LES PARTIES FIXES IL FAUDRAIT FAIRE "RESOURCEBUNDLE"
					+ " Dh" + (intPart.intValue() > 1 ? "s" : "") + " et " + FORMATTER.format(decPart) + " ct" + (decPart.intValue() > 1 ? "s" : "");
		}
	}
}
