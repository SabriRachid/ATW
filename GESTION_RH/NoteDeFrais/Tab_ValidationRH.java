package NoteDeFrais;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.utils.Logger;

import beans.NotesFrais;
import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class Tab_ValidationRH extends ConnexionBDD{

    /* ========================================= *
     * @Plateform :VDoc 14.2
     * @author r.sabri
     * @Creation_date :16/06/2017 10h17
     * ========================================= */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(Tab_ValidationRH.class);
    int count =0;
    @Override
    public boolean onAfterLoad() {
        // CLÉ DE L'ÉTAPE L'ÉTAPE COURANTE
        String workflowName=getWorkflowInstance().getParentInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("GRAND_VALIDATIONRH"))        
        {
        	
        	  String refVDoc = (String)getWorkflowInstance().getParentInstance().getValue(IProperty.System.REFERENCE);
              float idNote = (float) getWorkflowInstance().getValue("NDF_ID_VNDFR3");
              getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation3", null);
              String newStatutRH = (String)getWorkflowInstance().getValue("NDF_Tab_ValiderRH3");
              if(getStatutPV(refVDoc, idNote, newStatutRH)){
              	getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation3","");
                  getResourceController().showBodyBlock("FrMotif", false);
              }
              else{
              	getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", true);
                  getResourceController().showBodyBlock("FrMotif", true);
              }
//            String refVDoc = (String)getWorkflowInstance().getParentInstance().getValue(IProperty.System.REFERENCE);
//            int idNote = (int) getWorkflowInstance().getValue("NDF_ID_VNDFR3");
//            //getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation3", null);
//            String r = (String)getWorkflowInstance().getValue("NDF_Tab_ValiderRH3");
//            if (r.equals("Validé")) {
//                if(isRejected(refVDoc, idNote)){
//                    getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", true);
//                    getResourceController().showBodyBlock("FrMotif", true);
//                }else {
//                    getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", false);
//                    getResourceController().showBodyBlock("FrMotif", false);
//                }
//
//            }else{
//                getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", true);
//                getResourceController().showBodyBlock("FrMotif", true);
//            } 
        }
        return super.onAfterLoad();
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public void onPropertyChanged(IProperty property) {
        if (property.getName().equals("NDF_Tab_ValiderRH"))
        {
            getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation", null);
        }
        if (property.getName().equals("NDF_Tab_ValiderRH2"))
        {
            getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation2", null);
        }
        if (property.getName().equals("NDF_Tab_ValiderRH3"))
        {
            String refVDoc = (String)getWorkflowInstance().getParentInstance().getValue(IProperty.System.REFERENCE);
            float idNote = (float) getWorkflowInstance().getValue("NDF_ID_VNDFR3");
            getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation3", null);
            String newStatutRH = (String)getWorkflowInstance().getValue("NDF_Tab_ValiderRH3");
            if(getStatutPV(refVDoc, idNote, newStatutRH)){
            	getWorkflowInstance().setValue("NDF_Tab_MotifNonValidation3","");
                getResourceController().showBodyBlock("FrMotif", false);
            }
            else{
            	getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", true);
                getResourceController().showBodyBlock("FrMotif", true);
            }
            	
//            if (r.equals("Validé")) {
//                if(isRejected(refVDoc, idNote)){
//                    getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", true);
//                    getResourceController().showBodyBlock("FrMotif", true);
//                }else {
//                    getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", false);
//                    getResourceController().showBodyBlock("FrMotif", false);
//                }
//
//            }else{
//                getResourceController().setMandatory("NDF_Tab_MotifNonValidation3", true);
//                getResourceController().showBodyBlock("FrMotif", true);
//            }
        }
        super.onPropertyChanged(property);
    }
    /*
     * =================================================================================================================
     * CHECK STATUT MÉTHODE
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
            String query ="SELECT count(StatutPV) as 'statExist' FROM NotesFrais WHERE StatutPV='Rejeté'" + 
                    " and Ref_NF=? and ID_NF=?";
            st = cnx.prepareStatement(query);
            st.setString(1, RefNote);
            st.setFloat(2, noteId);
            rs= st.executeQuery();
            while (rs.next()) 
                statut = rs.getInt("statExist");
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
     * CHECK STATUT MÉTHODE
     * =================================================================================================================
     */
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
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return isSameStatut;
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty property) {
        if (property.getName().equals("NDF_Tab_ValiderRH"))
        {
            return true; 
        }
        if (property.getName().equals("NDF_Tab_ValiderRH2"))
        {
            return true;
        }

        if (property.getName().equals("NDF_Tab_ValiderRH3"))
        {
            return true;
        }
        return super.isOnChangeSubscriptionOn(property);
    }
    /*
     * =================================================================================================================
     * 
     * =================================================================================================================
     */
    @Override
    public boolean onBeforeSave() {
        float ID =0f;
        String REF_VDOC =(String) getWorkflowInstance().getParentInstance().getValue(IProperty.System.REFERENCE);
        String DECISION = null;
        String sqlStatut = null;
        // CLÉ DE L'ÉTAPE L'ÉTAPE COURANTE
        String workflowName=getWorkflowInstance().getParentInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("GRAND_VALIDATIONRH"))        
        {
            DECISION =(String) getWorkflowInstance().getValue("NDF_Tab_ValiderRH3");
            ID =(float) getWorkflowInstance().getValue("NDF_ID_VNDFR3");
            sqlStatut ="Statut";
        }else if(workflowName.equalsIgnoreCase("VALIDATIONRH")) { 
            DECISION =(String) getWorkflowInstance().getValue("NDF_Tab_ValiderRH2");
            ID = (float) getWorkflowInstance().getValue("NDF_ID_VNDFR");
            sqlStatut ="StatutPV";
        }else {
            DECISION =(String) getWorkflowInstance().getValue("NDF_Tab_ValiderRH");
            
            float idFloat = (float) getWorkflowInstance().getValue("NDF_ID_VNDF");
            ID = (float) idFloat;//(int) getWorkflowInstance().getValue("NDF_ID_VNDF");
            sqlStatut ="StatutPV";
        }
        SetNoteDeFrais(REF_VDOC, DECISION, ID, sqlStatut);
        return super.onBeforeSave();
    }
    /*
     * =================================================================================================================
     * SETNOTEDEFRAIS
     * =================================================================================================================
     */
    public void SetNoteDeFrais(String REF, String DEC, float id, String statut)
    {
        cnx = null;
        st = null;
        try {
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            String Query = "UPDATE NotesFrais SET "+statut+"=? WHERE ID_NF=? AND Ref_NF=?";
            st = cnx.prepareStatement(Query);
            st.setString(1,DEC);
            st.setFloat(2,id); 
            st.setString(3,REF);
            st.executeUpdate();
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
}
