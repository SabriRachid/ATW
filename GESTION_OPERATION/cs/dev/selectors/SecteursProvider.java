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
 * CREATION DATE 09/01/2018
 * Version_VDOC 14
 */


public class SecteursProvider extends BaseViewProvider implements ICollectionModelViewProvider {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(SecteursProvider.class);

    IContext ctx;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    /**
     * @param context
     * @param view
     */
    public SecteursProvider(INavigateContext context, CtlAbstractView view) {
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
        ViewModelColumn modelColumn = new ViewModelColumn("SecteurActivite", "Sécteurs d'activité",ViewModelColumn.TYPE_STRING);
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
            // we get Secteurs list
            ArrayList<Secteurs> List_Secteurs = getSecteurs(workflowInstance);
            for (Secteurs  Sect : List_Secteurs) {
                ViewModelItem viewModelItem = new ViewModelItem();			
                viewModelItem.setKey(Sect.getSecteur());
                viewModelItem.setValue("SecteurActivite", Sect.getSecteur());
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
     *  ARRAYLIST<SECTEURS> GETSECTEURS(IWORKFLOWINSTANCE WORKFLOWINSTANCE)
     * ==========================================================================================================
     */
    ArrayList<Secteurs> getSecteurs(IWorkflowInstance workflowInstance) {
        connection = null;
        st = null;
        result = null;
        ArrayList<Secteurs> SecteursList = new ArrayList<Secteurs>();
        try {
            connection=ConnectionDefinition("Ref_Attijari").getConnection();
            String query = "Select Libelle_Secteur from Secteurs_Activite";
            st = connection.prepareStatement(query);
            result = st.executeQuery();
            while (result.next()) {
                Secteurs  secteur = new Secteurs();
                secteur.setSecteur(result.getString("Libelle_Secteur"));
                SecteursList.add(secteur);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) {
                message = "";
            }
            log.error("Erreur dans la méthode -Selecteur- getSecteurs - (Seleteur Secteur activite) : "+ e.getClass() + "e4 - " + message);
        }finally {
            // LIBÉRER RESSOURCES DE LA MÉMOIRE.
            ConnexionBDD.close(connection, st,result);
        }
        return SecteursList;
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


