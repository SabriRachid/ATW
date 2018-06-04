package cs.dev.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

public class TableauxParties extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(TableauxParties.class);
    IWorkflowInstance instance;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    
    
    @Override
    public boolean onAfterLoad() {

        getResourceController().setMandatory("GL_LivrablePartie", false);
        getResourceController().showBodyBlock("GL_FragLivrablePartie", false);
        LivrablePartie();
        ValiderPartie();
        
        return super.onAfterLoad();
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *IS ON CHANGES
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty champ) {
        if(champ.getName().equals("GL_Tab_LivrablePartie")) {
            return true;
        }
        if(champ.getName().equals("GL_ValiderPartie")) {
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
        if(champ.getName().equals("GL_Tab_LivrablePartie")) {
            
            LivrablePartie();
        }
        if(champ.getName().equals("GL_ValiderPartie")) {
            
            ValiderPartie();
        }
        super.onPropertyChanged(champ);
    }
    

    public void LivrablePartie()
    {
        instance = getWorkflowInstance();
        int i=1;
        try
        {
          
                
              
                    String Livrable = (String)getWorkflowInstance().getValue("GL_Tab_LivrablePartie");
                    if (Livrable.equals("Non"))
                    {
                        i=0;
                        
                    }
               
               
                if (i==1) {
                    getResourceController().setMandatory("GL_LivrablePartie", true);
                    getResourceController().showBodyBlock("GL_FragLivrablePartie", true);
                }else
                {
                    getResourceController().setMandatory("GL_LivrablePartie", false);
                    getResourceController().showBodyBlock("GL_FragLivrablePartie", false);
                }
                
            
            
        }catch (Exception e) {
            log.error("CS Error in Tab_LivrablePartie() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        
        
    }
    
    /**
     * 
     */
    public void ValiderPartie()
    {
        instance = getWorkflowInstance();
        int i=1;
        try
        {
               
                    String Livrable = (String)getWorkflowInstance().getValue("GL_ValiderPartie");
                    if (Livrable.equals("Non"))
                    {
                        i=0;
                        
                    }
                
               
                if (i==1) {
                    getResourceController().setMandatory("GL_ObservationsVal", false);
                }else
                {
                    getResourceController().setMandatory("GL_ObservationsVal", true);
                }
                
           
            
        }catch (Exception e) {
            log.error("CS Error in Tab_ValiderPartie() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        
        
    }
    
}
