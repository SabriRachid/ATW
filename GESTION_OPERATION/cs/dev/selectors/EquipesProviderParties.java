package cs.dev.selectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IWorkflow;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.runtime.INavigateContext;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.providers.BaseViewProvider;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.core.providers.ICollectionModelViewProvider;
import com.axemble.vdp.ui.framework.composites.base.CtlAbstractView;
import com.axemble.vdp.ui.framework.composites.base.models.views.CollectionViewModel;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelColumn;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelItem;
import cs.dev.selectors.ApplicationDataProvider;

/**
 * @author SABRI Rachid
 * CREATION DATE 17/17/2017
 * Version_VDOC 14
 */
public class EquipesProviderParties extends BaseViewProvider implements ICollectionModelViewProvider{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(EquipesProviderParties.class);
	IContext ctx;
	Connection connection;
	PreparedStatement st;
	
	public EquipesProviderParties(INavigateContext context, CtlAbstractView view) {
		super(context, view);
	}

	public void init() {
		super.init();
		// We get view model
		CollectionViewModel viewModel = (CollectionViewModel) getModel();
		// We build columns and add them to view model
		ViewModelColumn modelColumn = new ViewModelColumn("Membre", "Membre",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
	}

	
	@Override
	public List<ViewModelItem> getModelItems() {
		IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
		ArrayList<ViewModelItem> cViewModelItem = new ArrayList<ViewModelItem>();
		try {

			// We get the workflowInstance
			IWorkflowInstance workflowInstance = ApplicationDataProvider.workflowInstance;
			// we get Equipes list
			ArrayList<Equipes> EKIP = getEquipes(workflowInstance);
			for (Equipes  equipe : EKIP) {
				
				ViewModelItem viewModelItem = new ViewModelItem();			
				viewModelItem.setKey(equipe.getMembre_Equipe());
				
				viewModelItem.setValue("Membre", equipe.getMembre_Equipe());
				cViewModelItem.add(viewModelItem);
			}
		} catch (Exception e) {
			log.error("Erreur dans la méthode getModelItems : "+e.getClass()+" _ "+e.getMessage());
		} finally {
		
			Modules.releaseModule(iDirectoryModule);
  		}
		return cViewModelItem;
	}
	
	ArrayList<Equipes> getEquipes(IWorkflowInstance workflowInstance) {
		ArrayList<Equipes> EquipeList = new ArrayList<Equipes>();
		try {
			connection=ConnectionDefinition("Ref_Attijari").getConnection();
			String query = "SELECT Fullname FROM Collaborateur where RefDossier =? and Fullname not in (select Fullname from Collaborateur where login=?)";
			st = connection.prepareStatement(query);
			st.setString(1, ApplicationDataProvider.Dossier);
			st.setString(2, ApplicationDataProvider.LogInUser);
			ResultSet result = st.executeQuery();
			while (result.next()) {
				Equipes  KIPO = new Equipes();
				KIPO.setMembre_Equipe(result.getString(1));
				EquipeList.add(KIPO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			if (message == null) {
				message = "";
			}
			log.error("Erreur dans la méthode EquipesProvider_getEquipes  : "+ e.getClass() + "e4 - " + message);
		} finally {

		}

		return EquipeList;
	}
	public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    	ctx = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
    }
}
