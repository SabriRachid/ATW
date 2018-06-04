package com.attijari.GestionSalarie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.axemble.vdoc.sdk.exceptions.WorkflowModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IAttachment;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdp.ui.framework.widgets.CtlImage;
import com.beans.attijariRh.CarteNational;
import com.beans.attijariRh.Conjoint;
import com.beans.attijariRh.Passeport;
import com.beans.attijariRh.Telephone;
import com.serviceRH.ServiceFicheSalarie;
import com.serviceRH.ServiceRH;

public class AddSalarie extends ConnexionBDD
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean onAfterLoad()
	{
		// TODO Auto-generated method stub
		IUser connectedUser = getWorkflowModule().getLoggedOnUser();
		String filiale = connectedUser.getOrganization().getLabel();
		getWorkflowInstance().setValue("P_GS_Filiale", filiale);
		//getWorkflowInstance().setValue("P_GS_PhotoImg", "skins//ImageFicheSalarie//anonyme.png");
		
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
			String matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
			try
			{
				List<IAttachment> photos = (List<IAttachment>) getWorkflowInstance().getValue("P_GS_Photo");
				if(photos.size()!=0){
					if(matricule!=null){
						String image =  sfs.copyImageTo(getWorkflowInstance(), "P_GS_Photo", matricule);
						getWorkflowInstance().setValue("P_GS_PhotoImg", "skins//ImageFicheSalarie//"+image);
					}
					else{
						getResourceController().alert("Veuillez saisir le matricule premièrement !!!");
					}
					
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
				
		if(property.getName().equals("P_GS_RIB")||property.getName().equals("P_GS_CIN")
				||property.getName().equals("P_GS_passePort")||property.getName().equals("P_GS_CMIM")||property.getName().equals("P_GS_CIMR")
				||property.getName().equals("P_GS_CNSS")||property.getName().equals("P_GS_CMIM_Conjoint")||property.getName().equals("P_GS_CINConjoint")){
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			String number = (String) getWorkflowInstance().getValue(property.getName());
			String matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
			if(sfs.ifThisNumberExist(number,matricule)){
				getResourceController().alert(property.getLabel()+" existe déjà ...");
				getWorkflowInstance().setValue("P_GS_PhotoImg", "skins//ImageFicheSalarie//anonyme.png");
				getWorkflowInstance().setValue("P_GS_Photo", null);
			}
		}
		else if(property.getName().equals("P_GS_Matricule")){
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			String matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
			if(sfs.ifThisNumberExist(matricule)){
				getResourceController().alert(property.getLabel()+" existe déjà ...");
			}
		}
		else if(property.getName().equals("P_GS_Nom")||property.getName().equals("P_GS_Prenom")){
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			String nom = (String) getWorkflowInstance().getValue("P_GS_Nom");
			String prenom = (String) getWorkflowInstance().getValue("P_GS_Prenom");
			String loginVdoc = ServiceFicheSalarie.getLoginVdoc(nom, prenom);
			String email = loginVdoc+"@attijari.ma";
			getWorkflowInstance().setValue("P_GS_VdocLogin", loginVdoc.toLowerCase());
			getWorkflowInstance().setValue("P_GS_Mail", email.toLowerCase());
		}
		
//		else if(property.getName().equals("P_GS_Sup")){
//			List<String> sups = (List<String>) getWorkflowInstance().getValue("P_GS_Sup");
//			String listsups = "";
//			for(String sup : sups){
//				try
//				{
//					IUser user = getWorkflowModule().getUserByLogin(sfs.getLoginOfUserByHisMatricule(sup));
//					listsups += user.getFullName()+"\n";
//				}
//				catch (WorkflowModuleException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			getWorkflowInstance().setValue("P_GS_SupNames", listsups);
//		}
		super.onPropertyChanged(property);
	}
	
	@SuppressWarnings({
			"static-access", "unchecked"
	})
	@Override
	public boolean onBeforeSubmit(IAction action)
	{
		// TODO Auto-generated method stub
		if(action.getName().equals("Valider")){
			
			ServiceFicheSalarie sfs = new ServiceFicheSalarie();
			
			
			
			String propertyValue = (String) getWorkflowInstance().getValue("P_GS_Matricule");
			String matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
			
			if(sfs.ifThisNumberExist(matricule)){
				getResourceController().alert("Pensez à changer le matricule du collaborateur !!!");
				return false;
			}
			
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
				getResourceController().alert("Pensez à changer le CIN du collaborateur !!!");
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
		if(action.getName().equals("Valider")){
			//Etat civil
			String matricule = (String) getWorkflowInstance().getValue("P_GS_Matricule");
			String photo="";
			try
			{
				photo = ServiceFicheSalarie.copyImageTo(getWorkflowInstance(), "P_GS_Photo", matricule);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String nom = ((String) getWorkflowInstance().getValue("P_GS_Nom")).toUpperCase();
			String prenom = ServiceFicheSalarie.capitalize((String) getWorkflowInstance().getValue("P_GS_Prenom"));
			String nomJeuneFille = getWorkflowInstance().getValue("P_GS_NomJeuneFille")!=null?((String) getWorkflowInstance().getValue("P_GS_NomJeuneFille")).toUpperCase():"";
			boolean sexeMasculin = ((String) getWorkflowInstance().getValue("P_GS_Sexe")).equals("Masculin") ? true : false;
			Date dateNaissance = (Date) getWorkflowInstance().getValue("P_GS_DateNaissance");
			String lieuNaissance = (String) getWorkflowInstance().getValue("P_GS_LieuNaiss");
			String nationalite1 = (String) getWorkflowInstance().getValue("P_GS_ListeNationalite");
			String nationalite2 = getWorkflowInstance().getValue("P_GS_ListeNationalite2")!=null?((String) getWorkflowInstance().getValue("P_GS_ListeNationalite2")):null;
			String situationFamiliale = (String) getWorkflowInstance().getValue("P_GS_S_LFamilliale");
			String nomConjoint = (String) getWorkflowInstance().getValue("P_GS_Nom_Conjoint");
			String PrenomConjoint = (String) getWorkflowInstance().getValue("P_GS_PrenomConjoint");
			Date dateNaissanceConjoint = (Date) getWorkflowInstance().getValue("P_GS_DDN_Conjoint");
			String rib = (String) getWorkflowInstance().getValue("P_GS_RIB");
			String agence = (String) getWorkflowInstance().getValue("P_GS_AGENCE");
			String adresse = (String) getWorkflowInstance().getValue("P_GS_Adrs_Perso");
			
			//Infos générales
			String cin = (String) getWorkflowInstance().getValue("P_GS_CIN");
			Date dateExpirationCIN = (Date) getWorkflowInstance().getValue("P_GS_date_expr_CIN");
			CarteNational carteNational = new CarteNational();
			carteNational.setCIN(cin);
			carteNational.setDateExperation(dateExpirationCIN);
			
			String numPasseport = (String) getWorkflowInstance().getValue("P_GS_passePort");
			Date dateExpirationPassport = (Date) getWorkflowInstance().getValue("P_GS_date_Exper_passeport");
			Date dateDelivrancePasseport = (Date) getWorkflowInstance().getValue("P_GS_DateDelivrance");
			Passeport passeport =  new Passeport();
			passeport.setNumPassport(numPasseport);
			passeport.setDateExperation(dateExpirationPassport);
			passeport.setDateDelivrance(dateDelivrancePasseport);
			
			String nCMIM = (String) getWorkflowInstance().getValue("P_GS_CMIM");
			String nCIMR = (String) getWorkflowInstance().getValue("P_GS_CIMR");
			String nCNSS = (String) getWorkflowInstance().getValue("P_GS_CNSS");
			String ncmimconjoint = (String) getWorkflowInstance().getValue("P_GS_CMIM_Conjoint");
			String ncinconjoint = (String) getWorkflowInstance().getValue("P_GS_CINConjoint");
			
			Telephone telephone = new Telephone();
			String tel1 = (String) getWorkflowInstance().getValue("P_GS_tel1");
			telephone.setTel1(tel1);
			String tel2 = (String) getWorkflowInstance().getValue("P_GS_Tel2");
			telephone.setTel2(tel2);
			String telUrgence = getWorkflowInstance().getValue("P_GS_Tel_Urgence")!=null?((String) getWorkflowInstance().getValue("P_GS_Tel_Urgence")).toUpperCase():"";
			telephone.setTelUrgences(telUrgence);
			String telContactUrgence = getWorkflowInstance().getValue("P_GS_Personne_urgence")!=null?((String) getWorkflowInstance().getValue("P_GS_Personne_urgence")).toUpperCase():"";
			telephone.setTelContactUrgence(telContactUrgence);
			

			//Emploi
			String poste = (String) getWorkflowInstance().getValue("P_GS_TPoste");
			String descriptifPoste = (String) getWorkflowInstance().getValue("P_GS_Descriptif_poste");
			String direction = (String) getWorkflowInstance().getValue("P_GS_Direction");
			String filiale = getWorkflowModule().getLoggedOnUser().getOrganization().getName().toLowerCase();
			Date dateEmbaucheGroupe = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_groupe");
			Date dateEmbaucheFiliale = (Date) getWorkflowInstance().getValue("P_GS_D_embuche_filiale");
			String loginVdoc = (String) getWorkflowInstance().getValue("P_GS_VdocLogin");
			String email = (String) getWorkflowInstance().getValue("P_GS_Mail");
			
			List<IUser> superieursList = (List<IUser>) getWorkflowInstance().getValue("P_GS_SupList");
			List<String> superieurs = new ArrayList<>();
			for(IUser user : superieursList){
				superieurs.add(user.getEmployeeNumber());
			}
			float droitAcquis = (float) getWorkflowInstance().getValue("P_GS_nbreConge");
			
			
			Conjoint conjoint = new Conjoint();
			conjoint.setCin(ncinconjoint);
			conjoint.setDateNaissance(dateNaissanceConjoint);
			conjoint.setNcmim(ncmimconjoint);
			conjoint.setNom(nomConjoint);
			conjoint.setPrenom(PrenomConjoint);
			
			List<ILinkedResource> diplomes = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_diplome");
			List<ILinkedResource> experiences = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Experience");
			List<ILinkedResource> remunerations = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("Tab_Remun");
			List<ILinkedResource> enfants = (List<ILinkedResource>) getWorkflowInstance().getLinkedResources("P_GS_TAB_Enfants");
			
			try
			{
				new ServiceFicheSalarie().addPersonnel(getWorkflowInstance(), enfants, diplomes, experiences, remunerations, matricule, nom, nomJeuneFille, 
						prenom, dateNaissance, adresse, email, nationalite1, nationalite2, dateEmbaucheGroupe, dateEmbaucheFiliale, photo, 
						situationFamiliale, nCNSS, nCIMR, nCMIM, rib, agence, droitAcquis, 0f, droitAcquis, loginVdoc, "Valider", null, sexeMasculin,
						lieuNaissance, true, ServiceFicheSalarie.getIdDirection(direction),poste, descriptifPoste, filiale, telephone, carteNational, passeport,conjoint,superieurs);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onAfterSubmit(action);
	}
}
