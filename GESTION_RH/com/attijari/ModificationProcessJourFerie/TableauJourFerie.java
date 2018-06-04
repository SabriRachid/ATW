package com.attijari.ModificationProcessJourFerie;

import java.util.Calendar;
import java.util.Date;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;

public class TableauJourFerie extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4809823329193207204L;
	private String anneeNS;
	private String dateJourFerieNS;
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		dateJourFerieNS = getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("DAT_JR_FER");
		anneeNS = getWorkflowInstance().getParentInstance().getCatalog().getConfiguration().getStringUserProperty("MOD_JRFRAN_ANNEMOD");
		return super.onAfterLoad();
	}
	
	@Override
	public boolean onBeforeSave()
	{
		try
		{
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
