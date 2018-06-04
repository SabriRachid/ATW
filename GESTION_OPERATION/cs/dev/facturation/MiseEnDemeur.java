package cs.dev.facturation;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
public class MiseEnDemeur extends ConnexionBDD{

    /**
     * Developped on 11/12/2016
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(MiseEnDemeur.class);
	//-----------------------------------------------------------------------------------------------------
	// EVENEMENT DE CHARGEMENT DE FORMULAIRE
	//-----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterLoad() {
    	String Reg =(String)getWorkflowInstance().getValue("PF_ReglementEffectueMiseEnDemeur");
		if (Reg==null)
		{
			getResourceController().getButtonContainer(2).get(0).setHidden(true); // CACHER BOUTON REANCER REGLEMENT
			getResourceController().getButtonContainer(2).get(1).setHidden(true); // CACHER BTN CLÙTURER
		}else{
			if (Reg.equalsIgnoreCase("Oui"))
			{
			    // VM => false;true
                // Server => true;false
				getResourceController().getButtonContainer(2).get(0).setHidden(true); // AFFICHER BOUTON CLÙTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(false);  // CACHER BOUTON REANCER REGLEMENT
			}else
			{
			    // VM => true;false
                // Server => false;true
				getResourceController().getButtonContainer(2).get(0).setHidden(false);  // CACHER BOUTON CLÙTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(true); // AFFICHER BOUTON REANCER REGLEMENT
			}
		}
    	return super.onAfterLoad();
    }
	// ----------------------------------------------------------------------------------------------------
	// EVENEMENT DE CHANGEMENT
	// ----------------------------------------------------------------------------------------------------
	@Override
	public void onPropertyChanged(IProperty property) {
		if(property.getName().equals("PF_ReglementEffectueMiseEnDemeur")) {
			String Reg =(String)getWorkflowInstance().getValue("PF_ReglementEffectueMiseEnDemeur");
			if (Reg.equalsIgnoreCase("Oui"))
			{ 
			    // VM => false;true
                // Server => true;false
				getResourceController().getButtonContainer(2).get(0).setHidden(true); // AFFICHER BOUTON CLÙTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(false);  // CACHER BOUTON REANCER REGLEMENT
			}else
			{
			    // VM => true;false
                // Server => false;true
				getResourceController().getButtonContainer(2).get(0).setHidden(false);  // CACHER BOUTON CLÙTURER
				getResourceController().getButtonContainer(2).get(1).setHidden(true); // AFFICHER BOUTON REANCER REGLEMENT
			}
		}
		super.onPropertyChanged(property);
	}
	// ----------------------------------------------------------------------------------------------------
	// IS ON CHANGE SUBSCRIPTION ON
	// ----------------------------------------------------------------------------------------------------
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property) {
		if(property.getName().equals("PF_ReglementEffectueMiseEnDemeur")) {
			return true;
		}
		return super.isOnChangeSubscriptionOn(property);
	}
}

