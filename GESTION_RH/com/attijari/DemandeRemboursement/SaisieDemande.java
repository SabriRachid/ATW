package com.attijari.DemandeRemboursement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;

import dao.SingletonConnexionBDD;

public class SaisieDemande extends ConnexionBDD
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -842439404291044478L;
	public String Matricule;
	private Connection connection;
	
	
	public String getDemandeur (String CMIM){
		String Benef = "";
		try {
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select nom+' '+prenom from Personnel where NCMIM= ? "
					+ "union select nom+' '+prenom from Conjoint where NCMIM= ? "
					+ "union select nom+' '+prenom from Enfant where NCMIM= ?";
			st=connection.prepareStatement(req1);
			st.setString(1, CMIM);
			st.setString(2, CMIM);
			st.setString(3, CMIM);
			ResultSet rs1= st.executeQuery();
			while(rs1.next()){
				Benef = rs1.getString(1);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Benef;
	}
	
	public boolean ExisteProfil (){
		
		String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
		int cpt = 0;
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select count(*) as cpt from DossierMutuelle where Personnelmatricule = ? and ProfilDossier='Demande daccord' and statutValidation ='Dossier accordé' and etat ='Accorder'";
			st=connection.prepareStatement(req1);
			st.setString(1, Matricule);
			ResultSet rs1= st.executeQuery();
			while (rs1.next()){
				cpt = rs1.getInt(1);
			}
			
			if(cpt>0)
				return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void getDossierDemandDaccord(){
		
		String Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req1 = "select DM.NumDossier, DM.type, DB.BeneficiaireNCMIM, DM.honoraires, DM.pharmacie, DM.divers , DM.totalARembourser "
					+ "from DossierMutuelle DM, DossierBeneficiaire DB "
					+ "where DM.NumDossier=DB.DossierMutuelleNumDossier and DM.Personnelmatricule = ? and DM.ProfilDossier= 'Demande daccord' and statutValidation ='Dossier accordé'";
			st=connection.prepareStatement(req1);
			st.setString(1, Matricule);
			rs= st.executeQuery();
			while (rs.next()){
			
				getWorkflowInstance().setValue("DRM_Numero", rs.getString(1));
				getWorkflowInstance().setValue("DRM_Type", rs.getString(2));
				getWorkflowInstance().setValue("DRM_beneficiaire", rs.getString(3));
				getWorkflowInstance().setValue("DRM_Honoraire", rs.getFloat(4));
				getWorkflowInstance().setValue("DRM_Pharmacie", rs.getFloat(5));
				getWorkflowInstance().setValue("DRM_Divers", rs.getFloat(6));
				getWorkflowInstance().setValue("DRM_Total", rs.getFloat(7));
				getWorkflowInstance().setValue("DRM_TypeSoin", "Soin Prothèse médicaux");
			}
			
			getResourceController().setEditable("DRM_Numero", false);
			getResourceController().setEditable("DRM_Type", false);
			getResourceController().setEditable("DRM_TypeSoin", false);
			getResourceController().setEditable("DRM_beneficiaire", false);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.ConnexionBDD.close(connection, st, rs);
		}
		
	}
	
	public String getCMIMpersonnel (String Matirucle){
		
		String CMIM_P = null;
		try{
		connection =SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String req1 = "select NCMIM from Personnel where matricule = ?";
		st=connection.prepareStatement(req1);
		st.setString(1, Matirucle);
		ResultSet rs1= st.executeQuery();
		while (rs1.next()){
			CMIM_P= rs1.getString(1);
			
		}	
		
		}catch(Exception e){
			
			e.printStackTrace();
		}

		
		return CMIM_P;

	}
	
	public String getCMIMConjoint(String Matricule){
		
		String CMIM_C = null;
		try{
		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String req1 = "select NCMIM from Personnel Matricule = ?";
		st=connection.prepareStatement(req1);
		st.setString(1, Matricule);
		ResultSet rs1= st.executeQuery();
		while (rs.next()){
			CMIM_C= rs1.getString(1);
			
		}	
		
		}catch(Exception e){
			
			e.printStackTrace();
		}

		
		return CMIM_C;
		
		
	}
	
	public String getMatricule(String NomPrenom)
	{
		String MatriculeUpdate = null;
		
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String req = "select matricule from Personnel where Prenom+' '+Nom= ? ";
			st = connection.prepareStatement(req);
			st.setString(1, NomPrenom);
			ResultSet rs = st.executeQuery();
			while (rs.next())
			{
				MatriculeUpdate = rs.getString(1);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return MatriculeUpdate;
	}

	public void addDossierMutuelle (String NumDossier, String Type, Date dateDemande, String matricule, float Honoraire, float pharmacie, float divers, float total, String profilD){
		try{
		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String query = "insert into DossierMutuelle(NumDossier,etat,statutValidation,statutRapprotValidation,type,Montant,DateDemande,Personnelmatricule,honoraires,pharmacie"
				+ ",divers,totalARembourser,ProfilDossier,MntLettre,dateRemboursement) values"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		st = connection.prepareStatement(query);
		st.setString(1, NumDossier);
		st.setString(2, "non receptionner");
		st.setString(3, "En cours");
		st.setString(4, null);
		st.setString(5,Type);
		st.setFloat(6,0f);
		java.sql.Date dateD = new java.sql.Date(dateDemande.getTime());
		st.setDate(7, (java.sql.Date) dateD);
		st.setString(8, matricule);
		st.setFloat(9, Honoraire);
		st.setFloat(10, pharmacie);
		st.setFloat(11, divers);
		st.setFloat(12, total);
		st.setString(13, profilD);
		st.setString(14, null);
		st.setString(15, null);

		st.executeUpdate();
		
		
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void addDossierBeneficiaire (String numDossier, String NCMIM, String TypeB){
		
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query = "insert into DossierBeneficiaire values (?,?,?)";
			st = connection.prepareStatement(query);
			st.setString(1, numDossier);
			st.setString(2, NCMIM);
			st.setString(3,TypeB );
			st.executeUpdate();
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		
		if(action.getName().equals("Envoyer")){
		try{
		String numDossier = (String) getWorkflowInstance().getValue("DRM_Numero");
		
		Date date = (Date) getWorkflowInstance().getValue("DRM_DateDemande"); 
		float Honoraire = getWorkflowInstance().getValue("DRM_Honoraire")!=null ? (float) getWorkflowInstance().getValue("DRM_Honoraire") : 0;
		float Pharmacie = getWorkflowInstance().getValue("DRM_Pharmacie")!=null ? (float) getWorkflowInstance().getValue("DRM_Pharmacie") : 0;
		float Divers = getWorkflowInstance().getValue("DRM_Divers")!=null ? (float) getWorkflowInstance().getValue("DRM_Divers") : 0;
		float Total = (float) getWorkflowInstance().getValue("DRM_Total");
		
		String NCMIM = (String) getWorkflowInstance().getValue("DRM_beneficiaire");
	
//		IUser iUser = getWorkflowModule().getLoggedOnUser();
//		String Utilisateur = iUser.getFullName();
		
		
		String typeSoin = (String) getWorkflowInstance().getValue("DRM_TypeSoin");
		String ProfilD ="";
		String testProfil = (String) getWorkflowInstance().getValue("DRM_TestProfil");
		
		if(testProfil!=null){
			
			updateProfilDoss(numDossier);
			
		}else {
			

			if(typeSoin!=null){
			if(typeSoin.equals("Prothèse médicales")){
					ProfilD="Demande daccord";
				}else{
					 ProfilD="Demande de remboursement";
				}
				
			}else
				ProfilD="Demande de remboursement";
			String Type = (String) getWorkflowInstance().getValue("DRM_Type");
			String Type2 = (String) getWorkflowInstance().getValue("DRM_Type2");
			
			String typeDemande = (String) getWorkflowInstance().getValue("DRM_Type_demande");
			
			if(typeDemande.equals("Demande de remboursement")){
				addDossierMutuelle(numDossier, Type, date, Matricule,Honoraire,Pharmacie,Divers,Total, ProfilD);
			}else{
				addDossierMutuelle(numDossier, Type2, date, Matricule,Honoraire,Pharmacie,Divers,Total, ProfilD);
			}
			
			
			
			if(NCMIM.equals(getCMIMpersonnel(Matricule))){
				String TypeB = "A";
				addDossierBeneficiaire(numDossier, NCMIM, TypeB);
				
				
			}else if(NCMIM.equals(getCMIMpersonnel(Matricule))){
				String TypeB = "C";
				addDossierBeneficiaire(numDossier, NCMIM, TypeB);
				
			}else{
				String TypeB = "E";
				addDossierBeneficiaire(numDossier, NCMIM, TypeB);
						
			}
		}
		
		//addDossierBeneficiaire(numDossier, NCMIM);
	

		}catch (Exception e){
			e.printStackTrace();
		}
		
		}
		// TODO Auto-generated method stub
		return super.onAfterSubmit(action);
	}

	@Override
	public boolean onBeforeSubmit(IAction action){
		
		if(action.getName().equals("Envoyer")){
		float total = (float) getWorkflowInstance().getValue("DRM_Total");
		
		if(total == 0){
			getResourceController().alert("vous ne pouvez pas ajouter un dossier avec le total 0");
			return false;
		}
		
		
		
		
		try{
		String numDossier = (String) getWorkflowInstance().getValue("DRM_Numero");
		String NV_ANC = (String) getWorkflowInstance().getValue("DRM_Nouveau");
		String dossier = "";
		String profilDemande ="";
		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String query = "select NumDossier,ProfilDossier from DossierMutuelle where NumDossier like ? ";//and (ProfilDossier like 'Demande de remboursement')";
		st = connection.prepareStatement(query);
		st.setString(1, numDossier);
		ResultSet rslt = st.executeQuery();
		boolean test = true;
		while (rslt.next()){
			dossier = rslt.getString(1);
			profilDemande = rslt.getString(2);
			
			if(dossier.equals(numDossier)){
				if("Nouvelle demande".equals(NV_ANC)){
					getResourceController().alert("Ce dossier est déjà ajouté veuillez vérifier le numéro de dossier ");
					test = false;
					break;
				}
				else if("Ancienne demande".equals(NV_ANC) && profilDemande.equals("Demande de remboursement") ){
					getResourceController().alert("Ce dossier est déjà ajouté veuillez vérifier le numéro de dossier ");
					test = false;
					break;
				}
				else if(NV_ANC==null){
					getResourceController().alert("Ce dossier est déjà ajouté veuillez vérifier le numéro de dossier ");
					test = false;
					break;
				}
				
			}		
		}
		return test;
		
			
	}catch(Exception e){
		e.printStackTrace();	}
		
		}
	
		return super.onBeforeSubmit(action);
	}

	public void updateProfilDoss(String numDoss){
		
		try{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
			String query ="update  DossierMutuelle set  ProfilDossier = 'Demande de remboursement' where NumDossier = ?"; 
			st = connection.prepareStatement(query);
			st.setString(1, numDoss);
			st.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	@Override
	public boolean onAfterLoad(){
		
		String ipAdresse = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
		getWorkflowInstance().setValue("URI", "http://"+ipAdresse+"/vdoc"+getWorkflowInstance().getURI());
		
		
	Matricule = getWorkflowModule().getLoggedOnUser().getEmployeeNumber();
	String Filiale = "";
	try{
		connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(getWorkflowModule(),getPortalModule()).getConnection();
		String req1 = "select FilialeIdFiliale from Personnel where Matricule like ?";
		st=connection.prepareStatement(req1);
		st.setString(1, Matricule);
		ResultSet rs1= st.executeQuery();
		while (rs1.next()){
			Filiale= rs1.getString(1);
			}
		List<IUser> users = new ArrayList<>();
		
		if(Filiale.equals("attijarifinancescorp")){
			IUser AFC = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AST_AFC"));
			users.add(AFC);
			getWorkflowInstance().setValue("DR_Assistante", users);
			getWorkflowInstance().save("DR_Assistante");
		}
		else if(Filiale.equals("attijariintermediation")){
			IUser ATI = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("AST_ATI"));
			users.add(ATI);
			getWorkflowInstance().setValue("DR_Assistante", users);
			getWorkflowInstance().save("DR_Assistante");
		}
		
		
		
		
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
		
		
		return super.onAfterLoad();
	}

	@Override
	public void onPropertyChanged(IProperty property) {
		if (property.getName().equals("DRM_Nouveau")){
			String NV_ANC = (String) getWorkflowInstance().getValue("DRM_Nouveau");
			if(NV_ANC!=null)
			if(NV_ANC.equals("Ancienne demande")){
				
				if(ExisteProfil()){
					getDossierDemandDaccord();
					getWorkflowInstance().setValue("DRM_Type2", "Dentaire");
					
					//getResourceController().setHidden("DRM_TypeSoin", true);
					}
					else{
						getResourceController().alert("Vous n'avez aucune demande d'accord a traité");
						getWorkflowInstance().setValue("DRM_Nouveau", "Nouvelle demande");
					}
				String numdoss= (String)getWorkflowInstance().getValue("DRM_Numero");
				if(numdoss!= null)
				{
					getWorkflowInstance().setValue("DRM_TestProfil", "DA--->DE");
				}
			}else{
				
				getResourceController().setMandatory("DRM_Numero", true); // Obligatoir true / fase
				getResourceController().setEditable("DRM_Numero", true); // Editable true / false
				getWorkflowInstance().setValue("DRM_Numero", null); 
				
				getResourceController().setMandatory("DRM_Type", true); // Obligatoir true / fase
				getResourceController().setEditable("DRM_Type", true); // Editable true / false
				getWorkflowInstance().setValue("DRM_Type", null); 
				
				getResourceController().setMandatory("DRM_TypeSoin", true); // Obligatoir true / fase
				getResourceController().setEditable("DRM_TypeSoin", false); // Editable true / false
				getWorkflowInstance().setValue("DRM_TypeSoin", "Prothèse médicales"); 
				getWorkflowInstance().setValue("DRM_Type2", "Dentaire");
//				getResourceController().setHidden("DRM_Type2", true);
				getResourceController().setHidden("DRM_TypeSoin", true);
			
				
				getResourceController().setMandatory("DRM_beneficiaire", true); // Obligatoir true / fase
				getResourceController().setEditable("DRM_beneficiaire", true); // Editable true / false
				getWorkflowInstance().setValue("DRM_beneficiaire", null); 	
				
				
				getWorkflowInstance().setValue("DRM_Honoraire", 0f);
				getWorkflowInstance().setValue("DRM_Pharmacie", 0f);
				getWorkflowInstance().setValue("DRM_Divers", 0f);
				getWorkflowInstance().setValue("DRM_Total", 0f);
//				getResourceController().setMandatory("DRM_Numero", true); // Obligatoir true / fase
//				getResourceController().setEditable("DRM_Numero", true); // Editable true / false
			
			}
		}
		
		if (property.getName().equals("DRM_Type")){
			String Type= (String) getWorkflowInstance().getValue("DRM_Type");
			if(Type!=null)
			if(Type.equals("Dentaire")){
				getWorkflowInstance().setValue("DRM_TypeSoin", "Dentaires et pharmaceutiques");
				getResourceController().setEditable("DRM_TypeSoin", false);
				
			}
			
		}
		
		
		if (property.getName().equals("DRM_Type_demande")){
			String Type_Demande = (String) getWorkflowInstance().getValue("DRM_Type_demande");
			if(Type_Demande.equals("Demande de remboursement")){
				getWorkflowInstance().setValue("DRM_TypeSoin", "Dentaires et pharmaceutiques");
				//getResourceController().showBodyBlock("DRM_TypeSoin", false);
				//getResourceController().setEditable("DRM_TypeSoin", false);
				getWorkflowInstance().setValue("DRM_Type", null);
				getWorkflowInstance().setValue("DRM_Type2", null);
				getWorkflowInstance().setValue("DRM_Nouveau", null);
				getWorkflowInstance().setValue("DRM_Numero", null);
				getWorkflowInstance().setValue("DRM_beneficiaire", null);
				getWorkflowInstance().setValue("DRM_TypeSoin", null);
				getResourceController().setEditable("DRM_Numero", true);
//		
			}
			if(Type_Demande.equals("Demande d'accord")){
				getWorkflowInstance().setValue("DRM_Type", null);
				//getResourceController().setHidden("DRM_Type", true);
				getResourceController().showBodyBlock("DRM_Type", true);
				getWorkflowInstance().setValue("DRM_Nouveau", null);
				getWorkflowInstance().setValue("DRM_Numero", null);
				getWorkflowInstance().setValue("DRM_beneficiaire", null);
				getWorkflowInstance().setValue("DRM_TypeSoin", null);
				getWorkflowInstance().setValue("DRM_Type2", null);
//				
				getResourceController().setEditable("DRM_Numero", true);
			}
		}
		
		
	
		
		if(property.getName().equals("DR_NBRJOURS")){
			
			int Nbrjour =  getWorkflowInstance().getValue("DR_NBRJOURS") == null ? 0 :  (int) getWorkflowInstance().getValue("DR_NBRJOURS");
			
			if(Nbrjour > 90 ){
				getResourceController().alert("votre dossier a depassé 3 mois, ne sera pas accepté par le CMIM");
				getWorkflowInstance().setValue("DR_Date_Acte", null );
				
			}
		}
		
		if(property.getName().equals("DRM_beneficiaire")){
			String CMIM = (String) getWorkflowInstance().getValue("DRM_beneficiaire");
			getWorkflowInstance().setValue("DRM_NOMPRENOM_Benif", " - "+ getDemandeur(CMIM));
		}
		
		
	super.onPropertyChanged(property);
}
}
