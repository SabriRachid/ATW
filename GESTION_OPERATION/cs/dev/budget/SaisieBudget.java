package cs.dev.budget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.utils.Logger;
import com.serviceRH.EncryptionFile;

import dao.ConnexionBDD;
/**
 * @AUTHOR R.SABRI
 * @CREATION_DATE 01/12/2016 11:45 AM
 * @PLATEFORM VDOC14 2.1 W
 */
public class SaisieBudget extends ConnexionBDD{

    private static final long serialVersionUID = 1L;
    protected static final Logger log =  Logger.getLogger(SaisieBudget.class);
    /*---------------------------------------------------------------------------------------------------------------------------------
     *  ONAFTERLOAD
     *---------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterLoad() {
        //  REMPLIR LA LISTE « DOSSIER » PAR LES RÉFÉRENCES VDOC DES OPÉRATIONS EN COURS (QUI ONT DÉPASSÉ L’ÉTAPE « REPONSE CLIENT AU MANDAT »).
        getWorkflowInstance().setList("PB_Dossier",getOperations());
        getResourceController().showBodyBlock("FragMandatBudget", false);
        return super.onAfterLoad();
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("PB_BudgetTotal"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_BudgetTotal");
            if (budget != null)
                Dbudget = Double.parseDouble(budget.toString());

            getWorkflowInstance().setValue("PB_BudgetTotal_Str", ConnexionBDD.SeparateurMilliers(Dbudget));
        }
        if(property.getName().equals("PB_Dossier")){
            cnx = null;
            st = null;
            rs =null;
            try {
                getResourceController().showBodyBlock("FragMandatBudget", false);
                String Dossier = (String) getWorkflowInstance().getValue("PB_Dossier");
                if ((Dossier!=null )||(Dossier!=""))
                {
                    // initialiser les tableaux
                    List<ILinkedResource> Tab = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PB_DetailBudget"); // VUE COURANT ( UPDATE )
                    getWorkflowInstance().deleteLinkedResources(Tab);
                    getWorkflowInstance().setValue("PB_BudgetTotal", 0f);
                    getWorkflowInstance().setValue("PB_Mandat", null);
                    Mandat(Dossier);
                    RemplireTableBudgets_DB();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("CS LOG - ERREUR DANS LA METHODE ONPROPERTYCHANGED :"+e.getClass()+" _ "+ e.getStackTrace() +e.getMessage());
            }finally {
                // LIBÉRER RESSOURCES DE LA MÉMOIRE.
                ConnexionBDD.close(cnx, st, rs);
            }
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if (property.getName().equals("PB_BudgetTotal"))
        {
            return true;
        }
        if(property.getName().equals("PB_Dossier")){
            return true;
        }
        return false;
    }
    /*-------------------------------------------------------------------------------------------------------------------------------
     *CHARGER LES DOSSIERS
     *-------------------------------------------------------------------------------------------------------------------------------
     */
    public ArrayList<IOption> getOperations()
    {
        cnx = null;
        st = null;
        rs = null;
        ArrayList<IOption> lst = new ArrayList<IOption>();
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            // String query ="select RefDossier from Dossiers where EtatDoc='En cours'and EtatPaiement='En cours'";
            String query ="select RefDossier from Dossiers where EtatPaiement='En cours'";
            st = cnx.prepareStatement(query);
            rs=st.executeQuery();
            while(rs.next())
            {
                lst.add(getWorkflowModule().createListOption(rs.getString(1),rs.getString(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - ERREUR DANS LA METHODE LISTE DES DOSSIERS :"+e.getClass()+" _ "+ e.getStackTrace() +e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return lst;
    }
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        try {
            if (action.getName().equals("PB_PourNotification")){
                String Dossier = (String) getWorkflowInstance().getValue("PB_Dossier");
                ModifyDocument();
                DetailsBudget(Dossier);
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
        //        try {
        //            if (action.getName().equals("PB_PourNotification")){
        //                if((getWorkflowInstance().getValue("GO_BudgetDefinitif") != null) || ((float) getWorkflowInstance().getValue("GO_BudgetDefinitif") != 0))
        //                {
        //                    float BudgetDefinitif =(float)getWorkflowInstance().getValue("GO_BudgetDefinitif");
        //                    float BudgetTotal =(float)getWorkflowInstance().getValue("GO_BudgetTotal");
        //
        //                    if(getWorkflowInstance().getValue("GO_BudgetDefinitif")!= null || BudgetDefinitif != 0)
        //                    {
        //                        if(BudgetDefinitif < BudgetTotal)
        //                        {
        //                            getResourceController().alert("VOUS AVEZ DÉPASSÉ LE BUDGET DÉFINITIF");
        //                            return false;
        //                        }else {
        //                            return true;
        //                        }
        //                    }
        //                }
        //
        //            }
        //        }catch(Exception e)
        //        {
        //            e.printStackTrace();
        //            log.error("CS LOG - PROBLèME DANS LA MéTHODE onBeforeSubmit :" + e.getMessage());
        //        }
        return super.onBeforeSubmit(action);
    }

    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    public void ModifyDocument() {
        cnx = null;
        st = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String RefFormater = (String) getWorkflowInstance().getValue("PB_Dossier");
            Object BudgeTotal = (Object) getWorkflowInstance().getValue("PB_BudgetTotal");
            double BudgeTota = Double.parseDouble(BudgeTotal.toString());
            String Query = "Update Dossiers set Budget = ? where RefDossier = ?";
            st = cnx.prepareStatement(Query);
            st.setDouble(1, BudgeTota);
            st.setString(2, RefFormater);
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
    public void DetailsBudget(String DOSSIER)
    {

        cnx = null;
        st = null;
        rs = null;
        try {
            List<ILinkedResource> tableListePersonnes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PB_DetailBudget");
            if (tableListePersonnes.size()!=0)
            {
                cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                // SUPPRIMER LES DETAILS BUDGET POUR UN DOSSIER SELECTIONNÉE
                String Query1 = "Delete from Details_budget_budget where Dossier = ?";
                st = cnx.prepareStatement(Query1);
                st.setString(1,DOSSIER);
                st.executeUpdate();
                // ************************************************
                for(ILinkedResource ligne : tableListePersonnes)
                {
                    String ANNEE =(String) ligne.getValue("PB_Tab_Annee");

                    double PSFIX = 0d;
                    Object psfix = (Object)ligne.getValue("PB_Tab_1erSemestreFixe");    
                    PSFIX = Double.parseDouble(psfix.toString());

                    double DSFIX = 0d;
                    Object dsfix =(Object)ligne.getValue("PB_Tab_2emeSemestreFixe"); 
                    DSFIX = Double.parseDouble(dsfix.toString());

                    double PSVAR = 0d;
                    Object psvar = (Object)ligne.getValue("PB_Tab_1erSemestreVariable"); 
                    PSVAR = Double.parseDouble(psvar.toString());

                    double DSVAR = 0d;
                    Object dsvar = (Object)ligne.getValue("PB_Tab_2emeSemestreVariable"); 
                    DSVAR =  Double.parseDouble(dsvar.toString());

                    String Etat =(String)ligne.getValue("PB_Tab_EtatVariable");
                    // MISE A JOUR DETAILS BUDGET
                    String Query2 = "insert into Details_budget_budget(AnneeBudget,PS_FIX,DS_FIX,PS_VAR,DS_VAR,Dossier,Etat) values(?,?,?,?,?,?,?)";
                    st = cnx.prepareStatement(Query2);
                    st.setString(1,ANNEE); 
                    st.setDouble(2, PSFIX); 
                    st.setDouble(3, DSFIX); 
                    st.setDouble(4, PSVAR); 
                    st.setDouble(5, DSVAR); 
                    st.setString(6,DOSSIER);
                    st.setString(7,Etat);
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG - PROBLEME DANS LA METHODE MODIFICATIONDETAILSBUDGET: " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
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
    /*----------------------------------------------------------------------------------------------------------------------------
     * CODE-D'ATTACHEMENT PIÈCE JOINT : MANDAT                   
     * -----------------------------------------------------------------------------------------------------------------------------
     */ 
    @SuppressWarnings("unchecked")
    public void Mandat(String Reference)
    {
        cnx =null;
        st = null;
        rs = null;
        String Status = null;
        String pj = null;
        String[] List=null;
        String Projet=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query = "SELECT MandatSign_pj,StatutMandat,client,RefDossier" + 
                    " FROM Dossiers" + 
                    " WHERE RefDossier = ?";
            st = cnx.prepareStatement(query);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                pj = rs.getString(1);
                Status = rs.getString(2);
                getWorkflowInstance().setValue("PB_Client", rs.getString(3));
                Projet = rs.getString(4); // Projet-ReferenceExterne ==> DCM17-170118000009
                if (Projet!=null)
                {  
                    if (Projet.contains("-"))
                    {
                        List = rs.getString(4).split("-"); // Nom du projet
                        getWorkflowInstance().setValue("PB_Project", List[0]);
                    }else {
                        getWorkflowInstance().setValue("PB_Project", rs.getString(4));
                    }
                    
                }
                if (pj!= null) {
                    // CRÉATION DE FICHIER DANS LE RÉPERTOIRE DE STOCKAGE TEMPORAIRE
                    //File newFile = new File(TurbineServlet.getRealPath("AttijariDoc_Temp") + "\\" + filename);
                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//"+rs.getString(1));
                    if (doc.exists())
                    {
                        EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + doc, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + doc);
                        IAttachment att = getWorkflowModule().addAttachment(getWorkflowInstance(), "PB_Mandat", doc);
                        List<IAttachment> Manadat_Attache = ((List<IAttachment>) getWorkflowInstance().getValue("PB_Mandat"));
                        // INITIALISER LE CHAMPS PIÉCE JOINTE
                        Manadat_Attache.clear();
                        // AFFECTER LE FICHIER STOCKÉ DEPUIS LE RÉPERTOIRE TEMPORAIRE
                        Manadat_Attache.add(att); 
                        getResourceController().showBodyBlock("FragMandatBudget", true);
                        // MANDAT AFFECTÉ POUR LA CRÉATION DE BUDGET
                        getWorkflowInstance().setValue("PB_Mandat", Manadat_Attache); 
                        // SUPPRIMER LE FICHIER TEMPORAIRE
                        //doc.delete(); 
                    }else {
                        //getResourceController().alert("Mandat (PJ) : "+rs.getString(1)+"\n"+"Le fichier spécifié est introuvable.");
                        getResourceController().inform("PB_Dossier", "Mandat (PJ) : "+rs.getString(1)+"\n"+"Le fichier spécifié est introuvable.");
                    }
                }else {
                    getResourceController().showBodyBlock("FragMandatBudget", false);
                    getWorkflowInstance().setValue("PB_Mandat", null);
                    if (Status != null)
                        if (Status.equals("Pas de mandat")) {
                            getResourceController().alert("CE DOCUMENT N'A PAS DE MANDAT");
                        }else if (Status.equals("Mandat signé")) {
                            getResourceController().alert("LE MANDAT EST EN COURS DE SIGNATURE");
                        }else if (Status.equals("Mandat finalisé")){
                            getResourceController().alert("LE MANDAT EST EN COURS DE PREPARATION");
                        }else {
                            getResourceController().showBodyBlock("FragMandatBudget", false);
                        }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in getNoteTable_Avant_Refuse() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *IMPORT VIA TABLE BUDGET 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void RemplireTableBudgets_DB() {
        try {
            List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PB_DetailBudget");
            getWorkflowInstance().deleteLinkedResources(lst);
            FillGrid();
            //FillGridOriginal();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("CS LOG - ERROR IN REMPLIRETABLEBUDGETS_DB() METHOD : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *METHODE DE CHARGEMENT TABLEAU DÉTAIL BUDGET
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private void FillGrid()
    {
        cnx = null;
        st = null;
        rs = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String cle =(String) getWorkflowInstance().getValue("PB_Dossier");
            String query = "select AnneeBudget,PS_FIX,DS_FIX,PS_VAR,DS_VAR,Etat from Details_budget_budget where Dossier =?";
            st = cnx.prepareStatement(query);
            st.setString(1, cle);
            rs= st.executeQuery();
            while (rs.next()) {
                // CRÉATION D'UNE LIGNE 
                ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "PB_DetailBudget" );
                //POSITIONNEMENT DE QUELQUES VALEURS 
                linkedResource.setValue("PB_Tab_Annee", rs.getString("AnneeBudget"));
                linkedResource.setValue("PB_Tab_1erSemestreFixe",rs.getDouble("PS_FIX"));  
                linkedResource.setValue("PB_Tab_2emeSemestreFixe",rs.getDouble("DS_FIX"));
                linkedResource.setValue("PB_Tab_1erSemestreVariable",rs.getDouble("PS_VAR")); 
                linkedResource.setValue("PB_Tab_2emeSemestreVariable",rs.getDouble("DS_VAR"));
                // ============================================
                linkedResource.setValue("PB_Tab_1erSemestreFixeStr",ConnexionBDD.SeparateurMilliers(rs.getDouble("PS_FIX")));  
                linkedResource.setValue("PB_Tab_2emeSemestreFixeStr",ConnexionBDD.SeparateurMilliers(rs.getDouble("DS_FIX")));
                linkedResource.setValue("PB_Tab_1erSemestreVariableStr",ConnexionBDD.SeparateurMilliers(rs.getDouble("PS_VAR"))); 
                linkedResource.setValue("PB_Tab_2emeSemestreVariableStr",ConnexionBDD.SeparateurMilliers(rs.getDouble("DS_VAR")));

                linkedResource.setValue("PB_Tab_EtatVariable",rs.getString("Etat")); 
                if (rs.getString("Etat").equals("Non Facturable"))
                {
                    linkedResource.setValue("PB_NonFacturable",true); 
                }else {
                    linkedResource.setValue("PB_NonFacturable",false); 
                }
                //linkedResource.setValue("PB_Tab_TotalVariable", 0);  // TOTAL VARIBALE
                linkedResource.setValue("PB_Tab_TotalAnnee",((rs.getDouble("PS_FIX"))+(rs.getDouble("DS_FIX")))); // TOTAL ANNEE
                linkedResource.setValue("PB_Tab_TotalAnneeStr",ConnexionBDD.SeparateurMilliers((rs.getDouble("PS_FIX"))+(rs.getDouble("DS_FIX"))));

                linkedResource.setValue("PB_Tab_TotalVariable",((rs.getDouble("PS_VAR"))+(rs.getDouble("DS_VAR")))); // TOTAL VARIBALE
                linkedResource.setValue("PB_Tab_TotalVariableStr",ConnexionBDD.SeparateurMilliers((rs.getDouble("PS_VAR"))+(rs.getDouble("DS_VAR"))));
                linkedResource.setValue("PB_Tab_TotalAnnee",((rs.getDouble("PS_FIX"))+(rs.getDouble("DS_FIX")))+(rs.getDouble("PS_VAR"))+(rs.getDouble("DS_VAR"))); // TOTAL ANNEE
                linkedResource.setValue("PB_Tab_TotalAnneeStr",ConnexionBDD.SeparateurMilliers((rs.getDouble("PS_FIX"))+(rs.getDouble("DS_FIX"))+(rs.getDouble("PS_VAR"))+(rs.getDouble("DS_VAR"))));
                // TOTAL FIX
                linkedResource.setValue("PB_Tab_TotalFixe",((rs.getDouble("PS_FIX"))+(rs.getDouble("DS_FIX"))));
                linkedResource.setValue("PB_Tab_TotalFixeStr",ConnexionBDD.SeparateurMilliers((rs.getDouble("PS_FIX"))+(rs.getDouble("DS_FIX"))));
                // AJOUT DE LA LIGNE AU TABLEAU
                getWorkflowInstance().addLinkedResource( linkedResource );
            }
        }catch(Exception e) {
            e.printStackTrace();
            log.info("CS LOG - ERROR IN FILLGRID() METHOD : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
}
