package com.attijari.DemandeRemboursement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;






import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.runtime.INavigateContext;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.modules.IModule;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.providers.BaseViewProvider;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.core.providers.ICollectionModelViewProvider;
import com.axemble.vdp.ui.framework.composites.base.CtlAbstractView;
import com.axemble.vdp.ui.framework.composites.base.models.views.CollectionViewModel;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelColumn;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelItem;

import dao.SingletonConnexionBDD;

public class SuperieurProvider extends BaseViewProvider implements ICollectionModelViewProvider
{
	
	
	
	private static final long serialVersionUID = 1;
    protected static final Logger log = Logger.getLogger(SuperieurProvider.class);
    PreparedStatement st;
    Connection connection;
    IContext ctx;
    ResultSet rs;
    
    
    public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException {
        this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
        return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
    }
    
    
    
    public ArrayList<Beneficiare> getBeneficiaire (IWorkflowInstance instance){
    	
    	  ArrayList<Beneficiare> ListBenef = new ArrayList<Beneficiare>();
    	  try{
    	  connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
    	  
    //	  IWorkflowModule workflowmodule = ApplicationDataProvider.workflowModule;
    	  
         String matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
    	 
    	// ApplicationDataProvider.resourceController.alert(Matricule);
    	 
    	  //dfsfndjhf dsgd = new dfhdfkjhdf()
    	  //dfsfndjhf.
    	  //Selectionner le personnel 
    	 
    	  
    	  String query = "select Nom, Prenom, NCMIM from Personnel where matricule = ? ";
          st = connection.prepareStatement(query);
          st.setString(1, matricule);
          rs = st.executeQuery();
          while (rs.next()) {
        	  Beneficiare BENEFICIARE = new Beneficiare();
         
        	  BENEFICIARE.setNom(rs.getString(1));
        	  BENEFICIARE.setPrenom(rs.getString(2));
        	  BENEFICIARE.setNCMIM(rs.getString(3));
          	            	
        	  ListBenef.add(BENEFICIARE);
          }
    	  
    	  // selectionner les conjoints 
          
    	  String query2 = "select Nom, Prenom, NCMIM from Conjoint where personnelMatricule = ? ";
          st = connection.prepareStatement(query2);
          st.setString(1, matricule);
          rs = st.executeQuery();
          while (rs.next()) {
        	  Beneficiare BENEFICIARE = new Beneficiare();
         
        	  BENEFICIARE.setNom(rs.getString(1));
        	  BENEFICIARE.setPrenom(rs.getString(2));
        	  BENEFICIARE.setNCMIM(rs.getString(3));
          	            	
        	  ListBenef.add(BENEFICIARE);
          }
      
          // selectionner les enfants 
          
          
          String query3 = "select Nom, Prenom, NCMIM from Enfant "
          		+ "where Personnelmatricule = ? ";
          st = connection.prepareStatement(query3);
          st.setString(1, matricule);
          rs = st.executeQuery();
          while (rs.next()) {
        	  Beneficiare BENEFICIARE = new Beneficiare();
         
        	  BENEFICIARE.setNom(rs.getString(1));
        	  BENEFICIARE.setPrenom(rs.getString(2));
        	  BENEFICIARE.setNCMIM(rs.getString(3));
          	            	
        	  ListBenef.add(BENEFICIARE);
          }
   
    	  }catch(Exception e){
    		  e.printStackTrace();
    	  }
    	  
    	 return ListBenef; 
    	
    }
    
    
   @Override
public void init()
{
	
	super.init();
	
	
	 CollectionViewModel viewModel = (CollectionViewModel)this.getModel();
     //ViewModelColumn modelColumn = new ViewModelColumn("tran_Id","Id tran",ViewModelColumn.TYPE_INTEGER);
     //viewModel.addColumn(modelColumn);
     
     ViewModelColumn modelColumn = new ViewModelColumn("NCMIM","NCMIM",ViewModelColumn.TYPE_STRING);
     viewModel.addColumn(modelColumn);
     
     modelColumn = new ViewModelColumn("Nom","Nom",ViewModelColumn.TYPE_STRING);
     viewModel.addColumn(modelColumn);
     
     modelColumn = new ViewModelColumn("Prenom","Prenom",ViewModelColumn.TYPE_STRING);
     viewModel.addColumn(modelColumn);
     
   
     
}

	public SuperieurProvider(INavigateContext context, CtlAbstractView view)
	{
		super(context, view);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ViewModelItem> getModelItems()
	{        ArrayList<ViewModelItem> cViewModelItem;
    IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
    cViewModelItem = new ArrayList<ViewModelItem>();
    try {
        try {
            IWorkflowInstance instance = ApplicationDataProvider.workflowInstance;
            ArrayList<Beneficiare> ListBenef =getBeneficiaire(instance);
            for (Beneficiare BENEFICIAIRE : ListBenef) {
                ViewModelItem viewModelItem = new ViewModelItem();
                viewModelItem.setKey(BENEFICIAIRE.getNCMIM());
               // viewModelItem.setValue("tran_Id",tran.getId());
                viewModelItem.setValue("NCMIM","NCMIM : "+BENEFICIAIRE.getNCMIM());
                viewModelItem.setValue("Nom","Nom : "+BENEFICIAIRE.getNom());
                viewModelItem.setValue("Prenom","Prenom : "+BENEFICIAIRE.getPrenom());
                cViewModelItem.add(viewModelItem);
            }
        }
        catch (Exception e) {
            log.error("CS:ATTIJARI: Erreur in getModelItems method" + e.getClass() + " _ " + e.getMessage());
            Modules.releaseModule((IModule)iDirectoryModule);
        }
    }
    finally {
        Modules.releaseModule((IModule)iDirectoryModule);
    }
    return cViewModelItem;
	
	}
	
	
	
	
	
	
	
	
	
}
