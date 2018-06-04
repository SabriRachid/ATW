package NoteDeFrais;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;

//import java.util.ArrayList;
//import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.utils.Logger;
import com.ibm.icu.text.DateFormat;
import com.serviceRH.EncryptionFile;

import beans.NotesFrais;
import dao.ConnexionBDD;
import dao.SingletonConnexionBDD;

public class SaisieNoteDeFrais extends ConnexionBDD{

    /* ========================================= *
     * @Plateform :VDoc 15.0
     * @author r.sabri
     * @Creation_date :07/02/2017 15h17
     * @Modefier 06/06/2017
     * ========================================= */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(SaisieNoteDeFrais.class);
    /*
     * =================================================================================================================
     * ONAFTERLOAD
     * =================================================================================================================
     */
    @Override
    public boolean onAfterLoad() {
    	
    	String ipAdresse = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
		getWorkflowInstance().setValue("URI", "http://"+ipAdresse+"/vdoc"+getWorkflowInstance().getURI());
		
    	
        cnx =null;
        st = null;
        rs = null;
        try{
            String Ref_VDoc =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
            // CLÉ DE L'ÉTAPE L'ÉTAPE COURANTE
            String workflowName=getWorkflowInstance().getCurrentTaskInstance(processContext).getTask().getName();
            if(workflowName.equalsIgnoreCase("NDF_SaisieNoteFrais"))        
            {
                // RÉCUPÉRER L'UTILISATEUR CONNECTÉ
                IUser utlisateur1 = getWorkflowModule().getLoggedOnUser();
                String str = utlisateur1.getFullName();
                String NumEmploye = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
                getWorkflowInstance().setValue("NDF_PersoMat", NumEmploye);
                getWorkflowInstance().setValue("NDF_Fullname", str);
                // RÉCUPÉRER LE FILIALE D'UTILISATEUR CONNECTÉ
                String query = "select Libelle from Personnel, Filiale" + 
                        " where IdFiliale=FilialeIdFiliale and matricule=?";
                cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                st = cnx.prepareStatement(query);
                st.setString(1,NumEmploye);
                rs = st.executeQuery();
                while(rs.next())
                {
                    getWorkflowInstance().setValue("NDF_Filliale", rs.getString("Libelle"));
                }
                IUser DRH = null;
                IUser ASSISTANTE = null;
                if (getWorkflowInstance().getValue("NDF_Filliale").equals("Attijari Intermédiation"))
                {
                	ASSISTANTE = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AST_ATI")); 
                	DRH = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI")); 
                }else if (getWorkflowInstance().getValue("NDF_Filliale").equals("Attijari Finances Corporate"))
                {
                	ASSISTANTE = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AST_AFC"));  
                	DRH = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC")); 
                }
                List<IUser> utlisateur = new ArrayList<>();
                utlisateur.add(ASSISTANTE);
                getWorkflowInstance().setValue("DR_Assistante", utlisateur);
                getWorkflowInstance().save("DR_Assistante");
                utlisateur = new ArrayList<>();
                utlisateur.add(DRH);
                getWorkflowInstance().setValue("NDF_ResponsableFinancier", utlisateur);
                getWorkflowInstance().save("NDF_ResponsableFinancier");
            }else {
                getNoteDeFrais(Ref_VDoc);  
            }
        }catch(Exception e){
            e.getStackTrace();
            log.info("Error in onAfterLoad() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
        return super.onAfterLoad();
    }
    /*
     * =================================================================================================================
     * ONAFTERSUBMIT
     * =================================================================================================================
     */
    @Override
    public boolean onAfterSubmit(IAction action) {
        // CLÉ DE L'ÉTAPE L'ÉTAPE COURANTE
        String workflowName=getWorkflowInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("NDF_SaisieNoteFrais"))        
        {
            if(action.getName().equals("NDF_Envoyer"))
            {
                String DEMANDEUR_NF =(String) getWorkflowInstance().getValue("NDF_Fullname");
                String REF_VDOC =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
                String MAT_NF =(String) getWorkflowInstance().getValue("NDF_PersoMat");
                String FILIAlE_NF =(String) getWorkflowInstance().getValue("NDF_Filliale");
                String TypeMissionNF =(String) getWorkflowInstance().getValue("NDF_TypeMission");
                SetNoteDeFrais(DEMANDEUR_NF, REF_VDOC, MAT_NF, FILIAlE_NF,TypeMissionNF);
            }
        }else{
        	if(action.getName().equals("Send_Note"))
            {
        		String DEMANDEUR_NF =(String) getWorkflowInstance().getValue("NDF_Fullname");
                String REF_VDOC =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
                String MAT_NF =(String) getWorkflowInstance().getValue("NDF_PersoMat");
                String FILIAlE_NF =(String) getWorkflowInstance().getValue("NDF_Filliale");
                String TypeMissionNF =(String) getWorkflowInstance().getValue("NDF_TypeMission");
                updateNoteDeFrais(DEMANDEUR_NF, REF_VDOC, MAT_NF, FILIAlE_NF,TypeMissionNF);
            }
        }
        
        
        return super.onAfterSubmit(action);
    }
    /*
     * =================================================================================================================
     * GETNOTEDEFRAIS
     * =================================================================================================================
     */
    public void getNoteDeFrais(String Reference)
    {
        cnx =null;
        st = null;
        rs = null;
        try {
            List<ILinkedResource> lst = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("NDF_TabNotesFrais_Perso");
            getWorkflowInstance().deleteLinkedResources(lst);
            String query1 = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF" + 
                    " FROM NotesFrais WHERE Ref_NF=? AND (StatutPV='Refusé' or Statut='Refusé')";
            //  String query1 = "SELECT Design_NF,Qte_NF,Mt_NF,(Qte_NF*Mt_NF) as 'Montant',DateJust_NF,PJ_NF,ID_NF" + 
            //  " FROM NotesFrais WHERE Ref_NF=? AND Statut='Refusé' AND StatutPV='Refusé'";
            cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
            st = cnx.prepareStatement(query1);
            st.setString(1, Reference);
            rs= st.executeQuery();
            while (rs.next()) {
                // CRÉATION D'UNE LIGNE 
                ILinkedResource linkedResource = getWorkflowInstance().createLinkedResource( "NDF_TabNotesFrais_Perso" );
                // POSITIONNEMENT DE QUELQUES VALEURS 
                linkedResource.setValue("NDF_Tab_Designation2", rs.getString(1));
                linkedResource.setValue("NDF_Tab_Quantite2",rs.getFloat(2));
                linkedResource.setValue("NDF_Tab_Montant2",rs.getFloat(3));  
                linkedResource.setValue("NDF_Tab_Total2",rs.getFloat(4)); 
                linkedResource.setValue("NDF_Tab_DateJustif2",rs.getDate(5)); 
                String PJ = rs.getString(6);
                if (PJ==null) {
                    linkedResource.setValue("NDF_Tab_PieceJustificative2", null);
                }else {
                    
                    EncryptionFile.decrypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));
                    File doc = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//"+rs.getString(6));
                    IAttachment att = getWorkflowModule().addAttachment(linkedResource, "NDF_Tab_PieceJustificative2", doc);
                    EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6), "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//RH//NDF//" + rs.getString(6));
                    
                    List<IAttachment> PieceJustif = ((List<IAttachment>) linkedResource.getValue("NDF_Tab_PieceJustificative2"));
                    PieceJustif.clear();
                    PieceJustif.add(att); 
                    linkedResource.setValue("NDF_Tab_PieceJustificative2", PieceJustif); 
                }
                linkedResource.setValue("NDF_ID", rs.getFloat(7));
                // AJOUT DE LA LIGNE AU TABLEAU
                getWorkflowInstance().addLinkedResource( linkedResource ); 
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in getNoteDeFrais() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st, rs);
        }
    }
    
    
    public void updateNoteDeFrais(String DEMANDEUR_NF, String REF_VDOC, String MAT_NF, String FILIAlE_NF, String TypeMiss)
    {
        cnx =null;
        st = null;
        rs = null;
        try {
            float ID_NF = 0f;
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabNotesFrais_Perso");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    ID_NF++;
                    String Des_NF =(String) asso.getValue("NDF_Tab_Designation2");
                    float QT_NF =(float) asso.getValue("NDF_Tab_Quantite2");
                    float MT_NF =(float) asso.getValue("NDF_Tab_Montant2");
                    String Query = "update NotesFrais set Design_NF=?, Qte_NF=? ,Mt_NF = ?,DateJust_NF=?,PJ_NF=?,DateNF=? where PersonnelMatricule = ? and REF_NF = ? and ID_NF=?" ;
                    cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                    st = cnx.prepareStatement(Query);
                    st.setString(1,Des_NF);
                    st.setFloat(2,QT_NF); 
                    st.setDouble(3,MT_NF);
                    if (Des_NF.equals("FRAIS KILOMETRIQUES") || Des_NF.equals("TAXI") || Des_NF.equals("AUTRES"))
                    {
                    	if(asso.getValue("NDF_Tab_DateJustif2")==null)
                        st.setDate(4,null); 
                    	else{
                        Date DateJ_NF =(Date) asso.getValue("NDF_Tab_DateJustif2");
                        java.util.Date utilDate = DateJ_NF;
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        st.setDate(4,sqlDate); 
                    	}
                    }
                    else{
                    	 Date DateJ_NF =(Date) asso.getValue("NDF_Tab_DateJustif2");
                         java.util.Date utilDate = DateJ_NF;
                         java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                         st.setDate(4,sqlDate);
                    }
                    
                    if (Des_NF.equals("FRAIS KILOMETRIQUES") || Des_NF.equals("TAXI") || Des_NF.equals("AUTRES"))
                    {
                    	if(asso.getValue("NDF_Tab_PieceJustificative2")==null)
                    		st.setString(5,null);
                    	else
                    		st.setString(5,Add_PJ_NDF(Des_NF, ID_NF, REF_VDOC,asso ,"NDF_Tab_PieceJustificative2"));
                    }else {
                        st.setString(5,Add_PJ_NDF(Des_NF, ID_NF, REF_VDOC,asso ,"NDF_Tab_PieceJustificative2"));
                    }
                    Date day = new Date();
                    java.util.Date UTiDate = day;
                    java.sql.Date SqlDate = new java.sql.Date(UTiDate.getTime());
                    st.setDate(6, SqlDate);
                    st.setString(7, MAT_NF);
                    st.setString(8, REF_VDOC);
                    float ID_NFUP = (float)asso.getValue("NDF_ID");
                    st.setFloat(9, ID_NFUP);
                    
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in SetNoteDeFrais() method : " + e.getClass() + " - " + e.getMessage());
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(cnx, st);
        }
    }
    
    
    /*
     * =================================================================================================================
     * SETNOTEDEFRAIS
     * =================================================================================================================
     */
    public void SetNoteDeFrais(String DEMANDEUR_NF, String REF_VDOC, String MAT_NF, String FILIAlE_NF, String TypeMiss)
    {
        cnx =null;
        st = null;
        rs = null;
        try {
            float ID_NF = 0f;
            Collection associ = (Collection) getWorkflowInstance().getLinkedResources("NDF_TabNotesFrais");
            if (associ.size() != 0) {
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {
                    ILinkedResource asso = (ILinkedResource) iter1.next();
                    ID_NF++;
                    String Des_NF =(String) asso.getValue("NDF_Tab_Designation");
                    float QT_NF =(float) asso.getValue("NDF_Tab_Quantite");
                    float MT_NF =(float) asso.getValue("NDF_Tab_Montant");
                    String Query = "INSERT INTO NotesFrais(Design_NF,Qte_NF,Mt_NF,DateJust_NF,Demandeur_NF,Ref_NF,PersonnelMatricule,Filiale_NF,PJ_NF,ID_NF,TypeMission,DateNF)" +
                            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    cnx=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
                    st = cnx.prepareStatement(Query);
                    st.setString(1,Des_NF);
                    st.setFloat(2,QT_NF); 
                    st.setDouble(3,MT_NF);
                    if (Des_NF.equals("FRAIS KILOMETRIQUES") || Des_NF.equals("TAXI") || Des_NF.equals("AUTRES"))
                    {
                    	if(asso.getValue("NDF_Tab_DateJustif")==null)
                        st.setDate(4,null); 
                    	else{
                        Date DateJ_NF =(Date) asso.getValue("NDF_Tab_DateJustif");
                        java.util.Date utilDate = DateJ_NF;
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        st.setDate(4,sqlDate); 
                    	}
                    }
                    else{
                    	 Date DateJ_NF =(Date) asso.getValue("NDF_Tab_DateJustif");
                         java.util.Date utilDate = DateJ_NF;
                         java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                         st.setDate(4,sqlDate);
                    }
                    st.setString(5,DEMANDEUR_NF); 
                    st.setString(6,REF_VDOC);
                    st.setString(7,MAT_NF);
                    st.setString(8,FILIAlE_NF);
                    if (Des_NF.equals("FRAIS KILOMETRIQUES") || Des_NF.equals("TAXI") || Des_NF.equals("AUTRES"))
                    {
                    	if(asso.getValue("NDF_Tab_PieceJustificative")==null)
                    		st.setString(9,null);
                    	else
                    		st.setString(9,Add_PJ_NDF(Des_NF, ID_NF, REF_VDOC,asso ,"NDF_Tab_PieceJustificative"));
                    }else {
                        st.setString(9,Add_PJ_NDF(Des_NF, ID_NF, REF_VDOC,asso ,"NDF_Tab_PieceJustificative"));
                    }
                    st.setFloat(10,ID_NF);
                    st.setString(11,TypeMiss);
                    Date day = new Date();
                    java.util.Date UTiDate = day;
                    java.sql.Date SqlDate = new java.sql.Date(UTiDate.getTime());
                    st.setDate(12, SqlDate);
                    st.executeUpdate();
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info("Error in SetNoteDeFrais() method : " + e.getClass() + " - " + e.getMessage());
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
    public String Add_PJ_NDF(String Libelle_NDF, float Id_NDF,String REF,ILinkedResource LNK,String sysname) throws IOException
    {
        try{
            ILinkedResource INSTANCE = LNK;
            String serverTemporaryFileFullPath = null;
            String filename = null;
            File newFile = null;
            String getFileExtension = null;
            List<IAttachment> cAttachment = (List<IAttachment>) INSTANCE.getValue(sysname);
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
                filename = Libelle_NDF + "_" + REF +"_"+ Id_NDF + "." + getFileExtension ;
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
            log.info("Error in Add_PJ_NDF() method : " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }
}
