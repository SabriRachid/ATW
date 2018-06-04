package com.attijari.ProcessusCongeConstates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IOperatorRole;
import com.axemble.vdoc.sdk.interfaces.IOrganization;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IRole;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.structs.Period;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _1SaisieCongeConstate extends BaseDocumentExtension
{
	private String supHieNS;
	private String demandeur;
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
	private String nbrjrcongdispoNS;
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
	private String droitAnnCongNS;
	private String motifNorNS;
	private String motifSpeNS;
	private String fragmotifNorNS;
	private String fragmotifSpeNS;
	private String frag_remplacantNS;
	private String resteJoursCongeNS;
	private String commentaireNS;
	private String alerteNS;
	private String fragAlerteNS;
	private String fragDemandeur;
	private static final long serialVersionUID = 4269810337595101111L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String resteDispoApresNS;
	
	private String droitAnnuelleNS;
	private String reliquatDroitAnnuelleNS;
	private String soldeAnterieurNS;
	private String reliquatSoldeAnterieurNS;
	private String soldeanneEncoursNS;
	private String reliquatSoldeanneEncoursNS;
	
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException
	{
		this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
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
	@SuppressWarnings({
			"unused", "unchecked"
	})
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
				try
				{
					// get nom systeme des champ GCC_FRAG_DEMANDEUR
					String ipAdresse = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
					getWorkflowInstance().setValue("URI", "http://"+ipAdresse+"/vdoc"+getWorkflowInstance().getURI());
					
					fragDemandeur = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_DEMANDEUR");
					demandeur = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("PERSONNEL");
					fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_ALERTE");
					resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
					commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Commentaire");
					frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_REMPLACANT");
					fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFSPE");
					fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFNOR");
					motifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MotifsSpe");
					motifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Motifs");
					dateCongeDebNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateReprise");
					dateCongeFinNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateEntree");
					dateCongeDebSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateRepriseSpe");
					dateCongeFinSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DateEntreeSpe");
					alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Alerte");
					droitAnnCongNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DroitAnnuelConge");
					nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsSoldesConge");
					nbrJrNonSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsNonSoldesConge");
					// nbrJrTotalSoldeNS =
					// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_TOTALJRSOLDNONSOLD");
					// nbrJrResteSoldeNS =
					// getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPO");
					momentsortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortieSpe");
					momententreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntreeSpe");
					matinApmidisortieSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortieSpe");
					matinApmidientreeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntreeSpe");
					nbrjrouvrSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsOuvrablesSpe");
					msgErreurPeriodeCongeSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MsgErrPerSpe");
					supHieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("SUPERIEUR_HIERARCHIQUE");
					// remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("REMPLACANT");
					btnenvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Envoyer");
					naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_CongeNormalCombine");
					typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_TypeConge");
					nbjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsPredefinis");
					momentsortieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentSortie");
					momententreeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MomentEntree");
					//periodecongenorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONG");
					nbrjrouvrNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsOuvrables");
					nbrjrcongdispoNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsDispoConge");
					justifabsNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_JustifAbscence");
					typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_TypeCongeSpecial");
					nbjrpredefspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsPredefinisSpe");
					//periodecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_PERCONGSPE");
					justifabsspeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_JustifAbscenceSpe");
					frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_DONNE_CONG_COMBINE");
					//frag_nbrjrpredefNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_NBR_JR_PREDEF_NOR");
					
					
					matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
					matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
					msgErreurPeriodeCongeNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_MsgErrPerConge");
					resteDispoApresNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoCongeApresModif");
					
					
					droitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_DroitAnnuelConge");
					reliquatDroitAnnuelleNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_NbreJrsDispoConge");
					soldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
					reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoCongeApresModif");
					soldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_SoldeAnneeEnCours");
					reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ReliquatSoldeAnneeEnCours");
					
					
					// get superieur hierarchique
//					IUser sup = getWorkflowModule().getLoggedOnUser().getHierarchicalManager();
//					List<IUser> users = new ArrayList<>();
//					users.add(sup);
//					getWorkflowInstance().setValue(supHieNS, users);
//					getWorkflowInstance().save(supHieNS);
					
					
					
					String filiale = "";
					String loginVdocOfUser = "";
					List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(demandeur);
					if(users!=null){
						if(users.size()!=0){
//							List<IUser> sups = new ArrayList<>();
//							sups.add(users.get(0).getHierarchicalManager());
//							getWorkflowInstance().setValue(supHieNS, sups);
//							getWorkflowInstance().save(supHieNS);
							getResourceController().showBodyBlock(fragDemandeur, true);
							// get nombre dispo du congé
							connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
							loginVdocOfUser = users.get(0).getLogin();
							
							String req = "SELECT NombreJoursConge,NombrJoursDispo,reliquatAnneEnCours,FilialeIdFiliale FROM Personnel where loginVdoc = ?";
							st = connection.prepareStatement(req);
							st.setString(1, loginVdocOfUser);
							ResultSet rs = st.executeQuery();
							
							while (rs.next())
							{
								getWorkflowInstance().setValue(droitAnnuelleNS, rs.getFloat(1));
								getWorkflowInstance().setValue(reliquatDroitAnnuelleNS, rs.getFloat(1));
								getWorkflowInstance().setValue(soldeAnterieurNS, rs.getFloat(2));
								getWorkflowInstance().setValue(soldeanneEncoursNS, rs.getFloat(3));
								
								filiale = rs.getString(4);
							}
							
						}
						else{
							getWorkflowInstance().setValue(supHieNS, null);
							getResourceController().showBodyBlock(fragDemandeur, false);
						}
						
					}
					else{
						getWorkflowInstance().setValue(supHieNS, null);
						getResourceController().showBodyBlock(fragDemandeur, false);
					}
					
					String filialeUser = getWorkflowModule().getLoggedOnUser().getOrganization().getName();
					
					if ("Attijariintermediation".equals(filialeUser))
					{
						getResourceController().showBodyBlock("FRAG_SUP", false);
						getResourceController().showBodyBlock("FRAG_ATI", true);
						getResourceController().showBodyBlock("FRAG_AFC", false);
					}
					else
					{
						getResourceController().showBodyBlock("FRAG_SUP",true );
						getResourceController().showBodyBlock("FRAG_ATI", false);
						getResourceController().showBodyBlock("FRAG_AFC", true);
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
					
					// controle affichage du nombre de jours prédéfini du congé normal
					String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
					if ("Normal payé".equals(typeconge) || typeconge == null || "Maladie".equals(typeconge))
					{
						getResourceController().showBodyBlock(frag_nbrjrpredefNS, false);
						
					}
					else
					{
						getResourceController().showBodyBlock(frag_nbrjrpredefNS, true);
						
					}
//					if ("Maladie".equals(typeconge))
//					{
//						getResourceController().setMandatory(justifabsNS, true);
//					}
//					else
//					{
//						getResourceController().setMandatory(justifabsNS, false);
//					}
					
//					if ("Divers".equals(typeconge))
//					{
//						getResourceController().showBodyBlock(fragmotifNorNS, true);
//						getResourceController().setMandatory(motifNorNS, true);
//					}
//					else
//					{
//						getResourceController().showBodyBlock(fragmotifNorNS, false);
//						getResourceController().setMandatory(motifNorNS, false);
//					}
//					
//					String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
//					if ("Divers".equals(typecongeSpe))
//					{
//						getResourceController().showBodyBlock(fragmotifSpeNS, true);
//						getResourceController().setMandatory(motifSpeNS, true);
//					}
//					else
//					{
//						getResourceController().showBodyBlock(fragmotifSpeNS, false);
//						getResourceController().setMandatory(motifSpeNS, false);
//					}
					
					// get nombre prédéfini du type du congé
					connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
					String req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
					st = connection.prepareStatement(req);
					st.setString(1, typeconge);
					ResultSet rs = st.executeQuery();
					while (rs.next())
					{
						getWorkflowInstance().setValue(nbjrpredefNS, rs.getFloat(1));
					}
					
					
					
					// controle affichage du nombre de jours prédéfini du congé combiné
					String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
					
					if ("Maladie".equals(typecongespe))
					{
						getResourceController().setMandatory(justifabsspeNS, true);
					}
					else
					{
						getResourceController().setMandatory(justifabsspeNS, false);
					}
					
					// get nombre prédéfini du type du congé
					connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
					req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
					st = connection.prepareStatement(req);
					st.setString(1, typecongespe);
					rs = st.executeQuery();
					while (rs.next())
					{
						getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(1));
					}
					
					float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
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
					
					
					if ("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe))
					{
						getResourceController().setEditable(dateCongeFinSpeNS, true);
					}
					else
					{
						getResourceController().setEditable(dateCongeFinSpeNS, false);
					}
					
					if ("Maladie".equals(typeconge) || "Normal payé".equals(typeconge))
					{
						getResourceController().setEditable(dateCongeFinNS, true);
					}
					else
					{
						getResourceController().setEditable(dateCongeFinNS, false);
					}
					
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
				|| property.getName().equals(momententreeNS) || property.getName().equals(momentsortieNS))
		{
			return true;
		}
		
		
		return super.isOnChangeSubscriptionOn(property);
	}
	
	public List<IUser> getUserOfOrganization(String organization){
		
		List<IUser> users = new ArrayList<>();
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "SELECT loginVdoc FROM Personnel where FilialeIdFiliale = ?";
			st = connection.prepareStatement(req);
			st.setString(1, organization);
			ResultSet rss = st.executeQuery();
			while (rss.next())
			{
				String login = rss.getString(1);
				IUser user = getWorkflowModule().getUserByLogin(login);
				users.add(user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
				try
				{
					
					String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
					
					if (property.getName().equals("COLLABORATEUR_ATI"))
					{
						List<IUser> users = (List<IUser>) getWorkflowInstance().getValue("COLLABORATEUR_ATI");
						if(users!=null){
							if(users.size()!=0){
								getWorkflowInstance().setValue("PERSONNEL", users);
								getWorkflowInstance().save("PERSONNEL");
							}
						}
					}
					if (property.getName().equals("COLLABORATEURATTIJARIFINANCESCORP"))
					{
						List<IUser> users = (List<IUser>) getWorkflowInstance().getValue("COLLABORATEURATTIJARIFINANCESCORP");
						if(users!=null){
							if(users.size()!=0){
								getWorkflowInstance().setValue("PERSONNEL", users);
								getWorkflowInstance().save("PERSONNEL");
							}
						}
					}
					if(property.getName().equals(demandeur)){
						List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(demandeur);
						if(users!=null){
							if(users.size()!=0){
//								List<IUser> sups = new ArrayList<>();
//								sups.add(users.get(0).getHierarchicalManager());
//								getWorkflowInstance().setValue(supHieNS, sups);
//								getWorkflowInstance().save(supHieNS);
								getResourceController().showBodyBlock(fragDemandeur, true);
								// get nombre dispo du congé
								connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
								String loginVdocOfUser = users.get(0).getLogin();
								String req = "SELECT NombreJoursConge,NombrJoursDispo,reliquatAnneEnCours,FilialeIdFiliale FROM Personnel where loginVdoc = ?";
								st = connection.prepareStatement(req);
								st.setString(1, loginVdocOfUser);
								ResultSet rs = st.executeQuery();
								String filiale = "";
								while (rs.next())
								{
									getWorkflowInstance().setValue(droitAnnuelleNS, rs.getFloat(1));
									getWorkflowInstance().setValue(reliquatDroitAnnuelleNS, rs.getFloat(1));
									getWorkflowInstance().setValue(soldeAnterieurNS, rs.getFloat(2));
									getWorkflowInstance().setValue(soldeanneEncoursNS, rs.getFloat(3));
									
									filiale = rs.getString(4);
								}
								
								if ("attijariintermediation".equals(filiale))
								{
									getResourceController().showBodyBlock(frag_remplacantNS, true);
									// get superieur hierarchique
									users = new ServiceRH().getSuperieurOf(loginVdocOfUser);
									getWorkflowInstance().setValue(supHieNS, users);
									
									
									
									
								}
								else
								{
									getResourceController().showBodyBlock(frag_remplacantNS, false);
									users = new ServiceRH().getSuperieurOf(loginVdocOfUser);
									getWorkflowInstance().setValue(supHieNS, users);
									getWorkflowInstance().save(supHieNS);
									
									
								}
							}
							else{
								getWorkflowInstance().setValue(supHieNS, null);
								getResourceController().showBodyBlock(fragDemandeur, false);
							}
							
						}
						else{
							getWorkflowInstance().setValue(supHieNS, null);
							getResourceController().showBodyBlock(fragDemandeur, false);
						}
						
					}
					
					if (property.getName().equals(naturecongeNS))
					{
						if (natureconge.equals("Normal"))
						{
							getResourceController().showBodyBlock(frag_conge_combineNS, false);
						}
						else
						{
							getResourceController().showBodyBlock(frag_conge_combineNS, true);
							//Period periodCongeNor = (Period) getWorkflowInstance().getValue(periodecongenorNS);
//							Date dateFin = periodCongeNor.getEndDate();
//							Period periodCongeSpe = new Period(dateFin, dateFin);
//							getWorkflowInstance().setValue(periodecongespeNS, periodCongeSpe);
							
						}
					}
					
					
					if (property.getName().equals(typecongespeNS))
					{
						getWorkflowInstance().setValue(dateCongeDebSpeNS, null);
						getWorkflowInstance().setValue(dateCongeFinSpeNS, null);
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, 0f);
						
						String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
						if ("Divers".equals(typecongespe))
						{
							getResourceController().showBodyBlock(fragmotifSpeNS, true);
							getResourceController().setMandatory(motifSpeNS, true);
						}
						else
						{
							getResourceController().showBodyBlock(fragmotifSpeNS, false);
							getResourceController().setMandatory(motifSpeNS, false);
						}
						
						if ("Maladie".equals(typecongespe) )
						{
							getResourceController().setMandatory(justifabsspeNS, true);
						}
						else
						{
							getResourceController().setMandatory(justifabsspeNS, false);
						}
						
						
						if ("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe))
						{
							getResourceController().setEditable(dateCongeFinSpeNS, true);
						}
						else
						{
							getResourceController().setEditable(dateCongeFinSpeNS, false);
						}
						
						// get nombre prédéfini du type du congé
						connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
						String req = "SELECT nbrJrDef FROM TypeConge where TypeConge = ?";
						st = connection.prepareStatement(req);
						st.setString(1, typecongespe);
						ResultSet rs = st.executeQuery();
						while (rs.next())
						{
							getWorkflowInstance().setValue(nbjrpredefspeNS, rs.getFloat(1));
						}
						
						// Period periodCongeNor = (Period) getWorkflowInstance().getValue(periodecongenorNS);
						// Date dateFin = periodCongeNor.getEndDate();
						// Period periodCongeSpe = new Period(dateFin, dateFin);
						// getWorkflowInstance().setValue(periodecongespeNS,periodCongeSpe);
					}
					
					if (property.getName().equals(typecongeNS))
					{
						getWorkflowInstance().setValue(dateCongeDebNS, null);
						getWorkflowInstance().setValue(dateCongeFinNS, null);
						getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
						String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
						if ("Divers".equals(typeconge))
						{
							getResourceController().showBodyBlock(fragmotifNorNS, true);
							getResourceController().setMandatory(motifNorNS, true);
						}
						else
						{
							getResourceController().showBodyBlock(fragmotifNorNS, false);
							getResourceController().setMandatory(motifNorNS, false);
						}
						if ("Normal payé".equals(typeconge) || typeconge == null || "Maladie".equals(typeconge))
						{
							getResourceController().showBodyBlock(frag_nbrjrpredefNS, false);
							
						}
						else
						{
							getResourceController().showBodyBlock(frag_nbrjrpredefNS, true);
						}
						
						if ("Maladie".equals(typeconge) || "Divers".equals(typeconge))
						{
							getResourceController().setMandatory(justifabsNS, true);
						}
						else
						{
							getResourceController().setMandatory(justifabsNS, false);
						}
						
						
						if ("Maladie".equals(typeconge) || "Normal payé".equals(typeconge))
						{
							getResourceController().setEditable(dateCongeFinNS, true);
						}
						else
						{
							getResourceController().setEditable(dateCongeFinNS, false);
						}
						
						// get nombre prédéfini du type du congé
						connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
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
					if ("Maladie".equals(typeconge)  || "Normal payé".equals(typeconge))
					{
						Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
						getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "");
						if (property.getName().equals(dateCongeDebNS) || property.getName().equals(dateCongeFinNS))
						{
							Date dateFin = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
							if (dateDeb != null && dateFin != null)
							{
								if (dateDeb.after(dateFin))
								{
									getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "La date de reprise est antérieur à la date d'entrée");
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
									float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
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
					else if (!("Maladie".equals(typeconge)  || "Normal payé".equals(typeconge)))
					{
						if (property.getName().equals(dateCongeDebNS) /*|| property.getName().equals(dateCongeFinNS)*/)
						{
							
							float nbJoursPredef = (float) getWorkflowInstance().getValue(nbjrpredefNS);
							GregorianCalendar calendar = new java.util.GregorianCalendar();
							Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
							if(dateDeb !=null ){
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
										nbJoursPredef++;
									}
									
								}
								
								getWorkflowInstance().setValue(dateCongeFinNS, dateCalcul);
								getWorkflowInstance().setValue(nbrjrouvrNS, (float) getWorkflowInstance().getValue(nbjrpredefNS));
								}
							else{
								getWorkflowInstance().setValue(nbrjrouvrNS, 0f);
							}
							
							
						}
						
					}
					
				
					String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
					if ("Maladie".equals(typecongespe)  || "Normal payé".equals(typecongespe))
					{
						Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
						getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "");
						if (property.getName().equals(dateCongeDebSpeNS) || property.getName().equals(dateCongeFinSpeNS))
						{
							Date dateFin = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
							if (dateDeb != null && dateFin != null)
							{
								if (dateDeb.after(dateFin))
								{
									getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "La date de reprise est antérieur à la date d'entrée");
								}
								else
								{
									matinApmidisortieSpeNS = "Matin";
									matinApmidientreeSpeNS = "Matin";
									getWorkflowInstance().setValue(momententreeSpeNS, "Matin");
									getWorkflowInstance().setValue(momentsortieSpeNS, "Matin");
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
									float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
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
										getResourceController().alert("Le nombre de jours demandé du deuxième congé n'est pas authentique au solde séléctionnée");
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
					else if (!("Maladie".equals(typecongespe) || "Normal payé".equals(typecongespe)))
					{
						if (property.getName().equals(dateCongeDebSpeNS) /*|| property.getName().equals(dateCongeFinNS)*/)
						{
							float nbJoursPredef = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
							GregorianCalendar calendar = new java.util.GregorianCalendar();
							Date dateDeb = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
							if(dateDeb!=null){
								matinApmidisortieSpeNS = "Matin";
								matinApmidientreeSpeNS = "Matin";
								getWorkflowInstance().setValue(momententreeSpeNS, "Matin");
								getWorkflowInstance().setValue(momentsortieSpeNS, "Matin");
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
										nbJoursPredef++;
									}
									
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
						Date datedeb1 = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
						Date datefin1 = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
						
						float days = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
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
								
//								if ("Après midi".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//									days += 0.5f;
//								}
//								else if ("Matin".equals(momentEntree)&&"Matin".equals(momentSortie) && property.getName().equals(momententreeNS)){
//									days -= 0.5f;
//								}	
							}
						}
						
						getWorkflowInstance().setValue(nbrjrouvrNS,(float) days);
						matinApmidisortieNS = (String) getWorkflowInstance().getValue(momentsortieNS);
						matinApmidientreeNS = (String) getWorkflowInstance().getValue(momententreeNS);
						// float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
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
						
						getWorkflowInstance().setValue(nbrjrouvrSpeNS, (float)days);
						matinApmidisortieSpeNS = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
						matinApmidientreeSpeNS = (String) getWorkflowInstance().getValue(momententreeSpeNS);
						
						float nbrCongeDispoSpe = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
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
							|| property.getName().equals(typecongespeNS) || property.getName().equals(resteJoursCongeNS) || property.getName().equals(dateCongeDebNS)
							|| property.getName().equals(dateCongeDebSpeNS) || property.getName().equals(nbrjrouvrNS) || property.getName().equals(dateCongeFinNS)
							|| property.getName().equals(dateCongeFinSpeNS) || property.getName().equals(nbrjrouvrSpeNS) || property.getName().equals(momententreeNS)
							|| property.getName().equals(momentsortieNS))
					{
						// calculate nombre congé soldé
						typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
						String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
						float nbrJourDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
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
									if ("Normal payé".equals(typeconge) || "Divers".equals(typeconge))
									{
										getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJour);
										
										if ("Normal payé".equals(typecongeSpe) || "Divers".equals(typecongeSpe))
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
										
										if ("Normal payé".equals(typecongeSpe) || "Divers".equals(typecongeSpe))
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
									if ("Normal payé".equals(typeconge) || "Divers".equals(typeconge))
									{
										getWorkflowInstance().setValue(nbrJrSoldeNS, (float)nbrJour);
									}
									else
									{
										getWorkflowInstance().setValue(nbrJrNonSoldeNS,(float) nbrJour);
									}
									if (("Normal payé".equals(typecongeSpe) || "Divers".equals(typeconge)) && "Combiné".equals(natureconge))
									{
										getWorkflowInstance().setValue(nbrJrSoldeNS, 0f + (float)nbrJour);
									}
									else if (!("Normal payé".equals(typecongeSpe) || "Divers".equals(typeconge)) && "Combiné".equals(natureconge))
									{
										getWorkflowInstance().setValue(nbrJrNonSoldeNS, 0f + (float)nbrJour);
									}
								}
								
								if ("Maladie".equals(typeconge) || "Divers".equals(typeconge))
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
						
					}
					float reste = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
					if (reste < 0)
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
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
		super.onPropertyChanged(property);
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
		while (date1.before(date2) || date1.equals(date2))
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
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try{
			String btnForcerNS = "GCC_ForcerConge";
			if (action.getName().equals(btnenvoyerNS)||action.getName().equals(btnForcerNS))
			{
				
				String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
				// float nbrCongeDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
				float nbrJrCongeOuvrable = (float) getWorkflowInstance().getValue(nbrjrouvrNS);
				String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
				String typecongeSpe = (String) getWorkflowInstance().getValue(typecongespeNS);
				
				if (typeconge.equals(typecongeSpe))
				{
					getResourceController().alert("Les types de congé doivent différés");
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
					getWorkflowInstance().setValue(msgErreurPeriodeCongeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
					getResourceController().alert("Le nombre de jours demandé du premier congé n'est pas authentique au solde séléctionnée");
					return false;
				}
				
				float nbrJrCongeOuvrableSpe = (float) getWorkflowInstance().getValue(nbrjrouvrSpeNS);
				float nbrCongePredefSpe = (float) getWorkflowInstance().getValue(nbjrpredefspeNS);
				if (nbrCongePredefSpe != nbrJrCongeOuvrableSpe && nbrCongePredefSpe != 0 && natureconge.equals("Combiné") && !typecongeSpe.equals("Normal payé"))
				{
					getWorkflowInstance().setValue(msgErreurPeriodeCongeSpeNS, "Le nombre de jours demandé n'est pas authentique au solde séléctionnée.");
					getResourceController().alert("Le nombre de jours demandé du deuxième congé n'est pas authentique au solde séléctionnée");
					return false;
				}
				
				if (nbrJrCongeOuvrable == 0 || (nbrJrCongeOuvrableSpe == 0 && natureconge.equals("Combiné")))
				{
					if (nbrJrCongeOuvrable == 0)
						getResourceController().alert("Le nombre de jours de congé égal à 0. Pensez à modifier la période du premier congé");
					if((nbrJrCongeOuvrableSpe == 0 && natureconge.equals("Combiné")))
						getResourceController().alert("Le nombre de jours de congé égal à 0. Pensez à modifier la période du deuxième congé");	
					return false;
				}
				
				Date dateFinNor = (Date) getWorkflowInstance().getValue(dateCongeFinNS);
				Date dateDebSpe = (Date) getWorkflowInstance().getValue(dateCongeDebSpeNS);
				Date dateDebNor = (Date) getWorkflowInstance().getValue(dateCongeDebNS);
				Date dateFinSpe = (Date) getWorkflowInstance().getValue(dateCongeFinSpeNS);
				String login = "";
				List<IUser> users = (List<IUser>) getWorkflowInstance().getValue(demandeur);
				if(users!=null){
					if(users.size()!=0){
						IUser user = users.get(0);
						login = user.getLogin();
					}
				}
				
				String momentSortieNor = (String) getWorkflowInstance().getValue(momentsortieNS);
				String momentEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
				if(testDatesIftheyWasInConge(dateDebNor,dateFinNor,momentSortieNor,momentEntreeNor, login)==false){
					getResourceController().alert("la période séléctionnée est en chevauchement avec une autre période, pensez à changer les dates ou les moments de sorties/entrées !!!");
					return false;
				}
				
				
				
				
				if (dateFinNor.before(dateDebNor))
				{
					getResourceController().alert("La date fin est antérieur à la date debut du premier congé");
					return false;
				}
				
				if (dateFinSpe != null && dateDebSpe != null)
				{
					if (dateFinSpe.before(dateDebSpe))
					{
						getResourceController().alert("La date fin est antérieur à la date debut du deuxième congé");
						return false;
					}
				}
				
//				if (natureconge.equals("Combiné"))
//				{
//					if( dateDebSpe !=null && testDatesIftheyWasInConge(dateDebSpe, login)==false){
//						getResourceController().alert("La date début du deuxième congé désiré est en chevauchement avec un autre congé");
//						return false;
//					}
//					if(dateFinSpe!=null && testDatesIftheyWasInConge(dateFinSpe, login)==false){
//						getResourceController().alert("La date fin du deuxième congé désiré est en chevauchement avec un autre congé");
//						return false;
//					}
//					
//					if (dateDebSpe != null && dateFinNor != null)
//					{
//						GregorianCalendar cDateDebSpe = new GregorianCalendar();
//						cDateDebSpe.setTime(dateDebSpe);
//						GregorianCalendar cDateFinNor = new GregorianCalendar();
//						cDateFinNor.setTime(dateFinNor);
//						
//						if (!((cDateDebSpe.get(cDateDebSpe.YEAR)==cDateFinNor.get(cDateFinNor.YEAR))&&
//							(cDateDebSpe.get(cDateDebSpe.MONTH)==cDateFinNor.get(cDateFinNor.MONTH))&&
//							(cDateDebSpe.get(cDateDebSpe.DATE)==cDateFinNor.get(cDateFinNor.DATE))))
//						{
//							getResourceController().alert("Il y a un chevauchement entre les périodes.");
//							return false;
//						}
//					}
//				}
				
				// float nbrJoursDispo = (float) getWorkflowInstance().getValue(nbrjrcongdispoNS);
				// float nbrJoursSolde = (float) getWorkflowInstance().getValue(nbrJrSoldeNS);
				// if(nbrJoursSolde>nbrJoursDispo){
				// getResourceController().alert("Le nombre de jours soldés dépasse votre solde disponible");
				// return false;
				// }
				
				String mpEntreeNor = (String) getWorkflowInstance().getValue(momententreeNS);
				String mpSortieSpe = (String) getWorkflowInstance().getValue(momentsortieSpeNS);
				if (!mpEntreeNor.equals(mpSortieSpe)&&natureconge.equals("Combiné"))
				{
					getResourceController().alert("il y a un chevauchement entre le moment entrée du premier congé et le moment de sortie du deuxième congé.");
					return false;
				}																
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return super.onBeforeSubmit(action);
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try
		{
			if (action.getName().equals(""))
			{
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return super.onAfterSubmit(action);
	}
	
}
