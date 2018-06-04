package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IOrganization;
import com.axemble.vdoc.sdk.interfaces.IProject;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflow;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ConnexionBDD extends BaseDocumentExtension{
    /**
     * 
     */
    protected static final Logger log = Logger.getLogger(ConnexionBDD.class);
    private static final long serialVersionUID = 1L;
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *DECLARATION DES VARRIABLES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public  ICatalog catalog ;
    public  IWorkflow workflow ;
    public  IContext processContext;
    public  IWorkflowInstance instance;
    public  IWorkflowModule module;
    public  Connection cnx;
    public  PreparedStatement st;
    public  String ReferenceBDD;
    public  IResourceController rc ;
    public  ResultSet rs;
    public IDirectoryModule iDirectoryModule;
    public IOrganization organization;
    public IProject project;
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public IConnectionDefinition<java.sql.Connection> getConnectionVDoc (String Ref_externe) throws PortalModuleException
    {
        processContext = getWorkflowModule().getContextByLogin("sysadmin");
        return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(processContext, Ref_externe);
    }
    @SuppressWarnings("unchecked")
    public IConnectionDefinition<Connection> ConnectionDefinition(IWorkflowModule im) throws PortalModuleException
    {
        processContext = im.getContextByLogin("sysadmin");
        return (IConnectionDefinition<Connection>) Modules.getPortalModule().getConnectionDefinition(processContext,"RH_ATTIJARI");
    }
    
    //POUR AJOUTER LES SEPARATEUR ENTRE LES MILIERS
    public static String SeparateurMilliers(double Number) {
        String NumberOut="";
        NumberFormat formatter = null;
        formatter=java.text.NumberFormat.getInstance(java.util.Locale.FRENCH);
        formatter = new DecimalFormat("##,###.## ");
        NumberOut = formatter.format(Number).replace(",", ".");
        return NumberOut;
    }
    
    // CLOSE SELECT STATEMENT
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // CLOSE (INSERT, UPDATE, DELETE) STATEMENT
    public static void close(Connection conn, PreparedStatement pstmt) {
        try {
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
