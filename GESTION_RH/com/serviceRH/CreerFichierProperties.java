package com.serviceRH;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.utils.Logger;

/**
 * ==========================
 * @author r.sabri
 * @creation_date 05.03.2018
 * ==========================
 */
public class CreerFichierProperties extends BaseDocumentExtension {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected static final Logger log = Logger.getLogger(CreerFichierProperties.class);

    final Properties prop = new Properties();
    OutputStream output = null;
    // ==================================================================================
    @Override
    public boolean onAfterLoad() {
        try {
            // chargement des propriétés
            Properties prop = CreerFichierProperties.load("C:\\VDocPlatform\\custom\\config.properties");
            // -----------------------
            // Information Serveur
            // -----------------------
            getWorkflowInstance().setValue("SMTPADRESS", prop.getProperty("SMTPADRESS", ""));
            getWorkflowInstance().setValue("IPADRESSE", prop.getProperty("IPADRESSE", ""));
            // -----------------------
            // Information AFC
            // -----------------------
            getWorkflowInstance().setValue("DG_AFC", prop.getProperty("DG_AFC", ""));
            getWorkflowInstance().setValue("DGA", prop.getProperty("DGA", ""));
            getWorkflowInstance().setValue("SMM", prop.getProperty("SMM", ""));
            getWorkflowInstance().setValue("SMI", prop.getProperty("SMI", ""));
            getWorkflowInstance().setValue("RH_AFC", prop.getProperty("RH_AFC", ""));
            getWorkflowInstance().setValue("ASTM_AFC", prop.getProperty("ASTM_AFC", ""));
            getWorkflowInstance().setValue("ASTN_AFC", prop.getProperty("ASTN_AFC", ""));
            // -----------------------
            // Information ATI
            // -----------------------
            getWorkflowInstance().setValue("DG_ATI", prop.getProperty("DG_ATI", ""));
            getWorkflowInstance().setValue("RH_ATI", prop.getProperty("RH_ATI", ""));
            getWorkflowInstance().setValue("ASTM_ATI", prop.getProperty("ASTM_ATI", ""));
            getWorkflowInstance().setValue("ASTN_ATI", prop.getProperty("ASTN_ATI", ""));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return super.onAfterLoad();
    }
    // ==================================================================================
    @Override
    public boolean onAfterSubmit(IAction action) {
        if (action.getName().equals("Envoyer"))
        {
            genererFichier("C:\\VDocPlatform\\custom\\config.properties");
        }
        return super.onAfterSubmit(action);
    }
    // ==================================================================================
    public void genererFichier(String path)
    {
        try {

            output = new FileOutputStream(path);

            //prop.store(output, "----------------------- \n Information Serveur \n -----------------------");

            prop.setProperty("IPADRESSE", (String) getWorkflowInstance().getValue("IPADRESSE"));
            prop.setProperty("SMTPADRESS", (String) getWorkflowInstance().getValue("SMTPADRESS"));
            // -----------------------
            // Information AFC
            // -----------------------
            prop.setProperty("DG_AFC", (String) getWorkflowInstance().getValue("DG_AFC"));
            prop.setProperty("DGA", (String) getWorkflowInstance().getValue("DGA"));
            prop.setProperty("SMM", (String) getWorkflowInstance().getValue("SMM"));
            prop.setProperty("SMI", (String) getWorkflowInstance().getValue("SMI"));
            prop.setProperty("RH_AFC", (String) getWorkflowInstance().getValue("RH_AFC"));
            prop.setProperty("ASTM_AFC", (String) getWorkflowInstance().getValue("ASTM_AFC"));
            prop.setProperty("ASTN_AFC", (String) getWorkflowInstance().getValue("ASTN_AFC"));
            // -----------------------
            // Information ATI
            // -----------------------
            prop.setProperty("DG_ATI", (String) getWorkflowInstance().getValue("DG_ATI"));
            prop.setProperty("RH_ATI", (String) getWorkflowInstance().getValue("RH_ATI"));
            prop.setProperty("ASTM_ATI", (String) getWorkflowInstance().getValue("ASTM_ATI"));
            prop.setProperty("ASTN_ATI", (String) getWorkflowInstance().getValue("ASTN_ATI"));

            // Enregistrer les propriétés dans la répertoire de stockage
            prop.store(output, null);

        } catch (final IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                    log.error("CS Error in genererFichier() - config.properties method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
                }
            }

        }
    }
    // ==================================================================

    public static Properties load(String filename) throws IOException, FileNotFoundException{
        Properties properties = new Properties();

        FileInputStream input = new FileInputStream(filename); 
        try{

            properties.load(input);
            return properties;

        }
        finally{

            input.close();

        }

    }
}
