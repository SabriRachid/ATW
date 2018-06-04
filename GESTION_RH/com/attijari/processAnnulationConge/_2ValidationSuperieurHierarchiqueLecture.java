package com.attijari.processAnnulationConge;

import java.sql.Connection;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;

public class _2ValidationSuperieurHierarchiqueLecture extends BaseDocumentExtension
{
	
	private String resteJoursCongeNS;
	private String alerteNS;
	private String fragAlerteNS;
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
			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTESUP");
			resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
			
			float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
			if (resteConge < 0)
			{
//				getWorkflowInstance().setValue(alerteNS, "Le reste des jours de cong� est inf�rieur � 0");
				getResourceController().showBodyBlock(fragAlerteNS, true);
			}
			else
			{
//				getWorkflowInstance().setValue(alerteNS, "");
				getResourceController().showBodyBlock(fragAlerteNS, false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	
	
	
}
