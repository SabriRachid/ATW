package cs.dev.budget;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.turbine.services.servlet.TurbineServlet;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.framework.components.events.ActionEvent;
import com.axemble.vdp.ui.framework.components.listeners.ActionListener;
import com.axemble.vdp.ui.framework.foundation.Navigator;
import com.axemble.vdp.ui.framework.widgets.CtlButton;
import com.axemble.vdp.ui.framework.widgets.CtlText;

import dao.ConnexionBDD;
/**
 * @AUTHOR R.SABRI
 * @CREATION_DATE 01/12/2016 11:45 AM
 * @PLATEFORM VDOC14 2.1 W
 */
public class SaisieBudget_old extends ConnexionBDD{
    private static final long serialVersionUID = 1L;
    protected static final Logger log =  Logger.getLogger(SaisieBudget_old.class);
    protected ActionListener listnerBtn;
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
    /*---------------------------------------------------------------------------------------------------------------------------------
     * ONPROPERTYCHANGED
     *---------------------------------------------------------------------------------------------------------------------------------
     */
    @SuppressWarnings("unused")
    @Override
    public void onPropertyChanged(IProperty property) {
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
                    List<ILinkedResource> Tab1 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PB_Tab_OriginauxDetailBudget"); // VUE ORIGINAL
                    getWorkflowInstance().deleteLinkedResources(Tab1);
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
        if (property.getName().equals("PB_BudgetTotal"))
        {
            double Dbudget = 0d;
            Object budget = (Object) getWorkflowInstance().getValue("PB_BudgetTotal");
            if (budget != null)
            Dbudget = Double.parseDouble(budget.toString());
            
            getWorkflowInstance().setValue("PB_BudgetTotal_Str", ConnexionBDD.SeparateurMilliers(Dbudget));
        }
        super.onPropertyChanged(property);
    }
    /*------------------------------------------------------------------------------------------------------------------------------
     *ISONCHANGESUBSCRIPTIONON
     *------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if(property.getName().equals("PB_Dossier")){
            return true;
        }
        if(property.getName().equals("PB_BudgetTotal")) {
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
            // MANAGER CONNECTER
            //IUser manager = getWorkflowModule().getLoggedOnUser();
            //String username = manager.getFullName();
            //String query = "select d.RefDossier from Dossiers d,Collaborateur c where d.RefDossier=c.RefDossier and d.EtatDoc='En cours'and d.Budget != 0 and c.Fullname=?";
            //String query ="select RefDossier from Dossiers where EtatDoc='En cours'and EtatPaiement='En cours' and Budget!=0";
            String query ="select RefDossier from Dossiers where EtatDoc='En cours'and EtatPaiement='En cours'";
            st = cnx.prepareStatement(query);
            //st.setString(1, username);
            rs=st.executeQuery();
            while(rs.next())
            {
                //String Dossier = rs.getString(1);
                //int tiret =Dossier.indexOf("-");
                //String Result =Dossier.substring(0,tiret); // Return the name of projet 
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
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query = "SELECT MandatSign_pj,StatutMandat" + 
                    " FROM Dossiers" + 
                    " WHERE RefDossier = ?";
            st = cnx.prepareStatement(query);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                pj = rs.getString(1);
                Status = rs.getString(2);
                if (pj!= null) {
                    // CRÉATION DE FICHIER DANS LE RÉPERTOIRE DE STOCKAGE TEMPORAIRE
                    //File newFile = new File(TurbineServlet.getRealPath("AttijariDoc_Temp") + "\\" + filename);
                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//"+rs.getString(1));
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
                    getResourceController().showBodyBlock("FragMandatBudget", false);
                    getWorkflowInstance().setValue("PB_Mandat", null);
                    if (Status == "Pas de mandat") {
                        getResourceController().alert("CE DOCUMENT N'A PAS DE MANDAT");
                    }else if (Status == "Mandat signé") {
                        getResourceController().alert("LE MANDAT EST EN COURS DE SIGNATURE");
                    }else if (Status == "Mandat finalisé"){
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
            FillGridOriginal();
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
                linkedResource.setValue("PB_Tab_1erSemestreFixe",rs.getFloat("PS_FIX"));  
                linkedResource.setValue("PB_Tab_2emeSemestreFixe",rs.getFloat("DS_FIX"));
                linkedResource.setValue("PB_Tab_1erSemestreVariable",rs.getFloat("PS_VAR")); 
                linkedResource.setValue("PB_Tab_2emeSemestreVariable",rs.getFloat("DS_VAR"));
                linkedResource.setValue("PB_Tab_EtatVariable",rs.getString("Etat")); 
                if (rs.getString("Etat").equals("Non Facturable"))
                {
                    linkedResource.setValue("PB_NonFacturable",true); 
                    linkedResource.setValue("PB_Tab_TotalVariable", 0);  // TOTAL VARIBALE
                    linkedResource.setValue("PB_Tab_TotalAnnee",((rs.getFloat("PS_FIX"))+(rs.getFloat("DS_FIX")))); // TOTAL ANNEE
                }else {
                    linkedResource.setValue("PB_NonFacturable",false); 
                    linkedResource.setValue("PB_Tab_TotalVariable",((rs.getFloat("PS_VAR"))+(rs.getFloat("DS_VAR")))); // TOTAL VARIBALE
                    linkedResource.setValue("PB_Tab_TotalAnnee",((rs.getFloat("PS_FIX"))+(rs.getFloat("DS_FIX")))+(rs.getFloat("PS_VAR"))+(rs.getFloat("DS_VAR"))); // TOTAL ANNEE
                }
                // TOTAL FIX
                linkedResource.setValue("PB_Tab_TotalFixe",((rs.getFloat("PS_FIX"))+(rs.getFloat("DS_FIX"))));
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
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *METHODE DE CHARGEMENT TABLEAU DÉTAIL BUDGET ORIGINAL
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private void FillGridOriginal()
    {
        cnx = null;
        st = null;
        rs = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String cle =(String) getWorkflowInstance().getValue("PB_Dossier");
            String query = "select AnneeBudget,PS_FIX,DS_FIX,PS_VAR,DS_VAR,Etat from Details_budget_operation where Dossier =?";
            st = cnx.prepareStatement(query);
            st.setString(1, cle);
            rs= st.executeQuery();
            while (rs.next()) {
                // CRÉATION D'UNE LIGNE 
                ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "PB_Tab_OriginauxDetailBudget" );
                //POSITIONNEMENT DE QUELQUES VALEURS 
                linkedResource.setValue("PB_Tab_Annee_Org", rs.getString("AnneeBudget"));
                linkedResource.setValue("PB_Tab_1erSemestreFixe_Org",rs.getFloat("PS_FIX"));  
                linkedResource.setValue("PB_Tab_2emeSemestreFixe_Org",rs.getFloat("DS_FIX"));
                linkedResource.setValue("PB_Tab_1erSemestreVariable_Org",rs.getFloat("PS_VAR")); 
                linkedResource.setValue("PB_Tab_2emeSemestreVariable_Org",rs.getFloat("DS_VAR"));
                linkedResource.setValue("PB_Tab_EtatVariable_Org",rs.getString("Etat"));
                // TOTAL FIX
                linkedResource.setValue("PB_Tab_TotalFixe_Org",((rs.getFloat("PS_FIX"))+(rs.getFloat("DS_FIX"))));
                // TOTAL VARIBALE
                linkedResource.setValue("PB_Tab_TotalVariable_Org",((rs.getFloat("PS_VAR"))+(rs.getFloat("DS_VAR"))));
                // TOTAL ANNÉE
                linkedResource.setValue("PB_Tab_TotalAnnee_Org",((rs.getFloat("PS_FIX"))+(rs.getFloat("DS_FIX")))+(rs.getFloat("PS_VAR"))+(rs.getFloat("DS_VAR")));
                // AJOUT DE LA LIGNE AU TABLEAU
                getWorkflowInstance().addLinkedResource( linkedResource );
            }
        }catch(Exception e) {
            e.printStackTrace();
            log.info("CS LOG - ERROR IN FILLGRIDORIGINAL() METHOD : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *MISE À JOUR DES DETAILS BUDGET
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterSubmit(IAction action) {
        if(action.getName().equals("PB_PourNotification")) {
            cnx = null;
            st = null;
            rs = null;
            try {
                String DOSSIER =(String) getWorkflowInstance().getValue("PB_Dossier");
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
                        float PSFIX = (float)ligne.getValue("PB_Tab_1erSemestreFixe");             
                        float DSFIX = (float)ligne.getValue("PB_Tab_2emeSemestreFixe"); 
                        float PSVAR = (float)ligne.getValue("PB_Tab_1erSemestreVariable"); 
                        float DSVAR = (float)ligne.getValue("PB_Tab_2emeSemestreVariable"); 
                        String Etat =(String)ligne.getValue("PB_Tab_EtatVariable");
                        // MISE A JOUR DETAILS BUDGET
                        String Query2 = "insert into Details_budget_budget(AnneeBudget,PS_FIX,DS_FIX,PS_VAR,DS_VAR,Dossier,Etat) values(?,?,?,?,?,?,?)";
                        st = cnx.prepareStatement(Query2);
                        st.setString(1,ANNEE); 
                        st.setFloat(2, PSFIX); 
                        st.setFloat(3, DSFIX); 
                        st.setFloat(4, PSVAR); 
                        st.setFloat(5, DSVAR); 
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
        return super.onAfterSubmit(action);
    }
}
