package com.attijari.processAnnulationConge;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;

import dao.SingletonConnexionBDD;

public class _2ValidationSupHierarchique extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7501902336230684835L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String btnEnvoyerNS;
	private String btnRefuserNS;
	private String alerteNS;
	private String resteJoursCongeNS;
	private String fragAlerteNS;
	private String commentaireNS;
	private String btnRejeterNS;
	private String codeCongeNS;
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
		codeCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_DemandeConge");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTESUP");
		btnRefuserNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Refuser");
		btnRejeterNS = (String) getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Rejeter");
		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
		commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_CommentaireSupHierarchique");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Alerte"); 
		
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
		
		
		
		return super.onAfterLoad();
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		try{
			if(action.getName().equals(btnEnvoyerNS)){
//				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
//				String codeVDOC = (String) getWorkflowInstance().getValue("sys_Reference");
//				String req = "update Conge set EtatConge = ? where CodeVdocDemandeConge = ?";
//				st = connection.prepareStatement(req);
//				st.setString(1, "en etat validation rh");
//				st.setString(2, codeVDOC);
//				st.executeUpdate();
			}
			
			if(action.getName().equals(btnRefuserNS)||action.getName().equals(btnRejeterNS)){
				connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
				String codeVDOC = (String) getWorkflowInstance().getValue(codeCongeNS);
				String req = "update Conge set EtatConge = ? where CodeVdocDemandeConge = ?";
				st = connection.prepareStatement(req);
				st.setString(1, "valide");
				st.setString(2, codeVDOC);
				st.executeUpdate();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterSubmit(action);
	}
}
