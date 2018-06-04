package com.attijari.GestionSalarie;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IUser;

public class TabEnfants extends BaseDocumentExtension
{
	@Override
	public boolean onBeforeSave()
	{
		// TODO Auto-generated method stub
		return super.onBeforeSave();
	}
	
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		try
		{
			
			//IUser connectedUser = getWorkflowModule().getLoggedOnUser();
			
			String sexe = (String) getWorkflowInstance().getParentInstance().getValue("P_GS_Sexe");
			if("Masculin".equals(sexe)){
				String nomPere = (String) getWorkflowInstance().getParentInstance().getValue("P_GS_Nom");
				getWorkflowInstance().setValue("P_GS_TAB_Enfant_Nom", nomPere);
			}
			
//			IUser rhATI = getWorkflowModule().getUserByLogin(getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI"));
//			IUser rhAFC = getWorkflowModule().getUserByLogin(getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"));
			
//			if (!(connectedUser.equals(rhAFC) || connectedUser.equals(rhATI)))
//			{
//				getResourceController().setEditable("P_GS_TAB_Enfant_Nom", false);
//				getResourceController().setEditable("P_GS_TAB_Enfant_Prenom", false);
//				getResourceController().setEditable("P_GS_TAB_Enfant_DateNa", false);
//				getResourceController().setEditable("P_GS_CMIM_Enfant", true);
//				getResourceController().setMandatory("P_GS_CMIM_Enfant", true);
//			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
}
