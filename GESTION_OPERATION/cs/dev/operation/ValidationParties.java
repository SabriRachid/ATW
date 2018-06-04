package cs.dev.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
//import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

public class ValidationParties extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ValidationParties.class);
    IWorkflowInstance instance;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    
    @Override
    public boolean onAfterLoad() {
        Collection<ILinkedResource> TablesLivraisonParties = (Collection<ILinkedResource>) getWorkflowInstance().getLinkedResources("GL_ValidationParties");
        if (TablesLivraisonParties.size()==0) {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GL_LivraisonParties");
            for(ILinkedResource ligne : tableListePersonnes){
                ILinkedResource ligneToAdd = getWorkflowInstance().createLinkedResource("GL_ValidationParties");
                List<IUser> liste = (List<IUser>) ligne.getValue("GL_EquipePartieLiv");
                List<IUser> liste2 = (List<IUser>) ligne.getValue("GL_ChefEquipeLiv");
                String Designation = (String)ligne.getValue("GL_DesignationPartieLiv");
                String Taches = (String) ligne.getValue("GL_TachesLiv");
                Date DAte = (Date) ligne.getValue("GL_DatePartieLiv");
                
                Collection<IAttachment> cAttachment = (Collection<IAttachment>) ligne.getValue("GL_LivrablePartie"); 
                getWorkflowInstance().setValue("GL_LivrablePartieVal", cAttachment); 
                
                ligneToAdd.setValue("GL_DesignationPartieVal",Designation);
                ligneToAdd.setValue("GL_TachesVal",Taches);
                ligneToAdd.setValue("GL_DatePartieVal",DAte);
                ligneToAdd.setValue("GL_EquipePartieVal", liste);
                ligneToAdd.setValue("GL_ChefEquipeVal", liste2);
                getWorkflowInstance().addLinkedResource(ligneToAdd);
                
                
            }
        }
       
       
        return super.onAfterLoad();
    }
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *IS ON CHANGES
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty champ) {
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
        if(champ.getName().equals("GL_ValiderPartie")) {
            
            ValiderPartie();
        }
        super.onPropertyChanged(champ);
    }
    
    public void ValiderPartie()
    {
        instance = getWorkflowInstance();
        int i=1;
        try
        {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GL_LivraisonParties");
         if (tableListePersonnes.size()!=0)
            {
                
             for(ILinkedResource ligne : tableListePersonnes){
                 
                    String Livrable = (String)ligne.getValue("GL_Tab_LivrablePartie");
                    if (Livrable.equals("Non"))
                    {
                        i=0;
                        
                    }
                }
               
                if (i==1) {
                    getResourceController().getBottomButton("GL_Valider").setHidden(false);
                    getResourceController().setHidden("GL_Valider", false);
                }else
                {
                    getResourceController().getBottomButton("GL_Valider").setHidden(true);
                    getResourceController().setHidden("GL_Valider", true);
                    getResourceController().getBottomButton("GL_Valider").isHidden();
                }
                
            }
            
        }catch (Exception e) {
            log.error("CS Error in ValiderPartie() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        
        
    }
    

}
