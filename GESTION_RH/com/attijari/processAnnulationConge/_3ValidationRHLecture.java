package com.attijari.processAnnulationConge;



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
		fragBouton = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAGBOUTONJS");
		fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_FRAG_ALERTERH");
		resteJoursCongeNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_ResteJrsDispoConge");
		alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("ADC_Alerte"); 
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
}
