package cs.dev.operation;

import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.utils.Logger;

public class DecisionEquipeProjet extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(DecisionEquipeProjet.class);
    IWorkflowInstance instance;
    
    
    @Override
    public boolean onAfterLoad() {
        
        getResourceController().getButtonContainer(1).setHidden(true);
        
        // récupération de l'utilisateur connecté
        IUser user = getWorkflowModule().getLoggedOnUser();
        String Name = user.getFullName();
        Show_Resources(Name);
        decision();
        return super.onAfterLoad();
    }
    
    /*
     * (non-Javadoc)
     * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#onPropertyChanged(com.axemble.vdoc.sdk.interfaces.IProperty)
     */
    @Override
    public void onPropertyChanged(IProperty property) {
        
        if(property.getName().equals("GO_Tab_Valider"))
        {
            decision();
        }
    }
    public void decision() {
        
        instance = getWorkflowInstance();
        String ReponseClient = (String)instance.getValue("GO_Tab_Valider");
        
        if(ReponseClient.equals("Oui"))
        {
            // Cachez champ GO_MandatSigne et rend le non obligatoire
            getResourceController().setMandatory("GO_Tab_Commentaire",false );
            
        }else
        {
            // Cachez champ GO_MandatSigne et rend le non obligatoire
            getResourceController().setMandatory("GO_Tab_Commentaire",true);
            
        }
    }
    /*----------------------------------------------------------------------------------------
     * (non-Javadoc)
     * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#isOnChangeSubscriptionOn(com.axemble.vdoc.sdk.interfaces.IProperty)
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if(property.getName().equals("GO_Tab_Valider"))
        {
            return true;
        }
        return false;
    }
    
    // --------------------------------------------------------------------------------------------------
    
    public void Show_Resources(String intervenant)
    {
        try {
                    String login =(String)getWorkflowInstance().getValue("GO_Tab_NomPrenom");
                    if (login.equals(intervenant)) {
                        getResourceController().setEditable("GO_Tab_Valider", true);
                        getResourceController().setEditable("GO_Tab_Commentaire", true);
                    }else {
                        getResourceController().setEditable("GO_Tab_Valider", false);
                        getResourceController().setEditable("GO_Tab_Commentaire", false);
                        
                        getResourceController().setMandatory("GO_Tab_Valider", false);
                        getResourceController().setMandatory("GO_Tab_Commentaire", false);
                    }
                        
               
        }catch (Exception e) {
            log.error("CS Error in Show_Resources() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
         
        }
    
}
