package cs.dev.facturation;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
public class PreContentieux extends ConnexionBDD{

	/**
	 * Developped on 10/12/2016
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(PreContentieux.class);
	//-----------------------------------------------------------------------------------------------------
	// EVENEMENT DE CHARGEMENT DE FORMULAIRE
	//-----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterLoad() {
    	String Reg =(String)getWorkflowInstance().getValue("PF_ReglementEffectuePrecontentieux");
		if (Reg==null)
		{
			getResourceController().getButtonContainer(2).get(0).setHidden(true); // CACHER BOUTON CLÔTURER
			getResourceController().getButtonContainer(2).get(1).setHidden(true); // CACHER BOUTON METTRE EN DEMEURE
			getResourceController().getButtonContainer(2).get(2).setHidden(true); // CACHER BOUTON REANCER REGLEMENT
		}else{
			if (Reg.equalsIgnoreCase("Oui"))
			{
			    // VM => false;true;true
                // Server => true;false;true
				getResourceController().getButtonContainer(2).get(0).setHidden(true);  // AFFICHER BOUTON CLÔTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(false);   // CACHER BOUTON METTRE EN DEMEURE
				getResourceController().getButtonContainer(2).get(2).setHidden(true);   // CACHER BOUTON REANCER REGLEMENT
			}else
			{
			    // VM => true;false;false
                // Server => false;true;false
				getResourceController().getButtonContainer(2).get(0).setHidden(false);   // CACHER BOUTON CLÔTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(true);  // AFFICHER BOUTON METTRE EN DEMEURE
				getResourceController().getButtonContainer(2).get(2).setHidden(false);  // AFFICHER BOUTON REANCER REGLEMENT
			}
		}
    	
    	return super.onAfterLoad();
    }
	// ----------------------------------------------------------------------------------------------------
	// EVENEMENT DE CHANGEMENT
	// ----------------------------------------------------------------------------------------------------
	@Override
	public void onPropertyChanged(IProperty property) {
		if(property.getName().equals("PF_ReglementEffectuePrecontentieux")) {
			String Reg =(String)getWorkflowInstance().getValue("PF_ReglementEffectuePrecontentieux");
			if (Reg.equalsIgnoreCase("Oui"))
			{
			    // VM => false;true;true
                // Server => true;false;true
				getResourceController().getButtonContainer(2).get(0).setHidden(true);  // AFFICHER BOUTON CLÔTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(false);   // CACHER BOUTON METTRE EN DEMEURE
				getResourceController().getButtonContainer(2).get(2).setHidden(true);   // CACHER BOUTON REANCER REGLEMENT
			}else
			{
			    // VM => true;false;false
                // Server => false;true;false
				getResourceController().getButtonContainer(2).get(0).setHidden(false);   // CACHER BOUTON CLÔTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(true);  // AFFICHER BOUTON METTRE EN DEMEURE
				getResourceController().getButtonContainer(2).get(2).setHidden(false);  // AFFICHER BOUTON REANCER REGLEMENT
			}
		}

		super.onPropertyChanged(property);
	}
	// ----------------------------------------------------------------------------------------------------
	// IS ON CHANGE SUBSCRIPTION ON
	// ----------------------------------------------------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
		if(property.getName().equals("PF_ReglementEffectuePrecontentieux")) {
			return true;
		}
		return super.isOnChangeSubscriptionOn(property);
	}
}
