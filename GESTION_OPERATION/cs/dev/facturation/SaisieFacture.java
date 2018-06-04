package cs.dev.facturation;
import java.text.SimpleDateFormat;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
public class SaisieFacture extends ConnexionBDD{

    /**
     * Developped on 13/01/2017
     * r.sabri
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(SaisieFacture.class);
    int count =0;
    //boolean ok = false;
    // ----------------------------------------------------------------------------------------------------
    //EVENEMENT D'ENVOIE DE FORMULAIRE
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onBeforeSubmit(IAction action) {
        boolean act = true;
        if (action.getName().equals("PF_PourReglement"))
        {
            if((String) getWorkflowInstance().getValue("PF_NFacture")!= null ||(String) getWorkflowInstance().getValue("PF_NFacture")!="")
            {
                String Num_fact = (String) getWorkflowInstance().getValue("PF_NFacture");
                if(IsExist(Num_fact)==false) {
                    act = false;
                    getResourceController().alert("Le numéro de facture "+Num_fact+" existe");
                }else {
                    act = true;
                }
            }
        }
        return act;
    }
    // -------------------------------------------------------------------------------------------------------
    // ON PROPERTY CHANGED
    // --------------------------------------------------------------------------------------------------------
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("PF_NFacture"))
        {
            if((String) getWorkflowInstance().getValue("PF_NFacture")!= null ||(String) getWorkflowInstance().getValue("PF_NFacture")!="")
            {
                String Num_fact = (String) getWorkflowInstance().getValue("PF_NFacture");
                if(IsExist(Num_fact)==false) {
                    getResourceController().alert("Le numéro de facture "+Num_fact+" existe");
                }
            }
        }
        super.onPropertyChanged(property);
    }
    // --------------------------------------------------------------------------------------------------------
    // L'EXISTANCE D'UN NUMÉRO DE FACTURE
    // --------------------------------------------------------------------------------------------------------
    public boolean IsExist(String N_facture){
        cnx=null;
        st=null;
        rs=null;
        boolean flag = false;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Query ="select COUNT(Num_Fact) from Factures where Num_Fact=?";
            st =cnx.prepareStatement(Query);
            st.setString(1, N_facture);
            rs = st.executeQuery();
            rs.next(); // set the pointer to the first row
            if (rs.getInt(1)==0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CS LOG - PROBLEME DANS LA METHODE IS EXIST N_FACTURE :" + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return flag;
    }
    // ---------------------------------------------------------------------------------------------------------
    // IS ON CHNAGE SUBSCRIPTION ON
    // ----------------------------------------------------------------------------------------------------------
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if(property.getName().equals("PF_NFacture"))
        {
            return true;
        }
        return false;
    }
    // ----------------------------------------------------------------------------------------------------
    // AFFICHAGE DE LA VUE DE L'HISTORIQUE DES FACTURES EN CAS DE PAIEMENT 
    // ----------------------------------------------------------------------------------------------------
    public void HistoriqueFacture()
    {
        cnx=null;
        st=null;
        rs=null;
        try{
            // DÉFINITION DE LA CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Dossier = (String) getWorkflowInstance().getValue("PF_Dossier");
            String query1 ="SELECT count(*) FROM Factures where Dossier=?";
            st = cnx.prepareStatement(query1);
            st.setString(1, Dossier);
            rs = st.executeQuery();
            while(rs.next())
                count = rs.getInt(1);
            // AFFICHAGE DE LA VU DES HISTORIQUE DES FACTURES PAYANTS 
            if (count>0){
                // FRAGMENT D'AFFICHAGE POUR UN DOSSIER => ICI LA VU S'AFFICHE 
                getResourceController().showBodyBlock("FragHistoriqueFactures", true);
                @SuppressWarnings("unchecked")
                List<ILinkedResource> TabHistoriqueFacture = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("PF_HistoriqueFactures");
                if(TabHistoriqueFacture.size()==0)
                {
                    String query2 = "SELECT Num_Fact,Montant_Fact,Date_Fact,Date_Echeance,Date_Remise FROM Factures where Dossier=?";
                    st = cnx.prepareStatement(query2);
                    st.setString(1, Dossier);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        // CRÉATION D'UNE LIGNE 
                        ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "PF_HistoriqueFactures" );
                        // POSITIONNEMENT DE QUELQUES VALEURS 
                        linkedResource.setValue("HF_Tab_NumFacture", rs.getString("Num_Fact"));
                        linkedResource.setValue("HF_Tab_Montant",rs.getDouble("Montant_Fact"));  
                        linkedResource.setValue("HF_Tab_DateFacture",rs.getString("Date_Fact"));
                        linkedResource.setValue("HF_Tab_DateEcheance",rs.getString("Date_Echeance")); 
                        linkedResource.setValue("HF_Tab_DateRemise",rs.getString("Date_Remise"));
                        // AJOUT DE LA LIGNE AU TABLEAU
                        getWorkflowInstance().addLinkedResource( linkedResource );
                    }   
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
    //-----------------------------------------------------------------------------------------------------
    // EVENEMENT DE CHARGEMENT DE FORMULAIRE
    //-----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterLoad() {
        // HISTORIQUE DES FACTURES
        HistoriqueFacture();
        return super.onAfterLoad();
    }
}
