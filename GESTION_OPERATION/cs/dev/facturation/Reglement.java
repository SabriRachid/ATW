package cs.dev.facturation;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
public class Reglement extends ConnexionBDD{

	/**
	 * Developped on 10/12/2016
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(Reglement.class);
	//-----------------------------------------------------------------------------------------------------
	// EVENEMENT DE CHARGEMENT DE FORMULAIRE
	//-----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterLoad() {
    	String Reg =(String)getWorkflowInstance().getValue("PF_ReglementEffectue");
		if (Reg==null)
		{
			getResourceController().getButtonContainer(2).get(0).setHidden(true); // CACHER BOUTON CLÔTURER
			getResourceController().getButtonContainer(2).get(1).setHidden(true); // CACHER BOUTON METTRE EN PRECONTENTIEUX
			getResourceController().getButtonContainer(2).get(2).setHidden(true); // CACHER BOUTON RELANCE REGLEMENT
		}else{
			if (Reg.equalsIgnoreCase("Oui"))
			{
			    // VM => true;true;false
                // Server => false;true;true
				getResourceController().getButtonContainer(2).get(0).setHidden(false);  // CACHER BOUTON CLÔTURER 
                getResourceController().getButtonContainer(2).get(1).setHidden(true);  // CACHER BOUTON METTRE EN PRECONTENTIEUX
                getResourceController().getButtonContainer(2).get(2).setHidden(true); // AFFICHER BOUTON RELANCE REGLEMENT
			}else
			{
			    // VM => false;false;true
                // Server => true;false;false
				getResourceController().getButtonContainer(2).get(0).setHidden(true);	// AFFICHER BOUTON CLÔTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(false); 	// AFFICHER BOUTON METTRE EN PRECONTENTIEUX
				getResourceController().getButtonContainer(2).get(2).setHidden(false); 	// CACHER BOUTON RELANCE REGLEMENT 
			}
		}
    	return super.onAfterLoad();
    }
	// ----------------------------------------------------------------------------------------------------
	// EVENEMENT DE CHANGEMENT
	// ----------------------------------------------------------------------------------------------------
	@Override
	public void onPropertyChanged(IProperty property) {

		if(property.getName().equals("PF_ReglementEffectue")) {
			String Reg =(String)getWorkflowInstance().getValue("PF_ReglementEffectue");
			if (Reg.equalsIgnoreCase("Oui"))
			{
			    // VM => true;true;false
                // Server => false;true;true
				getResourceController().getButtonContainer(2).get(0).setHidden(false);  // CACHER BOUTON RELANCE
				getResourceController().getButtonContainer(2).get(1).setHidden(true);  // CACHER BOUTON METTRE EN PRECONTENTIEUX
				getResourceController().getButtonContainer(2).get(2).setHidden(true); // AFFICHER BOUTON CLOTURER
				
			}else
			{
			    // VM => false;false;true
                // Server => true;false;false
				getResourceController().getButtonContainer(2).get(0).setHidden(true);	// AFFICHER BOUTON CLÔTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(false); 	// AFFICHER BOUTON METTRE EN PRECONTENTIEUX
				getResourceController().getButtonContainer(2).get(2).setHidden(false); 	// CACHER BOUTON RELANCE REGLEMENT 
			}
		}
		super.onPropertyChanged(property);
	}
	// ----------------------------------------------------------------------------------------------------
	// IS ON CHANGE SUBSCRIPTION ON
	// ----------------------------------------------------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
		if(property.getName().equals("PF_ReglementEffectue")) {
			return true;
		}
		return super.isOnChangeSubscriptionOn(property);
	}
}
