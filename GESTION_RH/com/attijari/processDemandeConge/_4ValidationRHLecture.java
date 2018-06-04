package com.attijari.processDemandeConge;



import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;

public class _4ValidationRHLecture extends BaseDocumentExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4102309135372902802L;
	
//	private String signatureNS;
	private String fragBouton;
	private String fragAlerteNS;
	private String reliquatSoldeanneEncoursNS;
	private String alerteNS;
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		fragBouton = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALRH_FRAGBOUTONJS");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_ALERTRH");	
		reliquatSoldeanneEncoursNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIF");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_ALERT"); 
		getResourceController().showBodyBlock(fragBouton, false);
		
		float resteConge = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
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
