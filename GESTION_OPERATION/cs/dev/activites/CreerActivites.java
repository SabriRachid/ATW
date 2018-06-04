package cs.dev.activites;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.utils.Logger;
import dao.ConnexionBDD;
/**
 *******************
 * @author R.SABRi
 * @Date_26.12.2017
 *******************
 */
public class CreerActivites extends ConnexionBDD {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log =  Logger.getLogger(CreerActivites.class);
    //*************************************************************************
    //
    //*************************************************************************
    @Override
    public boolean onAfterLoad() {
        getResourceController().getButtonContainer(1).setHidden(true);
        return super.onAfterLoad();
    }
    //*************************************************************************
    //
    //*************************************************************************
    @Override
    public boolean onAfterSubmit(IAction action) {
        if(action.getName().equals("Envoyer")){
            cnx=null;
            st=null;
            try{
                // DÈFINITION DE CONNEXION =========================
                cnx=getConnectionVDoc("Ref_Attijari").getConnection();
                String SecteurActivite =(String) getWorkflowInstance().getValue("ACT_SecteurActivite");
                String Query ="INSERT INTO Secteurs_Activite(Libelle_Secteur) VALUES(?)";
                st = cnx.prepareStatement(Query);
                st.setString(1, SecteurActivite); 
                st.executeUpdate();
            }catch(Exception e)
            {
                String message = e.getMessage();
                if (message == null)
                {
                    message = "";
                }
                e.printStackTrace();
                log.error("CS LOG : ERREUR DANS LA METHODE NouveauSecteurActivite : " + e.getClass()+ " _ " + message);
            }finally {
                // LIBÈRER RESSOURCES DE LA MÈMOIRE.
                ConnexionBDD.close(cnx, st);
            }
        }
        return super.onAfterSubmit(action);
    }
}
