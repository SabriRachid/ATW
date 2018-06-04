package cs.dev.operation;

//import java.util.Collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IUser;
//import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

/**
 * Created on 25 Octo. 2016 10:49 AM
 * @author R.SABRI
 * @version VDoc14
 */
public class PreparationLivrable extends BaseDocumentExtension {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(PreparationLivrable.class);
    IWorkflowInstance instance; 
    
    @Override
    public boolean onAfterLoad() {
        instance = getWorkflowInstance();
        try {
            //Collection<IAttachment> MandatSignPJ = (Collection<IAttachment>) instance.getValue("GO_MandatSigne");
            // Si la réponse client au mandat par oui alors le champs "Mandat signé" est en mode (ReadOnly)
            if ( instance.getValue("GO_ReponseClientMandat")!=null)
            {
                if (instance.getValue("GO_ReponseClientMandat").equals("Favorable")) {

                    getResourceController().setMandatory("GO_MandatSigne", false);
                    getResourceController().setEditable("GO_MandatSigne", false);

                }else {

                    getResourceController().setMandatory("GO_MandatSigne", true);
                    getResourceController().setEditable("GO_MandatSigne", true); 
                }
            }
        }catch(Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            log.error("Error in (OnAfterload) Methode :" + e.getClass() + " - " + message);
        }
        return super.onAfterLoad();
    }
    
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    /*--------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *--------------------------------------------------------------------------------------------------------------------------------
     */
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    context = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *onAfterSubmit
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterSubmit(IAction action) {
        try {
            connection=ConnectionDefinition("Ref_Attijari").getConnection();
            String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RÉFÉRENCE VDOC
			String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
			String RefFormater = NomProjet+"-"+RefDossier;
            String Query = "Update Dossiers set EtatDoc= ? where RefDossier = ?";
            st = connection.prepareStatement(Query);
            st.setString(1,"Clôturer");
            st.setString(2,RefFormater); 
            st.executeUpdate();
        }catch (Exception e) {
            log.error("Erreur dans la methode onAfterSubmit :"+e.getClass()+" _ "+ e.getStackTrace() +e.getMessage());
        }
        return super.onAfterSubmit(action);
    }
}
