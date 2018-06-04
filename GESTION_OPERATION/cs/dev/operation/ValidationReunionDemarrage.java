package cs.dev.operation;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;
/**
 * @author r.sabri
 * @Creation_Date 24/10/2016 12:25 AM
 * @Plateform VDOC14 2.1 
 */
public class ValidationReunionDemarrage extends BaseDocumentExtension{

    // ----------------------------------------------------------------------------------------------
    // DECISIONS EQUIPE PROJET
    // ----------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ValidationReunionDemarrage.class);
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    IWorkflowInstance instance;

    // -----------------------------------------------------------------------------------------------
    // CONNEXION AU BASE DE DONNEES 
    // -----------------------------------------------------------------------------------------------
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
        context = getWorkflowModule().getContextByLogin("sysadmin");
        return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }

    // ----------------------------------------------------------------------------------------------
    // onAfterLoad
    // ----------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterLoad() {
        try {
            getResourceController().showBodyBlock("FragmentFlag", false);
            PrivilegesMandat();
            document_Equipes();
            FlagParties();

        }catch (Exception e) {
            log.error("CS Error in OnAfterLoad() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }

        return super.onAfterLoad();
    }

    // ----------------------------------------------------------------------------------------------
    // document_Equipes
    // ----------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void document_Equipes()
    {
        // Manager :: GO_Manager
        // S Associate :: GO_SeniorAssociate
        // Associate :: GO_Associate
        // Decision Equipe projet 


        Set<IUser> users = new HashSet<>() ;

        List<IUser> role1 = (List<IUser>) getWorkflowInstance().getValue("GO_Manager");
        for(IUser ligne : role1){
            users.add(ligne);
        }
        List<IUser> role2 = (List<IUser>) getWorkflowInstance().getValue("GO_SeniorAssociate");
        for(IUser ligne : role2){
            users.add(ligne);
        }

        List<IUser> role3 = (List<IUser>) getWorkflowInstance().getValue("GO_Associate");
        for(IUser ligne : role3){
            users.add(ligne);
        }

        List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GO_DecisionsEquipeProjet");
        if (tableListePersonnes.size()==0)
        {
            //getWorkflowInstance().deleteLinkedResources(tableListePersonnes);
            for(IUser ligne : users){
                ILinkedResource ligneToAdd = getWorkflowInstance().createLinkedResource("GO_DecisionsEquipeProjet");
                //				String A = getWorkflowModule().getLoggedOnUser().getLogin();
                //				String B = ligne.getLogin();
                //				if (A.equals(B))
                //				{
                ligneToAdd.setValue("GO_Tab_NomPrenom", ligne.getFullName());
                getWorkflowInstance().addLinkedResource(ligneToAdd);
                //              }
            }
        }
    }
    // ----------------------------------------------------------------------------------------------
    // IS ON CHANGES
    // ---------------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty champ) {
        if(champ.getName().equals("GO_DecisionsEquipeProjet")) {
            return true;
        }
        return false;
    }

    // ---------------------------------------------------------------------------------------------
    // ON PROPERTY CHANGED
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onPropertyChanged(IProperty champ) {
        if(champ.getName().equals("GO_DecisionsEquipeProjet")) {

            FlagParties();
        }
        super.onPropertyChanged(champ);
    }

    // ---------------------------------------------------------------------------------------------
    // FlagParties
    // ---------------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void FlagParties()
    {
        instance = getWorkflowInstance();
        int i = 0;
        try
        {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GO_DecisionsEquipeProjet");
            if (tableListePersonnes.size()!=0)
            {

                for(ILinkedResource ligne : tableListePersonnes){

                    String Livrable = (String)ligne.getValue("GO_Tab_Valider");
                    if (Livrable.equals("Non"))
                    {
                        i++;


                    }else {

                    }
                }

                if (i>0) {
                    instance.setValue("GO_Flag", "0");
                }else
                {
                    instance.setValue("GO_Flag", "1"); 
                }

            }

        }catch (Exception e) {
            log.error("CS Error in FlagParties() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }


    }

    // ---------------------------------------------------------------------------------------------
    // PrivilegesMandat
    // ---------------------------------------------------------------------------------------------
    // Créer le 11/01/2017 23:16 
    public void PrivilegesMandat(){
        try {
            connection=ConnectionDefinition("Ref_Attijari").getConnection();
            String Mandat = null;
            String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RÉFÉRENCE VDOC
            String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
            String RefFormater = NomProjet+"-"+RefDossier;
            IUser Participant = getWorkflowModule().getLoggedOnUser();
            String FullName = Participant.getFullName();
            String Query = "Select Mandat from  Collaborateur where RefDossier =? and Fullname=?";
            st = connection.prepareStatement(Query);
            st.setString(1, RefFormater); 
            st.setString(2, FullName );
            result = st.executeQuery();
            while (result.next()){
                Mandat = result.getString(1);
            }
            if (Mandat!=null)
            {
                if (Mandat.equals("Oui"))
                {
                    getResourceController().showBodyBlock("FragMandat", true); // Affichez l'historique du mandat pour l'equipe FragMandat
                }else{
                    getResourceController().showBodyBlock("FragMandat", false); // Cachez l'historique du mandat pour l'equipe FragMandat
                }
            }
        }catch (Exception e) {
            log.error("CS Error in PrivilegesMandat() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }

    }
}
