package cs.dev.operation;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.utils.Logger;

import dao.ConnexionBDD;
/**
 * @author r.sabri
 * @Creation_Date 03/01/2017 12:25 AM
 * @Plateform VDOC14 2.1 
 */
public class PreparationMandat extends ConnexionBDD{

    /**
     * #DECISIONS EQUIPE PROJET
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(PreparationMandat.class);
    // ------------------------------------------------------------------------------------------------
    // onAfterLoad()
    // ------------------------------------------------------------------------------------------------

    @Override
    public boolean onAfterLoad() {
        try {
            document_Equipes();
        }catch (Exception e) {
            log.error("CS Error in OnAfterLoad() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        return super.onAfterLoad();
    }

    // ------------------------------------------------------------------------------------------------
    // document_Equipes()
    // ------------------------------------------------------------------------------------------------

    public void document_Equipes()
    {
        Set<IUser> users = new HashSet<>() ;
        @SuppressWarnings("unchecked")
        List<IUser> role1 = (List<IUser>) getWorkflowInstance().getValue("GO_Manager");
        for(IUser ligne : role1){
            users.add(ligne);
        }
        @SuppressWarnings("unchecked")
        List<IUser> role2 = (List<IUser>) getWorkflowInstance().getValue("GO_SeniorAssociate");
        for(IUser ligne : role2){
            users.add(ligne);
        }
        @SuppressWarnings("unchecked")
        List<IUser> role3 = (List<IUser>) getWorkflowInstance().getValue("GO_Associate");
        for(IUser ligne : role3){
            users.add(ligne);
        }
        @SuppressWarnings("unchecked")
        List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GO_PrivilgEquipeProjet");
        if (tableListePersonnes.size()==0)
        {
            for(IUser ligne : users){
                ILinkedResource ligneToAdd = getWorkflowInstance().createLinkedResource("GO_PrivilgEquipeProjet");
                String A = getWorkflowModule().getLoggedOnUser().getLogin();
                String B = ligne.getLogin();
                if (!A.equals(B))
                {
                    ligneToAdd.setValue("GO_Tab_NomPrenom_Prev", ligne.getFullName());
                    getWorkflowInstance().addLinkedResource(ligneToAdd);
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------
    // onAfterSubmit()
    // ------------------------------------------------------------------------------------------------

    @Override
    public boolean onAfterSubmit(IAction action) {
        cnx = null;
        st = null;
        if (action.getName().equals("GO_PourValidationDG"))
        {
            try {
                cnx= getConnectionVDoc("Ref_Attijari").getConnection();  
                String RefrDossier = (String) getWorkflowInstance().getValue("sys_Reference");
                String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
                String RefFormater = NomProjet+"-"+RefrDossier;
                List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GO_PrivilgEquipeProjet");
                if (tableListePersonnes.size()!=0)
                {
                    for(ILinkedResource linked : tableListePersonnes){
                        String Query = "Update Collaborateur set Mandat=? where Fullname=? and RefDossier=?";
                        st = cnx.prepareStatement(Query);
                        st.setString(1, (String) linked.getValue("GO_Tab_AfficherMandat_Prev"));
                        st.setString(2, (String) linked.getValue("GO_Tab_NomPrenom_Prev")); 
                        st.setString(3, RefFormater);
                        st.executeUpdate();
                    }
                }
            }catch (Exception e) {
                log.error("CS Error in onAfterSubmit() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
            }finally {
                // LIBERER RESSOURCES DE LA MEMOIRE.
                ConnexionBDD.close(cnx, st);
            }

        }
        return super.onAfterSubmit(action);
    }

}
