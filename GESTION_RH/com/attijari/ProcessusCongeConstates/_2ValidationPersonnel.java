package com.attijari.ProcessusCongeConstates;

import java.sql.Connection;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;

public class _2ValidationPersonnel extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2480242025510608704L;
	private IContext ctx;
	//private Connection connection;
	//private PreparedStatement st;
	private String btnEnvoyerNS;
	private String btnRefuserNS;
	private String alerteNS;
	private String resteJoursCongeNS;
	private String commentaireNS;
	private String fragAlerteNS;
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
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_ALERTDEM");
		btnEnvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Valider");
		btnRefuserNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Retour");
		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
		commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_CommentaireValDem");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Alerte"); 
		float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
		if(resteConge<0){
			getResourceController().setMandatory(commentaireNS, true);
			getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
			getResourceController().showBodyBlock(fragAlerteNS, true);
		}
		else{
			getResourceController().setMandatory(commentaireNS, false);
			getWorkflowInstance().setValue(alerteNS, "");
			getResourceController().showBodyBlock(fragAlerteNS, false);
		}
		return super.onAfterLoad();
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try{
			if(action.getName().equals(btnEnvoyerNS)){
//				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc().getConnection();
//				String codeVDOC = (String) getWorkflowInstance().getValue("sys_Reference");
//				String req = "update Conge set EtatConge = ? where CodeVdocDemandeConge = ?";
//				st = connection.prepareStatement(req);
//				st.setString(1, "en etat validation superieur hierarchique");
//				st.setString(2, codeVDOC);
//				st.executeUpdate();
			}
			
			if(action.getName().equals(btnRefuserNS)){
//				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc().getConnection();
//				String codeVDOC = (String) getWorkflowInstance().getValue("sys_Reference");
//				String req = "update Conge set EtatConge = ? where CodeVdocDemandeConge = ?";
//				st = connection.prepareStatement(req);
//				st.setString(1, "en etat refus superieur hierarchique");
//				st.setString(2, codeVDOC);
//				st.executeUpdate();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterSubmit(action);
	}
}
