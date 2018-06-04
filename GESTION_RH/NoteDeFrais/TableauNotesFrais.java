package NoteDeFrais;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.core.document.fields.ICoreField;
import com.serviceRH.EncryptionFile;

import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class TableauNotesFrais extends ConnexionBDD{

    /* ========================================= *
     * @Plateform :VDoc 14.2.1
     * @author r.sabri
     * @Creation_date :22/05/2017 16h17
     * @Modifier le 07/06/2017
     * ========================================= */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(TableauNotesFrais.class);
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public void onPropertyChanged(IProperty property) {
        if(property.getName().equals("NDF_Tab_Designation"))
        {
            frais_kilometrique_before();
        }
        /*
        if(property.getName().equals("NDF_Tab_Designation2"))
        {
            frais_kilometrique_after();
        }
        */
        super.onPropertyChanged(property);
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    public void frais_kilometrique_before()
    {
        try { 
            getResourceController().setMandatory("NDF_Tab_PieceJustificative", true);
            getResourceController().setMandatory("NDF_Tab_DateJustif", true); 
            String D1 =(String) getWorkflowInstance().getValue("NDF_Tab_Designation");
            if (D1!= null)
            {
                if (D1.equals("FRAIS KILOMETRIQUES")) {
                    getResourceController().setEditable("NDF_Tab_Montant", false);
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif", false); 
                    //ResetField();
                    //@SI(  NDF_Tab_Designation ="FRAIS KILOMETRIQUES"; 1,5; null)
                    getWorkflowInstance().setValue("NDF_Tab_Montant", 1.5f);
                }else if(D1.equals("TAXI")){
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif", false);
                    //ResetField();
                }else if(D1.equals("AUTRES")) {
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif", false); 
                    //ResetField();
                }else {
                    getResourceController().setEditable("NDF_Tab_Montant", true);
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative", true);
                    getResourceController().setMandatory("NDF_Tab_DateJustif", true); 
                    //ResetField();
                }
            }else {
                getResourceController().setEditable("NDF_Tab_Montant", true);
                getResourceController().setMandatory("NDF_Tab_PieceJustificative", true);
                getResourceController().setMandatory("NDF_Tab_DateJustif", true); 
                getWorkflowInstance().setValue("NDF_Tab_DateJustif", "");
                ResetField();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    /*
    public void frais_kilometrique_after()
    {
        try { 
            getResourceController().setMandatory("NDF_Tab_PieceJustificative2", true);
            getResourceController().setMandatory("NDF_Tab_DateJustif2", true); 
            String D1 =(String) getWorkflowInstance().getValue("NDF_Tab_Designation2");
            if (D1!= null)
            {
                if (D1.equals("FRAIS KILOMETRIQUES")) {
                    getResourceController().setEditable("NDF_Tab_Montant2", false);
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", false); 
                    //ResetField();
                    //@SI(  NDF_Tab_Designation ="FRAIS KILOMETRIQUES"; 1,5; null)
                    getWorkflowInstance().setValue("NDF_Tab_Montant2", 1.5f);
                }else if(D1.equals("TAXI")){
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", false);
                    //ResetField();
                }else if(D1.equals("AUTRES")) {
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", false); 
                    //ResetField();
                }else {
                    getResourceController().setEditable("NDF_Tab_Montant2", true);
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", true);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", true); 
                    //ResetField();
                }
            }else {
                getResourceController().setEditable("NDF_Tab_Montant2", true);
                getResourceController().setMandatory("NDF_Tab_PieceJustificative2", true);
                getResourceController().setMandatory("NDF_Tab_DateJustif2", true); 
                //ResetField();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    */
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if(property.getName().equals("NDF_Tab_Designation"))
        {
            return true;
        }
        /*
        if(property.getName().equals("NDF_Tab_Designation2"))
        {
            return true;
        }
        */
        return super.isOnChangeSubscriptionOn(property);
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public boolean onAfterLoad() {
        // Etape courante
        String workflowName=getWorkflowInstance().getParentInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("NDF_SaisieNoteFrais"))        
        {
            frais_kilometrique_before();
        }else {
            String D2 =(String) getWorkflowInstance().getValue("NDF_Tab_Designation2");
            //if (D2!= null)
            //{
                if (D2.equals("FRAIS KILOMETRIQUES")) {
                    getResourceController().setEditable("NDF_Tab_Montant2", false);
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", false); 
                }else if(D2.equals("TAXI")){
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", false);
                }else if(D2.equals("AUTRES")) {
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", false);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", false); 
                }else {
                    getResourceController().setEditable("NDF_Tab_Montant2", true);
                    getResourceController().setMandatory("NDF_Tab_PieceJustificative2", true);
                    getResourceController().setMandatory("NDF_Tab_DateJustif2", true); 
                }
            //}
        }
        return super.onAfterLoad();
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    private void ResetField() {
        String workflowName=getWorkflowInstance().getParentInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("NDF_SaisieNoteFrais"))        
        {
            getWorkflowInstance().setValue("NDF_Tab_Montant", null); // NDF_Tab_Montant
            getWorkflowInstance().setValue("NDF_Tab_Quantite", null); // NDF_Tab_Quantite
            getWorkflowInstance().setValue("NDF_Tab_Total", null); // NDF_Tab_Total
            getWorkflowInstance().setValue("NDF_Tab_PieceJustificative",null); // NDF_Tab_PieceJustificative
            getWorkflowInstance().setValue("NDF_Tab_DateJustif", null); //NDF_Tab_DateJustif
            //getWorkflowInstance().setValue("NDF_Tab_Commentaire", null); // NDF_Tab_Commentaire
        }
        /*else {
            getWorkflowInstance().setValue("NDF_Tab_Montant2", null); // NDF_Tab_Montant
            getWorkflowInstance().setValue("NDF_Tab_Quantite2", null); // NDF_Tab_Quantite
            getWorkflowInstance().setValue("NDF_Tab_Total2", null); // NDF_Tab_Total
            getWorkflowInstance().setValue("NDF_Tab_PieceJustificative2",null); // NDF_Tab_PieceJustificative
            getWorkflowInstance().setValue("NDF_Tab_DateJustif2", null); //NDF_Tab_DateJustif
            getWorkflowInstance().setValue("NDF_Tab_Commentaire2", null); // NDF_Tab_Commentaire
        }
        */
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public boolean onBeforeSave() {
        // CLÉ DE L'ÉTAPE L'ÉTAPE COURANTE.
        String workflowName=getWorkflowInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("SAISIENOTEDEFARIS"))        
        {
            float ID = (float) getWorkflowInstance().getValue("NDF_ID");
            String REF_VDOC =(String) getWorkflowInstance().getParentInstance().getValue(IProperty.System.REFERENCE);
            String Designation =(String) getWorkflowInstance().getValue("NDF_Tab_Designation2");
            float QT_NF =(float) getWorkflowInstance().getValue("NDF_Tab_Quantite2");
            float MT_NF =(float) getWorkflowInstance().getValue("NDF_Tab_Montant2");
            java.sql.Date sqlDate = null;
            if (!(Designation.equals("FRAIS KILOMETRIQUES") || Designation.equals("TAXI") || Designation.equals("AUTRES")))
            {
                Date DateJ_NF =(Date) getWorkflowInstance().getValue("NDF_Tab_DateJustif2");
                java.util.Date utilDate = DateJ_NF;
                sqlDate = new java.sql.Date(utilDate.getTime());  
            }
            SetNoteDeFrais(REF_VDOC, Designation, ID, QT_NF, MT_NF, sqlDate);
        }
        return super.onBeforeSave();
    }
    /*
     * =================================================================================================================
     * SETNOTEDEFRAIS
     * =================================================================================================================
     */
    public void SetNoteDeFrais(String REF, String DEC, float id, float Qte, float Montant, Date DateJustif)
    {
        cnx = null;
        st = null;
        try {
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            String Query = "UPDATE NotesFrais SET Qte_NF=?,Mt_NF=?,DateJust_NF=?,PJ_NF=? WHERE ID_NF=? AND Ref_NF=?";
            st = cnx.prepareStatement(Query);
            st.setFloat(1,Qte);
            st.setFloat(2,Montant);
            st.setDate(3, (java.sql.Date)DateJustif);
            st.setString(4,Add_PJ_NDF(DEC, id, REF,"NDF_Tab_PieceJustificative2"));
            st.setFloat(5,id); 
            st.setString(6,REF);
            st.executeUpdate();
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    /*
     * =================================================================================================================
     * AJOUTER UN FICHIER PJ AU RÉPÉRTOIRE DE SORTIE
     * =================================================================================================================
     */
    @SuppressWarnings("unchecked")
    public String Add_PJ_NDF(String Libelle_NDF, float Id_NDF,String REF,String sysname) throws IOException
    {
        try{
            String serverTemporaryFileFullPath = null;
            String filename = null;
            File newFile = null;
            String getFileExtension = null;
            List<IAttachment> cAttachment = (List<IAttachment>) getWorkflowInstance().getValue(sysname);
            if ( (cAttachment != null) && (!cAttachment.isEmpty()) )
            {
                for ( Iterator iter = cAttachment.iterator() ; iter.hasNext() ; ) 
                { 
                    IAttachment iProcessAttachment = (IAttachment)iter.next();
                    serverTemporaryFileFullPath = iProcessAttachment.getName();
                }
                // RÉCUPÉRER L'EXTENSION DU FICHIER
                getFileExtension = serverTemporaryFileFullPath.substring(serverTemporaryFileFullPath.lastIndexOf(".") + 1);
                // FORMATER LE NOM DU FICHIER
                filename = Libelle_NDF + "_" + REF +"_"+ Id_NDF +"."+ getFileExtension ;
                // DÉFINIR LE CHEMAIN POUR DÉPOSER LE FICHIER
                newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + filename);
                newFile.createNewFile();
                InputStream is = cAttachment.get(0).getInputStream();
                OutputStream os = new FileOutputStream(newFile);
                byte[] buffer = new byte[is.available()];
                int length;
                while ((length = is.read(buffer)) > 0)
                {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();
                EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + filename, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + filename);

            }
            return  filename;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
