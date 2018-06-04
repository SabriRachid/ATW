package cs.dev.operation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;
import com.serviceRH.EncryptionFile;

import dao.ConnexionBDD;
/**
 * @AUTHOR R.SABRI
 * @CREATION_DATE 04/07/2017 14:11 PM
 * @PLATEFORM VDOC14 2.1 
 */
public class MandatSigneAttachement extends ConnexionBDD{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(MandatSigneAttachement.class);
    // ----------------------------------------------------------------------------------------------------
    // 
    // ----------------------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        String ref =(String) getWorkflowInstance().getValue(IProperty.System.REFERENCE);
        String NomProjet = (String) getWorkflowInstance().getValue("GO_NomProjet");
        String RefFormater = NomProjet+"-"+ref;
        String Reponse = (String) getWorkflowInstance().getValue("GO_ReponseClientMandat");
        String workflowName=getWorkflowInstance().getCurrentTaskInstance(processContext).getTask().getName();
        if(workflowName.equalsIgnoreCase("GO_ReponseClientMandat"))        
        {
            if (Reponse.equals("Favorable"))
            {
                if (action.getName().equals("GO_EnvoyerRepCliMandat"))
                {
                    SetNoteDeFrais(RefFormater);
                }
            }
        }else {
            if ((String) getWorkflowInstance().getValue("GO_ReponseClientMandat")!=null)
            {
                if (Reponse.equals("Défavorable"))
                {
                    if (action.getName().equals("GO_Cloturer"))
                    {
                        SetNoteDeFrais(RefFormater);
                    }
                }
            }
        }

        return super.onAfterSubmit(action);
    }
    /*
     * =================================================================================================================
     * SETNOTEDEFRAIS
     * =================================================================================================================
     */
    public void SetNoteDeFrais(String RefOperation)
    {
        cnx = null;
        st = null;
        try {
            cnx=getConnectionVDoc("Ref_Attijari").getConnection();
            String Query = "UPDATE Dossiers SET MandatSign_pj=? WHERE RefDossier=?";
            st = cnx.prepareStatement(Query);
            st.setString(1,Add_MANDAT_SIGN(RefOperation,"GO_MandatSigne"));
            st.setString(2,RefOperation);
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
    public String Add_MANDAT_SIGN(String StrProjet, String sysname) throws IOException
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
                filename = StrProjet + "." + getFileExtension ;
                // DÉFINIR LE CHEMAIN POUR DÉPOSER LE FICHIER
                newFile = new File("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + filename);
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
                EncryptionFile.crypter("C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + filename, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//AttijariDoc_Temp//" + filename);
            }
            return  filename;
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in Add_MANDAT_SIGN() method : " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }
}
