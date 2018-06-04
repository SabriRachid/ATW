package com.attijari.ModificationProcessJourFerie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;

import dao.SingletonConnexionBDD;

public class ModificationDesJoursFeries extends BaseDocumentExtension
{

	/**
	 * 
	 */
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String anneeModifNS;
	private String tableauListeJrFrNS;
	private String libJrFerNS;
	private String nbrJrFerNS;
	private String dateJrFerNS;
	private String idFerNS;
	private String btnValiderNS;
	
	private static final long serialVersionUID = 2654470914422301870L;
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
	{
		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
	}
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		try{
			anneeModifNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("MOD_JRFRAN_ANNEMOD");
			tableauListeJrFrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("MOD_JRFRAN_TABJRFER");
			libJrFerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("LIB_JR_FER");
			nbrJrFerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("NBR_JR");
			dateJrFerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DAT_JR_FER");
			btnValiderNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("MOD_JRFRAN_BTNVALIDER");
			idFerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IDJOURFERIE"); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		try{
			
			if(property.getName().equals(anneeModifNS)){
				String anneeCh = (String) getWorkflowInstance().getValue(anneeModifNS);
				if(anneeCh!=null){
					getWorkflowInstance().setValue(tableauListeJrFrNS, null);
					int anneeInt = Integer.parseInt(anneeCh);
					connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
					String req = "SELECT  idJoursFerie,libelleJoursFerie,nbr_jours_ferie,date_jourferie FROM JourFerie,JourFerieAnnuelle "
							+ "where idJoursFerie in ('AIDADH','AFITR','ALMA','NVHEG') "
							+ "and idJoursFerie = id_jourferie and annee = ?";
					st = connection.prepareStatement(req);
					st.setInt(1, anneeInt);
					ResultSet rs = st.executeQuery();
					List<ILinkedResource> tableJourFerie = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(tableauListeJrFrNS);
					if (tableJourFerie != null)
					{
						if (tableJourFerie.isEmpty())
						{
							while (rs.next())
							{
								ILinkedResource ligne = getWorkflowInstance().createLinkedResource(tableauListeJrFrNS);
								ligne.setValue(libJrFerNS, rs.getString(2));
								ligne.setValue(nbrJrFerNS, rs.getInt(3));
								ligne.setValue(idFerNS, rs.getString(1));
								ligne.setValue(dateJrFerNS, rs.getDate(4));
								getWorkflowInstance().addLinkedResource(ligne);
							}
						}
					}
				}
				else{
					getWorkflowInstance().setValue(tableauListeJrFrNS, null);
				}
				
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		super.onPropertyChanged(property);
	}
	
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try{

		}catch(Exception e){
			e.printStackTrace();
		}
		return super.onBeforeSubmit(action);
	}
	
	@SuppressWarnings("static-access")
	public List<Date> getJourFeries(int annee) {
		
		List<Date> datesFeries = new ArrayList<Date>();
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "SELECT jfa.date_jourferie,jf.libelleJoursFerie,jf.nbr_jours_ferie FROM JourFerieAnnuelle jfa,JourFerie jf  where annee = ? and jfa.id_jourferie = jf.idJoursFerie";
			st = connection.prepareStatement(req);
			st.setInt(1, annee);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				Date date = new Date(rs.getDate(1).getTime());
				datesFeries.add(date);
				
				int nombreJoursDuFete = rs.getInt(3);
				if(nombreJoursDuFete==2){
					Date datePlus1 = date;
					Calendar ca = Calendar.getInstance();
					ca.setTime(datePlus1);
					int newjour = ca.get(ca.DATE)+1;
					ca.set(ca.DATE, newjour);
					datePlus1 = new Date(ca.getTimeInMillis());
					datesFeries.add(datePlus1);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		


		return datesFeries;
	}
	
	@SuppressWarnings("static-access")
	public float nbJours(Date d1, Date d2, boolean notionJourFerie, boolean priseCompteLundi, boolean priseCompteMardi, boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi, boolean priseCompteDimanche)
	{
		
		if (d2.compareTo(d1) <= 0)
			return 0;
		
		// Tableau des jours a prendre en compte
		Boolean[] joursPrisEncompte = new Boolean[]
		{
				priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
		};
		
		Calendar date1 = Calendar.getInstance();
		date1.setTime(d1);
		Calendar date2 = Calendar.getInstance();
		date2.setTime(d2);
		
		// Récupération des jours fériés
		List<Date> joursFeries = new ArrayList<Date>();
		int yeardeb = date1.get(Calendar.YEAR);
		int yearfin = date2.get(Calendar.YEAR);
		for (int i = yeardeb - 1; i <= yearfin; i++)
		{
			joursFeries.addAll(getJourFeries(i));
		}
		
		// Calcul du nombre de jour
		Calendar dateToCompare = Calendar.getInstance();
		
		float nbJour = 0;
		while (date1.before(date2) || (date1.get(date1.YEAR) == date2.get(date2.YEAR) && date1.get(date1.MONTH) == date2.get(date2.MONTH) && date1.get(date1.DATE) < date2.get(date2.DATE)))
		{
			boolean test = false;// joursFeries.contains(date1);
			for (Date ligne : joursFeries)
			{
				dateToCompare.setTime(ligne);
				boolean testyear = date1.get(date1.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				boolean testmonth = date1.get(date1.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				boolean testday = date1.get(date1.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test = true;
				}
			}
			
			if (test == false)
			{
				
				if (joursPrisEncompte[date1.get(Calendar.DAY_OF_WEEK) - 1])
					nbJour++;
			}
			
			date1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return nbJour ;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals(btnValiderNS)){
			try
			{
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				List<ILinkedResource> tableJourFerie = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources(tableauListeJrFrNS);
				String anneeCh = (String) getWorkflowInstance().getValue(anneeModifNS);
				int anneeInt = Integer.parseInt(anneeCh);
				for (ILinkedResource ligne : tableJourFerie)
				{
					String id_jourferie = (String) ligne.getValue(idFerNS);
					Date date_jourferie = (Date) ligne.getValue(dateJrFerNS);
					String insertReq = "update JourFerieAnnuelle set date_jourferie = ? where id_jourferie = ? and annee = ?";
					st = connection.prepareStatement(insertReq);
					java.sql.Date sqlDate = new java.sql.Date(date_jourferie.getTime());
					st.setDate(1, sqlDate);
					st.setString(2, id_jourferie);
					st.setInt(3, anneeInt);
					st.executeUpdate();
				}
				
				String reqGetConge = "select IdConge,Personnelmatricule,DateDeb,DateFin,NbrJoursOuvrables,NombrJoursDispo,TypeConge,maladieComptabilise from Conge,Personnel "
									+"where Conge.Personnelmatricule = Personnel.loginVdoc and Conge.EtatConge not like 'congé annulé' ";
				st = connection.prepareStatement(reqGetConge);
				ResultSet rs = st.executeQuery();
				while(rs.next()){
					String idConge = rs.getString(1);
					String loginVdocOfUser = rs.getString(2);
					float NbrJoursOuvrable = rs.getFloat(5);
					java.sql.Date dateDeb = rs.getDate(3);
					Date dateDebUtil = new Date(dateDeb.getTime());
					java.sql.Date dateFin = rs.getDate(4);
					Date dateFinUtil = new Date(dateFin.getTime());
					boolean decisionComptabilisation = rs.getBoolean(8);
					
					float workingDays = nbJours(dateDebUtil, dateFinUtil,false, true, true, true, true, true, false, false);
					if(workingDays!=NbrJoursOuvrable){
						float nombreJrModifier = workingDays-NbrJoursOuvrable;
						String upGetConge = "update Conge set NbrJoursOuvrables = ? where IdConge = ?";
						st = connection.prepareStatement(upGetConge);
						st.setFloat(1, workingDays);
						st.setString(2, idConge);
						st.executeUpdate();
						String typeconge = rs.getString(7);
						if(typeconge.equals("Normal payé") || (typeconge.equals("Divers")&&decisionComptabilisation==true)|| (typeconge.equals("Maladie")&&decisionComptabilisation==true)){
							setCalcule(loginVdocOfUser, nombreJrModifier);
						}
						else{
							if(nombreJrModifier<0){
								IUser user = getWorkflowModule().getUserByLogin(loginVdocOfUser);
								String msg = "Bonjour "+user.getSex()+" "+user.getFullName()+", <br>";
								msg+="Suite au changement des jours fériés religieux, ";
								msg+="vous avez "+(-nombreJrModifier)+" jour(s) spéciaux à nous compter.";
								String sender = getWorkflowInstance().getCreatedBy().getEmail();
								String receiver = user.getEmail();
								String subject = "Information de changement des jours fériés religieux";
								String server = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
								sendEmail(sender, receiver, msg, subject, server);
							}
							
						}
						
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
	
	 public static void sendEmail(String Assisstante, String Collaborateur, String MsgEmail,String subject,String server)
	  {    
	     // Recipient's email ID needs to be mentioned.
	     String to = Collaborateur;

	 	// Sender's email ID needs to be mentioned
			String from = Assisstante;
			
			// Assuming you are sending email from 192.168.1.22
			String host = server;
			
	     // Get system properties
	     Properties props = System.getProperties();

	     // Setup mail server
	     props.put("mail.smtp.host", host);
	 
	     // Get the default Session object.
	     Session session = Session.getDefaultInstance(props);

	     try{
	        // Create a default MimeMessage object.
	        MimeMessage message = new MimeMessage(session);

	        // Set From: header field of the header.
	        message.setFrom(new InternetAddress(from));

	        // Set To: header field of the header.
	        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

	        // Set Subject: header field
	        message.setSubject(subject);


	        Multipart multipart = new MimeMultipart();
	        BodyPart messagebp = new MimeBodyPart();
	        messagebp.setContent(MsgEmail, "text/html");
	        multipart.addBodyPart(messagebp);
	        
	        message.setContent(multipart);
	       
	        
	        // Send message

	        Transport.send(message);
	        
	     }catch (MessagingException mex) {
	        mex.printStackTrace();
	     }
	  }
	
	public void setCalcule(String loginVdocOfUser,float nbrJrModifie){
		try{
			String req = "SELECT NombrJoursDispo,reliquatAnneEnCours,NombreJoursCongeAnnuel FROM Personnel where loginVdoc = ?";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rsx = st.executeQuery();
			float oldSoldeAnterieur = 0;
			float oldSoldeEnCours = 0;
			float droitAnnuelleInit = 0;
			float nvSANC = 0;
			float nvSANT = 0;
			while (rsx.next())
			{
				oldSoldeAnterieur = rsx.getFloat(1);
				oldSoldeEnCours   = rsx.getFloat(2);
				droitAnnuelleInit = rsx.getFloat(3);
					 
			}
			
			
			if(oldSoldeEnCours<droitAnnuelleInit){
				
				
				if(nbrJrModifie+oldSoldeEnCours>droitAnnuelleInit){
					nvSANC = droitAnnuelleInit;
					nvSANT = nvSANT-nbrJrModifie-droitAnnuelleInit+oldSoldeEnCours;
				}
				else{
					nvSANC = -nbrJrModifie+oldSoldeEnCours;
				}
				
				
			}
			else if(oldSoldeEnCours==droitAnnuelleInit){
				 nvSANT = oldSoldeAnterieur - nbrJrModifie;
				 nvSANC = droitAnnuelleInit;
			}
			
			req = "update Personnel set NombrJoursDispo = ?, reliquatAnneEnCours = ?  where loginVdoc = ?";
			st = connection.prepareStatement(req);
			st.setFloat(1, nvSANT);
			st.setFloat(2, nvSANC);
			st.setString(3, loginVdocOfUser);
			st.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}
