package com.attijari.GestionSalarie;





import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.runtime.INavigateContext;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.modules.IModule;
import com.axemble.vdoc.sdk.providers.BaseViewProvider;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.core.providers.ICollectionModelViewProvider;
import com.axemble.vdp.ui.framework.composites.base.CtlAbstractView;
import com.axemble.vdp.ui.framework.composites.base.models.views.CollectionViewModel;
import com.axemble.vdp.ui.framework.composites.base.models.views.IViewModel;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelColumn;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelItem;

import dao.SingletonConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



	public class PersonnelProvider extends BaseViewProvider implements ICollectionModelViewProvider {
	    private static final long serialVersionUID = 1;
	    protected static final Logger log = Logger.getLogger(PersonnelProvider.class);
	    PreparedStatement st;
	    Connection connection;
	    IContext ctx;
	    ResultSet rs;

	    public PersonnelProvider(INavigateContext context, CtlAbstractView view) {
	        super(context, view);
	    }
	    public void init() {
	        super.init();
	        CollectionViewModel viewModel = (CollectionViewModel)this.getModel();
	        //ViewModelColumn modelColumn = new ViewModelColumn("tran_Id","Id tran",ViewModelColumn.TYPE_INTEGER);
	        //viewModel.addColumn(modelColumn);
	        
	        ViewModelColumn modelColumn = new ViewModelColumn("Matricule","Matricule ",ViewModelColumn.TYPE_STRING);
	        viewModel.addColumn(modelColumn);
	        
	        modelColumn = new ViewModelColumn("Nom","Nom ",ViewModelColumn.TYPE_STRING);
	        viewModel.addColumn(modelColumn);
	        
	        modelColumn = new ViewModelColumn("Prenom","Prénom ",ViewModelColumn.TYPE_STRING);
	        viewModel.addColumn(modelColumn);
	        
	        

	    }

	    public List<ViewModelItem> getModelItems() {
	        ArrayList<ViewModelItem> cViewModelItem;
	        IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
	        cViewModelItem = new ArrayList<ViewModelItem>();
	        try {
	            try {
	                IWorkflowInstance instance = ApplicationDataProvider.workflowInstance;
	                ArrayList<Personnel> Personnel =getPersonnel(instance);
	                for (Personnel PERSONNEL : Personnel) {
	                    ViewModelItem viewModelItem = new ViewModelItem();
	                    viewModelItem.setKey(PERSONNEL.getMatricule());
	                   // viewModelItem.setValue("tran_Id",tran.getId());
	                    viewModelItem.setValue("Matricule","Matricule : "+PERSONNEL.getMatricule());
	                    viewModelItem.setValue("Nom","Nom : "+PERSONNEL.getNom());
	                    viewModelItem.setValue("Prenom","Prenom : "+PERSONNEL.getPrenom());
	                    cViewModelItem.add(viewModelItem);
	                }
	            }
	            catch (Exception e) {
	                log.error("CS:BIOPHARMA: Erreur in getModelItems method" + e.getClass() + " _ " + e.getMessage());
	                Modules.releaseModule((IModule)iDirectoryModule);
	            }
	        }
	        finally {
	            Modules.releaseModule((IModule)iDirectoryModule);
	        }
	        return cViewModelItem;
	    }

	    public ArrayList<Personnel> getPersonnel(IWorkflowInstance instance) {
	        ArrayList<Personnel> SupPerson = new ArrayList<Personnel>();
	        try {
	            connection = (Connection)SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
	            IUser rh = getWorkflowModule().getLoggedOnUser();
	            String filiale = "";
	            if(rh.getOrganization().getLabel().equals("Attijari Finance Corporate")){
	            	filiale = "attijarifinancescorp";
	            }
	            else{
	            	filiale = "attijariintermediation";
	            }
	            String query = "select P.Matricule, P.nom, P.prenom from Personnel P where  P.FilialeIdFiliale = ? ";
	            st = connection.prepareStatement(query);
	            st.setString(1, filiale);
	            rs = st.executeQuery();
	            while (rs.next()) {
	            	Personnel PERSONNEL = new Personnel();
	           
	            	PERSONNEL.setMatricule(rs.getString(1));
	            	PERSONNEL.setNom(rs.getString(2));
	            	PERSONNEL.setPrenom(rs.getString(3));
	            	            	
	            	SupPerson.add(PERSONNEL);
	            }
	        }
	        catch (Exception e) {
	            log.error("CS:ATTIJARI:Error in getPersonnel method : " + e.getClass() + "e4 - " + e.getMessage());
	        }
	        return SupPerson;
	    }

	    public IConnectionDefinition<Connection> ConnectionDefinition(String Ref_externe) throws PortalModuleException {
	        this.ctx = this.getWorkflowModule().getContextByLogin("sysadmin");
	        return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
	    }

}
