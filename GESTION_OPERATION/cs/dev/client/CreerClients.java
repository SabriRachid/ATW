package cs.dev.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.framework.components.events.ActionEvent;
import com.axemble.vdp.ui.framework.components.listeners.ConfirmBoxListener;
import dao.ConnexionBDD;

public class CreerClients extends ConnexionBDD{
    /**
     * 26/12/2016 12:08 PM
     * last-Update 02/06/2017
     * R.SABRI
     * CRÉATION CLIENTS v3.1
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log =  Logger.getLogger(CreerClients.class);
    //--------------------------------------------------------------------------------------
    //DECLARATION DES VARRIABLES 
    //--------------------------------------------------------------------------------------
    String Action = null;
    String code = null;
    int count = 0;
    int siz = 0;
    @Override
    public boolean onAfterLoad() {
        try {
            // VERSION DE PROCESS 'GESION OPERATION'
            String workflowName=getWorkflowInstance().getWorkflow().getName();
            if(workflowName.equalsIgnoreCase("GESTIONDESOPERATIONS_1.0"))        
            {
                if (getWorkflowInstance().getValue("GO_CreateCustomers")=="Oui")
                    // WE GET THE REFRESH OF THE CUSTOMER SELECTOR
                    getWorkflowInstance().setValue("GO_CodeClients", null);
            }else{
                //  REMPLIR LA LISTE « CODE CLIENT » .
                getWorkflowInstance().setList("CodeClient_Mod",getOperations());
                getWorkflowInstance().setList("CodeClient_Rech",getOperations());
                // CACHEZ LE CONTAINER DU FORMULAIRE
                getResourceController().getButtonContainer(1).setHidden(true);
            } 
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - ERREUR DANS LA METHODE onAfterLoad :"+e.getClass()+" _ "+e.getMessage());
        }
        return super.onAfterLoad();
    }
    // -------------------------------------------------------------------------------------
    // LISTE DES CLIENTS
    // -------------------------------------------------------------------------------------
    public ArrayList<IOption> getOperations()
    {
        cnx=null;
        st=null;
        rs=null;
        ArrayList<IOption> lst = new ArrayList<IOption>();
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String query = "select CL_CodeClient,Cl_NomClient from Clients order by Cl_NomClient asc";
            st = cnx.prepareStatement(query);
            rs=st.executeQuery();
            while(rs.next())
            {
                String CODE = rs.getString(1);  
                int tiret =CODE.indexOf("-");
                String sub =CODE.substring(0,tiret);
                //String newCode = sub+"-"+rs.getString(2);
                //lst.add(getWorkflowModule().createListOption(rs.getString(1),newCode));
                lst.add(getWorkflowModule().createListOption(rs.getString(1),rs.getString(2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - ERREUR DANS LA METHODE LISTE DES CLIENTS :"+e.getClass()+" _ "+ e.getStackTrace() +e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return lst;
    }
    // -------------------------------------------------------------------------------------
    // EVENEMENT DE CHANGEMENT
    // -------------------------------------------------------------------------------------
    @SuppressWarnings({ "unused", "unchecked", "static-access" })
    @Override
    public void onPropertyChanged(IProperty property) {
        try {
            if(property.getName().equals("Actions"))
            {
                if (((String)getWorkflowInstance().getValue("Actions"))!=null || ((String)getWorkflowInstance().getValue("Actions"))!="")
                {
                    Action = (String) getWorkflowInstance().getValue("Actions");
                    if (Action.equals("ADD")){
                        // RÉNITIALISER LE FORMULAIRE D'INSERTION 
                        getWorkflowInstance().setValue("CodeClient", null);
                        getWorkflowInstance().setValue("NomClient", null);
                        getWorkflowInstance().setValue("FormJuridiqueSociete", null);
                        getWorkflowInstance().setValue("DroitSociete", null);
                        getWorkflowInstance().setValue("NumRC", null);
                        getWorkflowInstance().setValue("SecteurActivite", null);
                        getWorkflowInstance().setValue("TypologieEntreprise", null);
                        getWorkflowInstance().setValue("CoteBourceCasa", null);
                        getWorkflowInstance().setValue("SNI", null);
                        getWorkflowInstance().setValue("CapitalSocial", null);
                        // RÉNITIALISER LA VUE "TABLEAU DIRIGEANTS"
                        List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("TableauDirigeants");
                        getWorkflowInstance().deleteLinkedResources(lst);
                        // RÉNITIALISER LA VUE "STRUCTURE ACTIONNARIAT"
                        List<ILinkedResource> lst2 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("StructureActionnariat");
                        getWorkflowInstance().deleteLinkedResources(lst2);
                        // RÉNITIALISER LA VUE "DONNEES CHIFFREES"
                        List<ILinkedResource> lst3 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("DonneesChiffrees");
                        getWorkflowInstance().deleteLinkedResources(lst3);
                    }else if (Action.equals("MOD")){
                        // RÉNITIALISER LE FORMULAIRE DE MODIFICATION 
                        getWorkflowInstance().setValue("CodeClient_Mod", null);
                        getWorkflowInstance().setValue("NomClient_Mod", null);
                        getWorkflowInstance().setValue("FormeJuridiqueSoc_MOD", null);
                        getWorkflowInstance().setValue("DroitSociete_MOD", null);
                        getWorkflowInstance().setValue("NumRC_MOD", null);
                        getWorkflowInstance().setValue("SecteurActivite_MOD", null);
                        getWorkflowInstance().setValue("TypologieEntreprise_MOD", null);
                        getWorkflowInstance().setValue("CoteBourseCasa_MOD", null);
                        getWorkflowInstance().setValue("SNI_MOD", null);
                        getWorkflowInstance().setValue("CapitalSocial_MOD", null);
                        // RÉNITIALISER LA VUE "TABLEAU DIRIGEANTS"
                        List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("TableauDirigeants_MOD");
                        getWorkflowInstance().deleteLinkedResources(lst);
                        // RÉNITIALISER LA VUE "STRUCTURE ACTIONNARIAT"
                        List<ILinkedResource> lst2 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("StructureActionnariat_MOD");
                        getWorkflowInstance().deleteLinkedResources(lst2);
                        // RÉNITIALISER LA VUE "STRUCTURE ACTIONNARIAT"
                        List<ILinkedResource> lst3 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("DonneesChiffrees_MOD");
                        getWorkflowInstance().deleteLinkedResources(lst3);
                    }else if (Action.equals("DEL")){
                        // RÉNITIALISER LE FORMULAIRE 
                        getWorkflowInstance().setValue("CodeClient_Rech", null);
                    }
                }
            }
            // ================================================================
            if(property.getName().equals("CodeClient_Mod"))
            { 
                if (((String)getWorkflowInstance().getValue("CodeClient_Mod"))!=null || ((String)getWorkflowInstance().getValue("CodeClient_Mod"))!="")
                {
                    code = (String) getWorkflowInstance().getValue("CodeClient_Mod");
                }
                if(code==null)
                {
                    // RÉNITIALISER LE FORMULAIRE DE RECHERCHE 
                    getResourceController().setEditable("NomClient_Mod", false);
                }else{
                    RechecheClient();
                }
            }
            // ================================================================
            if(property.getName().equals("GO_CreateCustomers"))
            {
                String CreateCustomers =(String) getWorkflowInstance().getValue("GO_CreateCustomers");
                if (CreateCustomers.equals("O"))
                    // WE GET THE REFRESH OF THE CUSTOMER SELECTOR
                    getWorkflowInstance().setValue("GO_CodeClients", null);
            }
            // ================================================================
            if(property.getName().equals("GO_SP_Clients"))
            {
                // WE GET THE REFRESH OF THE CUSTOMER SELECTOR
                getWorkflowInstance().setValue("GO_CodeClients", null);
            }
            // ================================================================
            if(property.getName().equals("NomClient"))
            {
                getWorkflowInstance().setValue("CL_Client", getWorkflowInstance().getValue("NomClient"));
            }
            // ================================================================
            if(property.getName().equals("NomClient_Mod"))
            {
                getWorkflowInstance().setValue("CL_Client", getWorkflowInstance().getValue("NomClient_Mod"));
            }
            // ================================================================
            if(property.getName().equals("CodeClient_Rech"))
            {
                String result = null;
                cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                String code =(String)getWorkflowInstance().getValue("CodeClient_Rech");
                String query ="SELECT Cl_NomClient FROM Clients WHERE Cl_CodeClient=?";
                st =cnx.prepareStatement(query);
                st.setString(1, code);
                rs = st.executeQuery();
                while(rs.next())
                {
                    result = rs.getString("Cl_NomClient");
                }
                getWorkflowInstance().setValue("CL_Client",result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - ERREUR DANS LA METHODE onPropertyChanged :"+e.getClass()+" _ "+e.getMessage());
        }
        super.onPropertyChanged(property);
    }
    // -------------------------------------------------------------------------------------
    // IS ON CHANGE SUBSCRIPTION ON
    // -------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if(property.getName().equals("Actions"))
        {
            return true;
        }
        if(property.getName().equals("CodeClient_Mod"))
        {
            return true;
        }
        if(property.getName().equals("GO_CreateCustomers"))
        {
            return true;
        }
        if(property.getName().equals("NomClient"))
        {
            return true;
        }
        if(property.getName().equals("NomClient_Mod"))
        {
            return true;
        }
        if(property.getName().equals("CodeClient_Rech"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
    // -------------------------------------------------------------------------------------
    // CRÉER UN NOUVEAU CLIENT
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void NouveauClient()
    {
        cnx=null;
        st=null;
        try{
            // DÉFINITION DE CONNEXION ===============================
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Code_Client =(String) getWorkflowInstance().getValue("CodeClient");
            String Nom_Client =(String) getWorkflowInstance().getValue("NomClient");
            String FormeJuridique =(String) getWorkflowInstance().getValue("FormJuridiqueSociete");
            String Droit =(String) getWorkflowInstance().getValue("DroitSociete");
            String RC =(String) getWorkflowInstance().getValue("NumRC");
            String Secteur =(String) getWorkflowInstance().getValue("SecteurActivite");
            String Typologie =(String) getWorkflowInstance().getValue("TypologieEntreprise");
            String CoteBourceCasa =(String) getWorkflowInstance().getValue("CoteBourceCasa");
            String SNI =(String) getWorkflowInstance().getValue("SNI");
            String Capital =(String) getWorkflowInstance().getValue("CapitalSocial");
            String Query ="INSERT INTO Clients(Cl_CodeClient,Cl_NomClient,Cl_FormeJuridique,Cl_Droit,Cl_Capital," + 
                    "  Cl_RC ,Cl_Secteur,Cl_Typologie,Cl_Bource,Cl_SNI)" + 
                    "  VALUES (?,?,?,?,?,?,?,?,?,?)";
            st = cnx.prepareStatement(Query);
            st.setString(1, Code_Client); 		
            st.setString(2, Nom_Client); 		
            st.setString(3, FormeJuridique); 
            st.setString(4, Droit); 
            st.setString(5, Capital);
            st.setString(6, RC); 
            st.setString(7, Secteur);
            st.setString(8, Typologie); 
            st.setString(9, CoteBourceCasa);
            st.setString(10, SNI);
            st.executeUpdate();
            // METHODE D'INSERTION DES REPRESENTANTS
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("TableauDirigeants");
            if (associ.size() != 0) {
                SaveRepresentants(Code_Client);
            }
            // METHODE D'INSERTION DES ACTIONNAIRES
            Collection associ2 = (Collection) getWorkflowInstance().getLinkedResources("StructureActionnariat");
            if (associ2.size() != 0) {
                SaveActionnaires(Code_Client);
            }
            // METHODE D'INSERTION DES DONNÉES CHIFFRÉES
            Collection associ3 = (Collection) getWorkflowInstance().getLinkedResources("DonneesChiffrees");
            if (associ3.size() != 0) {
                SaveDonneesChiffrees(Code_Client);
            }
        }catch(Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";//
            }
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE NOUVEAUCLIENT : " + e.getClass()+ " _ " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // INSERTION DES REPRESENTANTS
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void SaveRepresentants(String Client)
    {
        cnx=null;
        st=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("TableauDirigeants");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    // ATTRIBUTS DES REPRESENTANTS  ===========================
                    String NomPrenom =(String)asso.getValue("TD_NomPrenom");
                    String Fonction =(String)asso.getValue("TD_Fonction");
                    String ID =(String)asso.getValue("TD_IdentiteMandant");
                    String Query ="INSERT INTO Representant([Re_NomPrenom],[Re_Fonction],[Re_Identite],[Code_Client])VALUES(?,?,?,?)";
                    st = cnx.prepareStatement(Query);
                    st.setString(1, NomPrenom);      
                    st.setString(2, Fonction);       
                    st.setString(3, ID);
                    st.setString(4, Client); 
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE SaveRepresentants : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // INSERTION DES ACTIONNAIRES
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void SaveActionnaires(String Client)
    {
        cnx=null;
        st=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("StructureActionnariat");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    // ATTRIBUT DES ACTIONNAIRES ==============================
                    String Action =(String)asso.getValue("SA_Action");
                    float Pourcentage =(float)asso.getValue("SA_Pourcentage");
                    String Query ="INSERT INTO Actionnaires (Ac_Action,Ac_Pourcentage,Code_Client)" + 
                            "  VALUES (?,?,?)";
                    st = cnx.prepareStatement(Query);
                    st.setString(1, Action);      
                    st.setFloat(2, Pourcentage);       
                    st.setString(3, Client); 
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE SaveActionnaires : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // INSERTION DES DONNÉES CHIFFRÉES
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void SaveDonneesChiffrees(String Client)
    {
        cnx=null;
        st=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("DonneesChiffrees");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) 
                {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String YEAR =(String) asso.getValue("DC_Tab_Annees");
                    String CA =(String) asso.getValue("DC_Tab_ChiffreAffaires");
                    String EBE = "";
                    String REX = "";
                    String RN = "";
                    String FP = "";
                    String TB = "";
                    String DN = "";
                    if (asso.getValue("DC_Tab_EBE")!= null ) {
                        EBE =(String) asso.getValue("DC_Tab_EBE");  
                    }
                    if (asso.getValue("DC_Tab_REX")!= null ) {
                        REX =(String) asso.getValue("DC_Tab_REX");
                    }
                    if (asso.getValue("DC_Tab_RN")!= null ) {
                        RN =(String) asso.getValue("DC_Tab_RN");
                    }
                    if (asso.getValue("DC_Tab_FP")!= null ) {
                        FP =(String) asso.getValue("DC_Tab_FP");
                    }
                    if (asso.getValue("DC_Tab_TotalBilan")!= null ) {
                        TB =(String) asso.getValue("DC_Tab_TotalBilan");
                    }
                    if (asso.getValue("DC_Tab_DN")!= null ) {
                        DN =(String) asso.getValue("DC_Tab_DN");
                    }
                    String Query ="INSERT INTO Donnees_Chiffrees(DC_Annee,DC_CA,DC_EBE,DC_REX,DC_RN,DC_FP,DC_TB,DC_DN,Code_Client) VALUES (?,?,?,?,?,?,?,?,?)";
                    st = cnx.prepareStatement(Query);
                    st.setString(1, YEAR); 
                    st.setString(2, CA);
                    st.setString(3, EBE);
                    st.setString(4, REX);
                    st.setString(5, RN);
                    st.setString(6, FP);
                    st.setString(7, TB);
                    st.setString(8, DN);      
                    st.setString(9, Client); 
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE DONNEESCHIFFREES : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st); 
        }
    }
    // -------------------------------------------------------------------------------------
    // MODIFEIR UN CLIENT EXISTANT
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void ModifierClient()
    {
        cnx=null;
        st=null;
        try{
            // DÉFINITION DE CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            if (((String)getWorkflowInstance().getValue("CodeClient_Mod"))!=null || ((String)getWorkflowInstance().getValue("CodeClient_Mod"))!="")
            {
                code = (String) getWorkflowInstance().getValue("CodeClient_Mod");
            }
            String Nom_Client =(String) getWorkflowInstance().getValue("NomClient_Mod"); 
            String FormeJuridique =(String) getWorkflowInstance().getValue("FormeJuridiqueSoc_MOD"); 
            String Droit =(String) getWorkflowInstance().getValue("DroitSociete_MOD"); 
            String RC =(String) getWorkflowInstance().getValue("NumRC_MOD"); 
            String Secteur =(String) getWorkflowInstance().getValue("SecteurActivite_MOD"); 
            String Typologie =(String) getWorkflowInstance().getValue("TypologieEntreprise_MOD"); 
            String CoteBourceCasa =(String) getWorkflowInstance().getValue("CoteBourseCasa_MOD"); 
            String SNI =(String) getWorkflowInstance().getValue("SNI_MOD"); 
            String Capital =(String) getWorkflowInstance().getValue("CapitalSocial_MOD"); 
            String Query = "UPDATE Clients SET Cl_NomClient = ?,Cl_FormeJuridique = ?,Cl_Droit = ?,Cl_Capital = ?,Cl_RC = ?,Cl_Secteur = ?,Cl_Typologie = ?," + 
                    " Cl_Bource = ?,Cl_SNI = ?  WHERE Cl_CodeClient = ?";
            st = cnx.prepareStatement(Query);
            st.setString(1, Nom_Client);      
            st.setString(2, FormeJuridique);        
            st.setString(3, Droit); 
            st.setString(4, Capital); 
            st.setString(5, RC);
            st.setString(6, Secteur); 
            st.setString(7, Typologie);
            st.setString(8, CoteBourceCasa); 
            st.setString(9, SNI);
            st.setString(10,code);
            st.executeUpdate();
            // MÉTHODE DE MODIFICATION  DES REPRESENTANTS
            Collection associMOD = (Collection) getWorkflowInstance().getLinkedResources("TableauDirigeants_MOD"); 
            if (associMOD.size() != 0) {
                UpdateRepresentants(code);
            }
            // MÉTHODE DE MODIFICATION  DES ACTIONNAIRES
            Collection associMOD2 = (Collection) getWorkflowInstance().getLinkedResources("StructureActionnariat_MOD"); 
            if (associMOD2.size() != 0) {
                UpdateActionnaires(code);
            }
            // MÉTHODE DE MODIFICATION DES DONNÉES CHIFFRÉES
            Collection associMOD3 = (Collection) getWorkflowInstance().getLinkedResources("DonneesChiffrees_MOD");
            if (associMOD3.size() != 0) {
                UpdateDonnesChiffrees(code);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE MODIFIER CLIENT : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // MODIFICATION DES DONNÉES CHIFFRÉES
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void UpdateDonnesChiffrees(String Client)
    {
        cnx=null;
        st=null;
        try {
            String EBE = "";
            String REX = "";
            String RN = "";
            String FP = "";
            String TB = "";
            String DN = "";
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("DonneesChiffrees_MOD");
            if (associ.size() != 0) {
                // SUPPRIMER LES VALEURS EXISTANT
                String QR = "DELETE FROM Donnees_Chiffrees WHERE Code_Client=?";
                st = cnx.prepareStatement(QR);
                st.setString(1,Client);
                st.executeUpdate();
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    String YEAR =(String) asso.getValue("DC_Tab_Annees_MOD");
                    String CA =(String) asso.getValue("DC_Tab_ChiffreAffaires_MOD");
                    if (asso.getValue("DC_Tab_EBE_MOD")!= null ) {
                        EBE =(String) asso.getValue("DC_Tab_EBE_MOD");
                    }
                    if (asso.getValue("DC_Tab_REX_MOD")!= null ) {
                        REX =(String) asso.getValue("DC_Tab_REX_MOD");
                    }
                    if (asso.getValue("DC_Tab_RN_MOD")!= null ) {
                        RN =(String) asso.getValue("DC_Tab_RN_MOD");
                    }
                    if (asso.getValue("DC_Tab_FP_MOD")!= null ) {
                        FP =(String) asso.getValue("DC_Tab_FP_MOD");
                    }
                    if (asso.getValue("DC_Tab_TotalBilan_MOD")!= null ) {
                        TB =(String) asso.getValue("DC_Tab_TotalBilan_MOD");
                    }
                    if (asso.getValue("DC_Tab_DN_MOD")!= null ) {
                        DN =(String) asso.getValue("DC_Tab_DN_MOD");
                    }
                    String Query ="INSERT INTO Donnees_Chiffrees(DC_Annee,DC_CA,DC_EBE,DC_REX,DC_RN,DC_FP,DC_TB,DC_DN,Code_Client) VALUES (?,?,?,?,?,?,?,?,?)";
                    st = cnx.prepareStatement(Query);
                    st.setString(1, YEAR); 
                    st.setString(2, CA);
                    st.setString(3, EBE);
                    st.setString(4, REX);
                    st.setString(5, RN);
                    st.setString(6, FP);
                    st.setString(7, TB);
                    st.setString(8, DN);      
                    st.setString(9, Client); 
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE UpdateDonnesChiffrees : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // MODIFICATION DES REPRESENTANTS
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void UpdateRepresentants(String Client)
    {
        cnx=null;
        st=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("TableauDirigeants_MOD");

            if (associ.size() != 0) {
                // SUPPRIMER LES VALEURS EXISTANT
                String UpRepresentants = "DELETE FROM Representant WHERE Code_Client=?";
                st = cnx.prepareStatement(UpRepresentants);
                st.setString(1,Client);
                st.executeUpdate();

                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    // ATTRIBUTS DES REPRESENTANTS  ===========================
                    String NomPrenom =(String)asso.getValue("NomPrenom_MOD");
                    String Fonction =(String)asso.getValue("Fonction_MOD");
                    String ID =(String)asso.getValue("IdentiteMandant_MOD");
                    // String Query ="UPDATE Representant SET Re_NomPrenom =?,Re_Fonction =?,Re_Identite =? WHERE Code_Client =?";
                    String Query ="INSERT INTO Representant(Re_NomPrenom,Re_Fonction,Re_Identite,Code_Client)" + 
                            "  VALUES (?,?,?,?)";
                    st.clearParameters();
                    st = cnx.prepareStatement(Query);
                    st.setString(1, NomPrenom);      
                    st.setString(2, Fonction);       
                    st.setString(3, ID);
                    st.setString(4, Client); 
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE SaveRepresentants : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // MODIFICATION DES ACTIONNAIRES
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    public void UpdateActionnaires(String Client)
    {
        cnx=null;
        st=null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("StructureActionnariat_MOD");

            if (associ.size() != 0) {
                // SUPPRIMER LES VALEURS EXISTANT
                String UpQuery = "DELETE FROM Actionnaires WHERE Code_Client=?";
                st = cnx.prepareStatement(UpQuery);
                st.setString(1,Client);
                st.executeUpdate();

                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    // ATTRIBUT DES ACTIONNAIRES ==============================
                    String Action =(String)asso.getValue("Action_MOD");
                    float Pourcentage =(float)asso.getValue("Pourcentage_MOD");
                    //String Query ="UPDATE Actionnaires SET Ac_Action =?,Ac_Pourcentage =? WHERE Code_Client =?";
                    String Query ="INSERT INTO Actionnaires (Ac_Action,Ac_Pourcentage,Code_Client)" + 
                            "  VALUES (?,?,?)";
                    st.clearParameters();
                    st = cnx.prepareStatement(Query);
                    st.setString(1, Action);      
                    st.setFloat(2, Pourcentage);       
                    st.setString(3, Client); 
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE SaveActionnaires : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // EXISTANCE DES CLIENT DANS LA BASE DE DONNÉES 
    // -------------------------------------------------------------------------------------
    public boolean IsExist(String CLIENT){
        cnx=null;
        st=null;
        rs=null;
        boolean flag = false;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Query ="select count(Cl_NomClient) from Clients where Cl_NomClient=?";
            st =cnx.prepareStatement(Query);
            st.setString(1, CLIENT);
            rs = st.executeQuery();
            rs.next(); // set the pointer to the first row
            if (rs.getInt(1)==0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - PROBLÈME DANS LA MÉTHODE ISEXIST CLIENT :" + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return flag;
    }
    // -------------------------------------------------------------------------------------
    // CONFIRMATION DE MISE A JOUS
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("serial")
    public boolean onAfterSubmit(IAction action) {
        if(action.getName().equals("Valider") || action.getName().equals("CL_ForcerValidation") ){
            try{
                if (((String)getWorkflowInstance().getValue("Actions"))!=null || ((String)getWorkflowInstance().getValue("Actions"))!="")
                {
                    Action = (String) getWorkflowInstance().getValue("Actions");
                    if (Action.equalsIgnoreCase("ADD")){
                        NouveauClient();  // MÉTHODE POUR AJOUTER UN CLIENT
                    }else if (Action.equalsIgnoreCase("MOD")){
                        ModifierClient(); // MÉTHODE POUR MODIFIER UN CLIENT
                    }else if (Action.equalsIgnoreCase("DEL")){
                        // BOITE DE DIALOGE : CONFIRMATION DE LA SUPPRESSION CLIENT
                        getResourceController().confirm("Voulez vous supprimer ce client ?", new ConfirmBoxListener() {
                            @Override
                            public void onOk(ActionEvent arg0) {
                                SupprimerClient(); // MÉTHODE POUR SUPPRIMER UN CLIENT
                            }
                            @Override
                            public void onCancel(ActionEvent arg0) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }
                }else{
                    getResourceController().alert("Veuillez choisir votre action");
                    return false;
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                log.error("CS LOG : ERREUR DANS LA METHODE ONAFTERSUBMIT : " + e.getClass());
            }
        }
        return super.onAfterSubmit(action);
    }
    // -------------------------------------------------------------------------------------
    // SUPPRIMER UN CLIENT
    // -------------------------------------------------------------------------------------
    public void SupprimerClient()
    {
        cnx=null;
        st=null;
        try{
            // DÉFINITION DE CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Code_Client_rech =(String) getWorkflowInstance().getValue("CodeClient_Rech");
            String Query = "DELETE FROM Clients WHERE Cl_CodeClient=?";
            st = cnx.prepareStatement(Query);
            st.setString(1,Code_Client_rech);
            st.executeUpdate();
            // =================
            st.clearParameters();
            String Query1 = "DELETE FROM Representant WHERE Code_Client=?";
            st = cnx.prepareStatement(Query1);
            st.setString(1,Code_Client_rech);
            st.executeUpdate();
            // =================
            st.clearParameters();
            String Query2 = "DELETE FROM Actionnaires WHERE Code_Client=?";
            st = cnx.prepareStatement(Query2);
            st.setString(1,Code_Client_rech);
            st.executeUpdate();
            // =================
            st.clearParameters();
            String Query3 = "DELETE FROM Donnees_Chiffrees WHERE Code_Client=?";
            st = cnx.prepareStatement(Query3);
            st.setString(1,Code_Client_rech);
            st.executeUpdate();
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE SUPPRIMERCLIENT : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // RECHERCHER UN CLIENT EXISTANT
    // -------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void RechecheClient()
    {
        cnx=null;
        st=null;
        rs=null;
        try{
            if (((String)getWorkflowInstance().getValue("CodeClient_Mod"))!=null || ((String)getWorkflowInstance().getValue("CodeClient_Mod"))!="")
            {
                code = (String) getWorkflowInstance().getValue("CodeClient_Mod");
                // DÉFINITION DE CONNEXION
                cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                siz = code.length(); // LA LENGUEUR DU CHAMPS CODE CLIENT
                if (siz!=0)
                {
                    String Query ="SELECT Cl_CodeClient,Cl_NomClient,Cl_FormeJuridique,Cl_Droit," + 
                            " Cl_Capital,Cl_RC,Cl_Secteur,Cl_Typologie,Cl_Bource,Cl_SNI" + 
                            " FROM Clients WHERE Cl_CodeClient =?";
                    st = cnx.prepareStatement(Query);
                    st.setString(1,code);
                    rs = st.executeQuery();
                    while(rs.next())
                    {
                        count = rs.getFetchSize(); // LA TAILLE DES VALEUR RETOURNER 
                        if (count!=0)
                        {
                            // RÉCUPÉRER LES DONNÉES DE CLIENT RECHERCHER
                            getWorkflowInstance().setValue("NomClient_Mod", rs.getString("Cl_NomClient"));
                            getWorkflowInstance().setValue("FormeJuridiqueSoc_MOD", rs.getString("Cl_FormeJuridique"));
                            getWorkflowInstance().setValue("DroitSociete_MOD", rs.getString("Cl_Droit"));
                            getWorkflowInstance().setValue("NumRC_MOD", rs.getString("Cl_RC"));
                            getWorkflowInstance().setValue("SecteurActivite_MOD", rs.getString("Cl_Secteur"));
                            getWorkflowInstance().setValue("TypologieEntreprise_MOD", rs.getString("Cl_Typologie"));
                            getWorkflowInstance().setValue("CoteBourseCasa_MOD", rs.getString("Cl_Bource"));
                            getWorkflowInstance().setValue("SNI_MOD", rs.getString("Cl_SNI"));
                            getWorkflowInstance().setValue("CapitalSocial_MOD", rs.getString("Cl_Capital"));
                        }
                    }
                    // =============================================================================================
                    // RÉNITIALISER LA VUE "STRUCTURE ACTIONNARIAT"
                    List<ILinkedResource> lst2 = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("StructureActionnariat_MOD");
                    getWorkflowInstance().deleteLinkedResources(lst2);
                    String Query1 ="SELECT Ac_Action,Ac_Pourcentage FROM Actionnaires WHERE Code_Client=?";
                    st.clearParameters();
                    st = cnx.prepareStatement(Query1);
                    st.setString(1,code);
                    rs = st.executeQuery();
                    while(rs.next())
                    {
                        // CRÉATION D'UNE LIGNE 
                        ILinkedResource LR = getWorkflowInstance().createLinkedResource("StructureActionnariat_MOD");
                        LR.setValue("Action_MOD", rs.getString("Ac_Action"));
                        LR.setValue("Pourcentage_MOD", rs.getFloat("Ac_Pourcentage")); 
                        getWorkflowInstance().addLinkedResource(LR);
                    }
                    // =============================================================================================
                    // RÉNITIALISER LA VUE "TABLEAU DIRIGEANTS"
                    List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("TableauDirigeants_MOD");
                    getWorkflowInstance().deleteLinkedResources(lst);
                    String Query2 ="SELECT Re_NomPrenom,Re_Fonction,Re_Identite FROM Representant WHERE Code_Client=?";
                    st.clearParameters();
                    st = cnx.prepareStatement(Query2);
                    st.setString(1,code);
                    rs = st.executeQuery();
                    while(rs.next())
                    {
                        // CRÉATION D'UNE LIGNE 
                        ILinkedResource LRS = getWorkflowInstance().createLinkedResource("TableauDirigeants_MOD");
                        LRS.setValue("NomPrenom_MOD", rs.getString("Re_NomPrenom"));
                        LRS.setValue("Fonction_MOD", rs.getString("Re_Fonction")); 
                        LRS.setValue("IdentiteMandant_MOD", rs.getString("Re_Identite")); 
                        getWorkflowInstance().addLinkedResource(LRS);
                    }
                    // =============================================================================================
                    // RÉNITIALISER LA VUE "TABLEAU DONNÉES CHIFFRÉES"
                    List<ILinkedResource> TabDC = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("DonneesChiffrees_MOD");
                    getWorkflowInstance().deleteLinkedResources(TabDC);
                    String Query3 ="SELECT DC_Annee,DC_CA,DC_EBE,DC_REX,DC_RN,DC_FP,DC_TB,DC_DN FROM Donnees_Chiffrees WHERE Code_Client=?";
                    st.clearParameters();
                    st = cnx.prepareStatement(Query3);
                    st.setString(1,code);
                    rs = st.executeQuery();
                    while(rs.next())
                    {
                        // CRÉATION D'UNE LIGNE
                        ILinkedResource LRDC = getWorkflowInstance().createLinkedResource("DonneesChiffrees_MOD");
                        LRDC.setValue("DC_Tab_Annees_MOD", rs.getString("DC_Annee"));
                        LRDC.setValue("DC_Tab_ChiffreAffaires_MOD", rs.getString("DC_CA")); 
                        LRDC.setValue("DC_Tab_DN_MOD", rs.getString("DC_DN")); 
                        LRDC.setValue("DC_Tab_EBE_MOD", rs.getString("DC_EBE"));
                        LRDC.setValue("DC_Tab_FP_MOD", rs.getString("DC_FP")); 
                        LRDC.setValue("DC_Tab_REX_MOD", rs.getString("DC_REX"));
                        LRDC.setValue("DC_Tab_RN_MOD", rs.getString("DC_RN"));
                        LRDC.setValue("DC_Tab_TotalBilan_MOD", rs.getString("DC_TB"));
                        getWorkflowInstance().addLinkedResource(LRDC);
                    }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.error("CS LOG : ERREUR DANS LA METHODE RECHERCHECLIENT : " + e.getClass());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // -------------------------------------------------------------------------------------
    // ON BEFORE SUBMIT
    // -------------------------------------------------------------------------------------
    @Override
    public boolean onBeforeSubmit(IAction action) {
        boolean act = true;
        if (getWorkflowInstance().getValue("Actions")!=null)
        {
            Action = (String) getWorkflowInstance().getValue("Actions");
            if (Action.equals("ADD")){
                if(getWorkflowInstance().getValue("NomClient")!=null) {
                    String Nom_Client = (String) getWorkflowInstance().getValue("NomClient");
                    if(IsExist(Nom_Client)==false) {
                        act = false;
                        getResourceController().alert("Le client "+Nom_Client+" existe");
                    }else {
                        if (getWorkflowInstance().getValue("SA_Total")!=null) {
                            if (((float) getWorkflowInstance().getValue("SA_Total"))==100)
                            {
                                act = true;
                            }else if (((float) getWorkflowInstance().getValue("SA_Total"))>100){
                                act = false;
                                getResourceController().alert("LE TOTAL DES ACTIONNARIATS NE DOIT PAS DÉPASSER 100%");
                            }else {
                                act = false;
                                getResourceController().alert("LE TOTAL DES ACTIONNARIATS EST INFÉRIEUR À 100%");
                            }   
                        }
                    }  
                }
            }else if (Action.equals("MOD")){
                if(getWorkflowInstance().getValue("CodeClient_Mod")!=null) {
                    String Nom_Client = (String) getWorkflowInstance().getValue("CodeClient_Mod");
                    if(IsExist(Nom_Client)==false) {
                        act = false;
                        getResourceController().alert("Le client "+Nom_Client+" existe");
                    }else {
                        if (getWorkflowInstance().getValue("Total_MOD")!= null) {
                            if (((float) getWorkflowInstance().getValue("Total_MOD"))==100)
                            {
                                act = true;

                            }else if (((float) getWorkflowInstance().getValue("Total_MOD"))>100){
                                act = false;
                                getResourceController().alert("LE TOTAL DES ACTIONS NE DOIT PAS DÉPASSER 100%");
                            }else {
                                act = false;
                                getResourceController().alert("LE TOTAL DES ACTIONS EST INFÉRIEUR À 100%");
                            }   
                        }
                    }
                }
            }
        }
        return act;
    }
}
