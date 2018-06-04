package NoteDeFrais;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.turbine.services.servlet.TurbineServlet;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.utils.Logger;
import com.serviceRH.EncryptionFile;

import beans.NotesFrais;
import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class Pre_ValidationRH extends ConnexionBDD{

    /* ========================================= *
     * @Plateform :VDoc 15.0
     * @author r.sabri
     * @Creation_date :07/02/2017 15h17
     * @Modifier par R.SABRI le 06/06/2017
     * ========================================= */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(Pre_ValidationRH.class);
    int count =0;
    int count2=0;
    /*
     * =================================================================================================================
     * ONPROPERTYCHANGED
     * =================================================================================================================
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("NDF_TabValidationRHNDF"))
        {
            count = 0;
            count2=0;
            List NF = (List) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF");
            if (NF.size() != 0)
                for (Iterator IT = NF.iterator(); IT.hasNext();)
                {
                    ILinkedResource ALLNF = (ILinkedResource) IT.next();
                    String Statut = (String) ALLNF.getValue("NDF_Tab_ValiderRH");
                    if("Refusé".equals(Statut)){
                        count ++;
                    }
                    if ("Validé".equals(Statut)) {
                        count2 ++;
                    }
                }
            if (count>=1)
            {
                // Si une des notes de frais réfusé  par le Directeur RH le demande retourne automatiquement vers le demandeur pour réctifier.
                getWorkflowInstance().setValue("NDF_Test_Statut", "deny"); 
            }else if (count2>=1){
                getWorkflowInstance().setValue("NDF_Test_Statut", "Valider");
            }else {
                getWorkflowInstance().setValue("NDF_Test_Statut", "Rejet");
            }
        }
        if (property.getName().equals("NDF_TabValidationRHNDF_Refuser"))
        {
            count = 0;
            count2=0;
            List NF = (List) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF_Refuser");
            if (NF.size() != 0)
                for (Iterator IT = NF.iterator(); IT.hasNext();)
                {
                    ILinkedResource ALLNF = (ILinkedResource) IT.next();
                    String Statut = (String) ALLNF.getValue("NDF_Tab_ValiderRH2");
                    if("Refusé".equals(Statut)){
                        count ++;
                    }
                    if ("Validé".equals(Statut)) {
                        count2 ++;
                    }
                }
            if (count>0) {
                // Si une des notes de frais réfusé  par le Directeur RH le demande retourne automatiquement vers le demandeur pour réctifier.
                getWorkflowInstance().setValue("NDF_Test_Statut_Refuse", "deny"); 
            }else if (count2>=1){
                getWorkflowInstance().setValue("NDF_Test_Statut_Refuse", "Valider");
            }else {
                getWorkflowInstance().setValue("NDF_Test_Statut_Refuse", "Rejet");
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
        if (property.getName().equals("NDF_TabValidationRHNDF")){
            return true;
        }
        if (property.getName().equals("NDF_TabValidationRHNDF_Refuser"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
    /*
     * =================================================================================================================
     * ONAFTERLOAD
     * =================================================================================================================
     */
    @Override
    public boolean onAfterLoad() {
        String Ref_VDoc =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
        // Clé de l'étape l'étape courante
        String workflowName=getWorkflowInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("NDF_ValidationRH"))        
        {
            // Initialise le statut NDF avant refuse
            getWorkflowInstance().setValue("NDF_Test_Statut", null);
            if(((Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF")).size()==0)
            {
                getNoteTable_Avant_Refuse(Ref_VDoc);  
            }
        }else {
            // Initialise le statut NDF après refuse
            getWorkflowInstance().setValue("NDF_Test_Statut_Refuse", null);
            getNoteTable_Apres_Refuse(Ref_VDoc);
        }
        return super.onAfterLoad();
    }
    /*
     * =================================================================================================================
     * GETNOTETABLE_AVANT_REFUSE
     * =================================================================================================================
     */
    public void getNoteTable_Avant_Refuse(String Reference)
    {
        cnx =null;
        st = null;
        rs = null;
        try {
            List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF");
            getWorkflowInstance().deleteLinkedResources(lst);
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            /*
             * String query = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF,Statut" + 
                    " FROM NotesFrais" + 
                    " WHERE Ref_NF=?";
             */
            String query = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF" + 
                    " FROM NotesFrais" + 
                    " WHERE Ref_NF=?";
            st = cnx.prepareStatement(query);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                // CRÉATION D'UNE LIGNE 
                ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "NDF_TabValidationRHNDF" );
                // POSITIONNEMENT DE QUELQUES VALEURS 
                linkedResource.setValue("NDF_Tab_DesignationRH", rs.getString(1));
                linkedResource.setValue("NDF_Tab_QuantiteRH",rs.getFloat(2));
                linkedResource.setValue("NDF_Tab_MontantRH",rs.getFloat(3));  
                linkedResource.setValue("NDF_Tab_TotalRH",rs.getFloat(4)); 
                linkedResource.setValue("NDF_Tab_DateJustifRH",rs.getDate(5)); 
                String PJ = rs.getString(6);
                if (PJ==null) {
                    linkedResource.setValue("NDF_Tab_PieceJustificativeRH", null);
                }else {
                	EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));
                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//"+rs.getString(6));
                    IAttachment att = getWorkflowModule().addAttachment(linkedResource, "NDF_Tab_PieceJustificativeRH", doc);
                    EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));
                    List<IAttachment> PieceJustif = ((List<IAttachment>) linkedResource.getValue("NDF_Tab_PieceJustificativeRH"));
                    PieceJustif.clear();
                    PieceJustif.add(att); 
                    linkedResource.setValue("NDF_Tab_PieceJustificativeRH", PieceJustif); 
                }
                linkedResource.setValue("NDF_ID_VNDF", rs.getFloat(7));
                //linkedResource.setValue("NDF_Tab_ValiderRH", rs.getString(8));
                // AJOUT DE LA LIGNE AU TABLEAU
                getWorkflowInstance().addLinkedResource( linkedResource ); 
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
    /*
     * =================================================================================================================
     * GETNOTETABLE_APRES_REFUSE
     * =================================================================================================================
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public void getNoteTable_Apres_Refuse(String Reference)
    {
        cnx =null;
        st = null;
        rs = null;
        try {
            List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF_Refuser");
            getWorkflowInstance().deleteLinkedResources(lst);
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            /*
             *  String query = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF,Statut" + 
                    " FROM NotesFrais WHERE Ref_NF=? and StatutPV='Refusé'";
             * */
            String query = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF" + 
                    " FROM NotesFrais WHERE Ref_NF=? and (StatutPV='Refusé' or Statut='Refusé')";
            // " FROM NotesFrais WHERE Ref_NF=? AND Flag=?";
            st = cnx.prepareStatement(query);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                // CRÉATION D'UNE LIGNE 
                ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "NDF_TabValidationRHNDF_Refuser" );
                // POSITIONNEMENT DE QUELQUES VALEURS 
                linkedResource.setValue("NDF_Tab_DesignationRH2", rs.getString(1));
                linkedResource.setValue("NDF_Tab_QuantiteRH2",rs.getFloat(2));
                linkedResource.setValue("NDF_Tab_MontantRH2",rs.getFloat(3));  
                linkedResource.setValue("NDF_Tab_TotalRH2",rs.getFloat(4)); 
                linkedResource.setValue("NDF_Tab_DateJustifRH2",rs.getDate(5)); 
                String PJ = rs.getString(6);
                if (PJ==null) {
                    linkedResource.setValue("NDF_Tab_PieceJustificativeRH2", null);
                }else {
                	EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));
                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//"+rs.getString(6));
                    IAttachment att = getWorkflowModule().addAttachment(linkedResource, "NDF_Tab_PieceJustificativeRH2", doc);
                    EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));
                    List<IAttachment> PieceJustif = ((List<IAttachment>) linkedResource.getValue("NDF_Tab_PieceJustificativeRH2"));
                    PieceJustif.clear();
                    PieceJustif.add(att); 
                    linkedResource.setValue("NDF_Tab_PieceJustificativeRH2", PieceJustif);
                }
                linkedResource.setValue("NDF_ID_VNDFR", rs.getFloat(7));
                //linkedResource.setValue("NDF_Tab_ValiderRH2", rs.getString(8));
                // AJOUT DE LA LIGNE AU TABLEAU
                getWorkflowInstance().addLinkedResource( linkedResource ); 
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in getNoteTable_Apres_Refuse() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
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
        // Clé de l'étape l'étape courante
        String workflowName=getWorkflowInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("NDF_ValidationRH"))        
        {
            if (action.getName().equals("NDF_ValiderRH"))
            {
                try {
                    int count=0;
                    // RECUPERER LES LIGNES DE LE TABLEAU DES NOTES DE FRAIS
                    Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF");
                    if (associ.size() != 0) {
                        for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                            // CRÉATION D'UNE LIGNE 
                            ILinkedResource asso = (ILinkedResource) iter1.next();
                            // VÉRIFIER SI LES NOTE DE FRAIS SONT VALIDÉ. 
                            if(asso.getValue("NDF_Tab_ValiderRH")==null){
                                count ++;
                            }
                        }
                        if (count>0) {
                            passer = false;
                            getResourceController().alert("Une ou plusieurs notes de frais ne sont pas validés.");
                        }
                    }
                }catch(Exception e)
                {
                    e.getStackTrace();
                }
            } 
        }else {
            if (action.getName().equals("Validate"))
            {
                try {
                    int count=0;
                    // RECUPERER LES LIGNES DE LE TABLEAU DES NOTES DE FRAIS
                    Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF_Refuser");
                    if (associ.size() != 0) {
                        for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                            // CRÉATION D'UNE LIGNE 
                            ILinkedResource asso = (ILinkedResource) iter1.next();
                            // VÉRIFIER SI LES NOTE DE FRAIS SONT VALIDÉ. 
                            if(asso.getValue("NDF_Tab_ValiderRH2")==null){
                                count ++;
                            }
                        }
                        if (count>0) {
                            passer = false;
                            getResourceController().alert("Une ou plusieurs notes de frais ne sont pas validés.");
                        }
                    }
                }catch(Exception e)
                {
                    e.getStackTrace();
                }
            }  
        }


        return passer;
    }
    /*
     * =================================================================================================================
     * ONAFTERSUBMIT
     * =================================================================================================================
     */
    /*
    @Override
    public boolean onAfterSubmit(IAction action) {
        if(action.getName().equals("Validate")) {
            cnx = null;
            st = null;
            try {
                Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationRHNDF_Refuser");
                if (associ.size() != 0) {
                    for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                        String Ref_VDoc =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
                        int ID_VNF =(int) asso.getValue("NDF_ID_VNDFR");
                        String DECISION =(String) asso.getValue("NDF_Tab_ValiderRH2");
                        String Query = "UPDATE NotesFrais SET Flag =?, Statut=? WHERE ID_NF=? AND Ref_NF=?";
                        st = cnx.prepareStatement(Query);
                        st.setString(1,"Ok");
                        st.setString(2,DECISION);
                        st.setFloat(3,ID_VNF); 
                        st.setString(4,Ref_VDoc);
                        st.executeUpdate();
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                log.info("Error in onAfterSubmit() method : " + e.getClass() + " - " + e.getMessage());
            }finally {
                // LIBÉRER RESSOURCES DE LA MÉMOIRE.
                ConnexionBDD.close(cnx, st);
            }
        }
        return super.onAfterSubmit(action);
    }
     */
}
