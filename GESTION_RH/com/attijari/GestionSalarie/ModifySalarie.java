package com.attijari.GestionSalarie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IResourceDefinition;
import com.axemble.vdoc.sdk.interfaces.ISecurityController;
import com.axemble.vdoc.sdk.interfaces.ISecurityController.IPermissionFlags.TablePermissionFlags;
import com.axemble.vdoc.sdk.interfaces.ISecurityController.IPermissionLevel;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdp.ui.core.document.fields.ResourceTableField;
import com.axemble.vdp.ui.framework.widgets.CtlButton;
import com.beans.attijariRh.CarteNational;
import com.beans.attijariRh.Conjoint;
import com.beans.attijariRh.Passeport;
import com.beans.attijariRh.Telephone;
import com.genererRapport.GenererPDF;
import com.serviceRH.ServiceFicheSalarie;
import com.serviceRH.ServiceRH;

import dao.SingletonConnexionBDD;

public class ModifySalarie extends ConnexionBDD
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		
		String ipAdresse = getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("IPADRESSE");
		getWorkflowInstance().setValue("URI", "http://"+ipAdresse+"/vdoc"+getWorkflowInstance().getURI());
		
		IUser connectedUser = getWorkflowModule().getLoggedOnUser();
		String filiale = connectedUser.getOrganization().getLabel();
		String filialeID = connectedUser.getOrganization().getName().toLowerCase();
		getWorkflowInstance().setValue("P_GS_Filiale", filiale);
		
		try
		{
			IUser rhATI = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_ATI"));
			IUser rhAFC = getWorkflowModule().getUserByLogin(getWorkflowInstance().getCatalog().getConfiguration().getStringUserProperty("RH_AFC"));
			if (connectedUser.equals(rhAFC) || connectedUser.equals(rhATI))
			{
//				getResourceController().setEditable("P_GS_Collab", true);
//				getResourceController().setEditable("P_GS_Statut", true);
//				getResourceController().setEditable("P_GS_TAB_Enfants", true);
//				
				getResourceController().setMandatory("P_GS_Collab", true);
				getResourceController().setMandatory("P_GS_SupList", true);
				
			}
			else
			{
				getResourceController().showBodyBlock("CacheMatricule", false);
				
				//état civil
				getResourceController().setEditable("P_GS_SupList", false);
				getResourceController().setEditable("P_GS_Collab", false);
				getResourceController().setEditable("P_GS_Statut", false);
				getResourceController().setEditable("P_GS_Photo", false);
				getResourceController().setEditable("P_GS_Nom", false);
				getResourceController().setEditable("P_GS_NomJeuneFille", false);
				getResourceController().setEditable("P_GS_Prenom", false);
				getResourceController().setEditable("P_GS_DateNaissance", false);
				getResourceController().setEditable("P_GS_LieuNaiss", false);
				getResourceController().setEditable("P_GS_ListeNationalite", false);
				getResourceController().setEditable("P_GS_ListeNationalite2", false);
//				getResourceController().setEditable("P_GS_Nom_Conjoint", false);
//				getResourceController().setEditable("P_GS_PrenomConjoint", false);
//				getResourceController().setEditable("P_GS_DDN_Conjoint", false);
				getResourceController().setEditable("P_GS_Sexe", false);
				
				getResourceController().setEditable("P_GS_CIN", false);
				getResourceController().setEditable("P_GS_CMIM", false);
				getResourceController().setEditable("P_GS_CIMR", false);
				getResourceController().setEditable("P_GS_CNSS", false);
				
				getResourceController().setEditable("P_GS_TPoste", false);
				getResourceController().setEditable("P_GS_Descriptif_poste", false);
				getResourceController().setEditable("P_GS_Direction", false);
				getResourceController().setEditable("P_GS_D_embuche_groupe", false);
				getResourceController().setEditable("P_GS_Filiale", false);
				getResourceController().setEditable("P_GS_D_embuche_filiale", false);
				getResourceController().setEditable("P_GS_SupList", false);
				getResourceController().setEditable("P_GS_nbreConge", false);
				getResourceController().setEditable("P_GS_ReliquatAnneEnCours", false);
				getResourceController().setEditable("P_GS_ReliquatSoldeAnterieur", false);
				
				getResourceController().setEditable("Tab_Remun", false);
				
				
				getWorkflowInstance().setValue("P_GS_Collab", connectedUser);
			}
			List<IUser> users = new ArrayList<>();
			IUser rh = filialeID.equals("attijariintermediation") ? rhATI : rhAFC;
			users.add(rh);
			getWorkflowInstance().setValue("P_GS_Res_RH", users);
			getWorkflowInstance().save("P_GS_Res_RH");
			
			
		
//			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
//			IUser collaborateurSelectionne = (IUser) getWorkflowInstance().getValue("P_GS_Collab");
//			if(collaborateurSelectionne!=null){
//				String matricule = collaborateurSelectionne.getEmployeeNumber();
//				sfs.getSalarieWithHisMatricule(getWorkflowInstance(),matricule);
//			}
		}
		catch (WorkflowModuleException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onAfterLoad();
	}
	
	@Override
	public boolean isOnChangeSubscriptionOn(IProperty property)
	{
		// TODO Auto-generated method stub
		return super.isOnChangeSubscriptionOn(property);
	}
	
	@SuppressWarnings({
			"static-access", "unchecked"
	})
	@Override
	public void onPropertyChanged(IProperty property)
	{
		// TODO Auto-generated method stub
		
		if(property.getName().equals("P_GS_Photo")){
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			String matricule = (String) getWorkflowInstance().getValue("P_GS_MatriculeUpdate");
			try
			{
				List<IAttachment> photos = (List<IAttachment>) getWorkflowInstance().getValue("P_GS_Photo");
				if(photos.size()!=0){
					
					String image = sfs.copyImageTo(getWorkflowInstance(), "P_GS_Photo", matricule);
					getWorkflowInstance().setValue("P_GS_PhotoImg", "skins//ImageFicheSalarie//"+image);
//					getWorkflowInstance().setValue("P_GS_Photo", null);
//					getWorkflowModule().addAttachment(getWorkflowInstance(), "P_GS_Photo", image, "C://VDocPlatform//JBoss//server//all//deploy//vdoc.ear//vdoc.war//skins//ImageFicheSalarie//"+image);
				}
				else{
					getWorkflowInstance().setValue("P_GS_PhotoImg", "skins//ImageFicheSalarie//anonyme.png");
//					getWorkflowInstance().setValue("P_GS_Photo", null);
					//getWorkflowModule().addAttachment(getWorkflowInstance(), "P_GS_Photo", "anonyme.png", "skins//default//images//anonyme.png");
					
				}
				
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		if(property.getName().equals("P_GS_Matricule")||property.getName().equals("P_GS_RIB")||property.getName().equals("P_GS_CIN")
//				||property.getName().equals("P_GS_passePort")||property.getName().equals("P_GS_CMIM")||property.getName().equals("P_GS_CIMR")
//				||property.getName().equals("P_GS_CNSS")||property.getName().equals("P_GS_CMIM_Conjoint")||property.getName().equals("P_GS_CINConjoint")){
//			String number = (String) getWorkflowInstance().getValue(property.getName());
//			if(sfs.ifThisNumberExist(number)){
//				getResourceController().alert(property.getLabel()+" existe déjà ...");
//			}
//		}
//		else if(property.getName().equals("P_GS_Nom")||property.getName().equals("P_GS_Prenom")){
//			String nom = (String) getWorkflowInstance().getValue("P_GS_Nom");
//			String prenom = (String) getWorkflowInstance().getValue("P_GS_Prenom");
//			String loginVdoc = ServiceFicheSalarie.getLoginVdoc(nom, prenom);
//			String email = loginVdoc+"@attijari.ma";
//			getWorkflowInstance().setValue("P_GS_VdocLogin", loginVdoc.toLowerCase());
//			getWorkflowInstance().setValue("P_GS_Mail", email.toLowerCase());
//		}
		
		/*else*/ 
		if(property.getName().equals("P_GS_Collab")){
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			IUser collaborateurSelectionne = (IUser) getWorkflowInstance().getValue(property.getName());
			if(collaborateurSelectionne!=null){
				String matricule = collaborateurSelectionne.getEmployeeNumber();
				sfs.getSalarieWithHisMatricule(getWorkflowInstance(),matricule);
			}else{
				getWorkflowInstance().setValue("P_GS_MatriculeUpdate", null);
				getWorkflowInstance().setValue("P_GS_Matricule", null);
				getWorkflowInstance().setValue("P_GS_PhotoImg", "skins\\ImageFicheSalarie\\anonyme.png");
				getWorkflowInstance().setValue("P_GS_Nom", null);
				getWorkflowInstance().setValue("P_GS_NomJeuneFille", null);
				getWorkflowInstance().setValue("P_GS_Prenom", null);
				getWorkflowInstance().setValue("P_GS_DateNaissance", null);
				getWorkflowInstance().setValue("P_GS_LieuNaiss", null);
				getWorkflowInstance().setValue("P_GS_ListeNationalite", null);
				getWorkflowInstance().setValue("P_GS_ListeNationalite2", null);
				getWorkflowInstance().setValue("P_GS_ListeNationalite2PJ", null);
				getWorkflowInstance().setValue("P_GS_S_LFamilliale", null);
				getWorkflowInstance().setValue("P_GS_JUSTIFSITUATIONFAMILIALE", null);
				getWorkflowInstance().setValue("P_GS_Nom_Conjoint", null);
				getWorkflowInstance().setValue("P_GS_PrenomConjoint", null);
				getWorkflowInstance().setValue("P_GS_DDN_Conjoint", null);
				getWorkflowInstance().setValue("P_GS_TAB_Enfants", null);
				
				getWorkflowInstance().setValue("P_GS_RIB", null);
				getWorkflowInstance().setValue("P_GS_AGENCE", null);
				getWorkflowInstance().setValue("P_GS_ListeNationalite2PJ", null);
				getWorkflowInstance().setValue("P_GS_Adrs_Perso", null);
				getWorkflowInstance().setValue("P_GS_CIN", null);
				getWorkflowInstance().setValue("P_GS_CMIM", null);
				getWorkflowInstance().setValue("P_GS_CIMR", null);
				getWorkflowInstance().setValue("P_GS_CNSS", null);
				getWorkflowInstance().setValue("P_GS_date_expr_CIN", null);
				getWorkflowInstance().setValue("P_GS_CINPJ", null);
				
				getWorkflowInstance().setValue("P_GS_passePort", null);
				getWorkflowInstance().setValue("P_GS_date_Exper_passeport", null);
				getWorkflowInstance().setValue("P_GS_passePortPJ", null);
				getWorkflowInstance().setValue("P_GS_DateDelivrance", null);
				getWorkflowInstance().setValue("P_GS_CINConjoint", null);
				getWorkflowInstance().setValue("P_GS_CINConjointPJ", null);
				getWorkflowInstance().setValue("P_GS_CMIM_Conjoint", null);
				
				getWorkflowInstance().setValue("P_GS_tel1", null);
				getWorkflowInstance().setValue("P_GS_Tel2", null);
				getWorkflowInstance().setValue("P_GS_Tel_Urgence", null);
				getWorkflowInstance().setValue("P_GS_Personne_urgence", null);
				getWorkflowInstance().setValue("P_GS_TPoste", null);
				getWorkflowInstance().setValue("P_GS_D_embuche_groupe", null);
				getWorkflowInstance().setValue("P_GS_Direction", null);
				getWorkflowInstance().setValue("P_GS_D_embuche_filiale", null);
				getWorkflowInstance().setValue("P_GS_VdocLogin", null);
				getWorkflowInstance().setValue("P_GS_Mail", null);
				getWorkflowInstance().setValue("P_GS_SupList", null);
				
				getWorkflowInstance().setValue("P_GS_ReliquatSoldeAnterieur", 0);
				getWorkflowInstance().setValue("P_GS_ReliquatAnneEnCours", 0);
				getWorkflowInstance().setValue("P_GS_nbreConge", 0);
				
				
				getWorkflowInstance().setValue("Tab_Remun", null);
				getWorkflowInstance().setValue("P_GS_TAB_diplome", null);
				getWorkflowInstance().setValue("P_GS_TAB_Experience", null);
				getWorkflowInstance().setValue("P_GS_TAB_Langues", null);
				getWorkflowInstance().setValue("P_GS_OutilsInformatiques", null);
				getWorkflowInstance().setValue("P_GS_LogicielsGestion", null);
				
				getWorkflowInstance().setValue("P_GS_Loisirs", null);
			}
			
		}
		super.onPropertyChanged(property);
	}
	
	
	@SuppressWarnings({
			"static-access", "unchecked"
	})
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals("Envoyer")){
			
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			
			
			
			String propertyValue = (String) getWorkflowInstance().getValue("P_GS_MatriculeUpdate");
			String matricule = (String) getWorkflowInstance().getValue("P_GS_MatriculeUpdate");
			
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_CINConjoint");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le numéro de CIN du conjoint !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_CMIM_Conjoint");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le NCMIM du conjoint !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_passePort");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le numéro de passport du collaborateur !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_CMIM");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le NCMIM du collaborateur !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_CIMR");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le NCIMR du collaborateur !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_CNSS");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le NCNSS du collaborateur !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_CIN");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer la CIN du collaborateur !!!");
				return false;
			}
			
			propertyValue = (String) getWorkflowInstance().getValue("P_GS_RIB");
			if(sfs.ifThisNumberExist(propertyValue,matricule)){
				getResourceController().alert("Pensez à changer le RIB du collaborateur !!!");
				return false;
			}
			
			
			List<ILinkedResource> enfants = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
			for(ILinkedResource enfant : enfants){
				propertyValue = (String) enfant.getValue("P_GS_CMIM_Enfant");
				if(sfs.ifThisNumberExist(propertyValue,matricule)){
					getResourceController().alert("Pensez à changer le CMIM de l'un de vos enfants !!!");
					return false;
				}
			}
			
		}
		return super.onBeforeSubmit(action);
	}
	
	@SuppressWarnings({
			"unchecked", "static-access"
	})
	@Override
	public boolean onAfterSubmit(IAction action)
	{
		// TODO Auto-generated method stub
//		if(action.getName().equals("P_GS_Valider")){
//			//Etat civil
//			String matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
//			String photo="";
//			try
//			{
//				photo = ServiceFicheSalarie.copyImageTo(getWorkflowInstance(), "P_GS_Photo", matricule);
//			}
//			catch (IOException e1)
//			{
//				e1.printStackTrace();
//			}
//			String nom = ((String) getWorkflowInstance().getValue("P_GS_Nom")).toUpperCase();
//			String prenom = ServiceFicheSalarie.capitalize((String) getWorkflowInstance().getValue("P_GS_Prenom"));
//			String nomJeuneFille = getWorkflowInstance().getValue("P_GS_NomJeuneFille")!=null?((String) getWorkflowInstance().getValue("P_GS_NomJeuneFille")).toUpperCase():"";
//			boolean sexeMasculin = ((String) getWorkflowInstance().getValue("P_GS_Sexe")).equals("Masculin") ? true : false;
//			Date dateNaissance = (Date) getWorkflowInstance().getValue("P_GS_DateNaissance");
//			String lieuNaissance = (String) getWorkflowInstance().getValue("P_GS_LieuNaiss");
//			String nationalite1 = (String) getWorkflowInstance().getValue("P_GS_ListeNationalite");
//			String nationalite2 = getWorkflowInstance().getValue("P_GS_ListeNationalite2")!=null?((String) getWorkflowInstance().getValue("P_GS_ListeNationalite2")).toUpperCase():"";
//			String situationFamiliale = (String) getWorkflowInstance().getValue("P_GS_S_LFamilliale");
//			String nomConjoint = (String) getWorkflowInstance().getValue("P_GS_Nom_Conjoint");
//			String PrenomConjoint = (String) getWorkflowInstance().getValue("P_GS_PrenomConjoint");
//			Date dateNaissanceConjoint = (Date) getWorkflowInstance().getValue("P_GS_DDN_Conjoint");
//			String rib = (String) getWorkflowInstance().getValue("P_GS_RIB");
//			String agence = (String) getWorkflowInstance().getValue("P_GS_AGENCE");
//			String adresse = (String) getWorkflowInstance().getValue("P_GS_Adrs_Perso");
//			
//			//Infos générales
//			String cin = (String) getWorkflowInstance().getValue("P_GS_CIN");
//			Date dateExpirationCIN = (Date) getWorkflowInstance().getValue("P_GS_date_expr_CIN");
//			CarteNational carteNational = new CarteNational();
//			carteNational.setCIN(cin);
//			carteNational.setDateExperation(dateExpirationCIN);
//			
//			String numPasseport = (String) getWorkflowInstance().getValue("P_GS_passePort");
//			Date dateExpirationPassport = (Date) getWorkflowInstance().getValue("P_GS_date_Exper_passeport");
//			Date dateDelivrancePasseport = (Date) getWorkflowInstance().getValue("P_GS_DateDelivrance");
//			Passeport passeport =  new Passeport();
//			passeport.setNumPassport(numPasseport);
//			passeport.setDateExperation(dateExpirationPassport);
//			passeport.setDateDelivrance(dateDelivrancePasseport);
//			
//			String nCMIM = (String) getWorkflowInstance().getValue("P_GS_CMIM");
//			String nCIMR = (String) getWorkflowInstance().getValue("P_GS_CIMR");
//			String nCNSS = (String) getWorkflowInstance().getValue("P_GS_CNSS");
//			String ncmimconjoint = (String) getWorkflowInstance().getValue("P_GS_CMIM_Conjoint");
//			String ncinconjoint = (String) getWorkflowInstance().getValue("P_GS_CINConjoint");
//			
//			Telephone telephone = new Telephone();
//			String tel1 = (String) getWorkflowInstance().getValue("P_GS_tel1");
//			telephone.setTel1(tel1);
//			String tel2 = (String) getWorkflowInstance().getValue("P_GS_Tel2");
//			telephone.setTel2(tel2);
//			String telUrgence = getWorkflowInstance().getValue("P_GS_Tel_Urgence")!=null?((String) getWorkflowInstance().getValue("P_GS_Tel_Urgence")).toUpperCase():"";
//			telephone.setTelUrgences(telUrgence);
//			String telContactUrgence = getWorkflowInstance().getValue("P_GS_Personne_urgence")!=null?((String) getWorkflowInstance().getValue("P_GS_Personne_urgence")).toUpperCase():"";
//			telephone.setTelContactUrgence(telContactUrgence);
//			
//
//			//Emploi
//			String poste = (String) getWorkflowInstance().getValue("P_GS_TPoste");
//			String descriptifPoste = (String) getWorkflowInstance().getValue("P_GS_Descriptif_poste");
//			String direction = (String) getWorkflowInstance().getValue("P_GS_Direction");
//			String filiale = getWorkflowModule().getLoggedOnUser().getOrganization().getName().toLowerCase();
//			Date dateEmbaucheGroupe = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_groupe");
//			Date dateEmbaucheFiliale = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_filiale");
//			String loginVdoc = (String) getWorkflowInstance().getValue("P_GS_VdocLogin");
//			String email = (String) getWorkflowInstance().getValue("P_GS_Mail");
//			
//			Object supObject = (Object) getWorkflowInstance().getValue("P_GS_Sup");
//			List<String> superieurs = ServiceFicheSalarie.getListFromSelecteur(supObject);
//			float droitAcquis = (float) getWorkflowInstance().getValue("P_GS_nbreConge");
//			float reliquatEncours = (float) getWorkflowInstance().getValue("P_GS_ReliquatAnneEnCours");
//			float reliquatAnterieur = (float) getWorkflowInstance().getValue("P_GS_ReliquatSoldeAnterieur");
//			
//			
//			Conjoint conjoint = new Conjoint();
//			conjoint.setCin(ncinconjoint);
//			conjoint.setDateNaissance(dateNaissanceConjoint);
//			conjoint.setNcmim(ncmimconjoint);
//			conjoint.setNom(nomConjoint);
//			conjoint.setPrenom(PrenomConjoint);
//			
//			String outilsInformatiques = getWorkflowInstance().getValue("P_GS_OutilsInformatiques")!=null?((String) getWorkflowInstance().getValue("P_GS_OutilsInformatiques")):"";
//			String logicielGestions = getWorkflowInstance().getValue("P_GS_LogicielsGestion")!=null?((String) getWorkflowInstance().getValue("P_GS_LogicielsGestion")):"";
//			String loisirs = getWorkflowInstance().getValue("P_GS_Loisirs")!=null?((String) getWorkflowInstance().getValue("P_GS_Loisirs")):"";
//			
//			List<ILinkedResource> diplomes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
//			List<ILinkedResource> experiences = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
//			List<ILinkedResource> remunerations = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("Tab_Remun");
//			List<ILinkedResource> enfants = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
//			List<ILinkedResource> langues = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Langues");
//			
//			try
//			{
//				new ServiceFicheSalarie().updatePersonnel(getWorkflowInstance(),langues, enfants, diplomes, experiences, remunerations, matricule, nom, nomJeuneFille, 
//						prenom, dateNaissance, adresse, email, nationalite1, nationalite2, dateEmbaucheGroupe, dateEmbaucheFiliale, photo, 
//						situationFamiliale, nCNSS, nCIMR, nCMIM, rib, agence, droitAcquis, reliquatEncours, reliquatAnterieur, loginVdoc, "Valider", loisirs, sexeMasculin,
//						lieuNaissance, true, ServiceFicheSalarie.getIdDirection(direction),poste, descriptifPoste, filiale, telephone, carteNational, passeport,conjoint,superieurs,outilsInformatiques,logicielGestions);
//			}
//			catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return super.onAfterSubmit(action);
	}
	
	
	public void generateCV(IWorkflowInstance iw, IWorkflowModule im, IResourceController ir, ILinkedResource ilr)
	{
		try
		{
			connection = SingletonConnexionBDD.getSqlSession().getConnectionVDoc(im,Modules.getPortalModule()).getConnection();
			String matricule = (String) iw.getValue("P_GS_MatriculeUpdate");
			Map<String, Object> parameters = new HashMap<String, Object>();
			String nom = ((String) iw.getValue("P_GS_Nom")).toUpperCase();
			String prenom = ServiceFicheSalarie.capitalize((String) iw.getValue("P_GS_Prenom"));
			String nomFichierPDF = "cv_"+nom+"_"+prenom;
			FileManager path = new FileManager();
			parameters.put("matricule", matricule);
			new GenererPDF().generer("CV", path.getOutDir(), nomFichierPDF, parameters, connection);
			iw.setValue("P_GS_Cv", null);
			im.addAttachment(iw, "P_GS_Cv", nomFichierPDF+".pdf", "C:\\VDocPlatform\\JBoss\\server\\all\\deploy\\vdoc.ear\\vdoc.war\\RH\\OUT\\" + nomFichierPDF+".pdf");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
