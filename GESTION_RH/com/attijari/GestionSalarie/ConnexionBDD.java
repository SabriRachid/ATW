package com.attijari.GestionSalarie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflow;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

public class ConnexionBDD extends BaseDocumentExtension{
	
	private static final long serialVersionUID = 1L;
	protected static final Logger log =Logger.getLogger(ConnexionBDD.class);
	
	
	public Connection connection;
	public  PreparedStatement st;   
	public  ResultSet rs;
    public  ICatalog catalog ;
    public  IWorkflow workflow ;
    public  IContext context;
    public  IWorkflowInstance instance;
    public  IWorkflowModule module;;
    public  Connection cnx;
   
    public  String ReferenceBDD;
    public  IResourceController rc ;
  
	
	
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @SuppressWarnings("unchecked")
	public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
        context = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }

	
}
