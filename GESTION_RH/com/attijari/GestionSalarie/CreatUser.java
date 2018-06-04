package com.attijari.GestionSalarie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.IConfiguration;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IGroup;
import com.axemble.vdoc.sdk.interfaces.IOrganization;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.crystaldecisions.enterprise.ocaframework.idl.ImplServ.OSCAFactoryPackage.connection_failure;

import dao.SingletonConnexionBDD;

/**
 * Created on 10 août 2010
 * @author vdoc
 * 
 */

public class CreatUser extends BaseDocumentExtension
{

	 public  IContext context;
	 public Connection connection;
	public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
        context = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }
    /**
     * 
     */
    private static final long serialVersionUID = 6504488905312810102L;
    protected static final Logger log = Logger.getLogger(CreatUser.class);

    /**
     * @see com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension#onAfterSubmit(com.axemble.vdoc.sdk.interfaces.IAction)
     */
    
    public String getgroupe(){
    	String groupe = "";
    	String filiale=(String) getWorkflowInstance().getValue("P_GS_Filiale");
    	String profil = (String) getWorkflowInstance().getValue("P_GS_ProfilUser");
    	
    	if(filiale.equals("Attijari Finances Corp.")){
    		
    		if(profil.equals("ASSOCIATE")){
    			groupe="Associates";
			}else if(profil.equals("MANAGER")){
				groupe="Managers";
			}else if(profil.equals("SENIOR ASSOCIATE")){
				groupe="SiniorsAssociates";
			}
    	}else if(filiale.equals("Attijari Intermédiation")){
    		groupe="Intermediation";
    	}
    	return groupe;
    }
    
    
    public String getSup (String matricule){
    	String LoginSuperieur = null;
    	try {
    		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req= "select loginVdoc from personnel where matricule = ?";
			PreparedStatement st =connection.prepareStatement(req);
			
			st.setString(1, matricule);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()){
				LoginSuperieur = rs.getString(1);
			}
    		
		} catch (Exception e) {
		e.printStackTrace();
		}
    	
    	
    	return LoginSuperieur;
    }
    
    
    @Override
    public boolean onAfterSubmit(IAction action)
    {
        // Initialize a directory module (not available by default in this run context)
        IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
        try
        {
        	String Groupe = getgroupe();
            // We get group name where user must be set
            String groupName = Groupe;
            if ( (groupName == null) || (groupName.equals("")) )
            {
                throw new Exception("Error in getting user's group name !");
            }
            
            // We use a sysadmin context to get group
            IContext context = iDirectoryModule.getContextByLogin("sysadmin");
            
            // We must get an organization to set to user created (and to get group)
            // We get it in user parameters in application
            String organizationName = "DefaultOrganization";
            if ( (organizationName == null) || (organizationName.equals("")) )
            {
                throw new Exception("Error in getting user's organization name !");
            }
            
            // We get organization of group
            IOrganization iOrganization = iDirectoryModule.getOrganization(context, organizationName);
            // We can get group after
            IGroup iGroup = iDirectoryModule.getGroup(context, iOrganization, groupName);
            
            // We can get data from document instance
            IWorkflowInstance iWorkflowInstance = getWorkflowInstance();
            String lastName = (String) iWorkflowInstance.getValue("P_GS_Nom");
            String firstName = (String) iWorkflowInstance.getValue("P_GS_Prenom");
            String eMail = (String) iWorkflowInstance.getValue("P_GS_Mail");
            String phoneNumber = (String) iWorkflowInstance.getValue("P_GS_tel1");
            String login = (String) iWorkflowInstance.getValue("P_GS_VdocLogin");
            String Matricule = (String) iWorkflowInstance.getValue("P_GS_Matricule");
            String matSup = (String) iWorkflowInstance.getValue("P_GS_Sup");
            String filiale = (String) iWorkflowInstance.getValue("P_GS_Filiale");
            String manager = (String) iWorkflowInstance.getValue("P_GS_Managers");
            String password = "vdoc";
            
         
            
 			
            // We test if user already exists
            if (iDirectoryModule.getUserByLogin(login) == null)
            {
                // Opening a transaction
                iDirectoryModule.beginTransaction();
                
                // We get organization with our sysadmin context
                IOrganization organization = iDirectoryModule.getOrganization(context, organizationName);
                
                // We create user
                IUser iUser = iDirectoryModule.createUser(context, login, password, organization);
                
                // We set data in this new user
                iUser.setLastName(lastName);
                iUser.setFirstName(firstName);
                iUser.setEmail(eMail);                
                iUser.setPhoneNumber(phoneNumber);
                iUser.setEmployeeNumber(Matricule);
                iUser.setSkin("Skin Attijari Finance & corp");
                
                String LoginSup = getSup(matSup);
                IUser Sup = getWorkflowModule().getUserByLogin(LoginSup);
                iUser.setHierarchicalManager(Sup);
                
                if(filiale.equals("Attijari Finances Corp.") ){
                	String loginmanager = getSup(manager);
         			IUser Manager = getWorkflowModule().getUserByLogin(loginmanager);

                	 iUser.setManager(Manager);
                }
               
                
                // We add this user in our group
                iDirectoryModule.addMember(iUser, iGroup);
                
                // Don't forget to save the user
                iUser.save(context);
                
                // Committing transaction
                iDirectoryModule.commitTransaction();
            }
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            log.error("Error in CreateUser onAfterSave method : " + e.getClass() + " - " + message);
            
            iDirectoryModule.rollbackTransaction();
        }
        finally
        {
            // Don't forget to release the module we initialize
            Modules.releaseModule(iDirectoryModule);
        }
        return super.onAfterSubmit(action);
    }

    /**
     * 
     * @param key
     * @return
     * @throws Exception
     */
    protected String getUserParameterInConfiguration(String key) throws Exception
    {
        String value = null;
        try
        {
            // We must get application (use workflow instance)
            ICatalog catalog = getWorkflowInstance().getCatalog();
            
            // We must get application configuration
            IConfiguration configuration = catalog.getConfiguration();
            
            value = configuration.getStringUserProperty(key);
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            log.error("Error in CreateUser getGroupNameInConfiguration method : " + e.getClass() + " - " + message);
            throw(e);
        }
        return value;
    }
    
}
