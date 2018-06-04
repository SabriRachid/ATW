package com.attijari.processJourFerie;

import java.util.Calendar;
import java.util.Date;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;

public class TableauJourFerie extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4809823329193207204L;
	private String idJourFerieNS;
	private String dateJourFerieNS;
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		idJourFerieNS = getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("IDJOURFERIE");
		dateJourFerieNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DAT_JR_FER");
		String idJourFerie = (String) getWorkflowInstance().getValue(idJourFerieNS);
		if( !("AFITR".equals(idJourFerie) || "AIDADH".equals(idJourFerie) ||
		   "ALMA".equals(idJourFerie) ||"NVHEG".equals(idJourFerie)) ){
			getResourceController().setEditable(dateJourFerieNS, false);
	}
		else{
			getResourceController().setEditable(dateJourFerieNS, true);
	}
		return super.onAfterLoad();
	}
	
	@Override
	public boolean onBeforeSave()
	{
		try
		{
			
			String anneeNS = getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("DEF_JF_ANNE");
			String dateJourFerieNS = getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("DAT_JR_FER");
			String anneeParentString = (String) getWorkflowInstance().getParentInstance().getValue(anneeNS);
				int anneeParent = Integer.parseInt(anneeParentString);
				Date dateSelectionne = (Date) getWorkflowInstance().getValue(dateJourFerieNS);
				Calendar ca = Calendar.getInstance();
				ca.setTime(dateSelectionne);
				int yearSelectionne = ca.get(Calendar.YEAR);
				if (anneeParent != yearSelectionne)
				{
					getResourceController().alert("l'année prévue pour les jours fériés n'est pas authentique à l'année séléctionnée");
					return false;
				}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.onBeforeSave();
	}
}
