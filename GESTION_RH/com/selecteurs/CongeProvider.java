package com.selecteurs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IUser;
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

import dao.SingletonConnexionBDD;


/**
 * @author R.SABRI
 * CREATION DATE 20/10/2015
 * Update 21-004-2016 18:14 -Selector Filter
 * VDOC 14
 */


@SuppressWarnings("deprecation")
public class CongeProvider extends BaseViewProvider implements ICollectionModelViewProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(CongeProvider.class);

	IContext ctx;
	Connection connection;
	PreparedStatement st;
	/**
	 * @param context
	 * @param view
	 */
	public CongeProvider(INavigateContext context, CtlAbstractView view) {
		super(context, view);
	}

	// La vu du selecteur
	@Override
	public void init() {
		super.init();
		// View model
		CollectionViewModel viewModel = (CollectionViewModel) getModel();
		// Cr�ation des colonnes dans le mod�le de la vue
		ViewModelColumn modelColumn = new ViewModelColumn("CongeID", "Num� du cong�",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("IdCongeNormal", "id de cong� normal",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("TypeConge", "Type de cong� normal",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("CongeNorDateDeb", "Date d�but cong� normal",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("CongeNorDateFin", "Date fin cong� normal", ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("IdCongeSpe", "id de cong� sp�cial",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("TypeCongeSpe", "Type de cong� sp�cial",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("CongeSpeDateDeb", "Date d�but cong� sp�cial",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		modelColumn = new ViewModelColumn("CongeSpeDateFin", "Date fin cong� sp�cial", ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		
	}


	@Override
	public List<ViewModelItem> getModelItems() {

		IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
		ArrayList<ViewModelItem> cViewModelItem = new ArrayList<ViewModelItem>();
		try {
			// We get the workflowInstance
			
			IWorkflowInstance workflowInstance = ApplicationDataProvider.workflowInstance;
			// we get articles list
			ArrayList<Conge> conges = getConges(workflowInstance);
			for (Conge conge : conges) {

				ViewModelItem viewModelItem = new ViewModelItem();
				// "SetKey" le champs qui s'affiche apr�s la s�l�ction 
				viewModelItem.setKey(conge.getCodeConge());
				// Affectation des valeurs
				viewModelItem.setValue("CongeID", conge.getCodeConge());
				//viewModelItem.setValue("IdCongeNormal", conge.getCongeIDNor());
				viewModelItem.setValue("TypeConge", conge.getTypeConNor());
				viewModelItem.setValue("CongeNorDateDeb", conge.getDateDebConNor());
				viewModelItem.setValue("CongeNorDateFin", conge.getDateFinConNor());
				//viewModelItem.setValue("IdCongeSpe", conge.getCongeIDSpe());
				viewModelItem.setValue("TypeCongeSpe", conge.getTypeConSpe());
				viewModelItem.setValue("CongeSpeDateDeb", conge.getDateDebConSpe());
				viewModelItem.setValue("CongeSpeDateFin", conge.getDateFinConSpe());

				cViewModelItem.add(viewModelItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			Modules.releaseModule(iDirectoryModule);
		}
		return cViewModelItem;
	}
	
	
	

	ArrayList<Conge> getConges(IWorkflowInstance workflowInstance) {

		ArrayList<Conge> CongeList = new ArrayList<Conge>();
		try {
			// D�claration de connexion 
			// R�specter le nom de la r�f�rence externe ici c'est "ref_ext_CIMAT"
			connection=SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			IUser user = getWorkflowModule().getLoggedOnUser();
			String login = user.getLogin();
			// Pr�parer la requette sql
			String query = "SELECT distinct n.CodeVdocDemandeConge,n.IdConge,n.TypeConge,n.DateDeb,n.DateFin,s.IdConge,s.TypeConge,s.DateDeb,s.DateFin  "
					+ "FROM Conge n,Conge s where (n.DateDeb>SYSDATETIME() ) and  n.Personnelmatricule=? and (n.EtatConge not like 'cong� annul�' and n.EtatConge not like 'cong� modifi�') and n.CodeVdocDemandeConge not like 'GCC%' and s.Personnelmatricule=n.Personnelmatricule and (n.DateFin = s.DateDeb and n.IdConge <> s.IdConge) "
					+ "union "
					+ "SELECT distinct n.CodeVdocDemandeConge,n.IdConge,n.TypeConge,n.DateDeb,n.DateFin,s.IdConge,s.TypeConge,s.DateDeb,s.DateFin  "
					+ "FROM Conge n,Conge s where (n.DateDeb>SYSDATETIME() ) and  s.TypeConge=n.TypeConge and (n.EtatConge not like 'cong� annul�' and n.EtatConge not like 'cong� modifi�') and n.CodeVdocDemandeConge not like 'GCC%' and n.Personnelmatricule=? and s.Personnelmatricule=n.Personnelmatricule and  ( n.IdConge = s.IdConge and "
					+ "(select COUNT(nn.CodeVdocDemandeConge) from Conge nn where nn.CodeVdocDemandeConge= n.CodeVdocDemandeConge )=1) ";
			st = connection.prepareStatement(query);
			st.setString(1, login);
			st.setString(2, login);
			ResultSet result = st.executeQuery();

			while (result.next()) { 
				Conge conge = new Conge();
				conge.setCodeConge(result.getString(1));
				conge.setCongeIDNor(result.getString(2));
				conge.setTypeConNor("Type du cong� : "+result.getString(3));
				conge.setDateDebConNor("Date d�but du cong� : "+result.getDate(4));
				conge.setDateFinConNor("Date fin du cong� : "+result.getDate(5));
				
				if(!result.getString(3).equals(result.getString(7))){
					conge.setCongeIDSpe(result.getString(6));
					conge.setTypeConSpe("Type du cong� sp�cial: "+result.getString(7));
					conge.setDateDebConSpe("Date d�but du cong� sp�cial : "+result.getDate(8));
					conge.setDateFinConSpe("Date fin du cong� sp�cial : "+result.getDate(9));
				}
				else{
//					conge.setCongeIDSpe("");
//					conge.setTypeConSpe("");
//					conge.setDateDebConSpe("");
//					conge.setDateFinConSpe("");
				}
				
				CongeList.add(conge);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} 

		return CongeList;
	}

	public String getMonthAndYearOfThisDate(Date d){
		String monthAndYear = "";
		int numMonth = d.getMonth();
		int numYear = d.getYear()+1900;
		switch(numMonth){
		case 0: monthAndYear = "JANVIER "+numYear;break;
		case 1: monthAndYear = "FEVRIER "+numYear;break;
		case 2: monthAndYear = "MARS "+numYear;break;
		case 3: monthAndYear = "APRIL "+numYear;break;
		case 4: monthAndYear = "MAI "+numYear;break;
		case 5: monthAndYear = "JUIN "+numYear;break;
		case 6: monthAndYear = "JUILLET "+numYear;break;
		case 7: monthAndYear = "AOUT "+numYear;break;
		case 8: monthAndYear = "SEPTEMBRE "+numYear;break;
		case 9: monthAndYear = "OCTOBRE "+numYear;break;
		case 10: monthAndYear = "NOVEMBRE "+numYear;break;
		case 11: monthAndYear = "DECEMBRE "+numYear;break;

		}
		return monthAndYear;
	}

	// Connexion DB
	@SuppressWarnings("unchecked")
	public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
	{
		ctx = getWorkflowModule().getContextByLogin("sysadmin");
		return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
	}
}

