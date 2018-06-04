package cs.dev.operation;

import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdoc.sdk.interfaces.IAction;
import dao.ConnexionBDD;
/**
 * @AUTHOR R.SABRI
 * @CREATION_DATE 05/07/2017 09:10 AM
 * @PLATEFORM VDOC14 2.1 
 */
public class InvitationReunionDemarrage extends ConnexionBDD{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(InvitationReunionDemarrage.class);
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        try {
            if ((action.getName().equals("GO_PourValidationReunion")) || (action.getName().equals("GO_ForcerValidation"))){
                String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); 
                String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
                String Statut = (String) getWorkflowInstance().getValue("GO_StatutMandat");
                UpdateStatus(RefDossier, NomProjet, Statut);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLÈME DANS LA MÉTHODE onBeforeSubmit :" + e.getMessage());
        }
        return super.onAfterSubmit(action);
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public void UpdateStatus(String Ref, String Projet, String Stat) {
        cnx = null;
        st = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String RefFormater = Projet+"-"+Ref;
            String Query = "Update Dossiers set StatutMandat=? where RefDossier = ?";
            st = cnx.prepareStatement(Query);
            st.setString(1, Stat);
            st.setString(2, RefFormater);
            st.executeUpdate();
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLÈME DANS LA MÉTHODE UPDATESTATUS - [InvitationReunionDemarrage]" + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    
}
