package cs.dev.operation;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;

public class LivraisonParties extends BaseDocumentExtension{

    /**
     * @author r.sabri
     * @date 08/11/2016
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(LivraisonParties.class);
    
    @Override
    public boolean onAfterLoad() {
        
        LivrablePartie();
        
        return super.onAfterLoad();
    }
    
    public void LivrablePartie()
    {
        
        try
        {
                 if (getWorkflowInstance().getValue("CP_PartieLivree")==null)
                 {
                     getResourceController().setMandatory("CP_LivrablePartie", false);
                     getResourceController().showBodyBlock("CP_FragLivrablePartie", false);
                       
                 }else {
                    
                    String Livrable = (String)getWorkflowInstance().getValue("CP_PartieLivree");
                    if (Livrable.equals("Non"))
                    {
                        getResourceController().setMandatory("CP_LivrablePartie", false);
                        getResourceController().showBodyBlock("CP_FragLivrablePartie", false);
                        
                    }else
                    {
                        getResourceController().setMandatory("CP_LivrablePartie", true);
                        getResourceController().showBodyBlock("CP_FragLivrablePartie", true);
                    }
                 }
        }catch (Exception e) {
            log.error("CS Error in LivrablePartie() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        
        
    }
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *IS ON CHANGES
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty champ) {
        if(champ.getName().equals("CP_PartieLivree")) {
            return true;
        }
        
        return false;
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ON PROPERTY CHANGED
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void onPropertyChanged(IProperty champ) {
        if(champ.getName().equals("CP_PartieLivree")) {
            
            LivrablePartie();
        }
        
        super.onPropertyChanged(champ);
    }
    
}
