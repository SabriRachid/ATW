package com.attijari.processDemandeConge;

import java.sql.Connection;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;

public class _3ValidationSuperieurHierarchiqueLecture extends BaseDocumentExtension
{
	
	private String reliquatSoldeanneEncoursNS;
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
			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_ALERTSUP");
			
			reliquatSoldeanneEncoursNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIF");
			
			float resteConge = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
			if (resteConge < 0)
			{
//				getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
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
