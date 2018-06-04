package cs.dev.facturation;
import java.text.SimpleDateFormat;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
public class Reglements extends ConnexionBDD{

    /**
     * Developped on 13/01/2017
     * r.sabri
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(Reglements.class);
    // ----------------------------------------------------------------------------------------------------
    // ÉVENEMENT D'ENVOIE DE FORMULAIRE
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        if (action.getName().equals("PF_CloturerReglement") || action.getName().equals("PF_CloturerPrec") || action.getName().equals("PF_CloturerDemeure"))
        {
            SaveFacture();        // MÉTHODE D'INSERTION DES FACTURES
            PaiementFactures();   // MÉTHODE DE PAIEMENT DES FACTURES  
        }
        
        return super.onAfterSubmit(action);
    }
    // ----------------------------------------------------------------------------------------------------
    // MÉTHODE D'INSERTION DES FACTURE 
    // ----------------------------------------------------------------------------------------------------
    public void SaveFacture(){
        cnx=null;
        st=null;
        try{
            // DÉFINITION DE LA CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String NumFact =(String) getWorkflowInstance().getValue("PF_NFacture");
            String Dossier =(String) getWorkflowInstance().getValue("PF_Dossier");
            String Client =(String) getWorkflowInstance().getValue("PF_Client");
            
            Object MONT = (Object) getWorkflowInstance().getValue("PF_MontantStr");  
            Double MontantFact = Double.parseDouble(MONT.toString().replaceAll(" ", ""));
            
            String DateFact =new SimpleDateFormat("dd-MM-yyyy").format( getWorkflowInstance().getValue("PF_DateFacture"));
            String DateEcheance =new SimpleDateFormat("dd-MM-yyyy").format( getWorkflowInstance().getValue("PF_Echeance"));
            String DateRemise =new SimpleDateFormat("dd-MM-yyyy").format( getWorkflowInstance().getValue("PF_DateRemiseClient"));

            String Query = "INSERT INTO Factures(Num_Fact,Client,Montant_Fact,Date_Fact,Date_Echeance,Date_Remise,Dossier)VALUES (?,?,?,?,?,?,?)";
            st = cnx.prepareStatement(Query);
            st.setString(1,NumFact);
            st.setString(2,Client); 
            st.setDouble(3,MontantFact);
            st.setString(4,DateFact); 
            st.setString(5,DateEcheance);
            st.setString(6,DateRemise); 
            st.setString(7,Dossier); 
            st.executeUpdate();
        }catch (Exception e) {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode savefacture: " + e.getClass() + " - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    // MÉTHODE DE PAIEMENT FACTURES
    // ----------------------------------------------------------------------------------------------------
    public void PaiementFactures()
    {
        cnx=null;
        st=null;
        try {
            // DÉFINITION DE LA CONNEXION
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Dossier =(String) getWorkflowInstance().getValue("PF_Dossier");
            // INDICATEUR 
            String Paiement =(String) getWorkflowInstance().getValue("PF_DernierPaiement");
            if (Paiement.equalsIgnoreCase("Oui"))
            {
                Paiement="Clôturer";
            }else
            {
                Paiement="En cours";
            }
            String Query = "Update Dossiers set EtatPaiement= ? where RefDossier = ?";
            st = cnx.prepareStatement(Query);
            st.setString(1,Paiement);
            st.setString(2,Dossier); 
            st.executeUpdate();
        }catch (Exception e) {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            e.printStackTrace();
            log.error("Erreur dans la methode Paiement Facture: " + e.getClass() + " - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
}
