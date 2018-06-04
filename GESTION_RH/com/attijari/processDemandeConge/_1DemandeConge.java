package com.attijari.processDemandeConge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import beans.Conge;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.structs.Period;
import com.axemble.vdp.ui.core.document.CoreDocument;
import com.serviceRH.ServiceFicheSalarie;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _1DemandeConge extends BaseDocumentExtension
{
	private String rhNS;
	private String supHieNS;
//	private String remplacantNS;
	private String btnenvoyerNS;
	private String naturecongeNS;
	private String typecongeNS;
	private String nbjrpredefNS;
	private String momentsortieNS;
	private String momententreeNS;
	private String dateCongeDebNS;
	private String dateCongeFinNS;
	private String dateCongeDebSpeNS;
	private String dateCongeFinSpeNS;
	private String periodecongenorNS;
	private String nbrjrouvrNS;
	//private String reliquatDroitAnnuelleNS;
	private String justifabsNS;
	private String typecongespeNS;
	private String nbjrpredefspeNS;
	private String periodecongespeNS;
	private String justifabsspeNS;
	private String frag_conge_combineNS;
	private String frag_nbrjrpredefNS;
	private String matinApmidisortieNS;
	private String matinApmidientreeNS;
	private String msgErreurPeriodeCongeNS;
	private String msgErreurPeriodeCongeSpeNS;
	private String nbrjrouvrSpeNS;
	private String matinApmidisortieSpeNS;
	private String matinApmidientreeSpeNS;
	private String momentsortieSpeNS;
	private String momententreeSpeNS;
	private String nbrJrSoldeNS;
	private String nbrJrNonSoldeNS;
	//private String droitAnnCongNS;
	private String motifNorNS;
	private String motifSpeNS;
	private String fragmotifNorNS;
	private String fragmotifSpeNS;
	private String frag_remplacantNS;
	
	private String commentaireNS;
	private String alerteNS;
	private String fragAlerteNS;
	
//	private String droitAnnuelleNS;
//	private String reliquatDroitAnnuelleNS;
	private String soldeAnterieurNS;
	private String reliquatSoldeAnterieurNS;
	private String soldeanneEncoursNS;
	private String reliquatSoldeanneEncoursNS;
	
	private String droitAnnuelleInitNS;
//	private String reliquatDroitAnnuelleInitNS;
//	private String reliquatSoldeAnterieurInitNS;
//	private String reliquatSoldeanneEncoursInitNS;
	
	
	// private String nbrJrTotalSoldeNS;
	// private String nbrJrResteSoldeNS;
	private static final long serialVersionUID = 4269810337595101111L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
	{
		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
	}
	
	
	public List<IUser> getSuperieurOf(String matriculeUser){
		List<IUser> superieurs = new ArrayList<IUser>();
		try{
			String loginVdocOfUser = matriculeUser;
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select p.loginVdoc "
					+ "from Superieur s , Personnel p , Personnel sup  "
					+ "where sup.loginVdoc = ? "
					+ "and s.PersonnelMatricule=sup.matricule "
					+ "and s.SupMatricule=p.matricule";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				String loginSuperieur = rs.getString(1);
				IUser superieur = getWorkflowModule().getUserByLogin(loginSuperieur);
				superieurs.add(superieur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return superieurs;
	}
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		try
		{
			// get nom systeme des champ 
			String ipAdresse = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
			getWorkflowInstance().setValue("URI", "http://"+ipAdresse+"/vdoc"+getWorkflowInstance().getURI());
			supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
			rhNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DIRECTEUR_RESSOURCES_HUMAINES");
//			remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_ALERTE");
			
			commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_COMM");
			frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_REMPLACANT");
			fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_MOTIFSPE");
			fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_MOTIFNOR");
			motifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOTIFSPE");
			motifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOTIFNOR");
			dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEREPRISE");
			dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEENTREE");
			dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEREPRISESPE");
			dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DATEENTREESPE");
			alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_ALERT");
			nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRSOLDCONG");
			nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRNSOLDCONG");
			momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORTSPE");
			momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTRSPE");
			matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORTSPE");
			matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTRSPE");
			nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJROUVSPE");
			msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGMSGSPE");
			
			// remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
			btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_BTNENVOYER");
			naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NATURECONGE");
			typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TYPECONGE");
			nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRDEF");
			momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMSORT");
			momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_MOMENTR");
			periodecongenorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
			nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJROUV");
			//reliquatDroitAnnuelleNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPO");
			justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_JUSTABSC");
			typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TYPECONGESP");
			nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRDEFSPE");
			periodecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
			justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_JUSTABSCSPE");
			frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_DONNE_CONG_COMBINE");
			frag_nbrjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
			matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
			matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
			msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGMSG");
			
			
			
//			droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_DROITANNCONG");
//			reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPO");
			soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
			reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOMODIF");
			soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURS");
			reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIF");
			
			droitAnnuelleInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGEINIT");
			
			String nbrJrPrisNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRPRIS");
			
//			reliquatDroitAnnuelleInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_NBRJRCONGDISPOINIT");
//			reliquatSoldeAnterieurInitNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOMODIFINIT");
//			reliquatSoldeanneEncoursInitNS  = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIFINIT");
			
			
			
			// get nombre dispo du congé
			String loginVdocOfUser = getWorkflowModule().getLoggedOnUser().getLogin();
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
//			String req = "select CASE WHEN SUM(NbrJoursOuvrables) IS NULL THEN (SELECT convert(DOUBLE PRECISION,0)) " + "ELSE (SELECT convert(DOUBLE PRECISION,SUM(NbrJoursOuvrables))) END"
//					+ " from Conge where (TypeConge = 'Normal payé' or (TypeConge='Maladie' and maladieComptabilise = 1) or (TypeConge='Divers' and maladieComptabilise = 1))" + "and Personnelmatricule = ? and EtatConge = 'valide' and (DATEDIFF(YEAR,DateDeb,(select SYSDATETIME()))=0)";
//			st = connection.prepareStatement(req);
//			st.setString(1, loginVdocOfUser);
//			ResultSet rs = st.executeQuery();
			float congePris = 0;
//			while (rs.next())
//			{
//				congePris = rs.getFloat(1);
//			}
//			
			congePris = new ServiceRH().calculateCongePris(loginVdocOfUser);
			
			getWorkflowInstance().setValue(nbrJrPrisNS, congePris);
			
			
			String req = "SELECT NombreJoursConge,NombrJoursDispo,reliquatAnneEnCours,FilialeIdFiliale FROM Personnel where loginVdoc = ? ";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rs = st.executeQuery();
			String filiale = "";
			while (rs.next())
			{
				getWorkflowInstance().setValue(droitAnnuelleInitNS, rs.getFloat(1));
//				getWorkflowInstance().setValue(reliquatDroitAnnuelleNS, rs.getFloat(1));
				getWorkflowInstance().setValue(soldeAnterieurNS, rs.getFloat(2));
				getWorkflowInstance().setValue(soldeanneEncoursNS, rs.getFloat(3));
				
				filiale = rs.getString(4);
			}
			
			if ("attijariintermediation".equals(filiale))
			{
				getResourceController().showBodyBlock(frag_remplacantNS, true);
				// get superieur hierarchique
//				IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
				List<IUser> users = new ArrayList<>();
//				users.add(sup);
				users.addAll(getSuperieurOf(loginVdocOfUser));
//				getWorkflowInstance().setValue(supHieNS, users);
//				getWorkflowInstance().save(supHieNS);
				getWorkflowInstance().setValue(supHieNS, users);
				getWorkflowInstance().save(supHieNS);
				getResourceController().showBodyBlock("FRAG_SUP", false);
//				getWorkflowInstance().setValue("EmailNejjar", "");
				// get rh
				
				
				IUser rh = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI"));
				users = new ArrayList<>();
				users.add(rh);
				getWorkflowInstance().setValue(rhNS, users);
				getWorkflowInstance().save(rhNS);
				
			}
			
//			else if ("attijariwb".equals(filiale))
//			{
//				getResourceController().showBodyBlock(frag_remplacantNS, true);
//				// get superieur hierarchique
////				IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//				List<IUser> users = new ArrayList<>();
////				users.add(sup);
//				users.addAll(getSuperieurOf(loginVdocOfUser));
//				getWorkflowInstance().setValue(supHieNS, users);
//				getWorkflowInstance().save(supHieNS);
//				getResourceController().showBodyBlock("FRAG_SUP", false);
////				getWorkflowInstance().setValue("EmailNejjar", "");
//				// get rh
//				
//				
//				//IUser rh = getWorkflowModule().getUserByLogin("s.kassani");m.oirrach
//				IUser rh = getWorkflowModule().getUserByLogin("m.oirrach");
//				users = new ArrayList<>();
//				users.add(rh);
//				getWorkflowInstance().setValue(rhNS, users);
//				getWorkflowInstance().save(rhNS);
//				
//			}
			else
			{
				getResourceController().showBodyBlock(frag_remplacantNS, false);
				// get superieur hierarchique
//				IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//				IUser sup1 = getWorkflowModule().getLoggedOnUser().getManager();
				List<IUser> users = new ArrayList<>();
				users.addAll(getSuperieurOf(loginVdocOfUser));
//				if(sup1 !=null)
//				users.add(sup1);
//				getResourceController().showBodyBlock("FRAG_SUP", true);
//				IUser connectedUser = getWorkflowModule().getLoggedOnUser();
//				if(!connectedUser.getLogin().equals(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DG_AFC"))){
//					IUser sup3 = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DG_AFC"));
//					users.add(sup3);
//					
//				}
				
				getWorkflowInstance().setValue(supHieNS, users);
				getWorkflowInstance().save(supHieNS);
				getWorkflowInstance().setValue("SUPERIEUR_HIERARCHIQUE", users);
				getWorkflowInstance().save("SUPERIEUR_HIERARCHIQUE");
//				IUser sup2 = getWorkflowModule().getUserByLogin("d.nejjar");
//				users.add(sup2);
				getResourceController().showBodyBlock("FRAG_SUP", false);
//				getWorkflowInstance().setValue("EmailNejjar", getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("EMAIL_SNI"));
				
				IUser rh = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"));
				users = new ArrayList<>();
				users.add(rh);
				getWorkflowInstance().setValue(rhNS, users);
				getWorkflowInstance().save(rhNS);
			}
			
			// nature congé controle
			String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
			if (natureconge.equals("Non combiné"))
			{
				getResourceController().showBodyBlock(frag_conge_combineNS, false);
			}
			else
			{
				getResourceController().showBodyBlock(frag_conge_combineNS, true);
			}
			
			// controle affichage du nombre de jours prédéfini du congé Normal payé
			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
			if ("Normal payé".equals(typeconge) || typeconge == null || "Maladie".equals(typeconge))
			{
				getResourceController().showBodyBlock(frag_nbrjrpredefNS, false);
				
			}
			else
			{
				getResourceController().showBodyBlock(frag_nbrjrpredefNS, true);
				
			}
			if ("Maladie".equals(typeconge)||"Divers".equals(typeconge))
			{
				//getResourceController().setMandatory(justifabsNS, true);
				getResourceController().setMandatory(motifNorNS, true);
			}
			else
			{
				//getResourceController().setMandatory(justifabsNS, false);
				getResourceController().setMandatory(motifSpeNS, true);
			}
			
			
			
			
			
			// get nombre prédéfini du type du congé
			
			req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
			st = connection.prepareStatement(req);
			st.setString(1, typeconge);
			rs = st.executeQuery();
			while (rs.next())
			{
				getWorkflowInstance().setValue(nbjrpredefNS, rs.getFloat(1));
			}
			
			
			// controle affichage du nombre de jours prédéfini du congé combiné
			String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
			
			if ("Maladie".equals(typecongespe))
			{
				//getResourceController().setMandatory(justifabsspeNS, true);
			}
			else
			{
				//getResourceController().setMandatory(justifabsspeNS, false);
			}
			
			// get nombre prédéfini du type du congé
			
			req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
			st = connection.prepareStatement(req);
			st.setString(1, typecongespe);
			rs = st.executeQuery();
			while (rs.next())
			{
				getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(1));
			}
			
			float resteConge = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
			if (resteConge < 0)
			{
				getResourceController().setMandatory(commentaireNS, true);
				getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
				getResourceController().showBodyBlock(fragAlerteNS, true);
			}
			else
			{
				getResourceController().setMandatory(commentaireNS, false);
				getWorkflowInstance().setValue(alerteNS, "");
				getResourceController().showBodyBlock(fragAlerteNS, false);
			}
			
			
			if ("Maladie".equals(typecongespe) ||  "Normal payé".equals(typecongespe) ||  "Divers".equals(typecongespe))
			{
				getResourceController().setEditable(dateCongeFinSpeNS, true);
			}
			else
			{
				getResourceController().setEditable(dateCongeFinSpeNS, false);
			}
			
			if ("Maladie".equals(typeconge) ||  "Normal payé".equals(typeconge) ||  "Divers".equals(typeconge))
			{
				getResourceController().setEditable(dateCongeFinNS, true);
			}
			else
			{
				getResourceController().setEditable(dateCongeFinNS, false);
			}
			
			
			//String momentSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
			String momentEntree = (String) getWorkflowInstance().getValue(momententreeNS);
			if(natureconge.equals("Combiné")){
				getWorkflowInstance().setValue(momentsortieSpeNS, momentEntree);
				getWorkflowInstance().setValue(momententreeSpeNS, momentEntree);
				
				if(typecongespe.equals("Maladie")||typecongespe.equals("Normal payé")||typecongespe.equals("Divers")){
					getResourceController().setEditable(momententreeSpeNS, true);
				}
				else{
					getResourceController().setEditable(momententreeSpeNS, false);
				}
			}
			
			
			
			
			getWorkflowInstance().save(soldeAnterieurNS);
			getWorkflowInstance().save(soldeanneEncoursNS);
			getWorkflowInstance().save(reliquatSoldeanneEncoursNS);
			getWorkflowInstance().save(reliquatSoldeAnterieurNS);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property)
	{
		
		if (property.getName().equals(momententreeSpeNS) || property.getName().equals(momentsortieSpeNS) || property.getName().equals(typecongeNS) || property.getName().equals(typecongespeNS)
				|| property.getName().equals(periodecongenorNS) || property.getName().equals(periodecongespeNS) || property.getName().equals(momententreeNS)
				|| property.getName().equals(momentsortieNS))
		{
			return true;
		}
		
		return super.isOnChangeSubscriptionOn(property);
	}
	
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		try
		{
			String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
			String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
			getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
			getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
			
			if (property.getName().equals(naturecongeNS))
			{
				if (natureconge.equals("Non combiné"))
				{
					getResourceController().showBodyBlock(frag_conge_combineNS, false);
					getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
					getWorkflowInstance().setValue(typecongespeNS, null);
				}
				else
				{
					Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
					getWorkflowInstance().setValue(dateCongeDebSpeNS, datefin1);
					getResourceController().showBodyBlock(frag_conge_combineNS, true);
					Period periodCongeNor = (Period) getWorkflowInstance().getValue(periodecongenorNS);
					Date dateFin = periodCongeNor.getEndDate();
					Period periodCongeSpe = new Period(dateFin, dateFin);
					getWorkflowInstance().setValue(periodecongespeNS, periodCongeSpe);
					
					if("Maladie".equals(typecongespe)||"Normal payé".equals(typecongespe)){
						getResourceController().setEditable(momententreeSpeNS, true);
					}
					else{
						getResourceController().setEditable(momententreeSpeNS, false);
					}
				}
			}
			
			
			if (property.getName().equals(typecongespeNS))
			{
				
				getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
				getWorkflowInstance().setValue(dateCongeFinSpeNS, null);
				//String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS); 
				if("Maladie".equals(typecongespe)||"Normal payé".equals(typecongespe)||"Divers".equals(typecongespe)){
					getResourceController().setEditable(momententreeSpeNS, true);
					getResourceController().setEditable(dateCongeFinSpeNS, true);
					
					if("Divers".equals(typecongespe)){
						getResourceController().setMandatory(motifSpeNS, true);
					}
					else{
						getResourceController().setMandatory(motifSpeNS, false);
					}
				}
				else{
					getResourceController().setEditable(momententreeSpeNS, false);
					getResourceController().setEditable(dateCongeFinSpeNS, false);
					getResourceController().setMandatory(motifSpeNS, false);
				}
				
				if(natureconge.equals("Combiné")){
					Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
					getWorkflowInstance().setValue(dateCongeDebSpeNS, datefin1);
				}
				
				getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
				
				
				
				// get nombre prédéfini du type du congé
				
				String req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
				st = connection.prepareStatement(req);
				st.setString(1, typecongespe);
				ResultSet rs = st.executeQuery();
				while (rs.next())
				{
					getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(1));
				}
				
			}
			
			if (property.getName().equals(typecongeNS))
			{
				getWorkflowInstance().setValue(dateCongeDebNS, null);
				getWorkflowInstance().setValue(dateCongeFinNS, null);
				getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
				String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
				
				if ("Normal payé".equals(typeconge) || typeconge == null || "Divers".equals(typeconge) || "Maladie".equals(typeconge))
				{
					getResourceController().showBodyBlock(frag_nbrjrpredefNS, false);
					getResourceController().setEditable(momententreeNS, true);
					getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
					if("Divers".equals(typeconge)){
						getResourceController().setMandatory(motifNorNS, true);
					}
					else{
						getResourceController().setMandatory(motifNorNS, false);
					}
				}
				else
				{
					getResourceController().showBodyBlock(frag_nbrjrpredefNS, true);
					getResourceController().setEditable(momententreeNS, false);
					getResourceController().setMandatory(motifNorNS, false);
					getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
				}
				
				
				
				if ("Maladie".equals(typeconge) || "Normal payé".equals(typeconge)|| "Divers".equals(typeconge))
				{
					getResourceController().setEditable(dateCongeFinNS, true);
				}
				else
				{
					getResourceController().setEditable(dateCongeFinNS, false);
				}
				
				// get nombre prédéfini du type du congé
				
				String req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
				st = connection.prepareStatement(req);
				st.setString(1, typeconge);
				ResultSet rs = st.executeQuery();
				while (rs.next())
				{
					getWorkflowInstance().setValue(nbjrpredefNS, rs.getFloat(1));
				}
			}
			
			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
			if ("Maladie".equals(typeconge) || "Normal payé".equals(typeconge) || "Divers".equals(typeconge))
			{
				Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
				if (property.getName().equals(dateCongeDebNS) || property.getName().equals(dateCongeFinNS))
				{
					Date dateFin = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
					if (dateDeb != null && dateFin != null)
					{
						
						
						if(natureconge.equals("Combiné")){
							getWorkflowInstance().setValue(dateCongeDebSpeNS, getWorkflowInstance().getValue(dateCongeFinNS));
						}
						else{
							getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
						}
						if (dateDeb.after(dateFin))
						{
							getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "La date de reprise est antérieur à la date d'entrée");
							getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
						}
						else
						{
							matinApmidisortieNS = "Matin";
							matinApmidientreeNS = "Matin";
							getWorkflowInstance().setValue(momententreeNS, "Matin");
							getWorkflowInstance().setValue(momentsortieNS, "Matin");
							float days = 0;
							
							
							
							if (dateDeb != null && dateFin != null)
							{
								String typedeconge = (String) getWorkflowInstance().getValue(typecongeNS);
								
								if("Maternité".equals(typedeconge)){
									days = nbJours(dateDeb, dateFin, true, true, true, true, true, true, true, true);
								}
								else{
									days = nbJours(dateDeb, dateFin, false, true, true, true, true, true, false, false);
								}
								
							}
							
							String momentSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
							String momentEntree = (String) getWorkflowInstance().getValue(momententreeNS);
							// if(!matinApmidisortie.equals(momentSortie)){
							if ("Après midi".equals(momentSortie))
								days -= 0.5;
							// }
							
							// if(!matinApmidientree.equals(momentEntree)){
							if ("Après midi".equals(momentEntree))
								days += 0.5;
							// }
							
							getWorkflowInstance().setValue(nbrjrouvrNS, (float)days);
							matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
							matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
							float nbrCongeDispo = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
							float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
							if (nbrCongeDispo < nbrJrCongeOuvrable)
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé dépasse le solde de votre congé.");
							}
							else
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
							}
							
							float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
							if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
								getResourceController().alert("Le nombre de jours demandé du premier congé n'est pas authentique au solde séléctionnée");
							}
							else if (nbrCongePredef != 0)
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
							}
							
						}
					}
					else{
						getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
					}
				}
			}
			else if (!("Maladie".equals(typeconge) || "Divers".equals(typeconge)|| "Normal payé".equals(typeconge)))
			{
				if (property.getName().equals(dateCongeDebNS) /*|| property.getName().equals(dateCongeFinNS)*/)
				{
					
					float nbJoursPredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
					GregorianCalendar calendar = new java.util.GregorianCalendar();
					Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
					
					
					if(dateDeb !=null ){
						
						if(natureconge.equals("Combiné")){
							getWorkflowInstance().setValue(dateCongeDebSpeNS, getWorkflowInstance().getValue(dateCongeFinNS));
						}
						else{
							getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
						}
						
						matinApmidisortieNS = "Matin";
						matinApmidientreeNS = "Matin";
						getWorkflowInstance().setValue(momententreeNS, "Matin");
						getWorkflowInstance().setValue(momentsortieNS, "Matin");
						calendar.setTime(dateDeb);
						// RÉCUPÉRATION DES JOURS FÉRIÉS
						List<Date> joursFeries = new ArrayList<Date>();
						int yeardeb = calendar.get(Calendar.YEAR);
						int yearfin = calendar.get(Calendar.YEAR);
						for (int i = yeardeb - 1; i <= yearfin; i++)
						{
							joursFeries.addAll(getJourFeries(i));
						}
						
						
						boolean priseCompteDimanche = false;
						boolean priseCompteLundi = true;
						boolean priseCompteMardi = true;
						boolean priseCompteMercredi = true;
						boolean priseCompteJeudi = true;
						boolean priseCompteVendredi = true;
						boolean priseCompteSamedi = false;
						
						String typedeconge = (String) getWorkflowInstance().getValue(typecongeNS);
						if("Maternité".equals(typedeconge)){
							priseCompteDimanche = true;
							priseCompteSamedi = true;
						}
						
						Boolean[] joursPrisEncompte = new Boolean[]
								{
										priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
								};
						
						
						Date dateCalcul = dateDeb;
						GregorianCalendar calendarDateCalcul = new GregorianCalendar();
						calendarDateCalcul.setTime(dateCalcul);
						calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, -1);
						for(int i=0;i<=nbJoursPredef;i++){
							
							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
							dateCalcul = calendarDateCalcul.getTime();
							GregorianCalendar dateToCompare  = new GregorianCalendar();
							boolean test = false;// joursFeries.contains(date1);
							for (Date ligne : joursFeries)
							{
								dateToCompare.setTime(ligne);
								boolean testyear = calendarDateCalcul.get(calendarDateCalcul.YEAR) == dateToCompare.get(dateToCompare.YEAR);
								boolean testmonth = calendarDateCalcul.get(calendarDateCalcul.MONTH) == dateToCompare.get(dateToCompare.MONTH);
								boolean testday = calendarDateCalcul.get(calendarDateCalcul.DATE) == dateToCompare.get(dateToCompare.DATE);
								
								if (testyear == true && testmonth == true && testday == true)
								{
									test = true;
								}
							}
							
							
							if (test == false)
							{
								int chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
								if (joursPrisEncompte[chaine]==false){
									nbJoursPredef++;
								}
									
							}
							else{
								if(!"Maternité".equals(typedeconge)){
									nbJoursPredef++;
								}
								
							}
							
						}
						
						joursPrisEncompte[0] = false;
						joursPrisEncompte[6] = false;
						int chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
						if(joursPrisEncompte[chaine]==false){
							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
							dateCalcul = calendarDateCalcul.getTime();
						}
						chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
						if(joursPrisEncompte[chaine]==false){
							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
							dateCalcul = calendarDateCalcul.getTime();
						}
						
						getWorkflowInstance().setValue(dateCongeFinNS, dateCalcul);
						getWorkflowInstance().setValue(nbrjrouvrNS, (float) getWorkflowInstance().getValue(nbjrpredefNS));
						}
					else{
						getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
					}
					
					
				}
				
			}
			
		
			//String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
			if ("Maladie".equals(typecongespe) || "Divers".equals(typecongespe) || "Normal payé".equals(typecongespe))
			{
				
				Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
				getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
				if (property.getName().equals(dateCongeDebSpeNS) || property.getName().equals(dateCongeFinSpeNS))
				{
					Date dateFin = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
					if (dateDeb != null && dateFin != null)
					{
						if(natureconge.equals("Combiné")){
							getWorkflowInstance().setValue(dateCongeDebSpeNS, getWorkflowInstance().getValue(dateCongeFinNS));
						}
						else{
							getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
						}
						
						if (dateDeb.after(dateFin))
						{
							getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "La date de reprise est antérieur à la date d'entrée");
							getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
						}
						else
						{
							matinApmidisortieSpeNS = "Matin";
							matinApmidientreeSpeNS = "Matin";
							getWorkflowInstance().setValue(momententreeSpeNS, getWorkflowInstance().getValue(momententreeNS));
							getWorkflowInstance().setValue(momentsortieSpeNS, getWorkflowInstance().getValue(momententreeNS));
							float days = 0;
							if (dateDeb != null && dateFin != null)
							{
								String typedecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
								if("Maternité".equals(typedecongespe)){
									days = nbJours(dateDeb, dateFin, true, true, true, true, true, true, true, true);
								}
								else{
									days = nbJours(dateDeb, dateFin, false, true, true, true, true, true, false, false);
								}
							}
							
							String momentSortie = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
							String momentEntree = (String) getWorkflowInstance().getValue(momententreeSpeNS);
							// if(!matinApmidisortie.equals(momentSortie)){
							if ("Après midi".equals(momentSortie))
								days -= 0.5;
							// }
							
							// if(!matinApmidientree.equals(momentEntree)){
							if ("Après midi".equals(momentEntree))
								days += 0.5;
							// }
							
							getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float)days);
							matinApmidisortieSpeNS = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
							matinApmidientreeSpeNS = (String) getWorkflowInstance().getValue(momententreeSpeNS);
							float nbrCongeDispo = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
							float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
							if (nbrCongeDispo < nbrJrCongeOuvrable)
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé dépasse le solde de votre congé.");
							}
							else
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
							}
							
							float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
							if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
								getResourceController().alert("Le nombre de jours demandé du premier n'est pas authentique au solde séléctionnée");
							}
							else if (nbrCongePredef != 0)
							{
								getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
							}
							
						}
					}
					else{
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
					}
				}
			}
			else if (!("Maladie".equals(typecongespe)|| "Divers".equals(typecongespe) || "Normal payé".equals(typecongespe)))
			{
				if (property.getName().equals(dateCongeDebSpeNS) /*|| property.getName().equals(dateCongeFinNS)*/)
				{
					float nbJoursPredef = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
					GregorianCalendar calendar = new java.util.GregorianCalendar();
					Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
					if(dateDeb!=null){
						matinApmidisortieSpeNS = "Matin";
						matinApmidientreeSpeNS = "Matin";
						getWorkflowInstance().setValue(momententreeSpeNS, (String) getWorkflowInstance().getValue(momententreeNS));
						getWorkflowInstance().setValue(momentsortieSpeNS, (String) getWorkflowInstance().getValue(momententreeNS));
						calendar.setTime(dateDeb);
						// RÉCUPÉRATION DES JOURS FÉRIÉS
						List<Date> joursFeries = new ArrayList<Date>();
						int yeardeb = calendar.get(Calendar.YEAR);
						int yearfin = calendar.get(Calendar.YEAR);
						for (int i = yeardeb - 1; i <= yearfin; i++)
						{
							joursFeries.addAll(getJourFeries(i));
						}
						
						
						boolean priseCompteDimanche = false;
						boolean priseCompteLundi = true;
						boolean priseCompteMardi = true;
						boolean priseCompteMercredi = true;
						boolean priseCompteJeudi = true;
						boolean priseCompteVendredi = true;
						boolean priseCompteSamedi = false;
						
						String typedeconge = (String) getWorkflowInstance().getValue(typecongespeNS);
						if("Maternité".equals(typedeconge)){
							priseCompteDimanche = true;
							priseCompteSamedi = true;
						}
						
						Boolean[] joursPrisEncompte = new Boolean[]
								{
										priseCompteDimanche, priseCompteLundi, priseCompteMardi, priseCompteMercredi, priseCompteJeudi, priseCompteVendredi, priseCompteSamedi
								};
						
						
						Date dateCalcul = dateDeb;
						GregorianCalendar calendarDateCalcul = new GregorianCalendar();
						calendarDateCalcul.setTime(dateCalcul);
						calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, -1);
						int compteurJrFr = 0;
						for(int i=0;i<=nbJoursPredef;i++){
							
							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
							dateCalcul = calendarDateCalcul.getTime();
							GregorianCalendar dateToCompare  = new GregorianCalendar();
							boolean test = false;// joursFeries.contains(date1);
							for (Date ligne : joursFeries)
							{
								dateToCompare.setTime(ligne);
								boolean testyear = calendarDateCalcul.get(calendarDateCalcul.YEAR) == dateToCompare.get(dateToCompare.YEAR);
								boolean testmonth = calendarDateCalcul.get(calendarDateCalcul.MONTH) == dateToCompare.get(dateToCompare.MONTH);
								boolean testday = calendarDateCalcul.get(calendarDateCalcul.DATE) == dateToCompare.get(dateToCompare.DATE);
								
								if (testyear == true && testmonth == true && testday == true)
								{
									test = true;
								}
							}
							
							if (test == false)
							{
								int chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
								if (joursPrisEncompte[chaine]==false){
									nbJoursPredef++;
								}
									
							}
							else{
								compteurJrFr++;
								if("Maternité".equals(typedeconge)){
									nbJoursPredef++;
								}
								
							}
							
						}
						
						joursPrisEncompte[0] = false;
						joursPrisEncompte[6] = false;
						int chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
						if(joursPrisEncompte[chaine]==false){
							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
							dateCalcul = calendarDateCalcul.getTime();
						}
						chaine  = calendarDateCalcul.get(calendarDateCalcul.DAY_OF_WEEK) - 1;
						if(joursPrisEncompte[chaine]==false){
							calendarDateCalcul.add(calendarDateCalcul.DAY_OF_MONTH, 1);
							dateCalcul = calendarDateCalcul.getTime();
						}
						
						getWorkflowInstance().setValue(dateCongeFinSpeNS, dateCalcul);
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float) getWorkflowInstance().getValue(nbjrpredefspeNS));
					}
					else{
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
					}
					
					
				}
				
			}
			
			if (property.getName().equals(momententreeNS) || property.getName().equals(momentsortieNS))
			{
				String momentSortie = (String) getWorkflowInstance().getValue(momentsortieNS);
				String momentEntree = (String) getWorkflowInstance().getValue(momententreeNS);
				
				float days = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
				Date datedeb1 = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				
				if(natureconge.equals("Combiné")){
					getWorkflowInstance().setValue(momentsortieSpeNS, momentEntree);
					getWorkflowInstance().setValue(momententreeSpeNS, momentEntree);
					
					if("Maladie".equals(typeconge)|| "Divers".equals(typeconge)||"Normal payé".equals(typeconge)){
						getResourceController().setEditable(momententreeNS, true);
						if (!matinApmidisortieNS.equals(momentSortie) && days != 0 && !datedeb1.equals(datefin1))
						{
							if ("Après midi".equals(momentSortie))
								days -= 0.5f;
							else
								days += 0.5f;
						}
						
						if (!matinApmidientreeNS.equals(momentEntree) && days != 0 && !datedeb1.equals(datefin1))
						{
							if ("Après midi".equals(momentEntree))
								days += 0.5f;
							else
								days -= 0.5f;
						}
					}
					else{
						getResourceController().setEditable(momententreeNS, false);
						if(property.getName().equals(momentsortieNS)){
						getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
						}
					}
				}else{
					getResourceController().setEditable(momententreeNS, true);
					if("Maladie".equals(typeconge)|| "Divers".equals(typeconge)||"Normal payé".equals(typeconge)){
						if (!matinApmidisortieNS.equals(momentSortie) && days != 0 && !datedeb1.equals(datefin1))
						{
							if ("Après midi".equals(momentSortie))
								days -= 0.5f;
							else
								days += 0.5f;
						}
						
						if (!matinApmidientreeNS.equals(momentEntree) && days != 0 && !datedeb1.equals(datefin1))
						{
							if ("Après midi".equals(momentEntree))
								days += 0.5f;
							else
								days -= 0.5f;
						}
						
					}else{
						//if(natureconge.equals("Combiné")){
						getResourceController().setEditable(momententreeNS, false);
						getWorkflowInstance().setValue(momententreeNS, getWorkflowInstance().getValue(momentsortieNS));
						//}
					}
					
				}
				
				
				
				if(datedeb1!=null&&datefin1!=null){
					Calendar dateDebC = Calendar.getInstance();
					dateDebC.setTime(datedeb1);
					Calendar dateFinC = Calendar.getInstance();
					dateFinC.setTime(datefin1);
					if(dateDebC.get(dateDebC.YEAR) == dateFinC.get(dateFinC.YEAR) && dateDebC.get(dateDebC.MONTH) == dateFinC.get(dateFinC.MONTH) && dateDebC.get(dateDebC.DATE) == dateFinC.get(dateFinC.DATE)){
						if (!matinApmidisortieNS.equals(momentSortie) )
						{
							if ("Après midi".equals(momentSortie))
								days -= 0.5f;
							else
								days += 0.5f;
						}
						
						if (!matinApmidientreeNS.equals(momentEntree) )
						{
							if ("Après midi".equals(momentEntree))
								days += 0.5f;
							else
								days -= 0.5f;
						}
						
						if ("Après midi".equals(momentSortie)&&"Matin".equals(momentEntree) && property.getName().equals(momentsortieNS)){
							days += 0.5f;
						}
						else if ("Matin".equals(momentSortie)&&"Matin".equals(momentEntree) && property.getName().equals(momentsortieNS)){
							days -= 0.5f;
						}	
						
						if(("Après midi".equals(momentSortie)&&"Matin".equals(momentEntree))||
						   ("Après midi".equals(momentSortie)&&"Après midi".equals(momentEntree)) ||
						   ("Matin".equals(momentSortie)&&"Matin".equals(momentEntree))){
							days=0;
						}
						
//						if ("Après midi".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//							days += 0.5f;
//						}
//						else if ("Matin".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//							days -= 0.5f;
//						}	
					}
				}
				
				
				getWorkflowInstance().setValue(nbrjrouvrNS,(float) days);
				matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
				matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
				// float nbrCongeDispo = (float) getWorkflowInstance().getValue(reliquatDroitAnnuelleNS);
				float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
				// if(nbrCongeDispo<nbrJrCongeOuvrable){
				// getWorkflowInstance().setValue(msgErreurPeriodeCongeNS,"Le nombre de jours demandé dépasse le solde de votre congé.");
				// }
				// else{
				// getWorkflowInstance().setValue(msgErreurPeriodeCongeNS,"");
				// }
				
				float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
				if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
					getResourceController().alert("Le nombre de jours demandé du premier congé n'est pas authentique au solde séléctionnée");
				}
				else
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
				}
				
				
				
			}
			
			if (property.getName().equals(momententreeSpeNS) || property.getName().equals(momentsortieSpeNS))
			{
				String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
				String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
				float days = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
				if (!matinApmidisortieSpeNS.equals(momentSortieSpe) && days != 0 && "Combiné".equals(natureconge))
				{
					if ("Après midi".equals(momentSortieSpe))
						days -= 0.5f;
					else
						days += 0.5f;
				}
				
				if (!matinApmidientreeSpeNS.equals(momentEntreeSpe) && days != 0 && "Combiné".equals(natureconge))
				{
					if ("Après midi".equals(momentEntreeSpe))
						days += 0.5f;
					else
						days -= 0.5f;
				}
				Date datedebSpe1 = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
				Date datefinSpe1 = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
				if(datedebSpe1!=null&&datefinSpe1!=null)
				if(datedebSpe1.equals(datefinSpe1)){
					if ("Après midi".equals(momentEntreeSpe)&& "Matin".equals(momentSortieSpe)){
						days += 0.5f;
					}
				}
				
				getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float)days);
				matinApmidisortieSpeNS = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
				matinApmidientreeSpeNS = (String) getWorkflowInstance().getValue(momententreeSpeNS);
				
				float nbrCongeDispoSpe = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
				float nbrJrCongeOuvrableSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
				if (nbrCongeDispoSpe < nbrJrCongeOuvrableSpe && natureconge.equals("Combiné"))
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé dépasse le solde de votre congé.");
				}
				else
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
				}
				
				float nbrCongePredefSpe = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
				if (nbrCongePredefSpe != nbrJrCongeOuvrableSpe && nbrCongePredefSpe != 0 && natureconge.equals("Combiné"))
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
					getResourceController().alert("Le nombre de jours demandé du deuxième congé n'est pas authentique au solde séléctionnée");
				}
				else
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
				}
			}
			
			if (property.getName().equals(naturecongeNS) || property.getName().equals(momententreeSpeNS) || property.getName().equals(momentsortieSpeNS) || property.getName().equals(typecongeNS)
					|| property.getName().equals(typecongespeNS) || property.getName().equals(reliquatSoldeanneEncoursNS) || property.getName().equals(dateCongeDebNS)
					|| property.getName().equals(dateCongeDebSpeNS) || property.getName().equals(nbrjrouvrNS) || property.getName().equals(dateCongeFinNS)
					|| property.getName().equals(dateCongeFinSpeNS) || property.getName().equals(nbrjrouvrSpeNS) || property.getName().equals(momententreeNS)
					|| property.getName().equals(momentsortieNS))
			{
				// calculate nombre congé soldé
				typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
				String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
				float nbrJourDispo = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
				float nbrJour = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
				float nbrJourSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
				if (typeconge != null)
				{
					if (!typeconge.equals(typecongeSpe))
					{
						getWorkflowInstance().setValue(nbrJrSoldeNS, 0f);
						getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f);
						if ("Combiné".equals(natureconge) && typeconge != null)
						{
							if ("Normal payé".equals(typeconge) )
							{
								getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJour);
								
								if ("Normal payé".equals(typecongeSpe) )
								{
									
									getWorkflowInstance().setValue(nbrJrSoldeNS, (float)(nbrJourSpe + nbrJour));
								}
								else
								{
									getWorkflowInstance().setValue(nbrJrNonSoldeNS,(float) nbrJourSpe);
								}
							}
							else
							{
								getWorkflowInstance().setValue(nbrJrNonSoldeNS, nbrJour);
								
								if ("Normal payé".equals(typecongeSpe) )
								{
									
									getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJourSpe);
								}
								else
								{
									getWorkflowInstance().setValue(nbrJrNonSoldeNS, (float)(nbrJourSpe + nbrJour));
								}
							}
							
						}
						
						else
						{
							if ("Normal payé".equals(typeconge) )
							{
								getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJour);
							}
							else
							{
								getWorkflowInstance().setValue(nbrJrNonSoldeNS,(float) nbrJour);
							}
							if (("Normal payé".equals(typecongeSpe) ) && "Combiné".equals(natureconge))
							{
								getWorkflowInstance().setValue(nbrJrSoldeNS, 0f + (float)nbrJour);
							}
							else if (!("Normal payé".equals(typecongeSpe)) && "Combiné".equals(natureconge))
							{
								getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f + (float)nbrJour);
							}
						}
						
						if ("Maladie".equals(typeconge))
						{
							float resteConge = nbrJourDispo - (nbrJourSpe + nbrJourSpe);
							if (resteConge < 0)
							{
								getResourceController().setMandatory(commentaireNS, true);
							}
							else
							{
								getResourceController().setMandatory(commentaireNS, false);
							}
						}
						else
						{
							getResourceController().setMandatory(commentaireNS, false);
						}
						
					}
				}
				else
				{
					getWorkflowInstance().setValue(nbrJrSoldeNS, 0f);
					getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f);
				}
				
				Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
				Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
				Period periodeConge = new Period(new Date(), new Date()); 
				if(natureconge.equals("Combiné") && dateDebNor!=null && dateFinSpe!=null){
					if(new ServiceRH().compareTwoDates(dateDebNor, dateFinSpe)<=0)
					periodeConge = new Period(dateDebNor, dateFinSpe);
				}
				else if(natureconge.equals("Non combiné") && dateDebNor!=null && dateFinNor!=null){
					if(new ServiceRH().compareTwoDates(dateDebNor, dateFinNor)<=0)
					periodeConge = new Period(dateDebNor, dateFinNor);
				}
				getWorkflowInstance().setValue("DEMCON_PERCONG",periodeConge);
				
			}
			float reste = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
			if (reste < 0)
			{
				getResourceController().setMandatory(commentaireNS, true);
				getWorkflowInstance().setValue(alerteNS, "Solde négatif");
				getResourceController().showBodyBlock(fragAlerteNS, true);
			}
			else
			{
				getResourceController().setMandatory(commentaireNS, false);
				getWorkflowInstance().setValue(alerteNS, "");
				getResourceController().showBodyBlock(fragAlerteNS, false);
			}
//			getWorkflowInstance().setValue(reliquatDroitAnnuelleInitNS, getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS));
//			getWorkflowInstance().setValue(reliquatSoldeanneEncoursInitNS, getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS));
//			getWorkflowInstance().setValue(reliquatSoldeAnterieurInitNS, getWorkflowInstance().getValue(reliquatSoldeAnterieurNS));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		super.onPropertyChanged(property);
	}
	
	@SuppressWarnings({
			"static-access", "deprecation"
	})
	public float nbJours(Date d1, Date d2, boolean notionJourFerie, boolean priseCompteLundi, boolean priseCompteMardi, boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi, boolean priseCompteDimanche)
	{
		
		if (d2.compareTo(d1) <= 0 || (d1.getDay()==0 && d2.getDay()== 6) || (d1.getDay()==6 && d2.getDay()== 0))
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
		while (date1.before(date2) || (date1.get(date1.YEAR) == date2.get(date2.YEAR) && date1.get(date1.MONTH) == date2.get(date2.MONTH) && date1.get(date1.DATE) == date2.get(date2.DATE)))
		{
			boolean test = false;// joursFeries.contains(date1);
			boolean test1 = false;
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
				
				testyear = date2.get(date2.YEAR) == dateToCompare.get(dateToCompare.YEAR);
				testmonth = date2.get(date2.MONTH) == dateToCompare.get(dateToCompare.MONTH);
				testday = date2.get(date2.DATE) == dateToCompare.get(dateToCompare.DATE);
				if (testyear == true && testmonth == true && testday == true)
				{
					test1 = true;
				}
			}
			
			if (test == false)
			{
				if (joursPrisEncompte[date1.get(Calendar.DAY_OF_WEEK) - 1])
					nbJour++;
			}
			
			if (test == true && test1 == true)
			{
				return 0;
			}
			
			date1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return nbJour - 1;
	}
	
	public static GregorianCalendar addDays(String date, Integer days){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar calendar = null;
		try {
			Date df = sdf.parse(date);
			 calendar = new java.util.GregorianCalendar();
			calendar.setTime(df);
			calendar.add (Calendar.DAY_OF_MONTH, days);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar;
	}
	
	@SuppressWarnings("static-access")
	public List<Date> getJourFeries(int annee)
	{
		
		List<Date> datesFeries = new ArrayList<Date>();
		try
		{
			
			String req = "SELECT jfa.date_jourferie,jf.libelleJoursFerie,jf.nbr_jours_ferie FROM JourFerieAnnuelle jfa,JourFerie jf  where annee = ? and jfa.id_jourferie = jf.idJoursFerie";
			st = connection.prepareStatement(req);
			st.setInt(1, annee);
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				Date date = new Date(rs.getDate(1).getTime());
				datesFeries.add(date);
				
				int nombreJoursDuFete = rs.getInt(3);
				if (nombreJoursDuFete == 2)
				{
					Date datePlus1 = date;
					Calendar ca = Calendar.getInstance();
					ca.setTime(datePlus1);
					int newjour = ca.get(ca.DATE) + 1;
					ca.set(ca.DATE, newjour);
					datePlus1 = new Date(ca.getTimeInMillis());
					datesFeries.add(datePlus1);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return datesFeries;
	}
	
	public int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate)
	{
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		
		int workDays = 0;
		
		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis())
		{
			return 0;
		}
		
		if (startCal.getTimeInMillis() > endCal.getTimeInMillis())
		{
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}
		
		do
		{
			// excluding start date
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
			{
				++workDays;
			}
		}
		while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()); // excluding end date
		
		return workDays;
	}
	
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		
		if (action.getName().equals(btnenvoyerNS))
		{
			
			String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
			// float nbrCongeDispo = (float) getWorkflowInstance().getValue(reliquatDroitAnnuelleNS);
			float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
			String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
			Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
			Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
			Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
			Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
			
			Period periodeConge; 
			if(natureconge.equals("Combiné")){
				periodeConge = new Period(dateDebNor, dateFinSpe);
			}
			else{
				periodeConge = new Period(dateDebNor, dateFinNor);
			}
			getWorkflowInstance().setValue("DEMCON_PERCONG",periodeConge);
			
			if (typeconge.equals(typecongeSpe))
			{
				getResourceController().alert("Les types de congé doivent différés !!!");
				return false;
			}
			
			// if(nbrCongeDispo<nbrJrCongeOuvrable && typeconge.equals("Normal payé")){
			// getWorkflowInstance().setValue(msgErreurPeriodeCongeNS,"Le nombre de jours demandé dépasse le solde de votre congé.");
			// getResourceController().alert("Le nombre de jours demandé dépasse le solde de votre congé.");
			// return false;
			// }
			
			float nbrCongePredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
			if (nbrCongePredef != nbrJrCongeOuvrable && nbrCongePredef != 0)
			{
//				getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
				getResourceController().alert("Le nombre de jours demandé du premier congé n'est pas authentique au solde séléctionnée");
				return false;
			}
			
			float nbrJrCongeOuvrableSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
			float nbrCongePredefSpe = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
			if (nbrCongePredefSpe != nbrJrCongeOuvrableSpe && nbrCongePredefSpe != 0 && natureconge.equals("Combiné") && !typecongeSpe.equals("Normal payé"))
			{
//				getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
				getResourceController().alert("Le nombre de jours demandé du deuxième congé n'est pas authentique au solde séléctionnée");
				return false;
			}
			
			if (nbrJrCongeOuvrable == 0 || (nbrJrCongeOuvrableSpe == 0 && natureconge.equals("Combiné")))
			{
				if(nbrJrCongeOuvrable == 0)
				getResourceController().alert("Le nombre de jours de congé égal à 0. Pensez à modifier la période du premier congé");
				if((nbrJrCongeOuvrableSpe == 0 && natureconge.equals("Combiné")))
					getResourceController().alert("Le nombre de jours de congé égal à 0. Pensez à modifier la période du deuxième congé");
				return false;
			}
			
			
			String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
			String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
			String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
			String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeSpeNS);
			
			
			
			String login = getWorkflowModule().getLoggedOnUser().getLogin();
			
			if(natureconge.equals("Non combiné")&&testDatesIftheyWasInConge(dateDebNor,dateFinNor,momentSortieNor,momentEntreeNor, login)==false){
				getResourceController().alert("la période séléctionnée est en chevauchement avec une autre période, pensez à changer les dates ou les moments de sorties/entrées !!!");
				return false;
			}
			
			if(!natureconge.equals("Non combiné")&&testDatesIftheyWasInConge(dateDebNor,dateFinSpe,momentSortieNor,momentEntreeSpe,login)==false){
				getResourceController().alert("la période séléctionnée est en chevauchement avec une autre période, pensez à changer les dates ou les moments de sorties/entrées !!!");
				return false;
			}
			
			
			
			
			if (dateFinNor.before(dateDebNor))
			{
				getResourceController().alert("La date fin est antérieur à la date de début du premier congé !!!");
				return false;
			}
			
			
			IUser demandeur = getWorkflowInstance().getCreatedBy();
			List<IUser> remplacants = (List<IUser>) getWorkflowInstance().getValue("REMPLACANT");
			if(remplacants!=null){
				if(remplacants.size()>0){
					IUser remplacant = remplacants.get(0);
					if(demandeur.getLogin().equals(remplacant.getLogin())){
						getResourceController().alert("Pensez à changer le remplaçant !!!");
						return false;
					}
				}
				
			}
			
			
			
			if (natureconge.equals("Combiné"))
			{
				if (dateDebSpe != null && dateFinNor != null)
				{
					
					if (dateFinSpe != null && dateDebSpe != null)
					{
						if (dateFinSpe.before(dateDebSpe))
						{
							getResourceController().alert("La date fin est antérieur à la date de début du deuxième congé");
							return false;
						}
					}
					if(testDatesIftheyWasInConge(dateDebNor,dateFinSpe,momentSortieNor,momentEntreeSpe,login)==false){
						getResourceController().alert("la période séléctionnée est en chevauchement avec une autre période, pensez à changer les dates ou les moments de sorties/entrées !!!");
						return false;
					}
					
					GregorianCalendar cDateDebSpe = new GregorianCalendar();
					cDateDebSpe.setTime(dateDebSpe);
					GregorianCalendar cDateFinNor = new GregorianCalendar();
					cDateFinNor.setTime(dateFinNor);
					
					if (!((cDateDebSpe.get(cDateDebSpe.YEAR)==cDateFinNor.get(cDateFinNor.YEAR))&&
						(cDateDebSpe.get(cDateDebSpe.MONTH)==cDateFinNor.get(cDateFinNor.MONTH))&&
						(cDateDebSpe.get(cDateDebSpe.DATE)==cDateFinNor.get(cDateFinNor.DATE))))
					{
						getResourceController().alert("Il y a un chevauchement entre les périodes.");
						return false;
					}
					
					
					
				}
			}
			
			// float nbrJoursDispo = (float) getWorkflowInstance().getValue(reliquatDroitAnnuelleNS);
			// float nbrJoursSolde = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
			// if(nbrJoursSolde>nbrJoursDispo){
			// getResourceController().alert("Le nombre de jours soldés dépasse votre solde disponible");
			// return false;
			// }
			
			if(dateDebNor.equals(dateFinNor)){
				if(dateDebNor.getDay()==0||dateDebNor.getDay()== 6||testDayIsJourFerie(dateDebNor)==true){
					getResourceController().alert("Ce jour est férié");
					return false;
				}
			}
			
			if(natureconge.equals("Non combiné")){
				if(dateFinNor.getDay()==0||dateFinNor.getDay()== 6||testDayIsJourFerie(dateFinNor)==true){
			    getResourceController().alert("le jour de reprise du premier congé est férié");
				return false;
				}
			}
			else{
				if(dateFinSpe.getDay()==0||dateFinSpe.getDay()== 6||testDayIsJourFerie(dateFinSpe)==true){
				    getResourceController().alert("le jour de reprise du deuxième congé est férié");
					return false;
				}
			}
			
			
			
			
			String mpEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
			String mpSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
			if (!mpEntreeNor.equals(mpSortieSpe)&&natureconge.equals("Combiné"))
			{
				getResourceController().alert("il y a un chevauchement entre le moment entrée du premier congé et le moment de sortie du deuxième congé.");
				return false;
			}
			
		}
		return super.onBeforeSubmit(action);
	}
	
	public boolean testDayIsJourFerie(Date date){
		// Récupération des jours fériés
		List<Date> joursFeries = new ArrayList<Date>();
		Calendar date1 = Calendar.getInstance();
		date1.setTime(date);
		int yeardeb = date1.get(Calendar.YEAR);
		int yearfin = date1.get(Calendar.YEAR);
		for (int i = yeardeb - 1; i <= yearfin; i++)
		{
			joursFeries.addAll(getJourFeries(i));
		}
		Calendar dateToCompare = Calendar.getInstance();
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
			return false;
		}
		
		return true;
	}
	
	public boolean testDatesIftheyWasInConge(Date dateDebCD, Date dateFinCD, String momSortieCD, String momEntreeCD, String user){
		int i=0;
		try{
			
			String req = "select DateDeb,MomSortie,DateFin,MomEntre "
					+ "from Conge "
					+ "where Personnelmatricule like ? "
					+ "and NOT "
					+ "("
					+ "		(		DATEDIFF(DAY,DateDeb, ?)<0 "
					+ "			or (DATEDIFF(DAY,DateDeb, ?)=0 and MomSortie='Après midi')"
					+ "			or (DATEDIFF(DAY,DateDeb, ?)=0 and MomSortie='Matin' and MomSortie=    ?    ))"
					+ "		or "
					+ " (		DATEDIFF(DAY,DateFin,?)>0 "
					+ " 	or (DATEDIFF(DAY,DateFin,?)=0 and MomEntre like 'Matin') "
					+ "		or (DATEDIFF(DAY,DateFin,?)=0 and MomEntre like 'Après midi' and MomEntre=    ?    )"
					+ ")"
					+ ") "
					+ "and EtatConge NOT LIKE 'congé annulé'  ";
			st = connection.prepareStatement(req);
			java.sql.Date dateDebCDsql = new java.sql.Date(dateDebCD.getTime());
			java.sql.Date dateFinCDsql = new java.sql.Date(dateFinCD.getTime());
			st.setString(1, user);
			st.setDate(2, dateFinCDsql);
			st.setDate(3, dateFinCDsql);
			st.setDate(4, dateFinCDsql);
			st.setString(5, momEntreeCD);
			st.setDate(6, dateDebCDsql);
			st.setDate(7, dateDebCDsql);
			st.setDate(8, dateDebCDsql);
			st.setString(9, momSortieCD);
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next())
			{
				i++;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		boolean test = i==0;
		return test;
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try
		{
			if (action.getName().equals(btnenvoyerNS))
			{
				// String codeDemCongVdoc = (String) getWorkflowInstance().getValue("sys_Reference");
				// String typecongeNor = (String) getWorkflowInstance().getValue(typecongeNS);
				// String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
				//
				// Period periodCongeNor = (Period) getWorkflowInstance().getValue(periodecongenorNS);
				// Date dateDebNor = periodCongeNor.getEndDate();
				// Date dateFinNor = periodCongeNor.getStartDate();
				//
				// Date dateDemandeConge = getWorkflowInstance().getCreatedDate();
				//
				// float nbrJrOuvrableCongeNor = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
				//
				// String etatCongeNor = "en attente validation remplaçant";
				//
				// String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
				// String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
				//
				// IUser personnel = (IUser) getWorkflowModule().getLoggedOnUser();
				// String matriculePersonnel = personnel.getEmployeeNumber();
				//
				// List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(supHieNS);
				// IUser superieur = users.get(0);
				// String matriculeSup = superieur.getEmployeeNumber();
				// users = (List<IUser>) getWorkflowInstance().getValue(remplacantNS);
				// IUser remplaçant = users.get(0);
				// String matriculeRemp = remplaçant.getEmployeeNumber();
				// 
				//
				// String req =
				// "insert into Conge(IdConge, supMatricule, remplacantMatricule, Personnelmatricule, DateDeb, DateFin, DateDemandeConge, NbrJoursOuvrables, TypeConge, EtatConge, MomSortie, MomEntre,CodeVdocDemandeConge) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				// st = connection.prepareStatement(req);
				// st.setString(1, codeDemCongVdoc+"NOR");
				// st.setString(2, matriculeSup);
				// st.setString(3,matriculeRemp);
				// st.setString(4,matriculePersonnel);
				// java.sql.Date dateDeb = new java.sql.Date(dateDebNor.getTime());
				// st.setDate(5, dateDeb);
				// java.sql.Date dateFin = new java.sql.Date(dateFinNor.getTime());
				// st.setDate(6, dateFin);
				// java.sql.Date dateDemande = new java.sql.Date(dateFinNor.getTime());
				// st.setDate(7, dateDemande);
				// st.setFloat(8, nbrJrOuvrableCongeNor);
				// st.setString(9, typecongeNor);
				// st.setString(10, etatCongeNor);
				// st.setString(11, momentSortieNor);
				// st.setString(12, momentEntreeNor);
				// st.setString(13, codeDemCongVdoc);
				// st.executeUpdate();
				//
				//
				// if(typecongeSpe!=null){
				// Period periodCongeSpe = (Period) getWorkflowInstance().getValue(periodecongenorNS);
				// Date dateDebSpe = periodCongeSpe.getEndDate();
				// Date dateFinSpe = periodCongeSpe.getStartDate();
				//
				// float nbrJrOuvrableCongeSpe = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
				//
				// String etatCongeSpe = "en attente validation remplaçant";
				//
				// String momentSortieSpe = (String) getWorkflowInstance().getValue(momentsortieNS);
				// String momentEntreeSpe = (String) getWorkflowInstance().getValue(momententreeNS);
				//
				// req =
				// "insert into Conge(IdConge, supMatricule, remplacantMatricule, Personnelmatricule, DateDeb, DateFin, DateDemandeConge, NbrJoursOuvrables, TypeConge, EtatConge, MomSortie, MomEntre,CodeVdocDemandeConge) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				// st = connection.prepareStatement(req);
				// st.setString(1, codeDemCongVdoc+"SPE");
				// st.setString(2, matriculeSup);
				// st.setString(3,matriculeRemp);
				// st.setString(4,matriculePersonnel);
				// dateDeb = new java.sql.Date(dateDebSpe.getTime());
				// st.setDate(5, dateDeb);
				// dateFin = new java.sql.Date(dateFinSpe.getTime());
				// st.setDate(6, dateFin);
				// dateDemande = new java.sql.Date(dateDemandeConge.getTime());
				// st.setDate(7, dateDemande);
				// st.setFloat(8, nbrJrOuvrableCongeSpe);
				// st.setString(9, typecongeSpe);
				// st.setString(10, etatCongeSpe);
				// st.setString(11, momentSortieSpe);
				// st.setString(12, momentEntreeSpe);
				// st.setString(13, codeDemCongVdoc);
				// st.executeUpdate();
				// }
				
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return super.onAfterSubmit(action);
	}
	
	
	private float calculateCongePris(String Login){
		float CongePrisAnneEnCours = 0;
		try{
			
				ServiceRH serviceRh = new ServiceRH();
				String loginVdoc = Login;
				String req = " select DateDeb,DateFin,MomSortie,MomEntre from Conge where Personnelmatricule = ?"
						+ " and( DATEDIFF(YEAR,DateDeb,?)>=0 and"
						+ " DATEDIFF(YEAR,DateFin,?)<=0)"
						+ " and(  ( TypeConge = 'Normal payé') or ( TypeConge = 'Divers' and maladieComptabilise = 'True')"
						+ " or ( TypeConge = 'Maladie' and maladieComptabilise = 'True'))and EtatConge = 'valide'"
						+ " union"
						+ " select DateDeb,DateFin,MomSortie,MomEntre"
						+ " from Conge,InfosCongeAnnule"
						+ " where Conge.Personnelmatricule = ?"
						+ " and Conge.EtatConge = 'congé modifié'"
						+ " and Conge.CodeVdocDemandeConge = InfosCongeAnnule.idConge";
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				st.setDate(2, new java.sql.Date(new Date().getTime()));
				st.setDate(3, new java.sql.Date(new Date().getTime()));
				st.setString(4, loginVdoc);
				ResultSet rs = st.executeQuery();
				List<Conge> conges = new ArrayList<Conge>();
				while(rs.next()){
					conges.add(new Conge(rs.getDate(1), rs.getDate(2), rs.getString(3), rs.getString(4)));
				}
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(new Date());
				
				
				Calendar dateDebc = Calendar.getInstance();
				Calendar dateFinc = Calendar.getInstance();
				
				Calendar dateDebAnne = Calendar.getInstance();
				Date debanne = new Date();
				dateDebAnne.setTime(debanne);
				dateDebAnne.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
				dateDebAnne.set(Calendar.MONTH, Calendar.JANUARY);
				dateDebAnne.set(Calendar.DATE, 01);
				
				
				
				Calendar dateFinAnne = Calendar.getInstance();
				Date finanne = new Date();
				dateFinAnne.setTime(finanne);
				dateFinAnne.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
				dateFinAnne.set(Calendar.MONTH, Calendar.DECEMBER);
				dateFinAnne.set(Calendar.DATE, 31);
				
				for(Conge conge : conges){
					dateDebc.setTime(conge.getDateDeb());
					dateFinc.setTime(conge.getDateFin());
					
					
					if(dateDebc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinc.get(Calendar.YEAR)>currentDate.get(Calendar.YEAR)){
						
						CongePrisAnneEnCours+=serviceRh.getNombreJoursOfConge(conge.getDateDeb(), dateFinAnne.getTime(), conge.getMomDeb(), conge.getMomFin());
					}
					
					else if(dateDebc.get(Calendar.YEAR)<currentDate.get(Calendar.YEAR)  && dateFinc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						
						CongePrisAnneEnCours+=serviceRh.getNombreJoursOfConge(dateDebAnne.getTime(), conge.getDateFin(), conge.getMomDeb(), conge.getMomFin());
					}
					
					else if(dateDebc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinc.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						
						CongePrisAnneEnCours+=serviceRh.getNombreJoursOfConge(conge.getDateDeb(), conge.getDateFin(), conge.getMomDeb(), conge.getMomFin());
					}
				}
				
				
				req = " select DateDeb,DateFin,MomSortie,MomEntre,dateEntreeExcep,dateSortieExcep,MomEntreeExcep,MomSortieExcep,Conge.IdConge"
						+ " from Conge,InfosCongeAnnule "
						+ " where Conge.Personnelmatricule = ? "
						+ " and Conge.EtatConge = 'congé modifié' "
						+ " and Conge.CodeVdocDemandeConge = InfosCongeAnnule.idConge"
						+ " and ( DATEDIFF(YEAR,InfosCongeAnnule.dateEntreeExcep,?)>=0"
						+ " and DATEDIFF(YEAR,InfosCongeAnnule.dateSortieExcep,?)<=0)";
				st = connection.prepareStatement(req);
				st.setString(1, loginVdoc);
				st.setDate(2, new java.sql.Date(new Date().getTime()));
				st.setDate(3, new java.sql.Date(new Date().getTime()));
				rs = st.executeQuery();
				
				while(rs.next()){
					Conge congeModifé = new Conge(rs.getDate(1), rs.getDate(2), rs.getString(3), rs.getString(4));
					Conge congeExcep = new Conge(rs.getString(7), rs.getString(8),rs.getDate(5), rs.getDate(6) );
					
					dateDebc = Calendar.getInstance();
					dateDebc.setTime(congeModifé.getDateDeb());
					
					dateFinc = Calendar.getInstance();
					dateFinc.setTime(congeModifé.getDateFin());
					
					Calendar dateDebCEAnne = Calendar.getInstance();
					dateDebCEAnne.setTime(congeExcep.getDateDeb());
					
					Calendar dateFinCEAnne = Calendar.getInstance();
					dateFinCEAnne.setTime(congeExcep.getDateFin());
					
					if(dateDebCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinCEAnne.get(Calendar.YEAR)>currentDate.get(Calendar.YEAR)){
						CongePrisAnneEnCours-=serviceRh.getNombreJoursOfConge(congeExcep.getDateDeb(), dateFinAnne.getTime(), congeExcep.getMomDebEx(), congeExcep.getMomFinEx());

					}
					else if(dateDebCEAnne.get(Calendar.YEAR)<currentDate.get(Calendar.YEAR)  && dateFinCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						CongePrisAnneEnCours-=serviceRh.getNombreJoursOfConge(dateDebAnne.getTime(), congeExcep.getDateFin(), congeExcep.getMomDebEx(), congeExcep.getMomFinEx());

					}
					else if(dateDebCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)  && dateFinCEAnne.get(Calendar.YEAR)==currentDate.get(Calendar.YEAR)){
						CongePrisAnneEnCours+=serviceRh.getNombreJoursOfConge(congeExcep.getDateDeb(), congeExcep.getDateFin(), congeExcep.getMomDebEx(), congeExcep.getMomFinEx());

					}
						
						
						
						
					
				}
		
			
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return CongePrisAnneEnCours;
		
	}
	
	
}
