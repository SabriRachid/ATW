package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.lang.reflect.Field;

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
import com.axemble.vdoc.sdk.modules.IPortalModule;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;


public class SingletonConnexionBDD extends BaseDocumentExtension{
    /**
     * 
     */
    protected static final Logger log = Logger.getLogger(SingletonConnexionBDD.class);
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
    
    
    public SingletonConnexionBDD(){
    }
    
    
    private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static SingletonConnexionBDD sqlinstance = new SingletonConnexionBDD();
	}
    
    
    public static SingletonConnexionBDD getSqlSession(){
    	
    	SingletonHolder sh = new SingletonHolder();
    	SingletonConnexionBDD sbdd = null; 
    	Field field = null;
		try
		{
			
			field = SingletonHolder.class.getDeclaredField("sqlinstance");
			field.setAccessible(true);
			sbdd = (SingletonConnexionBDD) field.get(sh);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	 return sbdd;
    }
    
    @SuppressWarnings("unchecked")
   	public IConnectionDefinition<java.sql.Connection> getConnectionVDoc(IWorkflowModule im,IPortalModule ip) throws PortalModuleException
    {
    	try{
    		processContext = im.getContextByLogin("sysadmin");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return (IConnectionDefinition<Connection>) ip.getConnectionDefinition(processContext, "RH_ATTIJARI");
    }
    
    
    @SuppressWarnings("unchecked")
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
