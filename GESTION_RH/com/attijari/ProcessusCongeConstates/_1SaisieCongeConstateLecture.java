package com.attijari.ProcessusCongeConstates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class _1SaisieCongeConstateLecture extends BaseDocumentExtension
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
			typecongespeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_TypeCongeSpecial");
			typecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_TypeConge");
			frag_remplacantNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_REMPLACANT");
			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_ALERTE");
			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
			commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Commentaire");
			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
			naturecongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_CongeNormalCombine");
			frag_conge_combineNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_DONNE_CONG_COMBINE");
			
			fragmotifSpeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFSPE");
			fragmotifNorNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_MOTIFNOR");
			// nature congé controle
			String natureconge = (String) getWorkflowInstance().getValue(naturecongeNS);
			
			List<IUser> user = (List<IUser>) getWorkflowInstance().getValue("PERSONNEL");
			if(user!=null){
				String loginVdocOfUser = user.get(0).getLogin();
				ServiceRH srv = new ServiceRH();
				String etatProcess = (String) getWorkflowInstance().getValue("ProcessState");
//				if(!etatProcess.equals("Terminé")){
//					String reliquatSoldeAnterieurNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_RESTESOLDEDISPOMODIF");
//					String reliquatSoldeanneEncoursNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ReliquatSoldeAnneeEnCours");
//					float droitAquis = srv.getDroitAcquis(loginVdocOfUser);
//					String nbrJoursOuvrableNS = "GCC_NbreJrsSoldesConge";
//					srv.setUpdatedSoldesMOINS(getWorkflowInstance(), reliquatSoldeanneEncoursNS, reliquatSoldeAnterieurNS, loginVdocOfUser, nbrJoursOuvrableNS, droitAquis);
//				
//				}
			}
			
			
			if (natureconge.equals("Non combiné"))
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
//				getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
				getResourceController().showBodyBlock(fragAlerteNS, true);
			}
			else
			{
				getResourceController().setMandatory(commentaireNS, false);
//				getWorkflowInstance().setValue(alerteNS, "");
				getResourceController().showBodyBlock(fragAlerteNS, false);
			}
			
			String loginVdocOfUser = getWorkflowInstance().getCreatedBy().getLogin();
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
				getResourceController().showBodyBlock("FRAG_AFC", false);
				getResourceController().showBodyBlock("FRAG_ATI", true);
			}
			else
			{
				getResourceController().showBodyBlock(frag_remplacantNS, false);
				getResourceController().showBodyBlock("FRAG_SUP", true);
				getResourceController().showBodyBlock("FRAG_AFC", true);
				getResourceController().showBodyBlock("FRAG_ATI", false);
			}
			
			String typeconge = (String) getWorkflowInstance().getValue(typecongeNS);
			if ("Divers".equals(typeconge))
			{
				getResourceController().showBodyBlock(fragmotifNorNS, true);
			}
			else
			{
				getResourceController().showBodyBlock(fragmotifNorNS, false);
			}
			
			String typecongespe = (String) getWorkflowInstance().getValue(typecongespeNS);
			if (natureconge.equals("Combiné")){
				if ("Divers".equals(typecongespe))
				{
					getResourceController().showBodyBlock(fragmotifSpeNS, true);
				}
				else
				{
					getResourceController().showBodyBlock(fragmotifSpeNS, false);
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	
	
	
}
