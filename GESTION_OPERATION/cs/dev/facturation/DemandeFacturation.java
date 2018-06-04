package cs.dev.facturation;

import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.List;
//import org.apache.turbine.services.servlet.TurbineServlet;

//import com.axemble.vdoc.sdk.Modules;
//import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
//import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
//import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.framework.widgets.CtlButton;
import com.serviceRH.EncryptionFile;

import dao.ConnexionBDD;
public class DemandeFacturation extends ConnexionBDD{

    /**---------------------------------------*
     * @Created_on 08/12/2016 
     * @Updated_on 08/01/2018
     * @author r.sabri
     * @version 5.0
     *----------------------------------------*/
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(DemandeFacturationV1.class);
    //-----------------------------------------------------------------------------------------------------
    // DÉCLARATION DES VARRIABLES
    //-----------------------------------------------------------------------------------------------------
    int count =0;
    boolean ok = false;
    //-----------------------------------------------------------------------------------------------------
    // EVENEMENT DE CHARGEMENT DE FORMULAIRE
    //-----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterLoad() {
        //  REMPLIR LA LISTE « DOSSIER » PAR LES RÉFÉRENCES VDOC DES OPÉRATIONS EN COURS ET CLÔTURÉES, 
        //  DONT L'ÉTAT DE PAIEMENT N'EST PAS CLÔTURÉ. 
        getWorkflowInstance().setList("PF_Dossier",getOperations());

        // HISTORIQUE DES FACTURES
        HistoriqueFacture();
        return super.onAfterLoad();
    }
    //-----------------------------------------------------------------------------------------------------
    // CHARGER LES DOSSIERS DANS UN LISTE DYNAMIQUE VIA LA BASE DE DONNEES SQL
    //-----------------------------------------------------------------------------------------------------
    public ArrayList<IOption> getOperations()
    {
        cnx = null;
        st = null;
        rs = null;
        ArrayList<IOption> list = new ArrayList<IOption>();
        try {
            // DÉFINITION DE CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            // MANAGER CONNECTÉ
            IUser manager = getWorkflowModule().getLoggedOnUser();
            String username = manager.getFullName();
            String query = "select RefDossier from Dossiers where EtatPaiement='En cours' and Budget!=0 and Manager=? order by RefDossier desc";
            st = cnx.prepareStatement(query);
            st.setString(1, username);
            rs=st.executeQuery();
            while(rs.next())
            {
                //String Dossier = rs.getString(1);
                //int tiret =Dossier.indexOf("-");
                //String Result =Dossier.substring(0,tiret);
                list.add(getWorkflowModule().createListOption(rs.getString(1),rs.getString(1)));
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode liste des dossiers : " + e.getClass()+ " _ " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return list;
    }
    // ----------------------------------------------------------------------------------------------------
    // RÉCUPÉRER MANDAT CLIENT
    // ----------------------------------------------------------------------------------------------------
    public void getMandatClient(String Reference)
    {
        cnx =null;
        st = null;
        rs = null;
        String Status = null;
        String pj = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query = "SELECT MandatSign_pj, Client,StatutMandat" + 
                    " FROM Dossiers" + 
                    " WHERE RefDossier = ?";
            st = cnx.prepareStatement(query);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                getWorkflowInstance().setValue("PF_Client", rs.getString(2));
                Status = rs.getString(3);
                pj = rs.getString(1);
                if (pj!= null) {
                    // CRÉATION DE FICHIER DANS LE RÉPERTOIRE DE STOCKAGE TEMPORAIRE
                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//"+rs.getString(1));
                    if (doc.exists())
                    {
                        EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + doc, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + doc);
                        IAttachment att = getWorkflowModule().addAttachment(getWorkflowInstance(), "PF_Mandat", doc);
                        List<IAttachment> Manadat_Attache = ((List<IAttachment>) getWorkflowInstance().getValue("PF_Mandat"));
                        // INITIALISER LE CHAMPS PIÉCE JOINTE
                        Manadat_Attache.clear();
                        // AFFECTER LE FICHIER STOCKÉ DEPUIS LE RÉPERTOIRE TEMPORAIRE
                        Manadat_Attache.add(att); 
                        getResourceController().showBodyBlock("FragMandat", true);
                        // MANDAT AFFECTÉ POUR LA CRÉATION DE BUDGET
                        getWorkflowInstance().setValue("PF_Mandat", Manadat_Attache); 
                        // SUPPRIMER LE FICHIER TEMPORAIRE
                        //doc.delete(); 
                    }else {
                        getResourceController().inform("PF_Dossier", "Mandat (PJ) : "+rs.getString(1)+"\n"+"Le fichier spécifié est introuvable.");
                    }
                }else {
                    getResourceController().showBodyBlock("FragMandat", false);
                    getWorkflowInstance().setValue("PF_Mandat", null);
                    if (Status.equals("Pas de mandat")) {
                        getResourceController().alert("CE DOCUMENT N'A PAS DE MANDAT");
                    }else if (Status.equals("Mandat signé")) {
                        getResourceController().alert("LE MANDAT EST EN COURS DE SIGNATURE");
                    }else if (Status.equals("Mandat finalisé")){
                        getResourceController().alert("LE MANDAT EST EN COURS DE PRÉPARATION");
                    }else {
                        getResourceController().showBodyBlock("FragMandat", false);
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
    // ----------------------------------------------------------------------------------------------------
    // AFFICHAGE DE LA VUE DE L'HISTORIQUE DES FACTURES EN CAS DE PAIEMENT 
    // ----------------------------------------------------------------------------------------------------
    public void HistoriqueFacture()
    {
        cnx=null;
        st=null;
        rs=null;
        double Mt_Total =0d;
        try{
            getResourceController().showBodyBlock("FragHistoriqueFactures", false);
            // DÉFINITION DE LA CONNEXION
            String Dossier = (String) getWorkflowInstance().getValue("PF_Dossier");
            if (FactureExiste(Dossier)){
                // FRAGMENT D'AFFICHAGE POUR UN DOSSIER => ICI LA VU S'AFFICHE 
                getResourceController().showBodyBlock("FragHistoriqueFactures", true);
                @SuppressWarnings("unchecked")
                List<ILinkedResource> TabHistoriqueFacture = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PF_HistoriqueFactures");
                if(TabHistoriqueFacture.size()==0)
                {
                    cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                    String query = "SELECT Num_Fact,Montant_Fact,Date_Fact,Date_Echeance,Date_Remise FROM Factures where Dossier=?";
                    st = cnx.prepareStatement(query);
                    st.setString(1, Dossier);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        // CRÉATION D'UNE LIGNE 
                        ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "PF_HistoriqueFactures" );
                        // POSITIONNEMENT DE QUELQUES VALEURS 
                        linkedResource.setValue("HF_Tab_NumFacture", rs.getString("Num_Fact"));
                        linkedResource.setValue("HF_Tab_Montant", rs.getDouble("Montant_Fact"));
                        linkedResource.setValue("HF_Tab_MontantSM",ConnexionBDD.SeparateurMilliers(rs.getDouble("Montant_Fact")));  //Séparateur Milliers
                        linkedResource.setValue("HF_Tab_DateFacture",rs.getString("Date_Fact"));
                        linkedResource.setValue("HF_Tab_DateEcheance",rs.getString("Date_Echeance")); 
                        linkedResource.setValue("HF_Tab_DateRemise",rs.getString("Date_Remise"));
                        // AJOUT DE LA LIGNE AU TABLEAU
                        getWorkflowInstance().addLinkedResource( linkedResource );
                        // Montant total des factures
                        Mt_Total=Mt_Total+(rs.getDouble("Montant_Fact"));
                    }
                    //Object TOT = (Object) getWorkflowInstance().getValue("PF_Tab_PaiementTotal");
                    //Double Total = Double.parseDouble(TOT.toString());
                    getWorkflowInstance().setValue("PF_Tab_PaiementTotalSM", ConnexionBDD.SeparateurMilliers(Mt_Total));
                }
            }else {
                // CACHEZ LA VUE DE L'HISTORIQUE FACTURES
                getResourceController().showBodyBlock("FragHistoriqueFactures", false);
            }
        }catch(Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode HistoriqueFacture: " + e.getClass() + " - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // EVENEMENT DE CHANGEMENT
    // ----------------------------------------------------------------------------------------------------
    public void onPropertyChanged(IProperty property) {

        // EN CAS DE CHANGEMENT DE DOSSIER
        if(property.getName().equals("PF_Dossier"))
        {
            try {
                String Dossier = (String) getWorkflowInstance().getValue("PF_Dossier");
                if (Dossier!=null)
                {
                    ClearForm(); 			        // INITIALISER LE FORMULAIRE
                    Budget(Dossier);    			        // BUDGET DÉFINITIVE

                    double Budget = 0d;
                    if ((Object) getWorkflowInstance().getValue("PF_Budget")!=null) {
                        Object BU = (Object) getWorkflowInstance().getValue("PF_Budget");
                        Budget = Double.parseDouble(BU.toString());       
                    }

                    if (FactureExiste(Dossier)) {
                        getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                        getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                    }else {
                        getWorkflowInstance().setValue("PF_LasteReste", Budget);
                        getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget));
                    }

                    getMandatClient(Dossier);       // CLIENT AFFECTER DANS UN DOSSIER
                    HistoriqueFacture();	        // VU D'AFFICHAGE DE L'HITORIQUE DES FACTURES PAYANTS

                }else{
                    ClearForm();			
                    getResourceController().showBodyBlock("FragMandat", false);
                    getResourceController().showBodyBlock("FragHistoriqueFactures", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // =========================================================================================================
        // EN CAS DE CHANGEMENT DE MANTANT
        if(property.getName().equals("PF_MontantStr"))  
        {
            String Dossier = (String) getWorkflowInstance().getValue("PF_Dossier");
            if (Dossier!= null)
            {
                double Montant = 0d;
                Object MT = (Object) getWorkflowInstance().getValue("PF_MontantStr");
                if (!MT.equals("")&& (isNumeric(MT.toString()))) {
                    MT = (Object) getWorkflowInstance().getValue("PF_MontantStr");
                    Montant = Double.parseDouble(MT.toString().replaceAll(" ", "")); 
                }

                double Budget = 0d;
                if ((Object) getWorkflowInstance().getValue("PF_Budget")!=null) {
                    Object BU = (Object) getWorkflowInstance().getValue("PF_Budget");
                    Budget = Double.parseDouble(BU.toString());       
                }

                if (!(MT.equals(""))) // Montant<> null
                {

                    if (!isNumeric((String) getWorkflowInstance().getValue("PF_MontantStr"))) // Montant n'est pas numérique
                    {
                        //getResourceController().alert("Le Montant non valide");
                        getResourceController().inform("PF_MontantStr", "Le Montant non valide");
                        getWorkflowInstance().setValue("PF_MontantStr", "0");
                        if(FactureExiste(Dossier)) {
                            getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                            getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                        }else {
                            getWorkflowInstance().setValue("PF_LasteReste", Budget);
                            getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget));
                        }
                    }else { // Montant numérique
                        if (Montant == Budget) { // Budget == Montant
                            if(FactureExiste(Dossier)) {
                                //getResourceController().alert("Le Montant non valide");
                                getResourceController().inform("PF_MontantStr", "Le Montant non valide");
                                getWorkflowInstance().setValue("PF_MontantStr", "0");
                                getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                                getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                            }else {
                                getWorkflowInstance().setValue("PF_LasteReste", 0d);
                                getWorkflowInstance().setValue("PF_LasteResteSM", "0");
                            }
                        }else if ((Montant>0)&&(Montant<Budget)) { // Budget>Montant>0
                            if(FactureExiste(Dossier)) {
                                if(Montant<=(Budget-SommeFactures(Dossier)))
                                {
                                    getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier)-Montant);
                                    getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)-Montant));
                                }else {
                                    getWorkflowInstance().setValue("PF_MontantStr", "0");
                                    getResourceController().inform("PF_MontantStr", "Le montant est supérieur au reste à payer");
                                    getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                                    getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                                }
                            }else {
                                getWorkflowInstance().setValue("PF_LasteReste", Budget-Montant);
                                getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-Montant));
                            }
                        }else if ((Montant<0)||(Montant>Budget)){ // Budget<Montant<0 
                            //getResourceController().alert("Le Montant est supérieur ou inférieur à zéro !!");
                            getResourceController().inform("PF_MontantStr", "Le Montant est supérieur au budget ou inférieur à zéro !!");
                            getWorkflowInstance().setValue("PF_MontantStr", "0");
                            if(FactureExiste(Dossier)) {
                                getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                                getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                            }else {
                                getWorkflowInstance().setValue("PF_LasteReste", Budget);
                                getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget));
                            } 
                        }else {
                            if(FactureExiste(Dossier)) {
                                getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                                getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                            }else {
                                getWorkflowInstance().setValue("PF_LasteReste", Budget);
                                getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget));
                            }
                        }
                    }
                }else { // Montant ==null
                    getWorkflowInstance().setValue("PF_MontantStr", "0"); // Montant=0
                    if(FactureExiste(Dossier)) {
                        getWorkflowInstance().setValue("PF_LasteReste", Budget-SommeFactures(Dossier));
                        getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget-SommeFactures(Dossier)));
                    }else {
                        getWorkflowInstance().setValue("PF_LasteReste", Budget);
                        getWorkflowInstance().setValue("PF_LasteResteSM", ConnexionBDD.SeparateurMilliers(Budget));
                    }
                }
            }
        }

        // ===============================================================================================

        if (property.getName().equals("PF_Tab_PaiementTotal"))
        {
            if (getWorkflowInstance().getValue("PF_Tab_PaiementTotal")!=null)
            {
                Object TOPAI = (Object) getWorkflowInstance().getValue("PF_Tab_PaiementTotal");
                double D_ToPaie = Double.parseDouble(TOPAI.toString());
                getWorkflowInstance().setValue("PF_Tab_PaiementTotalSM", ConnexionBDD.SeparateurMilliers(D_ToPaie)); 
            }
        }
        if (property.getName().equals("PF_LasteResteSM"))
        {
            DernierPaiement();
        }
        super.onPropertyChanged(property);
    }
    // ----------------------------------------------------------------------------------------------------
    // MÉTHODE D'INITIALISATION DE FORMULAIRE
    // ----------------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void ClearForm()
    {
        getWorkflowInstance().setValue("PF_Client",null);            // CLIENT
        getWorkflowInstance().setValue("PF_Mandat", null);           // MANADAT (PJ)
        getWorkflowInstance().setValue("PF_Budget",0);		         // BUDGET DÉFINITIF
        getWorkflowInstance().setValue("PF_BudgetSM","0");
        getWorkflowInstance().setValue("PF_LasteReste",0);	         // RESTE à PAYÉ 
        getWorkflowInstance().setValue("PF_LasteResteSM","0");
        getWorkflowInstance().setValue("PF_MontantStr","0");         // MANTANT SAISIE
        // RÉNITIALISER LA VUE "HISTORIQUEFACTURES"
        List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PF_HistoriqueFactures");
        getWorkflowInstance().deleteLinkedResources(lst);
    }
    // ----------------------------------------------------------------------------------------------------
    // ÉVENEMENT D'ENVOIE DE FORMULAIRE
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onBeforeSubmit(IAction action) {
        boolean ok = true;
        if (action.getName().equals("PF_DemanderFacturation"))
        {
            double Montant = 0d;
            Object MT = (Object) getWorkflowInstance().getValue("PF_MontantStr");
            if (!MT.equals("")&& (isNumeric(MT.toString()))) {
                MT = (Object) getWorkflowInstance().getValue("PF_MontantStr");
                Montant = Double.parseDouble(MT.toString().replaceAll(" ", "")); 
            }
            if (Montant==0d)
            {
                ok = false;
                //getResourceController().alert("Le Montant est supérieur/inférieur au budget ou égale à zéro !");
                getResourceController().alert("Le Montant égale à zéro !");
            }
        }
        return ok;
    }
    // ----------------------------------------------------------------------------------------------------
    // IS ON CHANGE SUBSCRIPTION ON
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        // RETOURNE "TRUE" ON CAS DE CHANGEMENT

        if(property.getName().equals("PF_Dossier"))
        {
            return true;
        }
        if(property.getName().equals("PF_MontantStr"))
        {
            return true;
        }
        if (property.getName().equals("PF_LasteResteSM"))
        {
            return true;
        }
        if (property.getName().equals("PF_Tab_PaiementTotal"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
    // ----------------------------------------------------------------------------------------------------
    // IMPORTER LE BUDGET DÉFINITIF POUR UN DOSSIER 
    // ----------------------------------------------------------------------------------------------------
    public void Budget(String Deal)
    {
        cnx=null;
        st=null;
        rs=null;
        try{
            // DÉFINITION DE LA CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query ="select SUM(PS_FIX+PS_VAR+DS_FIX+DS_VAR)as 'budget' from Details_budget_budget where Dossier=?";
            st = cnx.prepareStatement(query);
            st.setString(1, Deal);
            rs = st.executeQuery();
            while(rs.next())
            {
                getWorkflowInstance().setValue("PF_Budget", rs.getDouble(1)); 
                getWorkflowInstance().setValue("PF_BudgetSM", ConnexionBDD.SeparateurMilliers(rs.getDouble(1))); 
            }
        }catch(Exception e){
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode Budget: " + e.getClass() + " - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // INDICATEUR DE PAIEMENT DE TOTAL FACTURE(S)
    // ----------------------------------------------------------------------------------------------------
    public void DernierPaiement(){
        try{
            Object O_Reste = (Object) getWorkflowInstance().getValue("PF_LasteReste");
            double Reste = Double.parseDouble(O_Reste.toString());
            if (Reste == 0d){
                getWorkflowInstance().setValue("PF_DernierPaiement", "Oui");
            }else{
                getWorkflowInstance().setValue("PF_DernierPaiement", "Non");
            }
        }catch (Exception e) {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode DernierPaiement: " + e.getClass() + " - " + message);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // EXISTANCE DE LA FACTURE
    // ----------------------------------------------------------------------------------------------------
    public boolean FactureExiste(String Deal)
    {
        cnx=null;
        st=null;
        rs=null;
        boolean existe = false;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query ="SELECT count(*) FROM Factures where Dossier=?";
            st = cnx.prepareStatement(query);
            st.setString(1, Deal);
            rs = st.executeQuery();
            while(rs.next())
                count = rs.getInt(1);
            if (count>0){
                existe = true;
            }else {
                existe = false;
            }
        }catch (Exception e) {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode FactureExiste : " + e.getClass() + " - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return existe;
    }
    // ----------------------------------------------------------------------------------------------------
    // SOMME DES FACTURES
    // ---------------------------------------------------------------------------------------------------
    public double SommeFactures(String Deal) {
        double somme=0d;
        cnx=null;
        st=null;
        rs=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query ="Select (sum(Montant_Fact)) as 'LeReste' from Factures where Dossier=?";
            st = cnx.prepareStatement(query);
            st.setString(1, Deal);
            rs = st.executeQuery();
            while(rs.next())
                somme = rs.getDouble("LeReste");

        }catch (Exception e) {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode FactureExiste : " + e.getClass() + " - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return somme;
    }
    // ----------------------------------------------------------------------------------------------------
    // EXPRESSION RÉGULIÈRE
    // ----------------------------------------------------------------------------------------------------
    public static boolean isNumeric(String str)
    {
        str = str.replaceAll(" ", "");
        return str.matches("^(?:(?:\\-{1})?\\d+(?:\\.{1}\\d+)?)$");
    }
}

