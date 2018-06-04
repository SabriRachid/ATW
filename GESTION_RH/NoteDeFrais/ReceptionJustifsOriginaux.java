package NoteDeFrais;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import com.serviceRH.EncryptionFile;

import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class ReceptionJustifsOriginaux extends ConnexionBDD{

    /* ========================================================== *
     * @Plateform :VDoc 15.0
     * @author r.sabri
     * @Creation_date :07/02/2017 15h17 => update on 30.06.2017
     * @Modifier le 07/06/2017
     * ========================================================== */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ReceptionJustifsOriginaux.class);
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public boolean onAfterLoad() {
        String Ref_VDoc =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
        getNoteTableJustifOriginaux(Ref_VDoc);
        getWorkflowInstance().setValue("NDF_Test_Statut_Justifs", null);
        return super.onAfterLoad();
    }
    /*
     * =================================================================================================================
     * GETNOTETABLEJUSTIFORIGINAUX
     * =================================================================================================================
     */
    public void getNoteTableJustifOriginaux(String Reference)
    {
        cnx =null;
        st = null; 
        rs = null;
        try {
            List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("NDF_TabValidationJutifsOriginaux");
            getWorkflowInstance().deleteLinkedResources(lst);
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            String query = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF" + 
                    " FROM NotesFrais" + 
                    " WHERE ((StatutPV='Validé' and  (Statut not like 'Validé' or Statut is NULL) )or(StatutPV='Rejeté' and  Statut = 'Validé')) and Recept_NF='Non'  and Ref_NF=?";
            st = cnx.prepareStatement(query);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                // CRÉATION D'UNE LIGNE 
                ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "NDF_TabValidationJutifsOriginaux" );
                // POSITIONNEMENT DE QUELQUES VALEURS 
                linkedResource.setValue("NDF_Tab_DesignationRJO", rs.getString(1));
                linkedResource.setValue("NDF_Tab_QuantiteRJO",rs.getFloat(2));
                linkedResource.setValue("NDF_Tab_MontantRJO",rs.getFloat(3));  
                linkedResource.setValue("NDF_Tab_TotalRJO",rs.getFloat(4)); 
                linkedResource.setValue("NDF_Tab_DateJustifRJO",rs.getDate(5)); 
                String PJ = rs.getString(6);
                if (PJ==null) {
                    linkedResource.setValue("NDF_Tab_PieceJustificativeRJO", null);
                }else {
                    EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));

                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//"+rs.getString(6));
                    IAttachment att = getWorkflowModule().addAttachment(linkedResource, "NDF_Tab_PieceJustificativeRJO", doc);
                    EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));


                    List<IAttachment> PieceJustif = ((List<IAttachment>) linkedResource.getValue("NDF_Tab_PieceJustificativeRJO"));
                    PieceJustif.clear();
                    PieceJustif.add(att); 
                    linkedResource.setValue("NDF_Tab_PieceJustificativeRJO", PieceJustif);
                }
                linkedResource.setValue("ID_NDFRO", rs.getFloat(7));
                // AJOUT DE LA LIGNE AU TABLEAU
                getWorkflowInstance().addLinkedResource( linkedResource ); 
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in getNoteTableJustifOriginaux() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    /*
     * =================================================================================================================
     * ONAFTERSUBMIT
     * =================================================================================================================
     */
    @Override
    public boolean onAfterSubmit(IAction action) {
        if (action.getName().equals("NDF_ValiderRHRJO")) {
            String REF_VDOC = (String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
            UpdateJustifOriginaux(REF_VDOC);
        }
        return super.onAfterSubmit(action);
    }
    /*
     * =================================================================================================================
     * UPDATEJUSTIFORIGINAUX
     * =================================================================================================================
     */
    public void UpdateJustifOriginaux(String REF)
    {
        cnx = null;
        st =null;
        rs = null;
        try {
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationJutifsOriginaux");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    float ID = (float) asso.getValue("ID_NDFRO");
                    String ReceptJO = (String) asso.getValue("NDF_Tab_Receptionne");
                    cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                    String Query = "UPDATE NotesFrais SET Recept_NF=?,StatutPV='Validé' WHERE ID_NF=? AND Ref_NF=?";
                    st = cnx.prepareStatement(Query);
                    st.setString(1,ReceptJO);
                    st.setFloat(2,ID); 
                    st.setString(3,REF);
                    st.executeUpdate(); 
                }
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in UpdateJustifOriginaux() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /*
     * =================================================================================================================
     * ONBEFORESUBMIT
     * =================================================================================================================
     */
    @Override
    public boolean onBeforeSubmit(IAction action) {
        boolean passer = true;
        if (action.getName().equals("NDF_ValiderRHRJO"))
        {
            try {
                int count=0;
                // RECUPERER LES LIGNES DE LE TABLEAU DES NOTES DE FRAIS
                Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationJutifsOriginaux");
                if (associ.size() != 0) {
                    for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                        // CRÉATION D'UNE LIGNE 
                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        // VÉRIFIER SI LES NOTE DE FRAIS SONT VALIDÉ 
                        if(asso.getValue("NDF_Tab_Receptionne")==null){
                            count ++;
                        }
                    }
                    if (count>0) {
                        passer = false;
                        getResourceController().alert("Une ou plusieurs notes de frais ne sont pas réceptionnées.");
                    } 
                }
            }catch(Exception e)
            {
                e.getStackTrace();
                log.info("Error in onBeforeSubmit() method : " + e.getClass() + " - " + e.getMessage());
            }
        }
        return passer;
    }
    /*
     * =================================================================================================================
     * ONPROPERTYCHANGED
     * =================================================================================================================
     */
    @Override
    public void onPropertyChanged(IProperty property) {
        int count=0;
        if (property.getName().equals("NDF_TabValidationJutifsOriginaux"))
        {
            List NF = (List) getWorkflowInstance().getLinkedResources("NDF_TabValidationJutifsOriginaux");
            if (NF.size() != 0)
                count =0;
            for (Iterator IT = NF.iterator(); IT.hasNext();)
            {
                ILinkedResource ALLNF = (ILinkedResource) IT.next();
                String Statut = (String) ALLNF.getValue("NDF_Tab_Receptionne");
                if("Non".equals(Statut)){
                    count ++;
                }
            }
            if (count>0)
            {
                getWorkflowInstance().setValue("NDF_Test_Statut_Justifs", "Non");
                getResourceController().getButtonContainer(2).setHidden(true);
                getResourceController().alert("Veuillez enregistrer le document");
                getResourceController().alert("Le système envoie une notification au demandeur durant 3 jours avant la clôture de la demande");
            }else {
                getWorkflowInstance().setValue("NDF_Test_Statut_Justifs", "Oui");
                getResourceController().getButtonContainer(2).setHidden(false);
            }

        }
        super.onPropertyChanged(property);
    }
    /*
     * =================================================================================================================
     * ISONCHANGESUBSCRIPTIONON
     * =================================================================================================================
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if (property.getName().equals("NDF_TabValidationJutifsOriginaux"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
}
