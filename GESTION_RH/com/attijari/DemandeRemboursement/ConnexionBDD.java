package com.attijari.DemandeRemboursement;

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
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
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
}
