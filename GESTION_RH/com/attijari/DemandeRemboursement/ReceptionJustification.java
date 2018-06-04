package com.attijari.DemandeRemboursement;

import java.sql.Connection;

import com.axemble.vdoc.sdk.interfaces.IAction;

import dao.SingletonConnexionBDD;

public class ReceptionJustification extends ConnexionBDD
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection;
	
	public void updateDossier (String NumDoss){
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query ="update DossierMutuelle set etat ='Receptionner' where NumDossier = ?"; 
			st = connection.prepareStatement(query);
			st.setString(1, NumDoss);
			st.executeUpdate();

	}catch(Exception e)
	{
		e.printStackTrace();
	}
	
	}
	
	public void updateProfilDoss(String numDoss){
		
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query ="update  DossierMutuelle set  etat='Receptionner', statutValidation='En cours' where NumDossier = ?"; 
			st = connection.prepareStatement(query);
			st.setString(1, numDoss);
			st.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	@Override
	public boolean onAfterSubmit(IAction action){
		
		if(action.getName().equals("Receptionner")){
			String NumDoss = (String) getWorkflowInstance().getValue("DRM_Numero");
			String testProfil = (String) getWorkflowInstance().getValue("DRM_TestProfil");
			
			
			if(testProfil!=null){
				updateProfilDoss(NumDoss);
			}else{
				updateDossier(NumDoss);
			}
		}
		
		
//		// BOITE DE DIALOGE : CONFIRMATION DE LA SUPPRESSION CLIENT
//        getResourceController().confirm("Voulez vous supprimer ce client ?", new ConfirmBoxListener() {
//            public boolean c = false;
//        	@Override
//            public void onOk(ActionEvent arg0) {
//        		
//        	}
//            @Override
//            public void onCancel(ActionEvent arg0) {
//            	
//            }
//        });
		return super.onAfterSubmit(action);
	}
	
	
	
}
