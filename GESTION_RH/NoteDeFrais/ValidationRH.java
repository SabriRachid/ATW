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

public class ValidationRH extends ConnexionBDD{

    /* ========================================= *
     * @Plateform :VDoc 15.0
     * @author r.sabri
     * @Creation_date :07/02/2017 15h17
     * @Modifier par R.SABRI le 06/06/2017
     * ========================================= */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ValidationRH.class);
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
        if (property.getName().equals("NDF_TabValidationRH"))
        {
            count = 0;
            count2=0;
            String refVDoc = (String)getWorkflowInstance().getValue(IProperty.System.REFERENCE);
            List NF = (List) getWorkflowInstance().getLinkedResources("NDF_TabValidationRH");
            if (NF.size() != 0)
                for (Iterator IT = NF.iterator(); IT.hasNext();)
                {
                    ILinkedResource ALLNF = (ILinkedResource) IT.next();
                    String Statut = (String) ALLNF.getValue("NDF_Tab_ValiderRH3");
                    float idNote = (float) ALLNF.getValue("NDF_ID_VNDFR3");
                    if("Refusé".equals(Statut)){
                        count ++;
                    }
                    if (isRejected(refVDoc,idNote)) 
                        count2 ++;
                    /*
                    if ("Validé".equals(Statut)) {
                        if (isRejected(refVDoc,idNote)) 
                            count2 ++;
                    }
                     */
                }
            if (count>=1) {
                // Si une des notes de frais réfusé  par le Directeur RH le demande retourne automatiquement vers le demandeur pour réctifier.
                getWorkflowInstance().setValue("DNF_Statut_VRH", "deny");
            }else if(count2>=1){
                getWorkflowInstance().setValue("DNF_Statut_VRH", "Valider"); 
            }else {
                getWorkflowInstance().setValue("DNF_Statut_VRH", "close");
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
    if (property.getName().equals("NDF_TabValidationRH"))
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
@SuppressWarnings("unchecked")
@Override
public boolean onAfterLoad() {
    String Ref_VDoc =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
    //if(((Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationRH")).size()==0)
    //{
    getWorkflowInstance().setValue("DNF_Statut_VRH", null);
    getNoteTable_ValidationRH(Ref_VDoc);
    //}
    return super.onAfterLoad();
}
/*
 * =================================================================================================================
 * CHECK THE EXPENSE REPORT (NOTE DE FRAIS REJETÉ)
 * =================================================================================================================
 */
public boolean isRejected(String RefNote, float noteId)
{
    cnx =null;
    st = null;
    rs = null;
    boolean rejete= false;
    int statut = 0;
    try {
        cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
        //String query = "SELECT StatutPV FROM NotesFrais" + 
        //        " WHERE Ref_NF=? and ID_NF=?";
        String query ="SELECT count(StatutPV) FROM NotesFrais WHERE StatutPV='Rejeté'" + 
                " and Ref_NF=? and ID_NF=?" + 
                " and  StatutPV not like (select Statut from NotesFrais where Ref_NF=? and ID_NF=? and Statut='Validé')";
        st = cnx.prepareStatement(query);
        st.setString(1, RefNote);
        st.setFloat(2, noteId);
        st.setString(3, RefNote);
        st.setFloat(4, noteId);
        rs= st.executeQuery();
        while (rs.next()) 
            statut = rs.getInt(1);
        if (statut>=1)
        {
            rejete= true;
        }
    }catch(Exception e)
    {
        e.printStackTrace();
        log.info("Error in isRejected() method : " + e.getClass() + " - " + e.getMessage());
    }finally {
        // LIBÉRER RESSOURCES DE LA MÉMOIRE.
        ConnexionBDD.close(cnx, st, rs);
    }
    return rejete;
}
/*
 * =================================================================================================================
 * GETNOTETABLE_VALIDATION RH
 * =================================================================================================================
 */
@SuppressWarnings({ "unused", "unchecked" })
public void getNoteTable_ValidationRH(String Reference)
{
    cnx =null;
    st = null;
    rs = null;
    try {
        List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("NDF_TabValidationRH");
        getWorkflowInstance().deleteLinkedResources(lst);
        cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
        String query = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF,StatutPV" + 
                " FROM NotesFrais WHERE Ref_NF=? ";
        // " FROM NotesFrais WHERE Ref_NF=? AND Flag=?";
        st = cnx.prepareStatement(query);
        st.setString(1, Reference);
        rs= st.executeQuery();
        while (rs.next()) {
            // CRÉATION D'UNE LIGNE 
            ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "NDF_TabValidationRH" );
            // POSITIONNEMENT DE QUELQUES VALEURS 
            linkedResource.setValue("NDF_Tab_DesignationRH3", rs.getString(1));
            linkedResource.setValue("NDF_Tab_QuantiteRH3",rs.getFloat(2));
            linkedResource.setValue("NDF_Tab_MontantRH3",rs.getFloat(3));  
            linkedResource.setValue("NDF_Tab_TotalRH3",rs.getFloat(4)); 
            linkedResource.setValue("NDF_Tab_DateJustifRH3",rs.getDate(5)); 
            String PJ = rs.getString(6);
            if (PJ==null) {
                linkedResource.setValue("NDF_Tab_PieceJustificativeRH3", null);
            }else {
                EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));

                File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//"+rs.getString(6));
                IAttachment att = getWorkflowModule().addAttachment(linkedResource, "NDF_Tab_PieceJustificativeRH3", doc);
                EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));

                List<IAttachment> PieceJustif = ((List<IAttachment>) linkedResource.getValue("NDF_Tab_PieceJustificativeRH3"));
                PieceJustif.clear();
                PieceJustif.add(att); 
                linkedResource.setValue("NDF_Tab_PieceJustificativeRH3", PieceJustif);
            }
            linkedResource.setValue("NDF_ID_VNDFR3", rs.getFloat(7));
            linkedResource.setValue("NDF_Tab_ValiderRH3", rs.getString(8));
            // AJOUT DE LA LIGNE AU TABLEAU
            getWorkflowInstance().addLinkedResource( linkedResource ); 
        }
    }catch(Exception e)
    {
        e.printStackTrace();
        log.info("Error in getNoteTable_ValidationRH() method : " + e.getClass() + " - " + e.getMessage());
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
    if (action.getName().equals("Valider"))
    {
        try {
            int count=0;
            // RECUPERER LES LIGNES DE LE TABLEAU DES NOTES DE FRAIS
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabValidationRH");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    // CRÉATION D'UNE LIGNE 
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    // VÉRIFIER SI LES NOTE DE FRAIS SONT VALIDÉ. 
                    if(asso.getValue("NDF_Tab_ValiderRH3")==null){
                        count ++;
                    }
                }
                if (count>0) {
                    passer = false;
                    getResourceController().alert("Une ou plusieurs notes de frais ne sont pas validées.");
                }
            }
        }catch(Exception e)
        {
            e.getStackTrace();
        }
    }  
    return passer;
}



public boolean getStatutPV(String RefNote, float noteId,String newStatutRH)
{
    cnx =null;
    st = null;
    rs = null;
    boolean isSameStatut= false;
    String oldStatut = "";
    try {
        cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
        //String query = "SELECT StatutPV FROM NotesFrais" + 
        //        " WHERE Ref_NF=? and ID_NF=?";
        String query ="SELECT StatutPV FROM NotesFrais WHERE " + 
                " Ref_NF=? and ID_NF=?";
        st = cnx.prepareStatement(query);
        st.setString(1, RefNote);
        st.setFloat(2, noteId);
        rs= st.executeQuery();
        while (rs.next()) 
        	oldStatut = rs.getString(1);
        
        if (oldStatut.equals(newStatutRH))
        {
        	isSameStatut = true;
        }
    }catch(Exception e)
    {
        e.printStackTrace();
        log.info("Error in isRejected() method : " + e.getClass() + " - " + e.getMessage());
    }
    return isSameStatut;
}



/*
 * =================================================================================================================
 * ONAFTERSUBMIT
 * =================================================================================================================
 */
@Override
public boolean onAfterSubmit(IAction action) {
    if(action.getName().equals("Valider")) {
        cnx = null;
        st = null;
        String Query = "";
        String statut = "";
        String Flag ="";
        try {
            statut =(String) getWorkflowInstance().getValue("DNF_Statut_VRH");
            Collection<?> associ = (Collection<?>) getWorkflowInstance().getLinkedResources("NDF_TabValidationRH");
            if (associ.size() != 0) {
                for (Iterator<?> iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                    String Ref_VDoc =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
                    float ID_VNF =(float) asso.getValue("NDF_ID_VNDFR3");
                    //String DECISION =(String) asso.getValue("NDF_Tab_ValiderRH2");
                    //String Query = "UPDATE NotesFrais SET Flag =?, Statut=? WHERE ID_NF=? AND Ref_NF=?";
                    String statutNDF = (String) asso.getValue("NDF_Tab_ValiderRH3");
                    if(getWorkflowInstance().getValue("DNF_Statut_VRH")!=null) {
                        if(statut.equals("deny")) {
                            Query = "UPDATE NotesFrais SET Flag ='Demande Encours' , Statut= ?  , Recept_NF='Non'  WHERE ID_NF=? AND Ref_NF=?";
                        }else if (statut.equals("Valider")){
                        	if(!getStatutPV(Ref_VDoc, ID_VNF, statutNDF))
                            Query = "UPDATE NotesFrais SET Flag ='Demande Encours' , Statut= ? , Recept_NF='Non' WHERE ID_NF=? AND Ref_NF=?";
                        	else
                        		Query = "UPDATE NotesFrais SET Flag ='Demande Encours' , Statut= ?  WHERE ID_NF=? AND Ref_NF=?";	
                        }else {
                            Query = "UPDATE NotesFrais SET Flag ='Demande clôturé' , Statut= ?  WHERE ID_NF=? AND Ref_NF=?";
                        }
                    }
                    st = cnx.prepareStatement(Query);
                    st.setString(1,statutNDF);
                    st.setFloat(2,ID_VNF); 
                    st.setString(3,Ref_VDoc);
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
}
