package com.attijari.ProcessusCongeConstates;



import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;

public class _3ValidationRHLecture extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4102309135372902802L;
	
//	private String signatureNS;
	private String fragBouton;
	private String fragAlerteNS;
	private String resteJoursCongeNS;
	private String alerteNS;
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		fragBouton = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_BTNRH");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_FRAG_ALERTERH");
		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_ResteJrsDispoConge");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("GCC_Alerte"); 
		getResourceController().showBodyBlock(fragBouton, false);
		
		float resteConge = (float) getWorkflowInstance().getValue(resteJoursCongeNS);
		if (resteConge < 0)
		{
//			getWorkflowInstance().setValue(alerteNS, "Le reste des jours de congé est inférieur à 0");
			getResourceController().showBodyBlock(fragAlerteNS, true);
		}
		else
		{
//			getWorkflowInstance().setValue(alerteNS, "");
			getResourceController().showBodyBlock(fragAlerteNS, false);
		}
		
		return super.onAfterLoad();
	}
}
