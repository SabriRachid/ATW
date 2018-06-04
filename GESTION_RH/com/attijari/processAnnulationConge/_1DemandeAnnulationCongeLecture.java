package com.attijari.processAnnulationConge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _1DemandeAnnulationCongeLecture extends BaseDocumentExtension
{
	
	private String naturecongeNS;
	private String frag_conge_combineNS;
	private String resteJoursCongeNS;
	private String commentaireNS;
	private String alerteNS;
	private String typecongespeNS;
	private String typecongeNS;
	private String frag_remplacantNS;
	private String fragAlerteNS;
	private String fragmotifNorNS;
	private String fragmotifSpeNS;
//	private String nbrJrTotalSoldeNS;
//	private String nbrJrResteSoldeNS;
	private static final long serialVersionUID = 4269810337595101111L;
	private IContext ctx;
	
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
		try
		{
			// get nom systeme des champ
						typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeCongeSpecial");
						typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_TypeConge");
						frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_REMPLACANT");
						fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTE");
						resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
						commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_COMM");
						
						naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_CongeNormalCombine");
						frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_DONNE_CONG_COMBINE");
						
						fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_MOTIFSPE");
						fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_MOTIFNOR");
						String fragIDconge = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_IDCONGNOR");
						getResourceController().showBodyBlock(fragIDconge, false);
						
						String loginVdocOfUser = getWorkflowInstance().getCreatedBy().getLogin();
						
						String reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_RESTESOLDEDISPOAPRES");
						String reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_SOLDEANNENCOURSAPMODIF");
						
						
						
						
						
						// nature congé controle
						String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
						
						if ("Non combiné".equals(natureconge))
						{
							getResourceController().showBodyBlock(frag_conge_combineNS, false);
						}
						else
						{
							getResourceController().showBodyBlock(frag_conge_combineNS, true);
						}	
						
						float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
						if (resteConge < 0)
						{
							getResourceController().setMandatory(commentaireNS, true);
//							getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
							getResourceController().showBodyBlock(fragAlerteNS, true);
						}
						else
						{
							getResourceController().setMandatory(commentaireNS, false);
//							getWorkflowInstance().setValue(alerteNS, "");
							getResourceController().showBodyBlock(fragAlerteNS, false);
						}
						
						
						
						
						
						
						
						Connection connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
						String req = "SELECT FilialeIdFiliale FROM Personnel where loginVdoc = ?";
						PreparedStatement st = connection.prepareStatement(req);
						st.setString(1, loginVdocOfUser);
						ResultSet rs = st.executeQuery();
						String filiale = "";
						while (rs.next())
						{
							filiale = rs.getString(1);
						}
						
						if ("attijariintermediation".equals(filiale))
						{
							getResourceController().showBodyBlock(frag_remplacantNS, true);
							getResourceController().showBodyBlock("FRAG_SUP", false);
							IUser collaborateur = getWorkflowInstance().getCreatedBy();
							List<IUser> remplacants = (List<IUser>) getWorkflowInstance().getValue("REMPLACANT");
							IUser remplacant = remplacants!=null? remplacants.size()>0 ? remplacants.get(0): null : null;
							IUser connectedUser = getWorkflowModule().getLoggedOnUser();
							
							List<IUser> sups = new ArrayList<IUser>();
							sups = new ServiceRH().getSuperieurOf(collaborateur.getLogin());
							
							if(remplacant!=null)
							if(connectedUser.getLogin().equals(remplacant.getLogin())&& 
									
									!sups.contains(connectedUser)//!connectedUser.getLogin().equals(collaborateur.getHierarchicalManager().getLogin())
									){
								getResourceController().showBodyBlock("FRAG_INFOS_REMPLACANT", false);
							}
						}
//						else if ("attijariwb".equals(filiale))
//						{
//							getResourceController().showBodyBlock(frag_remplacantNS, true);
//							getResourceController().showBodyBlock("FRAG_SUP", false);
//							IUser collaborateur = getWorkflowInstance().getCreatedBy();
//							List<IUser> remplacants = (List<IUser>) getWorkflowInstance().getValue("REMPLACANT");
//							IUser remplacant = remplacants!=null? remplacants.size()>0 ? remplacants.get(0): null : null;
//							IUser connectedUser = getWorkflowModule().getLoggedOnUser();
//							if(remplacant!=null)
//							if(connectedUser.getLogin().equals(remplacant.getLogin())&& !connectedUser.getLogin().equals(collaborateur.getHierarchicalManager().getLogin())){
//								getResourceController().showBodyBlock("FRAG_INFOS_REMPLACANT", false);
//							}
//						}
						else
						{
							getResourceController().showBodyBlock(frag_remplacantNS, false);
							getResourceController().showBodyBlock("FRAG_SUP", true);
							getResourceController().showBodyBlock("FRAG_INFOS_REMPLACANT", true);
						}
						
						
						
						String comptabiliseNor = getWorkflowInstance().getValue("ADC_COMPTANOR")==null ? null : (String) getWorkflowInstance().getValue("ADC_COMPTANOR");
						if(comptabiliseNor==null){
							getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
						}
						else{
							if(comptabiliseNor.equals("comptabilisé")){
								getResourceController().showBodyBlock("FRAGCOMPTANOR", true);
							}else{
								getResourceController().showBodyBlock("FRAGCOMPTANOR", false);
							}
						}
						
						String comptabiliseSpe = getWorkflowInstance().getValue("ADC_COMPTASPE")==null ? null : (String) getWorkflowInstance().getValue("ADC_COMPTASPE");
						if(comptabiliseSpe==null){
							getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
						}
						else{
							if(comptabiliseSpe.equals("comptabilisé")){
								getResourceController().showBodyBlock("FRAGCOMPTASPE", true);
							}else{
								getResourceController().showBodyBlock("FRAGCOMPTASPE", false);
							}
						}
						
//						String etatProcess = (String) getWorkflowInstance().getValue("ProcessState");
//						if(!etatProcess.equals("Terminé")){
//							ServiceRH srv = new ServiceRH();
//							getWorkflowInstance().setValue(reliquatSoldeanneEncoursNS, srv.getSoldeEnCours(loginVdocOfUser));
//							getWorkflowInstance().setValue(reliquatSoldeAnterieurNS, srv.getSoldeAnterieur(loginVdocOfUser));
//							String nbrJrSoldeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_NbreJrsPrisConge");
//							float droitAquis = srv.getDroitAcquis(loginVdocOfUser);
//							srv.setUpdatedSoldesPLUS(getWorkflowInstance(), reliquatSoldeanneEncoursNS, reliquatSoldeAnterieurNS, loginVdocOfUser, nbrJrSoldeNS, droitAquis);
//						}
						
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	
	
	
}
