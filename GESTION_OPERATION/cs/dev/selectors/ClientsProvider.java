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
import dao.ConnexionBDD;

/**
 * @author SABRI Rachid
 * CREATION DATE 07/01/2017
 * Version_VDOC 14
 */


public class ClientsProvider extends BaseViewProvider implements ICollectionModelViewProvider {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ClientsProvider.class);

    IContext ctx;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    /**
     * @param context
     * @param view
     */
    public ClientsProvider(INavigateContext context, CtlAbstractView view) {
        super(context, view);
    }
    /**
     * ==========================================================================================================
     * INIT()
     * ==========================================================================================================
     */
    public void init() {
        super.init();
        // We get view model
        CollectionViewModel viewModel = (CollectionViewModel) getModel();
        // We build columns and add them to view model
        ViewModelColumn modelColumn = new ViewModelColumn("Nom_Client", "Nom Client",ViewModelColumn.TYPE_STRING);
        viewModel.addColumn(modelColumn);
    }
    /**
     * ==========================================================================================================
     * LIST<VIEWMODELITEM> GETMODELITEMS()
     * ==========================================================================================================
     */
    public List<ViewModelItem> getModelItems() {
        IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
        ArrayList<ViewModelItem> cViewModelItem = new ArrayList<ViewModelItem>();
        try {
            // We get the workflowInstance
            IWorkflowInstance workflowInstance = ApplicationDataProvider.workflowInstance;
            // we get Clients list
            ArrayList<Clients> Clients = getClients(workflowInstance);
            for (Clients  Client : Clients) {
                ViewModelItem viewModelItem = new ViewModelItem();			
                viewModelItem.setKey(Client.getNomClient());
                viewModelItem.setValue("Nom_Client", Client.getNomClient());
                cViewModelItem.add(viewModelItem);
            }
        } catch (Exception e) {
            log.error("Erreur dans la méthode getModelItems : "+e.getClass()+" _ "+e.getMessage());
        } finally {

            Modules.releaseModule(iDirectoryModule);
        }
        return cViewModelItem;
    }
    /**
     * ==========================================================================================================
     *  ARRAYLIST<CLIENTS> GETCLIENTS(IWORKFLOWINSTANCE WORKFLOWINSTANCE)
     * ==========================================================================================================
     */
    ArrayList<Clients> getClients(IWorkflowInstance workflowInstance) {
        connection = null;
        st = null;
        result = null;
        ArrayList<Clients> ClientsList = new ArrayList<Clients>();
        try {
            connection=ConnectionDefinition("Ref_Attijari").getConnection();
            String query = "SELECT Cl_NomClient FROM Clients";
            st = connection.prepareStatement(query);
            result = st.executeQuery();
            while (result.next()) {
                Clients  Client = new Clients();
                Client.setNomClient(result.getString("Cl_NomClient"));
                ClientsList.add(Client);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "";
            }
            log.error("Erreur dans la méthode ClientsProvider_getClients - (Seleteur Client) : "+ e.getClass() + "e4 - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(connection, st,result);
        }
        return ClientsList;
    }
    /**
     * ==========================================================================================================
     * ICONNECTIONDEFINITION
     * ==========================================================================================================
     */
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
        ctx = getWorkflowModule().getContextByLogin("sysadmin");
        return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
    }
}


