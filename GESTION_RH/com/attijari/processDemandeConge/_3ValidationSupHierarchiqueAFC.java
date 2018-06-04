package com.attijari.processDemandeConge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IOperator;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.modules.IPortalModule;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;

import dao.SingletonConnexionBDD;

public class _3ValidationSupHierarchiqueAFC extends BaseDocumentExtension
{
	
	private static final long serialVersionUID = -7501902336230684835L;
	private IContext ctx;
	private Connection connection;
	private PreparedStatement st;
	private String btnEnvoyerNS;
	private String btnRefuserNS;
	private String alerteNS;
	private String reliquatSoldeanneEncoursNS;
	private String fragAlerteNS;
	private String commentaireNS;
	
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		//List<IUser> users = (List<IUser>) getWorkflowInstance().getValue("sys_CurrentActors");
		
		
		try
		{
			
			fragAlerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("FRAG_ALERTSUP");
			btnEnvoyerNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALSUP_BTNVALIDER");
			btnRefuserNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALSUP_BTNREFUSER");
			reliquatSoldeanneEncoursNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_SOLDEANNENCOURSAPMODIF");
			commentaireNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("VALSUP_COMM");
			alerteNS = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("DEMCON_ALERT"); 
			
			float resteConge = (float) getWorkflowInstance().getValue(reliquatSoldeanneEncoursNS);
			if(resteConge<0){
				getResourceController().setMandatory(commentaireNS, true);
				getWorkflowInstance().setValue(alerteNS, "solde négatif");
				getResourceController().showBodyBlock(fragAlerteNS, true);
			}
			else{
				getResourceController().setMandatory(commentaireNS, false);
				getWorkflowInstance().setValue(alerteNS, "");
				getResourceController().showBodyBlock(fragAlerteNS, false);
			}
			
			
			
		
//			IContext ctx = getWorkflowModule().getContext(creator);
//			IAction action = getWorkflowModule().getAction(ctx, getWorkflowInstance().getCurrentTaskInstance(ctx).getTask(), "VALSUP_BTNVALIDER2");
			List<IOperator> operators = (List<IOperator>) getWorkflowInstance().getCurrentTaskInstance(ctx).getOperators();
			
			if(operators.size()==4){
				
				IUser loggedOnUser = getWorkflowModule().getLoggedOnUser();
				IUser creator = (IUser) getWorkflowInstance().getValue("sys_Creator");
				List<IUser> users = getSuperieurOf(creator.getLogin(), Modules.getPortalModule(), Modules.getWorkflowModule());
				users.remove(loggedOnUser);
				getWorkflowInstance().setValue("SUPERIEUR_HIERARCHIQUE", users);
				getWorkflowInstance().save("SUPERIEUR_HIERARCHIQUE");
				
//				IOperator operator = getWorkflowModule().getOperatorByLogin(loggedOnUser.getLogin());
//				getWorkflowInstance().getCurrentTaskInstance(ctx).removeOperator(operator);
			}
			if(operators.size()==1){
				IUser loggedOnUser = getWorkflowModule().getLoggedOnUser();
				IUser creator = (IUser) getWorkflowInstance().getValue("sys_Creator");
				List<IUser> users = getSuperieurOf(creator.getLogin(), Modules.getPortalModule(), Modules.getWorkflowModule());
				users.remove(loggedOnUser);
				getWorkflowInstance().setValue("SUPERIEUR_HIERARCHIQUE", users);
				getWorkflowInstance().save("SUPERIEUR_HIERARCHIQUE");
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.onAfterLoad();
	}
	
	public List<IUser> getSuperieurOf(String matriculeUser,IPortalModule ip, IWorkflowModule im){
		List<IUser> superieurs = new ArrayList<IUser>();
		try{
			String loginVdocOfUser = matriculeUser;
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(im,ip).getConnection();
			String req = "select p.loginVdoc "
					+ "from Superieur s , Personnel p , Personnel sup  "
					+ "where sup.loginVdoc = ? "
					+ "and s.PersonnelMatricule=sup.matricule "
					+ "and s.SupMatricule=p.matricule";
			st = connection.prepareStatement(req);
			st.setString(1, loginVdocOfUser);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				String loginSuperieur = rs.getString(1);
				IUser superieur = im.getUserByLogin(loginSuperieur);
				superieurs.add(superieur);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return superieurs;
	}
	
			@SuppressWarnings({
				"unused"
		})
		@Override
		public boolean onBeforeSubmit(IAction action)
		{
			try
			{
		//		IUser u1 = getWorkflowModule().getUserByLogin("m.berrada");
		//		IUser u2 = getWorkflowModule().getUserByLogin("f.squalli");
		//		IUser u3 = getWorkflowModule().getUserByLogin("d.nejjar");
		//		List<IUser> u = new ArrayList<IUser>();
		//		u.add(u1);
		//		u.add(u2);
		//		u.add(u3);
		//		getWorkflowInstance().setValue("Validateur2", u);
		//		getWorkflowInstance().save("Validateur2");
				
				if(action.getName().equals("VALSUP_BTNVALIDER2")){
					IUser loggedOnUser = getWorkflowModule().getLoggedOnUser();
		//			IUser creator = (IUser) getWorkflowInstance().getValue("sys_Creator");
		//			List<IUser> users = getSuperieurOf(creator.getLogin(), Modules.getPortalModule(), Modules.getWorkflowModule());
		//			users.remove(loggedOnUser);
		//			getWorkflowInstance().setValue("SUPERIEUR_HIERARCHIQUE", users);
		//			getWorkflowInstance().save("SUPERIEUR_HIERARCHIQUE");
					List<IOperator> operators = (List<IOperator>) getWorkflowInstance().getCurrentTaskInstance(ctx).getOperators();
					if(operators.size()==3){
						IOperator operator = getWorkflowModule().getOperatorByLogin(loggedOnUser.getLogin());
					getWorkflowInstance().getCurrentTaskInstance(ctx).addOperator(operator);
					}
					
					operators = (List<IOperator>) getWorkflowInstance().getCurrentTaskInstance(ctx).getOperators();
				}
				
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
	
	return super.onBeforeSubmit(action);
}
	
	@SuppressWarnings({
			"unused"
	})
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		try
		{
//			IUser u1 = getWorkflowModule().getUserByLogin("m.berrada");
//			IUser u2 = getWorkflowModule().getUserByLogin("f.squalli");
//			IUser u3 = getWorkflowModule().getUserByLogin("d.nejjar");
//			List<IUser> u = new ArrayList<IUser>();
//			u.add(u1);
//			u.add(u2);
//			u.add(u3);
//			getWorkflowInstance().setValue("Validateur2", u);
//			getWorkflowInstance().save("Validateur2");
			
			if(!action.getName().equals("VALSUP_BTNVALIDER2")){
				IUser loggedOnUser = getWorkflowModule().getLoggedOnUser();
//				IUser creator = (IUser) getWorkflowInstance().getValue("sys_Creator");
//				List<IUser> users = getSuperieurOf(creator.getLogin(), Modules.getPortalModule(), Modules.getWorkflowModule());
//				users.remove(loggedOnUser);
//				getWorkflowInstance().setValue("SUPERIEUR_HIERARCHIQUE", users);
//				getWorkflowInstance().save("SUPERIEUR_HIERARCHIQUE");
				List<IOperator> operators = (List<IOperator>) getWorkflowInstance().getCurrentTaskInstance(ctx).getOperators();
				if(operators.size()==3){
					IOperator operator = getWorkflowModule().getOperatorByLogin(loggedOnUser.getLogin());
				getWorkflowInstance().getCurrentTaskInstance(ctx).addOperator(operator);
				}
				
				operators = (List<IOperator>) getWorkflowInstance().getCurrentTaskInstance(ctx).getOperators();
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return super.onAfterSubmit(action);
	}
}
