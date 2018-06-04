package com.attijari.GestionSalarie;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.impl.ProcessLinkedResource;
import com.axemble.vdoc.sdk.view.extensions.BaseViewExtension;
import com.axemble.vdoc.sdk.view.extensions.ViewItem;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelItem;
import com.axemble.vdp.ui.framework.widgets.CtlImage;

@SuppressWarnings("serial")
public class TableEnfantsView extends BaseViewExtension {

	

	@Override
	public void onPrepareItem(ViewItem obj) {
		// TODO Auto-generated method stub
		try {
			ViewModelItem vmi = obj.getViewModelItem();
			ProcessLinkedResource iw  = (ProcessLinkedResource) obj.getResource();
			String nom = (String) iw.getValue("P_GS_TAB_Enfant_Nom");
			String prenom = (String) iw.getValue("P_GS_TAB_Enfant_Prenom");
			String path = "skins/123456_Hamda_Mohamed.jpg";
			CtlImage image = new CtlImage(path);
			image.setHeight(100);
			image.setWidth(100);
			vmi.setValue("Indicateur", image);
			
		} catch (WorkflowModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
