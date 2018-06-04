package cs.dev.operation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import dao.ConnexionBDD;
/**
 * @AUTHOR R.SABRI
 * @CREATION_DATE 24/10/2016 11:50 AM
 * @PLATEFORM VDOC14 2.1 
 */
public class ReponseClientMandat extends ConnexionBDD{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ReponseClientMandat.class);
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("GO_DetailBudget"))
        {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GO_DetailBudget");
            if (tableListePersonnes.size()!=0)
            {
                if(getWorkflowInstance().getValue("GO_BudgetDefinitif")!= null)
                {
                    float BudgetDefinitif =(float)getWorkflowInstance().getValue("GO_BudgetDefinitif");
                    if(getWorkflowInstance().getValue("GO_BudgetTotal")!= null)
                    {
                        float BudgetTotal =(float)getWorkflowInstance().getValue("GO_BudgetTotal");
                        if(BudgetDefinitif < BudgetTotal)
                            getResourceController().alert("VOUS AVEZ DÉPASSÉ LE BUDGET DÉFINITIF");
                    }
                }  
            }
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if (property.getName().equals("GO_DetailBudget"))
        {
            return true;
        }
        return false;
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        try {
            if (action.getName().equals("GO_Valider_CreatBudget")){
                String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RéFéRENCE VDOC
                String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
                String RefFormater = NomProjet+"-"+RefDossier;
                ModifyDocument();
                DetailsBudget(RefFormater);
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
    @Override
    public boolean onBeforeSubmit(IAction action) {
        try {
            if (action.getName().equals("GO_Valider_CreatBudget")){
                if((getWorkflowInstance().getValue("GO_BudgetDefinitif") != null) || ((float) getWorkflowInstance().getValue("GO_BudgetDefinitif") != 0))
                {
                    float BudgetDefinitif =(float)getWorkflowInstance().getValue("GO_BudgetDefinitif");
                    float BudgetTotal =(float)getWorkflowInstance().getValue("GO_BudgetTotal");
                    
                    if(getWorkflowInstance().getValue("GO_BudgetDefinitif")!= null || BudgetDefinitif != 0)
                    {
                        if(BudgetDefinitif < BudgetTotal)
                        {
                            getResourceController().alert("VOUS AVEZ DÉPASSÉ LE BUDGET DÉFINITIF");
                            return false;
                        }else {
                            return true;
                        }
                    }
                }
                
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLèME DANS LA MéTHODE onBeforeSubmit :" + e.getMessage());
        }
        return super.onBeforeSubmit(action);
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public void SaveDocument() {
        cnx = null;
        st = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RéFéRENCE VDOC
            String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
            String RefFormater = NomProjet+"-"+RefDossier;
            float Budget = (float) getWorkflowInstance().getValue("GO_BudgetDefinitif");
            // Manager connecter
            IUser manager = getWorkflowModule().getLoggedOnUser();
            String FullName = manager.getFullName();
            String Query = "insert into Dossiers(RefDossier,Budget,Manager) values(?,?,?)";
            st = cnx.prepareStatement(Query);
            st.setString(1, RefFormater);
            st.setFloat(2, Budget); 
            st.setString(3, FullName);
            st.executeUpdate();
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLèME DANS LA MéTHODE SAVEDOCUMENT :" + e.getMessage());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public void ModifyDocument() {
       cnx = null;
       st = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RéFéRENCE VDOC
            String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
            String RefFormater = NomProjet+"-"+RefDossier;
            float Budget = (float) getWorkflowInstance().getValue("GO_BudgetDefinitif");
            String Statut = (String) getWorkflowInstance().getValue("GO_StatutMandat");
            String Query = "Update Dossiers set Budget = ?, StatutMandat=? where RefDossier = ?";
            st = cnx.prepareStatement(Query);
            st.setFloat(1, Budget);
            st.setString(2, Statut);
            st.setString(3, RefFormater);
            st.executeUpdate();
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLèME DANS LA MéTHODE MODIFYDOCUMENT - [REPONSECLIENTMANDAT]" + e.getMessage());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public void ActionQuery() {
        cnx = null; 
        st =null;
        rs = null;
        try {
            int count=0;
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String RefDossier = (String) getWorkflowInstance().getValue("sys_Reference"); // RéFéRENCE VDOC
            String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
            String RefFormater = NomProjet+"-"+RefDossier;
            String Query = "Select COUNT(*) from Dossiers where RefDossier = ?";
            st = cnx.prepareStatement(Query);
            st.setString(1, RefFormater);
            rs = st.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            if (count==0) {
                SaveDocument();
                DetailsBudget(RefFormater);
            }else {
                ModifyDocument();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLèME DANS LA MéTHODE ACTIONQUERY :" + e.getMessage());
        }
        finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public void DetailsBudget(String DOSSIER)
    {
        cnx = null;
        st = null;
        rs = null;
        try {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("GO_DetailBudget");
            if (tableListePersonnes.size()!=0)
            {
                if(IsExist(DOSSIER)== false){
                    // DETAILS NOT EXIST
                    for(ILinkedResource ligne : tableListePersonnes){
                        String ANNEE =(String) ligne.getValue("GO_Tab_Annee");
                        float PSFIX = (float)ligne.getValue("GO_Tab_1erSemestreFixe");             
                        float DSFIX = (float)ligne.getValue("GO_Tab_2emeSemestreFixe"); 
                        float PSVAR = (float)ligne.getValue("GO_Tab_1erSemestreVariable"); 
                        float DSVAR = (float)ligne.getValue("GO_Tab_2emeSemestreVariable"); 
                        String Etat =(String) ligne.getValue("GO_Tab_FacturableEtat");
                        cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                        String Query = "insert into Details_budget_operation(AnneeBudget,PS_FIX,DS_FIX,PS_VAR,DS_VAR,Dossier,Etat) values(?,?,?,?,?,?,?)";
                        st = cnx.prepareStatement(Query);
                        st.setString(1, ""+ANNEE); // String.valueOf(ANNEE) or Integer.toString(ANNEE)
                        st.setFloat(2, PSFIX); 
                        st.setFloat(3, DSFIX); 
                        st.setFloat(4, PSVAR); 
                        st.setFloat(5, DSVAR); 
                        st.setString(6, DOSSIER);
                        st.setString(7, Etat);
                        st.executeUpdate();
                    }
                }else{
                    // SUPPRIMER LES DETAILS BUDGET POUR UN DOSSIER SELECTIONNéE
                    String Query1 = "Delete from Details_budget_operation  where Dossier = ?";
                    st = cnx.prepareStatement(Query1);
                    st.setString(1,DOSSIER);
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLèME DANS LA MéTHODE DETAILSBUDGET :" + e.getMessage());
        }
        finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public boolean IsExist(String Operation){
        cnx = null;
        st = null;
        rs = null;
        boolean flag = true;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Query ="select COUNT(*) from Details_budget_operation where Dossier=?";
            st =cnx.prepareStatement(Query);
            st.setString(1, Operation);
            rs = st.executeQuery();
            rs.next(); // set the pointer to the first row
            if (rs.getInt(1)==0) {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - PROBLèME DANS LA MéTHODE ISEXIST :" + e.getMessage());
        }finally {
            // LIBéRER RESSOURCES DE LA MéMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return flag;
    }
}
